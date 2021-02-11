import java.util.*;
import java.rmi.*;
import java.rmi.server.*;

import java.util.concurrent.ConcurrentHashMap;

public class MsgRMIServant implements MsgRMI {

	public MsgRMIServant() throws RemoteException {}

	// data structures
	private ConcurrentHashMap<String, User> users = new ConcurrentHashMap<String, User>();
	private ConcurrentHashMap<String, Group> groups = new ConcurrentHashMap<String, Group>();

	public String logIn(String username, String password, MsgListener listener) throws RemoteException {
		User user;
		if(MyUtils.isValid(username) && (user = users.get(username)) != null && !user.isConnected() && user.getPassword().equals(password)) {
			user.setMsgListener(listener);
			user.setIsConnected(true);
			notifyConnectedUsers(username);
			return "Connection established";
		} else {
			System.err.println(MyUtils.ANSI_RED + "Unable to connect user '" + username + "' with password '" + password + "' and listener '" + listener + "'" + MyUtils.ANSI_RESET);
			return MyUtils.ANSI_RED + "Error when connecting" + MyUtils.ANSI_RESET;
		}
	}

	public String logOut(String username) throws RemoteException {
		User user;
		if(MyUtils.isValid(username) && (user = users.get(username)) != null) {
			user.setIsConnected(false);
			return "You have been disconnected from the application";
		}else {
			System.err.println(MyUtils.ANSI_RED + "logOut: Error when connecting user '" + username + "'" + MyUtils.ANSI_RESET);
			return MyUtils.ANSI_RED + "Error when connecting" + MyUtils.ANSI_RESET;
		}
	}

	// new user creation and add it to the data structure
	public String newUser(String username, String password) throws RemoteException {
		if(!users.containsKey(username)) {
			users.put(username, new User(username, password));
			return "User successfully created";
		}else {
			return MyUtils.ANSI_YELLOW + "Warning: The user already exists, try with another name" + MyUtils.ANSI_RESET;
		}
	}

	// new user creation and add it to a group, and add it to the data structure
	public String newUser(String username, String password, String nameGroup) throws RemoteException {
		if(!users.containsKey(username) && groups.containsKey(nameGroup)) {
			users.put(username, new User(username, password, nameGroup));
			return "User successfully created";
		} else {
			return MyUtils.ANSI_YELLOW + "Warning: The new user could not be created, modify the parameters" + MyUtils.ANSI_RESET;
		}
	}

	// send a message to a specific user
	public void sendMsg(String from, String to, String msg) throws RemoteException {
		User user = users.get(to);
		try {
			if(MyUtils.isValid(from) && users.containsKey(from) && user != null && user.isConnected()) {
				user.getListener().sendMsg(from, MyUtils.getDate(), msg);
			}
		}
		catch (RemoteException re) { // control in case there is no connection through the listener
			System.err.println(MyUtils.ANSI_RED + "sendMsg: User '" + user.getUsername() + "' does not respond; deleting listener and setting its status to offline ..." + MyUtils.ANSI_RESET);
			user.setIsConnected(false);
			user.setMsgListener(null);
		}
	}

	// group creation and add it to the data structure
	public String newGroup(String nameGroup) {
		if(!groups.containsKey(nameGroup)) {
			groups.put(nameGroup, new Group(nameGroup));
			return "Group successfully created";
		}else {
			return MyUtils.ANSI_YELLOW + "Warning: The group already exists, try with another name" + MyUtils.ANSI_RESET;
		}
	}

	// adds the user to the group (if he/she was in another group, it is overwritten)
	public String joinGroup(String username, String nameGroup) {
		User user;
		Group group;
		if(MyUtils.isValid(username) && (user = users.get(username)) != null && (group = groups.get(nameGroup)) != null) {
			user.setGroup(group.getName());
			return "Added to the group correctly";
		}else {
			System.err.println(MyUtils.ANSI_RED + "joinGroup: El usuario '" + username + "' no se ha podido añadir al grupo '" + nameGroup + "'" + MyUtils.ANSI_RESET);
			return MyUtils.ANSI_RED + "Error adding to group" + MyUtils.ANSI_RESET;
		}
	}

	// send the message to all users belonging to the group and who are logged on
	public void sendMsgGroup(String username, String nameGroup, String msg) {
		User from;
		if(MyUtils.isValid(username) && (from = users.get(username)) != null && from.isConnected() && MyUtils.isValid(from.getGroup()) && from.getGroup().equals(nameGroup)) {
			for(Enumeration e = users.elements(); e.hasMoreElements();)
			{
				User user = (User) e.nextElement();

				try
				{
					if(!user.getUsername().equals(username) && user.isConnected() && MyUtils.isValid(user.getGroup()) && user.getGroup().equals(nameGroup)) {
						user.getListener().sendMsg(username, MyUtils.getDate(), nameGroup, msg);
					}
				}
				catch (RemoteException re) // control in case there is no connection through the listener
				{
					System.err.println(MyUtils.ANSI_RED + "sendMsgGroup: User '" + user.getUsername() + "' does not respond; deleting listener and setting its status to offline ..." + MyUtils.ANSI_RESET);
					user.setIsConnected(false);
					user.setMsgListener(null);
				}
			}
		}
	}

	// notify all previously logged-in users that a user has logged in that a user has logged in
	private void notifyConnectedUsers (String username) {
		for(Enumeration e = users.elements(); e.hasMoreElements();)
		{
			User user = (User) e.nextElement();

			try
			{
				if(!user.getUsername().equals(username) && user.isConnected()) {
					user.getListener().userConnected(username);
				}
			}
			catch (RemoteException re) // control in case there is no connection through the listener
			{
				System.err.println(MyUtils.ANSI_RED + "notifyConnectedUsers: User '" + user.getUsername() + "' does not respond; deleting listener and setting its status to offline ..." + MyUtils.ANSI_RESET);
				user.setIsConnected(false);
				user.setMsgListener(null);
			}
		}
	}
}