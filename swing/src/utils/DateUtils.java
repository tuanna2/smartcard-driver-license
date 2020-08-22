package utils;

public class DateUtils {

    public static String toDateString(String date) {
        try {
            StringBuilder dateFormated = new StringBuilder();
            dateFormated.append(Character.toString((char) date.charAt(0)));
            dateFormated.append(Character.toString((char) date.charAt(1)));
            dateFormated.append("/");
            dateFormated.append(Character.toString((char) date.charAt(2)));
            dateFormated.append(Character.toString((char) date.charAt(3)));
            dateFormated.append("/");
            dateFormated.append(Character.toString((char) date.charAt(4)));
            dateFormated.append(Character.toString((char) date.charAt(5)));
            dateFormated.append(Character.toString((char) date.charAt(6)));
            dateFormated.append(Character.toString((char) date.charAt(7)));
            return dateFormated.toString();
        } catch (Exception e) {
            return date;
        }

    }
}
