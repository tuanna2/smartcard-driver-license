package smrtdrv;

import javacard.framework.*;

public class LicenseApplet extends Applet {
  /**
   * INS LIST
   */

  static final byte INS_READ_BASIC_INFO = 0x00;

  static final byte INS_READ_FAULT_INFO = 0x01;

  static final byte INS_READ_AVATAR_IMAGE = 0x02;

  static final byte INS_WRITE_ALL_BASIC_INFO = 0x10;

  static final byte INS_REWRITE_CARD_ID = 0x11;

  static final byte INS_REWRITE_FULLNAME = 0x12;

  static final byte INS_REWRITE_BIRTH_DATE = 0x13;

  static final byte INS_REWRITE_HOMETOWN = 0x14;

  static final byte INS_REWRITE_GRANTED_DATE = 0x15;

  static final byte INS_REWRITE_EXPIRE_DATE = 0x16;

  static final byte INS_REWRITE_CARD_TYPE = 0x17;

  static final byte INS_REWRITE_AVATAR_IMAGE = 0x18;

  static final byte INS_WRITE_NEW_FAULT = 0x19;

  static final byte INS_CLEAR_RECENT_FAULT = 0x20;

  static final short CARD_ID_LENGTH = 0x08;

  static final short DATE_FORMAT_LENGTH = 0x04;

  static final byte EMPTY_VALUE = 0x00;

  static final int MAX_IMAGE_SIZE = 8192;
  /**
   * CARD TYPE
   */

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

  /**
   * PROPERTIES LIST
   */

  private static byte cardType;

  private static byte[] cardId;

  private static byte[] fullName;

  private static byte[] birthDate;

  private static byte[] hometown;

  private static byte[] grantedDate;

  private static byte[] expireDate;

  private static byte[] avatarImage;

  private static short cardIdLen, fullNameLen, birthDateLen, hometownLen, grantedDateLen, expireDateLen, avatarImageLen;

  /**
   * Fault List
   */
  static final byte MISSING_EQUIPMENT_FAULT = 0x01;

  static final byte LIGHT_SIGNAL_FAULT = 0x02;

  static final byte OVER_WEIGHT_FAULT = 0x03;

  static final byte WRONG_LANE_FAULT = 0x04;

  /**
   * Card State
   */
  private boolean isValid = true;

  private byte[] recentFaults = new byte[] {
    EMPTY_VALUE,
    0x00,
    0x00,
    0x00,
    0x00,
    EMPTY_VALUE,
    0x00,
    0x00,
    0x00,
    0x00,
    EMPTY_VALUE,
    0x00,
    0x00,
    0x00,
    0x00,
    EMPTY_VALUE,
    0x00,
    0x00,
    0x00,
    0x00,
    EMPTY_VALUE,
    0x00,
    0x00,
    0x00,
    0x00,
  };

  /**
   * Algo Implementation
   */

  public LicenseApplet() {
    /**
     * Init properties
     */
  }

  public static void install(byte[] bArray, short bOffset, byte bLength) {
    new LicenseApplet()
    .register(bArray, (short) (bOffset + 1), bArray[bOffset]);

    //
    cardType = CARD_TYPE_A1;
    cardId = new byte[CARD_ID_LENGTH];
    fullName = new byte[64];
    birthDate = new byte[DATE_FORMAT_LENGTH];
    hometown = new byte[64];
    grantedDate = new byte[DATE_FORMAT_LENGTH];
    expireDate = new byte[DATE_FORMAT_LENGTH];
    avatarImage = new byte[MAX_IMAGE_SIZE];

    /**
     * Default
     */
    byte[] tempCardId = new byte[] {
      0x00,
      0x01,
      0x02,
      0x03,
      0x04,
      0x05,
      0x06,
      0x07,
    };
    byte[] tempFullName = new byte[] {
      'N',
      'G',
      'U',
      'Y',
      'E',
      'N',
      ' ',
      'T',
      'I',
      'E',
      'N',
      ' ',
      'D',
      'U',
      'N',
      'G',
    };
    byte[] tempHometown = new byte[] {
      '1',
      '2',
      '1',
      ' ',
      'T',
      'R',
      'A',
      'N',
      ' ',
      'P',
      'H',
      'U',
      ' ',
      'H',
      'A',
      ' ',
      'D',
      'O',
      'N',
      'G',
    };
    byte[] tempBirthDate = new byte[] { 0x01, 0x06, 0x14, 0x14 };
    byte[] tempGrantedDate = new byte[] { 0x01, 0x0A, 0x14, 0x14 };
    byte[] tempExpireDate = new byte[] { 0x01, 0x0C, 0x14, 0x14 };

    //
    Util.arrayCopy(
      tempCardId,
      (short) 0,
      cardId,
      (short) 0,
      (short) tempCardId.length
    );
    Util.arrayCopy(
      tempFullName,
      (short) 0,
      fullName,
      (short) 0,
      (short) tempFullName.length
    );
    Util.arrayCopy(
      tempBirthDate,
      (short) 0,
      birthDate,
      (short) 0,
      (short) tempBirthDate.length
    );
    Util.arrayCopy(
      tempHometown,
      (short) 0,
      hometown,
      (short) 0,
      (short) tempHometown.length
    );
    Util.arrayCopy(
      tempGrantedDate,
      (short) 0,
      grantedDate,
      (short) 0,
      (short) tempGrantedDate.length
    );
    Util.arrayCopy(
      tempExpireDate,
      (short) 0,
      expireDate,
      (short) 0,
      (short) tempExpireDate.length
    );

    cardIdLen = (short) tempCardId.length;
    fullNameLen = (short) tempFullName.length;
    birthDateLen = (short) tempBirthDate.length;
    hometownLen = (short) tempHometown.length;
    grantedDateLen = (short) tempGrantedDate.length;
    expireDateLen = (short) tempExpireDate.length;
  }

  public void process(APDU apdu) {
    if (selectingApplet()) {
      return;
    }

    byte[] buf = apdu.getBuffer();
    short len = apdu.setIncomingAndReceive();
    switch (buf[ISO7816.OFFSET_INS]) {
      case INS_READ_BASIC_INFO:
        readBasicInfo(apdu);
        break;
      case INS_READ_FAULT_INFO:
        readRecentFault(apdu);
        break;
      case INS_READ_AVATAR_IMAGE:
        readAvatarImage(apdu);
        break;
      case INS_WRITE_ALL_BASIC_INFO:
        writeAllBasicInfo(apdu);
        break;
      case INS_REWRITE_CARD_ID:
        rewriteCardId(apdu);
        break;
      case INS_REWRITE_CARD_TYPE:
        rewriteCardType(apdu);
        break;
      case INS_REWRITE_FULLNAME:
        rewriteFullName(apdu, len);
        break;
      case INS_REWRITE_BIRTH_DATE:
        rewriteBirthDate(apdu);
        break;
      case INS_REWRITE_HOMETOWN:
        rewriteHometown(apdu, len);
        break;
      case INS_REWRITE_GRANTED_DATE:
        rewriteGrantedDate(apdu);
        break;
      case INS_REWRITE_EXPIRE_DATE:
        rewriteExpireDate(apdu);
        break;
      case INS_REWRITE_AVATAR_IMAGE:
        rewriteAvatarImage(apdu, len);
        break;
      case INS_WRITE_NEW_FAULT:
        writeNewFault(apdu);
        break;
      case INS_CLEAR_RECENT_FAULT:
        clearRecentFault();
        break;
      default:
        ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
    }
  }

  private void readBasicInfo(APDU apdu) {
    byte[] buf = apdu.getBuffer();
    short sendLen;
    sendLen =
      (short) (
        (short) (2) +
        (short) (cardIdLen) +
        (short) (fullNameLen) +
        (short) (hometownLen) +
        (short) (birthDateLen) +
        (short) (grantedDateLen) +
        (short) (expireDateLen) +
        (short) 0x01
      );
    apdu.setOutgoing();
    apdu.setOutgoingLength((short) sendLen);
    apdu.sendBytesLong(cardId, (short) 0, (short) cardIdLen);
    buf[0] = (byte) fullNameLen;
    apdu.sendBytes((short) 0, (short) 1);
    apdu.sendBytesLong(fullName, (short) 0, (short) fullNameLen);
    buf[0] = (byte) hometownLen;
    apdu.sendBytes((short) 0, (short) 1);
    apdu.sendBytesLong(hometown, (short) 0, (short) hometownLen);
    apdu.sendBytesLong(birthDate, (short) 0, (short) birthDateLen);
    apdu.sendBytesLong(grantedDate, (short) 0, (short) grantedDateLen);
    apdu.sendBytesLong(expireDate, (short) 0, (short) expireDateLen);
    buf[0] = cardType;
    apdu.sendBytes((short) 0, (short) 1);
  }

  private void readRecentFault(APDU apdu) {
    byte[] buf = apdu.getBuffer();
    apdu.setOutgoing();
    apdu.setOutgoingLength((short) recentFaults.length);

    apdu.sendBytesLong(recentFaults, (short) 0, (short) recentFaults.length);
  }

  private void readAvatarImage(APDU apdu) {
    byte[] buf = apdu.getBuffer();
    apdu.setOutgoing();
    apdu.setOutgoingLength((short) avatarImageLen);

    apdu.sendBytesLong(avatarImage, (short) 0, (short) avatarImageLen);
  }

  private void writeAllBasicInfo(APDU apdu) {
    JCSystem.beginTransaction();

    JCSystem.commitTransaction();
  }

  private void rewriteCardId(APDU apdu) {
    byte[] buf = apdu.getBuffer();
    Util.arrayCopy(
      buf,
      (short) ISO7816.OFFSET_CDATA,
      cardId,
      (short) 0,
      (short) CARD_ID_LENGTH
    );
  }

  private void rewriteCardType(APDU apdu) {
    byte[] buf = apdu.getBuffer();
    cardType = buf[ISO7816.OFFSET_P1];
  }

  private void rewriteFullName(APDU apdu, short len) {
    byte[] buf = apdu.getBuffer();
    Util.arrayCopy(buf, (short) ISO7816.OFFSET_CDATA, fullName, (short) 0, len);
    fullNameLen = len;
  }

  private void rewriteBirthDate(APDU apdu) {
    byte[] buf = apdu.getBuffer();
    Util.arrayCopy(
      buf,
      (short) ISO7816.OFFSET_CDATA,
      birthDate,
      (short) 0,
      (short) DATE_FORMAT_LENGTH
    );
    birthDateLen = DATE_FORMAT_LENGTH;
  }

  private void rewriteHometown(APDU apdu, short len) {
    byte[] buf = apdu.getBuffer();
    Util.arrayCopy(buf, (short) ISO7816.OFFSET_CDATA, hometown, (short) 0, len);
    hometownLen = len;
  }

  private void rewriteGrantedDate(APDU apdu) {
    byte[] buf = apdu.getBuffer();
    Util.arrayCopy(
      buf,
      (short) ISO7816.OFFSET_CDATA,
      grantedDate,
      (short) 0,
      (short) DATE_FORMAT_LENGTH
    );
    grantedDateLen = DATE_FORMAT_LENGTH;
  }

  private void rewriteExpireDate(APDU apdu) {
    byte[] buf = apdu.getBuffer();
    Util.arrayCopy(
      buf,
      (short) ISO7816.OFFSET_CDATA,
      expireDate,
      (short) 0,
      (short) DATE_FORMAT_LENGTH
    );
    expireDateLen = DATE_FORMAT_LENGTH;
  }

  private void writeNewFault(APDU apdu) {
    byte[] buf = apdu.getBuffer();
    Util.arrayCopy(
      recentFaults,
      (short) 0,
      recentFaults,
      (short) 5,
      (short) 20
    );
    Util.arrayCopy(
      buf,
      (short) ISO7816.OFFSET_CDATA,
      recentFaults,
      (short) 0,
      (short) 5
    );
  }

  private void rewriteAvatarImage(APDU apdu, short len) {
    byte[] buf = apdu.getBuffer();
    Util.arrayCopy(
      buf,
      (short) ISO7816.OFFSET_CDATA,
      avatarImage,
      (short) 0,
      (short) len
    );
    avatarImageLen = (short) len;

    apdu.setOutgoing();
    apdu.setOutgoingLength((short) avatarImageLen);
    buf[0] = (byte) len;

    apdu.sendBytes((short) 0, (short) 1);
  }

  private void clearRecentFault() {
    Util.arrayFillNonAtomic(recentFaults, (short) 0, (short) 8192, (byte) 0);
    avatarImageLen = 0;
  }
}
