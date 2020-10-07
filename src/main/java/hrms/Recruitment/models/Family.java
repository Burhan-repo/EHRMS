package hrms.Recruitment.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="Family")
@Audited
@DynamicUpdate
public class Family {

	@Id
	@SequenceGenerator(allocationSize=1, initialValue=1, sequenceName="family_id_seq", name="family_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "family_id_seq")
    @Column(name="Family_Id")
	private Long id;
	private String name;
	private int age;
	private String address;
	private String email;
	private String educationalQualification;
	private String dateOfBirth;

//	 @OneToOne(cascade = CascadeType.ALL)
//	 @JoinColumn(name = "Applicants_Id", referencedColumnName = "Applicants_Id")
//	 private Applicants applicants;

	
	@ManyToMany(mappedBy = "members")
	@JsonBackReference
	private List<Employee> employee = new ArrayList<>();
		
//	public Applicants getApplicants() {
//		return applicants;
//	}
//	public void setApplicants(Applicants applicants) {
//		this.applicants = applicants;
//	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getEducationalQualification() {
		return educationalQualification;
	}
	public void setEducationalQualification(String educationalQualification) {
		this.educationalQualification = educationalQualification;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<Employee> getEmployee() {
		return employee;
	}
	public void setEmployee(List<Employee> employee) {
		this.employee = employee;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}
