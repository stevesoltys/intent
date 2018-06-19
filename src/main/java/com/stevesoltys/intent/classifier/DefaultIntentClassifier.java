package com.stevesoltys.intent.classifier;

import com.stevesoltys.intent.entity.EntityTag;
import com.stevesoltys.intent.entity.EntityTagger;
import com.stevesoltys.intent.expression.Expression;
import com.stevesoltys.intent.expression.ExpressionFactory;
import com.stevesoltys.intent.Intent;
import com.stevesoltys.intent.IntentClassification;
import com.stevesoltys.intent.IntentContext;
import com.stevesoltys.intent.classifier.metric.Metric;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The default intent classifier implementation.
 *
 * @author Steve Soltys
 */
public class DefaultIntentClassifier extends IntentClassifierAdapter {

    private final DefaultIntentClassifierConfiguration configuration;

    private final EntityTagger entityTagger;

    private final ExpressionFactory expressionFactory;

    public DefaultIntentClassifier(DefaultIntentClassifierConfiguration configuration) {
        super(configuration.getIntentRepository());

        this.configuration = configuration;
        entityTagger = configuration.getEntityTagger();
        expressionFactory = configuration.getExpressionFactory();
    }

    public IntentClassification classify(String input) {
        return classify(new IntentContext(Collections.emptyList()), input);
    }

    public IntentClassification classify(IntentContext context, String input) {
        Expression inputExpression = expressionFactory.create(input);

        double resultIntentWeight = 0;
        Intent resultIntent = null;
        Expression resultExpression = null;

        Set<Intent> intents = getIntentRepository().getIntents().stream()
                .filter(intent -> intent.getContext() != null && intent.getContext().matches(context))
                .collect(Collectors.toSet());

        for (Intent relatedIntent : intents) {
            double intentWeight = 0;

            Expression relatedExpression = null;

            for (Expression relatedIntentExpression : relatedIntent.getExpressions()) {
                double expressionWeight = 0;

                if(inputExpression.getExpression().matches(relatedIntentExpression.getExpression())) {
                    expressionWeight = 1.0;

                } else {
                    int metricCount = 0;

                    for (Metric metric : configuration.getMetrics()) {

                        if (metric.triggered(inputExpression, relatedIntentExpression)) {
                            expressionWeight += metric.getWeight(inputExpression, relatedIntentExpression);
                            metricCount++;
                        }
                    }

                    if (metricCount > 0) {
                        expressionWeight /= metricCount;
                    }
                }

                if (expressionWeight > intentWeight) {
                    relatedExpression = relatedIntentExpression;
                    intentWeight = expressionWeight;

                } else if(expressionWeight == intentWeight && relatedExpression != null) {

                    if(relatedExpression.getTokens().size() < relatedIntentExpression.getTokens().size()) {
                        relatedExpression = relatedIntentExpression;
                    }
                }
            }

            if (intentWeight > resultIntentWeight) {
                resultIntentWeight = intentWeight;
                resultIntent = relatedIntent;
                resultExpression = relatedExpression;

            } else if(intentWeight == resultIntentWeight && relatedExpression != null && resultExpression != null) {

                if(resultExpression.getTokens().size() < relatedExpression.getTokens().size()) {
                    resultIntent = relatedIntent;
                    resultExpression = relatedExpression;
                }
            }
        }

        List<EntityTag> entities = entityTagger.tag(resultExpression, inputExpression);
        return new IntentClassification(resultIntentWeight, resultIntent, entities);
    }
}
