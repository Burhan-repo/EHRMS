package hrms.Recruitment.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hrms.Recruitment.models.FileModel;

@Repository
public interface FileModelRepository extends JpaRepository<FileModel, Long>{
	
	
	
//	  boolean existsByFileNameAndQid(String fileName,long id);

//	 @Query("select f from FileModel f where f.qid.MpscCandidates_Id=?1")
//	List<FileModel> findByMpscCandidates_Id(long candidateId);

	 @Query("select f from FileModel f where f.id =?1 and f.fileName=?2")
	FileModel findByIdAndFileName(Long valueOf,String fileName);


	List<FileModel> findAllBySent(boolean sent);


	List<FileModel> findAllByVerified(boolean b);

	List<FileModel> findAllBySentAndVerified(boolean b, boolean c);


//	@Query("select f from FileModel as f where f.dateFrom > CURRENT_DATE")
//	List<FileModel> findAllByDate();

	@Query("select f from FileModel as f where f.dateTime < ?1")
	List<FileModel> findAllByDate(LocalDateTime now);
	 

}
