package com.stevesoltys.intent.expression.token.semantic;

import com.stevesoltys.intent.expression.token.pos.PartOfSpeech;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author Steve Soltys
 */
public class DefaultSemanticHeuristic implements SemanticHeuristic {

    @Override
    public double compare(Pair<PartOfSpeech, String> first, Pair<PartOfSpeech, String> second) {
        return first.getLeft().equals(second.getLeft()) &&
                first.getRight().equalsIgnoreCase(second.getRight()) ? 1 : 0;
    }
}
