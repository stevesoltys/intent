package com.stevesoltys.intent.repository;

import com.stevesoltys.intent.Intent;
import com.stevesoltys.intent.IntentRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@NoArgsConstructor
public class InMemoryIntentRepository implements IntentRepository {

    @Getter
    private final Set<Intent> intents = new HashSet<>();

    public Optional<Intent> getIntentByIdentifier(String identifier) {
        return intents.stream().filter(intent -> intent.getIdentifier().equals(identifier)).findAny();
    }

    public boolean register(Intent intent) {
        return intents.add(intent);
    }

}
