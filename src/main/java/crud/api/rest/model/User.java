package crud.api.rest.model;

import java.util.Collection;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@SequenceGenerator(name = "seq_user", sequenceName = "seq_user", allocationSize = 1, initialValue = 1)
public class User implements UserDetails  {
	
	private static final long serialVersionUID = 1L;
		
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_user")
	private Long id;
	@Column(unique = true, nullable = false, length = 20)
	private String login;
	@Column(unique = true, nullable = false, length = 100)
	private String email;
	@Column(nullable = false, length = 100)
	private String senha;

	@JsonIgnore //verificar import
	@Column(nullable = false) //verificar SQL
	protected boolean anable = false;

	@JsonIgnore //verificar import
	protected String authCode = null;
	

	public Long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public void setAuthCode(String authCode) {
		if(authCode != null){
			this.authCode = authCode;
			//chama thread que exclui auth code
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	@Override
	public String getPassword() {
		return senha;
	}
	
	@Override
	public String getUsername() {
		return login;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	public void enable(String authCode){
		if(this.authCode.equals(authCode)){
			anable = true;
		}
	}
	@Override
	public boolean isEnabled() {
		return anable;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}
}
