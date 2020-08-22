package utils;

import java.awt.HeadlessException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;
import javax.swing.JOptionPane;

public class APDU {

    private Card card;
    private TerminalFactory factory;
    private CardChannel channel;
    private CardTerminal terminal;
    private List<CardTerminal> terminals;
    private ResponseAPDU response;
    private byte[] aID;
    private boolean isConnected = false;
    private static APDU instanse;

    public static APDU getInstanse() {
        if (instanse == null) {
            instanse = new APDU();
        }
        return instanse;
    }

    private APDU() {

    }

    public boolean connectCard(byte[] aid) {
        this.aID = aid;
        try {
            if (!isConnected) {
                factory = TerminalFactory.getDefault();
                terminals = factory.terminals().list();
                terminal = terminals.get(0);
                card = terminal.connect("T=0");
                channel = card.getBasicChannel();
                response = channel.transmit(new CommandAPDU(0x00, (byte) 0xA4, 0x04, 0x00, aid));
                String resul = Arrays.toString(response.getBytes());

                if (resul.trim().equals("[-112, 0]")) {
                    isConnected = true;
                    JOptionPane.showMessageDialog(null, "Connect thành công ");
                } else {
                    JOptionPane.showMessageDialog(null, "Connect không thành công");
                }
            }

        } catch (HeadlessException | CardException ex) {
            System.out.print(ex);
            JOptionPane.showMessageDialog(null, "Đã có lỗi xảy ra, vui lòng kiểm tra thẻ hoặc AID");
            return false;
        }
        return true;
    }

    public boolean disconnect() {
        try {
            card.disconnect(true);
            isConnected = false;
            return true;
        } catch (CardException e) {
            JOptionPane.showMessageDialog(null, "Đã có lỗi xảy ra");
            return false;
        }
    }

    public boolean reset() {
        try {
            card.disconnect(true);
            Thread.sleep(1000);
            factory = TerminalFactory.getDefault();
            terminals = factory.terminals().list();
            terminal = terminals.get(0);
            card = terminal.connect("T=0");
            channel = card.getBasicChannel();
            response = channel.transmit(new CommandAPDU(0x00, (byte) 0xA4, 0x04, 0x00, aID));
            return true;
        } catch (InterruptedException | CardException e) {
            JOptionPane.showMessageDialog(null, "Đã có lỗi xảy ra");
            return false;
        }
    }

    public ResponseAPDU makeCommand(int CLA, int INS, int P1, int P2) {
        try {
            return this.channel.transmit(new CommandAPDU(CLA, INS, P1, P2));
        } catch (CardException ex) {
            Logger.getLogger(APDU.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public ResponseAPDU makeCommand(int CLA, int INS, int P1, int P2, byte[] CDATA) {
        try {
            return this.channel.transmit(new CommandAPDU(CLA, INS, P1, P2, CDATA));
        } catch (CardException ex) {
            Logger.getLogger(APDU.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public ResponseAPDU makeCommand(int CLA, int INS, int P1, int P2, byte[] CDATA, int CLE) {
        try {
            return this.channel.transmit(new CommandAPDU(CLA, INS, P1, P2, CDATA, CLE));
        } catch (CardException ex) {
            Logger.getLogger(APDU.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
