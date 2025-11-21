package Model.Bean;

public class User {
	private int usid;
	private String username;
	private String password;
	private int grid;
	public int getUsid() {
		return usid;
	}
	public void setUsid(int usid) {
		this.usid = usid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getGrid() {
		return grid;
	}
	public User(int usid, String username, String password, int grid) {
		super();
		this.usid = usid;
		this.username = username;
		this.password = password;
		this.grid = grid;
	}
	public User(int usid, String username, String password) {
		super();
		this.usid = usid;
		this.username = username;
		this.password = password;
	}
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
}
