package com.example.huntinggame_v2;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TopTenManager {
    Score scores[];
    String json="";
    TypeToken token;
    private List<Integer> topTen;

    public TopTenManager() {
        token = new TypeToken<Score[]>() {};
        loadArray();
        topTen = new ArrayList<>();
        for(Score s : scores){
            topTen.add(s.getScore());
        }
        Collections.sort(topTen,Collections.reverseOrder());
    }

    public boolean isTopTen(int score, double latitude, double longitude) {
        if (score > topTen.get(9)) {
            for(Score s : scores){
                if(s.getScore() == topTen.get(9)){
                    s.setScore(score);
                    s.setLatitude(latitude);
                    s.setLongitude(longitude);
                    break;
                }
            }
            topTen.remove(9);
            topTen.add(score);
            Collections.sort(topTen,Collections.reverseOrder());
            return true;
        }
        return false;
    }

    public void saveArray() {
        json = new Gson().toJson(scores);
        MSP.getMe().putString("SCORES",json);
    }

    private void loadArray() {
        json = MSP.getMe().getString("SCORES","");
        try {
            scores = new Gson().fromJson(json,token.getType());
        }catch (Exception ex){}

        if(scores == null){
            scores = new Score[10];
            for(int i = 0; i< scores.length; i++){
                scores[i] = new Score();
            }
        }
    }

    public ArrayList<Score> getArray() {
        ArrayList<Score> sorted = new ArrayList<>();
        Collections.addAll(sorted,scores);
        Collections.sort(sorted, new Comparator<Score>() {
            @Override
            public int compare(Score score, Score t1) {
                return t1.getScore() - score.getScore();
            }
        });
        return sorted;
    }
}



