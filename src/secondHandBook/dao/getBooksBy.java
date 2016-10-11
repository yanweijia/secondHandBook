package secondHandBook.dao;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import secondHandBook.db.DBHelper;


@WebServlet("/getBooksBy")
public class getBooksBy extends HttpServlet {
	private static final long serialVersionUID = -4385597224138999842L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("contentType", "text/html;charset=UTF-8");
		
		String book_name = request.getParameter("book_name");
		String strLow = request.getParameter("low");
		String strHigh = request.getParameter("high");
		String strSinglePageNum = request.getParameter("singlePageNum");
		String strPage = request.getParameter("page");
		String strSortWay = request.getParameter("sortWay");
		Integer low,high,singlePageNum,page;
		Boolean sortWay;
		
		//解析最低价格
		if(strLow==null)
			low = null;
		else
			low = Integer.parseInt(strLow);
		//解析最高价格
		if(strHigh==null)
			high=null;
		else
			high = Integer.parseInt(strHigh);
		//解析单页数目
		if(strSinglePageNum==null)
			singlePageNum=null;
		else
			singlePageNum = Integer.parseInt(strSinglePageNum);
		//解析当前页数
		if(strPage==null)
			page = null;
		else
			page = Integer.parseInt(strPage);
		//解析排序方式
		if(strSortWay==null)
			sortWay = null;
		else
			sortWay = Boolean.parseBoolean(strSortWay);
		
		
		DBHelper dbHelper = new DBHelper(request);
		response.getWriter().append(dbHelper.getBookBy(book_name, low, high, singlePageNum, page, sortWay).toString());
		dbHelper.close();		//关闭数据库连接
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
