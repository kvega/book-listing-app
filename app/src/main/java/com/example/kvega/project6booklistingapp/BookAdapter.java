package com.example.kvega.project6booklistingapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kvega on 10/5/17.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public static final String LOG_TAG = BookAdapter.class.getName();

    public BookAdapter(Activity context, List<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the Book object located at this position in the list
        Book currentBook = getItem(position);

        String title = currentBook.getTitle();
        // Find the TextView in the list_item.xml layout with the ID title
        TextView titleTextView = (TextView) convertView.findViewById(R.id.title);
        // Set this title on the TextView
        titleTextView.setText(title);

        if (currentBook.getAuthors() != null) {
            ArrayList<String> authorList = currentBook.getAuthors();
            String authors = "By ";

            if (authorList.size() == 1) {
                authors = authors + authorList.get(0);
            } else {
                for (int i = 0; i < authorList.size() - 1; i++) {
                    authors = authors + authorList.get(i);
                }
                authors = authors + " and " + authorList.get(authorList.size() - 1);
            }

            // Find the TextView in the list_item.xml layout with the ID author
            TextView authorTextView = (TextView) convertView.findViewById(R.id.author);
            // Set this author on the TextView
            authorTextView.setText(authors);
        }

        return convertView;
    }
}