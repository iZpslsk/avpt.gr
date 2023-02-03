package avpt.gr.blocks32.asim;

import java.util.Arrays;

import static avpt.gr.common.UtilsArmG.windows1251;

/**
 * диагностическое сообщение
 */
public class Block32_C7 {

    private final byte[] values;

    public Block32_C7(byte[] values) {
        this.values = values;
    }

    /**
     * @return текст сообщения
     */
    public String getTextMessage() {
        byte[] bytes = Arrays.copyOfRange(values, 0, 28);
        return new String(bytes, windows1251).trim();
    }
}
