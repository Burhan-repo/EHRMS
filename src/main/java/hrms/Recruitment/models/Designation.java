package hrms.Recruitment.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="designation")
public class Designation {

	
	@Id
	private int designation_id;
	private String designation_name_en;
	private String designation_name_hi;
	private String designation_name_regional;
	private String description;
	private String designation_rank_name_en;
	private String service_group;
	private String is_active;
	private String is_gazetted_1;
	private String designation_code;
	public int getDesignation_id() {
		return designation_id;
	}
	public void setDesignation_id(int designation_id) {
		this.designation_id = designation_id;
	}
	public String getDesignation_name_en() {
		return designation_name_en;
	}
	public void setDesignation_name_en(String designation_name_en) {
		this.designation_name_en = designation_name_en;
	}

	public String getDesignation_name_hi() {
		return designation_name_hi;
	}
	public void setDesignation_name_hi(String designation_name_hi) {
		this.designation_name_hi = designation_name_hi;
	}
	public String getDesignation_name_regional() {
		return designation_name_regional;
	}
	public void setDesignation_name_regional(String designation_name_regional) {
		this.designation_name_regional = designation_name_regional;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDesignation_rank_name_en() {
		return designation_rank_name_en;
	}
	public void setDesignation_rank_name_en(String designation_rank_name_en) {
		this.designation_rank_name_en = designation_rank_name_en;
	}
	public String getService_group() {
		return service_group;
	}
	public void setService_group(String service_group) {
		this.service_group = service_group;
	}
	public String getIs_active() {
		return is_active;
	}
	public void setIs_active(String is_active) {
		this.is_active = is_active;
	}
	public String getIs_gazetted_1() {
		return is_gazetted_1;
	}
	public void setIs_gazetted_1(String is_gazetted_1) {
		this.is_gazetted_1 = is_gazetted_1;
	}
	public String getDesignation_code() {
		return designation_code;
	}
	public void setDesignation_code(String designation_code) {
		this.designation_code = designation_code;
	}
}
