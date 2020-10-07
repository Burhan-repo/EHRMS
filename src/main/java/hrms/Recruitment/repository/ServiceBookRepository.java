package hrms.Recruitment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hrms.Recruitment.models.Service_Book;

@Repository
public interface ServiceBookRepository extends JpaRepository<Service_Book, Long>{
	
	
	@Query("select sb from Service_Book sb where sb.R_Designation_Id=?1")
	List<Service_Book> findByR(String desId);

}
