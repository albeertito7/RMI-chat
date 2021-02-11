import java.rmi.*;

public interface MsgListener extends Remote {
	
	public void userConnected(String value) throws RemoteException;
	public void sendMsg(String username, String dateTime, String msg) throws RemoteException;
	public void sendMsg(String username, String dateTime, String nameGroup, String msg) throws RemoteException;
}