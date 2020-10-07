package hrms.Recruitment.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="Proposal")
@EntityListeners(AuditingEntityListener.class)
@Audited(auditParents = Auditable.class)
@DynamicUpdate
public class Proposal extends Auditable<String>{

	@Id
	@SequenceGenerator(allocationSize=1, initialValue=1, sequenceName="proposal_id_seq", name="proposal_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "proposal_id_seq")
    @Column(name="Proposal_Id")
    private Long id;
    private LocalDateTime date = LocalDateTime.now();
    
    private String processType;
    
    @Column(length = 2500)
    @Size(max = 2500)
    private String info="";
    
    private String depart = "testingdepart";
    
    @NotEmpty()
    @Size(min = 5,max = 255)
    private String subject;
    
    @NotEmpty
    @Column(updatable = false)
    private String designation;
    
    @Min(0)
    private int expectedvacancy;
    
    @Min(0)
    private int reservedvacancy;

    
    @Positive
    private int totalvacancy = 100;
    
    private String authority="";

    
    @Min(0)
    private int extravacancy = 0;
    
    private boolean approved;
    
    private boolean finalApproved;
    
    @Transient
    private Object dataurl;
    
    private boolean viewProposal;
    
    private boolean offlineImportEnable;
    
	public boolean isOfflineImportEnable() {
		return offlineImportEnable;
	}
	public void setOfflineImportEnable(boolean offlineImportEnable) {
		this.offlineImportEnable = offlineImportEnable;
	}
	public boolean isViewProposal() {
		return viewProposal;
	}
	public void setViewProposal(boolean viewProposal) {
		this.viewProposal = viewProposal;
	}
	public boolean isFinalApproved() {
		return finalApproved;
	}
	public void setFinalApproved(boolean finalApproved) {
		this.finalApproved = finalApproved;
	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="Proposal_ApprovalAuthority",
            joinColumns = @JoinColumn(
                    name = "Proposal_Proposal_Id", referencedColumnName = "Proposal_Id"),
            inverseJoinColumns = @JoinColumn(
                    name = "ApprovalAuthority_ApprovalAuthority_Id", referencedColumnName = "ApprovalAuthority_Id"))
	@JsonManagedReference
    List<ApprovalAuthority> approvalList = new ArrayList<>();
	    
	public List<ApprovalAuthority> getApprovalList() {
		return approvalList;
	}
	public void setApprovalList(List<ApprovalAuthority> approvalList) {
		this.approvalList = approvalList;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public Object getDataurl() {
		return dataurl;
	}
	public void setDataurl(Object dataurl) {
		this.dataurl = dataurl;
	}
	public String getProcessType() {
		return processType;
	}
	public void setProcessType(String processType) {
		this.processType = processType;
	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="Proposal_Category",
            joinColumns = @JoinColumn(
                    name = "Proposal_Proposal_Id", referencedColumnName = "Proposal_Id"),
            inverseJoinColumns = @JoinColumn(
                    name = "Category_Category_Id", referencedColumnName = "Category_Id"))
	@JsonManagedReference
    private List<Category> category = new ArrayList<>(); 
    

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="Proposal_MpscCandidates",
            joinColumns = @JoinColumn(
                    name = "Proposal_Proposal_Id", referencedColumnName = "Proposal_Id"),
            inverseJoinColumns = @JoinColumn(
                    name = "MpscCandidates_MpscCandidate_Id", referencedColumnName = "MpscCandidate_Id"))	
	@JsonManagedReference
	private List<MpscCandidates> Proposal_MpscCandidate = new ArrayList<>();
     
	
    public List<MpscCandidates> getProposal_MpscCandidate() {
		return Proposal_MpscCandidate;
	}
	public void setProposal_MpscCandidate(List<MpscCandidates> proposal_MpscCandidate) {
		Proposal_MpscCandidate = proposal_MpscCandidate;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public int getReservedvacancy() {
		return reservedvacancy;
	}
	public void setReservedvacancy(int reservedvacancy) {
		this.reservedvacancy = reservedvacancy;
	}
	public int getExtravacancy() {
		return extravacancy;
	}
	public void setExtravacancy(int extravacancy) {
		this.extravacancy = extravacancy;
	}
	public int getExpectedvacancy() {
		return expectedvacancy;
	}
	public void setExpectedvacancy(int expectedvacancy) {
		this.expectedvacancy = expectedvacancy;
	}

    private String filename;
    private byte[] data;
    
    
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public List<Category> getCategory() {
		return category;
	}
	public void setCategory(List<Category> category) {
		this.category = category;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getDepart() {
		return depart;
	}
	public void setDepart(String depart) {
		this.depart = depart;
	}
	public int getTotalvacancy() {
		return totalvacancy;
	}
	public void setTotalvacancy(int totalvacancy) {
		this.totalvacancy = totalvacancy;
	}

	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public boolean isApproved() {
		return approved;
	}
	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	

}
