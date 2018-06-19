package com.stevesoltys.intent.classifier.metric;

import com.stevesoltys.intent.expression.Expression;

/**
 * @author Steve Soltys
 */
public interface Metric {

    double getWeight(Expression inputExpression, Expression relatedIntentExpression);

    boolean triggered(Expression inputExpression, Expression relatedIntentExpression);
}
