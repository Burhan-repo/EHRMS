package hrms.Recruitment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hrms.Recruitment.models.Degree;

@Repository
public interface DegreeRepository extends JpaRepository<Degree, Long>{

}
