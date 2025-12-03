package Utils;

import java.util.ArrayList;

public class FolderInfo {
	Integer fid;
	String virtualPath;
	String nativePath;
	ArrayList<FolderInfo> childrens = new ArrayList<FolderInfo>();
	public String getVirtualPath() {
		return virtualPath;
	}
	public void setVirtualPath(String virtualPath) {
		this.virtualPath = virtualPath;
	}
	public int getFid() {
		return fid;
	}
	public ArrayList<FolderInfo> getChildrens() {
		return childrens;
	}
	public void addChildren(FolderInfo fi) {
		this.childrens.add(fi);
	}
	public FolderInfo(int fid, String virtualPath) {
		super();
		this.fid = fid;
		this.virtualPath = virtualPath;
	}
	
}
