package com.stevesoltys.intent.expression;

import com.stevesoltys.intent.expression.dependency.Dependency;
import com.stevesoltys.intent.expression.token.ExpressionToken;
import com.stevesoltys.intent.expression.token.Token;
import lombok.Getter;
import opennlp.tools.tokenize.WhitespaceTokenizer;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Steve Soltys
 */
public class Expression implements Serializable {

    private static final long serialVersionUID = 0L;

    @Getter
    private final String expression;

    @Getter
    private final String expressionPattern;

    @Getter
    private final List<Token> tokens;

    @Getter
    private final List<Dependency> dependencies;

    public Expression(String expression) {
        this(expression, new LinkedList<>());
    }

    public Expression(String expression, List<Dependency> dependencies) {
        this.expression = Pattern.quote(expression);
        this.dependencies = dependencies;

        expressionPattern = Pattern.quote(expression);
        tokens = buildTokens();
    }

    public Expression(List<Token> tokens) {
        this(tokens, new LinkedList<>());
    }

    public Expression(List<Token> tokens, List<Dependency> dependencies) {
        this.tokens = tokens;
        this.dependencies = dependencies;

        expression = String.join(" ", tokens.stream()
                .map(Token::getValue)
                .collect(Collectors.toList()));

        expressionPattern = String.join(" ", tokens.stream()
                .map(token -> token.isEntity() ? token.getValue() : Pattern.quote(token.getValue()))
                .collect(Collectors.toList()));
    }

    public static ExpressionBuilder builder() {
        return new ExpressionBuilder();
    }

    private List<Token> buildTokens() {
        return Arrays.stream(WhitespaceTokenizer.INSTANCE.tokenize(expression))
                .map(ExpressionToken::new)
                .collect(Collectors.toList());
    }

    public boolean hasEntity() {
        return tokens.stream().anyMatch(Token::isEntity);
    }
}
