package com.stevesoltys.intent;

import java.util.Optional;
import java.util.Set;

/**
 * @author Steve Soltys
 */
public interface IntentRepository {

    Optional<Intent> getIntentByIdentifier(String identifier);

    Set<Intent> getIntents();
}
