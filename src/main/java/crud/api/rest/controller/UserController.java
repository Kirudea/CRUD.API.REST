package crud.api.rest.controller;

import javax.mail.MessagingException;

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

import crud.api.rest.CodeObject;
import crud.api.rest.ErrorObject;
import crud.api.rest.model.User;
import crud.api.rest.repository.UserRepository;
import crud.api.rest.service.EmailToolsService;

@RestController
@RequestMapping(value = "/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;	
	private static ErrorObject statusResponse = new ErrorObject();
	private EmailToolsService mail = new EmailToolsService();
	
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
		statusResponse.setError(HttpStatus.NOT_ACCEPTABLE.toString());
		
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
							String code = mail.generateCode(5);

							user.setSenha(new BCryptPasswordEncoder().encode(user.getSenha()));
							user.setCode(code);
							
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
	public ResponseEntity<ErrorObject> create(@RequestBody User user) throws MessagingException { 
		
		if(user.getId() == 0) {		
			//Valida o user e atualiza o responseStatus
			user = validUserFields(user);
		
			if(user != null) {
				//Salva e inicia o timmer de 2min
				authCodeTimmer(userRepository.save(user));
				
				mail.sendAuthCodeEmail(user.getEmail(), "MySystem: Email de confirmação", 
						"O email "+user.getEmail()+" foi informado para utilização do sistema MySystem."+
						"\n\nUtilize o código abaixo para validar.\n\n"+user.getCode());

				statusResponse.setError(HttpStatus.CREATED.toString());
				statusResponse.setMessage("Usuário criado com sucesso!");
			}
		}else {
			statusResponse.setError(HttpStatus.NOT_ACCEPTABLE.toString());
			statusResponse.setMessage("ID foi informado!");
		}

		return new ResponseEntity<ErrorObject>(statusResponse, HttpStatus.OK);
	}
	
	@PutMapping(value = "/", produces = "application/json")
    public ResponseEntity<ErrorObject> update(@RequestBody User user) throws MessagingException{
		statusResponse.setError(HttpStatus.NOT_FOUND.toString());
		statusResponse.setMessage("Usuário não encontrado!");

		if(user.getId() != null && userRepository.existsById(user.getId())){
			//Valida o user e atualiza o responseStatus
			user = validUserFields(user);
            
            if(user != null){
            	userRepository.save(user); 

            	mail.sendAuthCodeEmail(user.getEmail(), "MySystem: Alteração de dados", "Foi realizado a alteração dos dados da sua conta.");
                
				statusResponse.setError(HttpStatus.OK.toString());
				statusResponse.setMessage("Usuário atualizado com sucesso!");
            }
        }

		return new ResponseEntity<ErrorObject>(statusResponse, HttpStatus.OK);
    }
	
	@DeleteMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<ErrorObject> delete(@PathVariable("id") long id) throws MessagingException{
		User user = userRepository.findById(id).orElse(null);

		statusResponse.setError(HttpStatus.NOT_FOUND.toString());
		statusResponse.setMessage("Usuário não encontrado!");
		
		if(user != null){
			userRepository.deleteById(id);

			mail.sendAuthCodeEmail(user.getEmail(), "MySystem: Exclusão de conta", "Foi realizado a exclusão da sua conta.");
			
			statusResponse.setError(HttpStatus.OK.toString());
			statusResponse.setMessage("Usuário deletado com sucesso!");
		}
		
		return new ResponseEntity<ErrorObject>(statusResponse, HttpStatus.OK);
	}
	
	@PostMapping(value = "/{id}/activate", produces = "application/json")
	public ResponseEntity<ErrorObject> ActiveUser(@PathVariable Long id, @RequestBody CodeObject code) { 
		statusResponse.setError(HttpStatus.NOT_FOUND.toString());
		statusResponse.setMessage("Não foi possivel ativar a conta!");
		
		
		User user = userRepository.findById(id).orElse(null);
		
		if(user != null && !user.isEnabled()) {
			user.enable(code.getCode());
			
			if(user.isEnabled()){
				userRepository.save(user);
				
				statusResponse.setError(HttpStatus.OK.toString());
				statusResponse.setMessage("Usuário foi ativado com sucesso!");
			}
		}
		
		return new ResponseEntity<ErrorObject>(statusResponse, HttpStatus.OK);
	}
	
}
