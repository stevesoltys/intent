package com.stevesoltys.intent.expression.token;

import com.stevesoltys.intent.expression.token.pos.PartOfSpeech;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author Steve Soltys
 */
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ExpressionToken extends Token implements Serializable {

    private static final long serialVersionUID = 0L;

    @Getter
    private final String value;

    @Getter
    private final PartOfSpeech partOfSpeech;

    public ExpressionToken(String value) {
        this(value, PartOfSpeech.UNKNOWN);
    }

    public boolean isEntity() {
        return false;
    }
}
