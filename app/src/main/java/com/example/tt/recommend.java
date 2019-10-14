package com.example.tt;

public class recommend {
    private String category;
    private float activity_rate;
    private float sociality_rate;
    private int outside;
    int score;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getActivity_rate() {
        return activity_rate + ;
    }

    public void setActivity_rate(float activity_rate) {
        this.activity_rate = activity_rate;
    }

    public float getSociality_rate() {
        return sociality_rate;
    }

    public void setSociality_rate(float sociality_rate) {
        this.sociality_rate = sociality_rate;
    }

    public int getOutside() {
        return outside;
    }

    public void setOutside(int outside) {
        this.outside = outside;
    }

    public void check_age(int age) {
        if(age > 65 && activity_rate > 5) {
            this.activity_rate = 5 + score/100;
        }
        if(age < 10 && sociality_rate > 5) {
            this.sociality_rate = 5 + score/100;
        }
    }
//성별은 필요 없겠다

}
