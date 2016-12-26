package com.egorpetruchcho.tweetstream.analysis;


public class Term {
    private final String word;
    private int occurrences;
    private int likesCount;

    public Term(String word, int occurences) {
        this.word = word;
        this.occurrences = occurences;
    }

    public String getWord() {
        return word;
    }

    public int getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(int occurrences) {
        this.occurrences = occurrences;
    }

    public void like() {
        likesCount++;
    }

    public void dislike() {
        likesCount--;
    }

    public int getLikesCount() {
        return likesCount;
    }
}
