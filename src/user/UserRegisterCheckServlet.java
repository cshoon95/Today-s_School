//아이디 중복체크 결과 반환시켜주는 서블릿
package user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/UserRegisterCheckServlet")
public class UserRegisterCheckServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	// post방식으로 클라이언트에게 매개변수를 받았을 때 처리를 하는 것.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String userID = request.getParameter("userID");
		if(userID == null || userID.equals("")) response.getWriter().write("-1");
		response.getWriter().write(new UserDAO().registerCheck(userID) + ""); // 공백("")을 넣는 이유 : 문자열로 출력하기 위해
	}

}
