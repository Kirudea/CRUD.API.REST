package crud.api.rest;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

	@Test
	void contextLoads() {
		
	}
	
	public static void main(String[] args) {
		String email = "kildere.java.test@g@mail.com.br";
		String senha = "javatest2002";
		
		String myEmail = "kilderehenriquedp@gmail.com";

		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex) {
			System.out.println("FFF");
		}
		
		/*
		try {			
			Properties p = new Properties();
			p.put("mail.smtp.auth", "true");
			//p.put("mail.smtp.starttls", "true");
			p.put("mail.smtp.host", "smtp.gmail.com");
			p.put("mail.smtp.port", "465");//587 ou 465
			p.put("mail.smtp.socketFactory.port", "465");
			p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");			
		
			Session session = Session.getInstance(p, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(email, senha);
				}
			}); 
			
			Address[] toEmails = InternetAddress.parse(email+", "+myEmail); 	
			
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(email)); //Remetente
			message.setRecipients(Message.RecipientType.TO, toEmails);
			message.setSubject("Mensagem de test");
			message.setText("Hello World!");
			
			Transport.send(message);
		
		} catch (Exception e) {
			System.out.println(e);
		}
		 */
	}
	
}
