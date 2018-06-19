package com.stevesoltys.intent.expression.token;

import com.stevesoltys.intent.entity.Entity;
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
public class EntityToken extends Token implements Serializable {

    private static final long serialVersionUID = 0L;

    @Getter
    private final Entity entity;

    @Getter
    private final String identifier;

    @Override
    public String getValue() {
        return entity.getPattern().pattern();
    }

    @Override
    public PartOfSpeech getPartOfSpeech() {
        return PartOfSpeech.UNKNOWN;
    }

    @Override
    public boolean isEntity() {
        return true;
    }
}
