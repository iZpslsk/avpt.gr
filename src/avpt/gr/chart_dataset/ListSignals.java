package avpt.gr.chart_dataset;

import java.util.ArrayList;

public class ListSignals {

    public static class ListSignal {
        private final ArrayList<ItemSignal> list = new ArrayList<ItemSignal>();
        private boolean isLock = false;

        public ArrayList<ItemSignal> getList() {
            return list;
        }
    }

    // 0x21_7 - отключено по умолчанию
    private final ListSignal listReady = new ListSignal();                  // готов
    private final ListSignal listCan = new ListSignal();                    // can_modem
    private final ListSignal listError = new ListSignal();                  // ошибки
    private final ListSignal listChangeAcceptedSchedule = new ListSignal();         // смена прин расп 11
    private final ListSignal listChangeAcceptedValidSchedule = new ListSignal();    // смена верн расп 16
    private final ListSignal listChangeLoadSchedule = new ListSignal();             // смена загр расп 17
    private final ListSignal listSourceSchedule = new ListSignal();         // ИР БД (источник расп)
    private final ListSignal listModeWork = new ListSignal();               // режим работы бмс
    private final ListSignal listLevelGPRS = new ListSignal();              // уровень GPRS
    private final ListSignal listSendDiag = new ListSignal();               // отпр диагн
    private final ListSignal listUpdate = new ListSignal();                 // обновление ПО
    private final ListSignal listEsmBs = new ListSignal();                  // скачивание д с ЕСМ БС
    private final ListSignal listLinkServer = new ListSignal();             // связь с сервером
    private final ListSignal listLinkGateway = new ListSignal();            // связь со шлюзом РОРС
    private final ListSignal listGPRS = new ListSignal();                   // есть GPRS услуга
    private final ListSignal listAutoSchedule = new ListSignal();           // Автовед. по расп. СИМ
    // 0x21_4
    private final ListSignal listBanThrust = new ListSignal();          // запрет тяги
    private final ListSignal listBanBrake = new ListSignal();           // запрет торможения
    private final ListSignal listEmulate = new ListSignal();            // режим эмуляции
    private final ListSignal listEPK = new ListSignal();                // ЭПК
    private final ListSignal listChainOff = new ListSignal();           // откл цепей
    private final ListSignal listTestBrakeCorrupt = new ListSignal();   // тест тормозов не пройден
    private final ListSignal listTestTractCorrupt = new ListSignal();   // тест тяги не пройден
    private final ListSignal listManeuver = new ListSignal();           // маневровые работы
    private final ListSignal listMainChannel = new ListSignal();        // связь по осн каналу
    private final ListSignal listAdditionChannel = new ListSignal();    // связь по доп каналу

    // 0x1D_4
    private final ListSignal listBanPT = new ListSignal();              // запрет ПТ
    private final ListSignal listBanET = new ListSignal();              // запрет ЭТ
    //
    private final ListSignal listGV = new ListSignal();                 // срабатывание ГВ (ЭС5К)
    private final ListSignal listProtect = new ListSignal();            // срабатывание защиты (ЭС5К)
    private final ListSignal listBox = new ListSignal();                // боксование (ЭС5К, ВЛ10)
    private final ListSignal listPressOver = new ListSignal();          // давл в ТЦ > 1,1 кгс/см2 (ЭС5К)
    private final ListSignal listMKCompressOff = new ListSignal();      // МК откл компрессор (ЭС5К)
    private final ListSignal listDMPressOil = new ListSignal();         // ДМ давление масла (ЭС5К)

    private final ListSignal listPst = new ListSignal();               // ПСТ
    private final ListSignal listPst1 = new ListSignal();               // ПСТ 1-я секц (ВЛ10)
    private final ListSignal listPst2 = new ListSignal();               // ПСТ 2-я секц (ВЛ10)
    private final ListSignal listEpk1 = new ListSignal();               // ЭПК 1-я секц (ВЛ10)
    private final ListSignal listEpk2 = new ListSignal();               // ЭПК 2-я секц (ВЛ10)
    private final ListSignal listBv = new ListSignal();                 // Отключение БВ (ВЛ10)
    private final ListSignal listPt_rec = new ListSignal();             // Позиция ПТ при рекуперации (ВЛ10)
    private final ListSignal listTm = new ListSignal();                 // Срабатывание тормозов
    private final ListSignal listRevers = new ListSignal();             // Контроль разворота групп.переключ (ВЛ10)
    private final ListSignal listOverload = new ListSignal();           // Реле перегрузки (ВЛ10)
    private final ListSignal listStop1 = new ListSignal();              // кнопка остановки поезда 1-я секция (ВЛ10)
    private final ListSignal listStop2 = new ListSignal();              // кнопка остановки поезда 2-я секция (ВЛ10)

    private final ListSignal listEmergencyBrake1 = new ListSignal();    // экстренное торможение 1-я секция (ВЛ80)
    private final ListSignal listEmergencyBrake2 = new ListSignal();    // экстренное торможение 2-я секция (ВЛ80)
    private final ListSignal listBox1 = new ListSignal();               // наличие боксования 1 (ВЛ80)
    private final ListSignal listBox2 = new ListSignal();               // наличие боксования 2 (ВЛ80)
    private final ListSignal listRP1 = new ListSignal();                // срабатывание РП 1 (ВЛ80)
    private final ListSignal listRP2 = new ListSignal();                // срабатывание РП 2 (ВЛ80)
    private final ListSignal listTD1 = new ListSignal();                // сигнал ТД 1 (ВЛ80)
    private final ListSignal listTD2 = new ListSignal();                // сигнал ТД 2 (ВЛ80)
    private final ListSignal listRZ1 = new ListSignal();                // сигнал РЗ 1 (ВЛ80)
    private final ListSignal listRZ2 = new ListSignal();                // сигнал РЗ 2 (ВЛ80)
    private final ListSignal listInterDriver1 = new ListSignal();       // вмешательство машиниста 1 (ВЛ80)
    private final ListSignal listInterDriver2 = new ListSignal();       // вмешательство машиниста 2 (ВЛ80)
    private final ListSignal listTM1 = new ListSignal();                // срабатывание тм 1 (ВЛ80)
    private final ListSignal listTM2 = new ListSignal();                // срабатывание тм 2 (ВЛ80)
    private final ListSignal listUKKNP1 = new ListSignal();             // УККНП 1 (ВЛ80)
    private final ListSignal listUKKNP2 = new ListSignal();             // УККНП 2 (ВЛ80)
    private final ListSignal listGV1 = new ListSignal();                // отключение ГВ 1 (ВЛ80)
    private final ListSignal listGV2 = new ListSignal();                // отключение ГВ 2 (ВЛ80)
    private final ListSignal listPPV1 = new ListSignal();               // отключение ППВ 1 (ВЛ80)
    private final ListSignal listPPV2 = new ListSignal();               // отключение ППВ 2 (ВЛ80)

    private final ListSignal listEmergencyBrake = new ListSignal();     // клапан аварийного торможения
    private final ListSignal listAcceleratedBrake = new ListSignal();   // клапан ускоренного торможения
    private final ListSignal listReleaseBrake = new ListSignal();       // отпуск тормозов
    private final ListSignal listSandAuto = new ListSignal();           // песок автоматически
    private final ListSignal listHandSpeedZero = new ListSignal();      // рукоятка задания скорости в 0 положении
    // ЭС5К МСУД-15
    private final ListSignal listTed1_1s = new ListSignal();             // отключение ТЭД-1 1-я секция
    private final ListSignal listTed2_1s = new ListSignal();             // отключение ТЭД-2 1-я секция
    private final ListSignal listTed3_1s = new ListSignal();             // отключение ТЭД-3 1-я секция
    private final ListSignal listTed4_1s = new ListSignal();             // отключение ТЭД-4 1-я секция

    private final ListSignal listTed1_2s = new ListSignal();             // отключение ТЭД-1 2-я секция
    private final ListSignal listTed2_2s = new ListSignal();             // отключение ТЭД-2 2-я секция
    private final ListSignal listTed3_2s = new ListSignal();             // отключение ТЭД-3 2-я секция
    private final ListSignal listTed4_2s = new ListSignal();             // отключение ТЭД-4 2-я секция

    private final ListSignal listTed1_3s = new ListSignal();             // отключение ТЭД-1 3-я секция
    private final ListSignal listTed2_3s = new ListSignal();             // отключение ТЭД-2 3-я секция
    private final ListSignal listTed3_3s = new ListSignal();             // отключение ТЭД-3 3-я секция
    private final ListSignal listTed4_3s = new ListSignal();             // отключение ТЭД-4 3-я секция

    private final ListSignal listTed1_4s = new ListSignal();             // отключение ТЭД-1 4-я секция
    private final ListSignal listTed2_4s = new ListSignal();             // отключение ТЭД-2 4-я секция
    private final ListSignal listTed3_4s = new ListSignal();             // отключение ТЭД-3 4-я секция
    private final ListSignal listTed4_4s = new ListSignal();             // отключение ТЭД-4 4-я секция

    // ЭС5К
    private final ListSignal listTed_1_2 = new ListSignal();
    private final ListSignal listTed_3_4 = new ListSignal();
    private final ListSignal listTed_5_6 = new ListSignal();
    private final ListSignal listTed_7_8 = new ListSignal();
    private final ListSignal listTed_9_10 = new ListSignal();
    private final ListSignal listTed_11_12 = new ListSignal();
    private final ListSignal listTed_13_14 = new ListSignal();
    private final ListSignal listTed_15_16 = new ListSignal();
    // ЭС4К
    private final ListSignal listDischargeAB = new ListSignal();
    // ВЛ11
    private final ListSignal listBV1 = new ListSignal();
    private final ListSignal listBV2 = new ListSignal();
    private final ListSignal listPSR1 = new ListSignal();
    private final ListSignal listPSR2 = new ListSignal();
    private final ListSignal listKP1 = new ListSignal();
    private final ListSignal listKP2 = new ListSignal();
    private final ListSignal listUKK1 = new ListSignal();
    private final ListSignal listUKK2 = new ListSignal();
    private final ListSignal listSER_BRAKE1 = new ListSignal();
    private final ListSignal listSER_BRAKE2 = new ListSignal();
    private final ListSignal listMV1 = new ListSignal();
    private final ListSignal listMV2 = new ListSignal();
    private final ListSignal listSSP1 = new ListSignal();
    private final ListSignal listSSP2 = new ListSignal();
    private final ListSignal listDB1 = new ListSignal();
    private final ListSignal listDB2 = new ListSignal();
    // ЭС5
    private final ListSignal listReadyAuto = new ListSignal();
    private final ListSignal listInterDriver = new ListSignal();
    private final ListSignal listKAET = new ListSignal();
    private final ListSignal listBSK = new ListSignal();
    private final ListSignal listRT = new ListSignal();
    // ЭС6
    private final ListSignal listControlAuto = new ListSignal();
    private final ListSignal listWhistleEPK = new ListSignal();
    // KZ8A
    private final ListSignal listUzTCU = new ListSignal();
    private final ListSignal listUzWSP = new ListSignal();
    // БХВ
    private final ListSignal listBHV_BrakeTail = new ListSignal();
    private final ListSignal listBHV_IsSlaveChan = new ListSignal();
    private final ListSignal listBHV_LinkSlaveChan = new ListSignal();
    private final ListSignal listBHV_LinkMainChan = new ListSignal();
    private final ListSignal listBHV_IsCommand = new ListSignal();
    private final ListSignal listBHV_FanEmergencyBrake = new ListSignal();
    private final ListSignal listBHV_FanBrake = new ListSignal();
    private final ListSignal listBHV_SensorPressRK = new ListSignal();
    private final ListSignal listBHV_SensorPressTM = new ListSignal();
    private final ListSignal listBHV_UpdateParam = new ListSignal();
    private final ListSignal listBHV_DebugOtp = new ListSignal();
    private final ListSignal listBHV_Ready = new ListSignal();
    private final ListSignal listBHV_Other = new ListSignal();
    private final ListSignal listBHV_InterventionRadio = new ListSignal();
    private final ListSignal listBHV_CrashAfuMost = new ListSignal();
    private final ListSignal listBHV_IsBhv = new ListSignal();
    private final ListSignal listBHV_AllowAnswer = new ListSignal();


    // keys для дискретных сигналов
    // 0x21_4
    public static final int KEY_BAN_THRUST = 1;//"BanThrust";    // запрет тяги
    public static final int KEY_BAN_BRAKE = 2;//"BanBrake";      // запрет торможения
    public static final int KEY_EMULATE = 3;//"Emulate";         // режим эмуляции
    public static final int KEY_EPK = 4;//"Epk";                 // ЭПК
    public static final int KEY_CHAIN_OFF = 5;//"ChainOff";      // откл цепей
    public static final int KEY_TEST_BRAKE_CORRUPT = 6;//"TestBrakeCorrupt"; // тест тормозов не пройден
    public static final int KEY_TEST_TRACT_CORRUPT = 7;//"TestTractCorrupt"; // тест тяги не пройден
    public static final int KEY_MANEUVER = 8;//"Maneuver";                   // маневровые работы
    public static final int KEY_MAIN_CHANNEL = 10;                  // связь по основному каналу
    public static final int KEY_ADDITIONAL_CHANNEL = 11;            // связь по доп каналу

    // 0x1D_4
    public static final int KEY_BAN_PT = 51;//"BanPt";                        //  запрет ПТ
    public static final int KEY_BAN_ET = 52;//"BanEt";                        // запрет ЭТ
    //
    public static final int KEY_GV = 101;                               // срабатывание ГВ (3С5К)
    public static final int KEY_PROTECT = 102;                          // срабатывание защиты (3С5К)
    public static final int KEY_BOX = 103;                              // боксование (3С5К, ВЛ10)
    public static final int KEY_PRESS_OVER = 104;                       // давл в ТЦ > 1,1 кгс/см2 (3С5К)
    public static final int KEY_MK_COMPRESS_OFF = 105;                  // МК откл компрессор (3С5К)
    public static final int KEY_DM_PRESS_OIL = 106;                     // ДМ давление масла (3С5К)
    //
    public static final int KEY_PST = 107;         // ПСТ
    public static final int KEY_PST1 = 108;         // ПСТ 1-я секц (ВЛ10 ВЛ80)
    public static final int KEY_PST2 = 109;         // ПСТ 2-я секц (ВЛ10 ВЛ80)
    public static final int KEY_EPK1 = 110;         // ЭПК 1-я секц (ВЛ10 ВЛ80)
    public static final int KEY_EPK2 = 111;         // ЭПК 2-я секц (ВЛ10 ВЛ80)
    public static final int KEY_BV = 112;           // Отключение БВ (ВЛ10)
    public static final int KEY_PT_REC = 113;       // Позиция ПТ при рекуперации (ВЛ10)
    public static final int KEY_TM = 114;           // Срабатывание тормозов
    public static final int KEY_REVERS = 115;       // Контроль разворота групп.переключ (ВЛ10)
    public static final int KEY_OVERLOAD = 116;     // Реле перегрузки (ВЛ10)
    public static final int KEY_STOP1 = 117;        // кнопка остановки поезда 1-я секция (ВЛ10)
    public static final int KEY_STOP2 = 118;        // кнопка остановки поезда 2-я секция (ВЛ10)

    public static final int KEY_EMERGENCY_BRAKE1 = 119; // экстренное торможение 1-я секция (ВЛ80)
    public static final int KEY_EMERGENCY_BRAKE2 = 120; // экстренное торможение 2-я секция (ВЛ80)
    public static final int KEY_BOX1 = 121;             // наличие боксования 1 (ВЛ80)
    public static final int KEY_BOX2 = 122;             // наличие боксования 2 (ВЛ80)
    public static final int KEY_RP1 = 123;             // срабатывание РП 1 (ВЛ80)
    public static final int KEY_RP2 = 124;             // срабатывание РП 4 (ВЛ80)
    public static final int KEY_TD1 = 125;             // сигнал ТД 1 (ВЛ80)
    public static final int KEY_TD2 = 126;             // сигнал ТД 2 (ВЛ80)
    public static final int KEY_RZ1 = 127;             // сигнал РЗ 1 (ВЛ80)
    public static final int KEY_RZ2 = 128;             // сигнал РЗ 2 (ВЛ80)
    public static final int KEY_INTER_DRIVER1 = 129;   // вмешательство машиниста 1 (ВЛ80 11)
    public static final int KEY_INTER_DRIVER2 = 130;   // вмешательство машиниста 2 (ВЛ80 11)
    public static final int KEY_TM1 = 131;             // срабатывание тм 1 (ВЛ80)
    public static final int KEY_TM2 = 132;             // срабатывание тм 2 (ВЛ80)
    public static final int KEY_UKKNP1 = 133;          // УККНП 1 (ВЛ80)
    public static final int KEY_UKKNP2 = 134;          // УККНП 2 (ВЛ80)
    public static final int KEY_GV1 = 135;             // отключение ГВ 1 (ВЛ80)
    public static final int KEY_GV2 = 136;             // отключение ГВ 2 (ВЛ80)
    public static final int KEY_PPV1 = 137;             // отключение ППВ 1 (ВЛ80)
    public static final int KEY_PPV2 = 138;             // отключение ППВ 2 (ВЛ80)

    public static final int KEY_EMERGENCY_BRAKE = 139;     // клапан аварийного торможения
    public static final int KEY_ACCELERATED_BRAKE = 140;   // клапан ускоренного торможения
    public static final int KEY_RELEASE_BRAKE = 141;       // отпуск тормозов
    public static final int KEY_SAND_AUTO = 142;           // песок автоматически
    public static final int KEY_HAND_SPEED_ZERO = 143;  // рукоятка задания скорости в нулевом положении
    // 0x21_7
    public static final int KEY_READY = 151;                    // готов
    public static final int KEY_CAN = 152;                      // can_modem
    public static final int KEY_ERROR = 153;                    // ошибки'{'связь сим'}
    public static final int KEY_CHANGE_ACCEPTED_SCHEDULE = 154; // смена прин расп
    public static final int KEY_CHANGE_VALID_SCHEDULE = 155;    // смена верн расп
    public static final int KEY_CHANGE_LOAD_SCHEDULE = 156;     // смена загр расп
    public static final int KEY_SOURCE_SCHEDULE = 157;          // ИР БД
    public static final int KEY_MODE_WORK = 158;                // режим работы бмс
    public static final int KEY_LEVEL_GPRS = 159;               // уровень GPRS
    public static final int KEY_SEND_DIAG = 160;                // отпр диагн
    public static final int KEY_UPDATE = 161;                   // обновление ПО
    public static final int KEY_ESM_BS = 162;                   // скачивание д с ЕСМ БС
    public static final int KEY_LINK_SERVER = 163;              // связь с сервером
    public static final int KEY_LINK_GATEWAY = 164;             // связь со шлюзом РОРС
    public static final int KEY_GPRS = 165;                     // есть GPRS услуга
    public static final int KEY_AUTO_SCHEDULE = 166;            // автовед. по расп. СИМ
    // ЭС5К МСУД-15
    public static final int KEY_TED1_1S = 200;                // отключеие тэд 1 1-й секции
    public static final int KEY_TED2_1S = 201;                // отключеие тэд 2 1-й секции
    public static final int KEY_TED3_1S = 202;                // отключеие тэд 3 1-й секции
    public static final int KEY_TED4_1S = 203;                // отключеие тэд 4 1-й секции

    public static final int KEY_TED1_2S = 204;                // отключеие тэд 1 2-й секции
    public static final int KEY_TED2_2S = 205;                // отключеие тэд 2 2-й секции
    public static final int KEY_TED3_2S = 206;                // отключеие тэд 3 2-й секции
    public static final int KEY_TED4_2S = 207;                // отключеие тэд 4 2-й секции

    public static final int KEY_TED1_3S = 208;                // отключеие тэд 1 3-й секции
    public static final int KEY_TED2_3S = 209;                // отключеие тэд 2 3-й секции
    public static final int KEY_TED3_3S = 210;                // отключеие тэд 3 3-й секции
    public static final int KEY_TED4_3S = 211;                // отключеие тэд 4 3-й секции

    public static final int KEY_TED1_4S = 212;                // отключеие тэд 1 4-й секции
    public static final int KEY_TED2_4S = 213;                // отключеие тэд 2 4-й секции
    public static final int KEY_TED3_4S = 214;                // отключеие тэд 3 4-й секции
    public static final int KEY_TED4_4S = 215;                // отключеие тэд 4 4-й секции
    // ЭС5К
    public static final int KEY_TED_1_2 = 216;                // отключеие тэд 1 и тэд 2
    public static final int KEY_TED_3_4 = 217;                // отключеие тэд 3 и тэд 4
    public static final int KEY_TED_5_6 = 218;                // отключеие тэд 5 и тэд 6
    public static final int KEY_TED_7_8 = 219;                // отключеие тэд 7 и тэд 8
    public static final int KEY_TED_9_10 = 220;               // отключеие тэд 9 и тэд 10
    public static final int KEY_TED_11_12 = 221;              // отключеие тэд 11 и тэд 12
    public static final int KEY_TED_13_14 = 222;              // отключеие тэд 13 и тэд 14
    public static final int KEY_TED_15_16 = 223;              // отключеие тэд 15 и тэд 16
    // ЭС4К
    public static final int KEY_DISCHARGE_AB = 224;          // разряд АБ
    // вл 11
    public static final int KEY_BV1 = 301;             //  БВ 1 (ВЛ11)
    public static final int KEY_BV2 = 302;             //  БВ 2 (ВЛ11)
    public static final int KEY_PSR1 = 303;             //  ПСР 1 (ВЛ11)
    public static final int KEY_PSR2 = 304;             //  ПСР 2 (ВЛ11)
    public static final int KEY_KP1 = 305;             //  КП 1 (ВЛ11)
    public static final int KEY_KP2 = 306;             //  КП 2 (ВЛ11)
    public static final int KEY_UKK1 = 307;             //  УКК 1 (ВЛ11)
    public static final int KEY_UKK2 = 308;             //  УКК 2 (ВЛ11)
    public static final int KEY_SER_BRAKE1 = 309;      //  УКК 1 (ВЛ11)
    public static final int KEY_SER_BRAKE2 = 310;     //  УКК 2 (ВЛ11)
    public static final int KEY_MV1 = 311;             //  МВ 1 (ВЛ11)
    public static final int KEY_MV2 = 312;             //  МВ 2 (ВЛ11)
    public static final int KEY_SSP1 = 313;             // ССП  1 (ВЛ11)
    public static final int KEY_SSP2 = 314;             // ССП  2 (ВЛ11)
    public static final int KEY_DB1 = 315;             // ДБ 1 (ВЛ11)
    public static final int KEY_DB2 = 316;             // ДБ 2 (ВЛ11)
    // ЭС5
    public static final int KEY_READY_AUTO = 401;           //  готов к автоведению
    public static final int KEY_INTER_DRIVER = 402;         //  вмешательство машиниста
    public static final int KEY_KAET = 403;                 //  КАЭТ
    public static final int KEY_BSK = 404;                  //  автоведение БСК
    public static final int KEY_RT = 405;                   // синхр упр РТ
    // ЭС5
    public static final int KEY_CONTROL_AUTO = 450;         // управленин от автоведения
    public static final int KEY_WHISTLE_EPK = 451;          // свисток ЭПК
    // KZ8
    public static final int KEY_UZ_TCU = 470;         // управленин от автоведения
    public static final int KEY_UZ_WSP = 471;          // свисток ЭПК

    // бхв
    public static final int KEY_BRAKE_TAIL = 500;           // торможение с хвоста
    public static final int KEY_IS_SLAVE_CHAN = 501;        // Есть доп канал (БХВ)
    public static final int KEY_LINK_SLAVE_CHAN = 502;      // связь доп канал (БХВ)
    public static final int KEY_LINK_MAIN_CHAN = 503;       // связь осн канал (БХВ)
    public static final int KEY_IS_COMMAND = 504;           // Наличие команды
    public static final int KEY_FAN_EMERGENCY_BRAKE = 505;  // Отказ вент экстр торм
    public static final int KEY_FAN_BRAKE = 506;            // Отказ вент служ торм
    public static final int KEY_SENSOR_PRESS_RK = 507;      // Отказ датч давл РК
    public static final int KEY_SENSOR_PRESS_TM = 508;      // Отказ датч давл ТМ
    public static final int KEY_UPDATE_PARAM = 509;         // Обн парам
    public static final int KEY_DEBUG_OTP = 510;            // Отладка реж отп
    public static final int KEY_BHV_READY = 511;            // БХВ настроен
    public static final int KEY_BHV_OTHER = 512;            // Другие БХВ
    public static final int KEY_INTERVENTION_RADIO = 513;   // Вмеш в радио
    public static final int KEY_CRASH_AFU_MOST = 514;       // Авария АФУ МОСТ
    public static final int KEY_IS_BHV = 515;               // БХВ включен
    public static final int KEY_ALLOW_ANSWER = 516;         // Незапр ответы

    /**
     * @param key - ключ сигнала
     * @param x - секунда ()
     * @return - активен ли сигнал на соответствующий x
     */
    public boolean isSignal(int key, int x) {
        boolean result = false;
        ListSignal listSignal = getListByKey(key);
        if (listSignal != null && !listSignal.list.isEmpty()) {
            for (int i = 0; i < listSignal.list.size(); i++) {
                ItemSignal itemSignal = listSignal.list.get(i);
                if (x >= itemSignal.begin && x <= itemSignal.end) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * участок дискретного сигнала;
     * begin- начало участка
     */
    public static class ItemSignal {
        int begin;
        int end;
        ItemSignal(int begin) {
            this.begin = begin;
        }

        public int getBegin() {
            return begin;
        }
    }

    /**
     * добавляем секунды начало и конца сигнала
     * @param arrDots - набор участков
     * @param second - тек секунда
     * @param isSignal - признак наличия сигнала
     */
    public void add(ListSignal arrDots, int second, boolean isSignal) {
        if (isSignal) {
            if (!arrDots.isLock) {
                arrDots.list.add(new ItemSignal(second));
                arrDots.isLock = true;
            }
            arrDots.list.get(arrDots.list.size() - 1).end = second;
        }
        else arrDots.isLock = false;
    }

    /**
     * @param key - ключ
     * @return список срабатываний одного сигнала
     */
    public ListSignal getListByKey(int key) {
        ListSignal listSignal = null;
        switch (key) {
            case KEY_READY: listSignal = listReady;
                break;
            case KEY_CAN: listSignal = listCan;
                break;
            case KEY_ERROR: listSignal = listError;
                break;
            case KEY_CHANGE_LOAD_SCHEDULE: listSignal = listChangeLoadSchedule;
                break;
            case KEY_CHANGE_ACCEPTED_SCHEDULE: listSignal = listChangeAcceptedSchedule;
                break;
            case KEY_CHANGE_VALID_SCHEDULE: listSignal = listChangeAcceptedValidSchedule;
                break;
            case KEY_SOURCE_SCHEDULE: listSignal = listSourceSchedule;
                break;
            case KEY_MODE_WORK: listSignal = listModeWork;
                break;
            case KEY_LEVEL_GPRS: listSignal = listLevelGPRS;
                break;
            case KEY_SEND_DIAG: listSignal = listSendDiag;
                break;
            case KEY_UPDATE: listSignal = listUpdate;
                break;
            case KEY_ESM_BS: listSignal = listEsmBs;
                break;
            case KEY_LINK_SERVER: listSignal = listLinkServer;
                break;
            case KEY_LINK_GATEWAY: listSignal = listLinkGateway;
                break;
            case KEY_GPRS: listSignal = listGPRS;
                break;
            case KEY_AUTO_SCHEDULE: listSignal = listAutoSchedule;
                break;

            case KEY_BAN_THRUST: listSignal = listBanThrust;
                break;
            case KEY_BAN_BRAKE: listSignal = listBanBrake;
                break;
            case KEY_EMULATE: listSignal = listEmulate;
                break;
            case KEY_EPK: listSignal = listEPK;
                break;
            case KEY_CHAIN_OFF: listSignal = listChainOff;
                break;
            case KEY_TEST_BRAKE_CORRUPT: listSignal = listTestBrakeCorrupt;
                break;
            case KEY_TEST_TRACT_CORRUPT: listSignal = listTestTractCorrupt;
                break;
            case KEY_MANEUVER: listSignal = listManeuver;
                break;
            case KEY_MAIN_CHANNEL: listSignal = listMainChannel;
                break;
            case KEY_ADDITIONAL_CHANNEL: listSignal = listAdditionChannel;
                break;

            case KEY_BAN_PT: listSignal = listBanPT;
                break;
            case KEY_BAN_ET: listSignal = listBanET;
                break;

            case KEY_GV: listSignal = listGV;
                break;
            case KEY_PROTECT: listSignal = listProtect;
                break;
            case KEY_BOX: listSignal = listBox;
                break;
            case KEY_PRESS_OVER: listSignal = listPressOver;
                break;
            case KEY_MK_COMPRESS_OFF: listSignal = listMKCompressOff;
                break;
            case KEY_DM_PRESS_OIL: listSignal = listDMPressOil;
                break;
            case KEY_PST: listSignal = listPst;
                break;
            case KEY_PST1: listSignal = listPst1;
                break;
            case KEY_PST2: listSignal = listPst2;
                break;
            case KEY_EPK1: listSignal = listEpk1;
                break;
            case KEY_EPK2: listSignal = listEpk2;
                break;
            case KEY_BV: listSignal = listBv;
                break;
            case KEY_PT_REC: listSignal = listPt_rec;
                break;
            case KEY_TM: listSignal = listTm;
                break;
            case KEY_REVERS: listSignal = listRevers;
                break;
            case KEY_OVERLOAD: listSignal = listOverload;
                break;
            case KEY_STOP1: listSignal = listStop1;
                break;
            case KEY_STOP2: listSignal = listStop2;
                break;

            case KEY_EMERGENCY_BRAKE1: listSignal = listEmergencyBrake1;
                break;
            case KEY_EMERGENCY_BRAKE2: listSignal = listEmergencyBrake2;
                break;
            case KEY_BOX1: listSignal = listBox1;
                break;
            case KEY_BOX2: listSignal = listBox2;
                break;
            case KEY_RP1: listSignal = listRP1;
                break;
            case KEY_RP2: listSignal = listRP2;
                break;
            case KEY_TD1: listSignal = listTD1;
                break;
            case KEY_TD2: listSignal = listTD2;
                break;
            case KEY_RZ1: listSignal = listRZ1;
                break;
            case KEY_RZ2: listSignal = listRZ2;
                break;
            case KEY_INTER_DRIVER1: listSignal = listInterDriver1;
                break;
            case KEY_INTER_DRIVER2: listSignal = listInterDriver2;
                break;
            case KEY_TM1: listSignal = listTM1;
                break;
            case KEY_TM2: listSignal = listTM2;
                break;
            case KEY_UKKNP1: listSignal = listUKKNP1;
                break;
            case KEY_UKKNP2: listSignal = listUKKNP2;
                break;
            case KEY_GV1: listSignal = listGV1;
                break;
            case KEY_GV2: listSignal = listGV2;
                break;
            case KEY_PPV1: listSignal = listPPV1;
                break;
            case KEY_PPV2: listSignal = listPPV2;
                break;

            case KEY_EMERGENCY_BRAKE: listSignal = listEmergencyBrake;
                break;
            case KEY_ACCELERATED_BRAKE: listSignal = listAcceleratedBrake;
                break;
            case KEY_RELEASE_BRAKE: listSignal = listReleaseBrake;
                break;
            case KEY_SAND_AUTO: listSignal = listSandAuto;
                break;
            case KEY_HAND_SPEED_ZERO: listSignal = listHandSpeedZero;
                break;
            // ЭС5К МСУД-15
            case KEY_TED1_1S: listSignal = listTed1_1s;
                break;
            case KEY_TED2_1S: listSignal = listTed2_1s;
                break;
            case KEY_TED3_1S: listSignal = listTed3_1s;
                break;
            case KEY_TED4_1S: listSignal = listTed4_1s;
                break;

            case KEY_TED1_2S: listSignal = listTed1_2s;
                break;
            case KEY_TED2_2S: listSignal = listTed2_2s;
                break;
            case KEY_TED3_2S: listSignal = listTed3_2s;
                break;
            case KEY_TED4_2S: listSignal = listTed4_2s;
                break;

            case KEY_TED1_3S: listSignal = listTed1_3s;
                break;
            case KEY_TED2_3S: listSignal = listTed2_3s;
                break;
            case KEY_TED3_3S: listSignal = listTed3_3s;
                break;
            case KEY_TED4_3S: listSignal = listTed4_3s;
                break;

            case KEY_TED1_4S: listSignal = listTed1_4s;
                break;
            case KEY_TED2_4S: listSignal = listTed2_4s;
                break;
            case KEY_TED3_4S: listSignal = listTed3_4s;
                break;
            case KEY_TED4_4S: listSignal = listTed4_4s;
                break;
            // ЭС5К
            case KEY_TED_1_2: listSignal = listTed_1_2;
                break;
            case KEY_TED_3_4: listSignal = listTed_3_4;
                break;
            case KEY_TED_5_6: listSignal = listTed_5_6;
                break;
            case KEY_TED_7_8: listSignal = listTed_7_8;
                break;
            case KEY_TED_9_10: listSignal = listTed_9_10;
                break;
            case KEY_TED_11_12: listSignal = listTed_11_12;
                break;
            case KEY_TED_13_14: listSignal = listTed_13_14;
                break;
            case KEY_TED_15_16: listSignal = listTed_15_16;
                break;
            case KEY_DISCHARGE_AB: listSignal = listDischargeAB;
                break;
            // вл11
            case KEY_BV1: listSignal = listBV1;
                break;
            case KEY_BV2: listSignal = listBV2;
                break;
            case KEY_PSR1: listSignal = listPSR1;
                break;
            case KEY_PSR2: listSignal = listPSR2;
                break;
            case KEY_KP1: listSignal = listKP1;
                break;
            case KEY_KP2: listSignal = listKP2;
                break;
            case KEY_UKK1: listSignal = listUKK1;
                break;
            case KEY_UKK2: listSignal = listUKK2;
                break;
            case KEY_SER_BRAKE1: listSignal = listSER_BRAKE1;
                break;
            case KEY_SER_BRAKE2: listSignal = listSER_BRAKE2;
                break;
            case KEY_MV1: listSignal = listMV1;
                break;
            case KEY_MV2: listSignal = listMV2;
                break;
            case KEY_SSP1: listSignal = listSSP1;
                break;
            case KEY_SSP2: listSignal = listSSP2;
                break;
            case KEY_DB1: listSignal = listDB1;
                break;
            case KEY_DB2: listSignal = listDB2;
                break;
            // эс5
            case KEY_READY_AUTO: listSignal = listReadyAuto;
                break;
            case KEY_INTER_DRIVER: listSignal = listInterDriver;
                break;
            case KEY_KAET: listSignal = listKAET;
                break;
            case KEY_BSK: listSignal = listBSK;
                break;
            case KEY_RT: listSignal = listRT;
                break;
            // эс6
            case KEY_CONTROL_AUTO: listSignal = listControlAuto;
                break;
            case KEY_WHISTLE_EPK: listSignal = listWhistleEPK;
                break;
            // kz8a
            case KEY_UZ_TCU: listSignal = listUzTCU;
                break;
            case KEY_UZ_WSP: listSignal = listUzWSP;
                break;

            case KEY_BRAKE_TAIL: listSignal = listBHV_BrakeTail;
                break;
            case KEY_IS_SLAVE_CHAN: listSignal = listBHV_IsSlaveChan;
                break;
            case KEY_LINK_SLAVE_CHAN: listSignal = listBHV_LinkSlaveChan;
                break;
            case KEY_LINK_MAIN_CHAN: listSignal = listBHV_LinkMainChan;
                break;
            case KEY_IS_COMMAND: listSignal = listBHV_IsCommand;
                break;
            case KEY_FAN_EMERGENCY_BRAKE: listSignal = listBHV_FanEmergencyBrake;
                break;
            case KEY_FAN_BRAKE: listSignal = listBHV_FanBrake;
                break;
            case KEY_SENSOR_PRESS_RK: listSignal = listBHV_SensorPressRK;
                break;
            case KEY_SENSOR_PRESS_TM: listSignal = listBHV_SensorPressTM;
                break;
            case KEY_UPDATE_PARAM: listSignal = listBHV_UpdateParam;
                break;
            case KEY_DEBUG_OTP: listSignal = listBHV_DebugOtp;
                break;
            case KEY_BHV_READY: listSignal = listBHV_Ready;
                break;
            case KEY_BHV_OTHER: listSignal = listBHV_Other;
                break;
            case KEY_INTERVENTION_RADIO: listSignal = listBHV_InterventionRadio;
                break;
            case KEY_CRASH_AFU_MOST: listSignal = listBHV_CrashAfuMost;
                break;
            case KEY_IS_BHV: listSignal = listBHV_IsBhv;
                break;
            case KEY_ALLOW_ANSWER: listSignal = listBHV_AllowAnswer;
                break;
        }
        return listSignal;
    }

    /**
     * @param key
     * @return description по ключу
     */
    public static String getDescriptionSygnal(int key) {
        String result = "";
        switch (key) {
            case KEY_READY: return "готов";
            case KEY_CAN: return "can_modem";
            case KEY_ERROR: return "ошибки'{'связь сим'}";
            case KEY_CHANGE_ACCEPTED_SCHEDULE: return "смена прин расп";
            case KEY_CHANGE_VALID_SCHEDULE: return "смена верн расп";
            case KEY_CHANGE_LOAD_SCHEDULE: return "смена загр расп";
            case KEY_SOURCE_SCHEDULE: return "ИР БД";
            case KEY_MODE_WORK: return "ручной режим работы";
            case KEY_LEVEL_GPRS: return "сигнал GPRS";
            case KEY_SEND_DIAG: return "отпр диагн";
            case KEY_UPDATE: return "обновление ПО";
            case KEY_ESM_BS: return "скачивание д с ЕСМ БС";
            case KEY_LINK_SERVER: return "связь с сервером";
            case KEY_LINK_GATEWAY: return "связь со шлюзом РОРС";
            case KEY_GPRS: return "есть GPRS услуга";
            case KEY_AUTO_SCHEDULE: return "автовед. по расп. СИМ";

            case KEY_BAN_THRUST: return "запрет тяги";
            case KEY_BAN_BRAKE: return "запрет торможения";
            case KEY_EMULATE: return "режим эмуляции";
            case KEY_EPK: return "эпк";
            case KEY_CHAIN_OFF: return "откл цепей";
            case KEY_TEST_BRAKE_CORRUPT: return "тест тормозов не пройден";
            case KEY_TEST_TRACT_CORRUPT: return "тест тяги не пройден";
            case KEY_MANEUVER: return "маневровые работы";
            case KEY_MAIN_CHANNEL: return "связь осн канал";
            case KEY_ADDITIONAL_CHANNEL: return "связь доп канал";

            case KEY_BAN_PT: return "запрет пт";
            case KEY_BAN_ET: return "запрет эт";

            case KEY_GV: return "гв";
            case KEY_PROTECT: return "защита";
            case KEY_BOX: return "боксование";
            case KEY_PRESS_OVER: return "давл в тц > 1,1 кгс/см2";
            case KEY_MK_COMPRESS_OFF: return "мк откл компрессор";
            case KEY_DM_PRESS_OIL: return "дм давление масла";
            case KEY_PST: return "пст";
            case KEY_PST1: return "пст 1-я секц";
            case KEY_PST2: return "пст 2-я секц";
            case KEY_EPK1: return "эпк 1-я секц";
            case KEY_EPK2: return "эпк 2-я секц";
            case KEY_BV: return "откючение бв";
            case KEY_PT_REC: return "пт при рекуперации";
            case KEY_TM: return "тм";
            case KEY_REVERS: return "контроль разворота групп.переключ";
            case KEY_OVERLOAD: return "реле перегрузки";
            case KEY_STOP1: return "кнопка остановки 1-я секция";
            case KEY_STOP2: return "кнопка остановки 2-я секция";

            case KEY_EMERGENCY_BRAKE1: return "экстренное торможение 1-секции";
            case KEY_EMERGENCY_BRAKE2: return "экстренное торможение 2-секции";
            case KEY_BOX1: return "боксование 1-секции";
            case KEY_BOX2: return "боксование 2-секции";
            case KEY_RP1: return "РП 1-секции";
            case KEY_RP2: return "РП 2-секции";
            case KEY_TD1: return "ТД 1-секции";
            case KEY_TD2: return "ТД 2-секции";
            case KEY_RZ1: return "РЗ 1-секции";
            case KEY_RZ2: return "РЗ 2-секции";
            case KEY_INTER_DRIVER1: return "вмешательство машиниста 1-секции";
            case KEY_INTER_DRIVER2: return "вмешательство машиниста 2-секции";
            case KEY_TM1: return "ТМ 1-секции";
            case KEY_TM2: return "ТМ 2-секции";
            case KEY_UKKNP1: return "УККНП 1-секции";
            case KEY_UKKNP2: return "УККНП 2-секции";
            case KEY_GV1: return "ГВ 1-секции";
            case KEY_GV2: return "ГВ 2-секции";
            case KEY_PPV1: return "ППВ 1-секции";
            case KEY_PPV2: return "ППВ 2-секции";

            case KEY_EMERGENCY_BRAKE: return "клапан аварийного торможения";
            case KEY_ACCELERATED_BRAKE: return "клапан ускоренного торможения";
            case KEY_RELEASE_BRAKE: return "отпуск тормозов";
            case KEY_SAND_AUTO: return "песок автоматически";
            case KEY_HAND_SPEED_ZERO: return "рукоятка скорости-0";
            // ЭС5К МСУД-15
            case KEY_TED1_1S: return "откл. ТЭД-1 1-й секции";
            case KEY_TED2_1S: return "откл. ТЭД-2 1-й секции";
            case KEY_TED3_1S: return "откл. ТЭД-3 1-й секции";
            case KEY_TED4_1S: return "откл. ТЭД-4 1-й секции";

            case KEY_TED1_2S: return "откл. ТЭД-1 2-й секции";
            case KEY_TED2_2S: return "откл. ТЭД-2 2-й секции";
            case KEY_TED3_2S: return "откл. ТЭД-3 2-й секции";
            case KEY_TED4_2S: return "откл. ТЭД-4 2-й секции";

            case KEY_TED1_3S: return "откл. ТЭД-1 3-й секции";
            case KEY_TED2_3S: return "откл. ТЭД-2 3-й секции";
            case KEY_TED3_3S: return "откл. ТЭД-3 3-й секции";
            case KEY_TED4_3S: return "откл. ТЭД-4 3-й секции";

            case KEY_TED1_4S: return "откл. ТЭД-1 4-й секции";
            case KEY_TED2_4S: return "откл. ТЭД-2 4-й секции";
            case KEY_TED3_4S: return "откл. ТЭД-3 4-й секции";
            case KEY_TED4_4S: return "откл. ТЭД-4 4-й секции";

            // ЭС5К МСУД
            case KEY_TED_1_2: return "откл. ТЭД-1 и ТЭД-2";
            case KEY_TED_3_4: return "откл. ТЭД-3 и ТЭД-4";
            case KEY_TED_5_6: return "откл. ТЭД-5 и ТЭД-6";
            case KEY_TED_7_8: return "откл. ТЭД-7 и ТЭД-8";
            case KEY_TED_9_10: return "откл. ТЭД-9 и ТЭД-10";
            case KEY_TED_11_12: return "откл. ТЭД-11 и ТЭД-12";
            case KEY_TED_13_14: return "откл. ТЭД-13 и ТЭД-14";
            case KEY_TED_15_16: return "откл. ТЭД-15 и ТЭД-16";

            case KEY_DISCHARGE_AB:return "разрядка АБ";
            // вл 11
            case KEY_BV1: return "БВ 1-й секции";
            case KEY_BV2: return "БВ 2-й секции";
            case KEY_PSR1: return "ПСР 1-й секции";
            case KEY_PSR2: return "ПСР 2-й секции";
            case KEY_KP1: return "КП 1-й секции";
            case KEY_KP2: return "КП 2-й секции";
            case KEY_UKK1: return "УКК 1-й секции";
            case KEY_UKK2: return "УКК 2-й секции";
            case KEY_SER_BRAKE1: return "вмешательство машиниста 1-й секции";
            case KEY_SER_BRAKE2: return "вмешательство машиниста 2-й секции";
            case KEY_MV1: return "МВ 1-й секции";
            case KEY_MV2: return "МВ 2-й секции";
            case KEY_SSP1: return "С-СП 1-й секции";
            case KEY_SSP2: return "С-СП 2-й секции";
            case KEY_DB1: return "ДБ 1-й секции";
            case KEY_DB2: return "ДБ 2-й секции";
            // эс5
            case KEY_READY_AUTO: return "готов к автоведению";
            case KEY_INTER_DRIVER: return "вмешательство машиниста";
            case KEY_KAET: return "КАЭТ";
            case KEY_BSK: return "автоведение БСК";
            case KEY_RT: return "синхр. упр. РТ";
            // эс6
            case KEY_CONTROL_AUTO: return "упр-е от автоведения";
            case KEY_WHISTLE_EPK: return "свисток ЭПК";
            // kz8a
            case KEY_UZ_TCU: return "юз от TCU";
            case KEY_UZ_WSP: return "юз от WSP";
            // БХВ
            case KEY_BRAKE_TAIL: return "торможение с хвоста";
            case KEY_IS_SLAVE_CHAN: return "есть доп канал (БХВ)";
            case KEY_LINK_SLAVE_CHAN: return "связь доп канал (БХВ)";
            case KEY_LINK_MAIN_CHAN: return "связь осн канал (БХВ)";
            case KEY_IS_COMMAND: return "наличие комманды";
            case KEY_FAN_EMERGENCY_BRAKE: return "отказ вент экстр торм";
            case KEY_FAN_BRAKE: return "отказ вент служ торм";
            case KEY_SENSOR_PRESS_RK: return "отказ датч давл РК";
            case KEY_SENSOR_PRESS_TM: return "отказ датч давл ТМ";
            case KEY_UPDATE_PARAM: return "обн парам";
            case KEY_DEBUG_OTP: return "отладка реж отп";
            case KEY_BHV_READY: return "бхв настроен";
            case KEY_BHV_OTHER: return "другие БХВ";
            case KEY_INTERVENTION_RADIO: return "вмеш в радио";
            case KEY_CRASH_AFU_MOST: return "авария АФУ МОСТ";
            case KEY_IS_BHV: return "бхв включен";
            case KEY_ALLOW_ANSWER: return "незапр ответы";
        }
        return result;
    }

    public ListSignal getListReady() {
        return listReady;
    }

    public ListSignal getListCan() {
        return listCan;
    }

    public ListSignal getListError() {
        return listError;
    }

    public ListSignal getListChangeAcceptedSchedule() {
        return listChangeAcceptedSchedule;
    }

    public ListSignal getListChangeAcceptedValidSchedule() {
        return listChangeAcceptedValidSchedule;
    }

    public ListSignal getListChangeLoadSchedule() {
        return listChangeLoadSchedule;
    }

    public ListSignal getListSourceSchedule() {
        return listSourceSchedule;
    }

    public ListSignal getListModeWork() {
        return listModeWork;
    }

    public ListSignal getListLevelGPRS() {
        return listLevelGPRS;
    }

    public ListSignal getListSendDiag() {
        return listSendDiag;
    }

    public ListSignal getListUpdate() {
        return listUpdate;
    }

    public ListSignal getListEsmBs() {
        return listEsmBs;
    }

    public ListSignal getListLinkServer() {
        return listLinkServer;
    }

    public ListSignal getListLinkGateway() {
        return listLinkGateway;
    }

    public ListSignal getListGPRS() {
        return listGPRS;
    }

    public ListSignal getListAutoSchedule() {
        return listAutoSchedule;
    }

    public ListSignal getListBanThrust() {
        return listBanThrust;
    }

    public ListSignal getListBanBrake() {
        return listBanBrake;
    }

    public ListSignal getListEmulate() {
        return listEmulate;
    }

    public ListSignal getListEPK() {
        return listEPK;
    }

    public ListSignal getListChainOff() {
        return listChainOff;
    }

    public ListSignal getListTestBrakeCorrupt() {
        return listTestBrakeCorrupt;
    }

    public ListSignal getListTestTractCorrupt() {
        return listTestTractCorrupt;
    }

    public ListSignal getListManeuver() {
        return listManeuver;
    }

    public ListSignal getListMainChannel() {
        return listMainChannel;
    }

    public ListSignal getListAdditionChannel() {
        return listAdditionChannel;
    }

    public ListSignal getListBanPT() {
        return listBanPT;
    }

    public ListSignal getListBanET() {
        return listBanET;
    }

    public ListSignal getListGV() {
        return listGV;
    }

    public ListSignal getListProtect() {
        return listProtect;
    }

    public ListSignal getListBox() {
        return listBox;
    }

    public ListSignal getListPressOver() {
        return listPressOver;
    }

    public ListSignal getListMKCompressOff() {
        return listMKCompressOff;
    }

    public ListSignal getListDMPressOil() {
        return listDMPressOil;
    }

    public ListSignal getListPst() {
        return listPst;
    }

    public ListSignal getListPst1() {
        return listPst1;
    }

    public ListSignal getListPst2() {
        return listPst2;
    }

    public ListSignal getListEpk1() {
        return listEpk1;
    }

    public ListSignal getListEpk2() {
        return listEpk2;
    }

    public ListSignal getListBv() {
        return listBv;
    }

    public ListSignal getListPt_rec() {
        return listPt_rec;
    }

    public ListSignal getListTm() {
        return listTm;
    }

    public ListSignal getListRevers() {
        return listRevers;
    }

    public ListSignal getListOverload() {
        return listOverload;
    }

    public ListSignal getListStop1() {
        return listStop1;
    }

    public ListSignal getListStop2() {
        return listStop2;
    }

    public ListSignal getListEmergencyBrake() {
        return listEmergencyBrake;
    }

    public ListSignal getListAcceleratedBrake() {
        return listAcceleratedBrake;
    }

    public ListSignal getListReleaseBrake() {
        return listReleaseBrake;
    }

    public ListSignal getListSandAuto() {
        return listSandAuto;
    }

    public ListSignal getListHandSpeedZero() {
        return listHandSpeedZero;
    }

    public ListSignal getListTed1_1s() {
        return listTed1_1s;
    }

    public ListSignal getListTed2_1s() {
        return listTed2_1s;
    }

    public ListSignal getListTed3_1s() {
        return listTed3_1s;
    }

    public ListSignal getListTed4_1s() {
        return listTed4_1s;
    }

    public ListSignal getListTed1_2s() {
        return listTed1_2s;
    }

    public ListSignal getListTed2_2s() {
        return listTed2_2s;
    }

    public ListSignal getListTed3_2s() {
        return listTed3_2s;
    }

    public ListSignal getListTed4_2s() {
        return listTed4_2s;
    }

    public ListSignal getListTed1_3s() {
        return listTed1_3s;
    }

    public ListSignal getListTed2_3s() {
        return listTed2_3s;
    }

    public ListSignal getListTed3_3s() {
        return listTed3_3s;
    }

    public ListSignal getListTed4_3s() {
        return listTed4_3s;
    }

    public ListSignal getListTed1_4s() {
        return listTed1_4s;
    }

    public ListSignal getListTed2_4s() {
        return listTed2_4s;
    }

    public ListSignal getListTed3_4s() {
        return listTed3_4s;
    }

    public ListSignal getListTed4_4s() {
        return listTed4_4s;
    }

    public ListSignal getListTed_1_2() {
        return listTed_1_2;
    }

    public ListSignal getListTed_3_4() {
        return listTed_3_4;
    }

    public ListSignal getListTed_5_6() {
        return listTed_5_6;
    }

    public ListSignal getListTed_7_8() {
        return listTed_7_8;
    }

    public ListSignal getListTed_9_10() {
        return listTed_9_10;
    }

    public ListSignal getListTed_11_12() {
        return listTed_11_12;
    }

    public ListSignal getListTed_13_14() {
        return listTed_13_14;
    }

    public ListSignal getListTed_15_16() {
        return listTed_15_16;
    }

    public ListSignal getListEmergencyBrake1() {
        return listEmergencyBrake1;
    }

    public ListSignal getListEmergencyBrake2() {
        return listEmergencyBrake2;
    }

    public ListSignal getListBox1() {
        return listBox1;
    }

    public ListSignal getListBox2() {
        return listBox2;
    }

    public ListSignal getListRP1() {
        return listRP1;
    }

    public ListSignal getListRP2() {
        return listRP2;
    }

    public ListSignal getListTD1() {
        return listTD1;
    }

    public ListSignal getListTD2() {
        return listTD2;
    }

    public ListSignal getListRZ1() {
        return listRZ1;
    }

    public ListSignal getListRZ2() {
        return listRZ2;
    }

    public ListSignal getListInterDriver1() {
        return listInterDriver1;
    }

    public ListSignal getListInterDriver2() {
        return listInterDriver2;
    }

    public ListSignal getListTM1() {
        return listTM1;
    }

    public ListSignal getListTM2() {
        return listTM2;
    }

    public ListSignal getListUKKNP1() {
        return listUKKNP1;
    }

    public ListSignal getListUKKNP2() {
        return listUKKNP2;
    }

    public ListSignal getListGV1() {
        return listGV1;
    }

    public ListSignal getListGV2() {
        return listGV2;
    }

    public ListSignal getListPPV1() {
        return listPPV1;
    }

    public ListSignal getListPPV2() {
        return listPPV2;
    }

    public ListSignal getListBV1() {
        return listBV1;
    }

    public ListSignal getListBV2() {
        return listBV2;
    }

    public ListSignal getListPSR1() {
        return listPSR1;
    }

    public ListSignal getListPSR2() {
        return listPSR2;
    }

    public ListSignal getListKP1() {
        return listKP1;
    }

    public ListSignal getListKP2() {
        return listKP2;
    }

    public ListSignal getListUKK1() {
        return listUKK1;
    }

    public ListSignal getListUKK2() {
        return listUKK2;
    }

    public ListSignal getListSER_BRAKE1() {
        return listSER_BRAKE1;
    }

    public ListSignal getListSER_BRAKE2() {
        return listSER_BRAKE2;
    }

    public ListSignal getListMV1() {
        return listMV1;
    }

    public ListSignal getListMV2() {
        return listMV2;
    }

    public ListSignal getListSSP1() {
        return listSSP1;
    }

    public ListSignal getListSSP2() {
        return listSSP2;
    }

    public ListSignal getListDB1() {
        return listDB1;
    }

    public ListSignal getListDB2() {
        return listDB2;
    }

    public ListSignal getListReadyAuto() {
        return listReadyAuto;
    }

    public ListSignal getListControlAuto() {
        return listControlAuto;
    }

    public ListSignal getListWhistleEPK() {
        return listWhistleEPK;
    }

    public ListSignal getListUzTCU() {
        return listUzTCU;
    }

    public ListSignal getListUzWSP() {
        return listUzWSP;
    }

    public ListSignal getListDischargeAB() {
        return listDischargeAB;
    }

    public ListSignal getListInterDriver() {
        return listInterDriver;
    }

    public ListSignal getListKAET() {
        return listKAET;
    }

    public ListSignal getListBSK() {
        return listBSK;
    }

    public ListSignal getListRT() {
        return listRT;
    }

    public ListSignal getListBHV_BrakeTail() {
        return listBHV_BrakeTail;
    }

    public ListSignal getListBHV_IsSlaveChan() {
        return listBHV_IsSlaveChan;
    }

    public ListSignal getListBHV_LinkSlaveChan() {
        return listBHV_LinkSlaveChan;
    }

    public ListSignal getListBHV_LinkMainChan() {
        return listBHV_LinkMainChan;
    }

    public ListSignal getListBHV_IsCommand() {
        return listBHV_IsCommand;
    }

    public ListSignal getListBHV_FanEmergencyBrake() {
        return listBHV_FanEmergencyBrake;
    }

    public ListSignal getListBHV_FanBrake() {
        return listBHV_FanBrake;
    }

    public ListSignal getListBHV_SensorPressRK() {
        return listBHV_SensorPressRK;
    }

    public ListSignal getListBHV_SensorPressTM() {
        return listBHV_SensorPressTM;
    }

    public ListSignal getListBHV_UpdateParam() {
        return listBHV_UpdateParam;
    }

    public ListSignal getListBHV_DebugOtp() {
        return listBHV_DebugOtp;
    }

    public ListSignal getListBHV_Ready() {
        return listBHV_Ready;
    }

    public ListSignal getListBHV_Other() {
        return listBHV_Other;
    }

    public ListSignal getListBHV_InterventionRadio() {
        return listBHV_InterventionRadio;
    }

    public ListSignal getListBHV_CrashAfuMost() {
        return listBHV_CrashAfuMost;
    }

    public ListSignal getListBHV_IsBhv() {
        return listBHV_IsBhv;
    }

    public ListSignal getListBHV_AllowAnswer() {
        return listBHV_AllowAnswer;
    }
}
