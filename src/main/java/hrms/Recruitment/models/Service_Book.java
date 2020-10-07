package hrms.Recruitment.models;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Service_Book")
public class Service_Book {
	
	@Id
	@Column(name = "Service_Book_Id")
	private Long id;
	private String R_Designation_Id;
	private int R_Vacancy;
	private LocalDate FromDate;
	private LocalDate ToDate;

	

	public LocalDate getFromDate() {
		return FromDate;
	}
	public void setFromDate(LocalDate fromDate) {
		FromDate = fromDate;
	}
	public LocalDate getToDate() {
		return ToDate;
	}
	public void setToDate(LocalDate toDate) {
		ToDate = toDate;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getR_Designation_ID() {
		return R_Designation_Id;
	}
	public void setR_Designation_ID(String r_Designation_Id) {
		R_Designation_Id = r_Designation_Id;
	}
	public int getR_Vacancy() {
		return R_Vacancy;
	}
	public void setR_Vacancy(int r_Vacancy) {
		R_Vacancy = r_Vacancy;
	}

}
