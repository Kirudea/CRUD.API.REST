package crud.api.rest.email;

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

public class EmailTools {

	public boolean validEmail(String email){
		try{
			new InternetAddress(email).validate();
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	public String generateAuthCode() {
		Random rnd = new Random();
		String authCode = "";

		for (int i = 0; i < 5; i++) {
			authCode += rnd.nextInt(10);
		}
		
		return authCode;
	}

	public boolean sendAuthCodeEmail(String email, String authCode){
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
			e.printStackTrace();
			
			return false;
		}
	}
	
}
