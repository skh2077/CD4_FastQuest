package com.example.tt.data;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

public class User {

    private String user_id;
    private LatLng user_location;
    private String email;
    private boolean ismoim = false;
    private boolean isinside = false;
    private Activity user_act;
    private String username;
    private String nickname;
    public int score;
    private float activity;
    private float sociality;
    private String gender;
    private int created;
    private int outside;
    public int age;
    private Bitmap image;


    private User() {
    }

    private static class LazyHolder {
        public static final User INSTANCE = new User();
    }

    /*public User(String Email, String Nickname, int Score, float Activity, float Sociality, int Gender, int Age, int  Created, int Outside) {
        this.email = Email;
        this.nickname = Nickname;
        this.score = Score;
        this.activity = Activity;
        this.sociality = Sociality;
        this.gender = Gender;
        this.created = Created;
        this.outside = Outside;
        this.age = Age;
        check_age();
    }

    public User(String User_id, String Email, String Nickname, int Score, float Activity, float Sociality, int Gender, int Age, int  Created, int Outside) {
        this.user_id = User_id;
        this.email = Email;
        this.nickname = Nickname;
        this.score = Score;
        this.activity = Activity;
        this.sociality = Sociality;
        this.gender = Gender;
        this.created = Created;
        this.outside = Outside;
        this.age = Age;
        check_age();
    }*/

    public String getUser_id() { return user_id; }
    public void setUser_id(String user_id) { this.user_id = user_id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public LatLng getUser_location() { return user_location; }
    public void setUser_location(LatLng user_location) { this.user_location = user_location; }

    public static User getInstance() {
        return LazyHolder.INSTANCE;
    }

    public int getScore() {
        return score;
    }
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setScore(int score) {
        this.score = score;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public float getActivity() {
        return activity ;
    }

    public void setActivity(float activity) {
        this.activity = activity;
    }

    public float getSociality() {
        return sociality;
    }

    public void setSociality(float sociality) {
        this.sociality = sociality;
    }

    public int getOutside() {
        return outside;
    }

    public void setOutside(int outside) {
        this.outside = outside;
    }


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean getismoim() {
        return ismoim;
    }

    public void setIsmoim(boolean ismoim) {
        this.ismoim = ismoim;
    }

    public Activity getUser_act() {
        return user_act;
    }

    public void setUser_act(Activity user_act) {
        this.user_act = user_act;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public boolean getIsinside() {
        return isinside;
    }

    public void setIsinside(boolean isinside) {
        this.isinside = isinside;
    }


    public void check_age() {
        if(age > 65 && activity > 5) {
            this.activity = Math.min(5 + score / 100, this.activity);
        }
        if(age < 10 && sociality > 5) {
            this.sociality = Math.min(5 + score/100, this.sociality);
        }
    }

}
