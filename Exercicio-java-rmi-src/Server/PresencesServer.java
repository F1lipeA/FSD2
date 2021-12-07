import java.lang.SecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class PresencesServer {
	
	String SERVICE_NAME="/PresencesRemote";

	private void bindRMI(Presences presences) throws RemoteException {
		
		System.getProperties().put( "java.security.policy", "./server.policy");

		if( System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		try { 
			LocateRegistry.createRegistry(1099);
		} catch( RemoteException e) {
			
		}
		try {
		  LocateRegistry.getRegistry("127.0.0.1",1099).rebind(SERVICE_NAME, presences);
		  } catch( RemoteException e) {
		  	System.out.println("Registry not found");
		  }
	}

	public PresencesServer() {
		super();
	}
	
	public void createPresences() {
		
		Presences presences = null;
		try {
			presences = new Presences();
		} catch (RemoteException e1) {
			System.err.println("unexpected error...");
			e1.printStackTrace();
		}
		
		try {
			bindRMI(presences);
		} catch (RemoteException e1) {
			System.err.println("erro ao registar o stub...");
			e1.printStackTrace();
		}
		
	}
}