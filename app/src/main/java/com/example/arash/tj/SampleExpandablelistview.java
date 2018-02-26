package com.example.arash.tj;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class SampleExpandablelistview extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    private List<String> _listMessageType; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public SampleExpandablelistview(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData ,List<String> ListMessageType ) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this._listMessageType = ListMessageType;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandchildbox, null);
        }
        Typeface custom_font = Typeface.createFromAsset(_context.getAssets(),  "fonts/IRANSansMobile.ttf");
        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.Detailstxt);

        txtListChild.setTypeface(custom_font);
        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Log.i("groupPosition", String.valueOf(groupPosition));
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }


    public Object getGroup2(int groupPosition) {
        return this._listMessageType.get(groupPosition);
    }



    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        String messageType = (String)getGroup2(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandgroupitem, null);
        }

        Typeface custom_font = Typeface.createFromAsset(_context.getAssets(),  "fonts/IRANSansMobile.ttf");
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.Subjecttxt);
        TextView msgType = (TextView)convertView.findViewById(R.id.Sendertxt);
        msgType.setTypeface(custom_font);
        msgType.setText("نوع پیام :" + " " + messageType);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setTypeface(custom_font);
        if (Objects.equals(messageType, "پیام خصوصی"))
        {
        msgType.setTextColor(Color.parseColor("#006600"));
        }else
        {
            msgType.setTextColor(Color.parseColor("#800000"));
        }
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}