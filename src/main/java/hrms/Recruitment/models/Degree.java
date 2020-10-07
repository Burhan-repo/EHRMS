package hrms.Recruitment.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="degree")
public class Degree {
		
	@Id
	@Column(name="degree_id")
	private Long id;
	private String degree_description;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDegree_description() {
		return degree_description;
	}
	public void setDegree_description(String degree_description) {
		this.degree_description = degree_description;
	}
}
