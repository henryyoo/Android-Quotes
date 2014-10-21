package com.example.henryyoo1015.funfacts;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by henryyoo1015 on 8/29/14.
 */
public class ColorBook {
    public String[] mColors ={ "#39add1",
            "#3079ab",
            "#c25975"};
    public int getColor(){

        //the button has been click and we need to update the funFact with a newone


        String newColor= "";
        //Randomly pick a fact and assign it
        Random randomGenerator = new Random();
        int randomNumber = randomGenerator.nextInt(mColors.length);
        newColor = mColors[randomNumber];
        int newColorInt = Color.parseColor(newColor);

        return newColorInt;
    }
}
