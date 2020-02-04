<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="user.UserDAO" %>
<%@ page import="util.SHA256" %>
<%@ page import="java.io.PrintWriter" %>
<% 
 	request.setCharacterEncoding("UTF-8");
 	String code = null;
 	if(request.getParameter("code") != null) {
 		code = request.getParameter("code");
 	}
 	
 	UserDAO userDAO = new UserDAO();
	String userID = null;
	if(session.getAttribute("userID") != null) { // 로그인 중이면 세션 값 가져와서 로그인 유지
		userID = (String) session.getAttribute("userID");
	}
	if(userID == null){ // 로그인하지 않은 경우
		request.getSession().setAttribute ("messageType", "오류 메시지");
		request.getSession().setAttribute ("messageContent", "로그인을 해주세요.");
		response.sendRedirect("login.jsp");
		return;	
	}
 	
	String userEmail = userDAO.getUserEmail(userID);
	boolean isRight =  (new SHA256().getSHA256(userEmail).equals(code)) ? true : false; //사용자가 보낸 코드값이 일치하면 트루 아니면 폴스 반환
	if(isRight == true) {
		userDAO.setUserEmailChecked(userID);
 		PrintWriter script = response.getWriter();
 		script.println("<script>");
 		script.println("alert('인증에 성공했습니다.');");
 		script.println("location.href = 'index.jsp'");
 		script.println("</script>");
 		script.close();
 		return;		
	} else {
 		PrintWriter script = response.getWriter();
 		script.println("<script>");
 		script.println("alert('유효하지 않은 코드입니다.');");
 		script.println("location.href = 'index.jsp'");
 		script.println("</script>");
 		script.close();
 		return;
	}
 	
 %>