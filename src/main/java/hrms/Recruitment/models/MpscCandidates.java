package hrms.Recruitment.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="MpscCandidates")
@Audited
@DynamicUpdate
public class MpscCandidates {

	@Id
	@SequenceGenerator(allocationSize=1, initialValue=1, sequenceName="mpsccandidate_id_seq", name="mpsccandidate_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "mpsccandidate_id_seq")
	@Column(name="MpscCandidate_Id")
	private long id;
	
	@Column(unique = true)
	private long mpsccandidatenumber;
	
	@Column(unique = true)
	private long rollnumber;
	private String candidatename;
	private String designation;
	private boolean apporoveddocument;
	private String city;
	private String identitynumber;
	private String offerletter;
	
	@ManyToMany(mappedBy = "Proposal_MpscCandidate")
	@JsonBackReference
	private List<Proposal> proposal = new ArrayList<>();
	
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="MpscCandidate_FileModel",
            joinColumns = @JoinColumn(
                    name = "MpscCandidates_MpscCandidate_Id", referencedColumnName = "MpscCandidate_Id"),
            inverseJoinColumns = @JoinColumn(
                    name = "CandidateFiles_CandidateFile_Id", referencedColumnName = "CandidateFile_Id"))
	@JsonManagedReference
	private List<FileModel> files = new ArrayList<>();
	  
		 	
	@NotEmpty
//	@NotNull
	private String appliedDate = LocalDate.now().toString();

	@NotEmpty
//	@NotNull
	private String joiningDate = LocalDate.now().toString();
	
	private boolean verified;
	
	@NotEmpty
//	@NotNull
	private String docId = UUID.randomUUID().toString();
	
	private String departmentDocId;
	
	@Transient
	private String fileName;
	
	@Transient
	private long proposalId;
	
	
	public List<Proposal> getProposal() {
		return proposal;
	}
	public void setProposal(List<Proposal> proposal) {
		this.proposal = proposal;
	}
	public long getProposalId() {
		return proposalId;
	}
	public void setProposalId(long proposalId) {
		this.proposalId = proposalId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public List<FileModel> getFiles() {
		return files;
	}
	public void setFiles(List<FileModel> files) {
		this.files = files;
	}
	public String getAppliedDate() {
		return appliedDate;
	}
	public void setAppliedDate(String appliedDate) {
		this.appliedDate = appliedDate;
	}
	public String getJoiningDate() {
		return joiningDate;
	}
	public void setJoiningDate(String joiningDate) {
		this.joiningDate = joiningDate;
	}
	public boolean isVerified() {
		return verified;
	}
	public void setVerified(boolean verified) {
		this.verified = verified;
	}
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	public String getDepartmentDocId() {
		return departmentDocId;
	}
	public void setDepartmentDocId(String departmentDocId) {
		this.departmentDocId = departmentDocId;
	}
	
	public long getMpsccandidatenumber() {
		return mpsccandidatenumber;
	}
	public void setMpsccandidatenumber(long mpsccandidatenumber) {
		this.mpsccandidatenumber = mpsccandidatenumber;
	}
	public long getRollnumber() {
		return rollnumber;
	}
	public void setRollnumber(long rollnumber) {
		this.rollnumber = rollnumber;
	}
	public String getCandidatename() {
		return candidatename;
	}
	public void setCandidatename(String candidatename) {
		this.candidatename = candidatename;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public boolean isApporoveddocument() {
		return apporoveddocument;
	}
	public void setApporoveddocument(boolean apporoveddocument) {
		this.apporoveddocument = apporoveddocument;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getIdentitynumber() {
		return identitynumber;
	}
	public void setIdentitynumber(String identitynumber) {
		this.identitynumber = identitynumber;
	}
	public String getOfferletter() {
		return offerletter;
	}
	public void setOfferletter(String offerletter) {
		this.offerletter = offerletter;
	}
	
	

}
