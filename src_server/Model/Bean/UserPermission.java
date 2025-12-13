package Model.Bean;

public class UserPermission {
	private int usid;
	private int fid;
	private String accessMode; //3 quyền RWD(Read Write Delete),RO(ReadOnly), DIS(Disabled)
	public int getUsid() {
		return usid;
	}
	public void setUsid(int usid) {
		this.usid = usid;
	}
	public int getFid() {
		return fid;
	}
	public void setFid(int fid) {
		this.fid = fid;
	}
	public String getAccessMode() {
		return accessMode;
	}
	public void setAccessMode(String accessMode) {
		this.accessMode = accessMode;
	}
	
}
