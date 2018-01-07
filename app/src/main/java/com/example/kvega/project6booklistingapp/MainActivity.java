package com.example.kvega.project6booklistingapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,LoaderManager.LoaderCallbacks<List<Book>> {

    private BookAdapter bookAdapter;
    private String bookQuery;
    private SearchView sv;
    boolean isConnected;
    private static final int BOOK_LOADER_ID = 1;
    private TextView emptyStateTextView;
    public static final String LOG_TAG = MainActivity.class.getName();
    private static final String BOOKS_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView bookListView = (ListView) findViewById(R.id.list);

        emptyStateTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(findViewById(R.id.empty_view));

        bookAdapter = new BookAdapter(this, new ArrayList<Book>());

        bookListView.setAdapter(bookAdapter);

        isConnected = checkNetworkStatus();

        if (isConnected) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(BOOK_LOADER_ID, null, this).forceLoad();
        } else {
            View loadingIndicator = findViewById(R.id.loading_spinner);
            loadingIndicator.setVisibility(GONE);
            emptyStateTextView.setText(R.string.no_internet);
        }
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        Log.d("LOADER", "Loader was just called");

        Uri baseUri;
        if (bookQuery != null) {
            // Case for when a query has been submitted
            baseUri = Uri.parse(BOOKS_REQUEST_URL + bookQuery);
            System.out.println(BOOKS_REQUEST_URL + bookQuery);
            Uri.Builder uriBuilder = baseUri.buildUpon();

            return new BookLoader(this, uriBuilder.toString());
        } else {
            return new BookLoader(this, null);
        }
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        Log.d("ON_LOAD_FINISHED", "onLoadFinished was just called");
        View loadingIndicator = findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(GONE);

        if (isConnected) {
            // Prompts the user to submit a query
            emptyStateTextView.setText(R.string.empty_query);
        } else {
            emptyStateTextView.setText(R.string.no_internet);
        }

        bookAdapter.clear();

        if (books != null && !books.isEmpty()) {
            bookAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        Log.d("ON_LOADER_RESET", "onLoaderReset was just called");
        bookAdapter.clear();
    }

    // Create a SearchView in the Action Bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add("Search");
        item.setIcon(android.R.drawable.ic_menu_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        sv = new SearchView(this);
        sv.setOnQueryTextListener(this);
        item.setActionView(sv);
        sv.clearFocus();
        return true;
    }

    public boolean onQueryTextChange(String s) {
        // Do nothing when changing text in the SearchView
        return true;
    }

    // Restart Loader with new query
    @Override
    public boolean onQueryTextSubmit(String s) {
        Log.d("ON_QUERY_TEXT_SUBMIT", "onQueryTextSubmit was just called");
        bookQuery = !TextUtils.isEmpty(s) ? s.replace(" ", "+") : null;

        isConnected = checkNetworkStatus();

        getLoaderManager().restartLoader(BOOK_LOADER_ID, null, this).forceLoad();

        sv.clearFocus();

        return true;
    }

    public boolean checkNetworkStatus() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }
}
