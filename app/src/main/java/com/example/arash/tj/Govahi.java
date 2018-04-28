package com.example.arash.tj;

import android.content.Context;

/**
 * Created by ARASH on 2/21/2018.
 */

public class Govahi {
    private String title;
    private String status;
    public Govahi() {

    }

    public Govahi(Context context , String title , String status) {
        this.title = title;
        this.status=status;

    }

    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status;
    }

    public void setTitle(String name) {
        this.title = name;
    }
}
