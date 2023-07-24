package com.example.magic_link_authentication.validation;

public class SpaceRemover {
    public static String removeSpaces(String input) {
        return input.replaceAll("\\s", "");
    }
}
