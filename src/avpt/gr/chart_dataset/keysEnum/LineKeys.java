package avpt.gr.chart_dataset.keysEnum;

import org.jfree.chart.renderer.xy.XYItemRenderer;

/**
 * перечисление ключей для линий
 */
public enum LineKeys {
    // поезда
    TRAIN("Train", ""),
    // напряжение контактной сети
    VOLT("Voltage", "  %.1f В"),
    VOLT_S1("Voltage_s1", " 1-й секции: %.1f В"),
    VOLT_S2("Voltage_s2", " 2-й секции: %.1f В"),
    VOLT_S3("Voltage_s3", " 3-й секции: %.1f В"),
    VOLT_S4("Voltage_s4", " 4-й секции: %.1f В"),
    // напряжение двигателя
    VOLT_ENG_S1("VoltageEngine_s1", " 1-й секции: %.1f В"),
    VOLT_ENG_S2("VoltageEngine_s2", " 2-й секции: %.1f В"),
    VOLT_ENG1_S1("VoltageEngine1_s1", " 1 1-й секции: %.1f В"),
    VOLT_ENG2_S1("VoltageEngine2_s1", " 2 1-й секции: %.1f В"),
    VOLT_ENG3_S1("VoltageEngine3_s1", " 3 1-й секции: %.1f В"),
    VOLT_ENG4_S1("VoltageEngine4_s1", " 4 1-й секции: %.1f В"),
    VOLT_ENG1_S2("VoltageEngine1_s2", " 1 2-й секции: %.1f В"),
    VOLT_ENG2_S2("VoltageEngine2_s2", " 2 2-й секции: %.1f В"),
    VOLT_ENG3_S2("VoltageEngine3_s2", " 3 2-й секции: %.1f В"),
    VOLT_ENG4_S2("VoltageEngine4_s2", " 4 2-й секции: %.1f В"),
    VOLT_ENG1_S3("VoltageEngine1_s3", " 1 3-й секции: %.1f В"),
    VOLT_ENG2_S3("VoltageEngine2_s3", " 2 3-й секции: %.1f В"),
    VOLT_ENG3_S3("VoltageEngine3_s3", " 3 3-й секции: %.1f В"),
    VOLT_ENG4_S3("VoltageEngine4_s3", " 4 3-й секции: %.1f В"),
    VOLT_ENG1_S4("VoltageEngine1_s4", " 1 4-й секции: %.1f В"),
    VOLT_ENG2_S4("VoltageEngine2_s4", " 2 4-й секции: %.1f В"),
    VOLT_ENG3_S4("VoltageEngine3_s4", " 3 4-й секции: %.1f В"),
    VOLT_ENG4_S4("VoltageEngine4_s4", " 4 4-й секции: %.1f В"),
    VOLT_AKB_BHV_GET("VoltageAkbBhvGet", " АКБ БХВ прием   : %.1f В"),
    VOLT_AKB_BHV_SEND("VoltageAkbBhvSend", " АКБ БХВ передача: %.1f В"),
    // общий ток
    AMP("listAmperage", "  %.1f A"),
    AMP_S1("listAmperage_s1"," 1-й секции: %.1f А"),
    AMP_S2("listAmperage_s2"," 2-й секции: %.1f А"),
    AMP_S3("listAmperage_s3"," 3-й секции: %.1f А"),
    AMP_S4("listAmperage_s4"," 4-й секции: %.1f А"),
    // ток якоря
    AMP_ANC("AmperageAnchor", " %.1f А"),
    AMP_ANC1_S1("AmperageAnchor1_s1", " 1 1-й секции: %.1f А"),
    AMP_ANC1_S2("AmperageAnchor1_s2", " 1 2-й секции: %.1f А"),
    AMP_ANC1_S3("AmperageAnchor1_s3", " 1 3-й секции: %.1f А"),
    AMP_ANC1_S4("AmperageAnchor1_s4", " 1 4-й секции: %.1f А"),
    AMP_ANC2_S1("AmperageAnchor2_s1", " 2 1-й секции: %.1f А"),
    AMP_ANC2_S2("AmperageAnchor2_s2", " 2 2-й секции: %.1f А"),
    AMP_ANC2_S3("AmperageAnchor2_s3", " 2 3-й секции: %.1f А"),
    AMP_ANC2_S4("AmperageAnchor2_s4", " 2 4-й секции: %.1f А"),
    AMP_ANC3_S1("AmperageAnchor3_s1", " 3 1-й секции: %.1f А"),
    AMP_ANC3_S2("AmperageAnchor3_s2", " 3 2-й секции: %.1f А"),
    AMP_ANC3_S3("AmperageAnchor3_s3", " 3 3-й секции: %.1f А"),
    AMP_ANC3_S4("AmperageAnchor3_s4", " 3 4-й секции: %.1f А"),
    AMP_ANC4_S1("AmperageAnchor4_s1", " 4 1-й секции: %.1f А"),
    AMP_ANC4_S2("AmperageAnchor4_s2", " 4 2-й секции: %.1f А"),
    AMP_ANC4_S3("AmperageAnchor4_s3", " 4 3-й секции: %.1f А"),
    AMP_ANC4_S4("AmperageAnchor4_s4", " 4 4-й секции: %.1f А"),
    // ток возбуждения
    AMP_EXC("AmperageExcitation", " %.1f А"),
    AMP_EXC_S1("AmperageExcitation_s1", " 1-й секции: %.1f А"),
    AMP_EXC_S2("AmperageExcitation_s2", " 2-й секции: %.1f А"),
    AMP_EXC1_S1("AmperageExcitation1_s1", " 1  1-й секции: %.1f А"),
    AMP_EXC1_S2("AmperageExcitation1_s2", " 1  2-й секции: %.1f А"),
    AMP_EXC1_S3("AmperageExcitation1_s3", " 1  3-й секции: %.1f А"),
    AMP_EXC1_S4("AmperageExcitation1_s4", " 1  4-й секции: %.1f А"),
    AMP_EXC2_S1("AmperageExcitation2_s1", " 2  1-й секции: %.1f А"),
    AMP_EXC2_S2("AmperageExcitation2_s2", " 2  2-й секции: %.1f А"),
    AMP_EXC2_S3("AmperageExcitation2_s3", " 2  3-й секции: %.1f А"),
    AMP_EXC2_S4("AmperageExcitation2_s4", " 2  4-й секции: %.1f А"),
    AMP_EXC3_S1("AmperageExcitation3_s1", " 3  1-й секции: %.1f А"),
    AMP_EXC3_S2("AmperageExcitation3_s2", " 3  2-й секции: %.1f А"),
    AMP_EXC3_S3("AmperageExcitation3_s3", " 3  3-й секции: %.1f А"),
    AMP_EXC3_S4("AmperageExcitation3_s4", " 3  4-й секции: %.1f А"),
    AMP_EXC4_S1("AmperageExcitation4_s1", " 4  1-й секции: %.1f А"),
    AMP_EXC4_S2("AmperageExcitation4_s2", " 4  2-й секции: %.1f А"),
    AMP_EXC4_S3("AmperageExcitation4_s3", " 4  3-й секции: %.1f А"),
    AMP_EXC4_S4("AmperageExcitation4_s4", " 4  4-й секции: %.1f А"),
    // расход энергии
    CONSUMPTION_EN("ConsumptionEn", " расход: %.0f кВт"),
    CONSUMPTION_EN_S1("ConsumptionEn_s1", " расход 1-й секции: %.0f кВт"),
    CONSUMPTION_EN_S2("ConsumptionEn_s2"," расход 2-й секции: %.0f кВт"),
    CONSUMPTION_EN_S3("ConsumptionEn_s3"," расход 3-й секции: %.0f кВт"),
    CONSUMPTION_EN_S4("ConsumptionEn_s4"," расход 4-й секции: %.0f кВт"),
    CONSUMPTION_R_EN("ConsumptionEn_r", " рекуперация: %.0f кВт"),
    CONSUMPTION_R_EN_S1("ConsumptionEn_r_s1"," рекуперация 1-й секции: %.0f кВт"),
    CONSUMPTION_R_EN_S2("ConsumptionEn_r_s2"," рекуперация 2-й секции: %.0f кВт"),
    CONSUMPTION_R_EN_S3("ConsumptionEn_r_s3"," рекуперация 3-й секции: %.0f кВт"),
    CONSUMPTION_R_EN_S4("ConsumptionEn_r_s4"," рекуперация 4-й секции: %.0f кВт"),
    // сила
    POWER_LOC("PowerLocomotive", " реализованная: %.1f кН "),
    POWER_LOC_SLAVE("PowerLocomotiveSlave", " реализованная 2-го: %.1f кН "),
    POWER_TASK_CONTROL("PowerTaskControl", " задание контроллера: %.1f кН "),
    POWER_MAX("PowerMax", " макс. тяга: %.1f кН "),
    POWER_MAX_REC("PowerMaxRec", " макс. рекуперация: %.1f кН "),
    POWER_TASK_AUTO("PowerTaskAuto", " задание автоведения: %.1f кН "),
    // расстояние до хвоста
    DISTANCE_TAIL("DistanceTail", " расстояние: %.1f км "),
    // давление
    PRESS_UR_USAVP("PressUR_Usavp"," УР (УСАВП): %.1f атм"),
    PRESS_TM_USAVP("PressTM_Usavp"," ТМ (УСАВП): %.1f атм"),
    PRESS_TC_USAVP("PressTC_Usavp"," ТЦ (УСАВП): %.1f атм"),
    PRESS_ZTS_USAVP("PressZTC_Usavp", " ЗТЦ(УСАВП): %.1f атм"),
    PRESS_PM("PressPM", " ПМ        : %.1f атм"),
    PRESS_UR("PressUR", " УР        : %.1f атм"),
    PRESS_TM("PressTM", " ТМ        : %.1f атм"),
    PRESS_TC("PressTC", " ТЦ        : %.1f атм"),
    PRESS_NM("PressNM", " НМ        : %.1f атм"),
    PRESS_UR_S1("PressUR_s1", " УР 1      : %.1f атм"),
    PRESS_TM_S1("PressTM_s1", " ТМ 1      : %.1f атм"),
    PRESS_TC_S1("PressTC_s1"," ТЦ 1      : %.1f атм"),
    PRESS_UR_S2("PressUR_s2"," УР 2      : %.1f атм"),
    PRESS_TM_S2("PressTM_s2"," ТМ 2      : %.1f атм"),
    PRESS_TC_S2("PressTC_s2"," ТЦ 2      : %.1f атм"),
    PRESS_TM_TAIL("PressTmTail"," ТМ хвоста : %.1f атм"),
    PRESS_CAMERA_BHV("PressCameraBhv"," Камера БХВ: %.1f атм"),
    // скорость
    SPEED_MAX("SpeedMax"," пост огр: %.1f км/ч"),
    SPEED_LIM_TMP("SpeedLimitTemp", " врем огр: %.1f км/ч"),
    SPEED_LIM_CUR("SpeedLimitCur", " тек огр:  %.1f км/ч"),
    SPEED_CLUB_LIM("SpeedClubLimit", " КЛУБ огр: %.1f км/ч"),
    SPEED_USAVP("SpeedUSAVP"," УСАВП:    %.1f км/ч"),
    SPEED_SLAVE("SpeedSlave"," Второго:  %.1f км/ч"),
    SPEED_ERG("SpeedErg"," ERG:      %.1f км/ч"),
    SPEED_MSUD("SpeedMSUD"," МСУД:     %.1f км/ч"),
    SPEED_CLUB("SpeedClub"," КЛУБ:     %.1f км/ч"),
    SPEED_BS_DPS("Speed_BS_DPS"," БС-ДПС:   %.1f км/ч"),
    SPEED_BUT("SpeedBut"," БУТ:      %.1f км/ч"),
    SPEED_BLOCK("SpeedBlock"," БЛОК:     %.1f км/ч"),
    SPEED_TASK("SpeedTask"," Задание:  %.1f км/ч"),
    // asim
    SPEED_CALC("SpeedCalc"," Расчет:   %.1f км/ч"),
    SPEED_FACT("SpeedFact"," Факт:     %.1f км/ч"),
    SPEED_GPS("SpeedGps"," GPS:      %.1f км/ч"),
    // позиция, ослабление поля
    POSITION_S5k("Position_s5k"," контроллера: %.0fA"),
    POSITION("Position"," контроллера: %.0f"),
    POSITION_TASK_AUTO("PositionTaskAuto"," задание авт: %.0f"),
    WEAK_FIELD("WeakeningField"," ослабление поля: %.0f"),
    // пофиль
    PROFILE_DIRECT("ProfileDirect","длина:     %.1f м"),
    PROFILE("Profile","tg уклона: %.1f"),
    // карта
    MAP_DIRECT("MapDirect", "map direct: %.1f"),
    MAP_LINE("MapLine", "map line: %.1f");

    private final String name;
    private final String description;

    LineKeys(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return  description;
    }

    public static void setColorVisibleAndStrokeLines(int index, LineKeys key, XYItemRenderer renderer) {

    }
}
