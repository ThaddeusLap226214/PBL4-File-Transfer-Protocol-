package FTPBean;
import FTP_Protocol.DataConnectionHandle;

public class Session {
	private int sessionID;		//mã phiên
	private String username;		//tên sau khi login
	private boolean loggedIn;	//Trạng thái login
	private int userId;			//Id trong csdl của user
	private String currentDirectory;	//thư mục hiện tại
	private CacheFolder cacheFolder = new CacheFolder();
	private DataConnectionHandle dataConnect;
	
	public DataConnectionHandle getDataConnect() {
		return dataConnect;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public boolean isLoggedIn() {
		return loggedIn;
	}
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	public String getCurrentDirectory() {
		return currentDirectory;
	}
	public void setCurrentDirectory(String currentDirectory) {
		this.currentDirectory = currentDirectory;
	}
	public CacheFolder getCacheFolder() {
		return cacheFolder;
	}
	public int getSessionID() {
		return sessionID;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Session(int sessionID) {
		super();
		this.sessionID = sessionID;
	}
	public Session(int sessionID, DataConnectionHandle dataConnect) {
		super();
		this.sessionID = sessionID;
		this.dataConnect = dataConnect;
	}
	
}
