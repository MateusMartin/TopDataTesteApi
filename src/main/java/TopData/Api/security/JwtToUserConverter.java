package TopData.Api.security;

import java.util.Collections;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import TopData.Api.document.User;

@Component
public class JwtToUserConverter  implements Converter<Jwt, UsernamePasswordAuthenticationToken>{

	@Override
	public UsernamePasswordAuthenticationToken convert(Jwt jwt) {
		
		User user = new User();
		user.setId(jwt.getSubject());
		
		// TODO Auto-generated method stub
		return new UsernamePasswordAuthenticationToken(user, jwt, Collections.EMPTY_LIST);
	}

}
