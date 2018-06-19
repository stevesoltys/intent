package com.stevesoltys.intent.entity.tagger;

import com.stevesoltys.intent.entity.Entity;
import com.stevesoltys.intent.entity.EntityTag;
import com.stevesoltys.intent.entity.EntityTagger;
import com.stevesoltys.intent.entity.system.WildcardEntity;
import com.stevesoltys.intent.expression.Expression;
import com.stevesoltys.intent.expression.token.EntityToken;
import com.stevesoltys.intent.expression.token.Token;
import com.stevesoltys.intent.expression.token.matcher.TokenMatcher;
import lombok.AllArgsConstructor;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.Span;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * The default entity tagger implementation.
 *
 * @author Steve Soltys
 */
@AllArgsConstructor
public class DefaultEntityTagger implements EntityTagger {

    private final TokenMatcher tokenMatcher;

    @Override
    public List<EntityTag> tag(Expression matchedExpression, Expression inputExpression) {
        List<EntityTag> results = new LinkedList<>();

        if (matchedExpression == null || inputExpression == null) {
            return results;
        }

        int inputIndex = 0;

        for (int matchedIndex = 0; matchedIndex < matchedExpression.getTokens().size(); matchedIndex++) {
            Token matchedToken = matchedExpression.getTokens().get(matchedIndex);

            if (!matchedToken.isEntity()) {
                continue;
            }

            List<Span> inputSpans = Arrays.asList(
                    SimpleTokenizer.INSTANCE.tokenizePos(inputExpression.getExpression()));
            if (matchedIndex > 0) {
                Token previousMatchedToken = matchedExpression.getTokens().get(matchedIndex - 1);

                if(!previousMatchedToken.isEntity()) {
                    while (inputIndex < inputSpans.size()) {
                        Span inputSpan = inputSpans.get(inputIndex++);
                        String inputToken = inputExpression.getExpression()
                                .substring(inputSpan.getStart(), inputSpan.getEnd());

                        if (tokenMatcher.matches(previousMatchedToken, inputToken)) {
                            break;
                        }
                    }
                }
            }

            Pair<EntityTag, Integer> taggedEntity = tagEntity(matchedExpression, inputExpression, matchedIndex,
                    inputIndex);

            results.add(taggedEntity.getLeft());
            inputIndex = taggedEntity.getRight();
        }

        return results;
    }

    private Pair<EntityTag, Integer> tagEntity(Expression matchedExpression, Expression inputExpression,
                                               int matchedIndex, int currentIndex) {

        List<Span> inputSpans = Arrays.asList(
                SimpleTokenizer.INSTANCE.tokenizePos(inputExpression.getExpression()));

        EntityToken matchedToken = (EntityToken) matchedExpression.getTokens().get(matchedIndex);
        int originalInputIndex = currentIndex;

        Entity entity = matchedToken.getEntity();
        String entityIdentifier = matchedToken.getIdentifier();

        Optional<Token> nextTokenOptional = Optional.empty();

        if (matchedIndex + 1 < matchedExpression.getTokens().size()) {
            nextTokenOptional = Optional.of(matchedExpression.getTokens().get(matchedIndex + 1));
        }

        List<Span> resultSpans = new LinkedList<>();
        int lastMatchedIndex = originalInputIndex;

        while (currentIndex < inputSpans.size()) {

            if (!(entity instanceof WildcardEntity)) {
                List<Span> currentSpans = inputSpans.subList(originalInputIndex, currentIndex + 1);

                String matchedEntity = buildMatchedEntity(inputExpression.getExpression(), currentSpans);

                if(tokenMatcher.matches(entity, matchedEntity)) {
                    resultSpans = currentSpans;
                    lastMatchedIndex = currentIndex + 1;
                }
            }

            if (entity instanceof WildcardEntity) {
                Span inputSpan = inputSpans.get(currentIndex);
                String inputToken = inputExpression.getExpression()
                        .substring(inputSpan.getStart(), inputSpan.getEnd());

                if(nextTokenOptional.isPresent()) {
                    Token nextToken = nextTokenOptional.get();

                    if (nextToken.isEntity()) {
                        if (!tagEntity(matchedExpression, inputExpression, matchedIndex + 1, currentIndex).getLeft()
                                .getValue().isEmpty()) {
                            break;
                        }

                    } else {
                        boolean lastMatch = inputSpans.subList(currentIndex + 1, inputSpans.size()).stream()
                                .noneMatch(tmpSpan -> {
                                    String tmpToken = inputExpression.getExpression()
                                            .substring(tmpSpan.getStart(), tmpSpan.getEnd());

                                    return tokenMatcher.matches(nextToken, tmpToken);
                                });

                        if (tokenMatcher.matches(nextToken, inputToken) && lastMatch) {
                            break;
                        }
                    }
                }

                resultSpans.add(inputSpan);
                lastMatchedIndex++;
            }

            currentIndex++;
        }

        String matchedEntity = buildMatchedEntity(inputExpression.getExpression(), resultSpans);

        if (!tokenMatcher.matches(entity, matchedEntity)) {
            matchedEntity = "";
            lastMatchedIndex = originalInputIndex;
        }

        return Pair.of(new EntityTag(entity, entityIdentifier, matchedEntity), lastMatchedIndex);
    }

    private String buildMatchedEntity(String expression, List<Span> tokens) {
        StringBuilder result = new StringBuilder();
        int lastEnd = -1;

        for (Span tokenSpan : tokens) {


            for(int i = 0; lastEnd > 0 && i < tokenSpan.getStart() - lastEnd; i++) {
                result.append(" ");
            }

            result.append(expression, tokenSpan.getStart(), tokenSpan.getEnd());
            lastEnd = tokenSpan.getEnd();
        }

        return result.toString();
    }
}
