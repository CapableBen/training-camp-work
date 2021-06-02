package org.geektimes.projects.user.validator.bean.validation;

import org.apache.commons.lang.StringUtils;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtils {

    public static final Pattern id_pattern = Pattern.compile("^[0-9]+$");

    public static final Pattern phone_pattern = Pattern.compile("1\\d{10}");

    public static final Pattern uuid_pattern = Pattern.compile("[^0-9]");

    public static boolean idValid(Long id) {
        if (StringUtils.isEmpty(id.toString())) {
            return false;
        }
        Matcher matcher = id_pattern.matcher(id.toString());
        return matcher.matches();
    }

    public static boolean passwordValid(String password, int max, int min) {
        if (StringUtils.isEmpty(password)) {
            return false;
        }
        return password.length() >= min && password.length() <= max;
    }

    public static boolean phoneValid(String phoneNumber) {
        if (StringUtils.isEmpty(phoneNumber)) {
            return false;
        }
        Matcher matcher = phone_pattern.matcher(phoneNumber);
        return matcher.matches();
    }


    public static Long idGenerator() {
        Matcher matcher = uuid_pattern.matcher(UUID.randomUUID().toString());
        String id = matcher.replaceAll("").trim().substring(0, 5);
        Long idLong = Long.valueOf(id);

        while (idLong < 1000) {
            idLong = idGenerator();
        }

        return idLong;
    }

}