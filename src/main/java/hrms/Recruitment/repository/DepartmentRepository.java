package hrms.Recruitment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hrms.Recruitment.models.Departments;

@Repository
public interface DepartmentRepository extends JpaRepository<Departments, Integer>{
}
