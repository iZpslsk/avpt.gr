package avpt.gr.start;

import avpt.gr.blocks32.ArrBlock32;
import avpt.gr.chart_dataset.ChartDataset;
import avpt.gr.common.UtilsArmG;
import avpt.gr.database.Db;
import avpt.gr.rmi.ServerToPgSql;
import avpt.gr.sqlite_base.CreateInsertSQLite;
import avpt.gr.train.ArrTrains;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import static avpt.gr.common.UtilsArmG.SQULite_BASE_NAME;

public class arm_g {

    private static void makeServerToPgSQL(int port) throws RemoteException, AlreadyBoundException {
        ServerToPgSql serverToPgSql = new ServerToPgSql();
        Registry registry = LocateRegistry.createRegistry(port);
        Remote stub = UnicastRemoteObject.exportObject(serverToPgSql, 0);
        registry.bind("server.toPgSQL", stub);
        System.out.println("server is created");
    }

    public static void main(String[] args) {

        if (args.length > 4 && args[0].equalsIgnoreCase("-sendToPgSQL")) {
            Db.toBase(args, true);
        }
        else if (args.length > 1 && args[0].equalsIgnoreCase("-w")) {
            final String fileName = args[1];
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {

                    StartFrame frame = new StartFrame();
                    frame.setUndecorated(true);
                    frame.setBounds(0, 0, 0, 0);
                    frame.setVisible(true);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    try {
                        frame.openFromComStr(fileName);
                    } catch (ExecutionException e) {
                        UtilsArmG.outWriteAndExit(1, e.getMessage(), fileName, true);
                    } catch (InterruptedException e) {
                        UtilsArmG.outWriteAndExit(1, e.getMessage(), fileName, true);
                    }
                }
            });
        }
        else if (args.length > 1 && args[0].equalsIgnoreCase("-nw")) {
            final String fileName = args[1];
            ArrBlock32 arrBlock32 = null;
            try {
                arrBlock32 = new ArrBlock32(fileName, true);
            } catch (IOException e) {
                UtilsArmG.outWriteAndExit(1, e.getMessage(), fileName, true);
//				e.printStackTrace();
            }
            ChartDataset chartDataset = new ChartDataset(arrBlock32, true, true);
            ArrTrains trains = chartDataset.getArrTrains();
            try {
                String outFileName = UtilsArmG.replaceExtFile(fileName, ".output");	// out file рядом с файлом поездки
                trains.writeInfo(outFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (args.length > 1 && args[0].equalsIgnoreCase("-esmbs")) {
//            final String fileName = args[1];
//            ArrBlock32 arrBlock32 = null;
//            try {
//                arrBlock32 = new ArrBlock32(fileName, true);
//            } catch (IOException e) {
//                UtilsArmG.outWriteAndExit(1, e.getMessage(), fileName);
//            }
//            ChartDataset chartDataset = new ChartDataset(arrBlock32, true);
//            ArrTrains trains = chartDataset.getArrTrains();
//            try {
//                trains.writeToEsmbs();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
        else if (args.length > 0 && args[0].equalsIgnoreCase("-server")) {
            try {
                makeServerToPgSQL(Integer.parseInt(args[1]));
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (AlreadyBoundException e) {
                e.printStackTrace();
            }
        }
        else
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        CreateInsertSQLite.createBase(SQULite_BASE_NAME);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    StartFrame frame = new StartFrame();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setVisible(true);
                }
            });
    }
}
