package com.example.kvega.project6booklistingapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by kvega on 10/7/17.
 */

public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getName();
    private static final String KEY_VOLUME_INFO = "volumeInfo";
    private static final String KEY_TITLE = "title";
    private static final String KEY_AUTHORS = "authors";
    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 15000;
    private static final int SLEEP_TIMEOUT = 2000;


    private QueryUtils() {
    }

    // Return a list of Book objects that has been built up from parsing a JSON response
    public static ArrayList<Book> fetchBooks(String requestUrl) {
        try {
            Thread.sleep(SLEEP_TIMEOUT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create URL object
        URL url = createURL(requestUrl);

        // Perform HTTP request to the URL and receive a JSON Response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Create an empty ArrayList that we can start adding books to
        ArrayList<Book> books = extractBooksFromJson(jsonResponse);

        return books;
    }

    private static URL createURL(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating url", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<Book> extractBooksFromJson(String booksJSON) {
        ArrayList<Book> books = new ArrayList<>();
        if (TextUtils.isEmpty(booksJSON)) {
            return null;
        }

        try {
            /* Parse the JSON response and build up a list of Book objects.
            * Convert the response string into a JSONObject
            * */
            JSONObject response = new JSONObject(booksJSON);
            System.out.println(response);
            // Check for "items" JSONArray
            if (response.has("items")) {
                // Extract "items" JSONArray
                JSONArray items = response.optJSONArray("items");
                // Get the length of the array
                int numItems = items.length();
                // Loop through each item in the array
                for (int i = 0; i < items.length(); i++) {
                    Book book;
                    JSONObject item = items.getJSONObject(i);
                    JSONObject volumeInfo = item.getJSONObject(KEY_VOLUME_INFO);
                    String title = volumeInfo.getString(KEY_TITLE);
                    if (volumeInfo.has(KEY_AUTHORS)){
                        JSONArray authors = volumeInfo.optJSONArray(KEY_AUTHORS);
                        // Turn JSONArray into an ArrayList
                        ArrayList<String> authorList = new ArrayList<>();
                        for (int j = 0; j < authors.length(); j++) {
                            authorList.add(authors.getString(j));
                        }

                        // Create Book object from title and authorList
                        book = new Book(title, authorList);
                    } else {
                        book = new Book(title);
                    }

                    books.add(book);
                }
            } else {
                return null;
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the book JSON results", e);
        }

        return books;
    }
}
