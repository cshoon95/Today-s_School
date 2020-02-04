package user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/UserRegisterServlet")
public class UserRegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		String userID = request.getParameter("userID");
		String userPassword1 = request.getParameter("userPassword1");
		String userPassword2 = request.getParameter("userPassword2");
		String userNickname = request.getParameter("userNickname");
		String userGender = request.getParameter("userGender");
		String userAge = request.getParameter("userAge"); //String���� �ϴ� ���� : ����ڷκ��� �Է¹޴� ���� ���ڿ� �����̱� ����
		String userEmail = request.getParameter("userEmail");
		String userProfile = request.getParameter("userProfile");
		
		if(userID == null || userID.equals("") || userPassword1 == null || userPassword1.equals("") || userPassword2 == null || userPassword2.equals("") 
				|| userNickname == null || userNickname.equals("") || userAge == null || userAge.equals("") || userGender == null || userGender.equals("") 
				|| userEmail == null || userEmail.equals("")) {
			request.getSession().setAttribute ("messageType", "���� �޽���");
			request.getSession().setAttribute ("messageContent", "�Է��� �� �� ������ �ֽ��ϴ�.");
			response.sendRedirect("join.jsp");
			return;
		}
		if(!userPassword1.equals(userPassword2)) { // ��й�ȣ�� ���� �ٸ� ���
			request.getSession().setAttribute ("messageType", "���� �޽���");
			request.getSession().setAttribute ("messageContent", "��й�ȣ�� ���� �ٸ��ϴ�.");
			response.sendRedirect("join.jsp");			
			return;
		}
		int result = new UserDAO().register(userID, userPassword1, userNickname, userAge, userGender,
				 userEmail, util.SHA256.getSHA256(userEmail), false, "");
		if(result == 1) {		
			request.getSession().setAttribute("userID", userID);
			request.getSession().setAttribute ("messageType", "���� �޽���");
			request.getSession().setAttribute ("messageContent", "ȸ�������� �Ϸ�Ǿ����ϴ�.");
			response.sendRedirect("emailSendAction.jsp"); 
			 //request.getSession().setAttribute("userID", userID); // �ڵ����� �α����� �����ָ鼭 ���ǰ���
			return;			
		} else {
			request.getSession().setAttribute ("messageType", "���� �޽���");
			request.getSession().setAttribute ("messageContent", "�̹� �����ϴ� ȸ���Դϴ�.");
			response.sendRedirect("join.jsp");
			return;				
		}
	}
	
}
