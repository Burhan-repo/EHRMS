package hrms.Recruitment.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="CandidateDocumentsStatic")
public class CandidateDocumentsStatic {
	
	@Id
	private Long id;
	private String name;
	private String AuthorityName;
	private String AuthorityEmail;

	
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

	
}
