package com.example.arash.tj;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

public class Expand  extends Fragment {
    public Expand (){

    }
    ExpandableListView lv;
    View v;
    private String[][] data;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         data = new String[][] {{"audia4"},{"bmwm6"},{"ferrarienzo"}};
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = (inflater.inflate(R.layout.fragment_massage_pag , container , false));


        return v;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // expandableListView =(ExpandableListView) v.findViewById(R.id.simpleExpandableListView);

      //  expandableListView.setAdapter(new SampleExpandablelistview(getActivity(), this.getActivity(),data ));


        lv = (ExpandableListView) view.findViewById(R.id.simpleExpandableListView);

        lv.setAdapter(new SampleExpandablelistview(getActivity(), this.getActivity(),data ));
        lv.setGroupIndicator(null);

    }
}