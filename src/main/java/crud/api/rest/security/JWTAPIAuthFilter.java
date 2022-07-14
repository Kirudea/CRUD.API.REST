package crud.api.rest.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

//Filtro onde as requisições são capturadas para auth
public class JWTAPIAuthFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//Envia o token para auth
		Authentication auth = new JWTTokenAuthService().getAuth((HttpServletRequest) request);
		
		//Salva auth no Spring
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		//Prossegue com o uso do sistema
		chain.doFilter(request, response);
	}
	
}
