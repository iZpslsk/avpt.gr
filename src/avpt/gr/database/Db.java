package avpt.gr.database;

import avpt.gr.common.UtilsArmG;
import java.io.IOException;
import java.sql.*;

public class Db {

    public static void toBase(String[] args, boolean isExit) {
        final String fileName = args[1];
        try {
            UtilsArmG.checkSizeFile(fileName);
        } catch (IOException e) {
            UtilsArmG.outWriteAndExit(300, e.getMessage(), fileName, isExit);
            return;
        }
        int codeRoad = Integer.parseInt(args[2]);
        int codeDepo = Integer.parseInt(args[3]);
        String connectionString = args[4];
        long guidFile = -1;
        if (args.length > 5)
            guidFile = Long.parseLong(args[5]);

        String[] strings = connectionString.split("[;]+");
        String host = "";
        String nameDB = "";
        String user = "";
        String pass = "";
        for (String str : strings) {
            if (str.replaceAll("\\s+","").toLowerCase().matches("^host=.*"))
                host = str.split("[=]")[1];
            else if (str.replaceAll("\\s+","").toLowerCase().matches("^databasename=.*"))
                nameDB = str.split("[=]")[1];
            else if (str.replaceAll("\\s+","").toLowerCase().matches("^user=.*"))
                user = str.split("[=]")[1];
            else if (str.replaceAll("\\s+","").toLowerCase().matches("^password=.*"))
                pass = str.split("[=]")[1];
        }
        toPgSql(fileName, host, nameDB, user, pass, codeRoad, codeDepo, guidFile, isExit);
    }

    public static void toPgSql(String fileName,
                               String host,
                               String nameDb,
                               String user,
                               String pass,
                               int codeRoad,
                               int codeDepo,
                               long guidFile,
                               boolean isExists) {
        try {
            new DataToBase(fileName, host, nameDb, user, pass, codeRoad, codeDepo, guidFile);
            UtilsArmG.outWriteAndExit(0, "успешно", fileName, isExists);
        } catch (SQLException e) {
            int errCode;
            try {
                errCode = Integer.parseInt(e.getSQLState());
            }
            catch (NumberFormatException var3) {
                errCode = 303;
            }
            UtilsArmG.outWriteAndExit(errCode, e.getMessage(), fileName, isExists);
        } catch (ClassNotFoundException e) {
            UtilsArmG.outWriteAndExit(302, e.getMessage(), fileName, isExists);
        }
    }
}
