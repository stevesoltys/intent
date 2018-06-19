package com.stevesoltys.intent.expression.token;

import com.stevesoltys.intent.expression.token.pos.PartOfSpeech;
import lombok.EqualsAndHashCode;

/**
 * @author Steve Soltys
 */
@EqualsAndHashCode
public abstract class Token {

    public abstract String getValue();

    public abstract PartOfSpeech getPartOfSpeech();

    public abstract boolean isEntity();
}
