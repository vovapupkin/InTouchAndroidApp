package intouchteam.intouch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    final static String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    final static String PASSWORD_PATTERN = "^((?=.*\\d)(?=.*[A-za-z]).{6,20})$";
    final static String USERNAME_PATTERN = "^[\\w.@+-]+.{3,30}$";

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPassword(String pass) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(pass);
        return matcher.matches();
    }

    public static boolean isValidUsername(String username) {
        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    public static boolean isValidName(String username) {
        //TODO: create name validation
        return true;
    }

    public static boolean isValidRestorePasswordCode(String username) {
        //TODO: create code validation
        return true;
    }
}
