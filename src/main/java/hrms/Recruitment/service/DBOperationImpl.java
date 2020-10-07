package hrms.Recruitment.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import hrms.Recruitment.models.Applicants;
import hrms.Recruitment.models.Category;
import hrms.Recruitment.models.Employee;
import hrms.Recruitment.models.Family;
import hrms.Recruitment.models.FileModel;
import hrms.Recruitment.models.Location;
import hrms.Recruitment.models.Proposal;
import hrms.Recruitment.models.ReservedCaste;
import hrms.Recruitment.repository.FileModelRepository;
import hrms.Recruitment.repository.LocationRepository;
import hrms.Recruitment.repository.NoticeRepository;

@Service
public class DBOperationImpl implements DBoperation{

	private static final Logger log = LoggerFactory.getLogger(DBOperationImpl.class);
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private AsyncExecutor exe;
	
//	@Autowired
//	private JavaMailSender sender;
	
	@Autowired
	private FileModelRepository fileRepo;
	
	@Autowired
	private NoticeRepository noticeRepo;
	
	@Autowired
	private EntityManager em;
	
	@Autowired
	private LocationRepository locRepo;
	
	@Autowired
	private TemplateEngine tempEngine;
	
//	@Autowired 
//	private MessageSource messageSource;
	
	@Override
	public ByteArrayOutputStream getPDffile(String noticeid,String random) throws DocumentException {
		if(noticeid != null) {
			Proposal notice = noticeRepo.findById(Long.valueOf(noticeid)).get();
			if(notice != null && notice.getFilename() != null) {
				Document document = new Document();
		        ByteArrayOutputStream out = new ByteArrayOutputStream();

				
				byte[] fileDate = notice.getData();
//				out.write(fileDate);
				System.out.println();   
				if(random != null && random.equalsIgnoreCase("html")) {
					try {
//						Document document = new Document();
			            PdfWriter writer = PdfWriter.getInstance(document, out);
			            StringBuilder htmlString = new StringBuilder();
			            htmlString.append(html(notice));
			            document.open();
			            InputStream is = new ByteArrayInputStream(htmlString.toString().getBytes());
			            XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
			            document.close();
//						InputStream is = new ByteArrayInputStream(html(notice).getBytes());
//						out.write(IOUtils.toByteArray(is));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if(random != null && random.equalsIgnoreCase("attach")) {
					try {
						out.write(fileDate);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				return out;
			}
		}
		
		return null;
	}
	
	
	public String html(Proposal notice) {
		String html = "<div class=\"col-sm-8\">\r\n" + 
				"				<h2><b>Prepare a recruitment proposal</b></h2>\r\n" + 
				"					<div class=\"row\">\r\n" + 
				"						<span>Recruitment Module > Recruitment by Individual Departments through MPSC > Prepare a Recruitment Proposal</span>\r\n" + 
				"					</div>\r\n" + 
				"					<div class=\"row\">\r\n" + 
				"						<form th:action=\"@{/registernotice}\" th:object=\"${notice}\" method=\"post\" enctype=\"multipart/form-data\" id=\"noticeregisterid\" onsubmit=\"return FileTypeValidate()\">\r\n" + 
				"						<div class=\"row\">\r\n" + 
				"							  <div class=\"form-group col-md-6\">\r\n" + 
				"							    <label for=\"designations\"><span >Desgination:</span>"+notice.getDesignation()+"</label>\r\n" + 
				"							  </div>							\r\n" + 
				"							  <div class=\"form-group col-md-6\" id=\"hidden_div\" style=\"color: red\">\r\n" + 
				"							    <label for=\"exampleFormControlInput1\"><span >Proposal Id:</span>"+notice.getId()+"</label>\r\n" + 
				"							  </div>						  \r\n" + 
				"						</div>\r\n" + 
				"							<div class=\"form-group\">\r\n" + 
				"						    <label for=\"expectedvacancy\"><span>Exptected Vacancy:</span>"+notice.getExpectedvacancy()+"</label>\r\n" + 
				"						  </div>\r\n" + 
				"						  <div class=\"form-group\">\r\n" + 
				"						    <label for=\"vacancy\"><span></span>TotalVacancy:"+notice.getTotalvacancy()+"</label>\r\n" + 
				"						  </div>\r\n" + 
				"						  <div class=\"row\">\r\n" + 
				"						  	<div class=\"form-group col-md-3\">\r\n" + 
				"							    <label for=\"OBC\">OBC:"+"OBC"+"</label>\r\n" + 
				"						    </div>\r\n" + 
				"						    <div class=\"form-group col-md-3\">						    \r\n" + 
				"							    <label for=\"ST\">ST:"+"ST"+"</label>\r\n" + 
				"						    </div>\r\n" + 
				"						  	<div class=\"form-group col-md-3\">\r\n" + 
				"							    <label for=\"SC\">SC:"+"SC"+"</label>\r\n" + 
				"							</div>\r\n" + 
				"						  	<div class=\"form-group col-md-3\">						    \r\n" + 
				"							    <label for=\"GENERAL\">GENERAL:"+"GENERAL"+"</label>\r\n" + 
				"							 </div>\r\n" + 
				"						  </div>\r\n" + 
				" -->						<div class=\"form-group\">\r\n" + 
				"						    <label for=\"reservedvacancy\"><span>ReservedVacancy:"+notice.getReservedvacancy()+"</span></label>\r\n" + 
				"						  </div>\r\n" + 
				"						  <div class=\"form-group\">\r\n" + 
				"						    <label for=\"extravacancy\"><span >ExtraVacancy:"+notice.getExtravacancy()+"</span></label>\r\n" + 
				"						  </div>						\r\n" + 
				"						  <div class=\"form-group\">\r\n" + 
				"						  	<label><span>Subject:</span>"+Jsoup.clean(notice.getInfo(), Whitelist.none())+"</label>\r\n" + 

				"						  </div>						  						\r\n" + 
				"						  \r\n" + 
				"						</form>\r\n" + 
				"				</div>\r\n" + 
				"			</div>";
		
		return html;
	}


	@Override
	public List<Category> getCategoryFromReservedCaste(List<ReservedCaste> reservedcate,int totalVacancy) {
		
		List<ReservedCaste> uniqueR = getReservedCasteUnique(reservedcate);
		
    	List<Category> caste = new ArrayList<>();
    	if(reservedcate != null) {
    		long categoryMaxId = 0;
    		long tempCategoryIndexForV = 0;
    		int indexForGenForV = 0;
    		long tempCategoryIndexForH = 0;
    		int indexForGenForH = 0;
    		int totalVacancyMod = totalVacancy;
    		ReservedCaste ss = null;
    		ReservedCaste sss = null;
    		for(ReservedCaste s : reservedcate) {
    			if(s.getCasteCode() != null && s.getCasteCode().equals("GEN") && s.getType().equals("V")) {
    				tempCategoryIndexForV = categoryMaxId;
    				categoryMaxId++;
    				indexForGenForV = reservedcate.indexOf(s);
    				ss = s;
    				continue;
    			}
    			if(s.getCasteCode() != null && s.getCasteCode().equals("GEN") && s.getType().equals("H")) {
    				tempCategoryIndexForH = categoryMaxId;
    				categoryMaxId++;
    				indexForGenForH = reservedcate.indexOf(s);
    				sss = s;
    				continue;
    			}

    			if(s.getCasteCode() != null) {
    				int modVac = 0;
    				Category cas = new Category();
    				cas.setCategory_Code(s.getCasteCode());
    				if(s.getPercentageReserved() !=0 && s.getType().equals("V")) {
    					int percentage = 0;
    					for(ReservedCaste rr : uniqueR) {
    						if(rr.getCasteCode().equals(s.getCasteCode())) {
    							percentage = rr.getPercentageReserved();
    						}
    					}
    					modVac = Math.round((totalVacancy*percentage)/100);
    					totalVacancyMod  = totalVacancyMod - modVac;
    					cas.setVacancy(modVac);
    				} else {
    					cas.setVacancy(0);
    				}
    				
    				cas.setPercentage_Reserved(s.getPercentageReserved());
    				cas.setId(categoryMaxId);
    				cas.setCategory_Name(s.getCasteName());
    				cas.setType(s.getType());
    				caste.add(cas);
    				categoryMaxId++;
    			}
    		}
    		if(ss != null  && ss.getType().equals("V")) {
				Category cas = new Category();
				cas.setCategory_Code(ss.getCasteCode());
				cas.setVacancy(totalVacancyMod);				
				cas.setPercentage_Reserved(ss.getPercentageReserved());
				cas.setId(tempCategoryIndexForV);
				cas.setCategory_Name(ss.getCasteName());
				cas.setType(ss.getType());
				caste.add(indexForGenForV, cas);
    		}    		
    		if(sss != null && sss.getType().equals("H")) {
				Category cas = new Category();
				cas.setCategory_Code(sss.getCasteCode());
				cas.setVacancy(0);				
				cas.setPercentage_Reserved(sss.getPercentageReserved());
				cas.setId(tempCategoryIndexForH);
				cas.setCategory_Name(sss.getCasteName());
				cas.setType(sss.getType());
				caste.add(indexForGenForH, cas);
    		}
    	}
    	return caste;
	}


	@Override
	public List<ReservedCaste> getReservedCasteUnique(List<ReservedCaste> reservedcate) {
		Map<String,ReservedCaste> ress = new HashMap<>();
		for(ReservedCaste s : reservedcate) {
			if(ress.containsKey(s.getCasteCode())) {
				ReservedCaste ss = ress.get(s.getCasteCode());
				int vac = ss.getPercentageReserved();
				vac += s.getPercentageReserved();
				ss.setPercentageReserved(vac);
				ress.put(s.getCasteCode(), ss);
			} else {
				ress.put(s.getCasteCode(), s);
			}
		}
		
		reservedcate = ress.values().stream().collect(Collectors.toList());
		Collections.sort(reservedcate, new Comparator<ReservedCaste>() {

			@Override
			public int compare(ReservedCaste o1, ReservedCaste o2) {
				return Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId()));
			}
		});
		return reservedcate;
	}


	@Override
	public AuditReader getAuditReader() {
		AuditReader reader = AuditReaderFactory.get(em);
		return reader;
	}


	@Override
	public String getEmailByDistrict(String districtName) {
		String MedicalEmail = "";
		String PoliceEmail = "";
		List<Location> listLocation = locRepo.findByDistrictName(districtName);
		for(Location l : listLocation) {
			if(l.getMedical_District_Email() != null) {
				MedicalEmail = l.getMedical_District_Email();
				break;
			}
		}
		for(Location l : listLocation) {
			if(l.getPolice_District_Email() != null) {
				PoliceEmail = l.getPolice_District_Email();
				break;
			}
		}
		return MedicalEmail+"_"+PoliceEmail;
	}


	@Override
	public void sendEmail() {
		List<FileModel> fileList = fileRepo.findAllBySent(false);
		if(fileList.size()>0) {
			log.info("Scheduler started for mail service for files not sent");
		}
		for(FileModel ff : fileList) {			
			String content = "<p>Please verify attached file within given time.</p>";			
			mailService.sendEmail(null, ff.getAuthorityEmail(), "Verification for", content, 
					ff.getFileName(), ff.getData(), ff.getContentType());
			ff.setSent(true);
			fileRepo.save(ff);			
		}
	}


	@Override
	public void sendEmailNotVerified() {
		List<FileModel> fileList = fileRepo.findAllBySentAndVerified(true,false);
		if(fileList.size()>0) {
			log.info("Scheduler started for mail service for files sent but not verified.");
		}
		for(FileModel ff : fileList) {
			exe.getAsyncAutoExecutor().execute(new Runnable() {				
				@Override
				public void run() {
					try {
						System.out.println("sent from reverification"+ff.getFileName());
						String content = "<p>Please verify attached file within given time.</p>";			
						mailService.sendEmail(null, ff.getAuthorityEmail(), "Verification for", content, 
								ff.getFileName(), ff.getData(), ff.getContentType());
						ff.setSent(true);
						fileRepo.save(ff);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}				
				}
			});

		}		
	}


	@Override
	public void sendReminder(FileModel ff) {
//		MimeMessage msg = sender.createMimeMessage();
		try {
			System.out.println("sent from reverification"+ff.getFileName());
			String content = "<p>Please verify attached file within given time.</p>";			
			LocalDateTime now = LocalDateTime.now().plusDays(7);
			ff.setDateTime(now);
			mailService.sendEmail(null, ff.getAuthorityEmail(), "Verification for", content, 
					ff.getFileName(), ff.getData(), ff.getContentType());
			ff.setSent(true);
			ff.setReminder(false);
			fileRepo.save(ff);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}


	@Override
	public void settingReminder() {
		List<FileModel> fileList = fileRepo.findAllByDate(LocalDateTime.now());
		for(FileModel ff :fileList) {
			if(!ff.isReminder() && ff.getAuthorityEmail() != null) {
				ff.setReminder(true);
				fileRepo.save(ff);
			}
		}
	}


	@Override
	public ByteArrayOutputStream generatePDf() {
		Locale locale = Locale.forLanguageTag("en");
		Context context = new Context(locale);
		String content = tempEngine.process("MailFormat", context);
		
//		Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
//		
//		
//        PdfWriter writer = null;
//		try {
//			writer = PdfWriter.getInstance(document, out);
//		} catch (DocumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        StringBuilder htmlString = new StringBuilder();
//        htmlString.append(content);
//        document.open();
//        InputStream is = new ByteArrayInputStream(htmlString.toString().getBytes());
//        try {
//			XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        document.close();
		ITextRenderer renderer = new ITextRenderer();
	    renderer.setDocumentFromString(content);
	    renderer.layout();
	    try {
			renderer.createPDF(out);
		} catch (com.lowagie.text.DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return out;
	}


	@Override
	public void sendApplicationToFamily(Employee emp,String baseURL) {
		if(emp.getMembers() != null && emp.getMembers().size()>0) {
			Collections.sort(emp.getMembers(),new Comparator<Family>() {
				@Override
				public int compare(Family o1, Family o2) {
					return Integer.valueOf(o1.getAge()).compareTo(Integer.valueOf(o2.getAge()));
				}
			});
			Family f = emp.getMembers().get(0);
			Locale locale = Locale.forLanguageTag("en");
			Context context = new Context(locale);
			context.setVariable("authCode", emp.getAuthtoken());
			context.setVariable("name", f.getName());
			context.setVariable("baseURL", baseURL);
			context.setVariable("employeename", emp.getName());
			context.setVariable("employeeId", emp.getId());
			String content = tempEngine.process("SendToFamily", context);
			
			mailService.sendEmail(null, f.getEmail(), "Application form", content, 
					null, null, null);
			
		}		
	}


	@Override
	public void sendmailaftersavingapplicantsInfo(Applicants app, Employee emp) {
		
	}
}
