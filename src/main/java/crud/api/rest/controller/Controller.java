package crud.api.rest.controller;

import java.util.Properties;
import java.util.Random;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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

import crud.api.rest.model.User;
import crud.api.rest.repository.UserRepository;

@RestController
@RequestMapping(value = "/user")
public class Controller {
	
	@Autowired
	private UserRepository userRepository;	
	
	private static HttpStatus status;
	private static String response;
	private static byte wrong_field;
	
	public static void main(String[] args) {
		System.out.println(validarEmail("kildere.java.test@gmail.com"));
	}

	public static boolean validarEmail(String email){
		try{
			new InternetAddress(email).validate();
			return true;
		}catch(Exception e) {
			return false;
		}
	}

	public static boolean authCodeEmail(String email){
		String systemAddress = "kildere.java.test@outlook.com";
		String senha = "javatest2002";
		
		try {	
			Properties p = new Properties();
			
			p.put("mail.smtp.auth", "true");
			p.put("mail.smtp.starttls", "true");
			p.put("mail.smtp.host", "smtp-mail.outlook.com");
			p.put("mail.smtp.port", "587");
			p.put("mail.smtp.starttls.enable","true");
			p.put("mail.smtp.auth", "true"); 
		
			Random rnd = new Random();
			String authCode = "";

			for (int i = 0; i < 5; i++) {
				authCode += rnd.nextInt(10);
			}

			Session session = Session.getInstance(p, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(systemAddress, senha);
				}
			}); 
			
			Address[] toEmails = InternetAddress.parse(email); 	
			
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(systemAddress)); //Remetente
			message.setRecipients(Message.RecipientType.TO, toEmails);
			message.setSubject("Confirmação de email");
			message.setText("O email "+email+" foi informado para utilização do sistema MySystem.\n\n"+
							"Utilize o códido abaixo para autenticação.\n\n"+
							 authCode);

			Transport.send(message);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public void CreateUser(User user) {
		if(user.getId() == 0) {		
			user = validUserFields(user);
			if(user != null) {
				//Criptografa senha
				user.setSenha(new BCryptPasswordEncoder().encode(user.getSenha()));
				userRepository.save(user);
				
				status = HttpStatus.CREATED;
				wrong_field = 0;
				response = "Usuário criado com sucesso!";
			}
		}else {
			wrong_field = 1;
			response = "ID foi informado!";
			status = HttpStatus.NOT_ACCEPTABLE;
		}
	}

	public void UpdateUser(User user) {
		if(userRepository.findById(user.getId()).orElse(null) != null) {
			user = validUserFields(user);

			if(user != null) {		
				//Criptografa senha
				user.setSenha(new BCryptPasswordEncoder().encode(user.getSenha()));
				userRepository.save(user);
				status = HttpStatus.OK;
				wrong_field = 0;
				response = "Usuário atualizado com sucesso!";				
			}		
		}else {
			status = HttpStatus.NOT_FOUND;
			wrong_field = 1;
			response = "Usuário não encontrado!";		
		}
	}
	
	public User validUserFields(User user) {
		status = HttpStatus.NOT_ACCEPTABLE;
		user.setLogin(user.getLogin().trim());
		user.setEmail(user.getEmail().trim());
		
		User aux;
		
		if(!(user.getLogin() == null || user.getLogin().isEmpty())) {
			aux = userRepository.findUserByLogin(user.getLogin());
			//Se não houver ou for o mesmo
			if(aux == null || (long) aux.getId() == (long) user.getId()) {
				if(validarEmail(user.getEmail())) {
					aux = userRepository.findUserByEmail(user.getEmail());
					//Se não houver ou for o mesmo
					if(aux == null || (long) aux.getId() == (long) user.getId()) {
						if(!(user.getSenha() == null || user.getSenha().length() < 8)) {
							return user;
						}else {
							wrong_field = 4;
							response = "Senha inválida!";
						}
					}else {
						wrong_field = 3;
						response = "Email em uso!";
					}
				}else {
					wrong_field = 3;
					response = "Email inválido!";
				}			
			}else {
				wrong_field = 2;
				response = "Login em uso!";
			}
		}else {
			wrong_field = 2;
			response = "Login inválido!";
		}
		
		return null;
	}
	
	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<String> Create(@RequestBody User user) { 
		
		CreateUser(user);
		
		response = "{\"status\": \""+status+"\","+
				    "\"wrong_field\": \""+wrong_field+"\","+
					"\"message\": \""+response+"\"}";

		return new ResponseEntity<String>(response, status);
	}
	
	@PutMapping(value = "/", produces = "application/json")
	public ResponseEntity<String> Update(@RequestBody User user) { 
		
		UpdateUser(user);
		
		response = "{\"status\": \""+status+"\","+
					"\"wrong_field\": \""+wrong_field+"\","+
					"\"message\": \""+response+"\"}";
		
		return new ResponseEntity<String>(response, status);
	}
	
	@DeleteMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<String> Delete(@PathVariable("id") long id){
		
		if(userRepository.existsById(id)){
			userRepository.deleteById(id);
			status = HttpStatus.OK;
			response = "Usuário deletado com sucesso!";
		}else {			
			status = HttpStatus.NOT_FOUND;
			response = "Usuário não encontrado!";
		}
		
		response = "{\"status\": \""+status+"\","+
					"\"message\": \""+response+"\"}";
		
		return new ResponseEntity<String>(response, status);
	}
	
}
