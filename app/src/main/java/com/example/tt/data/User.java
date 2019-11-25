package com.example.tt.data;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

public class User {

    private String user_id;
    private LatLng user_location;
    private String email;

    public boolean getismoim() {
        return ismoim;
    }

    public void setIsmoim(boolean ismoim) {
        this.ismoim = ismoim;
    }

    boolean ismoim = false;

    public Activity getUser_act() {
        return user_act;
    }

    public void setUser_act(Activity user_act) {
        this.user_act = user_act;
    }

    private Activity user_act;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;
    private String nickname;
    public int score;
    private float activity;
    private float sociality;
    private int gender;
    private int created;
    private int outside;
    public int age;

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

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
    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
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


    public void check_age() {
        if(age > 65 && activity > 5) {
            this.activity = Math.min(5 + score / 100, this.activity);
        }
        if(age < 10 && sociality > 5) {
            this.sociality = Math.min(5 + score/100, this.sociality);
        }
    }
//성별은 필요 없겠다
//빈도 저장할까? - score로 대체??
    //카테고리가 비슷한걸 먼저 찾는다, 사교성의 값의 범위가 비슷한걸 찾는다. 활동성의 값의 범위가 비슷한걸 찾는다. 안 밖을 확인한다.
    // score값이 클수록 점수를 반전시키고 나중가면 그냥 다 엇비슷하게 랜덤값줄까?
    public void recommend_act() {
        //this.search_cat(category);

    }
}
