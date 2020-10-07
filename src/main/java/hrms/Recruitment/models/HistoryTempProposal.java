package hrms.Recruitment.models;

public class HistoryTempProposal {
	
	private String info;
	private byte[] data;
	private String dataurl="";
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public String getDataurl() {
		return dataurl;
	}
	public void setDataurl(String dataurl) {
		this.dataurl = dataurl;
	}
}
