package com.stevesoltys.intent.entity;

import com.stevesoltys.intent.expression.Expression;

import java.util.List;

/**
 * @author Steve Soltys
 */
public interface EntityTagger {

    List<EntityTag> tag(Expression matchedExpression, Expression inputExpression);
}
