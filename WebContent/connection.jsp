<!-- 커넥션풀 테스트 -->
<html>
<head>
<%@ page import="java.sql.*, javax.sql.*, java.io.*, javax.naming.InitialContext, javax.naming.Context" %>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
	<%
		InitialContext initCtx = new InitialContext();
		Context envContext = (Context) initCtx.lookup("java:/comp/env");
		DataSource ds = (DataSource) envContext.lookup("jdbc/SH");
		Connection conn = ds.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery("SELECT VERSION();");
		while(rset.next()) {
			out.println("MYSQL Version: " + rset.getString("version()"));
		}
		rset.close();
		stmt.close();
		conn.close();
		initCtx.close();
	%>
</body>
</html>