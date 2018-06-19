package com.stevesoltys.intent.entity.system;

import com.stevesoltys.intent.entity.Entity;

import java.util.regex.Pattern;

/**
 * @author Steve Soltys
 */
public class WildcardEntity extends Entity {

    private static final String IDENTIFIER = "wildcard";

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public Pattern getPattern() {
        return Pattern.compile(".+", Pattern.CASE_INSENSITIVE);
    }
}
