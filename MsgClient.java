import java.rmi.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class MsgClient {

	private static String username; // parameter that will be used to keep in mind the established session, this will be the user with which we will be logged in, and will allow us to do the following actions

	public static void main(String args[]) {

		System.out.println("Starting Client ...");

		if (System.getSecurityManager() == null) { // instantiate the firewall
			System.setSecurityManager(new SecurityManager());
		}

		try {
			String host = null;
			if (args.length >=1)
				host = args[0];

			Registry registry = LocateRegistry.getRegistry(host);
			Remote servicioRemoto = registry.lookup("MsgRMI");
			MsgRMI servicioWhatsapp = (MsgRMI) servicioRemoto;
			System.out.println("Service located");

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); // input buffer
			
			// infinite loop
			String[] tokens;
			for(;;) {
				tokens = (br.readLine()).split("\\s"); // we read the input data and separate them by spaces
				if(tokens[0].equals("Exit") && tokens.length == 1) {
					servicioWhatsapp.logOut(username); // using the session parameter we log outs
					System.out.println("You have been disconnected from the system");
					System.exit(0);
				}else if(tokens[0].equals("Login") && tokens.length == 3) {
					username = tokens[1]; // assign
					System.out.println(servicioWhatsapp.logIn(tokens[1], tokens[2], (MsgListener) new MsgMonitor()));
				}else if (tokens[0].equals("Logout") && tokens.length == 1) {
					System.out.println(servicioWhatsapp.logOut(username));
				}
				else if(tokens[0].equals("NewUser")) {
					if(tokens.length == 3) {
						System.out.println(servicioWhatsapp.newUser(tokens[1], tokens[2]));
					}
					else if(tokens.length == 5 && tokens[3].equals("-g")) {
						System.out.println(servicioWhatsapp.newUser(tokens[1], tokens[2], tokens[4]));
					}else {
						System.out.println("");
					}
				}
				else if(tokens[0].equals("SendMsg")) {
					if(tokens.length >= 3 && !tokens[1].equals("-g")) {
						servicioWhatsapp.sendMsg(username, tokens[1], getSlice(tokens, 2, tokens.length));
					} 
					else if(tokens.length >= 4 && tokens[1].equals("-g")){
						servicioWhatsapp.sendMsgGroup(username, tokens[2], getSlice(tokens,3, tokens.length));
						
					}
				}
				else if(tokens[0].equals("NewGroup") && tokens.length == 2) {
					System.out.println(servicioWhatsapp.newGroup(tokens[1]));
				}
				else if(tokens[0].equals("JoinGroup") && tokens.length == 2) {
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

	private static String getSlice(String[] arr, int start, int end) { // to build the message
	        String slice = ""; 
	  
	        for (int i = start; i < end; i++) { 
	            slice += arr[i] + " "; 
	        } 
	  
	        return slice; 
	    } 
}