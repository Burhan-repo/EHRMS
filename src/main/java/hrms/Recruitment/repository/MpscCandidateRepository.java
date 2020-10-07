package hrms.Recruitment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hrms.Recruitment.models.MpscCandidates;

public interface MpscCandidateRepository extends JpaRepository<MpscCandidates, Long>{
	
	@Query("select m from MpscCandidates m where verified=?1")
	List<MpscCandidates> findByVerified(boolean b);
	
}
