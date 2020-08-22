package DriverLicense;

import utils.DateUtils;

public class Violation {

    public static final int NONE_FAULT = 0x00;

    public static final int MISSING_EQUIPMENT_FAULT = 0x01;

    public static final int LIGHT_SIGNAL_FAULT = 0x02;

    public static final int OVER_WEIGHT_FAULT = 0x03;

    public static final int WRONG_LANE_FAULT = 0x04;

    public static final int VIOLATE_ALCOHOL_FAULT = 0x05;

    public static final int OVER_SPEED_FAULT = 0x06;

    private int faultCode;

    private String date;

    public Violation() {
    }

    public Violation(int faultCode, String date) {
        this.faultCode = faultCode;
        this.date = date;
    }

    public int getFaultCode() {
        return faultCode;
    }

    public String getDate() {
        return DateUtils.toDateString(date);
    }

    public void setName(int faultCode) {
        this.faultCode = faultCode;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String codeToString(int faultCode) {
        switch (faultCode) {
            case MISSING_EQUIPMENT_FAULT:
                return "Thiếu trang thiết bị bảo hộ khi tham gia giao thông";
            case LIGHT_SIGNAL_FAULT:
                return "Vi phạm tín hiệu giao thông";
            case OVER_WEIGHT_FAULT:
                return "Vượt quá tải trọng cho phép";
            case WRONG_LANE_FAULT:
                return "Đi sai làn đường quy định";
            case VIOLATE_ALCOHOL_FAULT:
                return "Uống rượu bia khi tham gia giao thông";
            case OVER_SPEED_FAULT:
                return "Vượt quá tốc độ";
            case NONE_FAULT:
                return "Không có lỗi nào được ghi nhận";
            default:
                return "Lỗi này chưa được liệt kê trong thư viện";
        }
    }

    @Override
    public String toString() {
        return "Lỗi: " + codeToString(faultCode) + ", Ngày phạm lỗi: " + getDate();
    }

}
