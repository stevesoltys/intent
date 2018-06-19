package com.stevesoltys.intent.classifier.metric;

import com.stevesoltys.intent.expression.Expression;
import com.stevesoltys.intent.expression.dependency.Dependency;
import com.stevesoltys.intent.expression.dependency.DependencyType;
import com.stevesoltys.intent.expression.token.matcher.TokenMatcher;
import com.stevesoltys.intent.classifier.DefaultIntentClassifierConfiguration;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Steve Soltys
 */
public class DependencyMetric implements Metric {

    private final DefaultIntentClassifierConfiguration configuration;

    private final TokenMatcher tokenMatcher;

    public DependencyMetric(DefaultIntentClassifierConfiguration configuration) {
        this.configuration = configuration;

        tokenMatcher = configuration.getTokenMatcher();
    }

    public double getWeight(Expression inputExpression, Expression relatedIntentExpression) {
        double result = 0;
        double count = 0;
        Set<Dependency> usedDependencies = new HashSet<>();

        for (Dependency dependency : relatedIntentExpression.getDependencies()) {
            double dependencyWeight = getDependencyWeight(dependency.getDependencyType());

            if (!usedDependencies.contains(dependency)) {

                if (inputExpression.getDependencies().stream().anyMatch(inputDependency ->
                        inputDependency.equals(dependency))) {
                    result++;
                    count++;

                } else if (hasSimilarDependency(inputExpression, dependency)) {
                    result += dependencyWeight;
                    count += dependencyWeight;

                } else {
                    result -= dependencyWeight;
                    count += dependencyWeight;
                }

                usedDependencies.add(dependency);
            }
        }

        if (count < 1) {
            count = 1;
        }

        return result / count;
    }

    @Override
    public boolean triggered(Expression inputExpression, Expression relatedIntentExpression) {
        return !relatedIntentExpression.hasEntity();
    }

    private boolean hasSimilarDependency(Expression expression, Dependency dependency) {
        DependencyType dependencyType = dependency.getDependencyType();

        for (Dependency expDependency : expression.getDependencies()) {
            DependencyType expDependencyType = expDependency.getDependencyType();

            if (dependencyType == expDependencyType) {
                boolean governorMatch = tokenMatcher.matches(dependency.getGovernor(), expDependency.getGovernor());
                boolean dependentMatch = tokenMatcher.matches(dependency.getDependent(), expDependency.getDependent());

                return governorMatch || dependentMatch;
            }
        }

        return false;
    }

    private double getDependencyWeight(DependencyType dependencyType) {

        if (!configuration.getDependencyWeights().containsKey(dependencyType)) {
            return 0;
        }

        return configuration.getDependencyWeights().get(dependencyType);
    }
}
