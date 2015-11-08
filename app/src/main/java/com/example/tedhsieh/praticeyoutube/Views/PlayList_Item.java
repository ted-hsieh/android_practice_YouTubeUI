package com.example.tedhsieh.praticeyoutube.Views;

public class PlayList_Item {

    static final int ItemType_ListTitle = 0;
    static final int ItemType_Song = 1;
    static final int ItemType_MoreButton = 2;
    static final int ItemType_MaxCount = 3;

    private String PicName;
    private String Title;
    private String Owner;
    private String Information;
    private String Time;
    private int ItemType;

    PlayList_Item(String PicName, String Title, String Owner, String Information, String Time, int ItemType){
        this.PicName = PicName;
        this.Title = Title;
        this.Owner = Owner;
        this.Information = Information;
        this.Time = Time;
        this.ItemType = ItemType;
    }

    PlayList_Item(String Owner, int ItemType){
        this.Owner = Owner;
        this.ItemType = ItemType;
    }

    public String getPicName(){ return PicName; }

    public String getTitle(){ return Title; }

    public String getOwner(){ return Owner; }

    public String getInformation(){ return  Information; }

    public String getTime(){ return Time; }

    public int getItemType(){ return ItemType; }

    public void setPicName(String PicId){ this.PicName = PicId; }

    public void setTitle(String Title){ this.Title = Title; }

    public void setOwner(String Owner){ this.Owner = Owner; }

    public void setInformation(String Information){ this.Information = Information; }

    public void setTime(String Time){ this.Time = Time; }

    public void setItemType(int ItemType) { this.ItemType = ItemType; }


}
