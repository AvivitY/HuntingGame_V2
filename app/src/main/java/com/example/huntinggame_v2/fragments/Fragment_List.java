package com.example.huntinggame_v2.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.huntinggame_v2.CallBack_MapFocus;
import com.example.huntinggame_v2.R;
import com.example.huntinggame_v2.Score;

import java.util.ArrayList;
import java.util.List;

public class Fragment_List extends Fragment{
    private ListView list_LST_list;
    ArrayAdapter<String> arrayAdapter;
    private CallBack_MapFocus callBack_mapFocus;
    private ArrayList<Score> scores1 = new ArrayList<>();
    private ArrayList<String> scores = new ArrayList<>();

    public void setCallBack_mapFocus(CallBack_MapFocus callBack_mapFocus) {
        this.callBack_mapFocus = callBack_mapFocus;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, scores);;
        list_LST_list.setAdapter(arrayAdapter);
        list_LST_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem=(String) list_LST_list.getItemAtPosition(position);
                if (callBack_mapFocus != null) {
                    callBack_mapFocus.focusMap(scores1.get(position).getLatitude(),scores1.get(position).getLongitude());
                }
            }
        });
        return view;
    }

    private void findViews(View view) {
        list_LST_list = view.findViewById(R.id.list_LST_list);
    }

    public void setList(ArrayList<Score> list){
        scores1 = list;
        for (Score i : list) {
            this.scores.add(String.valueOf(i.getScore()));
        }
    }


}
