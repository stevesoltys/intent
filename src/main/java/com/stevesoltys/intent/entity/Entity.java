package com.stevesoltys.intent.entity;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * @author Steve Soltys
 */
public abstract class Entity implements Serializable {

    public abstract String getIdentifier();

    public abstract Pattern getPattern();
}
