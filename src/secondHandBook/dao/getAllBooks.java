package secondHandBook.dao;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import secondHandBook.db.DBHelper;


@WebServlet("/getAllBooks")
public class getAllBooks extends HttpServlet {

	private static final long serialVersionUID = -4577029222321503124L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("contentType", "text/html;charset=UTF-8");
		DBHelper dbHelper = new DBHelper(request);
		response.getWriter().append(dbHelper.getAllBooks().toString());
		dbHelper.close();		//关闭数据库连接
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
