import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;


public interface PresencesInterface extends Remote {

	public Vector<String> getPresences(String IPAddress, NewPresencesInterface cl) throws RemoteException;

}
