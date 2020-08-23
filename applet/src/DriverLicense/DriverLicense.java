package DriverLicense;

import javacard.framework.*;
import javacard.security.KeyBuilder;
import javacard.security.*;
import javacardx.crypto.*;

public class DriverLicense extends Applet {

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

  static final short CARD_ID_LENGTH = 0x08;
  static final short DATE_FORMAT_LENGTH = 0x04;
  static final int MAX_IMAGE_SIZE = 30000;

  private static byte cardType;

  private static byte[] cardId, fullName, birthDate, address, releaseDate, expireDate, avatarImage;

  private static short cardIdLen, fullNameLen, birthDateLen, addressLen, releaseDateLen, expireDateLen, avatarImageLen;

  private byte[] listViolation = new byte[] { 
    0x00, 0x00, 0x00, 0x00, 0x00,
    0x00, 0x00, 0x00, 0x00, 0x00,
    0x00, 0x00, 0x00, 0x00, 0x00, 
    0x00, 0x00, 0x00, 0x00, 0x00,
    0x00, 0x00, 0x00, 0x00, 0x00,
  };
  public static void install(byte[] bArray, short bOffset, byte bLength) {
    new DriverLicense()
    .register(bArray, (short) (bOffset + 1), bArray[bOffset]);

    cardType = 0x00;
    cardId = new byte[CARD_ID_LENGTH];
    fullName = new byte[50];
    birthDate = new byte[DATE_FORMAT_LENGTH];
    address = new byte[100];
    releaseDate = new byte[DATE_FORMAT_LENGTH];
    expireDate = new byte[DATE_FORMAT_LENGTH];
    avatarImage = new byte[MAX_IMAGE_SIZE];

    cardIdLen = CARD_ID_LENGTH;
    fullNameLen = (short) fullName.length;
    birthDateLen = (short) birthDate.length;
    addressLen = (short) address.length;
    releaseDateLen = (short) releaseDate.length;
    expireDateLen = (short) expireDate.length;
 }

  public void process(APDU apdu) {
    if (selectingApplet()) {
      return;
    }

    byte[] buf = apdu.getBuffer();
    short len = apdu.setIncomingAndReceive();
    switch (buf[ISO7816.OFFSET_INS]) {
      case INS_GET_USER_INFO:
        short lengthDataSend = (short) ((short) (2) + (short) (cardIdLen) + (short) (fullNameLen) + (short) (addressLen) +
          (short) (birthDateLen) + (short) (releaseDateLen) + (short) (expireDateLen) + (short) 0x01);
        apdu.setOutgoing();
        apdu.setOutgoingLength((short) lengthDataSend);
        apdu.sendBytesLong(cardId, (short) 0, (short) cardIdLen);
        buf[0] = (byte) fullNameLen;
        apdu.sendBytes((short) 0, (short) 1);
        apdu.sendBytesLong(fullName, (short) 0, (short) fullNameLen);
        buf[0] = (byte) addressLen;
        apdu.sendBytes((short) 0, (short) 1);
        apdu.sendBytesLong(address, (short) 0, (short) addressLen);
        apdu.sendBytesLong(birthDate, (short) 0, (short) birthDateLen);
        apdu.sendBytesLong(releaseDate, (short) 0, (short) releaseDateLen);
        apdu.sendBytesLong(expireDate, (short) 0, (short) expireDateLen);
        buf[0] = cardType;
        apdu.sendBytes((short) 0, (short) 1);
        break;
      case INS_GET_LIST_VIOLATION:
        apdu.setOutgoing();
        apdu.setOutgoingLength((short) listViolation.length);
        apdu.sendBytesLong(listViolation, (short)0, (short)listViolation.length);
        break;
      case INS_GET_AVATAR_IMAGE:
        apdu.setOutgoing();
        apdu.setOutgoingLength((short)avatarImageLen);
        apdu.sendBytesLong(avatarImage, (short) 0, (short)avatarImageLen);
        break;
      case INS_SET_CARD_ID:
        Util.arrayCopy(buf, (short)ISO7816.OFFSET_CDATA, cardId, (short)0, (short) CARD_ID_LENGTH);
        break;
      case INS_SET_CARD_TYPE:
        cardType = buf[ISO7816.OFFSET_P1];
        break;
      case INS_SET_FULLNAME:
        Util.arrayCopy(buf, (short) ISO7816.OFFSET_CDATA, fullName, (short) 0, len);
        fullNameLen = len;
        break;
      case INS_SET_BIRTH_DATE:
        Util.arrayCopy(buf, (short)ISO7816.OFFSET_CDATA, birthDate, (short)0, (short)DATE_FORMAT_LENGTH);
        birthDateLen = DATE_FORMAT_LENGTH;
        break;
      case INS_SET_ADDRESS:
        Util.arrayCopy(buf, (short)ISO7816.OFFSET_CDATA, address, (short)0, len);
        addressLen = len;
        break;
      case INS_SET_RELEASE_DATE:
        Util.arrayCopy(buf, (short) ISO7816.OFFSET_CDATA, releaseDate, (short)0, (short) DATE_FORMAT_LENGTH);
        releaseDateLen = DATE_FORMAT_LENGTH;
        break;
      case INS_SET_EXPIRE_DATE:
        Util.arrayCopy(buf, (short)ISO7816.OFFSET_CDATA, expireDate, (short)0, (short)DATE_FORMAT_LENGTH);
        expireDateLen = DATE_FORMAT_LENGTH;
        break;
      case INS_SET_AVATAR_IMAGE:
        Util.arrayCopy(buf, (short) ISO7816.OFFSET_CDATA, avatarImage, (short)0, (short)len);
        avatarImageLen = (short)len;
        apdu.setOutgoing();
        apdu.setOutgoingLength((short) avatarImageLen);
        buf[0] = (byte) len;
        apdu.sendBytes((short)0, (short)1);
        break;
      case INS_INSERT_VIOLATION:
          Util.arrayCopy(listViolation, (short) 0, listViolation, (short)5, (short) 20);
          Util.arrayCopy(buf, (short) ISO7816.OFFSET_CDATA, listViolation, (short)0, (short)5);
        break;
      case INS_CLEAR_LIST_VIOLATION:
        Util.arrayFillNonAtomic(listViolation, (short)0, (short)8192, (byte)0);
        avatarImageLen = 0;
        break;
      default:
        ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
    }
  }
}
