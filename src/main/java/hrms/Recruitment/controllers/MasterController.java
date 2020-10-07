package hrms.Recruitment.controllers;

import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itextpdf.text.DocumentException;

import hrms.Recruitment.service.DBoperation;

@Controller
public class MasterController {
	
	@Autowired
	private DBoperation dbOps;
	
    @GetMapping("/")
    public String root(Model model) {
    	if(SecurityContextHolder.getContext().getAuthentication().isAuthenticated() 
    			&& !SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {	
	        return "Recruitment";
    	} else {
    		return "login";
    	}
    }
	
    @GetMapping("/recruitment")
    public String getRecruitment() {
    	return "RecruitmentProcessList";
    }
    
	@GetMapping("getProcess/{type}")
	public String getProcess(@PathVariable String type,Model model,RedirectAttributes redirect) {		
		switch(type) {
			case "Recruitment by Individual Departments through MPSC":
					return "redirect:/inbox/page/1";
			case "Compassionate Recruitment":
					return "redirect:/closeservicebook/page/1";
			case "Recruitment of Group B (non-Gazetted) and Group C posts":
				    return "redirect:/inboxgroupbc/ptype/GROUPBC/page/1";

		}
		return "Recruitment";
	}
	
    @GetMapping("/getPDFFF") 
    public ResponseEntity<?> getFile() throws DocumentException {
    	
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=HTMLPreview.pdf");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(new ByteArrayInputStream(dbOps.generatePDf().toByteArray())));
    }
	
}
