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

// ajax��� (�񵿱��� ���)���� �ǽð����� ���.
@WebServlet("/ChatListServlet")
public class ChatListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String fromID = request.getParameter("fromID");
		String toID = request.getParameter("toID");
		String listType = request.getParameter("listType");
		
		if(fromID == null || fromID.equals("") || toID == null || toID.equals("") || listType == null || listType.equals("")) 
			response.getWriter().write(""); // ���鹮�ڸ� ��ȯ
		else if (listType.equals("ten")) response.getWriter().write(getTen(URLDecoder.decode(fromID, "UTF-8"), URLDecoder.decode(toID, "UTF-8"))); 
		else {
			 try {
				HttpSession session = request.getSession();
				if(!URLDecoder.decode(fromID, "UTF-8").equals((String) session.getAttribute("userID"))) {
					response.getWriter().write("");
					return;
				}
				response.getWriter().write(getID(URLDecoder.decode(fromID, "UTF-8"), URLDecoder.decode(toID, "UTF-8"), listType));
			} catch (Exception e) {
				response.getWriter().write("����");
			}
		}
	}
		
	public String getTen(String fromID, String toID) {
		StringBuffer result = new StringBuffer(""); // ���鹮�ڷ� �����ϴ� �ν��Ͻ� ��ȯ
		result.append("{\"result\":["); // JSON = �迭���� �͵��� ǥ���ϰ� ����.
		ChatDAO chatDAO = new ChatDAO();
		ArrayList<ChatDTO> chatList = chatDAO.getChatListByRecent(fromID, toID, 100);
		if(chatList.size()== 0) return ""; //list�� �������� ������ ���� ��ȯ
		for(int i=0; i<chatList.size(); i++) {
			result.append("[{\"value\": \"" + chatList.get(i).getFromID() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getToID() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatContent() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatTime() + "\"}]"); // ������ �����̱� ������ �ݾ������ ]
			if(i != chatList.size() - 1) result.append(","); // ������ ���Ұ� �ƴϰ� �� �ִٸ� ','�� �� �˷���.
		}
		result.append("], \"last\":\"" + chatList.get(chatList.size() - 1).getChatID() + "\"}");
		chatDAO.readChat(fromID, toID);
		return result.toString(); //json�� ���¸� ���ڿ��� ��ȯ
	}
	
	public String getID(String fromID, String toID, String chatID) {
		StringBuffer result = new StringBuffer(""); // ���鹮�ڷ� �����ϴ� �ν��Ͻ� ��ȯ
		result.append("{\"result\":["); // JSON �迭���� �͵��� ǥ���ϰ� ����.
		ChatDAO chatDAO = new ChatDAO();
		ArrayList<ChatDTO> chatList = chatDAO.getChatListByID(fromID, toID, chatID);
		if(chatList.size()== 0) return ""; //list�� �������� ������ ���� ��ȯ
		for(int i=0; i<chatList.size(); i++) {
			result.append("[{\"value\": \"" + chatList.get(i).getFromID() + "\"},"); // ���ȣ.
			result.append("{\"value\": \"" + chatList.get(i).getToID() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatContent() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatTime() + "\"}]"); // ������ �����̱� ������ �ݾ������ ]
			if(i != chatList.size() - 1) result.append(","); // ������ ���Ұ� �ƴϰ� �� �ִٸ� ','�� �� �˷���.
		}
		result.append("], \"last\":\"" + chatList.get(chatList.size() - 1).getChatID() + "\"}");
		chatDAO.readChat(fromID, toID);
		return result.toString(); //json�� ���¸� ���ڿ��� ��ȯ
	}	
}
