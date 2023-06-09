package TopData.Api.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import TopData.Api.document.User;
import TopData.Api.dto.UserDTO;
import TopData.Api.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	UserRepository userRepository;
	
	
	@GetMapping("/{id}")
	@PreAuthorize("#user.id == #id")
	public ResponseEntity user(@AuthenticationPrincipal User user,@PathVariable String id) 
	{
		
		
		return ResponseEntity.ok(UserDTO.from(userRepository.findUserById(id)));
	}
	
	
	
}
