package com.egorpetruchcho.tweetstream.analysis;


import android.util.Log;

import com.egorpetruchcho.tweetstream.model.TweetLikeable;
import com.egorpetruchcho.twitterstreamingapi.model.Tweet;

import java.util.HashMap;
import java.util.List;

public class NaiveBayes {
    private static NaiveBayes instance = new NaiveBayes();
    private HashMap<String, Term> termsMap;

    private int countOfAllWords;
    private int countOfLikedWords;

    private NaiveBayes() {
        termsMap = new HashMap<>();
        Term[] terms = FileUtils.getInstance().readTerms();
        for (Term term : terms) {
            termsMap.put(term.getWord(), term);
            countOfAllWords += term.getOccurrences();
            countOfLikedWords += term.getLikesCount();
            Log.d("DEBUGTAG", String.format("[%s = %s]", term.getWord(), term.getOccurrences()));
        }
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
        flush();
    }

    public void likeTweet(TweetLikeable tweet) {
        String[] words = parseTweet(tweet.getTweet());
        for (String word : words) {
            if (!StopWords.getInstance().isStopWord(word)) {
                likeWord(word);
            }
        }
        flush();
    }

    public void dislikeTweet(TweetLikeable tweet) {
        String[] words = parseTweet(tweet.getTweet());
        for (String word : words) {
            if (!StopWords.getInstance().isStopWord(word)) {
                dislikeWord(word);
            }
        }
        flush();
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
        countOfAllWords++;
    }

    private void likeWord(String word) {
        Term term = termsMap.get(word);
        if (term != null) {
            term.like();
            countOfLikedWords++;
        }
    }

    private void dislikeWord(String word) {
        Term term = termsMap.get(word);
        if (term != null) {
            term.dislike();
            countOfLikedWords--;
        }
    }

    public double calculateNaiveBayes(Tweet tweet) {
        double value = 100.0;
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

    private void flush() {
        Term[] terms = new Term[termsMap.size()];
        int i = 0;
        for (Term term : termsMap.values()) {
            terms[i++] = term;
        }
        FileUtils.getInstance().saveTerms(terms);
    }

    public void clear() {
        termsMap.clear();
        FileUtils.getInstance().clear();
        countOfAllWords = 0;
        countOfLikedWords = 0;
    }

    public void saveStats(int percentage) {
        FileUtils.getInstance().saveStats(countOfLikedWords, percentage);
    }
}
