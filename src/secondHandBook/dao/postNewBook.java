package secondHandBook.dao;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import secondHandBook.db.DBHelper;


@WebServlet("/postNewBook")
public class postNewBook extends HttpServlet {
	private static final long serialVersionUID = -7648043404728239397L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("contentType", "text/html;charset=UTF-8");
		JSONObject jsonResult = new JSONObject();
		String book_name = request.getParameter("book_name");
		String book_price = request.getParameter("book_price");
		String book_intro = request.getParameter("book_intro");
		String book_isbn = request.getParameter("book_isbn");
		String contact_name = request.getParameter("contact_name");
		String password = request.getParameter("password");
		String contact_phone = request.getParameter("contact_phone");
		String book_img = request.getParameter("book_img");
		String book_img_thumbnail = request.getParameter("book_img_thumbnail");
		//检查参数完整性
		if(book_name==null||book_price==null||book_intro==null||book_isbn==null||contact_name==null||password==null||contact_phone==null||book_img==null||book_img_thumbnail==null){
			jsonResult.put("status", "fail");
			jsonResult.put("reason", "参数传递不完整");
			response.getWriter().append(jsonResult.toString());
			return;
		}
		DBHelper dbHelper = new DBHelper(request);
		jsonResult = dbHelper.postNewBook(book_name, book_price, book_intro, book_isbn, contact_name, password, contact_phone, book_img, book_img_thumbnail);
		response.getWriter().append(jsonResult.toString());
		dbHelper.close();		//关闭数据库连接
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
