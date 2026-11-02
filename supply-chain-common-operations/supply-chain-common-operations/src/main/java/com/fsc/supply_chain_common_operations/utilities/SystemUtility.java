package com.fsc.supply_chain_common_operations.utilities;

import java.security.SecureRandom;
import java.util.*;

public class SystemUtility {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "@#$%&*!";
    private static final String ALL =
            UPPERCASE + LOWERCASE + DIGITS + SPECIAL;

    public static String generateId(String prefix) {
        int randomNumber = RANDOM.nextInt(900) + 100;
        return prefix + "-" + randomNumber;
    }


    public static String generatePassword(int length) {

        if (length < 8) {
            throw new IllegalArgumentException(
                    "Password length must be at least 8 characters"
            );
        }

        List<Character> password = new ArrayList<>();

        // Guarantee required character types
        password.add(randomCharacter(UPPERCASE));
        password.add(randomCharacter(LOWERCASE));
        password.add(randomCharacter(DIGITS));
        password.add(randomCharacter(SPECIAL));

        // Fill remaining characters
        while (password.size() < length) {
            password.add(randomCharacter(ALL));
        }

        // Randomize positions
        Collections.shuffle(password, RANDOM);

        StringBuilder result = new StringBuilder(length);

        for (Character character : password) {
            result.append(character);
        }

        return result.toString();
    }

    private static char randomCharacter(String characters) {
        return characters.charAt(
                RANDOM.nextInt(characters.length())
        );
    }

    public static String replaceTemplateVariableWithValues(
            String template,
            HashMap<String, String> variableValues) {

        if (template == null || template.isEmpty() || variableValues == null) {
            return template;
        }

        String result = template;

        for (Map.Entry<String, String> entry : variableValues.entrySet()) {
            String variable = "${" + entry.getKey() + "}";
            String value = entry.getValue() != null ? entry.getValue() : "";

            result = result.replace(variable, value);
        }

        return result;
    }


}
