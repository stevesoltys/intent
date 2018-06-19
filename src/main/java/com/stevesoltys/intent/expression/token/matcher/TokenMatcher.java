package com.stevesoltys.intent.expression.token.matcher;

import com.stevesoltys.intent.entity.Entity;
import com.stevesoltys.intent.expression.token.Token;

/**
 * @author Steve Soltys
 */
public interface TokenMatcher {

    boolean matches(Token similarToken, Token inputToken);

    boolean matches(Token similarToken, String inputToken);

    boolean matches(Entity entity, String inputToken);
}
