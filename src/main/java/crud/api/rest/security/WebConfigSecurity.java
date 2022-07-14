package crud.api.rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import crud.api.rest.service.ImplementUserDetailsService;

@Configuration
@EnableWebSecurity
//Mapea URL's e controla o acesso
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private ImplementUserDetailsService implementUserDetailsService; 
	
	@Override
	//Configura permissões de acesso
	protected void configure(HttpSecurity http) throws Exception {
		//Barra user sem token
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		
		//Permite URL para todos
		.disable().authorizeHttpRequests().antMatchers("/").permitAll()
		//.antMatchers("/").permitAll()
		
		//Config URL de logout
		.anyRequest().authenticated().and().logout().logoutSuccessUrl("/")
		
		//Mapea URL de logout
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		
		//add filtro que auth o login e gera o token
		.and().addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), 
							   UsernamePasswordAuthenticationFilter.class)
		
		//add filtro que auth o token recebido
		.addFilterBefore(new JWTAPIAuthFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//Consulta User no BD
		auth.userDetailsService(implementUserDetailsService)
		//Criptografia de senha
		.passwordEncoder(new BCryptPasswordEncoder());
	}
}
