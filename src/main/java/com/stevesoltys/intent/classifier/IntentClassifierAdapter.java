package com.stevesoltys.intent.classifier;

import com.stevesoltys.intent.IntentClassifier;
import com.stevesoltys.intent.IntentRepository;

/**
 * @author Steve Soltys
 */
public abstract class IntentClassifierAdapter implements IntentClassifier {

    private final IntentRepository intentRepository;

    public IntentClassifierAdapter(IntentRepository intentRepository) {
        this.intentRepository = intentRepository;
    }

    public IntentRepository getIntentRepository() {
        return intentRepository;
    }
}
