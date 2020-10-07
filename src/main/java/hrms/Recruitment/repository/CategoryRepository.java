package hrms.Recruitment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hrms.Recruitment.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
	
	@Query("select max(id) from Category")
	long findByMaxId();

//	List<Category> findByProposal_Id(Long id);

}
