package Model.Account;

import java.util.ArrayList;

public class AccountBO {

	public boolean findUser(String username) {
		//truy xuất dữ liệu nếu có username.
		
		//Dữ liệu giả
		ArrayList<String> usernames = new ArrayList<String>();
		usernames.add("LapDaiKa");
		usernames.add("Lap321");
		usernames.add("DanEm1");
		for (String user : usernames) {
			if(user.equals(username))
				return true;
		}
		return false;
	}

	public int checkLogin(String username, String password) {
		//truy xuất dữ liệu nếu có username.
		
		//Dữ liệu giả
		if((username.equals("LapDaiKa") && password.equals("12345"))
				||(username.equals("Lap321") && password.equals("123456"))){
			return 2; //id giả = 4
		}
		return -1; //không tìm thấy
	}

}
