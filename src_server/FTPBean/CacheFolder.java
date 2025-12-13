package FTPBean;

import java.util.HashMap;
import java.util.Map;

import Model.Bean.FolderPath;

public class CacheFolder {
	private Map<String, FolderPath> cache = new HashMap<String, FolderPath>();
	
	public void add(String key, FolderPath fp) {
		cache.put(key, fp);
	}
	
	//lấy thư mục cha (đường dẫn ảo cha)
	public String getParentDir(String path) {
		if (path == null || path.isEmpty()) {
	        return "/";
	    }
		// /a/b/c/ → /a/b/c
	    if (path.length() > 1 && path.endsWith("/")) {
	        path = path.substring(0, path.length() - 1);
	    }

	    // root
	    if (path.equals("/")) {
	        return "/";
	    }

	    // /a/b/c → /a/b
	    int lastSlash = path.lastIndexOf('/');
	    
	    // /a → /
	    if (lastSlash <= 0) {
	        return "/";
	    }

	    String parent = path.substring(0, lastSlash);
	    return parent.isEmpty() ? "/" : parent;
	}
	//Kiểm tra đường dẫn ảo có tồn tại trong cache không
	public boolean confirmPath(String path) {
		if (path == null || path.isEmpty()) {
	        return false;
	    }

	    // Chuẩn hóa: bỏ dấu / cuối nếu có (trừ root "/")
	    if (path.length() > 1 && path.endsWith("/")) {
	        path = path.substring(0, path.length() - 1);
	    }

	    return cache.containsKey(path);
	}
	
	//Lấy đường dẫn thực
	public String getNativePath(String path) {
		return cache.get(path).getNativePath();
	}

}
