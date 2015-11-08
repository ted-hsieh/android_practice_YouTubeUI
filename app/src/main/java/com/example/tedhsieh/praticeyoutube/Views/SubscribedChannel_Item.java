package com.example.tedhsieh.praticeyoutube.Views;

public class SubscribedChannel_Item {

    static final int ItemType_TopBlock = 0;
    static final int ItemType_ChannelListTitle = 1;
    static final int ItemType_ChannelListItem = 2;
    static final int ItemType_MoreButton = 3;
    static final int ItemType_MaxCount = 4;

    private String PicName;
    private String Title;
    private String Information;
    private int ItemType;

    public SubscribedChannel_Item(String PicName, String Title, String Information, int ItemType){
        this.PicName = PicName;
        this.Title = Title;
        this.Information = Information;
        this.ItemType = ItemType;
    }

    public SubscribedChannel_Item(String Title, int ItemType){
        this.Title = Title;
        this.ItemType = ItemType;
    }

    public String getPicName(){
        return PicName;
    }

    public String getTitle(){
        return Title;
    }

    public String getInformation(){
        return Information;
    }

    public int getItemType(){
        return ItemType;
    }

    public void setPicName(String PicName){
        this.PicName = PicName;
    }

    public void setTitle(String Title){
        this.Title = Title;
    }

    public void setInformation(String Information){
        this.Information = Information;
    }

    public void setItemType(int ItemType){
        this.ItemType = ItemType;
    }

}
