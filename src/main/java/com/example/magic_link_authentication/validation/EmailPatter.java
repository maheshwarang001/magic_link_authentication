package com.example.magic_link_authentication.validation;

import java.util.regex.Pattern;

/**STATIC CLASS TO VALIDATE EMAIL ID (REGEX)**/
public class EmailPatter {
    public static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
}
