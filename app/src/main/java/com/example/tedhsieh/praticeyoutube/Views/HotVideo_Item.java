package com.example.tedhsieh.praticeyoutube.Views;

public class HotVideo_Item {

    public static final int ItemType_Channel = 0;
    public static final int ItemType_Video = 1;
    public static final int ItemType_MaxCount = 2;

    private String Title;
    private String PicName;
    private String Time;
    private String Information;
    private int ItemType;

    public HotVideo_Item(int ItemType){
        this.Title = "Not a Video!";
        this.PicName = "";
        this.Time = "";
        this.Information = "";
        this.ItemType = ItemType;
    }

    public HotVideo_Item(String Title, String PicName, String Time, String Information, int ItemType){
        this.Title = Title;
        this.PicName = PicName;
        this.Time = Time;
        this.Information = Information;
        this.ItemType = ItemType;
    }

    public String getTitle(){
        return Title;
    }

    public String getPicName(){
        return PicName;
    }

    public String getTime(){
        return Time;
    }

    public String getInformation(){
        return Information;
    }

    public int getItemType(){
        return ItemType;
    }

    public void setTitle(String Title){
        this.Title = Title;
    }

    public void setPicName(String PicName){
        this.PicName = PicName;
    }

    public void setTime(String Time){
        this.Time = Time;
    }

    public void setInformation(String Information){
        this.Information = Information;
    }

    public void setItemType(int ItemType){
        this.ItemType = ItemType;
    }

}
