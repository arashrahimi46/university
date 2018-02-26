package com.example.arash.tj;


import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by ARASH on 2/20/2018.
 */

public class StudentDataFragment extends Fragment {


    ImageView userImage ;

    TextView StudentName;
    TextView StudentCode;
    TextView StudentFather;
    TextView StudentMajor;
    String StudentFatherString;
    String StudentMajorString;
    String UserImageString;

    ProgressBar prg;
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
    Typeface font ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf");

        return inflater.inflate(R.layout.student_data_fragment, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        StudentName = (TextView)view.findViewById(R.id.studnetName);
        StudentFather = (TextView)view.findViewById(R.id.studentFather);
        StudentMajor = (TextView)view.findViewById(R.id.studentMajor);
        StudentCode = (TextView)view.findViewById(R.id.studentCode);

        prg = (ProgressBar)view.findViewById(R.id.progressBar3);
        TextView t1 = (TextView)view.findViewById(R.id.t1);
        TextView t2 = (TextView)view.findViewById(R.id.t2);
        TextView t3= (TextView)view.findViewById(R.id.t3);
        TextView t4 = (TextView)view.findViewById(R.id.t4);

        StudentName.setTypeface(font);
        StudentFather.setTypeface(font);
        StudentMajor.setTypeface(font);
        StudentCode.setTypeface(font);

        t1.setTypeface(font);
        t2.setTypeface(font);
        t3.setTypeface(font);
        t4.setTypeface(font);

        webView = (WebView) view.findViewById(R.id.web1);
        webView.loadUrl("http://enroll.azad.ac.ir/pages/frm_viewsabtenam.aspx");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.addJavascriptInterface(new StudentDataFragment.MyJavaScriptInterface(), "HTMLOUT");
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(final WebView view, String url) {
                webView.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
            }
        });
        Intent intent = getActivity().getIntent();
        final String StudnetNameString = intent.getStringExtra("nam");
        final String StudentCodeString = intent.getStringExtra("daneshjuyi");

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                StudentName.setText(StudnetNameString);
                StudentFather.setText(StudentFatherString);
                StudentCode.setText(StudentCodeString);
                StudentMajor.setText(StudentMajorString);
                prg.setVisibility(View.INVISIBLE);
            }
        }, 5000);

    }


}
