package com.example.arash.tj;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.onesignal.OneSignal;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class MainActivity extends AppCompatActivity {

    public  String BodyHtml;
    public String NameAsli;
    public String StudentCode;
    ProgressBar prg;
    int i = 0;
    final Context myApp = this;
    Button loginbtn;

    CheckBox checkBox;
    Typeface custom_font ;

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
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        setContentView(R.layout.activity_main);
        ActionBar m_myActionBar=getSupportActionBar();

        m_myActionBar.hide();
          custom_font = Typeface.createFromAsset(getAssets(),  "fonts/IRANSansMobile.ttf");

        loginbtn= (Button)findViewById(R.id.button3);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        checkBox.setTypeface(custom_font);
        loginbtn.setTypeface(custom_font);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else {
            View decorView = getWindow().getDecorView();
            // Hide Status Bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
         prg = (ProgressBar)findViewById(R.id.progressBar3);
    }


    public void loginButton(View v)
    {
        prg.setVisibility(View.VISIBLE);
        webView = (WebView) findViewById(R.id.web1);
        webView.loadUrl("http://enroll.azad.ac.ir/login.aspx");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(final WebView view, String url) {

                EditText text1 = (EditText) findViewById(R.id.editText4);
                EditText text2 = (EditText) findViewById(R.id.editText5);
                final String pwd = text1.getText().toString();
                final String user = text2.getText().toString();
                Log.i("salam2" , user);
                Log.i("salam2" , pwd);

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

                        view.loadUrl("javascript:document.getElementById('TxtUserName').value = '"+ user +"';" +
                                "document.getElementById('TxtPassWord').value='"+ pwd +"';"+
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


    public void setUserData()
    {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.container));

                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText(" خوش آمدید "+NameAsli);

                text.setTypeface(custom_font);
                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.BOTTOM , 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();


                Intent myIntent = new Intent(MainActivity.this, FirstPage.class);
                myIntent.putExtra("nam" , NameAsli); //Optional parameters
                myIntent.putExtra("daneshjuyi" , StudentCode); //Optional parameters
                MainActivity.this.startActivity(myIntent);
            }
        }, 1000);
    }


}