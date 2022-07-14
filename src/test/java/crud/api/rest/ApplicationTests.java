package crud.api.rest;

import java.util.Properties;
import java.util.Random;

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
		Random rnd = new Random();

		for (int i = 0; i < 5; i++) {
			System.out.println(rnd.nextInt(10));
		}
	}
	

	/*
	 String email = "kildere.java.test@okok.com.br";
		String senha = "javatest2002";
		
		String myEmail = "kilderehenriquedp@gmail.com";

		try {	
			new InternetAddress(email).validate();
			
			Properties p = new Properties();
			
			p.put("mail.smtp.auth", "true");
			p.put("mail.smtp.starttls", "true");
			p.put("mail.smtp.host", "smtp-mail.outlook.com");
			p.put("mail.smtp.port", "587");
			p.put("mail.smtp.starttls.enable","true");
			p.put("mail.smtp.auth", "true"); 
		
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

			System.out.println("Enviado!");
		} catch (Exception e) {
			System.out.println(e);
		}
	 */
}
