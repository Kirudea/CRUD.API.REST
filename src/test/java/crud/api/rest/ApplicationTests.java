package crud.api.rest;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

	@Test
	void contextLoads() {
		
	}
	
	public static boolean m1() {
		System.out.println("m1");	
		return false;
	}
	public static boolean m2() {
		System.out.println("m2");
		return true;
	}
	public static void main(String[] args) throws InterruptedException {		
		String email = "kildere.java.test@gmail.com";
		String authCode = "1";
		String systemAddress = "kildere.java.test@outlook.com.br";
		String senha = "javatest2002";
		
		try {	
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
					return new PasswordAuthentication(systemAddress, senha);
				}
			}); 
			
			//session.getProperties().
			
			Address[] toEmails = InternetAddress.parse(email); 	
			
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(systemAddress)); //Remetente
			message.setRecipients(Message.RecipientType.TO, toEmails);
			message.setSubject("Confirmação de email");
			message.setText("O email "+email+" foi informado para utilização do sistema MySystem.\n\n"+
							"Utilize o códido abaixo para autenticação.\n\n"+
							 authCode);

			Transport.send(message);

			//return true;
		} catch (Exception e) {
			e.printStackTrace();
			
			//return false;
		}
		
		//Senha 123
		//$2a$10$rRSd.qDqQGjFYuFP.THNFOdmC3.VnVOgGJmsGn.sLv9CZG/LlWu9q
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
