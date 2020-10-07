package hrms.Recruitment.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService{
	
	@Autowired
	private JavaMailSender sender;

	@Async
	@Override
	public void sendEmail(String from, String to, String subject, String content, String fileName,byte[] data,String contentType) {
		
		MimeMessage msg = sender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(msg,true);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, true);
			if(fileName != null && data != null && data.length>0 && contentType != null) {
				helper.addAttachment(fileName, new ByteArrayResource(data),contentType);
			}
			sender.send(msg);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
