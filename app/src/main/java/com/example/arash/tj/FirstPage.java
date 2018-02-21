package com.example.arash.tj;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

public class FirstPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    ImageView userImage ;

    TextView StudentName;
    TextView StudentCode;
    TextView StudentFather;
    TextView StudentMajor;
    String StudentFatherString;
    String StudentMajorString;
    String UserImageString;
    Fragment newFragment = new StudentDataFragment();
    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

    TextView StudentDrawerName;
    class MyJavaScriptInterface
    {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(String html)
        {
            if (html == null)
            {
                return;
            }
            Document firstPage = Jsoup.parse(html);
            Element fatherName = firstPage.getElementById("WucStudentData1_Txt_FatherName");
            Element Major = firstPage.getElementById("WucStudentData1_Txt_ONVAN");
            Element imageElement = firstPage.getElementById("WucStudentData1_Image1");
            StudentFatherString = fatherName.text();
            StudentMajorString = Major.text();
            String UserImageString2 = imageElement.attr("src");
            UserImageString = UserImageString2.replace(".." , "http://enroll.azad.ac.ir");

            Log.i("zart" ,StudentFatherString);
            Log.i("zart" ,UserImageString);

        }
    }

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        StudentDrawerName = (TextView)headerView.findViewById(R.id.StudentDrawerName);
        userImage = (ImageView)headerView.findViewById(R.id.userImage);
        ft.replace(R.id.your_placeholder, new StudentDataFragment());
        ft.addToBackStack(null);
        ft.commit();

        Intent intent = getIntent();
        final String StudnetNameString = intent.getStringExtra("nam");
        final String StudentCodeString = intent.getStringExtra("daneshjuyi");





       /* StudentName = (TextView)findViewById(R.id.studnetName);
        StudentFather = (TextView)findViewById(R.id.studentFather);
        StudentMajor = (TextView)findViewById(R.id.studentMajor);
        StudentCode = (TextView)findViewById(R.id.studentCode); */

        webView = (WebView) findViewById(R.id.web2);
        webView.loadUrl("http://enroll.azad.ac.ir/pages/frm_viewsabtenam.aspx");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.addJavascriptInterface(new FirstPage.MyJavaScriptInterface(), "HTMLOUT");
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(final WebView view, String url) {
                webView.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                StudentDrawerName.setText(StudnetNameString);
                URL url = null;
                String imageUri = UserImageString;
                Picasso.with(FirstPage.this).load(imageUri).resize(120 , 120).transform(new CircleTransform()).into(userImage);
            }
        }, 5000);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_camera) {
            ft2.replace(R.id.your_placeholder, new StudentClassesFragment());
            ft2.addToBackStack(null);
            ft2.commit();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}


class CircleTransform implements Transformation {
    @Override
    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);

        squaredBitmap.recycle();
        return bitmap;
    }

    @Override
    public String key() {
        return "Circle";
    }
}



