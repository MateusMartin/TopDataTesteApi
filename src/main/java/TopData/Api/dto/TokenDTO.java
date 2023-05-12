package TopData.Api.dto;



public class TokenDTO {
	
	private String userID;
	private String accessToken;
	private String refreshT;
	
	
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getRefreshT() {
		return refreshT;
	}
	public void setRefreshT(String refreshT) {
		this.refreshT = refreshT;
	}

	
	
	
	
}
