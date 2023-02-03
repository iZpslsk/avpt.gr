package avpt.gr.blocks32.overall;

import static avpt.gr.common.UtilsArmG.toUnsignedInt;

/**
 * Обмен с автодиспетчером (расписание). ID = 7
 */
public class Block32_21_7 {

    private byte[] values;

    public Block32_21_7(byte[] values) {
        this.values = values;
    }

    /**
     * @return id подтипа для блока 0x21
     */
    public int getSubId() {
        return (toUnsignedInt(values[27]) & 0x60) >>> 3 |    // hi
                (toUnsignedInt(values[28]) & 0x60) >>> 5;    // lo
    }

    /**
     * @return готов - Признак готовности УСАВП к приёму данных
     */
    public int getReady() {
        return (toUnsignedInt(values[0])  >>> 1) & 0x01;
    }

    /**
     * 0 – сообщений нет, 1 – сообщения от модема поступают в БС.
     * @return CAN2 - обмен по CAN2
     */
    public int getCAN2() {
        return toUnsignedInt(values[0]) & 0x01;
    }

    /**
     * Номер локомотива – 14 бит Серия локомотива – 10 бит (данные получаемые от модема)
     * Серия локомотива	Код серии
     * 	Для БМС	Для СВЛ ТР
     * ВЛ-10	123 dec	32 dec
     * ВЛ-10У	138 dec	996 dec
     * ВЛ-80С	240 dec	51 dec
     * 3ЭС5К	253 dec
     * @return серия локомотива
     */
    public int getTypeLoc() {
        return  (toUnsignedInt(values[2]) & 0x03) << 8 |     // hi
                toUnsignedInt(values[1]);                    // lo
    }

    /**
     * @return номер локомотива
     */
    public int getNumLoc() {
        return  (toUnsignedInt(values[2]) & 0xFC) << 6 |     // hi
                toUnsignedInt(values[3]);                    // lo
    }

    /**
     * Табельный номер машиниста – макс. 24 бита (данные из сообщения ASOUP_DRIVER от модема).
     * @return табельный номер
     */
    public int getNumTab() {
        return  toUnsignedInt(values[18]) << 16 | // hi
                toUnsignedInt(values[5]) << 8 |
                toUnsignedInt(values[4]);        // lo
    }

    /**
     * Номер поезда – 16 бит (номер поезда, введенный машинистом, данные из сообщения USAVP_TRAIN для модема).
     * @return номер поезда от машиниста
     */
    public int getNumTrain() {
        return  toUnsignedInt(values[7]) << 8 |  // hi
                toUnsignedInt(values[6]);        // lo
    }

    /**
     * ЕСР-код станции проследования – 24 бита
     * Уникальный шестизначный ЕСР-код станции проследования
     * (берется из ББД, значение делится на 100, сообщение USAVP_ARRVL_A от УСАВП для Модема)
     * @return ЕСР-код станции проследования
     */
    public int getECPStation() {
        return  toUnsignedInt(values[10]) << 16 | // hi
                toUnsignedInt(values[9]) << 8 |
                toUnsignedInt(values[8]);         // lo
    }

    /**
     * №  последнего принятого расписания – номер последнего принятого расписания с верной структурой
     * (0x360 USAVP_READY_A 1byte)
     * (Равен нулю, когда используется расписание из ББД. Если система не активирована,
     * а расписание от сервера получено, то здесь  сохранится его номер)
     * @return №  последнего принятого расписания
     */
    public int getNumLastAcceptSchedule() {
        return toUnsignedInt(values[11]);
    }

    /**
     * Номер поезда от СИМ – 16 бит (номер поезда, полученный от модема, SOUP_TRAIN_INFO1).
     * @return номер поезда от сим
     */
    public int getNumTrainSim() {
        return  toUnsignedInt(values[13]) << 8 |  // hi
                toUnsignedInt(values[12]);        // lo
    }

    /**
     * GSM сигнал – модем СВЛ ТР не дает такую информацию:
     * равно 255 если в работе модема не зарегистрированы ошибки, иначе возвращает 0;
     * у модема БМС данные об уровне  GSM сигнала есть:
     * 6 бит: Уровень GSM сигнала;
     * 7 бит:  - 1 -Модем отправляет накопленную диагностическую информацию 0 – не отправляет
     * ( на 3es5k до версии 5.3.1.84 уровень сигнала gsm не сохранялся)
     * @return Уровень GSM сигнала
     */
    public int getLevelGSM() {
        return Math.min(toUnsignedInt(values[14]) & 0x7F, 100);
    }

    /**
     * Статус последнего принятого расписания – код обработки расписания с номером = №  последнего принятого расписания.
     * @return Статус последнего принятого расписания
     */
    public int getStatusLastSchedule() {
        return toUnsignedInt(values[15]);
    }

    /**
     * №  последнего принятого и верного расписания –
     * Уникальный  ID номер расписания на данную поездку с верной структурой данных  (0x361 USAVP_RES_A 1byte)
     * @return №  последнего принятого и верного расписания
     */
    public int getNumLastAcceptValidSchedule() {
        return toUnsignedInt(values[16]);
    }

    /**
     * № загруженного расписания – если источник расписания ББД вернет 0,
     * иначе вернет номер расписания, по которому будет контролироваться поездка в Автоведении поезда.
     * @return № загруженного расписания
     */
    public int getNumLoadSchedule() {
        return toUnsignedInt(values[17]);
    }

    /**
     * Режим работы БМС – 1 бит- Режим работы библиотеки: 0-авто, 1-ручной (f_mode)
     * @return Режим работы
     */
    public int getModeWork() {
        return toUnsignedInt(values[19]) >>> 7;
    }

    /**
     * Последняя выполненная команда БМС – 7бит - Команда БМС
     * @return Последняя выполненная команда БМС
     */
    public int getLastCommandBMS() {
        return toUnsignedInt(values[19]) & 0x7F;
    }

    /**
     * Номер задачи выполняемой библ. БМС – 4бита -Задача, выполняемая библиотекой БМС (g_ldr_proc_info.step)
     * @return Номер задачи выполняемой библ. БМС
     */
    public int getNumTaskBMS() {
        return toUnsignedInt(values[20]) & 0x0F;
    }

    /**
     * Статус выдачи расп. движ – 7бит  -(DISP_SCHED_COMM_STATES_A 1byte)
     * 1 - расп. нет, м. нет
     * 5 – расп. не прин, неверн. вр.
     * 18 – Нет ст. (не найдены станции),
     * 19 – ст. не послед.,
     * 21 – расп. прин., ном. Расп. =0
     * 22 –  против. н. (противопол. Направл.поездки)
     * 23 – ожид. загрузка. (ожидается загрузка)
     * 28 – использ. отлож. Из-за Маневр. (ожидается загрузка после откл. Маневр.)
     * 29 – СИМ не исп., м. нет, расп. прин.,
     * 30 - СИМ не исп., м. нет, расп. нет,
     * 31– СИМ не исп., м. нет, расп. прин., ст. нет
     * 32 - СИМ не исп., м. нет, расп. прин., неверное вр.
     * 33 – расп. прин, на ведом. Не испол.
     * @return Статус выдачи расп. движ.
     */
    public int getStatusSchedule() {
        return  (toUnsignedInt(values[0]) & 0x1C) << 2 |  // hi
                toUnsignedInt(values[20]) >>> 4;        // lo
    }

    /**
     * Ж/д коорд КЛУБ-У начала занятого блок-участка – 24 бит
     * @return Ж/д коорд КЛУБ-У начала занятого блок-участка
     */
    public int getStartCoordinateKLUB_U() {
        return  toUnsignedInt(values[23]) << 16 | // hi
                toUnsignedInt(values[22]) << 8 |
                toUnsignedInt(values[21]);         // lo
    }

    /**
     * @return Длина занятого участка
     */
    public int getLenKLUB_U() {
        return  toUnsignedInt(values[25]) << 16 |  // hi
                toUnsignedInt(values[24]);         // lo
    }

    /**
     * @return Фактическая скорость впередиидущего поезда
     */
    public int getSpeedForwardTrain() {
        return toUnsignedInt(values[26]);
    }

    /**
     * Ошибки в работе модема - 5бит– значение кода ошибки в работе модема из функции Bool isModemOk(BYTE* error).
     * enum errorInModem
     * {
     * 	EM_NO_LINK_WITH_MODEM  = 1,		//нет  сообщений от модема
     * 	EM_NO_LINK_WITH_USAVP = 2,		//в модеме нет  сообщений от УСАВП
     * 	EM_NO_LINK_WITH_SERVER = 3,		//в модеме нет связи с сервером
     * 	EM_NO_SIM = 4,				//в модме нет симкарты  - только мпдн
     * 	EM_NOT_REGISTERED = 5,		//модем не зарегистрирован - только бмс
     * 	EM_NOT_WORK = 6,			//модем не работает - только бмс
     * 	EM_NOT_LINK_WITH_NETWORK_GATEWAY = 7,    //нет связи со шлюзом - только бмс
     * 	EM_OLD_SOFT_WARE = 8,		//только мпдн - эта версия ПО для БС работает с ПО МПДН  версии не ниже  5.8(добавилась передача состава поезда)
     * 	EM_MODEM_LOAD_PO_FROM_SERVER = 9,//модем качает ПО с сервера
     * 	EM_RESET = 10,				//только БМС
     * 	EM_BAD_TIME_IN_BR = 11,		//не верное время в БР(КЛУБ)
     * 	EM_NOT_WORK_WAIT = 12,
     * 	EM_TIKS_GPRS_AVAILABLE = 13		//Не активирована GPRS услуга связи
     * };
     * @return Ошибки в работе модема
     */
    public int getErrModem() {
        return toUnsignedInt(values[27]) & 0x1F;
    }

    /**
     * @return Статус прохождения теста
     */
    public int getStatusTest() {
        return toUnsignedInt(values[28]) & 0x1F;
    }

    /**
     * ИР - источник расписания – варианты:
     * 1- ББД,
     * 2 - модем,
     * 3 -восстановлено из файла ( для версии БС ниже 5.3.1.78: 0 - ББД, 1 - модем, 2 -восстановлено из файла)
     * 1- ББД, 2 – М (модем), 3 – Ф (восстановлено из файла)
     * @return ИР - источник расписания
     */
    public int getSourceSchedule() {
        return  (toUnsignedInt(values[28]) & 0x80) >>> 6 |   // hi
                toUnsignedInt(values[27]) >>> 7;             // lo
    }
}
