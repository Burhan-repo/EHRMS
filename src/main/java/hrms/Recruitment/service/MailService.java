package hrms.Recruitment.service;

public interface MailService {

	void sendEmail(String from,String to, String subject,String content,String fileName,byte[] data,String contentType);
	
}
