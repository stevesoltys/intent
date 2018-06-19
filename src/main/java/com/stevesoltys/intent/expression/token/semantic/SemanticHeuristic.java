package com.stevesoltys.intent.expression.token.semantic;

import com.stevesoltys.intent.expression.token.pos.PartOfSpeech;
import org.apache.commons.lang3.tuple.Pair;

public interface SemanticHeuristic {

    double compare(Pair<PartOfSpeech, String> first, Pair<PartOfSpeech, String> second);
}
