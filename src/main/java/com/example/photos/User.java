package com.example.photos;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private static User instance;

    public ArrayList<Album> albums = new ArrayList<>();
    public ArrayList<Photo> photos = new ArrayList<>();

    public static User getInstance(Context context){
        if(instance == null){
            instance = readUser(context);
        }
        return instance;
    }

    private static User readUser(Context context){
        User ret;
        try {
            //ObjectInputStream ois = new ObjectInputStream(context.openFileInput(context.getFilesDir() + "/" + "user.dat"));
            ObjectInputStream ois = new ObjectInputStream(context.openFileInput("user.dat"));
            ret = (User) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            ret = new User();
            e.printStackTrace();
        }
        return ret;
    }

    public void saveInstance(Context context){
        File saveLoc = new File(context.getFilesDir() + "/" + "user.dat");

        if(saveLoc.exists()){
            saveLoc.delete();
        }

        try {
            saveLoc.getParentFile().mkdirs();
            saveLoc.createNewFile();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveLoc));
            oos.writeObject(instance);
            oos.close();
        } catch(IOException e){
            Toast.makeText(context, "There was an error saving application data", Toast.LENGTH_SHORT);
            e.printStackTrace();
        }
    }
}
