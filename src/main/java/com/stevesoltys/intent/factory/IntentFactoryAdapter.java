package com.stevesoltys.intent.factory;

import com.stevesoltys.intent.expression.Expression;
import com.stevesoltys.intent.Intent;
import com.stevesoltys.intent.IntentFactory;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Steve Soltys
 */
public abstract class IntentFactoryAdapter implements IntentFactory {

    public Intent create(String identifier, String... expressions) {
        return create(identifier, Arrays.stream(expressions)
                .map(expression -> new Expression(expression).getTokens())
                .collect(Collectors.toList()));
    }
}
