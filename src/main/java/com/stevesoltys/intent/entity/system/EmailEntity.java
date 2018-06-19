package com.stevesoltys.intent.entity.system;

import com.stevesoltys.intent.entity.Entity;

import java.util.regex.Pattern;

/**
 * @author Steve Soltys
 */
public class EmailEntity extends Entity {

    private static final String IDENTIFIER = "email";

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public Pattern getPattern() {
        return Pattern.compile("([A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6})", Pattern.CASE_INSENSITIVE);
    }
}
