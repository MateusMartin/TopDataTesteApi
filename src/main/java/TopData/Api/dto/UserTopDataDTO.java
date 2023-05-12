package TopData.Api.dto;

import TopData.Api.document.UserTopData;

public class UserTopDataDTO {
		
	private int id;
	private String nome;	
	private String email;
	private int profile_Id;
	
	
	
	
	public static UserTopDataDTO from(UserTopData topDataUser)
	{
		UserTopDataDTO user = new UserTopDataDTO();
		
		user.id = topDataUser.getId();
		user.nome = topDataUser.getNome();
		user.email = topDataUser.getEmail();
		user.profile_Id = topDataUser.getProfile().getId();
		
		
		return user;
	}
	
	
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getProfile_Id() {
		return profile_Id;
	}
	public void setProfile_Id(int profile_Id) {
		this.profile_Id = profile_Id;
	}
	

	
	
	
	
	
	
}
