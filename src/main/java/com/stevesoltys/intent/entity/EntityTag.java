package com.stevesoltys.intent.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Steve Soltys
 */
@AllArgsConstructor
public class EntityTag {

    @Getter
    private final Entity entity;

    @Getter
    private final String identifier;

    @Getter
    private final String value;
}
