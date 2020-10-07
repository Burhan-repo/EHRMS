package hrms.Recruitment.models;

import java.time.LocalDateTime;
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
@Table(name="CandidateFiles")
@Audited
@DynamicUpdate
public class FileModel {

	@Id
	@SequenceGenerator(allocationSize=1, initialValue=1, sequenceName="candidatefile_id_seq", name="candidatefile_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "candidatefile_id_seq")
	@Column(name="CandidateFile_Id")
	private long id;
		
	private String fileName;
	private byte[] data;
	private String AuthorityName;
	private String AuthorityEmail;
	private String District;
	private boolean sent;
	private boolean verified;
	private String contentType;
	
	private LocalDateTime dateTime = LocalDateTime.now();
	
	private boolean reminder=false;
	
	@ManyToMany(mappedBy = "files")
	@JsonBackReference
	private List<MpscCandidates> candidateList = new ArrayList<>();
		
	public LocalDateTime getDateTime() {
		return dateTime;
	}
	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}
	public boolean isReminder() {
		return reminder;
	}
	public void setReminder(boolean reminder) {
		this.reminder = reminder;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public boolean isSent() {
		return sent;
	}
	public void setSent(boolean sent) {
		this.sent = sent;
	}
	public boolean isVerified() {
		return verified;
	}
	public void setVerified(boolean verified) {
		this.verified = verified;
	}
	public String getDistrict() {
		return District;
	}
	public void setDistrict(String district) {
		District = district;
	}
	public String getAuthorityName() {
		return AuthorityName;
	}
	public void setAuthorityName(String authorityName) {
		AuthorityName = authorityName;
	}
	public String getAuthorityEmail() {
		return AuthorityEmail;
	}
	public void setAuthorityEmail(String authorityEmail) {
		AuthorityEmail = authorityEmail;
	}
	public List<MpscCandidates> getCandidateList() {
		return candidateList;
	}
	public void setCandidateList(List<MpscCandidates> candidateList) {
		this.candidateList = candidateList;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	
}
