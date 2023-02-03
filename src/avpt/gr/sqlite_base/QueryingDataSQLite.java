package avpt.gr.sqlite_base;

import avpt.gr.common.UtilsArmG;
import org.threeten.bp.LocalDateTime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryingDataSQLite {
    private final Connection conn;
    private final LocalDateTime beginDateTime;
    private final LocalDateTime endDateTime;
    private final int codeRoad;
    private final int nTch;

    public QueryingDataSQLite(Connection conn, LocalDateTime beginDateTime, LocalDateTime endDateTime, int codeRoad, int nTch) throws SQLException {
        this.conn = conn;
        this.beginDateTime = beginDateTime;
        this.endDateTime = endDateTime;
        this.codeRoad = codeRoad;
        this.nTch = nTch;
    //    fillArrTrainQuery();
    }

    public static long getCount_trains(
            Connection conn, LocalDateTime beginDateTime, LocalDateTime endDateTime,
            int codeRoad, int numTch, int numLoc, int typeLoc) throws SQLException {

        final String sql =
                "SELECT count() AS cnt " +
                        "FROM file f " +
                        "INNER JOIN avpt.gr.train t ON f.file_id = t.file_id " +
                        "INNER JOIN driver d ON d.driver_id = t.driver_id " +
                        "INNER JOIN locomotive l ON l.locomotive_id = t.locomotive_id " +
                        "INNER JOIN locomotive_type lt ON lt.code_type_loc = l.code_type_loc " +
//                        "INNER JOIN section s ON s.train_id = t.train_id " +
                        "WHERE date_begin BETWEEN ? AND ? " +
                        "   AND (t.code_road = ? OR 0 = ?) " +
                        "   AND (t.num_tch = ? OR 0 = ?) " +
                        "   AND (l.num_loc = ? OR -1 = ?) " +
                        "   AND (lt.code_type_loc = ? OR 0 = ?) " +
                        "ORDER BY t.train_id;";

        final int nDateBegin = 1;
        final int nDateEnd = 2;
        final int nCodeRoad1 = 3;
        final int nCodeRoad2 = 4;
        final int nTchNum1 = 5;
        final int nTchNum2 = 6;
        final int nNumLoc1 = 7;
        final int nNumLoc2 = 8;
        final int nTypeLoc1 = 9;
        final int nTypeLoc2 = 10;

        PreparedStatement prepStat = conn.prepareStatement(sql);
        prepStat.setString(nDateBegin, beginDateTime.format(UtilsArmG.formatDateTimeSQLite));
        prepStat.setString(nDateEnd, endDateTime.format(UtilsArmG.formatDateTimeSQLite));
        prepStat.setInt(nCodeRoad1, codeRoad);
        prepStat.setInt(nCodeRoad2, codeRoad);
        prepStat.setInt(nTchNum1, numTch);
        prepStat.setInt(nTchNum2, numTch);
        prepStat.setInt(nNumLoc1, numLoc);
        prepStat.setInt(nNumLoc2, numLoc);
        prepStat.setInt(nTypeLoc1, typeLoc);
        prepStat.setInt(nTypeLoc2, typeLoc);
        return prepStat.executeQuery().getLong(1);
    }

    public static ResultSet getResultSet_vsc_diagn(
            Connection conn, LocalDateTime beginDateTime, LocalDateTime endDateTime,
            int codeRoad, int numTch, int numLoc, int typeLoc, int codeAsoup) throws SQLException {

        final String sql =
                "SELECT lt.name_type_loc, l.num_loc, t.date_begin, t.train_num, t.route_name, t.seconds, t.distance, t.distance_auto, " +
                        "CASE WHEN t.distance = 0 THEN 0 ELSE t.distance_auto * 100 / t.distance END AS distance_auto_percent, " +
                        "vd.type_vsc, vd.main_link_modem_perc, vd.main_link_vsc_perc, vd.slave_is_on_perc, vd.slave_link_modem_perc, " +
                        "vd.slave_link_vsc_perc, vd.num_link_loc, vd.test_thrust, vd.test_brake, vd.u_max, vd.is_alsn, " +
                        "f.file_name, t.num_section " +
                "FROM file f " +
                        " INNER JOIN avpt.gr.train t ON f.file_id = t.file_id " +
                        " INNER JOIN driver d ON d.driver_id = t.driver_id " +
                        " INNER JOIN locomotive l ON l.locomotive_id = t.locomotive_id " +
                        " INNER JOIN locomotive_type lt ON lt.code_type_loc = l.code_type_loc AND lt.code_asoup = l.code_asoup " +
                        " INNER JOIN vsc_diagnostic vd ON vd.train_id = t.train_id " +
                        " INNER JOIN road r ON r.code_road = t.code_road " +
//                        "WHERE f.date_save BETWEEN ? AND ? " +
                        "WHERE date_begin BETWEEN ? AND ? " +
                        "    AND (t.code_road = ? OR 0 = ?) " +
                        "    AND (t.num_tch = ? OR 0 = ?) " +
                        "    AND (l.num_loc = ? OR -1 = ?) " +
                        "    AND (lt.code_type_loc = ? OR 0 = ?) " +
                        "    AND (lt.code_asoup = ? OR 0 = ?) " +
                "ORDER BY l.num_loc, t.date_begin";

        final int nDateBegin = 1;
        final int nDateEnd = 2;
        final int nCodeRoad1 = 3;
        final int nCodeRoad2 = 4;
        final int nTchNum1 = 5;
        final int nTchNum2 = 6;
        final int nNumLoc1 = 7;
        final int nNumLoc2 = 8;
        final int nTypeLoc1 = 9;
        final int nTypeLoc2 = 10;
        final int nCodeAsoup1 = 11;
        final int nCodeAsoup2 = 12;

        PreparedStatement prepStat = conn.prepareStatement(sql);
        prepStat.setString(nDateBegin, beginDateTime.format(UtilsArmG.formatDateTimeSQLite));
        prepStat.setString(nDateEnd, endDateTime.format(UtilsArmG.formatDateTimeSQLite));
        prepStat.setInt(nCodeRoad1, codeRoad);
        prepStat.setInt(nCodeRoad2, codeRoad);
        prepStat.setInt(nTchNum1, numTch);
        prepStat.setInt(nTchNum2, numTch);
        prepStat.setInt(nNumLoc1, numLoc);
        prepStat.setInt(nNumLoc2, numLoc);
        prepStat.setInt(nTypeLoc1, typeLoc);
        prepStat.setInt(nTypeLoc2, typeLoc);
        prepStat.setInt(nCodeAsoup1, codeAsoup);
        prepStat.setInt(nCodeAsoup2, codeAsoup);

        return  prepStat.executeQuery();

    }

    public static ResultSet getResultSet_trains(
            Connection conn, LocalDateTime beginDateTime, LocalDateTime endDateTime,
            int codeRoad, int numTch, int numLoc, int typeLoc, int codeAsoup) throws SQLException {

        final String sql =
                "SELECT " +
                        "s.num, t.train_id, t.date_begin, t.train_num, d.tab_num, lt.name_type_loc, l.num_loc, t.stations_start_end_name, t.work, t.distance, " +
                        "t.distance_auto, CASE WHEN t.distance = 0 THEN 0 ELSE t.distance_auto * 100 / t.distance END AS distance_auto_percent, " +
                        "t.distance_prompt, CASE WHEn t.distance_prompt = 0 THEN 0 ELSE t.distance_prompt * 100 / t.distance END AS distance_prompt_percent, t.seconds, " +
                        "s.act, s.rec, t.speed, t.speed_move, t.v_lim_cnt, t.v_lim_len, t.wags_cnt, t.weight, t.wags_empty_cnt, t.route_name, f.file_name, f.date_save, " +
                        "t.route_name, r.name_road, t.num_tch, t.ver_po, t.date_map, t.is_shed_load, t.num_section " +
                        "FROM file f " +
                        "INNER JOIN avpt.gr.train t ON f.file_id = t.file_id " +
                        "INNER JOIN driver d ON d.driver_id = t.driver_id " +
                        "INNER JOIN locomotive l ON l.locomotive_id = t.locomotive_id " +
                        "INNER JOIN locomotive_type lt ON lt.code_type_loc = l.code_type_loc AND lt.code_asoup = l.code_asoup " +
                        "INNER JOIN section s ON s.train_id = t.train_id " +
                        "INNER JOIN road r ON r.code_road = t.code_road " +
                        "WHERE date_begin BETWEEN ? AND ? " +
                        "    AND (t.code_road = ? OR 0 = ?) " +
                        "    AND (t.num_tch = ? OR 0 = ?) " +
                        "    AND (l.num_loc = ? OR -1 = ?) " +
                        "    AND (lt.code_type_loc = ? OR 0 = ?) " +
                        "    AND (lt.code_asoup = ? OR 0 = ?) " +
                        "ORDER BY t.train_id;";

        final int nDateBegin = 1;
        final int nDateEnd = 2;
        final int nCodeRoad1 = 3;
        final int nCodeRoad2 = 4;
        final int nTchNum1 = 5;
        final int nTchNum2 = 6;
        final int nNumLoc1 = 7;
        final int nNumLoc2 = 8;
        final int nTypeLoc1 = 9;
        final int nTypeLoc2 = 10;
        final int nCodeAsoup1 = 11;
        final int nCodeAsoup2 = 12;

        PreparedStatement prepStat = conn.prepareStatement(sql);
        prepStat.setString(nDateBegin, beginDateTime.format(UtilsArmG.formatDateTimeSQLite));
        prepStat.setString(nDateEnd, endDateTime.format(UtilsArmG.formatDateTimeSQLite));
        prepStat.setInt(nCodeRoad1, codeRoad);
        prepStat.setInt(nCodeRoad2, codeRoad);
        prepStat.setInt(nTchNum1, numTch);
        prepStat.setInt(nTchNum2, numTch);
        prepStat.setInt(nNumLoc1, numLoc);
        prepStat.setInt(nNumLoc2, numLoc);
        prepStat.setInt(nTypeLoc1, typeLoc);
        prepStat.setInt(nTypeLoc2, typeLoc);
        prepStat.setInt(nCodeAsoup1, codeAsoup);
        prepStat.setInt(nCodeAsoup2, codeAsoup);

        return  prepStat.executeQuery();
    }
}
