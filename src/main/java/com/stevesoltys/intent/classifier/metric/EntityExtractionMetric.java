package com.stevesoltys.intent.classifier.metric;

import com.stevesoltys.intent.entity.EntityTag;
import com.stevesoltys.intent.expression.Expression;
import com.stevesoltys.intent.classifier.DefaultIntentClassifierConfiguration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Steve Soltys
 */
public class EntityExtractionMetric implements Metric {

    private final DefaultIntentClassifierConfiguration configuration;

    public EntityExtractionMetric(DefaultIntentClassifierConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public double getWeight(Expression inputExpression, Expression relatedIntentExpression) {
        List<EntityTag> entities = configuration.getEntityTagger().tag(relatedIntentExpression, inputExpression);

        return entities.stream().collect(Collectors.averagingDouble(tag -> tag.getValue().isEmpty() ? 0 : 1));
    }

    @Override
    public boolean triggered(Expression inputExpression, Expression relatedIntentExpression) {
        return relatedIntentExpression.hasEntity();
    }
}
