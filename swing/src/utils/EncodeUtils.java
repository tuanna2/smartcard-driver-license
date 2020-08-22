package utils;

public class EncodeUtils {

    public static String hexToString(byte[] ba) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < ba.length; i++) {
            str.append(String.format("%x", ba[i]));
        }
        return str.toString();
    }

    public static String stringToHex(String str) {
        StringBuilder buf = new StringBuilder(200);
        for (char ch : str.toCharArray()) {
            buf.append(String.format("%02x", (int) ch));
        }
        return buf.toString();
    }



    public static byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }
}
