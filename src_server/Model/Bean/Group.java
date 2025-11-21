package Model.Bean;

public class Group {
	private int grid;
	private String groupName;
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public int getGrid() {
		return grid;
	}
	public Group(int grid, String groupName) {
		super();
		this.grid = grid;
		this.groupName = groupName;
	}
	public Group() {
		super();
		// TODO Auto-generated constructor stub
	}
}
