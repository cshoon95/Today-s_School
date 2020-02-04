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

// ajax통신 (비동기적 통신)으로 실시간으로 통신.
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
			response.getWriter().write(""); // 공백문자를 반환
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
				response.getWriter().write("오류");
			}
		}
	}
		
	public String getTen(String fromID, String toID) {
		StringBuffer result = new StringBuffer(""); // 공백문자로 시작하는 인스턴스 반환
		result.append("{\"result\":["); // JSON = 배열같은 것들을 표현하고 담음.
		ChatDAO chatDAO = new ChatDAO();
		ArrayList<ChatDTO> chatList = chatDAO.getChatListByRecent(fromID, toID, 100);
		if(chatList.size()== 0) return ""; //list가 존재하지 않으면 공백 반환
		for(int i=0; i<chatList.size(); i++) {
			result.append("[{\"value\": \"" + chatList.get(i).getFromID() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getToID() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatContent() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatTime() + "\"}]"); // 마지막 원소이기 때문에 닫아줘야함 ]
			if(i != chatList.size() - 1) result.append(","); // 마지막 원소가 아니고 더 있다면 ','를 찍어서 알려줌.
		}
		result.append("], \"last\":\"" + chatList.get(chatList.size() - 1).getChatID() + "\"}");
		chatDAO.readChat(fromID, toID);
		return result.toString(); //json의 형태를 문자열로 반환
	}
	
	public String getID(String fromID, String toID, String chatID) {
		StringBuffer result = new StringBuffer(""); // 공백문자로 시작하는 인스턴스 반환
		result.append("{\"result\":["); // JSON 배열같은 것들을 표현하고 담음.
		ChatDAO chatDAO = new ChatDAO();
		ArrayList<ChatDTO> chatList = chatDAO.getChatListByID(fromID, toID, chatID);
		if(chatList.size()== 0) return ""; //list가 존재하지 않으면 공백 반환
		for(int i=0; i<chatList.size(); i++) {
			result.append("[{\"value\": \"" + chatList.get(i).getFromID() + "\"},"); // 대괄호.
			result.append("{\"value\": \"" + chatList.get(i).getToID() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatContent() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatTime() + "\"}]"); // 마지막 원소이기 때문에 닫아줘야함 ]
			if(i != chatList.size() - 1) result.append(","); // 마지막 원소가 아니고 더 있다면 ','를 찍어서 알려줌.
		}
		result.append("], \"last\":\"" + chatList.get(chatList.size() - 1).getChatID() + "\"}");
		chatDAO.readChat(fromID, toID);
		return result.toString(); //json의 형태를 문자열로 반환
	}	
}
