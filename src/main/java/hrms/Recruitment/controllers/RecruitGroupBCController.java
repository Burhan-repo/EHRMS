package hrms.Recruitment.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import hrms.Recruitment.models.Designation;
import hrms.Recruitment.models.Proposal;
import hrms.Recruitment.repository.DesignationRepository;
import hrms.Recruitment.repository.NoticeRepository;
import hrms.Recruitment.security.SecurityUtils;

@Controller
public class RecruitGroupBCController 
{
	@Autowired
	private NoticeRepository noticeRepo;
	
	@Autowired
	private DesignationRepository designationRepo;
	
	 @GetMapping("/inboxgroupbc/ptype/{ptype}/page/{page}")
	    public String getnotices(@PathVariable("page") int page,@PathVariable("ptype") String ptype,@RequestParam(name="filter",required = false) String filter,Model model) {
	    	if(page<0) {
	    		page = 1;
	    	}
	    	String authority = SecurityUtils.getAuthority();    	
	    	Pageable pageable = PageRequest.of(page - 1, 5, Sort.by(Sort.Direction.ASC,"id"));
	    	Page<Proposal> noticePages = null;
	    	if(filter != null && filter.equalsIgnoreCase("ALL")) {
	    		noticePages = noticeRepo.findbyName(ptype,pageable);
	    	} else {
	    		if(ptype != null) {
	    			noticePages = noticeRepo.findByProcessTypeAndAuthority(ptype,authority,pageable);
	    		}
	    	}
			 int totalPages = noticePages.getTotalPages();
		     if(totalPages > 0) {
		         List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
		         model.addAttribute("pageNumbers", pageNumbers);
		     }
	    	model.addAttribute("noticelist", noticePages.getContent());    	
	    	return "inboxgroupbc";
	    }
	 
	 @GetMapping("/selectbcDesignation") 
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
			return "selectDesignationbc";
	    }

}
