package com.egorpetruchcho.tweetstream.analysis;


import android.util.Log;

import com.egorpetruchcho.tweetstream.model.TweetLikeable;
import com.egorpetruchcho.tweetstream.state.ApplicationSavedState;
import com.egorpetruchcho.twitterstreamingapi.model.Tweet;

import java.util.HashMap;
import java.util.List;

public class NaiveBayes {
    private static NaiveBayes instance = new NaiveBayes();
    private static double BAYES_BORDER = 0.5;
    private HashMap<String, Term> termsMap;

    public Stats stats;

    private NaiveBayes() {
        termsMap = new HashMap<>();
        Term[] terms = FileUtils.getInstance().readTerms();
        for (Term term : terms) {
            termsMap.put(term.getWord(), term);
            Log.d("DEBUGTAG", String.format("[%s = %s]", term.getWord(), term.getOccurrences()));
        }
        stats = getStats();
    }

    public static NaiveBayes getInstance() {
        return instance;
    }

    public void addTweets(List<TweetLikeable> tweets) {
        for (TweetLikeable tweet : tweets) {
            String[] words = parseTweet(tweet.getTweet());
            for (String word : words) {
                if (!StopWords.getInstance().isStopWord(word)) {
                    saveWord(word);
                }
            }
        }

        ApplicationSavedState.getInstance().setCountOfAllTweets(ApplicationSavedState.getInstance().getCountOfAllTweets() + tweets.size());
        flushTerms();
    }

    public void likeTweet(TweetLikeable tweet) {
        String[] words = parseTweet(tweet.getTweet());
        for (String word : words) {
            if (!StopWords.getInstance().isStopWord(word)) {
                likeWord(word);
            }
        }
        ApplicationSavedState.getInstance().setCountOfLikedTweets(ApplicationSavedState.getInstance().getCountOfLikedTweets() + 1);
        flushTerms();
    }

    public void dislikeTweet(TweetLikeable tweet) {
        String[] words = parseTweet(tweet.getTweet());
        for (String word : words) {
            if (!StopWords.getInstance().isStopWord(word)) {
                dislikeWord(word);
            }
        }
        ApplicationSavedState.getInstance().setCountOfLikedTweets(ApplicationSavedState.getInstance().getCountOfLikedTweets() - 1);
        flushTerms();
    }

    private String[] parseTweet(Tweet tweet) {
        String s = tweet.getText();
        String[] words = s.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].replaceAll("[^\\w]", "").toLowerCase();
        }
        return words;
    }

    private void saveWord(String word) {
        Term term = termsMap.get(word);
        if (term == null) {
            term = new Term(word, 1);
            termsMap.put(word, term);
        } else {
            term.setOccurrences(term.getOccurrences() + 1);
        }
    }

    private void likeWord(String word) {
        Term term = termsMap.get(word);
        if (term != null) {
            term.like();
        }
    }

    private void dislikeWord(String word) {
        Term term = termsMap.get(word);
        if (term != null) {
            term.dislike();
        }
    }

    public double calculateNaiveBayes(Tweet tweet) {
        double value = 1.0;
        boolean oneTermFounded = false;
        String[] words = parseTweet(tweet);
        for (String word : words) {
            Term term = termsMap.get(word);
            if (term == null || term.getLikesCount() < 2) {
                continue;
            }
            oneTermFounded = true;
            value *= 1.0 * term.getLikesCount() / term.getOccurrences();
        }
        if (!oneTermFounded) return 0.0;
        return value;
    }

    private void flushTerms() {
        Term[] terms = new Term[termsMap.size()];
        int i = 0;
        for (Term term : termsMap.values()) {
            terms[i++] = term;
        }
        FileUtils.getInstance().saveTerms(terms);
    }

    public void updateStats(List<TweetLikeable> tweets) {
        int liked = 0;
        for (TweetLikeable tweet : tweets) {
            if (tweet.isLiked()) liked++;
            double bayesValue = calculateNaiveBayes(tweet.getTweet());
            if (tweet.isLiked()) {
                if (bayesValue >= BAYES_BORDER) {
                    stats.incTP();
                } else {
                    stats.incFP();
                }
            } else {
                if (bayesValue >= BAYES_BORDER) {
                    stats.incFN();
                } else {
                    stats.incTN();
                }
            }
        }
        stats.addPoint((int) (100.0 * liked / tweets.size()), ApplicationSavedState.getInstance().getCountOfAllTweets(), ApplicationSavedState.getInstance().getCountOfLikedTweets());
        FileUtils.getInstance().saveStats(stats);
    }

    public void clear() {
        termsMap.clear();
        FileUtils.getInstance().saveTerms(new Term[0]);
        stats.clear();
        ApplicationSavedState.getInstance().setCountOfAllTweets(0);
        ApplicationSavedState.getInstance().setCountOfLikedTweets(0);
        FileUtils.getInstance().saveStats(stats);
    }

    private Stats getStats() {
        return FileUtils.getInstance().readStats();
    }
}
