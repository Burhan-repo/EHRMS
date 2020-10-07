package hrms.Recruitment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hrms.Recruitment.models.ReservedCaste;

public interface ReservedCasteRepository extends JpaRepository<ReservedCaste, Long>{
	
	@Query("select max(id) from ReservedCaste s")
	long findByMaxId();

	List<ReservedCaste> findAllByOrderById();
	
}
