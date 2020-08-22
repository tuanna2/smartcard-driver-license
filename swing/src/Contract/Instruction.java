/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Contract;

/**
 *
 * @author superuser
 */
public interface Instruction {

    static final byte INS_READ_BASIC_INFO = 0x00;

    static final byte INS_READ_FAULT_INFO = 0x01;

    static final byte INS_READ_AVATAR_IMAGE = 0x02;

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
}
