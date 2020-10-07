package hrms.Recruitment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hrms.Recruitment.models.AuthorityConfig;

public interface AuthorityConfigRepository extends JpaRepository<AuthorityConfig, Long>{

	@Query("select a from AuthorityConfig a where a.ProcessType=?1 order by a.AuthorityOrder")
	List<AuthorityConfig> findByCustom(String type);
	
}
