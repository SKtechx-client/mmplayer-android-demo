package com.skplanet.musicmall;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.sktechx.musicmate.lib.musicmallweb.MusicMallWebLibrary;
import com.sktechx.musicmate.lib.musicmallweb.MusicMateWebBridge;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;
import static com.skplanet.musicmall.R.id.webView;


public class TestAppActivity extends AppCompatActivity {
    private static final String TAG = "TestAppActivity";

    public static final String KEY_FROM_PLAYER = "KEY_FROM_PLAYER";

    private WebView mWebView;
    private MusicMateWebBridge mWebBridge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webview_main);

        mWebView = (WebView)findViewById(webView);
        initWebView();

        // musicmallweb 라이브러리 초기화
        mWebBridge = new MusicMateWebBridge();
        mWebBridge.init(this, mWebView);

        // 3G/LTE 셀룰러 데이터 허용 설정. default: true
        if (!MusicMallWebLibrary.isAllowCellularNetwork()) {
            MusicMallWebLibrary.setAllowCellularNetwork(true);
        }

        // TODO: 상용 서버와 사용자 아이디 정보를 포함하여 url을 생성합니다.
        String url = BuildConfig.MUSIC_MALL_SERVER;
        String userId = BuildConfig.MUSIC_MALL_USER_ID;

        if (!TextUtils.isEmpty(userId)) {
            url += "?user_id=" + userId;
        }

        Log.d(TAG, "MusicMall Web url: " + url);

        mWebView.loadUrl(url);


        // 로그아웃 테스트
        boolean testLogout = false;

        if (testLogout) {
            // 앱 실행 10초 후에 로그아웃
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!TestAppActivity.this.isFinishing()) {
                        new AlertDialog.Builder(TestAppActivity.this)
                                .setMessage(R.string.logout_confirm)
                                .setNegativeButton(R.string.cancel, null)
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 로그아웃
                                        MusicMallWebLibrary.logout();
                                    }
                                })
                                .show();
                    }

                }
            }, 10000);
        }

        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent != null) {
            super.onNewIntent(intent);
            boolean isFromPlayer = intent.getBooleanExtra(KEY_FROM_PLAYER, false);

            if (isFromPlayer) {
                Toast.makeText(this, "Launched from global player", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mWebBridge != null) {
            mWebBridge.onStart();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mWebBridge != null) {
            mWebBridge.onStop();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 모든 back key 를 웹뷰가 처리하도록 합니다.
        // 액티비티 종료는 웹뷰에서 lms://close 호출로 이루어집니다.
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (mWebBridge != null) {
                mWebBridge.response("onBack", null);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initWebView() {
        WebSettings settings = mWebView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("lms://close")) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(TestAppActivity.this)
                                    .setMessage(R.string.exit_confirm)
                                    .setNegativeButton(R.string.cancel, null)
                                    .setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                                    .show();
                        }
                    });
                    return true;
                }
                return false;
            }
        });
    }
}
