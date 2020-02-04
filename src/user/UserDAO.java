package user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;


public class UserDAO {
	
	DataSource dataSource;
	
	public UserDAO() {
		try {
			InitialContext initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			dataSource = (DataSource) envContext.lookup("jdbc/SH");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public int login(String userID, String userPassword) { //로그인
		String SQL = "SELECT * FROM USER WHERE userID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null; // PreparedStatement sql문장 안전하게 실행해줌.
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection(); //conn 초기화
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery(); // Query는 데이터베이스에서 검색한느 것 조회 등에 사용
			if(rs.next()) {
				if(rs.getString("userPassword").equals(userPassword)) { //사용자가 입력한 password랑 db에있는 password랑 똑같을 경우 로그인.
					return 1; //로그인 성공
				}
				return 2; // 비밀번호 틀림
			} else {
				return 0;  // 해당사용자가 존재하지 않음
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if(conn!=null) conn.close();} catch (Exception e) {e.printStackTrace();}
			try { if(pstmt!=null) pstmt.close();} catch (Exception e) {e.printStackTrace();}
			try { if(rs!=null) rs.close();} catch (Exception e) {e.printStackTrace();}
		}
		return -1; // db오류
	}
	
	public int registerCheck(String userID) { //아이디 중복체크
		String SQL = "SELECT * FROM USER WHERE userID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection(); //conn 초기화
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery(); // Query는 데이터베이스에서 검색한느 것 조회 등에 사용
			if(rs.next() || userID.equals("")) {
				return 0; // 이미 존재하는 회원
			} else {
				return 1;  // 가입 가능한 회원 아이디
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return -2; // db오류
	}
	
	//회원가입 , userAge는 int형이지만 String으로 반환
	public int register(String userID, String userPassword, String userNickname, String userAge, String userGender,
			String userEmail, String userEamilHash, boolean userEamilChecked, String userProfile) { 
		String SQL = "INSERT INTO USER VALUES(?, ?, ?, ?, ?, ?, ?, false, ?)";
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = dataSource.getConnection(); //conn 초기화
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			pstmt.setString(2, userPassword);
			pstmt.setString(3, userNickname); 
			pstmt.setInt(4, Integer.parseInt(userAge)); //int형은 integer.parseInt("");
			pstmt.setString(5, userGender);
			pstmt.setString(6, userEmail);
			pstmt.setString(7, userEamilHash);
			pstmt.setString(8, userProfile);
			return pstmt.executeUpdate(); // update는 update, alter, delete에 사용 query는 조회
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if(conn!=null) conn.close();} catch (Exception e) {e.printStackTrace();}
			try { if(pstmt!=null) pstmt.close();} catch (Exception e) {e.printStackTrace();}
		}
		return -1; // 회원가입 실패
	}
	
	public String getUserEmail(String userID) { //이메일 완료
		String SQL = "SELECT userEmail FROM USER WHERE userID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection(); //conn 초기화
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if(conn!=null) conn.close();} catch (Exception e) {e.printStackTrace();}
			try { if(pstmt!=null) pstmt.close();} catch (Exception e) {e.printStackTrace();}
			try { if(rs!=null) rs.close();} catch (Exception e) {e.printStackTrace();}
		}
		return null; // db 오류
	}

	public boolean getUserEmailChecked(String userID) { //이메일 검증
		String SQL = "SELECT userEmailChecked FROM USER WHERE userID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = dataSource.getConnection(); //conn 초기화
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getBoolean(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if(conn!=null) conn.close();} catch (Exception e) {e.printStackTrace();}
			try { if(pstmt!=null) pstmt.close();} catch (Exception e) {e.printStackTrace();}
			try { if(rs!=null) rs.close();} catch (Exception e) {e.printStackTrace();}
		}
		return false; // db 오류
	}	
	
	public boolean setUserEmailChecked(String userID) { //이메일 완료
		String SQL = "UPDATE USER SET userEmailChecked = true WHERE userID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection(); //conn 초기화
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			pstmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if(conn!=null) conn.close();} catch (Exception e) {e.printStackTrace();}
			try { if(pstmt!=null) pstmt.close();} catch (Exception e) {e.printStackTrace();}
			try { if(rs!=null) rs.close();} catch (Exception e) {e.printStackTrace();}
		}
		return false; // db 오류
	}	
	
	public UserDTO getUser(String userID) { //회원정보 가져옴. (회원정보수정용) (한 명의 사용자 정보를 반환하는 함수)
		String SQL = "SELECT * FROM USER WHERE userID = ?";
		UserDTO user = new UserDTO();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection(); //conn 초기화
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery(); // Query는 데이터베이스에서 검색한느 것 조회 등에 사용
			if(rs.next()) {
				user.setUserID(userID);
				user.setUserPassword(rs.getString("userPassword"));
				user.setUserNickname(rs.getString("userNickname"));
				user.setUserAge(rs.getInt("userAge"));
				user.setUserGender(rs.getString("userGender"));
				user.setUserEmail(rs.getString("userEmail"));
				user.setUserProfile(rs.getString("userProfile"));
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return user; // db오류
	}
	
	//회원 정보 수정
	public int update(String userID, String userPassword, String userNickname, String userAge, String userGender,
			String userEmail) { 
		String SQL = "UPDATE USER SET userPassword = ?, userNickname = ?, userAge = ?, userGender = ?, userEmail = ? WHERE userID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = dataSource.getConnection(); //conn 초기화
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userPassword);
			pstmt.setString(2, userNickname);
			pstmt.setInt(3, Integer.parseInt(userAge)); //int형은 integer.parseInt("");
			pstmt.setString(4, userGender);
			pstmt.setString(5, userEmail);
			pstmt.setString(6, userID);
			return pstmt.executeUpdate(); // update는 update, alter, delete에 사용 query는 조회
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if(conn!=null) conn.close();} catch (Exception e) {e.printStackTrace();}
			try { if(pstmt!=null) pstmt.close();} catch (Exception e) {e.printStackTrace();}
		}
		return -1; // 회원가입 실패
	}
	
	//프로필 사진 수정
	public int profile(String userID, String userProfile) {
		String SQL = "UPDATE USER SET userProfile = ? WHERE userID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = dataSource.getConnection(); //conn 초기화
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userProfile);
			pstmt.setString(2, userID);
			return pstmt.executeUpdate(); // update는 update, alter, delete에 사용 query는 조회
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if(conn!=null) conn.close();} catch (Exception e) {e.printStackTrace();}
			try { if(pstmt!=null) pstmt.close();} catch (Exception e) {e.printStackTrace();}
		}
		return -1; // DB 오류
	}
	
	public String getProfile(String userID) { //프로필사진 경로
		String SQL = "SELECT userProfile FROM USER WHERE userID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection(); //conn 초기화
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery(); // Query는 데이터베이스에서 검색한느 것 조회 등에 사용
			if(rs.next()) {
				if(rs.getString("userProfile").equals("")) { //사용자의 프로필사진이 공백인경우, 이제 막 회원가입을 한 경우
					return "http://localhost:8080/SH/images/icon.png";
				}
				return "http://localhost:8080/SH/upload/" + rs.getString("userProfile");
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "http://localhost:8080/SH/images/icon.png"; // db오류
	}
}
