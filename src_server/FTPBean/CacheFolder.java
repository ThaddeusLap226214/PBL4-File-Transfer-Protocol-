package FTPBean;

import java.util.HashMap;
import java.util.Map;

import Model.Bean.FolderPath;

public class CacheFolder {
	private Map<String, FolderPath> cache = new HashMap<String, FolderPath>();
	
	public void add(String key, FolderPath fp) {
		cache.put(key, fp);
	}

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
	
	public String confirmPath(String cwdPath) {
		//bỏ qua kiểm tra nó có tồn tại trong cache không
		
		return null;
	}
}
