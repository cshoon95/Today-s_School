<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="user.UserDAO" %>
<%@ page import="board.BoardDAO" %>
<%@ page import="likey.LikeyDAO"%>
<%@ page import="java.io.PrintWriter" %>
<%!	// <!% --선언문 --> ip주소 가져오기.
	public static String getClientIP(HttpServletRequest request) {
		String ip = request.getHeader("X-FORWARDED-FOR");
		if(ip == null || ip.length() == 0) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
%>
<% 	
	String userID = null;
	if(session.getAttribute("userID") != null) { // 로그인 중이면 세션 값 가져와서 로그인 유지
		userID = (String) session.getAttribute("userID");
	}
	
	if(userID == null) { // 로그인하지 않은 경우
		session.setAttribute("messageType", "오류 메시지");
		session.setAttribute("messageContent", "로그인을 해주세요.");
		response.sendRedirect("login.jsp");
		return;	
	}
 	
	String boardID = request.getParameter("boardID");
	if(boardID == null || boardID.equals("")) {
		session.setAttribute("messageType", "오류 메시지");
		session.setAttribute("messageContent", "접근할 수 없습니다.");
		response.sendRedirect("index.jsp");
		return;
	}
	
 	request.setCharacterEncoding("UTF-8");
 	String evaluationID = null;
 	if(request.getParameter("evaluationID") != null) {
 		evaluationID = request.getParameter("evaluationID");
 	}
 	LikeyDAO likeyDAO = new LikeyDAO();
 	BoardDAO boardDAO = new BoardDAO();
 	int result = likeyDAO.like(userID, boardID, getClientIP(request));
	if(result == 1) {
	   result = boardDAO.like(boardID);
	   if(result == 1) {
			session.setAttribute("messageType", "성공 메시지");
			session.setAttribute("messageContent", "추천이 완료되었습니다.");
			response.sendRedirect("boardView.jsp");
			return;
	   } else {
			session.setAttribute("messageType", "오류 메시지");
			session.setAttribute("messageContent", "DB 오류 발생");
			response.sendRedirect("index.jsp");
			return;
		}
	}
	else {
		session.setAttribute("messageType", "오류 메시지");
		session.setAttribute("messageContent", "이미 추천을 누른 글입니다.");
		response.sendRedirect("boardView.jsp");
		return;
	}
 %>