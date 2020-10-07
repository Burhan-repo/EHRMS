package hrms.Recruitment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hrms.Recruitment.models.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long>{

	@Query("select l from Location as l where l.District_Name=?1")
	List<Location> findByDistrictName(String districtName);

}
