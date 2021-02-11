import java.rmi.*;
import java.rmi.server.*;

public class MsgMonitor extends UnicastRemoteObject implements MsgListener
{
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_YELLOW = "\u001B[33m";

	public MsgMonitor() throws RemoteException {}

	public void userConnected(String username) throws RemoteException {
		System.out.println(ANSI_YELLOW + "User " + username + ", is now online" + ANSI_RESET);
	}

	public void sendMsg(String username, String dateTime, String msg) throws RemoteException {
		System.out.println(ANSI_YELLOW + "(" + dateTime + ") " + ANSI_RESET + username + ": " + msg);
	}

	public void sendMsg(String username, String dateTime, String nameGroup, String msg) throws RemoteException {
		System.out.println(ANSI_YELLOW + "(" + dateTime + ") " + ANSI_RESET + "Group: " + nameGroup + ", " + username + ": " + msg);
	}
}

/*
* Here we find the methods to be executed on the client, which were invoked from the server
*/