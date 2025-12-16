package Model.Bean;

public class FolderPath {
	private int fid;
	private String virtualPath;
	private String nativePath;

	private String accessMode; // RWD, RO, DIS

	public FolderPath() {
	}

	public FolderPath(int fid, String virtualPath, String nativePath) {
		this.fid = fid;
		this.virtualPath = virtualPath;
		this.nativePath = nativePath;
	}

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

	// ===== Thêm getter / setter cho accessMode =====
	public String getAccessMode() {
		return accessMode;
	}

	public void setAccessMode(String accessMode) {
		this.accessMode = accessMode;
	}
}
