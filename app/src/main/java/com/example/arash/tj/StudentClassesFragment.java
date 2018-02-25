package com.example.arash.tj;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;


public class StudentClassesFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String pageCode;


    public StudentClassesFragment() {
        // Required empty public constructor
    }

    Typeface font ;
    TableLayout tl;
    FrameLayout fl ;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudentClassesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentClassesFragment newInstance(String param1, String param2) {
        StudentClassesFragment fragment = new StudentClassesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_classes, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        final HorizontalScrollView Scroll = (HorizontalScrollView) view.findViewById(R.id.scroll);

         tl = (TableLayout) view.findViewById(R.id.tlGridTable);
        final WebView webView;
        webView = (WebView) view.findViewById(R.id.web3);
        webView.loadUrl("http://enroll.azad.ac.ir/pages/frm_viewsabtenam.aspx");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.addJavascriptInterface(new StudentClassesFragment.MyJavaScriptInterface(), "HTMLOUT");
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(final WebView view, String url) {
                webView.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
              System.out.println(pageCode);


              /* Find Tablelayout defined in main.xml */

              Document PageHtml = Jsoup.parse(pageCode);

              Elements trs = PageHtml.getElementById("DataGrid2").getElementsByTag("tr");

              int i = 0;
                for (Element tr2 : trs)
                {
                    Elements tds = tr2.getElementsByTag("td");
                    TableRow tr = new TableRow(getContext());
                    tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    tr.setPadding(24 , 24 , 24 ,24);
                    if ( i == 0)
                    {
                        tr.setBackgroundColor(Color.parseColor("#303F9F"));
                    }
                    else
                    {
                        if (i%2 == 0)
                        {
                            tr.setBackgroundColor(Color.parseColor("#eeeeee"));
                        }
                        else
                        {
                            tr.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        }
                    }
                    for (Element td : tds)
                    {

                        String tdText = td.text();
                        TextView tdTextView = new TextView(getContext());
                        tdTextView.setText(tdText);
                        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(100, 4 ,8 , 4);
                        tdTextView.setLayoutParams(lp);
                        tdTextView.setPadding(8 , 8 , 8 ,8);
                        tdTextView.setTextSize(16);
                        tdTextView.setGravity(Gravity.CENTER);
                        tdTextView.setTypeface(font);
                        if (i == 0)
                        {
                            tdTextView.setTextColor(Color.parseColor("#FFFFFF"));
                        }
                        else
                        {
                            tdTextView.setTextColor(Color.parseColor("#000000"));
                        }

                        tr.addView(tdTextView);
                    }
                    /* Create a new row to be added. */
                    tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    i++;
                }
                Scroll.postDelayed(new Runnable() {
                    public void run() {
                        Scroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                    }
                }, 100L);
            }
        }, 2000);


    }

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
            pageCode = html;


        }
    }


}
