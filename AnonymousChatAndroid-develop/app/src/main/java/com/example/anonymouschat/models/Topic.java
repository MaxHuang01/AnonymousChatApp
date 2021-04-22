package com.example.anonymouschat.models;

import java.io.Serializable;

public class Topic implements Serializable {
    public String id;
    public String name;

    public Topic(String id, String name) {
        this.id = id;
        this.name = name;
    }
}