package com.example.arash.tj;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListAdapter;
import android.widget.CompoundButton.OnCheckedChangeListener;
import java.util.HashMap;

public class SampleExpandablelistview extends BaseExpandableListAdapter implements ExpandableListAdapter {
    public Context context;
    CheckBox checkBox;
    private LayoutInflater vi;
    private String[][] data;
    int _objInt;
    public static Boolean checked[] = new Boolean[1];

    HashMap<Long,Boolean> checkboxMap = new HashMap<Long,Boolean>();
    private static final int GROUP_ITEM_RESOURCE = R.layout.expandgroupitem;
    private static final int CHILD_ITEM_RESOURCE = R.layout.expandchildbox;
    public String []check_string_array;

    public SampleExpandablelistview(Context context, Activity activity, String[][] data) {
        this.data = data;
        this.context = context;
       /// vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vi = LayoutInflater.from(context);
        _objInt = data.length;
        check_string_array = new String[_objInt];
        popolaCheckMap();
    }
    public void popolaCheckMap(){

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String buffer = null;

        for(int i=0; i<_objInt; i++){
            buffer = settings.getString(String.valueOf((int)i),"false");
            if(buffer.equals("false"))
                checkboxMap.put((long)i, false);
            else checkboxMap.put((long)i, true);
        }
    }

    public class CheckListener implements OnCheckedChangeListener {

        long pos;

        public void setPosition(long p){
            pos = p;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            Log.i("checkListenerChanged", String.valueOf(pos)+":"+String.valueOf(isChecked));
            checkboxMap.put(pos, isChecked);
            if(isChecked == true) check_string_array[(int)pos] = "true";
            else                                  check_string_array[(int)pos] = "false";
            // save checkbox state of each group
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor preferencesEditor = settings.edit();
            preferencesEditor.putString(String.valueOf((int)pos), check_string_array[(int)pos]);
            preferencesEditor.commit();
        }
    }
    public String getChild(int groupPosition, int childPosition) {
        return data[groupPosition][childPosition];
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public int getChildrenCount(int groupPosition) {
        return data[groupPosition].length;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View v = convertView;
        String child = getChild(groupPosition, childPosition);
        int id_res = 0;
        if(groupPosition == 0){
            if(childPosition == 0); id_res = R.string.mj;

        }
        else if(groupPosition == 1){
            if(childPosition == 0); id_res = R.string.mj;

        }
        else if(groupPosition == 2){
            if(childPosition == 0);  id_res= R.string.mj;

        }

        if (child != null) {
            v = vi.inflate(R.layout.expandchildbox, parent,false);
            ViewHolder holder = new ViewHolder(v);
            holder.text2.setText(Html.fromHtml(child));
            holder.text2.setText(id_res);
        }
        return v;
    }
    public String getGroup(int groupPosition) {
        return "group-" + groupPosition;
    }
    public int getGroupCount() {
        return data.length;
    }
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View v = convertView;
        String group = null;
        int details_res = 0;

        long group_id = getGroupId(groupPosition);
        if(group_id == 0){
            group = "خطای p1400";
            details_res = R.string.de1;
        }
        else if(group_id == 1){
           group = "خطای b1400";
           details_res = R.string.de2;
        }
        else if(group_id == 2){
            group = "خطای c1890";
            details_res = R.string.de3;
        }

        if (group != null) {
            v = vi.inflate(R.layout.expandgroupitem, parent, false);
            ViewHolder holder = new ViewHolder(v);

            holder.text.setText(Html.fromHtml(group));
           // holder.checkbox.setFocusable(false);
            CheckListener checkL = new CheckListener();
            checkL.setPosition(group_id);
           // holder.checkbox.setOnCheckedChangeListener(checkL);
           // holder.checkbox.setChecked(checkboxMap.get(group_id));
        }
        return v;
    }
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    public boolean hasStableIds() {
        return true;
    }
}
