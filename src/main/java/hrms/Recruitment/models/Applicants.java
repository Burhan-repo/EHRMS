package hrms.Recruitment.models;

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
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import hrms.Recruitment.constants.ApplicationType;

@Entity
@Table(name="Applicants")
@DynamicUpdate(true)
@Audited
@AuditOverride(forClass = Auditable.class)
public class Applicants extends Auditable<String>{
	
	@Id
	@SequenceGenerator(allocationSize=1, initialValue=1, sequenceName="applicants_id_seq", name="applicants_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "applicants_id_seq")
	@Column(name="Applicants_Id")
	private Long id;
	
	private String applicationType = ApplicationType.NEW.toString();
	
	private String familyId;
	
	private String employeeId;
	
	private String authority = "ROLE_DEPARTMENT";
	
	private String processType = "COMPASSIONATE";
	
	private String qualification;
	
	@Transient
	private String dataurl;
	
    @OneToOne(mappedBy = "applicants")
    private Employee employee;
    
	private String fileName;
	private byte[] data;
	private boolean approved;
	private String remarks;
	private String group;
	private boolean view;
	private boolean finalApproved;
	private boolean active;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="Applicants_ApprovalAuthority",
            joinColumns = @JoinColumn(
                    name = "Applicants_Applicants_Id", referencedColumnName = "Applicants_Id"),
            inverseJoinColumns = @JoinColumn(
                    name = "ApprovalAuthority_ApprovalAuthority_Id", referencedColumnName = "ApprovalAuthority_Id"))
	@JsonManagedReference
    List<ApprovalAuthority> approvalList = new ArrayList<>();

	private boolean waiting;
			
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public boolean isWaiting() {
		return waiting;
	}
	public void setWaiting(boolean waiting) {
		this.waiting = waiting;
	}
	public boolean isFinalApproved() {
		return finalApproved;
	}
	public void setFinalApproved(boolean finalApproved) {
		this.finalApproved = finalApproved;
	}
	public String getProcessType() {
		return processType;
	}
	public void setProcessType(String processType) {
		this.processType = processType;
	}
	public String getDataurl() {
		return dataurl;
	}
	public void setDataurl(String dataurl) {
		this.dataurl = dataurl;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public boolean isView() {
			return view;
		}
		public void setView(boolean view) {
			this.view = view;
		}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public String getQualification() {
		return qualification;
	}
	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getFamilyId() {
		return familyId;
	}
	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getApplicationType() {
		return applicationType;
	}
	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
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
	public boolean isApproved() {
		return approved;
	}
	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public List<ApprovalAuthority> getApprovalList() {
		return approvalList;
	}
	public void setApprovalList(List<ApprovalAuthority> approvalList) {
		this.approvalList = approvalList;
	}
	
}
