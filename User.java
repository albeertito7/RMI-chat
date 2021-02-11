import java.rmi.*;

public class User {
	
	private String username;
	private String password;
	private MsgListener listener;
	private String nameGroup; // in this version each user can only belong to one group, if we wanted to extend this limit we would use a data structure to store the nameGroup (id) of the groups to which the user is added
	private boolean isConnected;;

	public User (String username, String password) {
		this.username = username;
		this.password = password;
		this.isConnected = false;
	}

	public User (String username, String password, String nameGroup) {
		this.username = username;
		this.password = password;
		this.nameGroup = nameGroup;
		this.isConnected = false;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public MsgListener getListener() {
		return this.listener;
	}

	public void setMsgListener(MsgListener listener) {
		this.listener = listener;
	}

	public String getGroup() {
		return this.nameGroup;
	}

	public void setGroup(String nameGroup) {
		this.nameGroup = nameGroup;
	}

	public boolean isConnected() {
		return this.isConnected;
	}

	public void setIsConnected(boolean value) {
		this.isConnected = value;
	}
}

/*
* Class representing the User
* It is the model
*/