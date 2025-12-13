package Model.Bean;

public class FolderPath {
	private Integer fid;
	private String virtualPath;
	private String nativePath;
	public int getFid() {
		return fid;
	}
	public void setFid(int fid) {
		this.fid = fid;
	}
	public String getVirtualPath() {
		return virtualPath;
	}
	public void setVirtualPath(String virtualPath) {
		this.virtualPath = virtualPath;
	}
	public String getNativePath() {
		return nativePath;
	}
	public void setNativePath(String nativePath) {
		this.nativePath = nativePath;
	}
	public FolderPath(Integer fid, String virtualPath) {
		super();
		this.fid = fid;
		this.virtualPath = virtualPath;
	}
	public FolderPath() {
		super();
	}
	public FolderPath(Integer fid, String virtualPath, String nativePath) {
		super();
		this.fid = fid;
		this.virtualPath = virtualPath;
		this.nativePath = nativePath;
	}
	
}
