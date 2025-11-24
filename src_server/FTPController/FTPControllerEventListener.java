package FTPController;

public interface FTPControllerEventListener {
	void onClientCommand(int session, String username, String clientAddress, String command);
	void onServerRequest(int session, String username, String clientAddress, String request);
}
