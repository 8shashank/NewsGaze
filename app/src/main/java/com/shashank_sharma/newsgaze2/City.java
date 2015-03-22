package com.shashank_sharma.newsgaze2;

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

    public City(String city,double dist,double lat,double lon){
        this.city=city;
        this.dist=dist;
        this.lat=lat;
        this.lon=lon;
    }

    public String city;
    public double dist;
    public double lat;
    public double lon;
}

