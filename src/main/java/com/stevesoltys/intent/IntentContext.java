package com.stevesoltys.intent;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author Steve Soltys
 */
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class IntentContext implements Serializable {

    @Singular
    @Getter
    private final List<String> contextIdentifiers;

    public boolean matches(IntentContext context) {
        return context.getContextIdentifiers().containsAll(contextIdentifiers);
    }
}
