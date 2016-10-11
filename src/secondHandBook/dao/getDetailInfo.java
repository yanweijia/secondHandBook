package secondHandBook.dao;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import secondHandBook.db.DBHelper;


@WebServlet("/getDetailInfo")
public class getDetailInfo extends HttpServlet {
	private static final long serialVersionUID = -3817596599143071529L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("contentType", "text/html;charset=UTF-8");
		
		String book_id = request.getParameter("book_id");
		JSONObject jsonResult = new JSONObject();
		//如果参数有误,进行提示
		if(book_id == null){
			jsonResult.put("status", "fail");
			jsonResult.put("reason", "参数传递不正确,请检查是否传入了参数 book_id");
			response.getWriter().append(jsonResult.toString());
			return;
		}
		//从数据库中读取数据
		DBHelper dbHelper = new DBHelper(request);
		response.getWriter().append(dbHelper.getDetailInfo(book_id).toString());
		dbHelper.close();		//关闭数据库连接
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
