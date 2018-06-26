package com.stevesoltys.intent;

import com.stevesoltys.intent.expression.Expression;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author Steve Soltys
 */
@AllArgsConstructor
@EqualsAndHashCode(of = "identifier")
public class Intent implements Serializable {

    private static final long serialVersionUID = 0L;

    @Getter
    private final String identifier;

    @Singular
    @Getter
    private final List<Expression> expressions;

    @Getter
    private IntentContext context = IntentContext.builder().build();
}
