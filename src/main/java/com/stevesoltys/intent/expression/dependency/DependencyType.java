package com.stevesoltys.intent.expression.dependency;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author Steve Soltys
 */
@AllArgsConstructor
public enum DependencyType implements Serializable {

    ROOT("root"),

    COMPOUND("compound"),

    DETERMINER("det"),

    COMPOUND_PHRASAL_VERB_PARTICLE("compound:prt"),

    NOMINAL_SUBJECT("nsubj"),

    DIRECT_OBJECT("dobj"),

    RELATIVE_CLAUSE_MODIFIER("acl:relcl"),

    ADJECTIVAL_MODIFIER("amod"),

    NOMINAL_MODIFIER("nmod"),

    POSSESSIVE_NOMINAL_MODIFIER("nmod:poss"),

    NEGATION_MODIFIER("neg"),

    DISCOURSE("discourse"),

    NOUN_COMPOUND_MODIFIER("nn"),

    PHRASAL_VERB_PARTICLE("prt"),

    COPULA("cop"),

    ADVERBIAL_MODIFIER("advmod"),

    AUXILIARY("aux"),

    CASE_MARKING("case"),

    MARKER("mark"),

    OPEN_CLAUSAL_COMPLIMENT("xcomp"),

    ADJECTIVAL_COMPLEMENT("acomp"),

    DEPENDENT("dep"),

    COORDINATION("cc"),

    CONJUNCT("conj"),

    PREPOSITIONAL_MODIFIER("prep"),

    OBJECT_OF_A_PREPOSITION("pobj"),

    NUMBER("number"),

    POSSESSION_MODIFIER("poss"),

    UNKNOWN("");

    private static final long serialVersionUID = 0L;

    @Getter
    private final String identifier;

    public static DependencyType forIdentifier(String identifier) {
        for (DependencyType tag : values()) {

            if (tag.getIdentifier().equalsIgnoreCase(identifier)) {
                return tag;
            }
        }

        return UNKNOWN;
    }
}
