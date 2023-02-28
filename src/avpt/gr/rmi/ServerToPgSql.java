package avpt.gr.rmi;

import avpt.gr.database.Db;

import java.rmi.RemoteException;

public class ServerToPgSql implements ToPgSql {

    @Override
    public void sendToPgSql(String[] args) throws RemoteException {
//        Db.toPgSql(args, false);
        Db.toBase(args, false);
//        if (args.length > 4 && args[0].equalsIgnoreCase("-sendToPgSQL")) {
//            final String fileName = args[1];
//            try {
//                UtilsArmG.checkSizeFile(fileName);
//            } catch (IOException e) {
//                UtilsArmG.outWriteAndExit(300, e.getMessage(), fileName, false);
//            }
//            int codeRoad = Integer.parseInt(args[2]);
//            int codeDepo = Integer.parseInt(args[3]);
//            String connectionString = args[4];
//            long guidFile = -1;
//            if (args.length > 5)
//                guidFile = Long.parseLong(args[5]);
//
//            ArrBlock32 arrBlock32 = null;
//            try {
//                arrBlock32 = new ArrBlock32(fileName, true);
//            } catch (IOException e) {
//                UtilsArmG.outWriteAndExit(301, e.getMessage(), fileName, false);
//            }
//            ChartDataset chartDataset = new ChartDataset(arrBlock32, true, true);
//            ArrTrains trains = chartDataset.getArrTrains();
//
//            Db db = new Db(trains);
//            String messMilliseconds = "";
//            long msToBaseStart = System.currentTimeMillis();
//            try {
//                if (db.doConnect(connectionString)) {
//                    messMilliseconds = db.insertToBase(arrBlock32, codeRoad, codeDepo, guidFile);
//                    long msToBase = System.currentTimeMillis() - msToBaseStart;
//                    UtilsArmG.outWriteAndExit(0, "успешно\n\n" + messMilliseconds + "Insert to base:    " + msToBase + " ms\n", fileName, false);
//                }
//                else {
//                    UtilsArmG.outWriteAndExit(302, "нет соединения с сервером", fileName, false);
//                }
//            } catch (SQLException e) {
//                int errCode = parseToInt(e.getSQLState(), 303);
//                UtilsArmG.outWriteAndExit(errCode, e.getMessage(), fileName, false);
//            } catch (ClassNotFoundException e) {
//                UtilsArmG.outWriteAndExit(302, e.getMessage(), fileName, false);
//            }
////            long msToBase = System.currentTimeMillis() - msToBaseStart;
////            UtilsArmG.outWriteAndExit(0, "успешно\n\n" + messMilliseconds + "Insert to base:    " + msToBase + " ms\n", fileName, false);
////            System.out.println(fileName);
//        }
    }

//    @Override
//    public void multiply(String[] str) throws RemoteException {
//        System.out.println(str[0]);
//    }

//    @Override
//    public String insertToBase(ArrBlock32 arrBlock32, int codeRoad, int codeDepot, long guidFile) throws SQLException, RemoteException {
//        return null;
//    }
//
//    @Override
//    public boolean doConnect(String ConnectionString) throws SQLException, ClassNotFoundException, RemoteException {
//        return false;
//    }
}
