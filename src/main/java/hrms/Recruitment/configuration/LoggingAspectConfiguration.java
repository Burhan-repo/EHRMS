package hrms.Recruitment.configuration;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import hrms.Recruitment.aspect.LogginAspect;

@Configurable
@EnableAspectJAutoProxy
public class LoggingAspectConfiguration {
	
	@Bean
	public LogginAspect getAspect() {
		return new LogginAspect();
	}

}
