package com.MusicOrganizer;

/**
 * Created by user on 2/2/2017.
 */
public class Greeting {

    private final String name;
    private final long id;

    public Greeting(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }
}
