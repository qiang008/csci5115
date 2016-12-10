package com.theprotectors.theprotectors;

/**
 * Created by apple on 08/12/2016.
 */
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class TagDataBase {
    private MarkerOptions mark;
    private String title;
    private double latitude;
    private double longtitude;
    private List<String> path = new ArrayList<>();

    public TagDataBase(){}

    public TagDataBase(String title, double latitude, double longtitude, List<String> path)
    {
        this.title=title;
        this.latitude=latitude;
        this.longtitude=longtitude;
        this.path=path;
    }
    public void setTitle (String title)
    {
        this.title=title;
    }
    public void setLat(double latitude)
    {
        this.latitude=latitude;
    }
    public void setLong(double longtitude)
    {
        this.longtitude=longtitude;
    }
    public void setPath (List<String> path)
    {
        this.path=path;
    }

    public String getTitle()
    {
        return title;
    }

    public double getLat ()
    {
        return latitude;
    }
    public double getLong()
    {
        return longtitude;
    }

    public List<String> getPath(){
        return path;
    }


}
