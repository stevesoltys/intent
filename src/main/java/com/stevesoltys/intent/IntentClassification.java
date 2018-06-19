package com.stevesoltys.intent;

import com.stevesoltys.intent.entity.EntityTag;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @author Steve Soltys
 */
@AllArgsConstructor
public class IntentClassification {

    @Getter
    private final double weight;

    @Getter
    private final Intent intent;

    @Getter
    private final List<EntityTag> entities;
}
