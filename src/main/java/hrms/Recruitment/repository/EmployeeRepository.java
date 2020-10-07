package hrms.Recruitment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hrms.Recruitment.models.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

	
	@Query("select e from Employee as e order by e.id asc")
	List<Employee> findAllById();


}
