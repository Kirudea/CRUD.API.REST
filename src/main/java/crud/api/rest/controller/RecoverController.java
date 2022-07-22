package crud.api.rest.controller;


import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import crud.api.rest.ErrorObject;
import crud.api.rest.model.User;
import crud.api.rest.repository.UserRepository;
import crud.api.rest.service.EmailToolsService;

@RestController
@RequestMapping(value = "/forgotpassword")
public class RecoverController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	EmailToolsService emailToolsService;

	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<ErrorObject> RecoverPassword(@RequestBody User user) throws MessagingException {
		ErrorObject errorResponse = new ErrorObject();
		errorResponse.setError(HttpStatus.NOT_FOUND.toString());
		errorResponse.setMessage("Usuário não encontrado!");
		
		if(user != null) {
			user = userRepository.findUserByEmail(user.getEmail());

			if (user != null) {
				String novaSenha = emailToolsService.generateCode(8);
				
				user.setSenha(new BCryptPasswordEncoder().encode(novaSenha));
				userRepository.save(user);

				String assunto = "MySystem: Senha alterada";
				String mensagem = "Sua senha foi alterada para " + novaSenha + ".";
	
				emailToolsService.sendAuthCodeEmail(user.getEmail(), assunto, mensagem);
	
				errorResponse.setError(HttpStatus.OK.toString());
				errorResponse.setMessage("Acesso enviado para o email!");
			}
		}
		
		return new ResponseEntity<ErrorObject>(errorResponse, HttpStatus.OK);
	}
}
