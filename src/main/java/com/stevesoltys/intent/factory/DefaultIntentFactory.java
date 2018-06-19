package com.stevesoltys.intent.factory;

import com.stevesoltys.intent.expression.Expression;
import com.stevesoltys.intent.expression.ExpressionFactory;
import com.stevesoltys.intent.expression.token.Token;
import com.stevesoltys.intent.Intent;
import com.stevesoltys.intent.IntentContext;
import lombok.Builder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultIntentFactory extends IntentFactoryAdapter {

    private final ExpressionFactory expressionFactory;

    @Builder
    public DefaultIntentFactory(ExpressionFactory expressionFactory) {
        this.expressionFactory = expressionFactory;
    }

    @Override
    public Intent create(String identifier, List<String> context, Expression... expressions) {
        List<Expression> parsedExpressions = Arrays.stream(expressions)
                .map(expression -> expressionFactory.create(expression.getTokens()))
                .collect(Collectors.toList());

        IntentContext intentContext = new IntentContext(context);
        return new Intent(identifier, parsedExpressions, intentContext);
    }

    @Override
    public Intent create(String identifier, Expression... expressions) {
        return create(identifier, Collections.emptyList(), expressions);
    }

    @Override
    public Intent create(String identifier, Collection<Expression> expressions) {
        return create(identifier, expressions.toArray(new Expression[0]));
    }

    @Override
    public Intent create(String identifier, Collection<Expression> expressions, List<String> context) {
        return create(identifier, context, expressions.toArray(new Expression[0]));
    }

    @Override
    public Intent create(String identifier, List<List<Token>> expressions) {
        return create(identifier, expressions, Collections.emptyList());
    }

    @Override
    public Intent create(String identifier, List<List<Token>> Tokens, List<String> context) {
        List<Expression> parsedExpressions = Tokens.stream()
                .map(expressionFactory::create)
                .collect(Collectors.toList());

        IntentContext intentContext = new IntentContext(context);
        return new Intent(identifier, parsedExpressions, intentContext);
    }
}
