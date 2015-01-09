package org.ajaxanywhere.utils;

public class Hex {

    private static final char[] DIGITS =
            {
                    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
            };

    public static String hex(byte[] data, int length) {
        StringBuffer sb = new StringBuffer(length *2);

        for (int i = 0; i < length; i++) {
            sb.append(DIGITS[(0xF0 & data[i]) >>> 4]);
            sb.append(DIGITS[0x0F & data[i]]);
        }

        return sb.toString();
    }
}
