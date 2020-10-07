package hrms.Recruitment.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Location")
public class Location {

	@Id
	@Column(name="Location_Id")
	private Long id;
	
	private String Division_Name;
	
	private String District_Name;
	
	private String Police_District_Email;
	private String Medical_District_Email;
	
	public String getPolice_District_Email() {
		return Police_District_Email;
	}

	public void setPolice_District_Email(String police_District_Email) {
		Police_District_Email = police_District_Email;
	}

	public String getMedical_District_Email() {
		return Medical_District_Email;
	}

	public void setMedical_District_Email(String medical_District_Email) {
		Medical_District_Email = medical_District_Email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDivision_Name() {
		return Division_Name;
	}

	public void setDivision_Name(String division_Name) {
		Division_Name = division_Name;
	}

	public String getDistrict_Name() {
		return District_Name;
	}

	public void setDistrict_Name(String district_Name) {
		District_Name = district_Name;
	}
}
