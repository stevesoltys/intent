package com.stevesoltys.intent.classifier;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.stevesoltys.intent.entity.EntityTagger;
import com.stevesoltys.intent.entity.tagger.DefaultEntityTagger;
import com.stevesoltys.intent.expression.ExpressionFactory;
import com.stevesoltys.intent.expression.dependency.DependencyType;
import com.stevesoltys.intent.expression.token.matcher.DefaultTokenMatcher;
import com.stevesoltys.intent.expression.token.matcher.TokenMatcher;
import com.stevesoltys.intent.expression.token.pos.PartOfSpeech;
import com.stevesoltys.intent.expression.token.semantic.DefaultSemanticHeuristic;
import com.stevesoltys.intent.IntentRepository;
import com.stevesoltys.intent.classifier.metric.*;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Steve Soltys
 */
public class DefaultIntentClassifierConfiguration {

    private static final Set<String> DEFAULT_STOP_WORDS = ImmutableSet.of("a", "about", "above", "after", "again",
            "against", "all", "am", "an", "and", "any", "are", "aren't", "as", "at", "be", "because", "been", "before",
            "being", "below", "between", "both", "but", "by", "can't", "cannot", "could", "couldn't", "did", "didn't",
            "do", "does", "doesn't", "doing", "don't", "down", "during", "each", "few", "for", "from", "further", "had",
            "hadn't", "has", "hasn't", "have", "haven't", "having", "he", "he'd", "he'll", "he's", "her", "here",
            "here's", "hers", "herself", "him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've",
            "if", "in", "into", "is", "isn't", "it", "it's", "its", "itself", "let's", "me", "more", "most", "mustn't",
            "my", "myself", "no", "nor", "not", "of", "once", "only", "or", "other", "ought", "our", "ours",
            "ourselves", "out", "over", "own", "same", "shan't", "she", "she'd", "she'll", "she's", "should",
            "shouldn't", "so", "some", "such", "than", "that", "that's", "the", "their", "theirs", "them",
            "themselves", "then", "there", "there's", "these", "they", "they'd", "they'll", "they're", "they've",
            "this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "wasn't", "we", "we'd",
            "we'll", "we're", "we've", "were", "weren't", "what", "what's", "when", "when's", "where", "where's",
            "which", "while", "who", "who's", "whom", "why", "why's", "with", "won't", "would", "wouldn't", "you",
            "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves", "'s", "'ll", "'d", "'re",
            "'ve", "'m", ".", "'", "\"", "?", "!");

    private static final Map<PartOfSpeech, Double> DEFAULT_PART_OF_SPEECH_WEIGHTS =
            ImmutableMap.<PartOfSpeech, Double>builder()
                    .put(PartOfSpeech.UNKNOWN, 1.0)
                    .put(PartOfSpeech.INTERJECTION, 0.9)
                    .put(PartOfSpeech.NOUN, 0.75)
                    .put(PartOfSpeech.PRONOUN, 0.75)
                    .put(PartOfSpeech.VERB, 0.55)
                    .put(PartOfSpeech.ADVERB, 0.55)
                    .put(PartOfSpeech.PREPOSITION, 0.4)
                    .put(PartOfSpeech.ADJECTIVE, 0.3)
                    .put(PartOfSpeech.CARDINAL_NUMBER, 0.3)
                    .put(PartOfSpeech.COORDINATING_CONJUNCTION, 0.1)
                    .build();

    private static final Map<DependencyType, Double> DEFAULT_DEPENDENCY_WEIGHTS =
            ImmutableMap.<DependencyType, Double>builder()
                    .put(DependencyType.ROOT, 1.0)
                    .put(DependencyType.COMPOUND, 0.85)
                    .put(DependencyType.DETERMINER, 0.85)
                    .put(DependencyType.DIRECT_OBJECT, 0.75)
                    .put(DependencyType.NOMINAL_SUBJECT, 0.75)
                    .put(DependencyType.COMPOUND_PHRASAL_VERB_PARTICLE, 0.75)
                    .put(DependencyType.RELATIVE_CLAUSE_MODIFIER, 0.75)
                    .put(DependencyType.NOUN_COMPOUND_MODIFIER, 0.75)
                    .put(DependencyType.ADJECTIVAL_MODIFIER, 0.45)
                    .put(DependencyType.NOMINAL_MODIFIER, 0.45)
                    .put(DependencyType.PHRASAL_VERB_PARTICLE, 0.45)
                    .put(DependencyType.POSSESSIVE_NOMINAL_MODIFIER, 0.45)
                    .put(DependencyType.POSSESSION_MODIFIER, 0.45)
                    .put(DependencyType.NEGATION_MODIFIER, 0.45)
                    .put(DependencyType.DISCOURSE, 0.40)
                    .put(DependencyType.COPULA, 0.40)
                    .put(DependencyType.ADVERBIAL_MODIFIER, 0.40)
                    .put(DependencyType.AUXILIARY, 0.35)
                    .put(DependencyType.CASE_MARKING, 0.35)
                    .put(DependencyType.MARKER, 0.35)
                    .put(DependencyType.CONJUNCT, 0.35)
                    .put(DependencyType.NUMBER, 0.35)
                    .put(DependencyType.OPEN_CLAUSAL_COMPLIMENT, 0.1)
                    .put(DependencyType.ADJECTIVAL_COMPLEMENT, 0.1)
                    .put(DependencyType.COORDINATION, 0.1)
                    .put(DependencyType.PREPOSITIONAL_MODIFIER, 0.1)
                    .put(DependencyType.OBJECT_OF_A_PREPOSITION, 0.1)
                    .build();

    @Getter
    private final IntentRepository intentRepository;

    @Getter
    private final ExpressionFactory expressionFactory;

    @Getter
    private final EntityTagger entityTagger;

    @Getter
    private final Set<Metric> metrics;

    @Getter
    private final TokenMatcher tokenMatcher;

    @Getter
    private Set<String> stopWords;

    @Getter
    private Map<PartOfSpeech, Double> partOfSpeechWeights;

    @Getter
    private Map<DependencyType, Double> dependencyWeights;

    @Builder
    @SneakyThrows
    public DefaultIntentClassifierConfiguration(@NonNull IntentRepository intentRepository,
                                                @NonNull ExpressionFactory expressionFactory,
                                                EntityTagger entityTagger, TokenMatcher tokenMatcher,
                                                Set<Metric> metrics, Set<String> stopWords,
                                                Map<PartOfSpeech, Double> partOfSpeechWeights,
                                                Map<DependencyType, Double> dependencyWeights) {
        this.intentRepository = intentRepository;
        this.expressionFactory = expressionFactory;

        if (tokenMatcher == null) {
            tokenMatcher = new DefaultTokenMatcher(new NormalizedLevenshtein(), new DefaultSemanticHeuristic());
        }

        if (entityTagger == null) {
            entityTagger = new DefaultEntityTagger(tokenMatcher);
        }

        this.entityTagger = entityTagger;
        this.tokenMatcher = tokenMatcher;

        if(stopWords == null) {
            stopWords = DEFAULT_STOP_WORDS;
        }

        this.stopWords = stopWords;

        if(partOfSpeechWeights == null) {
            partOfSpeechWeights = DEFAULT_PART_OF_SPEECH_WEIGHTS;
        }

        this.partOfSpeechWeights = partOfSpeechWeights;

        if(dependencyWeights == null) {
            dependencyWeights = DEFAULT_DEPENDENCY_WEIGHTS;
        }

        this.dependencyWeights = dependencyWeights;

        if (metrics == null || metrics.isEmpty()) {
            metrics = new HashSet<>();

            metrics.add(new DependencyMetric(this));
            metrics.add(new PartOfSpeechMetric(this));
            metrics.add(new QuadGramMetric(this));
            metrics.add(new EntityExtractionMetric(this));
            metrics.add(new ExactMatchMetric());
            metrics.add(new SimilarityMetric());
        }

        this.metrics = metrics;
    }
}
