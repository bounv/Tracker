package com.company;

import java.util.ArrayList;

/**
 * Created by boun on 12/19/16.
 */
public class User {
    int id;
    String name;
    String password;

    //ArrayList<Book> books = new ArrayList<>();

    public User(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public User(String name) {
        this.name = name;
    }
}

