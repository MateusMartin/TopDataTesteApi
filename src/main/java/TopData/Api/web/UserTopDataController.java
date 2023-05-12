package TopData.Api.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import TopData.Api.document.Profile;
import TopData.Api.document.User;
import TopData.Api.document.UserTopData;
import TopData.Api.dto.BuscarUsuarioFiltrosDto;
import TopData.Api.dto.ErrorDto;
import TopData.Api.dto.LoginTopDataDTO;
import TopData.Api.dto.UserSlcDTO;
import TopData.Api.dto.UserTopDataDTO;
import TopData.Api.dto.UserUpdateDTO;
import TopData.Api.repository.ProfileRepository;
import TopData.Api.repository.UserRepository;
import TopData.Api.repository.UserTopDataRepository;

@RestController
@RequestMapping("/api/topdata/users")
@CrossOrigin("*")
public class UserTopDataController {

	
	@Autowired
	private UserTopDataRepository UserRep;
	
	@Autowired
	private ProfileRepository ProfileRep;
	
	
	
	@PostMapping("/buscar")	
	public ResponseEntity BuscarUsuarios(@RequestBody BuscarUsuarioFiltrosDto filtros) 
	{
		
		
		List<Object[]> users = UserRep.usersQuery(filtros.getNome(),filtros.getEmail(),filtros.getProfile());
		List<UserSlcDTO> dtos = mapToUserSlcDTO(users);
		
		 return ResponseEntity.status(HttpStatus.OK).body(dtos);	
	}
	
	@PostMapping(value= "/registrar")
	public ResponseEntity adcionar(@AuthenticationPrincipal User user,@RequestBody UserTopData usuario) 
	{
					
		Profile Profile = ProfileRep.findByidInt(usuario.getProfile().getId());
		
		
		
		if(Profile.getId() == 0) 
		{
			
			return ResponseEntity.status(HttpStatus.OK).body("Profile Não Cadastrada");
		} 
		
		List<UserTopData> List = UserRep.findByEmail(usuario.getEmail());
		
		if(List.isEmpty()) 
		{
			
			UserTopData newUser = UserRep.save(usuario);	
			return ResponseEntity.status(HttpStatus.OK).body(newUser);	
			
		} else 
		{
			
			return ResponseEntity.status(HttpStatus.OK).body("Email ja Cadastrado");
		}
			
		
	}
	
	
	
	@PostMapping(value= "/logar")	
	public ResponseEntity Logar(@AuthenticationPrincipal User user, @RequestBody LoginTopDataDTO usuario) 
	{
					
		List<UserTopData> List = UserRep.Logar(usuario.getUsername(),usuario.getPassword());		
	
		if(List.isEmpty()) 
		{						
			
			ErrorDto er = new ErrorDto();
			er.setMenssagem("Usuario ou Senha Incorretos");
			
			return ResponseEntity.status(HttpStatus.OK).body(er);
		} else 
		{			
					
			UserTopDataDTO logged = UserTopDataDTO.from(List.get(0));							
			return ResponseEntity.status(HttpStatus.OK).body(logged);	
			
		}
						
	}
	
	@PostMapping(value= "/update")
	public ResponseEntity update(@AuthenticationPrincipal User user, @RequestBody UserUpdateDTO usuario) 
	{
		
		Profile prof = new Profile();
		prof.setId(usuario.getProfile());
		
		int ret = UserRep.updateUser(usuario.getNome(),usuario.getEmail(), prof, usuario.getPassword(), usuario.getId());
		
		ErrorDto dtoret = new ErrorDto();
		
		if(ret == 1)
		{			
			
			dtoret.setMenssagem("Usuario alterado com sucesso");
			return ResponseEntity.status(HttpStatus.OK).body(dtoret);	
		} else 
		{
			dtoret.setMenssagem("Erro");
			return ResponseEntity.status(HttpStatus.OK).body(dtoret);			
		}
		
		
		
	}
	
	
	@GetMapping(value= "/profile")
	public ResponseEntity update(@AuthenticationPrincipal User user) 
	{
		
		List<?> list_prof = ProfileRep.findAll();
		
	
		return ResponseEntity.status(HttpStatus.OK).body(list_prof);			
				
		
		
	}
	
	
	public List<UserSlcDTO> mapToUserSlcDTO(List<Object[]> results) {
	    List<UserSlcDTO> dtos = new ArrayList();
	    for (Object[] result : results) {
	        UserSlcDTO dto = new UserSlcDTO();
	        dto.setId((int) result[0]);
	        dto.setNome((String) result[1]);
	        dto.setProfile((String) result[2]);
	        dto.setEmail((String) result[3]);
	        dtos.add(dto);
	    }
	    return dtos;
	 }
	
	
	
}
