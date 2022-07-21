package crud.api.rest.model;

import java.util.ArrayList;
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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;


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

	@JsonIgnore
	@Column(nullable = false) //verificar SQL
	protected boolean enable = false;

	@JsonIgnore
	@Column(unique = true)
	protected String auth_code = null;
	

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
	
	//@JsonIgnore afeta do Spring e dá erro no login e etc
	@JsonProperty(access = Access.WRITE_ONLY)
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

	@JsonIgnore
	public String getCode() {
		return auth_code;
	}
	public void setCode(String auth_code) {
		if(auth_code != null){
			this.auth_code = auth_code;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	@JsonIgnore
	@Override
	public String getPassword() {
		return senha;
	}
	
	@JsonIgnore
	@Override
	public String getUsername() {
		return login;
	}
	
	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	public void enable(String authCode){
		if(authCode != null && authCode.equals(this.auth_code)){
			this.auth_code = null;
			this.enable = true;
		}
	}
	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return enable;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return new ArrayList<>();
	}
}
