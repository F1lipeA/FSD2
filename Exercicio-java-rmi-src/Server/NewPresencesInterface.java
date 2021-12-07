import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NewPresencesInterface extends Remote {

	public void setNewPresence(String IPAddress) throws RemoteException;

}