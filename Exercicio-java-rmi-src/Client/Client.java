import java.rmi.registry.LocateRegistry;
import java.util.Iterator;
import java.util.Vector;

public class Client {
	
	String SERVICE_NAME="/PresencesRemote";
	String[] args;
	public Client(String[] args)  {
		this.args = args;
	}

	public void putPresence() {
		if (args.length != 2) {
			// System.out.println("Erro: use java ClientApp <ipClient> <ipNameServer>");
			System.exit(-1);
		}

		try {

			NewPresencesInterface cl = new NewPresences();

			PresencesInterface presences = (PresencesInterface) LocateRegistry.getRegistry(args[1]).lookup(SERVICE_NAME);
			
			Vector<String> presencesList = presences.getPresences(args[0], cl);
			
			Iterator<String> it = presencesList.iterator();
			while(it.hasNext())
				System.out.println(it.next());
		} catch (Exception e) {
			System.err.println("Error");
			e.printStackTrace();
		}
	}    
}

