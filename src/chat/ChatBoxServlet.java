package chat;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import user.UserDAO;

@WebServlet("/ChatBoxServlet")
public class ChatBoxServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String userID = request.getParameter("userID");
		if(userID == null || userID.equals("")) {
			response.getWriter().write(" "); //���̵� ���̰ų� �����Ͻ� �����߻� : 0�� ��ȯ
		} else {
			try {		
				HttpSession session = request.getSession();
				if(!URLDecoder.decode(userID, "UTF-8").equals((String) session.getAttribute("userID"))) {
					response.getWriter().write("");
					return;
				}
				userID = URLDecoder.decode(userID, "UTF-8");
				response.getWriter().write(getBox(userID));
			} catch (Exception e) {
				response.getWriter().write(new ChatDAO().getAllUnreadChat(userID) + ""); // ""������ �� ���� : ���ڿ��� ��ȯ�ϱ� ����
			}
		}
	}
	
	public String getBox(String userID) {
		StringBuffer result = new StringBuffer(""); // ���鹮�ڷ� �����ϴ� �ν��Ͻ� ��ȯ
		result.append("{\"result\":["); // JSON �迭���� �͵��� ǥ���ϰ� ����.
		ChatDAO chatDAO = new ChatDAO();
		ArrayList<ChatDTO> chatList = chatDAO.getBox(userID);
		if(chatList.size()== 0) return ""; //list�� �������� ������ ���� ��ȯ
		for(int i=chatList.size() -1; i>=0; i--) {
			String unread = "";
			String userProfile = "";
			if(userID.equals(chatList.get(i).getToID())) {
				unread = chatDAO.getUnreadChat(chatList.get(i).getFromID(), userID) + ""; // ������ �޽��� ��
				if(unread.equals("0")) unread = "";
			}
			if(userID.equals(chatList.get(i).getToID())) {
				userProfile = new UserDAO().getProfile(chatList.get(i).getFromID());
			} else {
				userProfile = new UserDAO().getProfile(chatList.get(i).getToID());
			}
			result.append("[{\"value\": \"" + chatList.get(i).getFromID() + "\"},"); // ���ȣ.
			result.append("{\"value\": \"" + chatList.get(i).getToID() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatContent() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatTime() + "\"},");
			result.append("{\"value\": \"" + unread + "\"},"); 
			result.append("{\"value\": \"" + userProfile + "\"}]"); 	// ������ �����̱� ������ �ݾ������ ]
			if(i != 0) result.append(","); // ������ ���Ұ� �ƴϰ� �� �ִٸ� ','�� �� �˷���.
		}
		result.append("], \"last\":\"" + chatList.get(chatList.size() - 1).getChatID() + "\"}");
		return result.toString(); //json�� ���¸� ���ڿ��� ��ȯ
	}	
	
}