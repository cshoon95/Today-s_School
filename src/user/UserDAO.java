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
	public int login(String userID, String userPassword) { //�α���
		String SQL = "SELECT * FROM USER WHERE userID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null; // PreparedStatement sql���� �����ϰ� ��������.
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection(); //conn �ʱ�ȭ
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery(); // Query�� �����ͺ��̽����� �˻��Ѵ� �� ��ȸ � ���
			if(rs.next()) {
				if(rs.getString("userPassword").equals(userPassword)) { //����ڰ� �Է��� password�� db���ִ� password�� �Ȱ��� ��� �α���.
					return 1; //�α��� ����
				}
				return 2; // ��й�ȣ Ʋ��
			} else {
				return 0;  // �ش����ڰ� �������� ����
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if(conn!=null) conn.close();} catch (Exception e) {e.printStackTrace();}
			try { if(pstmt!=null) pstmt.close();} catch (Exception e) {e.printStackTrace();}
			try { if(rs!=null) rs.close();} catch (Exception e) {e.printStackTrace();}
		}
		return -1; // db����
	}
	
	public int registerCheck(String userID) { //���̵� �ߺ�üũ
		String SQL = "SELECT * FROM USER WHERE userID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection(); //conn �ʱ�ȭ
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery(); // Query�� �����ͺ��̽����� �˻��Ѵ� �� ��ȸ � ���
			if(rs.next() || userID.equals("")) {
				return 0; // �̹� �����ϴ� ȸ��
			} else {
				return 1;  // ���� ������ ȸ�� ���̵�
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
		return -2; // db����
	}
	
	//ȸ������ , userAge�� int�������� String���� ��ȯ
	public int register(String userID, String userPassword, String userNickname, String userAge, String userGender,
			String userEmail, String userEamilHash, boolean userEamilChecked, String userProfile) { 
		String SQL = "INSERT INTO USER VALUES(?, ?, ?, ?, ?, ?, ?, false, ?)";
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = dataSource.getConnection(); //conn �ʱ�ȭ
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			pstmt.setString(2, userPassword);
			pstmt.setString(3, userNickname); 
			pstmt.setInt(4, Integer.parseInt(userAge)); //int���� integer.parseInt("");
			pstmt.setString(5, userGender);
			pstmt.setString(6, userEmail);
			pstmt.setString(7, userEamilHash);
			pstmt.setString(8, userProfile);
			return pstmt.executeUpdate(); // update�� update, alter, delete�� ��� query�� ��ȸ
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if(conn!=null) conn.close();} catch (Exception e) {e.printStackTrace();}
			try { if(pstmt!=null) pstmt.close();} catch (Exception e) {e.printStackTrace();}
		}
		return -1; // ȸ������ ����
	}
	
	public String getUserEmail(String userID) { //�̸��� �Ϸ�
		String SQL = "SELECT userEmail FROM USER WHERE userID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection(); //conn �ʱ�ȭ
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
		return null; // db ����
	}

	public boolean getUserEmailChecked(String userID) { //�̸��� ����
		String SQL = "SELECT userEmailChecked FROM USER WHERE userID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = dataSource.getConnection(); //conn �ʱ�ȭ
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
		return false; // db ����
	}	
	
	public boolean setUserEmailChecked(String userID) { //�̸��� �Ϸ�
		String SQL = "UPDATE USER SET userEmailChecked = true WHERE userID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection(); //conn �ʱ�ȭ
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
		return false; // db ����
	}	
	
	public UserDTO getUser(String userID) { //ȸ������ ������. (ȸ������������) (�� ���� ����� ������ ��ȯ�ϴ� �Լ�)
		String SQL = "SELECT * FROM USER WHERE userID = ?";
		UserDTO user = new UserDTO();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection(); //conn �ʱ�ȭ
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery(); // Query�� �����ͺ��̽����� �˻��Ѵ� �� ��ȸ � ���
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
		return user; // db����
	}
	
	//ȸ�� ���� ����
	public int update(String userID, String userPassword, String userNickname, String userAge, String userGender,
			String userEmail) { 
		String SQL = "UPDATE USER SET userPassword = ?, userNickname = ?, userAge = ?, userGender = ?, userEmail = ? WHERE userID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = dataSource.getConnection(); //conn �ʱ�ȭ
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userPassword);
			pstmt.setString(2, userNickname);
			pstmt.setInt(3, Integer.parseInt(userAge)); //int���� integer.parseInt("");
			pstmt.setString(4, userGender);
			pstmt.setString(5, userEmail);
			pstmt.setString(6, userID);
			return pstmt.executeUpdate(); // update�� update, alter, delete�� ��� query�� ��ȸ
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if(conn!=null) conn.close();} catch (Exception e) {e.printStackTrace();}
			try { if(pstmt!=null) pstmt.close();} catch (Exception e) {e.printStackTrace();}
		}
		return -1; // ȸ������ ����
	}
	
	//������ ���� ����
	public int profile(String userID, String userProfile) {
		String SQL = "UPDATE USER SET userProfile = ? WHERE userID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = dataSource.getConnection(); //conn �ʱ�ȭ
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userProfile);
			pstmt.setString(2, userID);
			return pstmt.executeUpdate(); // update�� update, alter, delete�� ��� query�� ��ȸ
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if(conn!=null) conn.close();} catch (Exception e) {e.printStackTrace();}
			try { if(pstmt!=null) pstmt.close();} catch (Exception e) {e.printStackTrace();}
		}
		return -1; // DB ����
	}
	
	public String getProfile(String userID) { //�����ʻ��� ���
		String SQL = "SELECT userProfile FROM USER WHERE userID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection(); //conn �ʱ�ȭ
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery(); // Query�� �����ͺ��̽����� �˻��Ѵ� �� ��ȸ � ���
			if(rs.next()) {
				if(rs.getString("userProfile").equals("")) { //������� �����ʻ����� �����ΰ��, ���� �� ȸ�������� �� ���
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
		return "http://localhost:8080/SH/images/icon.png"; // db����
	}
}
