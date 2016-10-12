package secondHandBook.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DBHelper {
	private String driver = "com.mysql.jdbc.Driver"; // 数据库驱动
	// 连接数据库的URL地址
	private String url = "jdbc:mysql://localhost:3306/secondHandBook?useUnicode=true&characterEncoding=UTF-8";
	private String username = "root";// 数据库用户名
	private String password = "";// 数据库的密码
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	
	
	
	
	/**
	 * 根据价格区间查询书籍
	 * @param book_name 书籍名称
	 * @param low 最低价格 如果为空,默认为0
	 * @param high 价格封顶 如果为空,默认为int的最大值
	 * @param singlePageNum 单页条数,如果为空,则默认为10条
	 * @param page 页数,第几页,如果为空,则默认为第一页
	 * @param sortWay 排序方式  如果为空则默认true,升序:true 降序:false
	 * @return 查询后的结果,json封装
	 */
	public JSONObject getBookBy(String book_name,Integer low,Integer high,Integer singlePageNum,Integer page,Boolean sortWay){
		if(low==null)
			low = 0;
		if(high==null)
			high=Integer.MAX_VALUE;
		if(singlePageNum==null)
			singlePageNum = 10;
		if(page==null)
			page=1;
		if(sortWay==null)
			sortWay=true;
		if(book_name==null || book_name.equals("null"))
			book_name="";
		String sql = "SELECT book_id,book_name,book_price,viewed,postdate,book_img_thumbnail FROM books WHERE status=1 AND book_name LIKE '%" + book_name +  "%' AND book_price>="+low+" AND book_price<=" + high+" ORDER BY book_price "+(sortWay?"ASC":"DESC") + " LIMIT "+((page-1)*singlePageNum) + "," + (page*singlePageNum);
		System.out.println(sql);
		JSONObject jsonResult = new JSONObject();
		try{
			getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			jsonResult.put("status", "success");
			jsonResult.put("reason", null);
			JSONArray jsonArrayBook = new JSONArray();
			int count = 0;	//计数器,总共多少个
			while(rs.next()){
				JSONObject jsonBook = new JSONObject();
				jsonBook.put("book_id", rs.getInt("book_id"));
				jsonBook.put("book_name", rs.getString("book_name"));
				jsonBook.put("book_price", rs.getString("book_price"));
				jsonBook.put("viewed", rs.getInt("viewed"));
				jsonBook.put("postdate", rs.getString("postdate"));
				jsonBook.put("book_img_thumbnail", rs.getString("book_img_thumbnail"));
				jsonArrayBook.add(jsonBook);
				count++;
			}
			jsonResult.put("count", count);
			jsonResult.put("books", jsonArrayBook);
			return jsonResult;
			
		}catch (Exception e) {
			e.printStackTrace();
			jsonResult.put("status", "fail");
			jsonResult.put("reason", "服务器异常:" + e.getMessage());
			return jsonResult;
		}
	}
	
	
	
	/**
	 * 发布一本新二手书
	 * @param book_name 书名
	 * @param book_price 书价格
	 * @param book_intro 书介绍
	 * @param book_isbn 书的ISBN
	 * @param contact_name 联系人
	 * @param password 信息管理密码
	 * @param contact_phone 联系方式
	 * @param book_img 介绍图片的Base64编码文字
	 * @param book_img_thumbnail 缩略图的Base64编码文字
	 * @return 添加结果的json表示
	 */
	public JSONObject postNewBook(String book_name,
									String book_price,
									String book_intro,
									String book_isbn,
									String contact_name,
									String password,
									String contact_phone,
									String book_img,
									String book_img_thumbnail){
		String postdate = Tools.getNowTime();
		String sql = "INSERT INTO books(book_name,book_price,book_intro,book_isbn,contact_name,password,contact_phone,postdate,book_img,book_img_thumbnail)VALUES("
				+ "'" + book_name+"'," + book_price + ",'" + book_intro+"','"+book_isbn+"','"+contact_name+"','"+password+"','"+contact_phone+"','"+ postdate + "','" +book_img+"','"+book_img_thumbnail+"')";
		JSONObject jsonResult = new JSONObject();
		try{
			getConnection();
			stmt = conn.createStatement();
			int count = stmt.executeUpdate(sql);
			if(count>=1){
				jsonResult.put("status", "success");
				jsonResult.put("reason", null);
				return jsonResult;
			}else{
				jsonResult.put("status", "fail");
				jsonResult.put("reason", "这是一个我也不知道是什么的错误,错误编号:XXXXX,应该是插入了数据但是受影响的行数小于1");
				return jsonResult;
			}
		}catch(Exception e){
			e.printStackTrace();
			jsonResult.put("status", "fail");
			jsonResult.put("reason", "数据库异常" + e.getMessage());
			return jsonResult;
		}
	}
	
	
	/**
	 * 删除指定书籍
	 * @param book_id 图书编号
	 * @param password 发布人设定的密码
	 * @return 删除结果的JSON表示
	 */
	public JSONObject deleteBook(String book_id,String password){
		String sql = "SELECT book_id FROM books WHERE status=1 AND book_id=" + book_id;
		JSONObject jsonResult = new JSONObject();
		try{
			getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);	//先判断数据库中有没有这本书
			if(rs.next()){
				sql = "UPDATE books SET status=0 WHERE book_id=" + book_id + " AND password='" + password + "'";
				int count = stmt.executeUpdate(sql);
				if(count>=1){	//受影响的行数>=1,则删除成功
					jsonResult.put("status", "success");
					jsonResult.put("reason", null);
					return jsonResult;
				}else{
					jsonResult.put("status", "fail");
					jsonResult.put("reason", "密码错误,删除失败");
					return jsonResult;
				}
			}else{//如果数据库中没有对应编号的书,进行提示
				jsonResult.put("status", "fail");
				jsonResult.put("reason", "数据库中没有编号为" + book_id + "的书籍");
				return jsonResult;
			}
		}catch(Exception e){
			e.printStackTrace();
			jsonResult.put("status","fail");
			jsonResult.put("reason","数据库异常:" + e.getMessage());
			return jsonResult;
		}
	}
	
	
	
	
	/**
	 * 获取指定编号书籍的详细信息
	 * @param book_id 书籍编号
	 * @return 详细信息的json封装
	 */
	public JSONObject getDetailInfo(String book_id){
		String sql = "SELECT book_id,book_name,book_price,book_intro,book_isbn,contact_name,contact_phone,viewed,postdate,book_img FROM books WHERE status=1 AND book_id=" + book_id;
		JSONObject jsonResult = new JSONObject();
		JSONObject jsonBook = new JSONObject();
		try{
			getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if(rs.next()){
				jsonResult.put("status", "success");
				jsonResult.put("reason", null);
				jsonBook.put("book_id", book_id);
				jsonBook.put("book_name", rs.getString("book_name"));
				jsonBook.put("book_price", rs.getDouble("book_price"));
				jsonBook.put("book_intro", rs.getString("book_intro"));
				jsonBook.put("book_isbn", rs.getString("book_isbn"));
				jsonBook.put("contact_name",rs.getString("contact_name"));
				jsonBook.put("contact_phone", rs.getString("contact_phone"));
				jsonBook.put("viewed", rs.getInt("viewed"));
				jsonBook.put("postdate", rs.getString("postdate"));
				jsonBook.put("book_img", rs.getString("book_img"));
				jsonResult.put("book", jsonBook);
				//将被浏览数+1
				sql = "UPDATE books SET viewed=viewed+1 WHERE book_id=" + book_id;
				stmt.executeUpdate(sql);
			}
			else{
				jsonResult.put("status", "fail");
				jsonResult.put("reason", "抱歉,数据库中没有指定书籍");
			}
			return jsonResult;
		}catch(Exception e){
			e.printStackTrace();
			jsonResult.clear();	//先清空,防止之前已经有放的信息了.
			jsonResult.put("status", "fail");
			jsonResult.put("reason", "数据库异常" + e.getMessage());
			return jsonResult;
		}
	}
	/**
	 * 获取所有书籍,最多100本,按时间顺序排列
	 * @return 书籍简介的json封装
	 */
	public JSONObject getAllBooks(){
		String sql = "SELECT book_id,book_name,book_price,viewed,postdate,book_img_thumbnail FROM books WHERE status=1 ORDER BY postdate DESC LIMIT 0,100";
		JSONObject jsonResult = new JSONObject();
		try{
			getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			jsonResult.put("status", "success");
			jsonResult.put("reason", null);
			JSONArray jsonArrayBook = new JSONArray();
			int count = 0;	//计数器,总共多少个
			while(rs.next()){
				JSONObject jsonBook = new JSONObject();
				jsonBook.put("book_id", rs.getInt("book_id"));
				jsonBook.put("book_name", rs.getString("book_name"));
				jsonBook.put("book_price", rs.getString("book_price"));
				jsonBook.put("viewed", rs.getInt("viewed"));
				jsonBook.put("postdate", rs.getString("postdate"));
				jsonBook.put("book_img_thumbnail", rs.getString("book_img_thumbnail"));
				jsonArrayBook.add(jsonBook);
				count++;
			}
			jsonResult.put("count", count);
			jsonResult.put("books", jsonArrayBook);
			return jsonResult;
			
		}catch (Exception e) {
			e.printStackTrace();
			jsonResult.put("status", "fail");
			jsonResult.put("reason", "服务器异常:" + e.getMessage());
			return jsonResult;
		}
	}
	
	
	
	
	
	
	

	/**
	 * 构造方法
	 * @param request
	 *            请求
	 */
	public DBHelper(HttpServletRequest request) {
		this(request.getSession().getServletContext().getRealPath("/")
				+ "config.properties");
	}

	/**
	 * 构造方法
	 * @param path
	 *            配置文件的路径
	 */
	public DBHelper(String path) {
		try {
			driver = getResourceByKey(path, "mysql_driver");
			url = getResourceByKey(path, "mysql_url");
			username = getResourceByKey(path, "mysql_username");
			password = getResourceByKey(path, "mysql_password");
//			System.out.println(driver);
//			System.out.println(url);
//			System.out.println(username);
//			System.out.println(password);
			Class.forName(driver);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// 单例模式返回数据库连接对象
	public Connection getConnection() throws Exception {
		if (conn == null) {
			conn = DriverManager.getConnection(url, username, password);
			return conn;
		}
		return conn;
	}

	/**
	 * 获取配置信息
	 * 
	 * @param key
	 *            键值
	 * @return 返回指定参数的值
	 */
	public static String getResourceByKey(String path, String key) {
		Properties properties = new Properties();
		String value = null;
		File file = new File(path);
//		System.out.println(file.getPath());
		try {
			if (!file.exists()) // 文件不存在则创建
				file.createNewFile();
			FileInputStream in = new FileInputStream(file);
			properties.load(in);
			value = properties.getProperty(key);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return value;
	}
	/**
	 * 关闭数据库
	 */
	public void close(){
		try{
			if(rs!=null)
				rs.close();
			if(stmt!=null)
				stmt.close();
			if(conn!=null)
				conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
