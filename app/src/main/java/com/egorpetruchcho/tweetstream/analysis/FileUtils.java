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
import java.util.Vector;

public class FileUtils {
    private static FileUtils instance = new FileUtils();
    private static Gson gson = new Gson();

    private static final String FILENAME = "myfile.txt";


    private FileUtils() {
    }

    public static FileUtils getInstance() {
        return instance;
    }

    public Term[] readTerms() {
        Context context = TweetsApplication.getInstance();
        FileInputStream fis;
        try {
            fis = context.openFileInput(FILENAME);
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
            outputStream = TweetsApplication.getInstance().openFileOutput(FILENAME, Context.MODE_PRIVATE);
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
