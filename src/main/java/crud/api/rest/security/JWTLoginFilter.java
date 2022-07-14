package crud.api.rest.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import crud.api.rest.model.User;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

	protected JWTLoginFilter(String url, AuthenticationManager authenticationManager) {
		//Obriga a auth da URL
		super(new AntPathRequestMatcher(url));
		
		setAuthenticationManager(authenticationManager);
	}	

	@Override
	//Retorna user após auth o login
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
		   throws AuthenticationException, IOException, ServletException {
		
		//Obtendo token
		User user = new ObjectMapper().
				readValue(request.getInputStream(), User.class);
		
		//Retorna os dados de user
		return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
	}
	
	@Override
	//Se auth for bem sucedida, gera e retorna o token como response
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		new JWTTokenAuthService().addAuth(response, authResult.getName());
	}

}
