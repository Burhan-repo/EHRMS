package hrms.Recruitment.models;

import javax.persistence.Embeddable;

@Embeddable
public class FileData {

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
    private String filename = "testing";
    private byte[] data = "testing.jpg".getBytes();
    
//	public Long getId() {
//		return id;
//	}
//	public void setId(Long id) {
//		this.id = id;
//	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	
}
