package TopData.Api.web;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import TopData.Api.document.User;
import TopData.Api.dto.SigupDTO;
import TopData.Api.dto.TokenDTO;
import TopData.Api.dto.loginDTO;
import TopData.Api.repository.ProfileRepository;
import TopData.Api.security.TokenGenerator;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	UserDetailsManager userDetailsManager;
	
	@Autowired
	ProfileRepository profRep;
	
	@Autowired
	TokenGenerator tokenGenerator;
	
	@Autowired
	DaoAuthenticationProvider daoAuthenticationProvider;
	
	@Autowired
	@Qualifier("jwtRefreshTokenAuthProvider")
	JwtAuthenticationProvider refreshTokenAuthProvider;
	
	
	@PostMapping("/register")
	public ResponseEntity register(@RequestBody SigupDTO signupDTO) 
	{
		User user = new User(signupDTO.getUsername(),signupDTO.getPassword());
		userDetailsManager.createUser(user);
	
		Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(user, signupDTO.getPassword(), Collections.EMPTY_LIST);
	
		return ResponseEntity.ok(tokenGenerator.createToken(authentication));
		
	}
	
	@PostMapping("/login")
	public ResponseEntity login(@RequestBody loginDTO login) 
	{	
		Authentication authentication = daoAuthenticationProvider.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(login.getUsername(), login.getPassword()));
		
		
		return ResponseEntity.ok(tokenGenerator.createToken(authentication));
		
		
	}
	
	
	@PostMapping("/token")
	public ResponseEntity token(@RequestBody TokenDTO tokenDTO) 
	{
		Authentication authentication = refreshTokenAuthProvider.authenticate(new BearerTokenAuthenticationToken(tokenDTO.getRefreshT()));
		
		return ResponseEntity.ok(tokenGenerator.createToken(authentication));
	}
	
	@GetMapping(value= "/profile")
	public ResponseEntity update(@AuthenticationPrincipal User user) 
	{
		
		List<?> list_prof = profRep.findAll();
		
	
		return ResponseEntity.status(HttpStatus.OK).body(list_prof);			
				
		
		
	}
	
}
