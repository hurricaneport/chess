package dataAccess.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    public static String hashPassword(String password) {
        return encoder.encode(password);
    }

    public static boolean checkPassword(String clearTextPassword, String hashedPassword) {
        return encoder.matches(clearTextPassword, hashedPassword);
    }
}
