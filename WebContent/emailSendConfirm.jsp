<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 이메일 --%>
<!DOCTYPE html>
<html>
<%
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
%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="css/bootstrap.css">
	<link rel="stylesheet" href="css/custom.css">
	<title>Insert title here</title>
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="js/bootstrap.js"></script>
	
</head>
<body>
	<nav class="navbar navbar-default">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="index.jsp">Today's School</a>
		</div>
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
			<<ul class="nav navbar-nav">
				<li><a href="index.jsp">홈</a>				
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" 
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false">메시지함<span id="unread" class="label label-info"></span><span class="caret"></span>
					</a>
					<ul class="dropdown-menu">
						<li><a href="find.jsp">친구찾기</a></li>
						<li><a href="box.jsp">채팅방</a></li>					
					</ul> 
				</li>		
				<li><a href="boardView.jsp">자유게시판</a></li>
			</ul>
			<%
				if(userID == null) {
			%>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" 
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false">접속하기<span class="caret"></span>
					</a>
					<ul class="dropdown-menu">
						<li><a href="login.jsp">로그인</a></li>
						<li class="active"><a href="join.jsp">회원가입</a></li>
					</ul> 
				</li>				
			</ul>
			<%
				} else {
			%>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" 
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false">회원관리<span class="caret"></span>
					</a>
					<ul class="dropdown-menu">
						<li><a href="update.jsp">회원정보수정</a></li>
						<li><a href="profileUpdate.jsp">프로필 수정</a></li> 
						<li><a href="logoutAction.jsp">로그아웃</a></li>
					</ul> 
				</li>				
			</ul>			
			<%
				}
			%>
		</div>
	</nav>
	<section class="container mt-3" style="max-width: 560px">
		<div class="alert alert-warning mt-4" role="alert">
			이메일 주소 인증을 하셔야 이용 가능합니다. <br> 인증메일을 받지 못하셨나요?
		</div>
		<a href="emailSendAction.jsp" class="btn btn-primary">인증 메일 다시 받기</a>
	</section>
	
	<%
		String messageContent = null;
		if(session.getAttribute("messageContent") != null) {
			messageContent = (String) session.getAttribute("messageContent");
		}
		String messageType = null;
		if(session.getAttribute("messageType") != null) {
			messageType = (String) session.getAttribute("messageType");
		}
		if(messageContent != null) {
	%>
	<div class="modal fade" id="messageModal" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="vertical-alignment-helper">
			<div class="modal-dialog vertical-align-center">
				<div class="modal-content <%if(messageType.equals("오류 메시지")) out.println("panel-warning"); else out.println("panel-success"); %>">
					<div class="modal-header panel-heading">
						<button type="button" class="close" data-dismiss="modal"> <!-- 닫기 버튼 -->
							<span aria-hidden="true">&times</span> <!-- x버튼에 해당하는 그림 -->
							<span class="sr-only">Close</span>
						</button>
						<h4 class="modal-title">
							<%= messageType %>
						</h4>
					</div>
					<div class="modal-body">
						<%= messageContent %>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary" data-dismiss="modal">확인</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script>
		$('#messageModal').modal("show"); // id가 messageModal 인 것을 모달창 보이게 설정
	</script>
	<%
		//서버로부터 세션값을 받아 사용하므로 한 번 쓰면 세션 종료
		session.removeAttribute("messageContent"); 
		session.removeAttribute("messageType");
		}
	%>
	<%
		if(userID != null) {
	%>
		<script type="text/javascript">
			$(document).ready(function() {
				getUnread();
				getInfiniteUnread();
			});
		</script>
	<%
		}
	%>
</body>
</html>