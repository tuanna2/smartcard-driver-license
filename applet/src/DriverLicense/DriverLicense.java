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

  private static Key tempDesKey3;
  private static short desKeyLen;
  private static byte[] desKey = new byte[] {'N', 'G', 'U', 'Y', 'E','N', 'A', 'N', 'H', 'T', 'U','A', 'N','N', 'G', 'U', 'Y', 'E','N', 'A', 'N', 'H', 'T', 'U','A'};
  private static byte[] desICV = new byte[]{'T','U','A','N','H','I','E','U'};
  private static Cipher desEcbCipher;
  private static Cipher desCbcCipher;

  static final short CARD_ID_LENGTH = 0x08;
  static final short DATE_FORMAT_LENGTH = 0x04;
  private static short MAX_IMAGE_SIZE;

  private static byte cardType;

  private static byte[] pinInput, pin, cardId, fullName, birthDate, address, releaseDate, expireDate, avatarImage;

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

    pin = new byte[100];
    cardType = 0x00;
    cardId = new byte[CARD_ID_LENGTH];
    fullName = new byte[100];
    birthDate = new byte[DATE_FORMAT_LENGTH];
    address = new byte[100];
    releaseDate = new byte[DATE_FORMAT_LENGTH];
    expireDate = new byte[DATE_FORMAT_LENGTH];
    avatarImage = new byte[254];

  	pinInput = new byte[8];
    pinLen = (short) pin.length;
    cardIdLen = CARD_ID_LENGTH;
    fullNameLen = (short) fullName.length;
    birthDateLen = (short) birthDate.length;
    addressLen = (short) address.length;
    releaseDateLen = (short) releaseDate.length;
    expireDateLen = (short) expireDate.length;
    avatarImageLen = (short) avatarImage.length;

    desKeyLen = (short) desKey.length;

    desEcbCipher = Cipher.getInstance(Cipher.ALG_DES_ECB_NOPAD, false);
    desCbcCipher = Cipher.getInstance(Cipher.ALG_DES_CBC_NOPAD, false);

    tempDesKey3 = KeyBuilder.buildKey(KeyBuilder.TYPE_DES_TRANSIENT_DESELECT, KeyBuilder.LENGTH_DES3_3KEY, false);

    JCSystem.requestObjectDeletion();
 }

  public void process(APDU apdu) {
    if (selectingApplet()) {
      return;
    }

    byte[] buf = apdu.getBuffer();
    short len = apdu.setIncomingAndReceive();
    short outlen = 0;
    switch (buf[ISO7816.OFFSET_INS]) {
      case INS_INIT_PIN:
      	desEcbCipher.init(tempDesKey3, Cipher.MODE_ENCRYPT);
      	outlen = desEcbCipher.doFinal(buf, ISO7816.OFFSET_CDATA, len, buf, (short)0);
        Util.arrayCopy(buf, (short) 0, pin, (short) 0, outlen);
        pinLen = len;
        break;
      case INS_VERIFY_PIN:
		    Util.arrayCopy(buf, (short)ISO7816.OFFSET_CDATA, pinInput, (short)0, len);
      	desCbcCipher.init(tempDesKey3, Cipher.MODE_DECRYPT, desICV, (short)0, (short)8);
        outlen = desCbcCipher.doFinal(buf, ISO7816.OFFSET_CDATA, len, pin, (short)0);
        for(short i = 0; i < pinLen; i++){
          if((byte)pinInput[i] != (byte)buf[i]){
              ISOException.throwIt(ISO7816.SW_DATA_INVALID);
          }
        }
        break;
      case INS_GET_USER_INFO:
		    desCbcCipher.init(tempDesKey3, Cipher.MODE_DECRYPT, desICV, (short)0, (short)8);
        short lengthDataSend = (short) ((short) (2) + (short) (cardIdLen) + (short) (fullNameLen) + (short) (addressLen) +
          (short) (birthDateLen) + (short) (releaseDateLen) + (short) (expireDateLen) + (short) 0x01);
        apdu.setOutgoing();
        apdu.setOutgoingLength((short) lengthDataSend);
        outlen = desCbcCipher.doFinal(buf, ISO7816.OFFSET_CDATA, len, cardId, (short)0);
        apdu.sendBytes((short) 0, outlen);
        outlen = desCbcCipher.doFinal(buf, ISO7816.OFFSET_CDATA, len, fullName, (short)0);
        apdu.sendBytes((short) 0, outlen);
        outlen = desCbcCipher.doFinal(buf, ISO7816.OFFSET_CDATA, len, address, (short)0);
        apdu.sendBytes((short) 0, outlen);
        outlen = desCbcCipher.doFinal(buf, ISO7816.OFFSET_CDATA, len, birthDate, (short)0);
        apdu.sendBytes((short) 0, outlen);
        outlen = desCbcCipher.doFinal(buf, ISO7816.OFFSET_CDATA, len, releaseDate, (short)0);
        apdu.sendBytes((short) 0, outlen);
        outlen = desCbcCipher.doFinal(buf, ISO7816.OFFSET_CDATA, len, expireDate, (short)0);
        apdu.sendBytes((short) 0, outlen);  
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
        desEcbCipher.init(tempDesKey3, Cipher.MODE_ENCRYPT);
      	outlen = desEcbCipher.doFinal(buf, ISO7816.OFFSET_CDATA, len, buf, (short)0);
        Util.arrayCopy(buf, (short) 0, cardId, (short) 0, outlen);
        break;
      case INS_SET_CARD_TYPE:
        cardType = buf[ISO7816.OFFSET_P1];
        break;
      case INS_SET_FULLNAME:
        desEcbCipher.init(tempDesKey3, Cipher.MODE_ENCRYPT);
      	outlen = desEcbCipher.doFinal(buf, ISO7816.OFFSET_CDATA, len, buf, (short)0);
        Util.arrayCopy(buf, (short) 0, fullName, (short) 0, outlen);
        fullNameLen = len;
        break;
      case INS_SET_BIRTH_DATE:
        desEcbCipher.init(tempDesKey3, Cipher.MODE_ENCRYPT);
      	outlen = desEcbCipher.doFinal(buf, ISO7816.OFFSET_CDATA, len, buf, (short)0);
        Util.arrayCopy(buf, (short) 0, birthDate, (short) 0, outlen);
        birthDateLen = DATE_FORMAT_LENGTH;
        break;
      case INS_SET_ADDRESS:
        desEcbCipher.init(tempDesKey3, Cipher.MODE_ENCRYPT);
      	outlen = desEcbCipher.doFinal(buf, ISO7816.OFFSET_CDATA, len, buf, (short)0);
        Util.arrayCopy(buf, (short) 0, address, (short) 0, outlen);
        addressLen = len;
        break;
      case INS_SET_RELEASE_DATE:
        desEcbCipher.init(tempDesKey3, Cipher.MODE_ENCRYPT);
      	outlen = desEcbCipher.doFinal(buf, ISO7816.OFFSET_CDATA, len, buf, (short)0);
        Util.arrayCopy(buf, (short) 0, releaseDate, (short) 0, outlen);
        releaseDateLen = DATE_FORMAT_LENGTH;
        break;
      case INS_SET_EXPIRE_DATE:
        desEcbCipher.init(tempDesKey3, Cipher.MODE_ENCRYPT);
      	outlen = desEcbCipher.doFinal(buf, ISO7816.OFFSET_CDATA, len, buf, (short)0);
        Util.arrayCopy(buf, (short) 0, expireDate, (short) 0, outlen);
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
