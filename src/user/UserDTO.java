package user;

public class UserDTO {
	
	String userID;
	String userPassword;
	String userNickname;
	int userAge;
	String userGender;
	String userProfile;
	String userEmail;
	String userEmailHash;
	boolean userEmailChecked;
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getUserNickname() {
		return userNickname;
	}
	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}
	public int getUserAge() {
		return userAge;
	}
	public void setUserAge(int userAge) {
		this.userAge = userAge;
	}
	public String getUserGender() {
		return userGender;
	}
	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}
	public String getUserProfile() {
		return userProfile;
	}
	public void setUserProfile(String userProfile) {
		this.userProfile = userProfile;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserEmailHash() {
		return userEmailHash;
	}
	public void setUserEmailHash(String userEmailHash) {
		this.userEmailHash = userEmailHash;
	}
	public boolean isUserEmailChecked() {
		return userEmailChecked;
	}
	public void setUserEmailChecked(boolean userEmailChecked) {
		this.userEmailChecked = userEmailChecked;
	}
	public UserDTO(String userID, String userPassword, String userNickname, int userAge, String userGender,
			String userProfile, String userEmail, String userEmailHash, boolean userEmailChecked) {
		super();
		this.userID = userID;
		this.userPassword = userPassword;
		this.userNickname = userNickname;
		this.userAge = userAge;
		this.userGender = userGender;
		this.userProfile = userProfile;
		this.userEmail = userEmail;		
		this.userEmailHash = userEmailHash;
		this.userEmailChecked = userEmailChecked;
	}
	
	public UserDTO() {
		
	}
	
}
