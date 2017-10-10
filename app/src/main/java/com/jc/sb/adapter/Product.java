package com.jc.sb.adapter;


public class Product {
    private String pTitle, pThumbnail;
    private double pPrice;
    private int pID, cID;

    public Product(int pid, int cid, String title, double price, String thumbnail){
        this.pID = pid;
        this.cID = cid;
        this.pTitle = title;
        this.pPrice = price;
        this.pThumbnail = thumbnail;
    }

    public int getProductID(){
        return  pID;
    }

    public void setProductID(int pid){
        this.pID = pid;
    }

    public int getCategoryID(){
        return  cID;
    }

    public void setCategoryID(int cid){
        this.cID = cid;
    }

    public String getProductTitle(){
        return  pTitle;
    }

    public void setProductTitle(String title){
        this.pTitle = title;
    }

    public double getProductPrice(){
        return pPrice;
    }

    public void setProductPrice(double price){
        this.pPrice = price;
    }

    public String getProductThumbnail(){
        return  pThumbnail;
    }

    public void setProductThumbnail(String thumbnail){
        this.pThumbnail = thumbnail;
    }
}
