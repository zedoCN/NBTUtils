package main.Utils;

import java.util.BitSet;

public class BitsUtils {
    /**
     * 格式化打印比特位 11000000 00001000 00000000 00000000 00000000 00000000 00000000 00000000
     * @param bits 要打印的BitSet
     * @return 格式化后的文本
     */
    public static String bits2fStr(BitSet bits) {
        StringBuilder rt = new StringBuilder();
        if (bits.size() == 0) {
            return "00000000 ";
        }
        for (int i = 0; i < ((int) Math.ceil(bits.size() / 8.d) * 8); i++) {
            rt.append(bits.get(i) ? 1 : 0);
            if (i % 8 == 7)
                rt.append(" ");
        }

        return rt.toString();
    }

    /**
     * 格式化打印比特位 1100000000001
     * @param bits 要打印的BitSet
     * @return 格式化后的文本
     */
    public static String bits2fStr2(BitSet bits) {
        StringBuilder rt = new StringBuilder();
        if (bits.length() == 0) {
            return "0";
        }
        for (int i = 0; i < bits.length(); i++) {
            rt.append(bits.get(i) ? 1 : 0);
        }

        return rt.toString();
    }


}
