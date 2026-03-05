package com.mycompany.librarymanager;

import lombok.Data;

import java.io.Serializable;

@Data
public class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;
    
    private int id;
    private String title;
    private String author;
    private String genre;
    private int year;
    private String status;
    private double rating;
    
    public Book(String title, String author, String genre, int year) {
        this.id = nextId++;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.year = year;
        this.status = "Не прочитана";
        this.rating = 0;
    }
}