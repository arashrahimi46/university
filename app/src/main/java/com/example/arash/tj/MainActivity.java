package com.example.arash.tj;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainActivity extends Activity {

    public  String BodyHtml;
    public String NameAsli;
    public String StudentCode;
    int i = 0;
    final Context myApp = this;

    /* An instance of this class will be registered as a JavaScript interface */
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
            BodyHtml = html;
            Document firstPage = Jsoup.parse(html);
            Element nam = firstPage.getElementById("lbl_NameFamily");
            Element Stdcode = firstPage.getElementById("Lbl_Reg_No");

                NameAsli = nam.text();
                StudentCode= Stdcode.text();
        }
    }

    private static WebView webView;

    String currentUrl;
    String Html ;
    @SuppressLint("JavascriptInterface")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String zart;
        webView = (WebView) findViewById(R.id.web1);
        webView.loadUrl("http://enroll.azad.ac.ir/login.aspx");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(final WebView view, String url) {


                injectScriptFile(view, "script.js"); // see below ...

                // test if the script was loaded
                view.loadUrl("javascript:setTimeout(test(), 500)");
                // do your stuff here

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        webView.measure(View.MeasureSpec.makeMeasureSpec(
                                View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                        webView.layout(0, 0, webView.getMeasuredWidth(),
                                webView.getMeasuredHeight());


                        webView.setDrawingCacheEnabled(true);
                        webView.buildDrawingCache();
                        final Bitmap bm = Bitmap.createBitmap(webView.getMeasuredWidth(),
                                webView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

                        Canvas bigcanvas = new Canvas(bm);
                        Paint paint = new Paint();
                        int iHeight = bm.getHeight();
                        bigcanvas.drawBitmap(bm, 0, iHeight, paint);
                        webView.draw(bigcanvas);
                        System.out.println("1111111111111111111111="
                                + bigcanvas.getWidth());
                        System.out.println("22222222222222222222222="
                                + bigcanvas.getHeight());

                        String Captcha = TRecognize(bm);
                        if (Captcha == null || Captcha == "")
                        {
                            Captcha = "0";
                        }
                        String user="9419914254";
                        String pwd="13760020276389";
                        view.loadUrl("javascript:document.getElementById('TxtUserName').value = '"+user+"';" +
                                "document.getElementById('TxtPassWord').value='"+pwd+"';"+
                                "document.getElementById('Txt_Captcha').value='"+ Captcha +"';"+
                                "document.getElementById('Button1').click();" );
                    }
                }, 1000);

                if (Objects.equals(webView.getUrl(), "http://enroll.azad.ac.ir/homepage.aspx"))
                {
                    if (i == 0)
                    {
                        Log.i("zart" , "zart");
                        webView.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                        i++;
                        setUserData();
                    }
                }
                else
                {
                Log.i("zart", "zart2");

                }
            }

            private void injectScriptFile(WebView view, String scriptFile) {
                InputStream input;
                try {
                    input = getAssets().open(scriptFile);
                    byte[] buffer = new byte[input.available()];
                    input.read(buffer);
                    input.close();

                    // String-ify the script byte-array using BASE64 encoding !!!
                    String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
                    view.loadUrl("javascript:(function() {" +
                            "var parent = document.getElementsByTagName('head').item(0);" +
                            "var script = document.createElement('script');" +
                            "script.type = 'text/javascript';" +
                            // Tell the browser to BASE64-decode the string into your script !!!
                            "script.innerHTML = window.atob('" + encoded + "');" +
                            "parent.appendChild(script)" +
                            "})()");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    public String TRecognize(Bitmap bitmap)
    {
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        String imageText = "";

        Frame imageFrame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);

        for (int i = 0; i < textBlocks.size(); i++) {
            TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));
            imageText = textBlock.getValue();                   // return string
        }
        Log.i("salam" ,imageText);

        return imageText;
    }

    private class MyBrowser extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            currentUrl= url;
            Log.i("zarch" , currentUrl);
            return true;
        }
    }

    private void getName(String Captcha)
    {
        String user="9519910824";
        String pwd="13770021061963";
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                try {
                    Document doc = Jsoup.connect("http://enroll.azad.ac.ir/homepage.aspx").get();
                    String title = doc.title();
                    Elements links = doc.select("a[href]");

                    builder.append(title).append("\n");

                    for (Element link : links) {
                        builder.append("\n").append("Link : ").append(link.attr("href"))
                                .append("\n").append("Text : ").append(link.text());
                    }
                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(builder.toString());

                    }
                });
            }
        }).start();
    }

    public void setUserData()
    {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext()," خوش آمدید "+NameAsli,Toast.LENGTH_SHORT).show();
            }
        }, 1000);
    }






/*                if (bm != null) {
                    try {
                        String path = Environment.getExternalStorageDirectory()
                                .toString();
                        OutputStream fOut = null;
                        File file = new File(path, "/aaaa.png");
                        fOut = new FileOutputStream(file);

                        bm.compress(Bitmap.CompressFormat.PNG, 50, fOut);
                        fOut.flush();
                        fOut.close();
                        bm.recycle();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/


}