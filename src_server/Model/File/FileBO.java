package Model.File;

import Model.Bean.FolderPath;

public class FileBO {
	private FileDAO fileDAO = new FileDAO();
	public FolderPath getFolderPathByFid(Integer i) {
		return fileDAO.getByFid(i);
	}

}
