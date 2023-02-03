package avpt.gr.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ToPgSql extends Remote {

//    void multiply(String[] str) throws RemoteException;

    void sendToPgSql(String[] args) throws RemoteException;

//    String insertToBase(ArrBlock32 arrBlock32, int codeRoad, int codeDepot, long guidFile) throws SQLException, RemoteException;
//
//    boolean doConnect(String ConnectionString) throws SQLException, ClassNotFoundException, RemoteException;

}
