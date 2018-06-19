package com.stevesoltys.intent.entity.system;

import com.stevesoltys.intent.entity.Entity;

import java.util.regex.Pattern;

/**
 * @author Steve Soltys
 */
public class UrlEntity extends Entity {

    private static final String IDENTIFIER = "url";

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public Pattern getPattern() {
        return Pattern.compile("(\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]+[-a-zA-Z0-9+&@#/%=~_|])");
    }
}
