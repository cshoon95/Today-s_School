package user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/UserUpdateServlet")
public class UserUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		String userID = request.getParameter("userID");
		HttpSession session = request.getSession();
		String userPassword1 = request.getParameter("userPassword1");
		String userPassword2 = request.getParameter("userPassword2");
		String userNickname = request.getParameter("userNickname");
		String userGender = request.getParameter("userGender");
		String userAge = request.getParameter("userAge"); //String으로 하는 이유 : 사용자로부터 입력받는 것은 문자열 형태이기 때문
		String userEmail = request.getParameter("userEmail");
		
		if(userID == null || userID.equals("") || userPassword1 == null || userPassword1.equals("") || userPassword2 == null || userPassword2.equals("") 
				|| userNickname == null || userNickname.equals("") || userAge == null || userAge.equals("") || userGender == null || userGender.equals("") 
				) {
			request.getSession().setAttribute ("messageType", "오류 메시지");
			request.getSession().setAttribute ("messageContent", "입력이 안 된 사항이 있습니다.");
			response.sendRedirect("update.jsp");
			return;
		}
		if(!userID.equals((String) session.getAttribute("userID"))) {
			session.setAttribute("messageType", "오류 메시지");
			session.setAttribute("messageContent", "로그인을 하셔야 접근 가능합니다.");
			response.sendRedirect("login.jsp");
			return;
		}
		if(!userPassword1.equals(userPassword2)) { // 비밀번호가 서로 다른 경우
			request.getSession().setAttribute ("messageType", "오류 메시지");
			request.getSession().setAttribute ("messageContent", "비밀번호가 서로 다릅니다.");
			response.sendRedirect("update.jsp");			
			return;
		}
		int result = new UserDAO().update(userID, userPassword1, userNickname, userAge, userGender,
				 userEmail);
		if(result == 1) {		
			request.getSession().setAttribute("userID", userID);
			request.getSession().setAttribute ("messageType", "성공 메시지");
			request.getSession().setAttribute ("messageContent", "회원정보 수정이 완료되었습니다.");
			response.sendRedirect("index.jsp"); 
			 //request.getSession().setAttribute("userID", userID); // 자동으로 로그인을 시켜주면서 세션값줌
			return;			
		} else {
			request.getSession().setAttribute ("messageType", "오류 메시지");
			request.getSession().setAttribute ("messageContent", "DB오류 발생");
			response.sendRedirect("update.jsp");
			return;				
		}
	}
	
}
