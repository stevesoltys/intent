package com.stevesoltys.intent.expression;

import com.stevesoltys.intent.entity.Entity;
import com.stevesoltys.intent.expression.token.EntityToken;
import com.stevesoltys.intent.expression.token.ExpressionToken;
import com.stevesoltys.intent.expression.token.Token;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Steve Soltys
 */
public class ExpressionBuilder {

    private final List<Token> tokens;

    public ExpressionBuilder() {
        tokens = new LinkedList<>();
    }

    public ExpressionBuilder token(String token) {
        tokens.add(new ExpressionToken(token));
        return this;
    }

    public ExpressionBuilder token(Entity entity, String identifier) {
        tokens.add(new EntityToken(entity, identifier));
        return this;
    }

    public Expression build() {
        return new Expression(tokens);
    }
}
