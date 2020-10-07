package hrms.Recruitment.service;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.hibernate.envers.AuditReader;

import com.itextpdf.text.DocumentException;

import hrms.Recruitment.models.Applicants;
import hrms.Recruitment.models.Category;
import hrms.Recruitment.models.Employee;
import hrms.Recruitment.models.FileModel;
import hrms.Recruitment.models.ReservedCaste;

public interface DBoperation {
	
	ByteArrayOutputStream getPDffile(String noticeid,String random) throws DocumentException;
	
	List<ReservedCaste> getReservedCasteUnique(List<ReservedCaste> reservedCastes);
	
	List<Category> getCategoryFromReservedCaste(List<ReservedCaste> reservedCaste, int i);
	
	AuditReader getAuditReader();
	
	String getEmailByDistrict(String districtName);
	
	void sendEmail();
	
	void sendEmailNotVerified();
	
	void sendReminder(FileModel fileModel);
	
	void settingReminder();
	
	ByteArrayOutputStream generatePDf();
	
	void sendApplicationToFamily(Employee emp,String baseURL);
	
	void sendmailaftersavingapplicantsInfo(Applicants app,Employee emp);
}
