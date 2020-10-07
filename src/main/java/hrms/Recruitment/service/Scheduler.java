package hrms.Recruitment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class Scheduler {
	
//	private static final org.slf4j.Logger log = LoggerFactory.getLogger(Scheduler.class);
	
	@Autowired
	private DBoperation dbOps;
	
//	static boolean check=false;
	
	
//	@Scheduled(cron = "*/10 * * * * * ")
	public void mailingTask() {
		System.out.println("starting every 10 sec");
			dbOps.sendEmail();
	}

//	@Scheduled(cron = "* * 10 * * MON")
	public void mailingTaskNotVerified() {
		System.out.println("sending not verified doc to authority");
			dbOps.sendEmailNotVerified();
	}

//	@Scheduled(cron = "* * 10 * * MON")
	@Scheduled(cron = "*/10 * * * * * ")	
	public void settingReminder() {
//		if(!check) {
//			check = true;
//			System.out.println("setting Reminder");
			dbOps.settingReminder();
//		}
	}	

	@Scheduled(cron = "*/10 * * * * * ")	
	public void deletingApplicants() {
		  dbOps.settingReminder();
	}	
}
