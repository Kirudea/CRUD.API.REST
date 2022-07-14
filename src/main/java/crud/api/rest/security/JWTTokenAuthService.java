package crud.api.rest.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import crud.api.rest.ApplicationContextLoad;
import crud.api.rest.model.User;
import crud.api.rest.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Component
public class JWTTokenAuthService {
	
	//Em milisegundos
	//1 dia = 86400000
	private static final long EXPIRATION_TIME = 86400000L;
	
	//Senha para melhorar a auth
	private static final String SECRET = "senhaS$DF&&%F*&758756%Dvf5afdv&";
	
	private static final String TOKEN_PREFIX = "Bearer";
	
	private static final String HEADER_STRING = "Authorization";
	
	//Gerando token
	public void addAuth(HttpServletResponse response, String username) throws IOException {
		//Montar token
		String JWT = Jwts.builder()
		.setSubject(username)
		
		//Tempo de expiração => tempoAtual + duração
		.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
		
		//Tipo de criptografia e chave secreta
		.signWith(SignatureAlgorithm.HS512, SECRET)
		.compact();
		
		String token = TOKEN_PREFIX+" "+JWT;
		
		response.addHeader(HEADER_STRING, token);
		response.getWriter().write("{\"Authorization\": \""+token+"\"}");
	}
	
	public Authentication getAuth(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);
		
		if(token != null) {
			String userName = Jwts.parser().setSigningKey(SECRET) //Cria analisador e define a chave secreta
			.parseClaimsJws(token.replace(TOKEN_PREFIX, "")) //Traduz o token para json
			.getBody().getSubject(); //Pega o usuário do corpo do json
			
			if(userName != null) { 
				//Como não foi possivel utilizar injeção do UserRepository
				//Foi criado um Contexto de Aplicação para chama-lo
				User user = ApplicationContextLoad.getApplicationContext()
							.getBean(UserRepository.class).findUserByLogin(userName);
				
				if(user != null)
					return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities()); 
			}
		}
		
		return null;
	}
}
