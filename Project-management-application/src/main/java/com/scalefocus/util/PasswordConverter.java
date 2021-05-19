package com.scalefocus.util;

public class PasswordConverter {

    public static String encodePassword(String password){
        StringBuilder asciiString = new StringBuilder();
        asciiString.append("[");

        for (int i = 0; i < password.length(); i++) {
            asciiString.append((int) password.charAt(i));
            asciiString.append(", ");
        }
        asciiString.deleteCharAt(asciiString.lastIndexOf(", "));
        asciiString.deleteCharAt(asciiString.lastIndexOf(" "));
        asciiString.append("]");
        return asciiString.toString();
    }

    public static boolean matches(String password, String hashed){
        password = encodePassword(password);
        if(password.length() != hashed.length()){
            return false;
        }
        String[] passArr = password.split(", ");
        String[] hashedArr = hashed.split(", ");

        for (int i = 0; i < passArr.length; i++) {
            if (!passArr[i].equals(hashedArr[i])){
                return false;
            }
        }
        return true;
    }
}
