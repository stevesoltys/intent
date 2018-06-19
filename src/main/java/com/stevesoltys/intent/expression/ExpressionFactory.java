package com.stevesoltys.intent.expression;

import com.stevesoltys.intent.expression.token.Token;

import java.util.List;

/**
 * @author Steve Soltys
 */
public interface ExpressionFactory {

    Expression create(String input);

    Expression create(List<Token> input);
}
