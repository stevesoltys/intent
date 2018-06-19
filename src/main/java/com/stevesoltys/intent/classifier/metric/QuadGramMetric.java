package com.stevesoltys.intent.classifier.metric;

import com.stevesoltys.intent.expression.Expression;
import com.stevesoltys.intent.expression.token.ExpressionToken;
import com.stevesoltys.intent.expression.token.Token;
import com.stevesoltys.intent.expression.token.pos.PartOfSpeech;
import com.stevesoltys.intent.expression.token.matcher.TokenMatcher;
import com.stevesoltys.intent.classifier.DefaultIntentClassifierConfiguration;

import java.util.*;

/**
 * @author Steve Soltys
 */
public class QuadGramMetric implements Metric {

    private final DefaultIntentClassifierConfiguration configuration;

    private final TokenMatcher matcher;

    public QuadGramMetric(DefaultIntentClassifierConfiguration configuration) {
        this.configuration = configuration;

        matcher = configuration.getTokenMatcher();
    }

    @Override
    public double getWeight(Expression inputExpression, Expression relatedIntentExpression) {
        double result = 0;
        double count = 0;

        List<List<Token>> inputQuads = getQuads(inputExpression);
        Set<List<Token>> usedQuads = new HashSet<>();

        for (List<Token> quad : getQuads(relatedIntentExpression)) {

            if (usedQuads.contains(quad)) {
                continue;
            }

            Token first = quad.get(0);
            Token second = quad.get(1);
            Token third = quad.get(2);
            Token fourth = quad.get(3);

            double weight = getQuadWeight(first, second, third, fourth);

            if (inputQuads.stream().anyMatch(inputQuad -> matches(inputQuad, quad))) {
                result += weight;

            } else {
                boolean trigramMatch = inputQuads.stream().anyMatch(inputQuad ->
                        containsSimilarTrigram(inputQuad, first, second, third) ||
                                containsSimilarTrigram(inputQuad, second, third, fourth));

                boolean bigramMatch = inputQuads.stream().anyMatch(inputQuad ->
                        containsSimilarBigram(inputQuad, first, second) ||
                                containsSimilarBigram(inputQuad, second, third) ||
                                containsSimilarBigram(inputQuad, third, fourth));

                if (trigramMatch) {
                    result += weight;

                } else if (bigramMatch) {
                    result += weight / 2;
                }
            }

            usedQuads.add(quad);
            count += weight;
        }

        if (count == 0) {
            return 1;
        }

        return result / count;
    }

    @Override
    public boolean triggered(Expression inputExpression, Expression relatedIntentExpression) {
        return relatedIntentExpression.getTokens().stream().noneMatch(Token::isEntity);
    }

    private List<List<Token>> getQuads(Expression expression) {
        List<List<Token>> result = new LinkedList<>();

        for (int index = 0; index == 0 || index < expression.getTokens().size() - 4; index++) {
            result.add(getQuad(expression, index));
        }

        return result;
    }

    private List<Token> getQuad(Expression expression, int index) {
        Token first = new ExpressionToken(""), second = new ExpressionToken(""),
                third = new ExpressionToken(""), fourth = new ExpressionToken("");

        if (index < expression.getTokens().size()) {
            first = expression.getTokens().get(index);
        }

        if (index + 1 < expression.getTokens().size()) {
            second = expression.getTokens().get(index + 1);
        }

        if (index + 2 < expression.getTokens().size()) {
            third = expression.getTokens().get(index + 2);
        }

        if (index + 3 < expression.getTokens().size()) {
            fourth = expression.getTokens().get(index + 3);
        }

        return Arrays.asList(first, second, third, fourth);
    }

    private double getQuadWeight(Token first, Token second, Token third, Token fourth) {
        double result = 0;
        double count = 0;

        if (configuration.getStopWords().stream().noneMatch(word -> word.equalsIgnoreCase(first.getValue())) &&
                !first.isEntity() && !second.getValue().isEmpty()) {
            double partOfSpeechWeight = getPartOfSpeechWeight(first.getPartOfSpeech());

            result += partOfSpeechWeight;
            count += partOfSpeechWeight;
        }

        if (configuration.getStopWords().stream().noneMatch(word -> word.equalsIgnoreCase(second.getValue())) &&
                !second.isEntity() && !second.getValue().isEmpty()) {
            double partOfSpeechWeight = getPartOfSpeechWeight(second.getPartOfSpeech());

            result += partOfSpeechWeight;
            count += partOfSpeechWeight;
        }

        if (configuration.getStopWords().stream().noneMatch(word -> word.equalsIgnoreCase(third.getValue())) &&
                !third.isEntity() && !third.getValue().isEmpty()) {
            double partOfSpeechWeight = getPartOfSpeechWeight(third.getPartOfSpeech());

            result += partOfSpeechWeight;
            count += partOfSpeechWeight;
        }

        if (configuration.getStopWords().stream().noneMatch(word -> word.equalsIgnoreCase(fourth.getValue())) &&
                !fourth.isEntity() && !fourth.getValue().isEmpty()) {
            double partOfSpeechWeight = getPartOfSpeechWeight(fourth.getPartOfSpeech());

            result += partOfSpeechWeight;
            count += partOfSpeechWeight;
        }

        if (count == 0) {
            return 0;

        } else if (result < 1) {
            return 0;
        }

        return result / count;
    }

    private boolean containsSimilarTrigram(List<Token> inputQuad, Token first, Token second, Token third) {

        if (first.getValue().isEmpty() && second.getValue().isEmpty() && third.getValue().isEmpty()) {
            return false;

        } else if (first.isEntity() || second.isEntity() || third.isEntity()) {
            return false;
        }

        boolean firstThreeMatch = matcher.matches(first, inputQuad.get(0)) &&
                matcher.matches(second, inputQuad.get(1)) &&
                matcher.matches(third, inputQuad.get(2));

        boolean lastThreeMatch = matcher.matches(first, inputQuad.get(1)) &&
                matcher.matches(second, inputQuad.get(2)) &&
                matcher.matches(third, inputQuad.get(3));

        return firstThreeMatch || lastThreeMatch;
    }

    private boolean containsSimilarBigram(List<Token> inputQuad, Token first, Token second) {

        if (first.getValue().isEmpty() && second.getValue().isEmpty()) {
            return false;

        } else if (first.isEntity() || second.isEntity()) {
            return false;
        }

        boolean firstTwoMatch = matcher.matches(first, inputQuad.get(0)) &&
                matcher.matches(second, inputQuad.get(1));

        boolean centerTwoMatch = matcher.matches(first, inputQuad.get(1)) &&
                matcher.matches(second, inputQuad.get(2));

        boolean lastTwoMatch = matcher.matches(first, inputQuad.get(2)) &&
                matcher.matches(second, inputQuad.get(3));

        return firstTwoMatch || centerTwoMatch || lastTwoMatch;
    }

    private boolean matches(List<Token> inputQuad, List<Token> similarQuad) {
        return matcher.matches(similarQuad.get(0), inputQuad.get(0)) &&
                matcher.matches(similarQuad.get(1), inputQuad.get(1)) &&
                matcher.matches(similarQuad.get(2), inputQuad.get(2)) &&
                matcher.matches(similarQuad.get(3), inputQuad.get(3));
    }

    private double getPartOfSpeechWeight(PartOfSpeech partOfSpeech) {
        return configuration.getPartOfSpeechWeights().getOrDefault(partOfSpeech, 0d);
    }
}
