package com.example.arash.tj;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class ViewHolder {
    public TextView text;
    public TextView text1;
    public TextView text2;
    public CheckBox checkbox;

    public ViewHolder(View v) {

        this.text = (TextView)v.findViewById(R.id.Subjecttxt);
        this.text1 = (TextView)v.findViewById(R.id.Sendertxt);
        this.text2 = (TextView)v.findViewById(R.id.Detailstxt);

     //   this.checkbox=(CheckBox)v.findViewById(R.id.checkBox) ;

    }

}
