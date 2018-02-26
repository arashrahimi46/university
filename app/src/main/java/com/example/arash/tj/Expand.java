package com.example.arash.tj;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ExpandableListView;
import android.widget.HorizontalScrollView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Expand  extends Fragment {
    public Expand (){

    }
    ExpandableListView lv;
    View v;
    WebView webView;
    String pageCode;
    private String[][] data;
    Typeface font;
    ProgressBar prg;
    List<String> listDataHeader;
    List<String> listSender;
    HashMap<String, List<String>> listDataChild;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         data = new String[][] {{"audia4"},{"bmwm6"},{"ferrarienzo"}};
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = (inflater.inflate(R.layout.fragment_massage_pag , container , false));
         font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf");


        return v;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // expandableListView =(ExpandableListView) v.findViewById(R.id.simpleExpandableListView);

      //  expandableListView.setAdapter(new SampleExpandablelistview(getActivity(), this.getActivity(),data ));
        lv = (ExpandableListView) view.findViewById(R.id.simpleExpandableListView);
        webView = (WebView) view.findViewById(R.id.web3);
        webView.loadUrl("http://enroll.azad.ac.ir/Default1.aspx");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.addJavascriptInterface(new Expand.MyJavaScriptInterface(), "HTMLOUT");
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

                Elements Prmessages = PageHtml.getElementsByAttributeValue("style" , "border:1px solid silver;color:Maroon;padding:2px;border-color:Gray");
                Elements Pumessages = PageHtml.getElementsByAttributeValue("style" , "border:1px solid silver;color:Maroon;padding:2px;border-color:silver");



                int i = 0;

                for (Element msg : Prmessages) {
                    Element Title = msg.getElementsByAttributeValue("style" , "font-size:12px;font-weight:bold;border:1px solid silver;background-color:#FFFFF0;color:Maroon;padding:1px").first();
                    Element mtext = msg.getElementsByAttributeValue("style" , "font-size:12px;padding:10px;color:Black").first();
                    String Title2 = Title.text().replace("/" , "");
                    listDataHeader.add(Title2);
                    List<String> msgText = new ArrayList<String>();
                    msgText.add(mtext.text());
                    listDataChild.put(listDataHeader.get(i), msgText);
                    listSender.add("پیام خصوصی");
                    i++;
                }
                int j = 0 ;
                for (Element msg : Pumessages) {
                    Element Title = msg.getElementsByAttributeValue("style" , "font-size:12px;font-weight:bold;border:1px solid silver;background-color:#FFFFF0;color:Maroon;padding:1px").first();
                    Element mtext = msg.getElementsByAttributeValue("style" , "font-size:12px;padding:10px;color:Black").first();
                    String Title2 = Title.text().replace("/" , "");
                    listDataHeader.add(Title2);
                    List<String> msgText = new ArrayList<String>();
                    msgText.add(mtext.text());
                    listSender.add("پیام عمومی");
                    listDataChild.put(listDataHeader.get(j), msgText);
                    j++;
                }

            }
        }, 4000);

        prg = (ProgressBar)view.findViewById(R.id.progressBar4);

        listDataHeader = new ArrayList<String>();
        listSender = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        final Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                prg.setVisibility(View.INVISIBLE);
                lv.setAdapter(new SampleExpandablelistview(getActivity(), listDataHeader ,listDataChild ,listSender ));
                lv.setGroupIndicator(null);

            }
        }, 5000);



    }

    private void prepareListData() {


       /* // Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);*/
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
