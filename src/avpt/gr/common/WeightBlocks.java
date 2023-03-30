package avpt.gr.common;

import avpt.gr.chart_dataset.SeriesLines;
import org.jfree.chart.plot.XYPlot;

import java.util.prefs.Preferences;

import static avpt.gr.graph.ChartArm.*;

public class WeightBlocks {

    public final static int W_TRAIN = 12;
    public final static int W_LINE = 100;
    public final static int W_MAP = 70 * W_LINE / 100;
    public final static int W_SIGNAL = 70 * W_LINE / 100;
    public final static int W_GANTT = 5 * W_LINE / 100;
    public final static int W_GANTT_UATL = 10 * W_LINE / 100;
    public final static int W_PROF = 20 * W_LINE / 100;
    public final static int W_POS = 20 * W_LINE / 100;
    public final static int W_KEY = 200 * W_LINE / 100;
    public final static int W_LINK = 7 * W_LINE / 100;;
    private final static int EMPTY = 0;
    // высота блоков линий
    private static int weight_train = W_TRAIN;
    private static boolean visibleTrain = true;
    private static int weight_voltage_cs = W_LINE;
    private static boolean visibleVoltage_cs = false;
    private static int weight_voltage = W_LINE;
    private static boolean visibleVoltage = true;

    private static int weight_amperage_common = W_LINE;
    private static boolean visibleAmperageCommon = false;
    private static int weight_amperage_anchor = W_LINE;
    private static boolean visibleAmperageAnchor = true;
    private static int weight_amperage_excitation = W_LINE;
    private static boolean visibleAmperageExcitation = false;

    private static int weight_amperage_engine = W_LINE;
    private static boolean visibleAmperageEngine = true;
    private static int weight_amperage = W_LINE;
    private static boolean visibleAmperage = true;

    private static int weight_consumption = W_LINE;
    private static boolean visibleConsumption = false;
    private static int weight_power = W_LINE;
    private static boolean visiblePower = true;
    private static int weight_tail = W_LINE;
    private static boolean visibleTail = false;
    private static int weight_press = W_LINE;
    private static boolean visiblePress = true;
    private static int weight_speed = W_LINE;
    private static boolean visibleSpeed = true;

    private static int weight_prof = W_PROF;
    private static boolean visibleProfile = false;
    private static int weight_position = W_POS;
    private static boolean visiblePosition = true;
    private static int weight_map = W_MAP;
    private static boolean visibleMap = true;

    private static int weight_alsn = W_GANTT;
    private static boolean visibleAlsn = true;
    private static int weight_alsn_br = W_GANTT;
    private static boolean visibleAlsnBr = false;
    private static int weight_alsn_club = W_GANTT;
    private static boolean visibleAlsnClub = false;

    private static int weight_auto_drive = W_GANTT;
    private static boolean visibleAutoDrive = true;

    private static int weight_cabin = W_GANTT;
    private static boolean visibleCabin = false;

    private static int weight_kkm = W_GANTT;
    private static boolean visibleKkm = false;
    private static int weight_kkm2 = W_GANTT;
    private static boolean visibleKkm2 = false;
    private static int weight_kkm_vl10 = W_GANTT;
    private static boolean visibleKkmVl10 = false;
    private static int weight_kkm_s5 = W_GANTT;
    private static boolean visibleKkmS5 = false;

    private static int weight_bhv = W_GANTT;
    private static boolean visibleBhv = false;

    private static int weight_main_control = W_GANTT;
    private static boolean visibleMainControl = false;

    private static int weight_pneumatic1 = W_GANTT;
    private static boolean visiblePneumatic1 = false;

    private static int weight_pneumatic2 = W_GANTT;
    private static boolean visiblePneumatic2 = false;

    private static int weight_pneumatic_usavp = W_GANTT;
    private static boolean visiblePneumatic_usavp = false;

    private static int weight_uatl = W_GANTT_UATL;
    private static boolean visibleUatl = false;

    private static int weight_push_key = W_KEY;
    private static boolean visiblePushKey = false;

    private static int weight_rev_control = W_GANTT;
    private static boolean visibleRevControl = false;

    private static int weight_schema = W_GANTT;
    private static boolean visibleSchema = false;

    private static int weight_vsc = W_GANTT;
    private static boolean visibleVsc = false;

    // сигналы
    private static int weight_signals = W_SIGNAL;
    private static boolean visibleSignals = true;
    private static int weight_signals_autodis = W_SIGNAL;
    private static boolean visibleSignalsAutodis = false;
    private static int weight_signals_bhv = W_SIGNAL;
    private static boolean visibleSignalsBhv = false;
    private static int weight_signals_ted = W_SIGNAL;
    private static boolean visibleSignalsTed = false;
    private static int weight_signals_ted_s5k = W_SIGNAL;
    private static boolean visibleSignalsTedS5k = false;
    private static int weight_signals_link = W_LINK;
    private static boolean visibleSignalsLink = false;

    private static boolean modified = false;

    /**
     * @param isAsim - видимость блоков для асим по умолчанию
     */
    public static void setDefaultAsim(boolean isAsim) {
     //   visibleVoltage_cs = isAsim;
     //   visibleAmperageCommon = isAsim;
    }

    public static int getWeight_train(boolean isInvert) {
        if (isInvert) visibleTrain = !visibleTrain;
        return visibleTrain ? weight_train : EMPTY;
    }

    public static void setWeight_train(int weight_train) {
        WeightBlocks.weight_train = weight_train;
    }

    public static boolean isVisibleTrain(boolean isInvert) {
        if (isInvert) visibleTrain = !visibleTrain;
        return visibleTrain;
    }

    public static void setVisibleTrain(boolean visibleTrain) {
        WeightBlocks.visibleTrain = visibleTrain;
    }

    public static int getWeight_voltage_cs(boolean isInvert) {
        if (isInvert) visibleVoltage_cs = !visibleVoltage_cs;
        return visibleVoltage_cs ? weight_voltage_cs : EMPTY;
    }

    public static void setWeight_voltage_cs(int weight_voltage_cs) {
        WeightBlocks.weight_voltage_cs = weight_voltage_cs;
    }

    public static boolean isVisibleVoltage_cs(boolean isInvert) {
        if (isInvert) visibleVoltage_cs = !visibleVoltage_cs;
        return visibleVoltage_cs;
    }

    public static void setVisibleVoltage_cs(boolean visibleVoltage_cs) {
        WeightBlocks.visibleVoltage_cs = visibleVoltage_cs;
    }

    // voltage 1 sec
    public static int getWeight_voltage(boolean isInvert) {
        if (isInvert) visibleVoltage = !visibleVoltage;
        return visibleVoltage ? weight_voltage : EMPTY;
    }

    public static void setWeight_voltage(int weight_voltage) {
        WeightBlocks.weight_voltage = weight_voltage;
    }

    public static boolean isVisibleVoltage1(boolean isInvert) {
        if (isInvert) visibleVoltage = !visibleVoltage;
        return visibleVoltage;
    }

    public static void setVisibleVoltage(boolean visibleVoltage) {
        WeightBlocks.visibleVoltage = visibleVoltage;
    }

    public static int getWeight_amperage_common(boolean isInvert) {
        if (isInvert) visibleAmperageCommon = !visibleAmperageCommon;
        return visibleAmperageCommon ? weight_amperage_common : EMPTY;
    }

    public static void setWeight_amperage_common(int weight_amperage_common) {
        WeightBlocks.weight_amperage_common = weight_amperage_common;
    }

    public static boolean isVisibleAmperageCommon(boolean isInvert) {
        if (isInvert) visibleAmperageCommon = !visibleAmperageCommon;
        return visibleAmperageCommon;
    }

    public static void setVisibleAmperageCommon(boolean visibleAmperageCommon) {
        WeightBlocks.visibleAmperageCommon = visibleAmperageCommon;
    }

    // amperage anchor
    public static int getWeight_amperage_anchor(boolean isInvert) {
        if (isInvert) visibleAmperageAnchor = !visibleAmperageAnchor;
        return visibleAmperageAnchor ? weight_amperage_anchor : EMPTY;
    }

    public static void setWeight_amperage_anchor(int weight_amperage_anchor) {
        WeightBlocks.weight_amperage_anchor = weight_amperage_anchor;
    }

    public static boolean isVisibleAmperage1(boolean isInvert) {
        if (isInvert) visibleAmperageAnchor = !visibleAmperageAnchor;
        return visibleAmperageAnchor;
    }

    public static void setVisibleAmperageAnchor(boolean visibleAmperageAnchor) {
        WeightBlocks.visibleAmperageAnchor = visibleAmperageAnchor;
    }

    // amperage excitation
    public static int getWeight_amperage_excitation(boolean isInvert) {
        if (isInvert) visibleAmperageExcitation = !visibleAmperageExcitation;
        return visibleAmperageExcitation ? weight_amperage_excitation : EMPTY;
    }

    public static void setWeight_amperage_excitation(int weight_amperage_excitation) {
        WeightBlocks.weight_amperage_excitation = weight_amperage_excitation;
    }

    public static boolean isVisibleAmperage2(boolean isInvert) {
        if (isInvert) visibleAmperageExcitation = !visibleAmperageExcitation;
        return visibleAmperageExcitation;
    }

    public static void setVisibleAmperageExcitation(boolean visibleAmperageExcitation) {
        WeightBlocks.visibleAmperageExcitation = visibleAmperageExcitation;
    }

    // amperage engine
    public static int getWeight_amperage_engine(boolean isInvert) {
        if (isInvert) visibleAmperageEngine = !visibleAmperageEngine;
        return visibleAmperageEngine ? weight_amperage_engine : EMPTY;
    }

    public static void setWeight_amperage_engine(int weight_amperage_engine) {
        WeightBlocks.weight_amperage_engine = weight_amperage_engine;
    }

    public static boolean isVisibleAmperageEngine(boolean isInvert) {
        if (isInvert) visibleAmperageEngine = !visibleAmperageEngine;
        return visibleAmperageEngine;
    }

    public static void setVisibleAmperageEngine(boolean visibleAmperageEngine) {
        WeightBlocks.visibleAmperageEngine = visibleAmperageEngine;
    }

    // amperage
    public static int getWeight_amperage(boolean isInvert) {
        if (isInvert) visibleAmperage = !visibleAmperage;
        return visibleAmperage ? weight_amperage : EMPTY;
    }

    public static void setWeight_amperage(int weight_amperage) {
        WeightBlocks.weight_amperage = weight_amperage;
    }

    public static boolean isVisibleAmperage(boolean isInvert) {
        if (isInvert) visibleAmperage = !visibleAmperage;
        return visibleAmperage;
    }

    public static void setVisibleAmperage(boolean visibleAmperage) {
        WeightBlocks.visibleAmperage = visibleAmperage;
    }

    public static int getWeight_consumption(boolean isInvert) {
        if (isInvert) visibleConsumption = !visibleConsumption;
        return visibleConsumption ? weight_consumption : EMPTY;
    }

    public static void setWeight_consumption(int weight_consumption) {
        WeightBlocks.weight_consumption = weight_consumption;
    }

    public static boolean isVisibleConsumption(boolean isInvert) {
        if (isInvert) visibleConsumption = !visibleConsumption;
        return visibleConsumption;
    }

    public static void setVisibleConsumption(boolean visibleConsumption) {
        WeightBlocks.visibleConsumption = visibleConsumption;
    }

    public static int getWeight_power(boolean isInvert) {
        if (isInvert) visiblePower = !visiblePower;
        return visiblePower ? weight_power : EMPTY;
    }

    public static void setWeight_power(int weight_power) {
        WeightBlocks.weight_power = weight_power;
    }

    public static boolean isVisiblePower(boolean isInvert) {
        if (isInvert) visiblePower = !visiblePower;
        return visiblePower;
    }

    public static void setVisiblePower(boolean visiblePower) {
        WeightBlocks.visiblePower = visiblePower;
    }

    public static int getWeight_tail(boolean isInvert) {
        if (isInvert) visibleTail = !visibleTail;
        return visibleTail ? weight_tail : EMPTY;
    }

    public static void setWeight_tail(int weight_tail) {
        WeightBlocks.weight_tail = weight_tail;
    }

    public static boolean isVisibleTail(boolean isInvert) {
        if (isInvert) visibleTail = !visibleTail;
        return visibleTail;
    }

    public static void setVisibleTail(boolean visibleTail) {
        WeightBlocks.visibleTail = visibleTail;
    }

    public static int getWeight_press(boolean isInvert) {
        if (isInvert) visiblePress = !visiblePress;
        return visiblePress ? weight_press : EMPTY;
    }

    public static void setWeight_press(int weight_press) {
        WeightBlocks.weight_press = weight_press;
    }

    public static boolean isVisiblePress(boolean isInvert) {
        if (isInvert) visiblePress = !visiblePress;
        return visiblePress;
    }

    public static void setVisiblePress(boolean visiblePress) {
        WeightBlocks.visiblePress = visiblePress;
    }

    public static int getWeight_speed(boolean isInvert) {
        if (isInvert) visibleSpeed = !visibleSpeed;
        return visibleSpeed ? weight_speed : EMPTY;
    }

    public static void setWeight_speed(int weight_speed) {
        WeightBlocks.weight_speed = weight_speed;
    }

    public static boolean isVisibleSpeed(boolean isInvert) {
        if (isInvert) visibleSpeed = !visibleSpeed;
        return visibleSpeed;
    }

    public static void setVisibleSpeed(boolean visibleSpeed) {
        WeightBlocks.visibleSpeed = visibleSpeed;
    }

    public static int getWeight_prof(boolean isInvert) {
        if (isInvert) visibleProfile = !visibleProfile;
        return visibleProfile ? weight_prof : EMPTY;
    }

    public static void setWeight_prof(int weight_prof) {
        WeightBlocks.weight_prof = weight_prof;
    }

    public static boolean isVisibleProfile(boolean isInvert) {
        if (isInvert) visibleProfile = !visibleProfile;
        return visibleProfile;
    }

    public static void setVisibleProfile(boolean visibleProfile) {
        WeightBlocks.visibleProfile = visibleProfile;
    }

    public static int getWeight_position(boolean isInvert) {
        if (isInvert) visiblePosition = !visiblePosition;
        return visiblePosition ? weight_position : EMPTY;
    }

    public static void setWeight_position(int weight_position) {
        WeightBlocks.weight_position = weight_position;
    }

    public static boolean isVisiblePosition(boolean isInvert) {
        if (isInvert) visiblePosition = !visiblePosition;
        return visiblePosition;
    }

    public static void setVisiblePosition(boolean visiblePosition) {
        WeightBlocks.visiblePosition = visiblePosition;
    }

    public static int getWeight_map(boolean isInvert) {
        if (isInvert) visibleMap = !visibleMap;
        return visibleMap ? weight_map : EMPTY;
    }

    public static void setWeight_map(int weight_map) {
        WeightBlocks.weight_map = weight_map;
    }

    public static boolean isVisibleMap(boolean isInvert) {
        if (isInvert) visibleMap = !visibleMap;
        return visibleMap;
    }

    public static void setVisibleMap(boolean visibleMap) {
        WeightBlocks.visibleMap = visibleMap;
    }

    public static int getWeight_alsn(boolean isInvert) {
        if (isInvert) visibleAlsn = !visibleAlsn;
        return visibleAlsn ? weight_alsn : EMPTY;
    }

    public static void setWeight_alsn(int weight_alsn) {
        WeightBlocks.weight_alsn = weight_alsn;
    }

    public static boolean isVisibleAlsn(boolean isInvert) {
        if (isInvert) visibleAlsn = !visibleAlsn;
        return visibleAlsn;
    }

    public static void setVisibleAlsn(boolean visibleAlsn) {
        WeightBlocks.visibleAlsn = visibleAlsn;
    }

    public static int getWeight_alsn_br(boolean isInvert) {
        if (isInvert) visibleAlsnBr = !visibleAlsnBr;
        return visibleAlsnBr ? weight_alsn_br : EMPTY;
    }

    public static void setWeight_alsn_br(int weight_alsn_br) {
        WeightBlocks.weight_alsn_br = weight_alsn_br;
    }

    public static boolean isVisibleAlsnBr(boolean isInvert) {
        if (isInvert) visibleAlsnBr = !visibleAlsnBr;
        return visibleAlsnBr;
    }

    public static void setVisibleAlsnBr(boolean visibleAlsnBr) {
        WeightBlocks.visibleAlsnBr = visibleAlsnBr;
    }

    public static int getWeight_alsn_club(boolean isInvert) {
        if (isInvert) visibleAlsnClub = !visibleAlsnClub;
        return visibleAlsnClub ? weight_alsn_club : EMPTY;
    }

    public static void setWeight_alsn_club(int weight_alsn_club) {
        WeightBlocks.weight_alsn_club = weight_alsn_club;
    }

    public static boolean isVisibleAlsnClub(boolean isInvert) {
        if (isInvert) visibleAlsnClub = !visibleAlsnClub;
        return visibleAlsnClub;
    }

    public static void setVisibleAlsnClub(boolean visibleAlsnClub) {
        WeightBlocks.visibleAlsnClub = visibleAlsnClub;
    }

    public static int getWeight_auto_drive(boolean isInvert) {
        if (isInvert) visibleAutoDrive = !visibleAutoDrive;
        return visibleAutoDrive ? weight_auto_drive : EMPTY;
    }

    public static void setWeight_auto_drive(int weight_auto_drive) {
        WeightBlocks.weight_auto_drive = weight_auto_drive;
    }

    public static boolean isVisibleAutoDrive(boolean isInvert) {
        if (isInvert) visibleAutoDrive = !visibleAutoDrive;
        return visibleAutoDrive;
    }

    public static void setVisibleAutoDrive(boolean visibleAutoDrive) {
        WeightBlocks.visibleAutoDrive = visibleAutoDrive;
    }

    public static int getWeight_cabin(boolean isInvert) {
        if (isInvert) visibleCabin = !visibleCabin;
        return visibleCabin ? weight_cabin : EMPTY;
    }

    public static void setWeight_cabin(int weight_cabin) {
        WeightBlocks.weight_cabin = weight_cabin;
    }

    public static boolean isVisibleCabin(boolean isInvert) {
        if (isInvert) visibleCabin = !visibleCabin;
        return visibleCabin;
    }

    public static void setVisibleCabin(boolean visibleCabin) {
        WeightBlocks.visibleCabin = visibleCabin;
    }

    public static int getWeight_kkm(boolean isInvert) {
        if (isInvert) visibleKkm = !visibleKkm;
        return visibleKkm ? weight_kkm : EMPTY;
    }

    public static void setWeight_kkm(int weight_kkm) {
        WeightBlocks.weight_kkm = weight_kkm;
    }

    public static boolean isVisibleKkm(boolean isInvert) {
        if (isInvert) visibleKkm = !visibleKkm;
        return visibleKkm;
    }

    public static void setVisibleKkm(boolean visibleKkm) {
        WeightBlocks.visibleKkm = visibleKkm;
    }

    public static int getWeight_bhv(boolean isInvert) {
        if (isInvert) visibleBhv = !visibleBhv;
        return visibleBhv ? weight_bhv : EMPTY;
    }

    public static void setWeight_bhv(int weight_bhv) {
        WeightBlocks.weight_bhv = weight_bhv;
    }

    public static boolean isVisibleBhv(boolean isInvert) {
        if (isInvert) visibleBhv = !visibleBhv;
        return visibleBhv;
    }

    public static void setVisibleBhv(boolean visibleBhv) {
        WeightBlocks.visibleBhv = visibleBhv;
    }

    public static int getWeight_kkm2(boolean isInvert) {
        if (isInvert) visibleKkm2 = !visibleKkm2;
        return visibleKkm2 ? weight_kkm2 : EMPTY;
    }

    public static void setWeight_kkm2(int weight_kkm2) {
        WeightBlocks.weight_kkm2 = weight_kkm2;
    }

    public static boolean isVisibleKkm2(boolean isInvert) {
        if (isInvert) visibleKkm2 = !visibleKkm2;
        return visibleKkm2;
    }

    public static void setVisibleKkm2(boolean visibleKkm2) {
        WeightBlocks.visibleKkm2 = visibleKkm2;
    }

    public static int getWeight_kkm_vl10(boolean isInvert) {
        if (isInvert) visibleKkmVl10 = !visibleKkmVl10;
        return visibleKkmVl10 ? weight_kkm_vl10 : EMPTY;
    }

    public static void setWeight_kkm_vl10(int weight_kkm_vl10) {
        WeightBlocks.weight_kkm_vl10 = weight_kkm_vl10;
    }

    public static boolean isVisibleKkmVl10(boolean isInvert) {
        if (isInvert) visibleKkmVl10 = !visibleKkmVl10;
        return visibleKkmVl10;
    }

    public static int getWeight_kkm_s5(boolean isInvert) {
        if (isInvert) visibleKkmS5 = !visibleKkmS5;
        return visibleKkmS5 ? weight_kkm_s5 : EMPTY;
    }

    public static void setWeight_kkm_s5(int weight_kkm_s5) {
        WeightBlocks.weight_kkm_s5 = weight_kkm_s5;
    }

    public static boolean isVisibleKkmS5(boolean isInvert) {
        if (isInvert) visibleKkmS5 = !visibleKkmS5;
        return visibleKkmS5;
    }

    public static void setVisibleKkmS5(boolean visibleKkmS5) {
        WeightBlocks.visibleKkmS5 = visibleKkmS5;
    }

    public static void setVisibleKkmVl10(boolean visibleKkmVl10) {
        WeightBlocks.visibleKkmVl10 = visibleKkmVl10;
    }

    public static int getWeight_main_control(boolean isInvert) {
        if (isInvert) visibleMainControl = !visibleMainControl;
        return visibleMainControl ? weight_main_control : EMPTY;
    }

    public static void setWeight_main_control(int weight_main_control) {
        WeightBlocks.weight_main_control = weight_main_control;
    }

    public static boolean isVisibleMainControl(boolean isInvert) {
        if (isInvert) visibleMainControl = !visibleMainControl;
        return visibleMainControl;
    }

    public static void setVisibleMainControl(boolean visibleMainControl) {
        WeightBlocks.visibleMainControl = visibleMainControl;
    }

    public static int getWeight_pneumatic1(boolean isInvert) {
        if (isInvert) visiblePneumatic1 = !visiblePneumatic1;
        return visiblePneumatic1 ? weight_pneumatic1 : EMPTY;
    }

    public static void setWeight_pneumatic1(int weight_pneumatic1) {
        WeightBlocks.weight_pneumatic1 = weight_pneumatic1;
    }

    public static int getWeight_pneumatic2(boolean isInvert) {
        if (isInvert) visiblePneumatic2 = !visiblePneumatic2;
        return visiblePneumatic2 ? weight_pneumatic2 : EMPTY;
    }

    public static int getWeight_pneumatic_usavp(boolean isInvert) {
        if (isInvert) visiblePneumatic_usavp = !visiblePneumatic_usavp;
        return visiblePneumatic_usavp ? weight_pneumatic_usavp : EMPTY;
    }

    public static void setWeight_pneumatic2(int weight_pneumatic2) {
        WeightBlocks.weight_pneumatic2 = weight_pneumatic2;
    }

    public static void setVisiblePneumatic2(boolean visiblePneumatic2) {
        WeightBlocks.visiblePneumatic2 = visiblePneumatic2;
    }

    public static void setWeight_pneumatic_usavp(int weight_pneumatic_usavp) {
        WeightBlocks.weight_pneumatic_usavp = weight_pneumatic_usavp;
    }

    public static void setVisiblePneumatic_usavp(boolean visiblePneumatic_usavp) {
        WeightBlocks.visiblePneumatic_usavp = visiblePneumatic_usavp;
    }

    public static boolean isVisibleTaskPneumatic(boolean isInvert) {
        if (isInvert) visiblePneumatic1 = !visiblePneumatic1;
        return visiblePneumatic1;
    }

    public static void setVisiblePneumatic1(boolean visiblePneumatic1) {
        WeightBlocks.visiblePneumatic1 = visiblePneumatic1;
    }

    public static int getWeight_uatl(boolean isInvert) {
        if (isInvert) visibleUatl = !visibleUatl;
        return visibleUatl ? weight_uatl : EMPTY;
    }

    public static void setWeight_uatl(int weight_uatl) {
        WeightBlocks.weight_uatl = weight_uatl;
    }

    public static boolean isVisibleUatl(boolean isInvert) {
        if (isInvert) visibleUatl = !visibleUatl;
        return visibleUatl;
    }

    public static void setVisibleUatl(boolean visibleUatl) {
        WeightBlocks.visibleUatl = visibleUatl;
    }

    public static int getWeight_push_key(boolean isInvert) {
        if (isInvert) visiblePushKey = !visiblePushKey;
        return visiblePushKey ? weight_push_key : EMPTY;
    }

    public static void setWeight_push_key(int weight_push_key) {
        WeightBlocks.weight_push_key = weight_push_key;
    }

    public static boolean isVisiblePushKey(boolean isInvert) {
        if (isInvert) visiblePushKey = !visiblePushKey;
        return visiblePushKey;
    }

    public static void setVisiblePushKey(boolean visiblePushKey) {
        WeightBlocks.visiblePushKey = visiblePushKey;
    }

    public static int getWeight_rev_control(boolean isInvert) {
        if (isInvert) visibleRevControl = !visibleRevControl;
        return visibleRevControl ? weight_rev_control : EMPTY;
    }

    public static void setWeight_rev_control(int weight_rev_control) {
        WeightBlocks.weight_rev_control = weight_rev_control;
    }

    public static boolean isVisibleRevControl(boolean isInvert) {
        if (isInvert) visibleRevControl = !visibleRevControl;
        return visibleRevControl;
    }

    public static void setVisibleRevControl(boolean visibleRevControl) {
        WeightBlocks.visibleRevControl = visibleRevControl;
    }

    public static int getWeight_schema(boolean isInvert) {
        if (isInvert) visibleSchema = !visibleSchema;
        return visibleSchema ? weight_schema : EMPTY;
    }

    public static void setWeight_schema(int weight_schema) {
        WeightBlocks.weight_schema = weight_schema;
    }

    public static boolean isVisibleSchema(boolean isInvert) {
        if (isInvert) visibleSchema = !visibleSchema;
        return visibleSchema;
    }

    public static void setVisibleSchema(boolean visibleSchema) {
        WeightBlocks.visibleSchema = visibleSchema;
    }

    public static int getWeight_vsc(boolean isInvert) {
        if (isInvert) visibleVsc = !visibleVsc;
        return visibleVsc ? weight_vsc : EMPTY;
    }

    public static void setWeight_vsc(int weight_vsc) {
        WeightBlocks.weight_vsc = weight_vsc;
    }

    public static boolean isVisibleVsc(boolean isInvert) {
        if (isInvert) visibleVsc = !visibleVsc;
        return visibleVsc;
    }

    public static void setVisibleVsc(boolean visibleVsc) {
        WeightBlocks.visibleVsc = visibleVsc;
    }

    public static int getWeight_signals(boolean isInvert) {
        if (isInvert) visibleSignals = !visibleSignals;
        return visibleSignals ? weight_signals : EMPTY;
    }

    public static void setWeight_signals(int weight_signals) {
        WeightBlocks.weight_signals = weight_signals;
    }

    public static boolean isVisibleSignals(boolean isInvert) {
        if (isInvert) visibleSignals = !visibleSignals;
        return visibleSignals;
    }

    public static void setVisibleSignals(boolean visibleSignals) {
        WeightBlocks.visibleSignals = visibleSignals;
    }

    public static int getWeight_signals_autodis(boolean isInvert) {
        if (isInvert) visibleSignalsAutodis = !visibleSignalsAutodis;
        return visibleSignalsAutodis ? weight_signals_autodis : EMPTY;
    }

    public static void setWeight_signals_autodis(int weight_signals_autodis) {
        WeightBlocks.weight_signals_autodis = weight_signals_autodis;
    }

    public static boolean isVisibleSignalsAutodis(boolean isInvert) {
        if (isInvert) visibleSignalsAutodis = !visibleSignalsAutodis;
        return visibleSignalsAutodis;
    }

    public static int getWeight_signals_bhv(boolean isInvert) {
        if (isInvert) visibleSignalsBhv = !visibleSignalsBhv;
        return visibleSignalsBhv ? weight_signals_bhv : EMPTY;
    }

    public static void setWeight_signals_bhv(int weight_signals_bhv) {
        WeightBlocks.weight_signals_bhv = weight_signals_bhv;
    }

    public static boolean isVisibleSignalsBhv(boolean isInvert) {
        if (isInvert) visibleSignalsBhv = !visibleSignalsBhv;
        return visibleSignalsBhv;
    }

    public static void setVisibleSignalsBhv(boolean visibleSignalsBhv) {
        WeightBlocks.visibleSignalsBhv = visibleSignalsBhv;
    }

    public static void setVisibleSignalsAutodis(boolean visibleSignalsAutodis) {
        WeightBlocks.visibleSignalsAutodis = visibleSignalsAutodis;
    }

    public static int getWeight_signals_ted(boolean isInvert) {
        if (isInvert) visibleSignalsTed = !visibleSignalsTed;
        return visibleSignalsTed ? weight_signals_ted : EMPTY;
    }

    public static void setWeight_signals_ted(int weight_signals_ted) {
        WeightBlocks.weight_signals_ted = weight_signals_ted;
    }

    public static boolean isVisibleSignalsTed(boolean isInvert) {
        if (isInvert) visibleSignalsTed = !visibleSignalsTed;
        return visibleSignalsTed;
    }

    public static void setVisibleSignalsTed(boolean visibleSignalsTed) {
        WeightBlocks.visibleSignalsTed = visibleSignalsTed;
    }

    public static int getWeight_signals_ted_s5k(boolean isInvert) {
        if (isInvert) visibleSignalsTedS5k = !visibleSignalsTedS5k;
        return visibleSignalsTedS5k ? weight_signals_ted_s5k : EMPTY;
    }

    public static void setWeight_signals_ted_s5k(int weight_signals_ted_s5k) {
        WeightBlocks.weight_signals_ted_s5k = weight_signals_ted_s5k;
    }

    public static boolean isVisibleSignalsS5k(boolean isInvert) {
        if (isInvert) visibleSignalsTedS5k = !visibleSignalsTedS5k;
        return visibleSignalsTedS5k;
    }

    public static void setVisibleSignalsTedS5k(boolean visibleSignalsTedS5k) {
        WeightBlocks.visibleSignalsTedS5k = visibleSignalsTedS5k;
    }



    public static int getWeight_signals_link(boolean isInvert) {
        if (isInvert) visibleSignalsLink = !visibleSignalsLink;
        return visibleSignalsLink ? weight_signals_link : EMPTY;
    }

    public static void setWeight_signals_link(int weight_signals_link) {
        WeightBlocks.weight_signals_link = weight_signals_link;
    }

    public static boolean isVisibleSignalsLink(boolean isInvert) {
        if (isInvert) visibleSignalsLink = !visibleSignalsLink;
        return visibleSignalsLink;
    }

    public static void setVisibleSignalsLink(boolean visibleSignalsLink) {
        WeightBlocks.visibleSignalsLink = visibleSignalsLink;
    }

    public static void saveSettings(Preferences node) {
        node.putInt("weight_voltage_cs", weight_voltage_cs);
        node.putBoolean("visibleVoltage_cs", visibleVoltage_cs);
        node.putInt("weight_voltage", weight_voltage);
        node.putBoolean("visibleVoltage", visibleVoltage);
        node.putInt("weight_amperage_common", weight_amperage_common);
        node.putBoolean("visibleAmperageCommon", visibleAmperageCommon);
        node.putInt("weight_amperage_anchor", weight_amperage_anchor);
        node.putBoolean("visibleAmperageAnchor", visibleAmperageAnchor);
        node.putInt("weight_amperage_excitation", weight_amperage_excitation);
        node.putBoolean("visibleAmperageExcitation", visibleAmperageExcitation);

        node.putInt("weight_amperage_engine", weight_amperage_engine);
        node.putBoolean("visibleAmperageEngine", visibleAmperageEngine);
        node.putInt("weight_amperage", weight_amperage);
        node.putBoolean("visibleAmperage", visibleAmperage);

        node.putInt("weight_consumption", weight_consumption);
        node.putBoolean("visibleConsumption", visibleConsumption);
        node.putInt("weight_power", weight_power);
        node.putBoolean("visiblePower", visiblePower);
        node.putInt("weight_tail", weight_tail);
        node.putBoolean("visibleTail", visibleTail);
        node.putInt("weight_press", weight_press);
        node.putBoolean("visiblePress", visiblePress);
        node.putInt("weight_speed", weight_speed);
        node.putBoolean("visibleSpeed", visibleSpeed);
        node.putInt("weight_prof", weight_prof);
        node.putBoolean("visibleProfile", visibleProfile);
        node.putInt("weight_position", weight_position);
        node.putBoolean("visiblePosition", visiblePosition);
        node.putInt("weight_map", weight_map);
        node.putBoolean("visibleMap", visibleMap);

    //    node.putInt("weight_alsn", weight_alsn);
        node.putBoolean("visibleAlsn", visibleAlsn);
    //    node.putInt("weight_alsn_br", weight_alsn_br);
        node.putBoolean("visibleAlsnBr", visibleAlsnBr);
    //    node.putInt("weight_alsn_club", weight_alsn_club);
        node.putBoolean("visibleAlsnClub", visibleAlsnClub);
    //    node.putInt("weight_auto_drive", weight_auto_drive);
        node.putBoolean("visibleAutoDrive", visibleAutoDrive);
    //    node.putInt("weight_cabin", weight_cabin);
        node.putBoolean("visibleCabin", visibleCabin);
    //    node.putInt("weight_kkm", weight_kkm);
        node.putBoolean("visibleKkm", visibleKkm);
    //    node.putInt("weight_kkm2", weight_kkm2);
        node.putBoolean("visibleKkm2", visibleKkm2);
    //    node.putInt("weight_kkm_vl10", weight_kkm_vl10);
        node.putBoolean("visibleKkm2", visibleKkm2);
    //    node.putInt("weight_kkm_s5", weight_kkm_s5);
        node.putBoolean("visibleKkmS5", visibleKkmS5);
    //    node.putInt("weight_bhv", weight_bhv);
        node.putBoolean("visibleBhv", visibleBhv);
    //    node.putInt("weight_main_control", weight_main_control);
        node.putBoolean("visibleMainControl", visibleMainControl);
    //    node.putInt("weight_pneumatic1", weight_pneumatic1);
        node.putBoolean("visiblePneumatic1", visiblePneumatic1);
    //    node.putInt("weight_pneumatic2", weight_pneumatic2);
        node.putBoolean("visiblePneumatic2", visiblePneumatic2);
    //    node.putInt("weight_pneumatic_usavp", weight_pneumatic_usavp);
        node.putBoolean("visiblePneumatic_usavp", visiblePneumatic_usavp);
    //    node.putInt("weight_uatl", weight_uatl);
        node.putBoolean("visibleUatl", visibleUatl);
    //    node.putInt("weight_push_key", weight_push_key);
        node.putBoolean("visiblePushKey", visiblePushKey);
    //    node.putInt("weight_rev_control", weight_rev_control);
        node.putBoolean("visibleRevControl", visibleRevControl);
    //    node.putInt("weight_schema", weight_schema);
        node.putBoolean("visibleSchema", visibleSchema);
    //    node.putInt("weight_vsc", weight_vsc);
        node.putBoolean("visibleVsc", visibleVsc);

    //    node.putInt("weight_signals", weight_signals);
        node.putBoolean("visibleSignals", visibleSignals);
    //    node.putInt("weight_signals_autodis", weight_signals_autodis);
        node.putBoolean("visibleSignalsAutodis", visibleSignalsAutodis);
    //    node.putInt("weight_signals_bhv", weight_signals_bhv);
        node.putBoolean("visibleSignalsBhv", visibleSignalsBhv);
    //    node.putInt("weight_signals_ted", weight_signals_ted);
        node.putBoolean("visibleSignalsTed", visibleSignalsTed);
    //    node.putInt("weight_signals_ted_s5k", weight_signals_ted_s5k);
        node.putBoolean("visibleSignalsTedS5k", visibleSignalsTedS5k);
    //    node.putInt("weight_signals_link", weight_signals_link);
        node.putBoolean("visibleSignalsLink", visibleSignalsLink);

        // видимость линий
        String str = UtilsArmG.trimFirstLast(SeriesLines.getMapVisible().toString(), 1, 1);
        node.put("viewLinesStr", str);
        modified = false;
    }

    public static void loadSettings(Preferences node, boolean is_default) {
        weight_voltage_cs = node.getInt("weight_voltage_cs", W_LINE);
        visibleVoltage_cs = !is_default && node.getBoolean("visibleVoltage_cs", false);
        weight_voltage = node.getInt("weight_voltage", W_LINE);
        visibleVoltage = !is_default && node.getBoolean("visibleVoltage", true);

        weight_amperage_common = node.getInt("weight_amperage_common", W_LINE);
        visibleAmperageCommon = !is_default && node.getBoolean("visibleAmperageCommon", false);
        weight_amperage_anchor = node.getInt("weight_amperage_anchor", W_LINE);
        visibleAmperageAnchor = !is_default && node.getBoolean("visibleAmperageAnchor", true);
        weight_amperage_excitation = node.getInt("weight_amperage_excitation", W_LINE);
        visibleAmperageExcitation = !is_default && node.getBoolean("visibleAmperageExcitation", false);

        weight_amperage_engine = node.getInt("weight_amperage_engine", W_LINE);
        visibleAmperageEngine = !is_default && node.getBoolean("visibleAmperageEngine", true);
        weight_amperage = node.getInt("weight_amperage", W_LINE);
        visibleAmperage = !is_default && node.getBoolean("visibleAmperage", true);

        weight_consumption = node.getInt("weight_consumption", W_LINE);
        visibleConsumption = !is_default && node.getBoolean("visibleConsumption", false);
        weight_power = node.getInt("weight_power", W_LINE);
        visiblePower = !is_default && node.getBoolean("visiblePower", true);
        weight_tail = node.getInt("weight_tail", W_LINE);
        visibleTail = !is_default && node.getBoolean("visibleTail", false);
        weight_press = node.getInt("weight_press", W_LINE);
        visiblePress = !is_default && node.getBoolean("visiblePress", true);
        weight_speed = node.getInt("weight_speed", W_LINE);
        visibleSpeed = !is_default && node.getBoolean("visibleSpeed", true);
        weight_prof = node.getInt("weight_prof", W_LINE / 5);
        visibleProfile = !is_default && node.getBoolean("visibleProfile", false);
        weight_position = node.getInt("weight_position", W_LINE / 2);
        visiblePosition = !is_default && node.getBoolean("visiblePosition", true);
        weight_map = node.getInt("weight_map", W_MAP);
        visibleMap = !is_default && node.getBoolean("visibleMap", true);

        visibleAlsn = !is_default && node.getBoolean("visibleAlsn", true);
        visibleAlsnBr = !is_default && node.getBoolean("visibleAlsnBr", false);
        visibleAlsnClub = !is_default && node.getBoolean("visibleAlsnClub", false);
        visibleAutoDrive = !is_default && node.getBoolean("visibleAutoDrive", true);
        visibleCabin = !is_default && node.getBoolean("visibleCabin", false);
        visibleKkm = !is_default && node.getBoolean("visibleKkm", false);
        visibleKkm2 = !is_default && node.getBoolean("visibleKkm2", false);
        visibleKkmVl10 = !is_default && node.getBoolean("visibleKkmVl10", false);
        visibleKkmS5 = !is_default && node.getBoolean("visibleKkmS5", false);
        visibleBhv = !is_default && node.getBoolean("visibleBhv", false);
        visibleMainControl = !is_default && node.getBoolean("visibleMainControl", false);
        visiblePneumatic1 = !is_default && node.getBoolean("visiblePneumatic1", false);
        visiblePneumatic2 = !is_default && node.getBoolean("visiblePneumatic2", false);
        visiblePneumatic_usavp = !is_default && node.getBoolean("visiblePneumatic_usavp", false);
        visibleUatl = !is_default && node.getBoolean("visibleUatl", false);
        visiblePushKey = !is_default && node.getBoolean("visiblePushKey", false);
        visibleRevControl = !is_default && node.getBoolean("visibleRevControl", false);
        visibleSchema = !is_default && node.getBoolean("visibleSchema", false);
        visibleVsc = !is_default && node.getBoolean("visibleVsc", false);

        visibleSignals = !is_default && node.getBoolean("visibleSignals", true);
        visibleSignalsAutodis = !is_default && node.getBoolean("visibleSignalsAutodis", false);
        visibleSignalsBhv = !is_default && node.getBoolean("visibleSignalsBhv", false);
        visibleSignalsTed = !is_default && node.getBoolean("visibleSignalsTed", false);
        visibleSignalsTedS5k = !is_default && node.getBoolean("visibleSignalsTedS5k", false);
        visibleSignalsLink = !is_default && node.getBoolean("visibleSignalsLink", false);
        // видимость линий
        if (!is_default) {
            String str = node.get("viewLinesStr", "");
            SeriesLines.setMapVisible(UtilsArmG.getMapFromStr(str));
        }
//        ChartPanelArm.setMapViewLines(hm);
    }

    public static void setWeightDef(XYPlot subplot) {
        // lines
        if (subplot.getRangeAxis().getLabel().equals(VOLTAGE_CS_LABEL)) {
            subplot.setWeight(EMPTY);
            setWeight_voltage_cs(W_LINE);
            setVisibleVoltage_cs(false);
        }
        else if (subplot.getRangeAxis().getLabel().equals(VOLTAGE_ENGINE_LABEL)) {
            subplot.setWeight(W_LINE);
            setWeight_voltage(W_LINE);
            setVisibleVoltage(true);
        }
        else if (subplot.getRangeAxis().getLabel().equals(AMPERAGE_COMMON_LABEL)) {
            subplot.setWeight(EMPTY);
            setWeight_amperage_common(W_LINE);
            setVisibleAmperageCommon(false);
        }
        else if (subplot.getRangeAxis().getLabel().equals(AMPERAGE_ANCHOR_LABEL)) {
            subplot.setWeight(W_LINE);
            setWeight_amperage_anchor(W_LINE);
            setVisibleAmperageAnchor(true);
        }
        else if (subplot.getRangeAxis().getLabel().equals(AMPERAGE_EXCITATION_LABEL)) {
            subplot.setWeight(EMPTY);
            setWeight_amperage_excitation(W_LINE);
            setVisibleAmperageExcitation(false);
        }
        else if (subplot.getRangeAxis().getLabel().equals(AMPERAGE_ENGINE_LABEL)) {
            subplot.setWeight(W_LINE);
            setWeight_amperage_engine(W_LINE);
            setVisibleAmperageEngine(true);
        }
        else if (subplot.getRangeAxis().getLabel().equals(AMPERAGE_LABEL)) {
            subplot.setWeight(W_LINE);
            setWeight_amperage(W_LINE);
            setVisibleAmperage(true);
        }
        else if (subplot.getRangeAxis().getLabel().equals(CONSUMPTION_LABEL)) {
            subplot.setWeight(EMPTY);
            setWeight_consumption(W_LINE);
            setVisibleConsumption(false);
        }
        else if (subplot.getRangeAxis().getLabel().equals(POWER_LABEL)) {
            subplot.setWeight(W_LINE);
            setWeight_power(W_LINE);
            setVisiblePower(true);
        }
        else if (subplot.getRangeAxis().getLabel().equals(TAIL_LABEL)) {
            subplot.setWeight(EMPTY);
            setWeight_tail(W_LINE);
            setVisibleTail(false);
        }
        else if (subplot.getRangeAxis().getLabel().equals(PRESS_LABEL)) {
            subplot.setWeight(W_LINE);
            setWeight_press(W_LINE);
            setVisiblePress(true);
        }
        else if (subplot.getRangeAxis().getLabel().equals(SPEED_LABEL)) {
            subplot.setWeight(W_LINE);
            setWeight_speed(W_LINE);
            setVisibleSpeed(true);
        }
        else if (subplot.getRangeAxis().getLabel().equals(PROF_LABEL)) {
            subplot.setWeight(EMPTY);
            setWeight_prof(W_PROF);
            setVisibleProfile(false);
        }
        else if (subplot.getRangeAxis().getLabel().equals(POSITION_LABEL)) {
            subplot.setWeight(W_POS);
            setWeight_position(W_POS);
            setVisiblePosition(true);
        }
        else if (subplot.getRangeAxis().getLabel().equals(MAP_LABEL)) {
            subplot.setWeight(W_MAP);
            setWeight_map(W_MAP);
            setVisibleMap(true);
        }
        // gnat
        else if (subplot.getRangeAxis().getLabel().equals(ALSN_LABEL)) {
            subplot.setWeight(W_GANTT);
            setWeight_alsn(W_GANTT);
            setVisibleAlsn(true);
        }
        else if (subplot.getRangeAxis().getLabel().equals(ALSN_BR_LABEL)) {
            subplot.setWeight(EMPTY);
            setWeight_alsn_br(W_GANTT);
            setVisibleAlsnBr(false);
        }
        else if (subplot.getRangeAxis().getLabel().equals(ALSN_CLUB_LABEL)) {
            subplot.setWeight(EMPTY);
            setWeight_alsn_club(W_GANTT);
            setVisibleAlsnClub(false);
        }
        else if (subplot.getRangeAxis().getLabel().equals(AUTO_LABEL)) {
            subplot.setWeight(W_GANTT);
            setWeight_auto_drive(W_GANTT);
            setVisibleAutoDrive(true);
        }
        else if (subplot.getRangeAxis().getLabel().equals(CABIN_LABEL)) {
            subplot.setWeight(EMPTY);
            setWeight_cabin(W_GANTT);
            setVisibleCabin(false);
        }
        else if (subplot.getRangeAxis().getLabel().equals(KKM_S5K_LABEL)) {
            subplot.setWeight(EMPTY);
            setWeight_kkm(W_GANTT);
            setVisibleKkm(false);
        }
        else if (subplot.getRangeAxis().getLabel().equals(KKM_S5K_2_LABEL)) {
            subplot.setWeight(EMPTY);
            setWeight_kkm2(W_GANTT);
            setVisibleKkm2(false);
        }
        else if (subplot.getRangeAxis().getLabel().equals(KKM_VL10_LABEL)) {
            subplot.setWeight(EMPTY);
            setWeight_kkm_vl10(W_GANTT);
            setVisibleKkmVl10(false);
        }
        else if (subplot.getRangeAxis().getLabel().equals(KKM_S5_LABEL)) {
            subplot.setWeight(EMPTY);
            setWeight_kkm_s5(W_GANTT);
            setVisibleKkmS5(false);
        }
        else if (subplot.getRangeAxis().getLabel().equals(BHV_LABEL)) {
            subplot.setWeight(EMPTY);
            setWeight_bhv(W_GANTT);
            setVisibleBhv(false);
        }
        else if (subplot.getRangeAxis().getLabel().equals(MAIN_CONTROL_LABEL)) {
            subplot.setWeight(EMPTY);
            setWeight_main_control(W_GANTT);
            setVisibleMainControl(false);
        }
        else if (subplot.getRangeAxis().getLabel().equals(PNEUMATIC_LABEL)) {
            subplot.setWeight(EMPTY);
            setWeight_pneumatic1(W_GANTT);
            setVisiblePneumatic1(false);
        }
        else if (subplot.getRangeAxis().getLabel().equals(PNEUMATIC2_LABEL)) {
            subplot.setWeight(EMPTY);
            setWeight_pneumatic2(W_GANTT);
            setVisiblePneumatic2(false);
        }
        else if (subplot.getRangeAxis().getLabel().equals(PNEUMATIC_USAVP_LABEL)) {
            subplot.setWeight(EMPTY);
            setWeight_pneumatic_usavp(W_GANTT);
            setVisiblePneumatic_usavp(false);
        }
        else if (subplot.getRangeAxis().getLabel().equals(UATL_LABEL)) {
            subplot.setWeight(EMPTY);
            setWeight_uatl(W_GANTT_UATL);
            setVisibleUatl(false);
        }
        else if (subplot.getRangeAxis().getLabel().equals(KEYS_LABEL)) {
            subplot.setWeight(EMPTY);
            setWeight_push_key(W_KEY);
            setVisiblePushKey(false);
        }
        else if (subplot.getRangeAxis().getLabel().equals(REV_CONTROL_LABEL)) {
            subplot.setWeight(EMPTY);
            setWeight_rev_control(W_GANTT);
            setVisibleRevControl(false);
        }
        else if (subplot.getRangeAxis().getLabel().equals(SCHEMA_LABEL)) {
            subplot.setWeight(EMPTY);
            setWeight_schema(W_GANTT);
            setVisibleSchema(false);
        }
        else if (subplot.getRangeAxis().getLabel().equals(VSC_LABEL)) {
            subplot.setWeight(EMPTY);
            setWeight_vsc(W_GANTT);
            setVisibleVsc(false);
        }
        // сигналы
        else if (subplot.getRangeAxis().getLabel().equals(SIGNALS_LABEL)) {
            subplot.setWeight(W_SIGNAL);
            setWeight_signals(W_SIGNAL);
            setVisibleSignals(true);
        }
        else if (subplot.getRangeAxis().getLabel().equals(AUTODIS_LABEL)) {
            subplot.setWeight(EMPTY);
            setWeight_signals_autodis(W_SIGNAL);
            setVisibleSignalsAutodis(false);
        }
        else if (subplot.getRangeAxis().getLabel().equals(SIGN_BHV_LABEL)) {
            subplot.setWeight(EMPTY);
            setWeight_signals_bhv(W_SIGNAL);
            setVisibleSignalsBhv(false);
        }
        else if (subplot.getRangeAxis().getLabel().equals(SIGN_TED_LABEL)) {
            subplot.setWeight(EMPTY);
            setWeight_signals_ted(W_SIGNAL);
            setVisibleSignalsTed(false);
        }
        else if (subplot.getRangeAxis().getLabel().equals(SIGN_TED_S5K_LABEL)) {
            subplot.setWeight(EMPTY);
            setWeight_signals_ted_s5k(W_SIGNAL);
            setVisibleSignalsTedS5k(false);
        }
        else if (subplot.getRangeAxis().getLabel().equals(SIGN_LINK_LABEL)) {
            subplot.setWeight(EMPTY);
            setWeight_signals_link(W_LINK);
            setVisibleSignalsLink(false);
        }


    }

    public static boolean isModified() {
        return modified;
    }

    public static void setModified(boolean modified) {
        WeightBlocks.modified = modified;
    }
}
