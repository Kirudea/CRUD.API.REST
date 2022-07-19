package crud.api.rest.service;

import java.util.Properties;
import java.util.Random;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class EmailToolsService {

	private String login = "kildere.java.test@outlook.com";
	private String senha = "javatest2002";

	public boolean validEmail(String email){
		try{
			new InternetAddress(email).validate();
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	public String generateCode(int tamanho) {
		Random rnd = new Random();
		String authCode = "";

		for (int i = 0; i < tamanho; i++) {
			authCode += rnd.nextInt(10);
		}
		
		return authCode;
	}
	
	public void sendAuthCodeEmail(String email, String assunto, String mensagem) throws MessagingException {		
		Properties p = new Properties();
		
		p.put("mail.smtp.auth", "true");
		p.put("mail.smtp.starttls", "true");
		p.put("mail.smtp.host", "smtp-mail.outlook.com");
		p.put("mail.smtp.port", "587");
		p.put("mail.smtp.starttls.enable","true");
		
		Session session = Session.getInstance(p, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(login, senha);
			}
		}); 
		
		Address[] toEmails = InternetAddress.parse(email); 	
		
		Message mail = new MimeMessage(session);
		mail.setFrom(new InternetAddress(login)); //Remetente
		mail.setRecipients(Message.RecipientType.TO, toEmails);
		mail.setSubject(assunto);
		mail.setText(mensagem);

		Transport.send(mail);
	}
	
}
