package com.stevesoltys.intent.classifier.metric;

import com.stevesoltys.intent.expression.Expression;

/**
 * @author Steve Soltys
 */
public class ExactMatchMetric implements Metric {

    @Override
    public double getWeight(Expression inputExpression, Expression relatedIntentExpression) {
        return 1;
    }

    @Override
    public boolean triggered(Expression inputExpression, Expression relatedIntentExpression) {
        return inputExpression.getExpression().matches(relatedIntentExpression.getExpression());
    }
}
