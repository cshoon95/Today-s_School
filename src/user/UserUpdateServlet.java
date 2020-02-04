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
		String userAge = request.getParameter("userAge"); //String���� �ϴ� ���� : ����ڷκ��� �Է¹޴� ���� ���ڿ� �����̱� ����
		String userEmail = request.getParameter("userEmail");
		
		if(userID == null || userID.equals("") || userPassword1 == null || userPassword1.equals("") || userPassword2 == null || userPassword2.equals("") 
				|| userNickname == null || userNickname.equals("") || userAge == null || userAge.equals("") || userGender == null || userGender.equals("") 
				) {
			request.getSession().setAttribute ("messageType", "���� �޽���");
			request.getSession().setAttribute ("messageContent", "�Է��� �� �� ������ �ֽ��ϴ�.");
			response.sendRedirect("update.jsp");
			return;
		}
		if(!userID.equals((String) session.getAttribute("userID"))) {
			session.setAttribute("messageType", "���� �޽���");
			session.setAttribute("messageContent", "�α����� �ϼž� ���� �����մϴ�.");
			response.sendRedirect("login.jsp");
			return;
		}
		if(!userPassword1.equals(userPassword2)) { // ��й�ȣ�� ���� �ٸ� ���
			request.getSession().setAttribute ("messageType", "���� �޽���");
			request.getSession().setAttribute ("messageContent", "��й�ȣ�� ���� �ٸ��ϴ�.");
			response.sendRedirect("update.jsp");			
			return;
		}
		int result = new UserDAO().update(userID, userPassword1, userNickname, userAge, userGender,
				 userEmail);
		if(result == 1) {		
			request.getSession().setAttribute("userID", userID);
			request.getSession().setAttribute ("messageType", "���� �޽���");
			request.getSession().setAttribute ("messageContent", "ȸ������ ������ �Ϸ�Ǿ����ϴ�.");
			response.sendRedirect("index.jsp"); 
			 //request.getSession().setAttribute("userID", userID); // �ڵ����� �α����� �����ָ鼭 ���ǰ���
			return;			
		} else {
			request.getSession().setAttribute ("messageType", "���� �޽���");
			request.getSession().setAttribute ("messageContent", "DB���� �߻�");
			response.sendRedirect("update.jsp");
			return;				
		}
	}
	
}
