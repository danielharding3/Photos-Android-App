package com.example.photos;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

public class Photo implements Serializable {
    public String filepath;
    public ArrayList<String> peopleTagged;
    public ArrayList<String> locationsTagged;

    public Photo(String filepath){
        this.filepath = filepath;
        this.peopleTagged = new ArrayList<String>();
        this.locationsTagged = new ArrayList<String>();
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Photo){
            Photo other = (Photo) o;
            return this.filepath.equals(other.filepath);
        }
        return false;
    }
}
