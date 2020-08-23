package DriverLicense;

import java.util.ArrayList;
import java.util.List;
import javax.smartcardio.ResponseAPDU;

public class Service {

    private static Service instanse;

    private Service() {

    }

    public static Service getInstanse() {
        if (instanse == null) {
            instanse = new Service();
        }
        return instanse;
    }

    public UserInfo getLicenseInfo(ResponseAPDU response) {
        UserInfo cardLicense = new UserInfo();
        /**
         * get CARD ID
         */
        byte[] buffer = response.getBytes();
        int pointer = 0;
        int cur_pointer;

        StringBuilder cardId = new StringBuilder();
        for (int i = 0; i < Applet.CARD_ID_LENGTH; i++, pointer++) {
            cardId.append(buffer[i]);
        }
        cardLicense.setCardId(cardId.toString());
        /**
         * get NAME
         */
        StringBuilder fullName = new StringBuilder();
        int fullNameLen = buffer[pointer++];
        cur_pointer = pointer;
        for (int i = pointer; i < cur_pointer + fullNameLen; i++, pointer++) {
            fullName.append(Character.toString((char) buffer[i]));
        }
        cardLicense.setFullName(fullName.toString());

        /**
         * get Address
         */
        StringBuilder address = new StringBuilder();
        int addressLen = buffer[pointer++];
        cur_pointer = pointer;
        for (int i = cur_pointer; i < cur_pointer + addressLen; i++, pointer++) {
            address.append(Character.toString((char) buffer[i]));
        }
        cardLicense.setAddress(address.toString());
        /**
         * get Birth date
         */
        StringBuilder birthDate = new StringBuilder();
        cur_pointer = pointer;
        for (int i = cur_pointer; i < cur_pointer + Applet.DATE_FORMAT_LENGTH; i++, pointer++) {
            String text = (buffer[i] < 10 ? "0" : "") + buffer[i];
            birthDate.append(text);
        }
        cardLicense.setBirthDate(birthDate.toString());

        /**
         * get Release date
         */
        StringBuilder releaseDate = new StringBuilder();
        cur_pointer = pointer;
        for (int i = cur_pointer; i < cur_pointer + Applet.DATE_FORMAT_LENGTH; i++, pointer++) {
            String text = (buffer[i] < 10 ? "0" : "") + buffer[i];
            releaseDate.append(text);
        }
        cardLicense.setReleaseDate(releaseDate.toString());
        /**
         * Get expire date
         */
        StringBuilder expireDate = new StringBuilder();
        cur_pointer = pointer;
        for (int i = cur_pointer; i < cur_pointer + Applet.DATE_FORMAT_LENGTH; i++, pointer++) {
            String text = (buffer[i] < 10 ? "0" : "") + buffer[i];
            expireDate.append(text);
        }
        cardLicense.setExpireDate(expireDate.toString());
        cardLicense.setCardType((int) buffer[pointer]);
        return cardLicense;
    }

    public List<Violation> getViolationList(ResponseAPDU response) {
        ArrayList<Violation> violationList = new ArrayList<>();
        byte[] buffer = response.getBytes();
        int violationCode;

        for (int i = 0; i < 5; i++) {
            StringBuilder violationDate = new StringBuilder();
            violationCode = buffer[i * 5];
            violationDate.append(buffer[i * 5 + 1] < 10 ? "0" : "").append(buffer[i * 5 + 1]);
            violationDate.append(buffer[i * 5 + 2] < 10 ? "0" : "").append(buffer[i * 5 + 2]);
            violationDate.append(buffer[i * 5 + 3] < 10 ? "0" : "").append(buffer[i * 5 + 3]);
            violationDate.append(buffer[i * 5 + 4] < 10 ? "0" : "").append(buffer[i * 5 + 4]);

            Violation violation = new Violation(violationCode, violationDate.toString());
            violationList.add(violation);
        }

        return violationList;
    }
}
