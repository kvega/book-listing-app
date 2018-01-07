package com.example.kvega.project6booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by kvega on 10/7/17.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private static final String LOG_TAG = BookLoader.class.getName();

    private String url;

/*
    // Constructor for when the user has not et submitted a query with which to create a valid url
    public BookLoader(Context context) {
        super(context);
    }
*/

    // Constructor for a valid url
    public BookLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        Log.d("ON_START_LOADING", "onStartLoading was just called");
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        Log.d("LOAD_IN_BACKGROUND", "loadInBackground was just called");
        if (this.url == null) {
            return null;
        }

        List<Book> books = QueryUtils.fetchBooks(this.url);

        return books;
    }
}
