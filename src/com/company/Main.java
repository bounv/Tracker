package com.company;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static HashMap<String, User> users = new HashMap<>();
    public static ArrayList<Book> books = new ArrayList<>();

    public static void main(String[] args) {
        Spark.init();

        Spark.get(
                "/",
                ((request, response) -> {
                    Session session = request.session();
                    String userName = session.attribute("userName");

                    //put books in this hashmap
                    HashMap m = new HashMap();
                    m.put("userName", userName);
                    m.put("books", books);

                    return new ModelAndView(m, "home.html");
                }),
                new MustacheTemplateEngine()
        );


        Spark.post(
                "/login",
                ((request, response) -> {
                    String userName = request.queryParams("loginName");
                    if (userName == null) {
                        throw new Exception("Login name not found.");
                    }

                    User user = users.get(userName);
                    if (user == null) {
                        user = new User(userName);
                        users.put(userName, user);
                    }

                    Session session = request.session();
                    session.attribute("userName", userName);

                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/logout",
                ((request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/add-book",
                ((request, response) -> {
                    Session session = request.session();
                    String userName = session.attribute("userName");
                    if (userName == null) {
                        throw new Exception("Not logged in.");
                    }

                    String title = request.queryParams("messageTitle");
                    if (title == null) {
                        throw new Exception("Didn't get necessary query parameters.");
                    }

                    String author = request.queryParams("messageAuthor");
                    if (author == null) {
                        throw new Exception("Didn't get necessary query parameters.");
                    }

                    String publisher = request.queryParams("messagePublisher");
                    if (publisher == null) {
                        throw new Exception("Didn't get necessary query parameters.");
                    }

                    int releaseYear = Integer.parseInt(request.queryParams("messageYear"));
                    if (releaseYear < 1980  && releaseYear > 2017) {
                        throw new Exception("Didn't get necessary query parameters.");
                    }

                    //Book m = new Book(bookId, title, author, publisher, releaseYear);
                    Book m = new Book(books.size(), title, author, publisher, releaseYear, userName);
                    books.add(m);

                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/update-book",
                ((request, response) -> {
                    Session session = request.session();
                    String userName = session.attribute("userName");
                    if (userName == null) {
                        throw new Exception("Not logged in.");
                    }

                    String title = request.queryParams("messageTitle");
                    if (title == null) {
                        throw new Exception("Didn't get necessary query parameters.");
                    }

                    String author = request.queryParams("messageAuthor");
                    if (author == null) {
                        throw new Exception("Didn't get necessary query parameters.");
                    }

                    String publisher = request.queryParams("messagePublisher");
                    if (publisher == null) {
                        throw new Exception("Didn't get necessary query parameters.");
                    }

                    int releaseYear = Integer.parseInt(request.queryParams("messageYear"));
                    if (releaseYear < 1980  && releaseYear > 2017) {
                        throw new Exception("Didn't get necessary query parameters.");
                    }

                    String userId = request.queryParams("messageUserId");
                    if (publisher == null) {
                        throw new Exception("Didn't get necessary query parameters.");
                    }

                    int id = Integer.parseInt(request.queryParams("bookId"));

                    String book = request.queryParams("updateBook");
                    Book m = new Book(books.size(), title, author, publisher, releaseYear, userId);
                    Book b = books.get(id -1);

                    if(b.userId.equals(userName)) {
                        books.remove(id -1);
                        books.add(id - 1, m);
                    } else {
                        throw new Exception("EDIT your own book");
                    }

                    response.redirect("/");
                    return "";
                })
        );


        Spark.post(
                "/delete-book",
                ((request, response) -> {
                    Session session = request.session();
                    String userName = session.attribute("userName");
                    if (userName == null) {
                        throw new Exception("Not logged in.");
                    }

                    int id = Integer.parseInt(request.queryParams("bookId"));

                    Book b = books.get(id);

                    if(b.userId.equals(userName)) {
                        books.remove(id);
                    } else {
                        throw new Exception("DELETE your own book");
                    }

                    response.redirect("/");
                    return "";
                })
        );

    }
}

