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
@Table(name="ApprovalAuthority")
@Audited
@DynamicUpdate
public class ApprovalAuthority {

	@Id
	@SequenceGenerator(allocationSize=1, initialValue=1, sequenceName="approvalauthority_id_seq", name="approvalauthority_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "approvalauthority_id_seq")
    @Column(name="ApprovalAuthority_Id")
	private Long id;
	
	private String Authority;
	private boolean Optional;
	private boolean Approved;
	
	@ManyToMany(mappedBy = "approvalList")
	@JsonBackReference
	private List<Proposal> proposal = new ArrayList<>();

	
	public List<Proposal> getProposal() {
		return proposal;
	}
	public void setProposal(List<Proposal> proposal) {
		this.proposal = proposal;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAuthority() {
		return Authority;
	}
	public void setAuthority(String authority) {
		Authority = authority;
	}
	public boolean isOptional() {
		return Optional;
	}
	public void setOptional(boolean optional) {
		Optional = optional;
	}
	public boolean isApproved() {
		return Approved;
	}
	public void setApproved(boolean approved) {
		Approved = approved;
	}
}
