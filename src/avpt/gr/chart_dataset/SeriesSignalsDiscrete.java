package avpt.gr.chart_dataset;

import java.awt.*;

import static avpt.gr.chart_dataset.ListSignals.*;

/**
 * дискретные сигналы
 */
public class SeriesSignalsDiscrete extends SeriesSignals {

    public SeriesSignalsDiscrete(ListSignals listSignals) {
        // 0x21_4
        addTaskSeries(KEY_BAN_THRUST, listSignals.getListBanThrust());
        addTaskSeries(KEY_BAN_BRAKE, listSignals.getListBanBrake());
        addTaskSeries(KEY_EMULATE, listSignals.getListEmulate());
        addTaskSeries(KEY_EPK, listSignals.getListEPK());
        addTaskSeries(KEY_CHAIN_OFF, listSignals.getListChainOff());
        addTaskSeries(KEY_TEST_BRAKE_CORRUPT, listSignals.getListTestBrakeCorrupt());
        addTaskSeries(KEY_TEST_TRACT_CORRUPT, listSignals.getListTestTractCorrupt());
        addTaskSeries(KEY_MANEUVER, listSignals.getListManeuver());
//        addTaskSeries(KEY_MAIN_CHANNEL, listSignals.getListMainChannel());
//        addTaskSeries(KEY_ADDITIONAL_CHANNEL, listSignals.getListAdditionChannel());
        // 0x1D_4
        addTaskSeries(KEY_BAN_PT, listSignals.getListBanPT());
        addTaskSeries(KEY_BAN_ET, listSignals.getListBanET());
        //
        addTaskSeries(KEY_GV, listSignals.getListGV());
        addTaskSeries(KEY_PROTECT, listSignals.getListProtect());
        addTaskSeries(KEY_BOX, listSignals.getListBox());
        addTaskSeries(KEY_PRESS_OVER, listSignals.getListPressOver());
        addTaskSeries(KEY_MK_COMPRESS_OFF, listSignals.getListMKCompressOff());
        addTaskSeries(KEY_DM_PRESS_OIL, listSignals.getListDMPressOil());

        addTaskSeries(KEY_PST, listSignals.getListPst());
        addTaskSeries(KEY_PST1, listSignals.getListPst1());
        addTaskSeries(KEY_PST2, listSignals.getListPst2());
        addTaskSeries(KEY_EPK1, listSignals.getListEpk1());
        addTaskSeries(KEY_EPK2, listSignals.getListEpk2());
        addTaskSeries(KEY_BV, listSignals.getListBv());
        addTaskSeries(KEY_PT_REC, listSignals.getListPt_rec());
        addTaskSeries(KEY_TM, listSignals.getListTm());
        addTaskSeries(KEY_REVERS, listSignals.getListRevers());
        addTaskSeries(KEY_OVERLOAD, listSignals.getListOverload());
        addTaskSeries(KEY_STOP1, listSignals.getListStop1());
        addTaskSeries(KEY_STOP2, listSignals.getListStop2());
        addTaskSeries(KEY_EMERGENCY_BRAKE, listSignals.getListEmergencyBrake());
        addTaskSeries(KEY_ACCELERATED_BRAKE, listSignals.getListAcceleratedBrake());
        addTaskSeries(KEY_RELEASE_BRAKE, listSignals.getListReleaseBrake());
        addTaskSeries(KEY_SAND_AUTO, listSignals.getListSandAuto());
        addTaskSeries(KEY_HAND_SPEED_ZERO, listSignals.getListHandSpeedZero());

        addTaskSeries(KEY_EMERGENCY_BRAKE1, listSignals.getListEmergencyBrake1());
        addTaskSeries(KEY_EMERGENCY_BRAKE2, listSignals.getListEmergencyBrake2());
        addTaskSeries(KEY_BOX1, listSignals.getListBox1());
        addTaskSeries(KEY_BOX2, listSignals.getListBox2());
        addTaskSeries(KEY_RP1, listSignals.getListRP1());
        addTaskSeries(KEY_RP2, listSignals.getListRP2());
        addTaskSeries(KEY_TD1, listSignals.getListTD1());
        addTaskSeries(KEY_TD2, listSignals.getListTD2());
        addTaskSeries(KEY_RZ1, listSignals.getListRZ1());
        addTaskSeries(KEY_RZ2, listSignals.getListRZ2());
        addTaskSeries(KEY_INTER_DRIVER1, listSignals.getListInterDriver1());
        addTaskSeries(KEY_INTER_DRIVER2, listSignals.getListInterDriver2());
        addTaskSeries(KEY_TM1, listSignals.getListTM1());
        addTaskSeries(KEY_TM2, listSignals.getListTM2());
        addTaskSeries(KEY_UKKNP1, listSignals.getListUKKNP1());
        addTaskSeries(KEY_UKKNP2, listSignals.getListUKKNP2());
        addTaskSeries(KEY_GV1, listSignals.getListGV1());
        addTaskSeries(KEY_GV2, listSignals.getListGV2());
        addTaskSeries(KEY_PPV1, listSignals.getListPPV1());
        addTaskSeries(KEY_PPV2, listSignals.getListPPV2());

        addTaskSeries(KEY_BV1, listSignals.getListBV1());
        addTaskSeries(KEY_BV2, listSignals.getListBV2());
        addTaskSeries(KEY_PSR1, listSignals.getListPSR1());
        addTaskSeries(KEY_PSR2, listSignals.getListPSR2());
        addTaskSeries(KEY_KP1, listSignals.getListKP1());
        addTaskSeries(KEY_KP2, listSignals.getListKP2());
        addTaskSeries(KEY_UKK1, listSignals.getListUKK1());
        addTaskSeries(KEY_UKK2, listSignals.getListUKK2());
        addTaskSeries(KEY_SER_BRAKE1, listSignals.getListSER_BRAKE1());
        addTaskSeries(KEY_SER_BRAKE2, listSignals.getListSER_BRAKE2());
        addTaskSeries(KEY_MV1, listSignals.getListMV1());
        addTaskSeries(KEY_MV2, listSignals.getListMV2());
        addTaskSeries(KEY_SSP1, listSignals.getListSSP1());
        addTaskSeries(KEY_SSP2, listSignals.getListSSP2());
        addTaskSeries(KEY_DB1, listSignals.getListDB1());
        addTaskSeries(KEY_DB2, listSignals.getListDB2());

        addTaskSeries(KEY_READY_AUTO, listSignals.getListReadyAuto());
        addTaskSeries(KEY_INTER_DRIVER, listSignals.getListInterDriver());
        addTaskSeries(KEY_KAET, listSignals.getListKAET());
        addTaskSeries(KEY_BSK, listSignals.getListBSK());
        addTaskSeries(KEY_RT, listSignals.getListRT());

        addTaskSeries(KEY_CONTROL_AUTO, listSignals.getListControlAuto());
        addTaskSeries(KEY_WHISTLE_EPK, listSignals.getListWhistleEPK());

        addTaskSeries(KEY_UZ_TCU, listSignals.getListUzTCU());
        addTaskSeries(KEY_UZ_WSP, listSignals.getListUzWSP());

        addTaskSeries(KEY_DISCHARGE_AB, listSignals.getListDischargeAB());
    }

    public Color getColorSeries(int key) {
        Color color =Color.GRAY;
        switch (key) {
            // 0x21_4
            case KEY_BAN_THRUST : color = new Color(0xFFA07A);
                break;
            case KEY_BAN_BRAKE : color = new Color(0x32CD32);
                break;
            case KEY_EMULATE : color = new Color(0x800000);
                break;
            case KEY_EPK : color = new Color(0xBA55D3);
                break;
            case KEY_CHAIN_OFF : color = new Color(0x0000FF);
                break;
            case KEY_TEST_BRAKE_CORRUPT : color = new Color(0x8A2BE2);
                break;
            case KEY_TEST_TRACT_CORRUPT : color = new Color(0xA52A2A);
                break;
            case KEY_MANEUVER : color = new Color(0xE2E2E2);
                break;
            case KEY_MAIN_CHANNEL: color = new Color(0xF6F619);
                break;
            case KEY_ADDITIONAL_CHANNEL: color = new Color(0x73E04E);
                break;

            // 0x1D_4
            case KEY_BAN_PT : color = new Color(0x5F9EA0);
                break;
            case KEY_BAN_ET : color = new Color(0xD2691E);
                break;
            //
            case KEY_GV : color = new Color(0x6495ED);
                break;
            case KEY_PROTECT : color = new Color(0x8B008B);
                break;
            case KEY_BOX : color = new Color(0xDC143C);
                break;
            case KEY_PRESS_OVER : color = new Color(0x008B8B);
                break;
            case KEY_MK_COMPRESS_OFF : color = new Color(0xB8860B);
                break;
            case KEY_DM_PRESS_OIL : color = new Color(0x006400);
                break;
            case KEY_PST : color = new Color(0x5A641C);
                break;
            case KEY_PST1 : color = new Color(0x5A641C);
                break;
            case KEY_PST2 : color = new Color(0x42641C);
                break;
            case KEY_EPK1 : color = new Color(0xD369B9);
                break;
            case KEY_EPK2 : color = new Color(0x9B5AD3);
                break;
            case KEY_BV : color = new Color(0x6495ED);
                break;
            case KEY_PT_REC : color = new Color(0x74EDCA);
                break;
            case KEY_TM : color = new Color(0x8ADFED);
                break;
            case KEY_REVERS : color = new Color(0xC8EDA3);
                break;
            case KEY_OVERLOAD : color = new Color(0xEDC79C);
                break;
            case KEY_STOP1 : color = new Color(0xED9B9E);
                break;
            case KEY_STOP2 : color = new Color(0xED6065);
                break;
            case KEY_EMERGENCY_BRAKE : color = new Color(0xED050B);
                break;
            case KEY_ACCELERATED_BRAKE: color = new Color(0xED6FC0);
                break;
            case KEY_RELEASE_BRAKE: color = new Color(0x5161ED);
                break;
            case KEY_SAND_AUTO: color = new Color(0xEDD993);
                break;
            case KEY_HAND_SPEED_ZERO: color = new Color(0x9FB6ED);
                break;

            case KEY_EMERGENCY_BRAKE1: color = new Color(0x80F6DA);
                break;
            case KEY_EMERGENCY_BRAKE2: color = new Color(0x80F6DA);
                break;
            case KEY_BOX1: color = new Color(0x80BBF6);
                break;
            case KEY_BOX2: color = new Color(0x80BBF6);
                break;
            case KEY_RP1: color = new Color(0x8098F6);
                break;
            case KEY_RP2: color = new Color(0x8098F6);
                break;
            case KEY_TD1: color = new Color(0xA780F6);
                break;
            case KEY_TD2: color = new Color(0xAB80F6);
                break;
            case KEY_RZ1: color = new Color(0xF680D5);
                break;
            case KEY_RZ2: color = new Color(0xF680CD);
                break;
            case KEY_INTER_DRIVER1: color = new Color(0x80F686);
                break;
            case KEY_INTER_DRIVER2: color = new Color(0x80F698);
                break;
            case KEY_TM1: color = new Color(0xCFF680);
                break;
            case KEY_TM2: color = new Color(0xCBF680);
                break;
            case KEY_UKKNP1: color = new Color(0xF6DE80);
                break;
            case KEY_UKKNP2: color = new Color(0xF6E680);
                break;
            case KEY_GV1: color = new Color(0xF6AD80);
                break;
            case KEY_GV2: color = new Color(0xF6BB80);
                break;
            case KEY_PPV1: color = new Color(0x244654);
                break;
            case KEY_PPV2: color = new Color(0x244654);
                break;

            case KEY_BV1: color = new Color(0x370C56);
                break;
            case KEY_BV2: color = new Color(0x370C56);
                break;
            case KEY_PSR1: color = new Color(0x434F01);
                break;
            case KEY_PSR2: color = new Color(0x434F01);
                break;
            case KEY_KP1: color = new Color(0xFFC2C2);
                break;
            case KEY_KP2: color = new Color(0xFFC2C2);
                break;
            case KEY_UKK1: color = new Color(0x0000FF);
                break;
            case KEY_UKK2: color = new Color(0x0000FF);
                break;
            case KEY_SER_BRAKE1: color = new Color(0x016050);
                break;
            case KEY_SER_BRAKE2: color = new Color(0x016050);
                break;
            case KEY_MV1: color = new Color(0xFFFFFF);
                break;
            case KEY_MV2: color = new Color(0xFFFFFF);
                break;
            case KEY_SSP1: color = new Color(0x244654);
                break;
            case KEY_SSP2: color = new Color(0x244654);
                break;
            case KEY_DB1: color = new Color(0x70E797);
                break;
            case KEY_DB2: color = new Color(0x70E797);
                break;

            case KEY_READY_AUTO: color = new Color(0x014D75);
                break;
            case KEY_INTER_DRIVER: color = new Color(0x80F686);
                break;
            case KEY_KAET: color = new Color(0x0000FF);
                break;
            case KEY_BSK: color = new Color(0xFFC2C2);
                break;
            case KEY_RT: color = new Color(0x434F01);
                break;
            case KEY_CONTROL_AUTO: color = new Color(0x014D74);
                break;
            case KEY_WHISTLE_EPK: color = new Color(0x80F612);
                break;
            case KEY_DISCHARGE_AB: color = new Color(0xD26FFC);
                break;
            case KEY_UZ_TCU: color = new Color(0x9C1522);
                break;
            case KEY_UZ_WSP: color = new Color(0xA22D76);
                break;
        }
        return color;
    }
}
