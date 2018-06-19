package com.stevesoltys.intent;

import com.stevesoltys.intent.expression.Expression;
import com.stevesoltys.intent.expression.token.Token;

import java.util.Collection;
import java.util.List;

/**
 * @author Steve Soltys
 */
public interface IntentFactory {

    Intent create(String identifier, Expression... expressions);

    Intent create(String identifier, Collection<Expression> expressions);

    Intent create(String identifier, Collection<Expression> expressions, List<String> context);

    Intent create(String identifier, List<String> context, Expression... expressions);

    Intent create(String identifier, List<List<Token>> expressions);

    Intent create(String identifier, List<List<Token>> expressions, List<String> context);

    Intent create(String identifier, String... expressions);
}
