package hrms.Recruitment.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Reserved_Category_Static")
public class ReservedCaste {

	@Id
	@Column(name="ReservedCaste_Id")
	private long id;
	
	private String CasteCode;
	private String CasteName;
	private int PercentageReserved;
	private String Type;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCasteCode() {
		return CasteCode;
	}
	public void setCasteCode(String casteCode) {
		CasteCode = casteCode;
	}
	public String getCasteName() {
		return CasteName;
	}
	public void setCasteName(String casteName) {
		CasteName = casteName;
	}
	public int getPercentageReserved() {
		return PercentageReserved;
	}
	public void setPercentageReserved(int percentageReserved) {
		PercentageReserved = percentageReserved;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}

}
