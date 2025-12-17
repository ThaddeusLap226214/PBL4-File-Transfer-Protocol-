package FTPControl;

import java.net.InetAddress;
import java.time.LocalDateTime;

public interface FTPControlEventListener {
	void onClientCommand(LocalDateTime time, int sessionID, InetAddress host, String command);
	void onServerResponse(LocalDateTime time, int sessionID, InetAddress host, String response);
	void onClientLoginSuccess(LocalDateTime time, int id, InetAddress IP, String Username);
}
