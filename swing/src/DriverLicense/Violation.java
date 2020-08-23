package DriverLicense;

import utils.DateUtils;

public class Violation {

    public static final int NON_FAULT = 0x00;
    public static final int MISSING_EQUIPMENT_FAULT = 0x01;
    public static final int LIGHT_SIGNAL_FAULT = 0x02;
    public static final int OVER_WEIGHT_FAULT = 0x03;
    public static final int WRONG_LANE_FAULT = 0x04;
    public static final int VIOLATE_ALCOHOL_FAULT = 0x05;
    public static final int OVER_SPEED_FAULT = 0x06;

    private int violationCode;

    private String date;

    public Violation() {
    }

    public Violation(int violationCode, String date) {
        this.violationCode = violationCode;
        this.date = date;
    }

    public int getviolationCode() {
        return violationCode;
    }

    public String getDate() {
        return DateUtils.toDateString(date);
    }

    public void setName(int violationCode) {
        this.violationCode = violationCode;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String codeToString(int violationCode) {
        switch (violationCode) {
            case MISSING_EQUIPMENT_FAULT:
                return "Không đội mũ bảo hiểm";
            case LIGHT_SIGNAL_FAULT:
                return "Vượt đèn đỏ";
            case OVER_WEIGHT_FAULT:
                return "Vượt quá tải trọng quy định";
            case WRONG_LANE_FAULT:
                return "Đi sai làn đường";
            case VIOLATE_ALCOHOL_FAULT:
                return "Uống rượu bia khi tham gia giao thông";
            case OVER_SPEED_FAULT:
                return "Vượt quá tốc độ";
            case NON_FAULT:
                return "Không có lỗi nào được ghi nhận";
            default:
                return "Lỗi khác";
        }
    }

    @Override
    public String toString() {
        return "Lỗi: " + codeToString(violationCode) + ", Ngày phạm lỗi: " + getDate();
    }

}
