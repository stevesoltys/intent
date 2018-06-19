package com.stevesoltys.intent.expression.token.matcher;

import com.stevesoltys.intent.entity.Entity;
import com.stevesoltys.intent.expression.token.EntityToken;
import com.stevesoltys.intent.expression.token.Token;
import com.stevesoltys.intent.expression.token.semantic.SemanticHeuristic;
import info.debatty.java.stringsimilarity.interfaces.NormalizedStringDistance;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author Steve Soltys
 */
public class DefaultTokenMatcher implements TokenMatcher {

    private final NormalizedStringDistance distanceMetric;

    private final SemanticHeuristic semanticHeuristic;

    public DefaultTokenMatcher(NormalizedStringDistance distanceMetric, SemanticHeuristic semanticHeuristic) {
        this.distanceMetric = distanceMetric;
        this.semanticHeuristic = semanticHeuristic;
    }

    @Override
    public boolean matches(Token similarToken, Token inputToken) {

        if (similarToken instanceof EntityToken) {
            return ((EntityToken) similarToken).getEntity().getPattern().matcher(inputToken.getValue()).matches();
        }

        return matches(similarToken.getValue(), inputToken.getValue()) ||
                semanticHeuristic.compare(Pair.of(similarToken.getPartOfSpeech(), similarToken.getValue()),
                        Pair.of(inputToken.getPartOfSpeech(), inputToken.getValue())) > 0.8;
    }

    @Override
    public boolean matches(Token similarToken, String inputToken) {

        if (similarToken instanceof EntityToken) {
            return ((EntityToken) similarToken).getEntity().getPattern().matcher(inputToken).matches();
        }

        return matches(similarToken.getValue(), inputToken);
    }

    @Override
    public boolean matches(Entity entity, String inputToken) {
        return entity.getPattern().matcher(inputToken).matches();
    }

    private boolean matches(String similarToken, String inputToken) {
        return distanceMetric.distance(sanitize(similarToken), sanitize(inputToken)) <= 0.15;
    }

    private String sanitize(String token) {
        return token.replaceAll("[\\p{Punct}]+", "");
    }
}
