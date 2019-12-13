package com.example.photos;

import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {

    public String name;
    public ArrayList<Photo> photos;

    public Album(String name){
        this.name = name;
        this.photos = new ArrayList<Photo>();
    }

    public String toString(){
        return this.name;
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof Album){
            Album o = (Album) other;
            return this.name.equals(o.name);
        }
        return false;
    }
}
