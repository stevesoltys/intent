package com.stevesoltys.intent.expression.token.pos;

import lombok.Getter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author Steve Soltys
 */
public enum PartOfSpeech implements Serializable {

    INTERJECTION("UH"),

    NOUN("NN", "NNS", "NNP", "NNPS"),

    PRONOUN("PRP", "PRP$", "WP"),

    ADVERB("RB", "RBR", "RBS"),

    VERB("VB", "VBD", "VBP", "VBG", "VBN", "VBP", "VBZ"),

    ADJECTIVE("JJ"),

    CARDINAL_NUMBER("CD"),

    PREPOSITION("IN"),

    COORDINATING_CONJUNCTION("CC"),

    PUNCTUATION("."),

    UNKNOWN();

    private static final long serialVersionUID = 0L;

    @Getter
    private final List<String> identifiers;

    PartOfSpeech(String... identifiers) {
        this.identifiers = Arrays.asList(identifiers);
    }

    public static PartOfSpeech parseIdentifier(String identifier) {

        for (PartOfSpeech tag : values()) {
            if (tag.getIdentifiers().contains(identifier)) {
                return tag;
            }
        }

        return UNKNOWN;
    }
}
