package com.egorpetruchcho.tweetstream.analysis;


import java.util.ArrayList;
import java.util.List;

public class Stats {

    private List<Integer> percentages;
    private List<Integer> countOfAllTweets;
    private List<Integer> countOfLikedTweets;
    private List<Double> F1;

    private int TP, TN, FP, FN;

    Stats() {
        percentages = new ArrayList<>();
        countOfAllTweets = new ArrayList<>();
        countOfLikedTweets = new ArrayList<>();

        F1 = new ArrayList<>();
    }

    public void incTP() {
        TP++;
    }

    public void incTN() {
        TN++;
    }

    public void incFP() {
        FP++;
    }

    public void incFN() {
        FN++;
    }

    public void addPoint(int percentage, int countAll, int countLiked) {
        percentages.add(percentage);
        countOfAllTweets.add(countAll);
        countOfLikedTweets.add(countLiked);

        double precision = 1.0 * TP / (TP + FP);
        double recall = 1.0 * TP / (TP + FN);
        double f1 = 2 * precision * recall / (precision + recall);
        F1.add(f1);
    }

    public List<Double> getF1() {
        return F1;
    }

    public void clear() {
        percentages.clear();
        countOfAllTweets.clear();
        countOfLikedTweets.clear();
        TP = TN = FN = FP = 0;
    }

    public List<Integer> getPercentages() {
        return percentages;
    }

    public List<Integer> getCountOfAllTweets() {
        return countOfAllTweets;
    }

    public List<Integer> getCountOfLikedTweets() {
        return countOfLikedTweets;
    }
}
