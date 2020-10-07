package hrms.Recruitment.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class AsyncExecutor {
	
	static ExecutorService executorAuto = Executors.newFixedThreadPool(1000);
	@Bean
	public ExecutorService getAsyncAutoExecutor() {
	        return executorAuto;
	}
}
