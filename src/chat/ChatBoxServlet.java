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
			response.getWriter().write(" "); //아이디가 널이거나 공백일시 오류발생 : 0을 반환
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
				response.getWriter().write(new ChatDAO().getAllUnreadChat(userID) + ""); // ""공백을 준 이유 : 문자열로 반환하기 위해
			}
		}
	}
	
	public String getBox(String userID) {
		StringBuffer result = new StringBuffer(""); // 공백문자로 시작하는 인스턴스 반환
		result.append("{\"result\":["); // JSON 배열같은 것들을 표현하고 담음.
		ChatDAO chatDAO = new ChatDAO();
		ArrayList<ChatDTO> chatList = chatDAO.getBox(userID);
		if(chatList.size()== 0) return ""; //list가 존재하지 않으면 공백 반환
		for(int i=chatList.size() -1; i>=0; i--) {
			String unread = "";
			String userProfile = "";
			if(userID.equals(chatList.get(i).getToID())) {
				unread = chatDAO.getUnreadChat(chatList.get(i).getFromID(), userID) + ""; // 안읽은 메시지 수
				if(unread.equals("0")) unread = "";
			}
			if(userID.equals(chatList.get(i).getToID())) {
				userProfile = new UserDAO().getProfile(chatList.get(i).getFromID());
			} else {
				userProfile = new UserDAO().getProfile(chatList.get(i).getToID());
			}
			result.append("[{\"value\": \"" + chatList.get(i).getFromID() + "\"},"); // 대괄호.
			result.append("{\"value\": \"" + chatList.get(i).getToID() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatContent() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatTime() + "\"},");
			result.append("{\"value\": \"" + unread + "\"},"); 
			result.append("{\"value\": \"" + userProfile + "\"}]"); 	// 마지막 원소이기 때문에 닫아줘야함 ]
			if(i != 0) result.append(","); // 마지막 원소가 아니고 더 있다면 ','를 찍어서 알려줌.
		}
		result.append("], \"last\":\"" + chatList.get(chatList.size() - 1).getChatID() + "\"}");
		return result.toString(); //json의 형태를 문자열로 반환
	}	
	
}
