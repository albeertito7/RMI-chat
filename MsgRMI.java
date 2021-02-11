import java.rmi.*;

public interface MsgRMI extends Remote {
		
	public String logIn(String username, String password, MsgListener listener) throws RemoteException;
	public String logOut(String username) throws RemoteException;
	public String newUser(String username, String password) throws RemoteException;
	public String newUser(String username, String password, String nameGroup) throws RemoteException;
	public void sendMsg(String from, String to, String msg) throws RemoteException;
	public String newGroup(String nameGroup) throws RemoteException;
	public String joinGroup(String username, String nameGroup) throws RemoteException;
	public void sendMsgGroup(String from, String nameGroup, String msg) throws RemoteException;
}