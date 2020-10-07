package hrms.Recruitment.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itextpdf.text.DocumentException;

import hrms.Recruitment.models.ApprovalAuthority;
import hrms.Recruitment.models.AuthorityConfig;
import hrms.Recruitment.models.CandidateDocumentsStatic;
import hrms.Recruitment.models.Category;
import hrms.Recruitment.models.Designation;
import hrms.Recruitment.models.FileModel;
import hrms.Recruitment.models.MpscCandidates;
import hrms.Recruitment.models.Proposal;
import hrms.Recruitment.models.ReservedCaste;
import hrms.Recruitment.models.Service_Book;
import hrms.Recruitment.repository.AuthorityConfigRepository;
import hrms.Recruitment.repository.CandidateDocumentsStaticRepository;
import hrms.Recruitment.repository.DesignationRepository;
import hrms.Recruitment.repository.FileModelRepository;
import hrms.Recruitment.repository.MpscCandidateRepository;
import hrms.Recruitment.repository.NoticeRepository;
import hrms.Recruitment.repository.ReservedCasteRepository;
import hrms.Recruitment.repository.ServiceBookRepository;
import hrms.Recruitment.security.SecurityUtils;
import hrms.Recruitment.service.DBoperation;

@Controller
public class MainController {

	 @PersistenceContext
     private EntityManager em;
	
	@Autowired
	private FileModelRepository fileModelRepo;
	
	@Autowired
	private MasterController masterControl;
	
	@Autowired
	private CandidateDocumentsStaticRepository candRepo;
	
	@Autowired
	private ServiceBookRepository ServiceRepo;
	
	@Autowired
	private AuthorityConfigRepository authorityConfigRepo;
	
	@Autowired
	private MpscCandidateRepository mpscRepo;

	@Autowired
	private DBoperation dbOp;
	
	@Autowired
	private NoticeRepository noticeRepo;
	
	@Autowired
	private DesignationRepository designationRepo;
	
//	@Autowired
//	private UserService userService;
	
	@Autowired
	private ReservedCasteRepository reservedCasteRepo;
		
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/selectDesignation") 
    public String selectDesgination(Model model) {
    	List<Designation> design = designationRepo.findAll();
    	Map<String,List<String>> designationMap = new HashMap<>();
    	for(Designation des : design) {
    		if(designationMap.containsKey(des.getService_group())) {
    			List<String> designation = designationMap.get(des.getService_group());
    			designation.add(des.getDesignation_name_en());
    			designationMap.put(des.getService_group(), designation);
    		} else {
    			List<String> designation = new ArrayList<>();
    			designation.add(des.getDesignation_name_en());
    			designationMap.put(des.getService_group(), designation);
    		}
    	}
		model.addAttribute("designationmap", designationMap);
		model.addAttribute("designation",new Designation());
		return "SelectDesignation";
    }
    
    @PostMapping("/newproposal") 
    public String postDesgination(Designation designation,Model model) {
    	System.out.println();
    	
    	List<Designation> design = designationRepo.findByDesignationName(designation.getDesignation_name_en());
    	List<Service_Book> serviceBook = ServiceRepo.findByR(String.valueOf(design.get(0).getDesignation_id()));    	
    	LocalDate today = LocalDate.now();
    	int i = 0;
    	for(Service_Book sb : serviceBook) {
    		if(today.isAfter(sb.getFromDate()) 
    				&& today.isEqual(sb.getFromDate()) 
    				&& today.isBefore(sb.getToDate()) 
    				&& today.isEqual(sb.getToDate())) {
    			i = serviceBook.indexOf(sb);
    			break;
    		}
    	}    	
		return newProposal(model,design.get(0).getDesignation_name_en(),serviceBook.get(i).getR_Vacancy());
    }
    
    
//    @GetMapping("/newproposal") 
    public String newProposal(Model model,String designationName,int Vacancy) {
    	Proposal notice = new Proposal();
    	List<AuthorityConfig> authorityConfig = authorityConfigRepo.findByCustom("MPSCA");
    	notice.setDesignation(designationName);
    	List<ApprovalAuthority> approvedList = new LinkedList<>();
    	for(AuthorityConfig authConfig : authorityConfig) {
    		ApprovalAuthority aa = new ApprovalAuthority();
    		aa.setAuthority(authConfig.getAuthority());
    		aa.setOptional(authConfig.isOptional());
    		approvedList.add(aa);
    	}
    	notice.setApprovalList(approvedList);
    	notice.setTotalvacancy(Vacancy);
//    	List<String> designation = new ArrayList<>();
//    	List<Designation> design = designationRepo.findAll();
    	List<ReservedCaste> reservedcate = new ArrayList<>();
    	reservedcate = reservedCasteRepo.findAllByOrderById();
//    	if(design != null) {
//    		design.forEach(s->{
//    			if(s.getDesignation_name_en() != null) {
//    				designation.add(s.getDesignation_name_en());
//    			}
//    		});
//    	}
//    	reservedcate = dbOp.getReservedCasteUnique(reservedcate);
    	List<Category> caste = new ArrayList<>();
    	caste = dbOp.getCategoryFromReservedCaste(reservedcate,notice.getTotalvacancy());    	
    	notice.setCategory(caste);
    	notice.setExpectedvacancy(notice.getTotalvacancy()-1);
    	notice.setReservedvacancy(1);
//		model.addAttribute("designation", designation);
		model.addAttribute("reservedcate", reservedcate);
		model.addAttribute("notice", notice);

    	return "index";
    }
    
    
    @ModelAttribute("designation") 
    public Designation getDesignation() {
    	return new Designation();
    }
    
    @ModelAttribute("notice")
    public Proposal getDepartments() {
    	return new Proposal();
    }
    
	
	  @ModelAttribute("candidate") 
	  	  public MpscCandidates getMpscCandidate() {
		  return new MpscCandidates(); 
	  }
	 
    
//    @PostMapping("noticeEdit")
//    public String editNotice(@ModelAttribute("notice") Proposal notice,BindingResult result,Model model) {
//    	if(notice != null && notice.getInfo() != null && notice.getInfo().length()>0) {
//    		notice = noticeRepo.save(notice);
//			model.addAttribute("notice", notice);
//    	}
//    	return getnotices(1, model);
//    }

    @GetMapping("/getmpscfile/{proposalId}") 
    public String getmpscfile(@PathVariable(required = false) String proposalId,MultipartFile file,Model model)	{
    	model.addAttribute("proposalId", proposalId);
    	return "importmpscfile";
    }
    
    @PostMapping("mpscfileupload/{proposalId}")
    public String uploadMPSC(@PathVariable(required = false) String proposalId
    		,@RequestParam("file") MultipartFile file,Model model, RedirectAttributes attributes) throws IOException {
    	try {
    	List<String> contentTypes = new ArrayList<>();
    	contentTypes.add("application/csv");
    	contentTypes.add("application/vnd.ms-excel");
    	contentTypes.add("application/excel");
    	contentTypes.add("application/x-excel");
    	contentTypes.add("application/x-msexcel");
    	contentTypes.add("application/vnd");
    	contentTypes.add("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    	if(file != null && file.getOriginalFilename() != null && file.getContentType() != null 
    			&& contentTypes.contains(file.getContentType())) {
    		List<MpscCandidates> tempStudentList = new ArrayList<>();
        	Proposal notice = noticeRepo.findById(Long.valueOf(proposalId)).get();
        	        	
        	
    	    XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
    	    XSSFSheet worksheet = workbook.getSheetAt(0);
    	    for(int i=1;i<worksheet.getPhysicalNumberOfRows() ;i++) {
    	        MpscCandidates tempStudent = new MpscCandidates();
    	            
    	        try {
	    	        XSSFRow row = worksheet.getRow(i);
	    	            
	    	        tempStudent.setMpsccandidatenumber((long)row.getCell(0).getNumericCellValue());
	    	        tempStudent.setMpsccandidatenumber((long)row.getCell(1).getNumericCellValue());
	    	        tempStudent.setRollnumber((long)row.getCell(2).getNumericCellValue());
	    	        tempStudent.setCandidatename(row.getCell(3).getStringCellValue());
	    	        tempStudent.setDesignation(row.getCell(4).getStringCellValue());
	    	        tempStudent.setApporoveddocument(row.getCell(5).getBooleanCellValue());
	    	        tempStudent.setCity(row.getCell(6).getStringCellValue());
	    	        tempStudent.setIdentitynumber(String.valueOf(row.getCell(7).getNumericCellValue()));
	    	        tempStudent.setOfferletter(row.getCell(8).getStringCellValue());    	        
	    	        tempStudentList.add(tempStudent);   
    	        } catch(Exception a) {
    	        	a.printStackTrace();
    	        	attributes.addFlashAttribute("message", "Please provice proper format of excel for uploading");
    	        	break;
    	        }
    	    }
//    	    mpscRepo.saveAll(tempStudentList);
    	    workbook.close();
    	    if(tempStudentList.size()>0 && tempStudentList.size()<= (notice.getTotalvacancy()+notice.getExtravacancy())) {
    	    	List<MpscCandidates> cadList = mpscRepo.findAll();
    	    	for(MpscCandidates cad : cadList) {
    	    		for(MpscCandidates cc : tempStudentList) {
    	    			if(cad.getMpsccandidatenumber() == cc.getMpsccandidatenumber()) {
    	    	    		attributes.addFlashAttribute("message", "Mpsc Candidate number should be unique.");
    	    	        	return "redirect:/getmpscfile/"+proposalId;
    	    			}
    	    		}
    	    	}
    	    	
    	    	if(notice.getProposal_MpscCandidate() != null) {
    	    		notice.getProposal_MpscCandidate().addAll(tempStudentList);
    	    	} else {
    	    		notice.setProposal_MpscCandidate(tempStudentList);
    	    	}
    	    	try {
    	    		notice = noticeRepo.save(notice);
    	    	} catch(Exception a) {
    	    		attributes.addFlashAttribute("message", "Roll number and Mpsc Candidate number should be unique.");
    	        	return "redirect:/getmpscfile/"+proposalId;
    	    	}
    	    } else {
    	    	attributes.addFlashAttribute("message", "Import data exceed total vacancy of proposal.");
    	    }
    	    workbook.close();
    		attributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + '!');
    		
    	} else {
//			ObjectError error = new ObjectError("fileexcel", "File should be excel format.");
//			result.addError(error);
    		attributes.addFlashAttribute("message", "Please select a file to upload in excel format");
    	}
    	} catch(Exception a) {
    		attributes.
    		addFlashAttribute("message", "Something went wrong with File upload format. Please verify format before uploading");
        	return "redirect:/getmpscfile/"+proposalId;    		
    	}
    	return "redirect:/getmpscfile/"+proposalId;
    }
    
    @PostMapping("editcandidate")
    public String editCandidate(@ModelAttribute("candidate") @Valid MpscCandidates mpscCandidate,BindingResult result,Model model,
    		@RequestParam(value="file",required = false) MultipartFile file,
    		@RequestParam(value="action", required=false) String action) throws IOException {
    	if(result.hasErrors()) {
    		List<FileModel> files = new ArrayList<>();
			model.addAttribute("candidate", mpscCandidate);
			model.addAttribute("files",files);
			return "CandidateDetails";
    	}
    	MpscCandidates candiDateCopy = mpscRepo.findById(mpscCandidate.getId()).get();
    	if(mpscCandidate.getDocId() != null) {
    		if(candiDateCopy.getFiles() == null || candiDateCopy.getFiles().size()==0) {
    			List<FileModel> files = new ArrayList<>();
    			mpscCandidate.setFiles(files);
    		} else {
    			for(FileModel fileMode : candiDateCopy.getFiles()) {
    				for(FileModel ff : mpscCandidate.getFiles()) {
    					if(fileMode.getFileName().equals(ff.getFileName())) {
    						fileMode.setVerified(ff.isVerified());
    					}
    				}
    			}
    			mpscCandidate.setFiles(candiDateCopy.getFiles());
    		}
    		if(file != null && file.getContentType() != null && file.getContentType().equalsIgnoreCase("application/pdf")) {
    			FileModel fileModel = null;
    			boolean fileCheckFound  = false;
    			for(FileModel fileMode:mpscCandidate.getFiles()) {
    				if(fileMode.getFileName().equalsIgnoreCase(mpscCandidate.getFileName())) {
    					fileCheckFound = true;
    					if(file.getBytes().length>0 && file.getOriginalFilename().length()>0) {
    						fileMode.setData(file.getBytes());
    					}
    					break;
    				}
    			}
    			if(!fileCheckFound) {
    			fileModel = new FileModel();
    				fileModel.setDistrict(mpscCandidate.getCity().toUpperCase());
        			fileModel.setFileName(mpscCandidate.getFileName());
        			fileModel.setData(file.getBytes());
					fileModel.setContentType(file.getContentType());
        			CandidateDocumentsStatic cc = candRepo.findByName(mpscCandidate.getFileName());
        			if(cc != null) {
        				if(cc.getAuthorityName() != null && !cc.getAuthorityName().equalsIgnoreCase("District")) {
            				if(cc.getAuthorityName() != null) {
            					fileModel.setAuthorityName(cc.getAuthorityName());
            				}
            				if(cc.getAuthorityEmail() != null && !cc.getAuthorityName().equalsIgnoreCase("District")) {
            					fileModel.setAuthorityEmail(cc.getAuthorityEmail());
            				}        					
        				} else {
        					String districtEmail = dbOp.getEmailByDistrict(fileModel.getDistrict());
        					if(districtEmail.length()>0) {
        						String [] ss = districtEmail.split("[_]");
        						if(fileModel.getFileName().equalsIgnoreCase("Medical Certificate")) {
        							fileModel.setAuthorityEmail(ss[0]);
        						}
        						if(fileModel.getFileName().equalsIgnoreCase("Police Verification Certificate")) {
        							fileModel.setAuthorityEmail(ss[1]);
        						}
        					}
        				}
        			}
        			mpscCandidate.getFiles().add(fileModel);
    			}
    		}
    		mpscCandidate = mpscRepo.save(mpscCandidate);
    	}    	
    	return "redirect:/candidateslist/page/1";
    }
    
    
    @PostMapping("registernotice")
    public String registerNotice(@ModelAttribute("notice") @Valid Proposal notice
    		,BindingResult result,Model model,@RequestParam("FileUpload1") MultipartFile file, 
    		@RequestParam(value="action", required=false) String action) throws IOException {
    	notice.setRemoteIp(((WebAuthenticationDetails)SecurityContextHolder
    			.getContext().getAuthentication().getDetails()).getRemoteAddress());
    	List<AuthorityConfig> authorityConfig = authorityConfigRepo.findByCustom("MPSCA");
    	List<ApprovalAuthority> approvedList = new LinkedList<>();
    	String authority = SecurityUtils.getAuthority();

    	for(AuthorityConfig authConfig : authorityConfig) {
    		ApprovalAuthority aa = new ApprovalAuthority();
    		aa.setAuthority(authConfig.getAuthority());
    		aa.setOptional(authConfig.isOptional());
    		approvedList.add(aa);
    	}
    	notice.setApprovalList(approvedList);

    	if(result.hasErrors()) {
    		return returnIndexFailValidation(model,notice);
    	}
    	if(!checkvacancyvalidation(notice)) {
			ObjectError error = new ObjectError("Vacancy Validate", "Please add propar vacancy in form againt total vacancy.");
			result.addError(error);    		
    		return returnIndexFailValidation(model,notice);
    	}
    	
    	
    	if(action != null && action.equalsIgnoreCase("save")) {
    		Proposal noticeCopy = null;
    		if(notice.getId() != null) {
				noticeCopy = noticeRepo.findById(notice.getId()).get();
				notice.setApprovalList(noticeCopy.getApprovalList());
				if(file.getOriginalFilename() != null && file.getOriginalFilename().length()>0 
						&& file.getContentType().equals("application/pdf")) {
					notice.setFilename(file.getOriginalFilename());
					notice.setData(file.getBytes());
				} else {
					notice.setFilename(noticeCopy.getFilename());
					notice.setData(noticeCopy.getData());
				}
				notice.setViewProposal(true);
				notice.setCreatedBy(noticeCopy.getCreatedBy());
				notice.setCreatedDate(noticeCopy.getCreatedDate());
//				notice.setApproved(noticeCopy.isApproved());
				notice.setApprovalList(setApprovedAuth(noticeCopy, authority, notice.isApproved()).getApprovalList());
				if(authority == "VAC") {
					notice.setFinalApproved(notice.isApproved());
				} else {
					notice.setFinalApproved(finalApproval(notice));
				}
  				if(noticeCopy.getCategory() != null && noticeCopy.getCategory().size()>0) {
					for(Category c : noticeCopy.getCategory()) {
						for(Category cc : notice.getCategory()) {
							if(c.getCategory_Code().equals(cc.getCategory_Code()) && c.getType().equals(cc.getType())) {
								cc.setId(c.getId());
							}
						}
					}
				}
    		}
        	if(notice.getFilename() == null && file.getOriginalFilename().length()==0) {
    			ObjectError error = new ObjectError("filename", "File not found for type PDF. Please select file.");
    			result.addError(error);
        		return returnIndexFailValidation(model,notice);
        		
        	}
        	if((notice != null 
        			&& ((notice.getFilename() != null && notice.getData() != null) 
        					|| (file.getContentType() != null && file != null 
        					&& file.getContentType().equalsIgnoreCase("application/pdf"))))) {
        			if(notice.getFilename() == null) {
        				notice.setFilename(file.getOriginalFilename());
        				notice.setData(file.getBytes());
        			}
//        	    	User user = userService.findByEmail(userService.getCurrentUser().getUsername());
//    				if(notice.getCategory().size()==4 && notice.isApproved()) {
//    					notice.getCategory().addAll(categoryListHori(notice.getCategory()));
 //   				}
    				
        	    	
        			notice.setProcessType("MPSCA");
        			notice.setAuthority(authority);
        			notice.setRemoteIp(((WebAuthenticationDetails)SecurityContextHolder
        					.getContext().getAuthentication().getDetails()).getRemoteAddress());
        			notice = noticeRepo.save(notice);

        			model.addAttribute("notice", notice);
        	} else {
    			ObjectError error = new ObjectError("filename", "File not found for type pdf.Please select file.");
    			result.addError(error);
        		return returnIndexFailValidation(model,notice);
        	}    		
    	} else if(action != null && action.equalsIgnoreCase("auditingauthority")) {
    		if(notice != null && notice.getId()>0) {
       			Proposal noticeCopy = noticeRepo.findById(notice.getId()).get();
    				notice.setFilename(noticeCopy.getFilename());
    				notice.setApprovalList(noticeCopy.getApprovalList());
    				notice.setData(noticeCopy.getData());
    				notice.setCreatedBy(noticeCopy.getCreatedBy());
    				notice.setCreatedDate(noticeCopy.getCreatedDate());
    				notice.setApprovalList(
    						setApprovedAuth(noticeCopy, noticeCopy.getAuthority(), notice.isApproved()).getApprovalList());
//    				notice.setFinalApproved(finalApproval(notice));
    				if(authority == "VAC") {
    					notice.setFinalApproved(notice.isApproved());
    				} else {
    					notice.setFinalApproved(finalApproval(notice));
    				}
     				if(noticeCopy.getCategory() != null && noticeCopy.getCategory().size()>0) {
    					for(Category c : noticeCopy.getCategory()) {
    						for(Category cc : notice.getCategory()) {
    							if(c.getCategory_Code().equals(cc.getCategory_Code()) && c.getType().equals(cc.getType())) {
    								cc.setId(c.getId());
    							}
    						}
    					}
    				}
    			notice.setAuthority("AA");
    			notice.setProcessType("MPSCA");
    			notice.setViewProposal(false);
    			noticeRepo.save(notice);
    			model.addAttribute("message", "Appointing Authority");
    			return "sendto";
    		}
    	} else if(action != null && action.equalsIgnoreCase("sendbackdept")) {
    		if(notice != null && notice.getId()>0) {
    			Proposal noticeCopy = noticeRepo.findById(notice.getId()).get();
				notice.setApprovalList(noticeCopy.getApprovalList());
				notice.setFilename(noticeCopy.getFilename());
				notice.setData(noticeCopy.getData());
				notice.setCreatedBy(noticeCopy.getCreatedBy());
				notice.setCreatedDate(noticeCopy.getCreatedDate());
				notice.setApprovalList(setApprovedAuth(noticeCopy, authority, notice.isApproved()).getApprovalList());
				if(authority == "VAC") {
					notice.setFinalApproved(notice.isApproved());
				} else {
					notice.setFinalApproved(finalApproval(notice));
				}
 				if(noticeCopy.getCategory() != null && noticeCopy.getCategory().size()>0) {
					for(Category c : noticeCopy.getCategory()) {
						for(Category cc : notice.getCategory()) {
							if(c.getCategory_Code().equals(cc.getCategory_Code()) && c.getType().equals(cc.getType())) {
								cc.setId(c.getId());
							}
						}
					}
				}
    			notice.setAuthority("DEP");
    			notice.setProcessType("MPSCA");
    			notice.setViewProposal(false);
    			noticeRepo.save(notice);
    			model.addAttribute("message", "Department");
    			return "sendto";
    		}
    	} else if(action != null && action.equalsIgnoreCase("sendto16b")) {
    		if(notice != null && notice.getId()>0) {
       			Proposal noticeCopy = noticeRepo.findById(notice.getId()).get();
    				notice.setFilename(noticeCopy.getFilename());
    				notice.setApprovalList(noticeCopy.getApprovalList());
    				notice.setData(noticeCopy.getData());
    				notice.setCreatedBy(noticeCopy.getCreatedBy());
    				notice.setCreatedDate(noticeCopy.getCreatedDate());
    				notice.setApprovalList(setApprovedAuth(noticeCopy, authority, notice.isApproved()).getApprovalList());
    				if(authority == "VAC") {
    					notice.setFinalApproved(notice.isApproved());
    				} else {
    					notice.setFinalApproved(finalApproval(notice));
    				}
     				if(noticeCopy.getCategory() != null && noticeCopy.getCategory().size()>0) {
    					for(Category c : noticeCopy.getCategory()) {
    						for(Category cc : notice.getCategory()) {
    							if(c.getCategory_Code().equals(cc.getCategory_Code()) && c.getType().equals(cc.getType())) {
    								cc.setId(c.getId());
    							}
    						}
    					}
    				}
    			notice.setAuthority("16B");
    			notice.setProcessType("MPSCA");
    			model.addAttribute("message", "16B");
    			notice.setViewProposal(false);
    			noticeRepo.save(notice);
    			System.out.println();
    			return "sendto";
    		}
    	} else if(action != null && action.equalsIgnoreCase("sendtovac")) {
     		if(notice != null && notice.getId()>0) {
       			Proposal noticeCopy = noticeRepo.findById(notice.getId()).get();
    				notice.setFilename(noticeCopy.getFilename());
    				notice.setApprovalList(noticeCopy.getApprovalList());
    				notice.setData(noticeCopy.getData());
    				notice.setCreatedBy(noticeCopy.getCreatedBy());
    				notice.setCreatedDate(noticeCopy.getCreatedDate());
    				notice.setApprovalList(setApprovedAuth(notice, authority, notice.isApproved()).getApprovalList());
    				if(authority == "VAC") {
    					notice.setFinalApproved(notice.isApproved());
    				} else {
//    					notice.setFinalApproved(finalApproval(notice));
    				}
     				if(noticeCopy.getCategory() != null && noticeCopy.getCategory().size()>0) {
    					for(Category c : noticeCopy.getCategory()) {
    						for(Category cc : notice.getCategory()) {
    							if(c.getCategory_Code().equals(cc.getCategory_Code()) && c.getType().equals(cc.getType())) {
    								cc.setId(c.getId());
    							}
    						}
    					}
    				}
     			notice.setAuthority("VAC");
    			notice.setProcessType("MPSCA");
    			model.addAttribute("message", "VAC");
    			notice.setViewProposal(false);
     			noticeRepo.save(notice);
     			return "sendto";
     		}
     	} else if(action != null && action.equalsIgnoreCase("sendtompsc")) {
     		if(notice != null && notice.getId()>0) {
       			Proposal noticeCopy = noticeRepo.findById(notice.getId()).get();
     			noticeCopy.setOfflineImportEnable(true);
     			noticeRepo.save(noticeCopy);
    			model.addAttribute("message", "MPSC");

     			return "sendto";
     		}
     	}

    	return masterControl.root(model);
    }
    
	/*
	 * @GetMapping("/getCandidateDetail") public String getCandidateDetail(Model
	 * model) { return "CandidateDetails"; }
	 */
    
    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/user")
    public String userIndex() {
        return "user/index";
    }
    
    @GetMapping("/candidateslist/page/{page}")
    public String getCandidateList(@PathVariable("page") int page,Model model) {
    	if(page<0) {
    		page = 1;
    	}
    	PageRequest pageable = PageRequest.of(page - 1, 5, Sort.by(Sort.Direction.ASC,"id"));
    	Page<MpscCandidates> noticePages = mpscRepo.findAll(pageable);
		 int totalPages = noticePages.getTotalPages();
	     if(totalPages > 0) {
	         List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
	         model.addAttribute("pageNumbers", pageNumbers);
	     }
    	model.addAttribute("candidatelist", noticePages.getContent());    	
    	
    	return "candidatelist";
    }
    
    @GetMapping("/inbox/page/{page}")
    public String getnotices(@PathVariable("page") int page,@RequestParam(name="filter",required = false) String filter,Model model) {
    	if(page<0) {
    		page = 1;
    	}
    	String authority = SecurityUtils.getAuthority();    	
    	Pageable pageable = PageRequest.of(page - 1, 5, Sort.by(Sort.Direction.ASC,"id"));
    	Page<Proposal> noticePages = null;
    	if(filter != null && filter.equalsIgnoreCase("ALL")) {
    		noticePages = noticeRepo.findAll(pageable);
    	} else {
    		noticePages = noticeRepo.findByAuthority(authority,pageable);
    	}
		 int totalPages = noticePages.getTotalPages();
	     if(totalPages > 0) {
	         List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
	         model.addAttribute("pageNumbers", pageNumbers);
	     }
    	model.addAttribute("noticelist", noticePages.getContent());    	
    	return "inbox";
    }
    
    @GetMapping("editCandidateDetail/{candidateId}")
    public String editCandidateDetail(@PathVariable("candidateId") int candidateId,Model model) {
    	if(candidateId != 0)  {
    		MpscCandidates candidate = mpscRepo.findById((long) candidateId).get();
//    		List<FileModel> files = fileModelRepo.findByMpscCandidates_Id((long)candidateId);
    		List<CandidateDocumentsStatic> candStatic = candRepo.findAll();
    		List<FileModel> files = new ArrayList<>();
    		if(candidate.getFiles() != null) {
    			files = candidate.getFiles();
    		}
    		if(candidate != null) {
    			model.addAttribute("candidate", candidate);
    			model.addAttribute("files",files);
    			model.addAttribute("candDocs", candStatic);
    		}
    	}
    	return "CandidateDetails";
    }
    
    @GetMapping("/createproposal/{noticeId}")
    public String createProposal(@PathVariable("noticeId") int noticeId,Model model) {
//    	List<String> designation = new ArrayList<>();
//    	List<Designation> design = designationRepo.findAll();
//    	if(design != null) {
//    		design.forEach(s->{
//    			if(s.getDesignation_name_en() != null) {
//    				designation.add(s.getDesignation_name_en());
//    			}
//    		});
//    	}
    	
    	
    	Proposal notice = noticeRepo.findById((long)noticeId).get();
    	notice.setViewProposal(true);
    	noticeRepo.save(notice);
    	String authority = SecurityUtils.getAuthority();
    	if(!notice.getAuthority().equalsIgnoreCase(authority)) {
    		return "redirect:/inbox/page/1";
    	}
    	notice.setInfo("");
    	Collections.sort(notice.getCategory(),new Comparator<Category>() {

			@Override
			public int compare(Category o1, Category o2) {
				return Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId()));
			}
		});
    	List<Proposal> proposalList = new ArrayList<>();
		if(notice.getId() != null) {
			@SuppressWarnings("unchecked")
			List<Proposal> proposals = (List<Proposal>) dbOp.getAuditReader().createQuery()
	                .forRevisionsOfEntity(Proposal.class, true, true)
	                .add(AuditEntity.property("id").eq(notice.getId()))
	                .getResultList();
			System.out.println();
			proposalList.addAll(proposals);
		}
		proposalList.forEach(s->{
			if(s.getData() != null) {
				if(s.getInfo() == null) {
					s.setInfo("");
				}
				s.setDataurl("data:application/pdf;base64,"+Base64.getEncoder().encodeToString(s.getData()));
			}			
		});

    	List<ReservedCaste> reservedcate = new ArrayList<>();
    	reservedcate = reservedCasteRepo.findAllByOrderById();
    	if(notice.getCategory().size() < reservedcate.size()) {
//    		for(int i=(notice.getCategory().size()-1);i<reservedcate.size();i++) {
//    			Category c = new Category();
//    			c.setCategory_Code(reservedcate.get(i).getCasteCode());
//    			c.setCategory_Name(reservedcate.get(i).getCasteName());
//    			c.setPercentage_Reserved(reservedcate.get(i).getPercentageReserved());
//    			c.setType(reservedcate.get(i).getType());
//    			c.setVacancy(0);
//    			c.setId(0);
//    			notice.getCategory().add(c);
//    		}
    		List<Category> cate = dbOp.getCategoryFromReservedCaste(reservedcate, 100);
			for(Category c : notice.getCategory()) {
				for(Category cc : cate) {
					if(c.getCategory_Code().equals(cc.getCategory_Code()) && c.getType().equals(cc.getType())) {
						cc.setId(c.getId());
					}
				}
			}
			notice.setCategory(cate);
			notice.setViewProposal(true);
    	}
    	if(notice != null) {
    		model.addAttribute("notice", notice);
//    		model.addAttribute("designation", designation);
    		model.addAttribute("reservedcate", reservedcate);
    		model.addAttribute("proposalList", proposalList);
    	}
    	
    	return "proposal";
    }
    
    @GetMapping("/pdf/{noticeid}") 
    public void getff(@PathVariable String noticeid,HttpServletResponse response) throws IOException {
    	 response.setContentType("application/pdf");
 		Proposal notice = noticeRepo.findById(Long.valueOf(noticeid)).get();
   	 response.setHeader("Content-Disposition","inline; filename=Accepted.pdf");

    	    InputStream inputStream = new ByteArrayInputStream(notice.getData());
    	    int nRead;
    	    while ((nRead = inputStream.read()) != -1) {
    	        response.getWriter().write(nRead);
    	    }
    	 
    }
    
    @GetMapping("/generatePDF/{noticeId}/{random}") 
    public ResponseEntity<?> getFile(@PathVariable String noticeId,@PathVariable String random) throws DocumentException {
    	
    	Proposal notice = null;
    	if(noticeId != null) {
    		notice = noticeRepo.findById(Long.valueOf(noticeId)).get();
    	}
    	HttpHeaders headers = new HttpHeaders();
    	if(random != null && random.equalsIgnoreCase("html")) {
            headers.add("Content-Disposition", "inline; filename=HTMLPreview_"+noticeId+".pdf");
    		
    	} else if(random != null && random.equalsIgnoreCase("attach")) {
            headers.add("Content-Disposition", "inline; filename="+notice.getFilename()+"_"+noticeId+".pdf");    		
    	}

        
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(new ByteArrayInputStream(dbOp.getPDffile(noticeId,random).toByteArray())));
    }
    
    @GetMapping("/downloadDoc/{docId}/{fileName}") 
    public ResponseEntity<?> getCandidateFile(@PathVariable String docId,@PathVariable String fileName) throws DocumentException {
    	
    	FileModel f = fileModelRepo.findByIdAndFileName(Long.valueOf(docId),fileName);
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename="+f.getFileName()+".pdf");
    		

        
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(new ByteArrayInputStream(f.getData())));
    }
    @GetMapping("/generateAppointment")
    public String getOfferLetter(Model model) {
		
		  List<MpscCandidates> candidates = mpscRepo.findByVerified(true);
		  if(candidates != null && candidates.size()>0) { 
			  StringBuffer buff = new StringBuffer(); 
			  buff.append("<table><tr>"
			  		+ "<th>Index</th>"
			  		+ "<th>Candidate Name</th> "
			  		+ "<th>Candidate Caste</th>"
			  		+ "<th>Require Cast</th>"
			  		+ "<th>City</th>"
			  		+ "<th>Selected City</th>"
			  		+ "<th>Selected Address</th>"
			  		+ "<th>Designation City</th></tr>"); 
			  for(MpscCandidates cad : candidates) {
				  if(cad.isVerified()) {
	 				  buff.append("<tr>"
					  		+ "<td>"+cad.getId()+"</td>"
					  				+ "<td>"+cad.getCandidatename()+"</td>"
					  						+ "<td>Open</td><td>Open</td>"
					  						+ "<td>"+cad.getCity()+"</td>"
					  								+ "<td>"+cad.getCity()+"</td>"
					  								+"<td>Address abcdfg</td><td>"+cad.getCity()+"</td></tr>");
				  }
			  }
			  buff.append("</table>");
			  model.addAttribute("tableContent",buff.toString());
		  }
		 
    	return "GenerateAppointmentLetter";
    } 
    
    public String returnIndexFailValidation(Model model,Proposal notice) {
    	List<ReservedCaste> reservedcate = new ArrayList<>();
    	reservedcate = reservedCasteRepo.findAllByOrderById();
//    	reservedcate = dbOp.getReservedCasteUnique(reservedcate);
//    	List<String> designation = new ArrayList<>();
    	List<AuthorityConfig> authorityConfig = authorityConfigRepo.findByCustom("MPSCA");
    	List<ApprovalAuthority> approvedList = new LinkedList<>();
    	for(AuthorityConfig authConfig : authorityConfig) {
    		ApprovalAuthority aa = new ApprovalAuthority();
    		aa.setAuthority(authConfig.getAuthority());
    		aa.setOptional(authConfig.isOptional());
    		approvedList.add(aa);
    	}
    	notice.setApprovalList(approvedList);

//    	List<Designation> design = designationRepo.findAll();
//    	if(design != null) {
//    		design.forEach(s->{
//    			if(s.getDesignation_name_en() != null) {
//    				designation.add(s.getDesignation_name_en());
//    			}
//    		});
//    	}


		model.addAttribute("notice", notice);
		model.addAttribute("reservedcate", reservedcate);
//		model.addAttribute("designation", designation);
		return "index";

    }
    
    public List<Category> categoryListHori(List<Category> cVertical) {
    	List<Category> cateHori = new ArrayList<>();
//    	int maxIndex = cVertical.size()-1;
    	for(Category c : cVertical) {
 //   		maxIndex++;
    		Category cc = new Category();
    		cc.setCategory_Code(c.getCategory_Code());
    		cc.setCategory_Name(c.getCategory_Name());
 //   		cc.setId(maxIndex);
    		cc.setPercentage_Reserved(c.getPercentage_Reserved());
    		cc.setType("H");
    		cc.setVacancy(0);
    		cateHori.add(cc);
    	}
    	return cateHori;
    }
    
    public boolean checkvacancyvalidation(Proposal notice) {
    	int totalCateVacan = 0;
    	if(notice.getCategory() != null) {
    		for(Category c : notice.getCategory()) {
    			totalCateVacan += c.getVacancy();    			
    		}
    		
    	}
    	return (notice.getTotalvacancy() == totalCateVacan); 
    }    
    
    public Proposal setApprovedAuth(Proposal proposal,String authority,boolean approved) {
    	if(proposal.getApprovalList() != null) {
    		for(ApprovalAuthority approvAuth : proposal.getApprovalList()) {
    			if(approvAuth.getAuthority().equalsIgnoreCase(authority)) {
    				approvAuth.setApproved(approved);
    			}
    		}
    	}
    	return proposal;
    }
    
    public boolean finalApproval(Proposal proposal) {
    	List<Boolean> boolList= new ArrayList<>();
    	proposal.getApprovalList().forEach(s->{    		
    		if(!s.isOptional()) {
    			boolList.add(s.isApproved());
    		}
    	});
    	return !boolList.contains(false);
    }
    
    
    @GetMapping("/sendReminder/{docId}/{fileName}/{candidateId}") 
    public String sendReminder(@PathVariable String docId,@PathVariable String fileName,@PathVariable String candidateId,
    		RedirectAttributes redirect) {    	
    	FileModel f = fileModelRepo.findByIdAndFileName(Long.valueOf(docId),fileName);
    	if(f.getAuthorityEmail() != null) {
    		dbOp.sendReminder(f);
    		redirect.addFlashAttribute("message", "Reminder sent successfully");
    	} else {
    		redirect.addFlashAttribute("message", "Authority Email not Found");
    	}
    	return "redirect:/editCandidateDetail/"+candidateId;
    }
}
