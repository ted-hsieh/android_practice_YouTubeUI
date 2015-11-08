package com.example.tedhsieh.praticeyoutube.Views;

public class AccountPage_InformationItem {
    private String PicName;
    private String Title;

    AccountPage_InformationItem(String PicName, String Title){
        this.PicName = PicName;
        this.Title = Title;
    }

    public String getPicName (){
        return PicName;
    }

    public String getTitle(){
        return Title;
    }

    public void setPicName(String PicName){
        this.PicName = PicName;
    }

    public void setTitle(String Title){
        this.Title = Title;
    }

}
