package FTPEntity;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.Bean.FolderPath;

public class RootFolders {
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
//	public boolean confirmPath(String path) {
//		if (path == null || path.isEmpty()) {
//	        return false;
//	    }
//
//	    // Chuẩn hóa: bỏ dấu / cuối nếu có (trừ root "/")
//	    if (path.length() > 1 && path.endsWith("/")) {
//	        path = path.substring(0, path.length() - 1);
//	    }
//
//	    return cache.containsKey(path);
//	}
	
	//Kiểm tra đường dẫn ảo có được sinh ra từ các root
	public String mapVirtualToNative(String virtualPath) {
		if (virtualPath == null || virtualPath.isBlank()) {
	        return null;
	    }

	    String vp = normalizeVirtualPath(virtualPath);

	    for (FolderPath root : cache.values()) {
	        String rootVp = normalizeVirtualPath(root.getVirtualPath());

	        // 1. khớp đúng root
	        if (vp.equals(rootVp)) {
	            return Paths.get(root.getNativePath()).normalize().toString();
	        }

	     // 2. vp là con của root
	        if (vp.startsWith(rootVp + "/")) {
	            String relative = vp.substring(rootVp.length() + 1);

	            Path nativeRoot = Paths.get(root.getNativePath()).normalize();
	            Path target = nativeRoot.resolve(relative).normalize();

	            // 3. chặn thoát root
	            if (target.startsWith(nativeRoot)) {
	                return target.toString();
	            }
	        }
	    }

	    return null;
	}

	//Chuẩn hóa đường dẫn
	public String normalizeVirtualPath(String path) {
	    String p = path.trim();

	    if (!p.startsWith("/")) {
	        p = "/" + p;
	    }

	    if (p.length() > 1 && p.endsWith("/")) {
	        p = p.substring(0, p.length() - 1);
	    }

	    // Xử lý . và ..
	    String[] parts = p.split("/");
	    List<String> stack = new ArrayList<>();

	    for (String part : parts) {
	        if (part.isEmpty() || part.equals(".")) continue;
	        if (part.equals("..")) {
	            if (!stack.isEmpty()) stack.remove(stack.size() - 1);
	        } else {
	            stack.add(part);
	        }
	    }

	    return "/" + String.join("/", stack);
	}

	
	//Lấy đường dẫn thực
	public String getNativePath(String path) {
		return cache.get(path).getNativePath();
	}
	
	//Lấy các thư mục gốc
	public List<FolderPath> getListFolderRoot(){
		return new ArrayList<>(cache.values());
	}
}
