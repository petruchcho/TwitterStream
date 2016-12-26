package com.egorpetruchcho.tweetstream.analysis;


import android.content.Context;

import com.egorpetruchcho.tweetstream.core.TweetsApplication;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtils {
    private static FileUtils instance = new FileUtils();
    private static Gson gson = new Gson();

    private static final String TERMS_FILENAME = "myfile.txt";
    private static final String STATS_FILENAME = "stats.txt";

    private FileUtils() {
    }

    public static FileUtils getInstance() {
        return instance;
    }

    public Term[] readTerms() {
        Context context = TweetsApplication.getInstance();
        FileInputStream fis;
        try {
            fis = context.openFileInput(TERMS_FILENAME);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new Term[0];
        }
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader bufferedReader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Term[0];
        }

        String json = sb.toString();
        return gson.fromJson(json, Term[].class);
    }

    public void saveTerms(Term[] terms) {
        String s = gson.toJson(terms);

        FileOutputStream outputStream;

        try {
            outputStream = TweetsApplication.getInstance().openFileOutput(TERMS_FILENAME, Context.MODE_PRIVATE);
            outputStream.write(s.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public StatsPoint[] readStats() {
        Context context = TweetsApplication.getInstance();
        FileInputStream fis;
        try {
            fis = context.openFileInput(STATS_FILENAME);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new StatsPoint[0];
        }
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader bufferedReader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new StatsPoint[0];
        }

        String json = sb.toString();
        return gson.fromJson(json, StatsPoint[].class);
    }

    public void saveStats(int countOfLikedWords, int goodFeedPercentage) {
        StatsPoint[] alreadyCollected = readStats();
        StatsPoint[] withNewOne = new StatsPoint[alreadyCollected.length + 1];
        System.arraycopy(alreadyCollected, 0, withNewOne, 0, alreadyCollected.length);
        withNewOne[withNewOne.length - 1] = new StatsPoint(countOfLikedWords, goodFeedPercentage);
        String s = gson.toJson(withNewOne);

        FileOutputStream outputStream;

        try {
            outputStream = TweetsApplication.getInstance().openFileOutput(STATS_FILENAME, Context.MODE_PRIVATE);
            outputStream.write(s.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        saveTerms(new Term[0]);
    }
}
