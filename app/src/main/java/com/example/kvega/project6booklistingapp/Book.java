package com.example.kvega.project6booklistingapp;

import java.util.ArrayList;

/**
 * Created by kvega on 10/5/17.
 */

public class Book {

    private String title;

    private String subtitle;

    private ArrayList<String> authors;

    public Book(String title) {
        this.title = title;
    }

    public Book(String title, ArrayList<String> authors) {
        this.title = title;
        this.authors = authors;
    }

    public Book(String title, String subtitle, ArrayList<String> authors) {
        this.title = title;
        this.subtitle = subtitle;
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    @Override
    public String toString() {
        return "Book{" + "title: " + this.title +
                "subtitle: " + this.subtitle +
               "authors: " + this.authors + '}';
    }
}
