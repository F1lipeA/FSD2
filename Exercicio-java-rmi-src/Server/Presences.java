import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


public class Presences extends UnicastRemoteObject implements PresencesInterface {
	
	private Hashtable<String, IPInfo> presentIPs = new Hashtable<String, IPInfo>();
	
	public Presences() throws RemoteException {
		super();
	}

	public Vector<String> getPresences(String IPAddress, NewPresencesInterface cl) throws RemoteException {
		
		long actualTime = new Date().getTime();
		
		synchronized(this) {
			if (presentIPs.containsKey(IPAddress)) {
				IPInfo newIp = presentIPs.get(IPAddress);
				newIp.setLastSeen(actualTime);
				newIp.setCl(cl);
			}
			else {
				IPInfo newIP = new IPInfo(IPAddress, actualTime, cl);
				presentIPs.put(IPAddress,newIP);
			}
		}
		for (Enumeration<IPInfo> e = presentIPs.elements(); e.hasMoreElements(); ) {
			IPInfo element = e.nextElement();
			if (element.getIP() != IPAddress) {
				try {
				  element.getCl().setNewPresence(IPAddress);
				} catch( RemoteException exception) {
		  			System.out.println("Client "+ element.getIP() + " not available");
		  			synchronized(this) {
		  				presentIPs.remove(element.getIP());
		  			}
		  		}
			}
		}
		return getIPList();
	}
	
	private Vector<String> getIPList(){
		Vector<String> result = new Vector<String>();
		for (Enumeration<IPInfo> e = presentIPs.elements(); e.hasMoreElements(); ) {
			IPInfo element = e.nextElement();
			if (!element.timeOutPassed(180*1000)) {
				result.add(element.getIP());
			}
		}
		return result;
	}
}

class IPInfo {
	
	private String ip;
	private long lastSeen;
	private NewPresencesInterface cl;

	public IPInfo(String ip, long lastSeen, NewPresencesInterface cl) {
		this.ip = ip;
		this.lastSeen = lastSeen;
		this.cl = cl;
	}

	public String getIP () {
		return this.ip;
	}

    public NewPresencesInterface getCl () {
		return this.cl;
	}

	public void setLastSeen(long time){
		this.lastSeen = time;
	}

	public void setCl(NewPresencesInterface cl){
		this.cl = cl;
	}

	public boolean timeOutPassed(int timeout){
		boolean result = false;
		long timePassedSinceLastSeen = new Date().getTime() - this.lastSeen;
		if (timePassedSinceLastSeen >= timeout)
			result = true;
		return result;
	}
}



