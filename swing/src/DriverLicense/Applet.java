package DriverLicense;

public interface Applet {

    static final String APPLET_AID = "001122334400";

    static final short CARD_ID_LENGTH = 0x08;
    static final short DATE_FORMAT_LENGTH = 0x04;
    static final int MAX_IMAGE_SIZE = 8192;

    static final byte INS_GET_USER_INFO = 0x00;
    static final byte INS_GET_LIST_VIOLATION = 0x01;
    static final byte INS_GET_AVATAR_IMAGE = 0x02;
    
    static final byte INS_SET_CARD_ID = 0x11;
    static final byte INS_SET_FULLNAME = 0x12;
    static final byte INS_SET_BIRTH_DATE = 0x13;
    static final byte INS_SET_ADDRESS = 0x14;
    static final byte INS_SET_RELEASE_DATE = 0x15;
    static final byte INS_SET_EXPIRE_DATE = 0x16;
    static final byte INS_SET_CARD_TYPE = 0x17;
    static final byte INS_SET_AVATAR_IMAGE = 0x18;
    static final byte INS_INSERT_VIOLATION = 0x19;
    static final byte INS_CLEAR_LIST_VIOLATION = 0x20;

}
