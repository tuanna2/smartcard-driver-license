package utils;

public class LicenseUtils {

    static final byte CARD_TYPE_A1 = 0x01;
    static final byte CARD_TYPE_A2 = 0x02;
    static final byte CARD_TYPE_A3 = 0x03;
    static final byte CARD_TYPE_A4 = 0x04;
    static final byte CARD_TYPE_B1 = 0x11;
    static final byte CARD_TYPE_B2 = 0x12;
    static final byte CARD_TYPE_C = 0x21;
    static final byte CARD_TYPE_D = 0x31;
    static final byte CARD_TYPE_E = 0x41;
    static final byte CARD_TYPE_F = 0x51;
    static final byte CARD_TYPE_FB2 = 0x52;
    static final byte CARD_TYPE_FC = 0x53;
    static final byte CARD_TYPE_FD = 0x54;
    static final byte CARD_TYPE_FE = 0x55;

    public static String cardType(int cardType) {
        switch (cardType) {
            case CARD_TYPE_A1:
                return "A1";
            case CARD_TYPE_A2:
                return "A2";
            case CARD_TYPE_A3:
                return "A3";
            case CARD_TYPE_A4:
                return "A4";
            case CARD_TYPE_B1:
                return "B1";
            case CARD_TYPE_B2:
                return "B2";
            case CARD_TYPE_C:
                return "C";
            case CARD_TYPE_D:
                return "D";
            case CARD_TYPE_E:
                return "E";
            case CARD_TYPE_F:
                return "F";
            case CARD_TYPE_FB2:
                return "FB2";
            case CARD_TYPE_FC:
                return "FC";
            case CARD_TYPE_FD:
                return "FD";
            case CARD_TYPE_FE:
                return "FE";

            default:
                return "Unkown type!";
        }
    }
}
