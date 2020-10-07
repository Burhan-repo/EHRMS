package hrms.Recruitment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hrms.Recruitment.models.Designation;

public interface DesignationRepository extends JpaRepository<Designation, Long> {
	
	@Query("select d from Designation d where d.designation_name_en=?1")
	List<Designation> findByDesignationName(String name);

}
