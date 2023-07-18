package avpt.gr.dialogs;

import javax.swing.*;

public class HelpDialog {

    // аттрибуты шрифта правой части
    private static final String fontAttr = " font face = \"monospace\" size=\"4\" color=#D2691E";
    // цвет заголовков
    private static final String colorH = "#1f618d";

    private static final String helpContent = String.format(
            "<html> " +
                    "<ul>" +

                    "<h3> <font color=%2$s> <u>Масштаб:</u> </h3>" +
                    "<li>" +
                    "<i>изменение относительно курсора</i> - " +
                    "<font %1$s>[CTRL]+[<font size=\"5\">+</font>], [CTRL]+[<font size=\"5\">-</font>]</font>" +
                    "</li>" +
                    "<li>" +
                    "<i>изменение относительно курсора мыши</i> - " +
                    "<font %1$s> колесо мыши </font>" +
                    "</li>" +
                    "<li>" +
                    "<i>изменение высоты блока под курсором мыши</i> - " +
                    "<font %1$s> [CTRL]+колесо мыши </font>" +
                    "</li>" +
                    "<li>" +
                    "<i>изменение размера шкалы блока под курсором мыши</i> - " +
                    "<font %1$s>[ALT]+колесо мыши </font>" +
                    "</li>" +

                    "<h3><font color=%2$s> <u>Перемещение курсора: </u></h3>" +
                    "<li>" +
                    "<i>шаг</i> - " +
                    "<font %1$s>[<font size=\"5\">←</font>], [<font size=\"5\">→</font>]</font>" +
                    "</li>" +
                    "<li>" +
                    "<i>широкий шаг</i> - " +
                    "<font %1$s>[<font size=\"5\">←</font>]+[CTRL], [<font size=\"5\">→</font>]+[CTRL]</font>" +
                    "</li>" +
                    "<li>" +
                    "<i>в конец, в начало</i> - " +
                    "<font %1$s>[HOME], [END]</font>" +
                    "</li>" +
                    "<li>" +
                    "<i>на позицию ближайшего дискретного сигнала</i> - " +
                    "<font %1$s>Double click по названию дискретного сигнала на инфо-панели</font>" +
                    "</li>" +
                    "<li>" +
                    "<i>на позицию ближайшей смены (АЛСН, ККМ, Автоведение и т.п.)</i> - " +
                    "<font %1$s>Double click по названию на инфо-панели</font>" +
                    "</li>" +

                    "<h3><font color=%2$s> <u>Сдвиг канвы: </u></h3>" +
                    "<li>" +
                    "<i>основного окна</i> - " +
                    "<font %1$s>Захват левой кнопкой мыши или скроллбар внизу</font>" +
                    "</li>" +
                    "<li>" +
                    "<i>окна инфо-панели</i> - " +
                    "<font %1$s>Захват левой кнопкой мыши или скроллбар справа</font>" +
                    "</li>" +

                    "<h3><font color=%2$s> <u>Переключение видимости: (для линейных графиков)</u> </h3>" +
                    "<li>" +
                    "<i>свернуть/развернуть блок</i> - " +
                    "<font %1$s>Double click на названии блока на инфо-панели</font>" +
                    "</li>" +
                    "<li>" +
                    "<i>одной линии</i> - " +
                    "<font %1$s>Double click на названии линии на инфо-панели</font>" +
                    "</li>" +
//                    "<li>" +
//                    "<i> всех линий блока </i> - " +
//                    "<font %1$s>Double click+[CTRL] на названии линии на инфо-панели </font>" +
//                    "</li>" +
//                    "<li>" +
//                    "<i> всех линий блока одной секции</i> - " +
//                    "<font %1$s>Double click+[CTRL]+[SHIFT] на названии линии на инфо-панели </font>" +
//                    "</li>" +

//                    "<h3><font color=%2$s> <u>Отчеты: </u></h3>" +
//                    "<li>" +
//                    "<i>по поезду</i> - " +
//                    "<font %1$s>Right click на канве соответствующей поездки</font>" +
//                    "</li>" +

                    "</ul>" +
                    "</html>", fontAttr, colorH );

    public static void show(JFrame owner) {
        Object[] options = {"Закрыть"};
        JOptionPane.showOptionDialog(owner, helpContent, null,
            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
    }

}
