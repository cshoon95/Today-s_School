package likey;

public class LikeyDTO {
	String userID;
	int boardID;
	String userIP;
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public String getUserIP() {
		return userIP;
	}
	public void setUserIP(String userIP) {
		this.userIP = userIP;
	}
	
	public int getBoardID() {
		return boardID;
	}
	public void setBoardID(int boardID) {
		this.boardID = boardID;
	}
	
	public LikeyDTO() {
		
	}
	public LikeyDTO(String userID, int boardID, String userIP) {
		super();
		this.userID = userID;
		this.boardID = boardID;
		this.userIP = userIP;
	}
	
	
	
}
