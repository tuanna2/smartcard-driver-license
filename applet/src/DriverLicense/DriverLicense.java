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

  static final byte INS_INIT_PIN = 0x21;
  static final byte INS_VERIFY_PIN = 0x22;

  private Key tempDesKey3;
  private byte desKeyLen;
  private byte[] desKey;
  private byte[] desICV;
  private Cipher desEcbCipher;
  private Cipher desCbcCipher;

  static final short CARD_ID_LENGTH = 0x08;
  static final short DATE_FORMAT_LENGTH = 0x04;
  private static short MAX_IMAGE_SIZE;

  private static byte cardType;

  private static byte[] pin, cardId, fullName, birthDate, address, releaseDate, expireDate, avatarImage;

  private static short pinLen, cardIdLen, fullNameLen, birthDateLen, addressLen, releaseDateLen, expireDateLen, avatarImageLen;

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

    pin = new byte[4];
    cardType = 0x00;
    cardId = new byte[CARD_ID_LENGTH];
    fullName = new byte[50];
    birthDate = new byte[DATE_FORMAT_LENGTH];
    address = new byte[100];
    releaseDate = new byte[DATE_FORMAT_LENGTH];
    expireDate = new byte[DATE_FORMAT_LENGTH];
    avatarImage = new byte[254];

    pinIdLen = (short) pin.length;
    cardIdLen = CARD_ID_LENGTH;
    fullNameLen = (short) fullName.length;
    birthDateLen = (short) birthDate.length;
    addressLen = (short) address.length;
    releaseDateLen = (short) releaseDate.length;
    expireDateLen = (short) expireDate.length;
    avatarImageLen = (short) avatarImage.length;

    desKey = new byte[] {'10','E9','76','F8','9D','25','61','A1','10','E9','76','F8','9D','25','61','A1','10','E9','76','F8','9D','25','61','A1'};
    desICV = new byte[] {'30','E9','11','11','58','9C','B4','32'};
    desKeyLen = desKey.length;

    desEcbCipher = Cipher.getInstance(Cipher.ALG_DES_ECB_NOPAD, false);
    desCbcCipher = Cipher.getInstance(Cipher.ALG_DES_CBC_NOPAD, false);

    tempDesKey3 = KeyBuilder.buildKey(KeyBuilder.TYPE_DES_TRANSIENT_DESELECT, KeyBuilder.LENGTH_DES3_3KEY, false);

    JCSystem.requestObjectDeletion();
 }

  private void encryptData() {
    cipher.init(tempDesKey3, Cipher.MODE_ENCRYPT);
    cipher.doFinal(buf, ISO7816.OFFSET_CDATA, len, buffer, (short)0);

    cipher.init(tempDesKey3, Cipher.MODE_DECRYPT, desICV, (short)0, (short)8);
    cipher.doFinal(buf, ISO7816.OFFSET_CDATA, len, buffer, (short)0);

  }

  public void process(APDU apdu) {
    if (selectingApplet()) {
      return;
    }

    byte[] buf = apdu.getBuffer();
    short len = apdu.setIncomingAndReceive();
    switch (buf[ISO7816.OFFSET_INS]) {
      case INS_INIT_PIN:
        Util.arrayCopy(buf, (short)ISO7816.OFFSET_CDATA, pin, (short)0, len);
        pinIdLen = len;
        break;
      case INS_VERIFY_PIN:
        byte pinInput[];
        Util.arrayCopy(buf, (short)ISO7816.OFFSET_CDATA, pinInput, (short)0, len);
        for(short i = 0; i < pinIdLen, i++){
          if((byte)buf[i] != (byte)pin[i]){
              ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
          }
        }
        break;
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
        avatarImageLen = len;
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
