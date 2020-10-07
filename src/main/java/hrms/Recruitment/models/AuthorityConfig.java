package hrms.Recruitment.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="AuthorityConfig")
public class AuthorityConfig {

	@Id
	@Column(name="AuthorityConfig_Id")
	private long id;
	private int AuthorityOrder;
	private String ProcessType;
	private boolean Optional;
	private String Authority;
	private String authorityname;
	
	public String getAuthorityname() {
		return authorityname;
	}
	public void setAuthorityname(String authorityname) {
		this.authorityname = authorityname;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getAuthorityOrder() {
		return AuthorityOrder;
	}
	public void setAuthorityOrder(int authorityOrder) {
		AuthorityOrder = authorityOrder;
	}
	public String getProcessType() {
		return ProcessType;
	}
	public void setProcessType(String processType) {
		ProcessType = processType;
	}
	public boolean isOptional() {
		return Optional;
	}
	public void setOptional(boolean optional) {
		Optional = optional;
	}
	public String getAuthority() {
		return Authority;
	}
	public void setAuthority(String authority) {
		Authority = authority;
	}

}
