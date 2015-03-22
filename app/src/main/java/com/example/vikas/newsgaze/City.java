package com.example.vikas.newsgaze;

/**
 * Created by Shank on 3/22/2015.
 */
import java.lang.*;

public class City implements Comparable<City>{
    public int compareTo(City c2){
        if (dist<c2.dist){
            return -1;
        }
        else if (dist>c2.dist){
            return 1;
        }
        else{
            return 0;
        }
    }

    public City(String city,double dist){
        this.city=city;
        this.dist=dist;
    }

    public String city;
    public double dist;
}

