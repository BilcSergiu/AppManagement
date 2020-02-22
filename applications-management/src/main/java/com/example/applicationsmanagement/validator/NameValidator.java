package com.example.applicationsmanagement.validator;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameValidator {
    /**
     *
     * @param name string which
     * @return true if the string is valid and doesnt contains numbers
     */

    public static boolean isValid(String name) {
        if(name==""){
            return false;
        }
        String regx = "[a-zA-Z0-9]+\\.?";
        Pattern pattern = Pattern.compile(regx,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);
        return matcher.find();
    }
}
