package crud.api.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import crud.api.rest.AuthCodeObject;
import crud.api.rest.ErrorObject;
import crud.api.rest.model.User;
import crud.api.rest.repository.UserRepository;
import crud.api.rest.util.EmailTools;

@RestController
@RequestMapping(value = "/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;	
	private static ErrorObject statusResponse = new ErrorObject();
	private EmailTools mail = new EmailTools();
	
	public void authCodeTimmer(User user) {
		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//Tempo para ação
					Thread.sleep(2*60*1000);
					
					if(!userRepository.findById(user.getId()).get().isEnabled()) {
						userRepository.deleteById(user.getId());
					}
					
				} catch (InterruptedException e) {}
			}
		});
		
		if(user != null && user.getId() != 0) {
			thread.start();
		}
	}
	
	public User validUserFields(User user) {
		statusResponse.setHttpStatus(HttpStatus.NOT_ACCEPTABLE);
		
		user.setLogin(user.getLogin().trim());
		user.setEmail(user.getEmail().trim());
		
		User auxUser;
		
		if(!(user.getLogin() == null || user.getLogin().isEmpty())) {
			auxUser = userRepository.findUserByLogin(user.getLogin());
			//Se não houver ou for o mesmo
			if(auxUser == null || (long) auxUser.getId() == (long) user.getId()) {
				if(mail.validEmail(user.getEmail())) {
					auxUser = userRepository.findUserByEmail(user.getEmail());
					//Se não houver ou for o mesmo
					if(auxUser == null || (long) auxUser.getId() == (long) user.getId()) {
						if(!(user.getSenha() == null || user.getSenha().length() < 8)) {
							String code = mail.generateAuthCode();

							user.setSenha(new BCryptPasswordEncoder().encode(user.getSenha()));
							user.setAuthCode(code);
							
							return user;
						}else {
							statusResponse.setMessage("Senha inválida!");
						}
					}else {
						statusResponse.setMessage("Email em uso!");
					}
				}else {
					statusResponse.setMessage("Email inválido!");
				}			
			}else {
				statusResponse.setMessage("Login em uso!");
			}
		}else {
			statusResponse.setMessage("Login inválido!");
		}
		
		return null;
	}
	
	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<ErrorObject> Create(@RequestBody User user) { 
		
		if(user.getId() == 0) {		
			user = validUserFields(user);
			if(user != null) {
				authCodeTimmer(userRepository.save(user));
				
				if(mail.sendAuthCodeEmail(user, "O email "+user.getEmail()+" foi informado para utilização do sistema MySystem.")) {					
					statusResponse.setHttpStatus(HttpStatus.CREATED);
					statusResponse.setMessage("Usuário criado com sucesso!");
				}else {
					statusResponse.setHttpStatus(HttpStatus.NOT_ACCEPTABLE);
					statusResponse.setMessage("Email de confirmação não enviado!");
				}
				
			}
		}else {
			statusResponse.setHttpStatus(HttpStatus.NOT_ACCEPTABLE);
			statusResponse.setMessage("ID foi informado!");
		}

		return new ResponseEntity<ErrorObject>(statusResponse, HttpStatus.OK);
	}
	
	@PutMapping(value = "/", produces = "application/json")
	public ResponseEntity<ErrorObject> Update(@RequestBody User user) { 

		User auxUser = user;
		
		if(userRepository.existsById(user.getId())) {
			user = validUserFields(user);

			if(user != null) {		
				userRepository.save(user);

				statusResponse.setHttpStatus(HttpStatus.OK);
				statusResponse.setMessage("Usuário atualizado com sucesso!");
				
				user.setLogin(null);
				auxUser.setLogin(null);
									
				//código de confirmação para alterar
				if(!mail.sendAuthCodeEmail(user, null)) {
					statusResponse.setHttpStatus(HttpStatus.NOT_ACCEPTABLE);
					statusResponse.setMessage("Email de confirmação não enviado!");
				}
			}		
		}else {
			statusResponse.setHttpStatus(HttpStatus.NOT_FOUND);
			statusResponse.setMessage("Usuário não encontrado!");		
		}
		
		return new ResponseEntity<ErrorObject>(statusResponse, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<ErrorObject> Delete(@PathVariable("id") long id){
		User user = userRepository.findById(id).orElse(null);

		statusResponse.setHttpStatus(HttpStatus.NOT_FOUND);
		statusResponse.setMessage("Usuário não encontrado!");
		
		if(user != null){
			mail.sendAuthCodeEmail(user, "Foi solicitado a exclusão da sua conta.");

			userRepository.deleteById(id);
			statusResponse.setHttpStatus(HttpStatus.OK);
			statusResponse.setMessage("Usuário deletado com sucesso!");
		}
		
		return new ResponseEntity<ErrorObject>(statusResponse, HttpStatus.OK);
	}
	
	@PutMapping(value = "/{id}/active", produces = "application/json")
	public ResponseEntity<ErrorObject> ActiveUser(@PathVariable("id") long id, @RequestBody AuthCodeObject code) { 
		statusResponse.setHttpStatus(HttpStatus.NOT_FOUND);
		statusResponse.setMessage("Não foi possivel ativar a conta!");
		
		User user = userRepository.findById(id).orElse(null);
		
		if(user != null && !user.isEnabled()) {
			user.enable(code.getCode());

			if(user.isEnabled()){
				userRepository.save(user);
				statusResponse.setHttpStatus(HttpStatus.OK);
				statusResponse.setMessage("Usuário foi ativado com sucesso!");
			}
		}
		
		return new ResponseEntity<ErrorObject>(statusResponse, HttpStatus.OK);
	}
	
}
