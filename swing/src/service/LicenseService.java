/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import Contract.Applet;
import Contract.Fault;
import java.util.ArrayList;
import java.util.List;
import javax.smartcardio.ResponseAPDU;
import model.License;

/**
 *
 * @author superuser
 */
public class LicenseService {

    private static LicenseService instanse;

    private LicenseService() {

    }

    public static LicenseService getInstanse() {
        if (instanse == null) {
            instanse = new LicenseService();
        }
        return instanse;
    }

    public License getLicenseInfo(ResponseAPDU response) {
        License cardLicense = new License();
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
         * get Granted date
         */
        StringBuilder grantedDate = new StringBuilder();
        cur_pointer = pointer;
        for (int i = cur_pointer; i < cur_pointer + Applet.DATE_FORMAT_LENGTH; i++, pointer++) {
            String text = (buffer[i] < 10 ? "0" : "") + buffer[i];
            grantedDate.append(text);
        }
        cardLicense.setGrantedDate(grantedDate.toString());
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

    public List<Fault> getFaultList(ResponseAPDU response) {
        ArrayList<Fault> faultList = new ArrayList<>();
        byte[] buffer = response.getBytes();
        int faultCode;

        for (int i = 0; i < 5; i++) {
            StringBuilder faultDate = new StringBuilder();
            faultCode = buffer[i * 5];
            faultDate.append(buffer[i * 5 + 1] < 10 ? "0" : "").append(buffer[i * 5 + 1]);
            faultDate.append(buffer[i * 5 + 2] < 10 ? "0" : "").append(buffer[i * 5 + 2]);
            faultDate.append(buffer[i * 5 + 3] < 10 ? "0" : "").append(buffer[i * 5 + 3]);
            faultDate.append(buffer[i * 5 + 4] < 10 ? "0" : "").append(buffer[i * 5 + 4]);

            Fault fault = new Fault(faultCode, faultDate.toString());
            faultList.add(fault);
        }

        return faultList;
    }
}
