import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class MsgServer extends MsgRMIServant {

	public MsgServer() throws RemoteException {};

	public static void main(String args[]) {

		System.out.println("Starting Server ...");

		if (System.getSecurityManager() == null){ // instantiate the firewall
			System.setSecurityManager(new SecurityManager());
		}

		try {

			MsgRMIServant servicio = new MsgRMIServant();
			MsgRMI msg = (MsgRMI) UnicastRemoteObject.exportObject(servicio, 0);

			String host = null;
			if (args.length >=1)
				host = args[0];

			Registry registry = LocateRegistry.getRegistry(host); //createRegistry(1022);
			registry.rebind("MsgRMI", msg);
			System.out.println("Server ready");
		}
		catch (RemoteException re) {
			System.err.println("Remote Error - " + re);
		}
		catch (Exception e) {
			System.err.println("Error - " + e);
		}
	}
}