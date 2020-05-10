package com.kumu.myapplication;

import java.util.ArrayList;

public class Control {

    public String phoneText;
    public ArrayList<String> phoneNumberList;


    public void setPhoneText(String phoneText) {
        this.phoneText = phoneText;
    }

    public void setPhoneNumberList(ArrayList<String> phoneNumberList) {
        this.phoneNumberList = phoneNumberList;
    }

    public int getPhoneNumberList() {
        int counter = 0;
        for(int i = 0;i<phoneNumberList.size();i++){
            if(phoneNumberList.get(i).equals(phoneText)){
                counter = 1;
                break;
            }
        }
        return counter;
    }


}
