package hrms.Recruitment.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import hrms.Recruitment.models.User;

public interface UserService extends UserDetailsService {

    User findByEmail(String email);
    
    UserDetails getCurrentUser();

}
