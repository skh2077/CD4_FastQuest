package com.example.tt.data;


import java.util.ArrayList;

public  class UserData {

    private boolean firstFlag = false;
    boolean flag_art =false;
    boolean flag_book=false;
    boolean flag_camera=false;
    boolean flag_dance=false;
    boolean flag_food =false;
    boolean flag_game=false;
    boolean flag_language=false;
    boolean flag_meet =false;
    boolean flag_movie=false;
    boolean flag_music=false;
    boolean flag_sports=false;
    boolean flag_travel=false;
    boolean flag_volunteer=false;

    static ArrayList<Boolean> BoolList =  new ArrayList<Boolean>();

    public  UserData( ){

        if(firstFlag == false){

            BoolList.add(flag_art);
            BoolList.add(flag_book);
            BoolList.add(flag_camera);
            BoolList.add(flag_dance);
            BoolList.add(flag_food);
            BoolList.add(flag_game);
            BoolList.add(flag_language);
            BoolList.add(flag_meet);
            BoolList.add(flag_movie);
            BoolList.add(flag_music);
            BoolList.add(flag_sports);
            BoolList.add(flag_travel);
            BoolList.add(flag_volunteer);

        }
        firstFlag= true;

    }

    public UserData(ArrayList<Boolean> BoolList){

        setBoolList(BoolList);
    }


    public ArrayList<Boolean> getBooList() {
        return BoolList;
    }

    public void setBoolList(ArrayList<Boolean> BList) {
        this.BoolList = BoolList;
    }

}