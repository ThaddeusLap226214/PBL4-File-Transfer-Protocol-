package Model.File;

import Model.Bean.FolderPath;

public class FileBO {
	private FileDAO fileDAO = new FileDAO();
	public FolderPath getFolderPathByFid(Integer i) {
		return fileDAO.getByFid(i);
	}
	public FolderPath insertIfNotExists(String virtualPath, String nativePath) {
        return fileDAO.insertIfNotExists(virtualPath, nativePath);
	}
	public FolderPath getByVirtualAndNativePath(String vpath, String npath) {
        return fileDAO.getByVirtualAndNativePath(vpath, npath);
	}

}
