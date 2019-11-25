package com.example.tt.data;


import java.util.ArrayList;

public  class UserData {

    private boolean firstFlag = false;
    boolean flag_art =true;
    boolean flag_book=true;
    boolean flag_camera=true;
    boolean flag_dance=true;
    boolean flag_food =true;
    boolean flag_game=true;
    boolean flag_language=true;
    boolean flag_meet =true;
    boolean flag_movie=true;
    boolean flag_music=true;
    boolean flag_sports=true;
    boolean flag_travel=true;
    boolean flag_volunteer=true;

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