package com.example.arash.tj;


import android.app.Activity;
import android.content.DialogInterface;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.widget.EditText;
import android.widget.ProgressBar;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;



public class GovahiEshteghal extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String pageCode;
    private List<Govahi> govahiList = new ArrayList<>();
    private RecyclerView recyclerView;
    private GovahiAdapter mAdapter;
    ProgressBar prg;
     WebView webView;
    public GovahiEshteghal() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GovahiEshteghal.
     */
    // TODO: Rename and change types and number of parameters
    public static GovahiEshteghal newInstance(String param1, String param2) {
        GovahiEshteghal fragment = new GovahiEshteghal();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_govahi_eshteghal, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        prg = (ProgressBar)view.findViewById(R.id.progressBar2);

        webView = (WebView) view.findViewById(R.id.web4);
        webView.loadUrl("http://enroll.azad.ac.ir/Pages/Daneshjoee/Frm_DocumentRequest.aspx");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.addJavascriptInterface(new GovahiEshteghal.MyJavaScriptInterface(), "HTMLOUT");
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(final WebView view, String url) {
                webView.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recycle1);
        mAdapter = new GovahiAdapter(govahiList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* showChangeLangDialog();*/
            }
        });


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
              /* Find Tablelayout defined in main.xml */

                Document PageHtml = Jsoup.parse(pageCode);

                Elements trs = PageHtml.getElementById("GridStudentRequest").getElementsByTag("tr");
                int i = 0;
                for(Element tr : trs)
                {
                    if (i == 0)
                    {

                    }
                    else
                    {
                        Element td = tr.getElementsByTag("td").get(4);
                        Element status = tr.getElementsByTag("td").get(3);
                        Govahi movie = new Govahi(getActivity() , td.text() , status.text());
                        govahiList.add(movie);
                    }
                    i++;
                }
                mAdapter.notifyDataSetChanged();
            }
        }, 2000);
    }

/*
    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_layout, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.editText);

        dialogBuilder.setTitle("درخواست گواهی اشتغال به تحصیل");
        dialogBuilder.setPositiveButton("افزودن درخواست", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
               prg.setVisibility(View.VISIBLE);

               webView.loadUrl("http://enroll.azad.ac.ir/Pages/Daneshjoee/Frm_DocumentRequest.aspx");
                webView.setWebViewClient(new WebViewClient() {
                    public void onPageFinished(final WebView view, String url) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                view.loadUrl("javascript:document.getElementById('Txt_Remark').value = '"+ edt.getText() +"';" +
                                        "document.getElementById('Cmd_Save').click();" );
                            }
                        }, 1000);
                    }
                });
                Fragment frg = null;
                frg = getFragmentManager().findFragmentByTag("GovahiEshteghal");
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
            }
        });
        dialogBuilder.setNegativeButton("لغو", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }
*/

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



