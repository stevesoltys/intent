package com.stevesoltys.intent.classifier.metric;

import com.stevesoltys.intent.expression.Expression;
import com.stevesoltys.intent.expression.token.Token;
import com.stevesoltys.intent.expression.token.matcher.TokenMatcher;
import com.stevesoltys.intent.expression.token.pos.PartOfSpeech;
import com.stevesoltys.intent.classifier.DefaultIntentClassifierConfiguration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Steve Soltys
 */
public class PartOfSpeechMetric implements Metric {

    private final DefaultIntentClassifierConfiguration configuration;

    private final TokenMatcher matcher;

    public PartOfSpeechMetric(DefaultIntentClassifierConfiguration configuration) {
        this.configuration = configuration;

        matcher = configuration.getTokenMatcher();
    }

    public double getWeight(Expression inputExpression, Expression relatedIntentExpression) {
        double result = 0;
        double count = 0;

        for (PartOfSpeech partOfSpeech : PartOfSpeech.values()) {
            double partOfSpeechWeight = configuration.getPartOfSpeechWeights().getOrDefault(partOfSpeech, 0d);

            List<Token> inputValues = getTokens(inputExpression, partOfSpeech);
            List<Token> relatedValues = getTokens(relatedIntentExpression, partOfSpeech);

            Set<Token> usedTokens = new HashSet<>();

            for (Token relatedToken : relatedValues) {

                if (usedTokens.contains(relatedToken) || relatedToken.isEntity()) {
                    continue;
                }

                boolean isInputToken = inputValues.stream().anyMatch(inputToken ->
                        matcher.matches(relatedToken, inputToken));

                boolean isStopToken = configuration.getStopWords().stream()
                        .anyMatch(stopToken -> matcher.matches(relatedToken, stopToken));

                if (isInputToken && !isStopToken) {
                    result += 1;
                    count += 1;

                    usedTokens.add(relatedToken);

                } else if (!isStopToken) {
                    result -= partOfSpeechWeight;
                    count += partOfSpeechWeight;

                    usedTokens.add(relatedToken);
                }
            }
        }

        if (count == 0) {
            return 1;
        }

        return result / count;
    }

    @Override
    public boolean triggered(Expression inputExpression, Expression relatedIntentExpression) {
        return relatedIntentExpression.getTokens().stream().anyMatch(token -> !token.isEntity());
    }

    private List<Token> getTokens(Expression expression, PartOfSpeech partOfSpeech) {
        return expression.getTokens().stream()
                .filter(token -> token != null && !token.getValue().isEmpty())
                .filter(token -> token.getPartOfSpeech() == partOfSpeech)
                .filter(token -> !token.isEntity())
                .collect(Collectors.toList());
    }
}
