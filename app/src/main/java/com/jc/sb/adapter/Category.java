package com.jc.sb.adapter;


public class Category {

    private String cTitle, cDescription, cImage, cCover_Image, cCreated_At;
    private int cID;

    public Category(int id, String title, String description, String image, String cover_image, String created_at){
        this.cID = id;
        this.cTitle = title;
        this.cDescription = description;
        this.cImage = image;
        this.cCover_Image = cover_image;
        this.cCreated_At = created_at;
    }

    public int getCategoryID(){
        return  cID;
    }

    public void setCategoryID(int id){
        this.cID = id;
    }

    public String getCategoryTitle(){
        return  cTitle;
    }

    public void setCategoryTitle(String title){
        this.cTitle = title;
    }

    public String getCategoryDescription(){
        return  cDescription;
    }

    public void setCategoryDescription(String description){
        this.cDescription = description;
    }

    public String getCategoryImage(){
        return  cImage;
    }

    public void setCategoryImage(String image){
        this.cImage = image;
    }

    public String getCategoryCoverImage(){
        return  cCover_Image;
    }

    public void setCategoryCoverImage(String cover_image){
        this.cCover_Image = cover_image;
    }

    public String getCategoryCreatedAt(){
        return  cCreated_At;
    }

    public void setCategoryCreatedAt(String created_at){
        this.cCreated_At = created_at;
    }


}