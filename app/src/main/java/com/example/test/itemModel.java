package com.example.test;

public class itemModel {

    String itemName,date,itemPrice,description;

    public itemModel(String itemName,String itemPrice,String date,String description){
        this.date=date;
        this.itemPrice=itemPrice;
        this.itemName=itemName;
        this.description=description;

    }

    public itemModel(){
        
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
