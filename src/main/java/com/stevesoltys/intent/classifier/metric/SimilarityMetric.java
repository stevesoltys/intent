package com.stevesoltys.intent.classifier.metric;

import com.stevesoltys.intent.expression.Expression;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import info.debatty.java.stringsimilarity.interfaces.StringDistance;

/**
 * @author Steve Soltys
 */
public class SimilarityMetric implements Metric {

    private final StringDistance stringDistance = new NormalizedLevenshtein();

    @Override
    public double getWeight(Expression inputExpression, Expression relatedIntentExpression) {
        return 1 - stringDistance.distance(sanitize(inputExpression.getExpression()),
                sanitize(relatedIntentExpression.getExpression()));
    }

    @Override
    public boolean triggered(Expression inputExpression, Expression relatedIntentExpression) {
        return !relatedIntentExpression.hasEntity() && stringDistance.distance(
                sanitize(inputExpression.getExpression()),
                sanitize(relatedIntentExpression.getExpression())) <= 0.9;
    }

    private String sanitize(String sentence) {
        return sentence.replaceAll("[\\p{Punct}]+", "").toLowerCase();
    }
}
