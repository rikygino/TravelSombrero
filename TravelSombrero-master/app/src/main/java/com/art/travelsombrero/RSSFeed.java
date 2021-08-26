package com.art.travelsombrero;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public class RSSFeed {
    private String title = null;
    private String pubDate = null;
    private ArrayList<RSSItem> items;
        
    public RSSFeed() {
        items = new ArrayList<RSSItem>(); 
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
    
    public String getPubDate() {
        return pubDate;
    }
    
    public int addItem(RSSItem item) {
        items.add(item);
        return items.size();
    }
    
    public RSSItem getItem(int index) {
        return items.get(index);
    }
    
    public ArrayList<RSSItem> getAllItems() {
        return items;
    }    
}