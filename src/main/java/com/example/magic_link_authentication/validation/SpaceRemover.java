package com.example.magic_link_authentication.validation;


/**EDGE CASE TO REMOVE EXTRA SPACE (EMAIL_ID)**/
public class SpaceRemover {
    public static String removeSpaces(String input) {
        return input.replaceAll("\\s", "");
    }
}
