package likey;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class LikeyDAO {
	
	DataSource dataSource;
	
	public LikeyDAO() {
		try {
			InitialContext initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			dataSource = (DataSource) envContext.lookup("jdbc/SH");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int like(String userID, String boardID, String userIP) {
		String SQL = "INSERT INTO LIKEY VALUES(?, ?, ?)";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection(); //conn 초기화
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			pstmt.setString(2, boardID);
			pstmt.setString(3, userIP);
			return pstmt.executeUpdate(); // update는 update, alter, delete에 사용 query는 조회
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if(conn!=null) conn.close();} catch (Exception e) {e.printStackTrace();}
			try { if(pstmt!=null) pstmt.close();} catch (Exception e) {e.printStackTrace();}
			try { if(rs!=null) rs.close();} catch (Exception e) {e.printStackTrace();}
		}
		return -1; // 추천 중복 오류		
	}
}
