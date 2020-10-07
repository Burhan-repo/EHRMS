package hrms.Recruitment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hrms.Recruitment.models.CandidateDocumentsStatic;

@Repository
public interface CandidateDocumentsStaticRepository extends JpaRepository<CandidateDocumentsStatic, Long>{

	CandidateDocumentsStatic findByName(String fileName);

}
