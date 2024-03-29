package com.daarulhijrah.kitabkuning.Fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.daarulhijrah.kitabkuning.R;

public class FragmentInfoTab3_ContactUs extends Fragment {

    private WebView webview;
    EditText emailSubject = null;
    EditText emailBody = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact, container, false);

        emailSubject = (EditText) rootView.findViewById(R.id.subject);
        emailBody = (EditText) rootView.findViewById(R.id.body);

        Button btnSend = (Button) rootView.findViewById(R.id.send);
        Button btnChatWa = (Button) rootView.findViewById(R.id.button_whatsapp);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = emailSubject.getText().toString();
                String message = emailBody.getText().toString();

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.app_email)});
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message);

                // need this to prompts email client only
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Choose an Email client"));
            }
        });

        btnChatWa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://wa.me/6287878697644?text=Melaporkan%20Issue%20pada%20Aplikasi%20"+getResources().getString(R.string.app_name));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        webview = (WebView) rootView.findViewById(R.id.webview1);

        // web settings
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setBuiltInZoomControls(false);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        webview.getSettings().setDefaultTextEncodingName("UTF-8");
        WebSettings webSettings = webview.getSettings();
        Resources res = getResources();
        int fontSize = res.getInteger(R.integer.font_size);
        webSettings.setDefaultFontSize(fontSize);

        webview.setWebViewClient(new myWebClient());
//        webview.loadUrl("file:///android_asset/html/contact_us.html");
        webview.loadUrl("https://kitabkuning.id/apps/html/tab3.html");

        webview.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
                    webview.goBack();
                    return true;
                }
                return false;
            }
        });

        return rootView;
    }

    public class myWebClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_LONG).show();
            view.loadUrl("about:blank");
        }

    }

}
