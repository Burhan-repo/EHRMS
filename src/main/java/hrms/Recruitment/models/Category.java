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
import javax.validation.constraints.Min;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="Category")
@Audited
@DynamicUpdate
public class Category {
	
	@Id
	@SequenceGenerator(allocationSize=1, initialValue=1, sequenceName="category_id_seq", name="category_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "category_id_seq")
	@Column(name="Category_Id")
	private long id;
	private String Category_Name;
	private String Category_Code;
	private float Percentage_Reserved;
	
	@ManyToMany(mappedBy = "category")
	@JsonBackReference
	private List<Proposal> proposal = new ArrayList<>();
	
	@Min(0)
	private int Vacancy;
	private String Type;
	
//	@ManyToOne(fetch = FetchType.LAZY,optional = false)
//	@JoinColumn(name="Proposal_Id")
//	private Proposal proposal;

	
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCategory_Name() {
		return Category_Name;
	}
	public void setCategory_Name(String category_Name) {
		Category_Name = category_Name;
	}
	public String getCategory_Code() {
		return Category_Code;
	}
	public void setCategory_Code(String category_Code) {
		Category_Code = category_Code;
	}
	public float getPercentage_Reserved() {
		return Percentage_Reserved;
	}
	public void setPercentage_Reserved(float percentage_Reserved) {
		Percentage_Reserved = percentage_Reserved;
	}
	public int getVacancy() {
		return Vacancy;
	}
	public void setVacancy(int vacancy) {
		Vacancy = vacancy;
	}
}
