package board;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/BoardDeleteServlet")
public class BoardDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response); //처리내용을 doPost와 동일하게 처리
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		HttpSession session = request.getSession(); // 현재 세션값을 가져옴..
		String userID = (String) session.getAttribute("userID");
		String boardID = request.getParameter("boardID");
		if(boardID == null || boardID.equals("")) {
			request.getSession().setAttribute("userID", userID);
			request.getSession().setAttribute("messageType", "오류 메시지");
			request.getSession().setAttribute("messageContent", "접근할 수 없습니다.");
			response.sendRedirect("index.jsp");
			return;	// return -> 함수 종료		
		}
		BoardDAO boardDAO = new BoardDAO();
		BoardDTO board = boardDAO.getBoard(boardID); // 게시판 정보 가져옴.
		if(!userID.equals(board.getUserID())) {
			request.getSession().setAttribute("userID", userID);
			request.getSession().setAttribute("messageType", "오류 메시지");
			request.getSession().setAttribute("messageContent", "접근할 수 없습니다.");
			response.sendRedirect("index.jsp");
			return;				
		}
		String savePath = request.getRealPath("/upload").replaceAll("\\\\", "/");
		String prev = boardDAO.getRealFile(boardID);
		int result = boardDAO.delete(boardID);
		if(result == -1) {
			request.getSession().setAttribute("userID", userID);
			request.getSession().setAttribute("messageType", "오류 메시지");
			request.getSession().setAttribute("messageContent", "접근할 수 없습니다.");
			response.sendRedirect("index.jsp");
			return;				
		} else {
			File prevFile = new File(savePath = "/" + prev);
			if(prevFile.exists()) {
				prevFile.delete();
			}
			request.getSession().setAttribute("userID", userID);
			request.getSession().setAttribute("messageType", "성공 메시지");
			request.getSession().setAttribute("messageContent", "삭제가 완료되었습니다.");
			response.sendRedirect("boardView.jsp");
			return;	
			
		}
	}

}
