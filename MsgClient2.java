import java.rmi.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class MsgClient2 {

	private static String username;

	public static void main(String args[]) {

		System.out.println("Starting Client ...");

		try {
			String host = null;
			if (args.length >=1)
				host = args[0];

			Registry registry = LocateRegistry.getRegistry(host);
			Remote servicioRemoto = registry.lookup("MsgRMI");
			MsgRMI servicioWhatsapp = (MsgRMI) servicioRemoto;
			System.out.println("Service located");

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			
			String[] tokens;
			for(;;) {
				tokens = (br.readLine()).split("\\s");
				if(tokens[0].equals("exit") && tokens.length == 1) {
					//servicioWhatsapp.logOut(username);
					System.out.println("You have been disconnected from the system");
					System.exit(0);
				}else if(tokens[0].equals("login") && tokens.length == 3) {
					username = tokens[1];
					System.out.println(servicioWhatsapp.logIn(tokens[1], tokens[2], (MsgListener) new MsgMonitor()));
				}else if (tokens[0].equals("logout") && tokens.length == 1) {
					//System.out.println(servicioWhatsapp.logOut(username));
				}
				else if(tokens[0].equals("newuser") && tokens.length == 3) {
					System.out.println(servicioWhatsapp.newUser(tokens[1], tokens[2]));
				}
				else if(tokens[0].equals("send") && tokens.length == 3) {
					servicioWhatsapp.sendMsg(username, tokens[1], tokens[2]);
				}
				else if(tokens[0].equals("newgroup") && tokens.length == 2) {
					System.out.println(servicioWhatsapp.newGroup(tokens[1]));
				}
				else if(tokens[0].equals("joingroup") && tokens.length == 2) {
					System.out.println(servicioWhatsapp.joinGroup(username, tokens[1]));
				}
				else {
					System.out.println("Command not recognized, try again ...");
				}
			}
		}
		catch(NotBoundException nbe) {
			System.err.println("There is no bulb service in the registry!");
		}
		catch(RemoteException re) {
			System.err.println("Remote Error - " + re);
		}
		catch(IOException ioe) {
			System.err.println("IOException occurred!");
		}
		catch(Exception e) {
			System.err.println("Error - " + e);
		}

	}
}