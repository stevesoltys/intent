package com.stevesoltys.intent;

/**
 * @author Steve Soltys
 */
public interface IntentClassifier {

    IntentClassification classify(IntentContext context, String input);

    IntentClassification classify(String input);
}
