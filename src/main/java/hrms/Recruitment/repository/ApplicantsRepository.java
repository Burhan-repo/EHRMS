package hrms.Recruitment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import hrms.Recruitment.models.Applicants;

public interface ApplicantsRepository extends JpaRepository<Applicants, Long>{

	Page<Applicants> findByAuthority(String role, Pageable pageable);

	Page<Applicants> findByAuthorityAndWaiting(String role, boolean b, Pageable pageable);

}
