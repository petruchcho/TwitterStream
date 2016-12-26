package com.egorpetruchcho.tweetstream.analysis;


public class StatsPoint {
    private final int countOfLikedWords, percentage;

    public StatsPoint(int countOfLikedWords, int percentage) {
        this.countOfLikedWords = countOfLikedWords;
        this.percentage = percentage;
    }

    public int getCountOfLikedWords() {
        return countOfLikedWords;
    }

    public int getPercentage() {
        return percentage;
    }
}
