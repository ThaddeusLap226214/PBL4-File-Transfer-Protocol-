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
	
	//Tạo đường dẫn ảo bằng đường dẫn thực, và đường dẫn ảo của cha
	public static String createVirtualPath(String nativePath, String fatherVirPath) {

        if (nativePath == null || fatherVirPath == null) {
            return null;
        }

        // 1. Chuẩn hóa nativePath về dạng UNIX
        // VD: D:\FTP_ROOT\docs\images -> D:/FTP_ROOT/docs/images
        String normalizedNative = nativePath.replace("\\", "/");
        
        // 2. Chuẩn hóa fatherVirPath:
        //    - Bắt đầu bằng /
        //    - Kết thúc bằng /
        String normalizedFather = fatherVirPath;

        if (!normalizedFather.startsWith("/")) {
            normalizedFather = "/" + normalizedFather;
        }
        if (!normalizedFather.endsWith("/")) {
            normalizedFather = normalizedFather + "/";
        }
        
        // 3. Tìm vị trí của "/father/"
        int index = normalizedNative.indexOf(normalizedFather);

        // 4. Không tìm thấy -> không tạo được virtual path
        if (index == -1) {
            return null;
        }

        // 5. Cắt từ vị trí father path đến hết
        // VD: "/docs/images/logo.png"
        String virtualPath = normalizedNative.substring(index);

        // 6. Loại bỏ dấu "/" dư ở cuối (nếu có), trừ root "/"
        if (virtualPath.length() > 1 && virtualPath.endsWith("/")) {
            virtualPath = virtualPath.substring(0, virtualPath.length() - 1);
        }
        return virtualPath;
    }
}
