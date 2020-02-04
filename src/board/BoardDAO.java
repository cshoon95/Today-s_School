package board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {
		
		DataSource dataSource;
		
		public BoardDAO() {
			try {
				InitialContext initContext = new InitialContext();
				Context envContext = (Context) initContext.lookup("java:/comp/env");
				dataSource = (DataSource) envContext.lookup("jdbc/SH");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	public int write(String userID, String boardTitle, String boardContent, String boardFile, String boardRealFile) { 
		String SQL = "INSERT INTO BOARD SELECT ?, IFNULL((SELECT MAX(boardID) + 1 FROM BOARD), 1), ?, ?, now(), 0, ?, ?, IFNULL((SELECT MAX(boardGroup) + 1 FROM BOARD), 0), 0, 0, 1, 0";
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = dataSource.getConnection(); //conn 초기화
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			pstmt.setString(2, boardTitle);
			pstmt.setString(3, boardContent); 
			pstmt.setString(4, boardFile); 
			pstmt.setString(5, boardRealFile); 
			return pstmt.executeUpdate(); // update는 update, alter, delete에 사용 query는 조회
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if(conn!=null) conn.close();} catch (Exception e) {e.printStackTrace();}
			try { if(pstmt!=null) pstmt.close();} catch (Exception e) {e.printStackTrace();}
		}
		return -1; // 회원가입 실패
	}
	
	public BoardDTO getBoard(String boardID) { //게시판 정보 조회. 
		String SQL = "SELECT * FROM BOARD WHERE boardID = ?";
		BoardDTO board = new BoardDTO();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection(); //conn 초기화
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, boardID);
			rs = pstmt.executeQuery(); // Query는 데이터베이스에서 검색한느 것 조회 등에 사용
			if(rs.next()) {
				board.setUserID(rs.getString("userID"));
				board.setBoardID(rs.getInt("boardID"));
				board.setBoardTitle(rs.getString("boardTitle").replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>;"));
				board.setBoardContent(rs.getString("boardContent").replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>;"));
				board.setBoardDate(rs.getString("boardDate").substring(0, 11));
				board.setBoardHit(rs.getInt("boardHit"));
				board.setBoardFile(rs.getString("boardFile"));
				board.setBoardRealFile(rs.getString("boardRealFile"));
				board.setBoardSequence(rs.getInt("boardSequence"));
				board.setBoardLevel(rs.getInt("boardLevel"));
				board.setBoardAvailable(rs.getInt("boardAvailable"));
				board.setBoardLikeCount(rs.getInt("boardLikeCount"));
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
		return board; // db오류
	}
	
	public ArrayList<BoardDTO> getList(String pageNumber) { //게시판 정보 조회. 
		ArrayList<BoardDTO> boardList = null;
		String SQL = "SELECT * FROM BOARD WHERE boardGroup > (SELECT MAX(boardGroup) FROM BOARD) - ? AND boardGroup <= (SELECT MAX(boardGroup) FROM BOARD) - ? ORDER BY boardGroup DESC, boardSequence ASC";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boardList = new ArrayList<BoardDTO>();
		try {
			conn = dataSource.getConnection(); //conn 초기화
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, Integer.parseInt(pageNumber) * 10);
			pstmt.setInt(2, (Integer.parseInt(pageNumber) -1) * 10);
			rs = pstmt.executeQuery(); // Query는 데이터베이스에서 검색한느 것 조회 등에 사용
			while(rs.next()) {
				BoardDTO board = new BoardDTO();
				board.setUserID(rs.getString("userID"));
				board.setBoardID(rs.getInt("boardID"));
				board.setBoardTitle(rs.getString("boardTitle").replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>;"));
				board.setBoardContent(rs.getString("boardContent").replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>;"));
				board.setBoardDate(rs.getString("boardDate").substring(0, 11));
				board.setBoardHit(rs.getInt("boardHit"));
				board.setBoardFile(rs.getString("boardFile"));
				board.setBoardRealFile(rs.getString("boardRealFile"));
				board.setBoardSequence(rs.getInt("boardSequence"));
				board.setBoardLevel(rs.getInt("boardLevel"));
				board.setBoardAvailable(rs.getInt("boardAvailable"));
				board.setBoardLikeCount(rs.getInt("boardLikeCount"));
				boardList.add(board);
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
		return boardList; // db오류
	}
	
	public int hit(String boardID) {  //조회수
		String SQL = "UPDATE BOARD SET boardHit = boardHit + 1 WHERE boardID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = dataSource.getConnection(); //conn 초기화
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, boardID);
			return pstmt.executeUpdate(); // update는 update, alter, delete에 사용 query는 조회
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if(conn!=null) conn.close();} catch (Exception e) {e.printStackTrace();}
			try { if(pstmt!=null) pstmt.close();} catch (Exception e) {e.printStackTrace();}
		}
		return -1; // 실패
	}
	
	public String getFile(String boardID) {
		String SQL = "SELECT boardFile FROM BOARD WHERE boardID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection(); //conn 초기화
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, boardID);
			rs = pstmt.executeQuery(); // Query는 데이터베이스에서 검색한느 것 조회 등에 사용
			if(rs.next()) {
				return rs.getString("boardFile"); // boardFile을 문자열 형태로 리턴.				
			}
			return ""; // 존재하지않거나 오류일 때 공백 출력
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
		return ""; // db오류		
	}
	
	public boolean nextPage(String pageNumber) {
		String SQL = "SELECT * FROM BOARD WHERE boardGroup >= ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection(); //conn 초기화
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, Integer.parseInt(pageNumber) * 10);
			rs = pstmt.executeQuery(); // Query는 데이터베이스에서 검색한느 것 조회 등에 사용
			if(rs.next()) {
				return true; // 게시물이 존재한다면 true;				
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
		return false; //존재하지않는다면	
	}
	
	public int targetPage(String pageNumber) {
		String SQL = "SELECT COUNT(boardGroup) FROM BOARD WHERE boardGroup > ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection(); //conn 초기화
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, (Integer.parseInt(pageNumber) -1)* 10);
			rs = pstmt.executeQuery(); // Query는 데이터베이스에서 검색한느 것 조회 등에 사용
			if(rs.next()) {
				return rs.getInt(1) / 10;			
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
		return 0;
	}
	
	public String getRealFile(String boardID) {
		String SQL = "SELECT boardRealFile FROM BOARD WHERE boardID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection(); //conn 초기화
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, boardID);
			rs = pstmt.executeQuery(); // Query는 데이터베이스에서 검색한느 것 조회 등에 사용
			if(rs.next()) {
				return rs.getString("boardRealFile"); // boardFile을 문자열 형태로 리턴.				
			}
			return ""; // 존재하지않거나 오류일 때 공백 출력
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
		return ""; // db오류		
	}
	
	public int update(String boardID, String boardTitle, String boardContent, String boardFile, String boardRealFile) { 
		String SQL = "UPDATE BOARD SET boardTitle = ?, boardContent = ?, boardFile = ?, boardRealFile = ? WHERE boardID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = dataSource.getConnection(); //conn 초기화
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, boardTitle);
			pstmt.setString(2, boardContent);
			pstmt.setString(3, boardFile); 
			pstmt.setString(4, boardRealFile); 
			pstmt.setInt(5, Integer.parseInt(boardID)); 
			return pstmt.executeUpdate(); // update는 update, alter, delete에 사용 query는 조회
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if(conn!=null) conn.close();} catch (Exception e) {e.printStackTrace();}
			try { if(pstmt!=null) pstmt.close();} catch (Exception e) {e.printStackTrace();}
		}
		return -1; // 회원가입 실패
	}	
	
	public int delete(String boardID) { 
		String SQL = "UPDATE BOARD SET boardAvailable = 0 WHERE boardID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = dataSource.getConnection(); //conn 초기화
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, Integer.parseInt(boardID)); 
			return pstmt.executeUpdate(); // update는 update, alter, delete에 사용 query는 조회
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if(conn!=null) conn.close();} catch (Exception e) {e.printStackTrace();}
			try { if(pstmt!=null) pstmt.close();} catch (Exception e) {e.printStackTrace();}
		}
		return -1; // 회원가입 실패
	}	
	
	// Parent -> 답변을 다는 글 (부모글)
	public int reply(String userID, String boardTitle, String boardContent, String boardFile, String boardRealFile, BoardDTO parent) { 
		String SQL = "INSERT INTO BOARD SELECT ?, IFNULL((SELECT MAX(boardID) + 1 FROM BOARD), 1), ?, ?, now(), 0, ?, ?, ?, ?, ?, 1";
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = dataSource.getConnection(); //conn 초기화
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			pstmt.setString(2, boardTitle);
			pstmt.setString(3, boardContent); 
			pstmt.setString(4, boardFile); 
			pstmt.setString(5, boardRealFile); 
			pstmt.setInt(6, parent.getBoardGroup()); 
			pstmt.setInt(7, parent.getBoardSequence() + 1); 
			pstmt.setInt(8, parent.getBoardLevel() + 1); 
			return pstmt.executeUpdate(); // update는 update, alter, delete에 사용 query는 조회
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if(conn!=null) conn.close();} catch (Exception e) {e.printStackTrace();}
			try { if(pstmt!=null) pstmt.close();} catch (Exception e) {e.printStackTrace();}
		}
		return -1; // 회원가입 실패
	}
	
	public int replayUpdate(BoardDTO parent) { 
		String SQL = "UPDATE BOARD SET boardSequence = boardSequence + 1 WHERE boardGroup = ? AND boardSequence > ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = dataSource.getConnection(); //conn 초기화
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, parent.getBoardGroup());
			pstmt.setInt(2, parent.getBoardSequence());
			return pstmt.executeUpdate(); // update는 update, alter, delete에 사용 query는 조회
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if(conn!=null) conn.close();} catch (Exception e) {e.printStackTrace();}
			try { if(pstmt!=null) pstmt.close();} catch (Exception e) {e.printStackTrace();}
		}
		return -1; // 회원가입 실패
	}
	
	public int like(String boardID) { //추천
		String SQL = "UPDATE BOARD SET boardLikeCount = boardLikeCount + 1 WHERE boardID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection(); //conn 초기화
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(2, Integer.parseInt(boardID));
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { if(conn!=null) conn.close();} catch (Exception e) {e.printStackTrace();}
			try { if(pstmt!=null) pstmt.close();} catch (Exception e) {e.printStackTrace();}
			try { if(rs!=null) rs.close();} catch (Exception e) {e.printStackTrace();}
		} 	
		return -1; // db 오류
	}

}
