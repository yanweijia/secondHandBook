package secondHandBook.dao;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import secondHandBook.db.DBHelper;


@WebServlet("/deleteBook")
public class deleteBook extends HttpServlet {
	private static final long serialVersionUID = -4320946407316458461L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("contentType", "text/html;charset=UTF-8");
		
		String book_id = request.getParameter("book_id");
		String password = request.getParameter("password");
		if(book_id == null || password == null){
			JSONObject jsonResult = new JSONObject();
			jsonResult.put("status", "success");
			jsonResult.put("reason", "请将参数传递完整!");
			response.getWriter().append(jsonResult.toString());
			return;
		}
		
		DBHelper dbHelper = new DBHelper(request);
		response.getWriter().append(dbHelper.deleteBook(book_id, password).toString());
		dbHelper.close();		//关闭数据库连接
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
