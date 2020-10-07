package hrms.Recruitment.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="Employee")
@Audited
@DynamicUpdate(true)
public class Employee {

	@Id
	@SequenceGenerator(allocationSize=1, initialValue=1, sequenceName="employee_id_seq", name="employee_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "employee_id_seq")
    @Column(name="Employee_Id")
	private Long id;
	
	private String name;
	private int age;
	private boolean active;
	private boolean informedFamily;
	private String authtoken;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="Employee_Family",
            joinColumns = @JoinColumn(
                    name = "Employee_Employee_Id", referencedColumnName = "Employee_Id"),
            inverseJoinColumns = @JoinColumn(
                    name = "Family_Family_Id", referencedColumnName = "Family_Id"))
	@JsonManagedReference
	private List<Family> members = new ArrayList<>();
	
	 @OneToOne(cascade = CascadeType.ALL)
	 @JoinColumn(name = "Applicants_Id", referencedColumnName = "Applicants_Id")
	 private Applicants applicants;
	
	
	private LocalDate birthDate;
	
	@NotEmpty
	private String reasonCloseBook;
	
	@NotEmpty(message = "{InformationError}")
	private String information;
	
	@NotNull(message = "{dateOfClose}")
	private String dateOfClose;

	private String fileName;

	private byte[] data;
	
	private String contentType;
	
	private Long applicantsId;
			
	
	public Applicants getApplicants() {
		return applicants;
	}
	public void setApplicants(Applicants applicants) {
		this.applicants = applicants;
	}
	public Long getApplicantsId() {
		return applicantsId;
	}
	public void setApplicantsId(Long applicantsId) {
		this.applicantsId = applicantsId;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public String getReasonCloseBook() {
		return reasonCloseBook;
	}
	public void setReasonCloseBook(String reasonCloseBook) {
		this.reasonCloseBook = reasonCloseBook;
	}
	public String getInformation() {
		return information;
	}
	public void setInformation(String information) {
		this.information = information;
	}

	public String getDateOfClose() {
		return dateOfClose;
	}
	public void setDateOfClose(String dateOfClose) {
		this.dateOfClose = dateOfClose;
	}
	public LocalDate getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}
	public List<Family> getMembers() {
		return members;
	}
	public void setMembers(List<Family> members) {
		this.members = members;
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
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public boolean isInformedFamily() {
		return informedFamily;
	}
	public void setInformedFamily(boolean informedFamily) {
		this.informedFamily = informedFamily;
	}
	public String getAuthtoken() {
		return authtoken;
	}
	public void setAuthtoken(String authtoken) {
		this.authtoken = authtoken;
	}
	
//	private List<Family> members = new ArrayList<>();
}
