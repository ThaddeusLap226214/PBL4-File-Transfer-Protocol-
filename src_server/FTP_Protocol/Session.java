package FTP_Protocol;

public class Session {
	private int sessionID;		//mã phiên
	private String username;		//tên sau khi login
	private boolean loggedIn;	//Trạng thái login
	private String currentDirectory;	//thư mục hiện tại
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
	public int getSessionID() {
		return sessionID;
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
