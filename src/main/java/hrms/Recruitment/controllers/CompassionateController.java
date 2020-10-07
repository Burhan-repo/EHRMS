package hrms.Recruitment.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hrms.Recruitment.constants.ApplicationType;
import hrms.Recruitment.models.Applicants;
import hrms.Recruitment.models.ApprovalAuthority;
import hrms.Recruitment.models.AuthorityConfig;
import hrms.Recruitment.models.Degree;
import hrms.Recruitment.models.Employee;
import hrms.Recruitment.models.ReasonMaster;
import hrms.Recruitment.repository.ApplicantsRepository;
import hrms.Recruitment.repository.AuthorityConfigRepository;
import hrms.Recruitment.repository.DegreeRepository;
import hrms.Recruitment.repository.EmployeeRepository;
import hrms.Recruitment.repository.ReasonMasterRepository;
import hrms.Recruitment.security.SecurityUtils;
import hrms.Recruitment.service.DBoperation;

@Controller
public class CompassionateController {
	
	@Autowired
	private EmployeeRepository EmployeeRepo;
		
	@Autowired
	private ApplicantsRepository applicantRepo;
		
	@Autowired
	private DegreeRepository degreeRepo;
	
	@Autowired
	private AuthorityConfigRepository authorityConfigRepo;
	
	@Autowired
	private MessageSource messageSource;
		
	@Autowired
	private LocaleResolver loc;
	
	@Autowired
	private ReasonMasterRepository reasonRepo;
	
//	@Autowired
//	private FamilyRepository famRepo;
	
	@Autowired
	private DBoperation dbOps;
	
	@GetMapping("/closeservicebook/page/{page}")
	public String getEmployeeList(@PathVariable("page") int page,@ModelAttribute("employee") Employee employee,BindingResult result,Model model,
			@RequestParam(name = "status",required = false) String status,
			@RequestParam(name="employeeId",required = false) String employeeId) {
		if(status == null) {
	    	if(page<0) {
	    		page = 1;
	    	}
	    	Pageable pageable = PageRequest.of(page - 1, 5, Sort.by(Sort.Direction.ASC,"id"));			
//			Page<Employee> employeeList = new ArrayList<>();
			Page<Employee> employeeList = EmployeeRepo.findAll(pageable);
			 int totalPages = employeeList.getTotalPages();
		     if(totalPages > 0) {
		         List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
		         model.addAttribute("pageNumbers", pageNumbers);
		     }
	    	model.addAttribute("employeeList", employeeList.getContent());    	
			model.addAttribute("employeeListEnable", true);
		}
		return "CompassionateRecruitment";
	}

	@GetMapping("/editemployeedetail/{id}")
	public String employeeDetail(@PathVariable String id,Model model) {
		Employee emp = EmployeeRepo.findById(Long.valueOf(id)).get();
		List<ReasonMaster> reasonList = reasonRepo.findAll();
		model.addAttribute("employee", emp);
		model.addAttribute("reasonList", reasonList);
		model.addAttribute("employeeDetailEnable", true);		
		return "EmployeeDetail";
	}
	
	
	@PostMapping("/editemployeedetail")
	public String editEmployee(@ModelAttribute("employee") @Valid Employee employee,
			BindingResult result,Model model,RedirectAttributes redirect,MultipartFile file,HttpServletRequest req) throws IOException {
		List<ReasonMaster> reasonList = reasonRepo.findAll();
		model.addAttribute("reasonList", reasonList);
		if(result.hasErrors()) {
			return "EmployeeDetail";
		}			
		if(file != null && file.getOriginalFilename().length()>0 && file.getContentType().equalsIgnoreCase("application/pdf")) {
			Employee ee = EmployeeRepo.findById(employee.getId()).get();			
			ee.setFileName(file.getOriginalFilename());
			ee.setData(file.getBytes());
			ee.setContentType(file.getContentType());
			ee.setDateOfClose(employee.getDateOfClose());
			ee.setInformation(employee.getInformation());
			ee.setReasonCloseBook(employee.getReasonCloseBook());
			try {
				employee = EmployeeRepo.save(ee);
			} catch(Exception a) {
				model.addAttribute("message", "Something went wrong in saving");
			}
			System.out.println();
		} else {
			model.addAttribute("fileNameNotFound", messageSource.getMessage("fileNameNotFound", null,loc.resolveLocale(req)));
		}
		model.addAttribute("message", "Saved Successfully");
		return "EmployeeDetail";
	}
	
	@GetMapping("/generateApplicationForm/{employeeId}")
	public String getApplicationFrom(@PathVariable String employeeId,RedirectAttributes redirect,
			HttpServletRequest request,HttpServletResponse response) {
		if(employeeId != null) {
			Employee emp = EmployeeRepo.findById(Long.valueOf(employeeId)).get();
			if(emp.getAuthtoken() != null) {
				redirect.addFlashAttribute("message", "Application Form already generated");
			} else {
				String UUID_Employee = UUID.randomUUID().toString();
				emp.setAuthtoken(UUID_Employee);
				emp.setInformedFamily(true);
				String baseUrl = request.getScheme() +
					    "://" +
					    request.getServerName() +
					    ":" +
					    request.getServerPort() +
					    request.getContextPath();
				dbOps.sendApplicationToFamily(emp, baseUrl);
				EmployeeRepo.save(emp);
				redirect.addFlashAttribute("message", "Application Form generated with GUID and sent to respective Employee's memeber");
			}
		}
		return "redirect:/closeservicebook/page/1";
	}
	
	@GetMapping("/getApplicationForm/{authCode}/{empId}")
	public String getApplicationForm(@PathVariable String authCode,@PathVariable String empId,Model model,RedirectAttributes redirect) {
		if(empId != null && authCode != null) {
			Employee emp = EmployeeRepo.findById((Long.valueOf(empId))).get();
			List<Degree> degreeList = degreeRepo.findAll();
			if(emp.getAuthtoken().equalsIgnoreCase(authCode)) {
				Applicants a = null;
				if(emp.getApplicants() != null && emp.getApplicants().getId() != null 
						&& applicantRepo.existsById(emp.getApplicants().getId())) {
					a = applicantRepo.findById(emp.getApplicantsId()).get();
					if(a.getData() != null && a.getData().length>0) {
						a.setDataurl("data:application/pdf;base64,"+Base64.getEncoder().encodeToString(a.getData()));
					}
					if(SecurityUtils.isAuthenticated()) {
						a.setView(true);
						emp.setApplicants(a);
						emp = EmployeeRepo.save(emp);
					}
				} else {
					a = new Applicants();
					a.setApplicationType(ApplicationType.NEW.toString());
					a.setEmployeeId(String.valueOf(emp.getId()));
					a.setEmployee(emp);
			    	List<AuthorityConfig> authorityConfig = authorityConfigRepo.findByCustom(a.getProcessType());
			    	List<ApprovalAuthority> approvedList = new LinkedList<>();
			    	for(AuthorityConfig authConfig : authorityConfig) {
			    		ApprovalAuthority aa = new ApprovalAuthority();
			    		aa.setAuthority(authConfig.getAuthority());
			    		aa.setOptional(authConfig.isOptional());
			    		approvedList.add(aa);
			    	}
			    	a.setApprovalList(approvedList);
					a = applicantRepo.save(a);
					emp.setApplicants(a);
					emp.setApplicantsId(a.getId());
					emp = EmployeeRepo.save(emp);
					a = emp.getApplicants();
					System.out.println();
				}
				model.addAttribute("applicants", a);				
				model.addAttribute("familyList", emp.getMembers());
				model.addAttribute("employeeId", emp.getId());
				model.addAttribute("degreeList", degreeList);
				return "GenerateApplicationForm";
			}
		}
		return "redirect:/";
	}
	
	@PostMapping("/registerApplicants")
	public String registerApplicants(@ModelAttribute("applicants") @Valid Applicants applicants, BindingResult result,
			Model model,RedirectAttributes redirect,MultipartFile file,HttpServletRequest req,
			@RequestParam(value="action", required=false) String action) throws IOException {
		Applicants app = applicantRepo.findById(applicants.getId()).get();
		Employee emp = EmployeeRepo.findById(app.getEmployee().getId()).get();
		if(SecurityUtils.isAuthenticated()) {
			app.setRemoteIp(((WebAuthenticationDetails)SecurityContextHolder
    			.getContext().getAuthentication().getDetails()).getRemoteAddress());
		}
		if(result.hasErrors()) {
			return "ApplicationForm";
		}
		if(applicants != null && applicants.getId() != null) {
//			Applicants app = applicantRepo.findById(applicants.getId()).get();
			app.setView(false);
			List<Degree> degreeList = degreeRepo.findAll();
			if(app.getFileName() != null && app.getFileName().length()>0) {
				if(file != null && file.getOriginalFilename().length()>0 			
						&& file.getContentType().equalsIgnoreCase("application/pdf")){
					app.setFileName(file.getOriginalFilename());
					app.setData(file.getBytes());
					app.setGroup(applicants.getGroup());
					app.setFamilyId(applicants.getFamilyId());
					app.setApplicationType(applicants.getApplicationType());
					app.setQualification(applicants.getQualification());
					if(SecurityUtils.isAuthenticated()) {
						app.setApproved(applicants.isApproved());
						app.setApprovalList(getApprovalList(app.getApprovalList(), SecurityUtils.getRole(), app.isApproved()));
						app.setRemarks(applicants.getRemarks());
						app.setFinalApproved(finalApproval(app.getApprovalList()));
					}
					app = applicantRepo.save(app);
				} else {
					app.setFamilyId(applicants.getFamilyId());
					app.setApplicationType(applicants.getApplicationType());
					app.setGroup(applicants.getGroup());
					app.setQualification(applicants.getQualification());
					if(SecurityUtils.isAuthenticated()) {
						app.setApproved(applicants.isApproved());
						app.setApprovalList(getApprovalList(app.getApprovalList(), SecurityUtils.getRole(), app.isApproved()));
						app.setRemarks(applicants.getRemarks());
						app.setFinalApproved(finalApproval(app.getApprovalList()));
					}
					app = applicantRepo.save(app);
				}
			} else {
					if(file != null && file.getOriginalFilename().length()>0 			
					&& file.getContentType().equalsIgnoreCase("application/pdf")){
						app.setFileName(file.getOriginalFilename());
						app.setData(file.getBytes());
						app.setFamilyId(applicants.getFamilyId());
						app.setApplicationType(applicants.getApplicationType());
						app.setGroup(applicants.getGroup());
						app.setQualification(applicants.getQualification());
						if(SecurityUtils.isAuthenticated()) {
							app.setApproved(applicants.isApproved());
							app.setApprovalList(getApprovalList(app.getApprovalList(), SecurityUtils.getRole(), app.isApproved()));
							app.setRemarks(applicants.getRemarks());
							app.setFinalApproved(finalApproval(app.getApprovalList()));							
						}
						app = applicantRepo.save(app);
					} else {
						model.addAttribute("fileNameNotFound", 
								messageSource.getMessage("fileNameNotFound", null,loc.resolveLocale(req)));
						model.addAttribute("familyList", emp.getMembers());
						model.addAttribute("employeeId", emp.getId());
						model.addAttribute("degreeList", degreeList);
						return "ApplicationForm";
					}
			}
		}
		if(SecurityUtils.isAuthenticated()) {
			if(SecurityUtils.getRole().equalsIgnoreCase("ROLE_DEPARTMENT")) {				
				if(action != null && action.equalsIgnoreCase("save")) {
					app.setAuthority(SecurityUtils.getRole());
					app = applicantRepo.save(app);
					redirect.addFlashAttribute("message", "Application saved by department.");
					return "redirect:/getApplicants/page/1";
				} else if(action != null && action.equalsIgnoreCase("appointingauthority")) {
					app.setAuthority("ROLE_AA");
					app = applicantRepo.save(app);					
					redirect.addFlashAttribute("message", "Application forwarded to appointing authority.");
					return "redirect:/getApplicants/page/1";					
				} else if(action != null && action.equalsIgnoreCase("sendtowaiting")) {
					app.setAuthority("ROLE_DEPARTMENT");
					app.setWaiting(true);
					app = applicantRepo.save(app);					
					redirect.addFlashAttribute("message", "Applicants send to waiting list.");
					return "redirect:/getApplicants/page/1";										
				} else {
					return "redirect:/getApplicants/page/1";
				}

			} else if(SecurityUtils.getRole().equalsIgnoreCase("ROLE_AA")) {
				if(action != null && action.equalsIgnoreCase("save")) {
					app.setAuthority(SecurityUtils.getRole());
					app = applicantRepo.save(app);
					redirect.addFlashAttribute("message", "Application saved by Appointing Authority.");
					return "redirect:/getApplicants/page/1";
				} else if(action != null && action.equalsIgnoreCase("sendbackdept")) {
					app.setAuthority("ROLE_DEPARTMENT");
					app = applicantRepo.save(app);					
					redirect.addFlashAttribute("message", "Application forwarded to Department.");
					return "redirect:/getApplicants/page/1";					
				} else {
					return "redirect:/getApplicants/page/1";
				}
			} else {
				return "redirect:/getApplicants/page/1";
			}
		} else {
			redirect.addFlashAttribute("message", "Application submitted successfully by family member.");
			return "redirect:/getApplicationForm/"+emp.getAuthtoken()+"/"+emp.getId();
		}
	}
	
	@GetMapping("/getApplicants/page/{page}")
	public String getApplicants(@PathVariable int page,Model model,@RequestParam(name="filter",required = false) String filter) {
		if(page<0) {
			page = 1;
		}
    	Pageable pageable = PageRequest.of(page - 1, 5, Sort.by(Sort.Direction.ASC,"id"));
    	Page<Applicants> applicantsList = null;
    	
    	if(filter != null && filter.equalsIgnoreCase("ALL")) {
    		applicantsList = applicantRepo.findAll(pageable);
    	} else if(filter != null && filter.equalsIgnoreCase("WAITING")){ 
    		applicantsList = applicantRepo.findByAuthorityAndWaiting(SecurityUtils.getRole(),true,pageable);
    	} else {
    		applicantsList = applicantRepo.findByAuthorityAndWaiting(SecurityUtils.getRole(),false,pageable);
    	}

		 int totalPages = applicantsList.getTotalPages();
	     if(totalPages > 0) {
	         List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
	         model.addAttribute("pageNumbers", pageNumbers);
	     }
	     model.addAttribute("applicantsList", applicantsList.getContent());
		return "ApplicantsList";
	}
	
    public boolean finalApproval(List<ApprovalAuthority> approvalList) {
    	List<Boolean> boolList= new ArrayList<>();
    	approvalList.forEach(s->{    		
    		if(!s.isOptional()) {
    			boolList.add(s.isApproved());
    		}
    	});
    	return !boolList.contains(false);
    }
    
    public List<ApprovalAuthority> getApprovalList(List<ApprovalAuthority> approvalList,String authority,boolean approved) {
		for(ApprovalAuthority approvAuth : approvalList) {
			if(approvAuth.getAuthority().equalsIgnoreCase(authority)) {
				approvAuth.setApproved(approved);
			}
		}
    	return approvalList;
    }
	
}
