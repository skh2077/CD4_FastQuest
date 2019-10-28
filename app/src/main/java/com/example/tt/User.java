package com.example.tt;

public class User {

    private int user_id;
    private String email;
    private String nickname;
    int score;
    private float activity;
    private float sociality;
    private int gender;
    private int created;
    private int outside;
    int age;

    public User(int User_id, String Email, String Nickname, int Score, float Activity, float Sociality, int Gender, int Age, int  Created, int Outside) {
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
    }
    public int getUser_id() { return user_id; }
    public void setUser_id(int user_id) { this.user_id = user_id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
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
