package TopData.Api.dto;

import TopData.Api.document.User;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;


@Data
public class UserDTO {

	private String id;
	private String username;
	
	

	public static UserDTO from(User user) 
	{
		
		UserDTO dto = new UserDTO();
		
		dto.setId(user.getId());
		dto.setUsername(user.getUsername());
								
		return dto;
			
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}
	
	
	
	
	
}
