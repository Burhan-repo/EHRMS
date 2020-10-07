package hrms.Recruitment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hrms.Recruitment.models.Proposal;

@Repository
public interface NoticeRepository extends JpaRepository<Proposal, Long>{
	Page<Proposal> findAll(Pageable pageable);
	
	@Query("select p from Proposal p where p.processType=?1")
	Page<Proposal> findbyName(String processType,Pageable pageable);
	
	@Query("select max(id) from Proposal")
	Long findMaxId();

	Page<Proposal> findByAuthority(String authority, Pageable pageable);

	Page<Proposal> findByProcessTypeAndAuthority(String ptype, String authority, Pageable pageable);

}
