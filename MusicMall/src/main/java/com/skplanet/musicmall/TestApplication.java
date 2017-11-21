package com.skplanet.musicmall;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import com.skplanet.musicmate.model.source.remote.Host;
import com.skplanet.musicmate.util.MMLog;
import com.sktechx.musicmate.lib.mediaplayer.MMMediaPlayerLibrary;
import com.sktechx.musicmate.lib.mediaplayer.model.source.remote.RemoteSource;
import com.sktechx.musicmate.lib.musicmallweb.MusicMallWebLibrary;

/**
 * Created by 1100496 on 2017. 10. 19..
 */

public class TestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 디버그 로그를 출력할 지 설정
        MMLog.setDebuggable(BuildConfig.DEBUG);

        // musicmallweb 라이브러리를 초기화
        MusicMallWebLibrary.init(this, "MUSIC_MALL");

        // global player를 터치 했을 경우 실행할 Intent를 설정. default: null, null로 설정한 경우 현재 앱의 LaunchIntent를 사용.
        MMMediaPlayerLibrary.getConfig().setGlobalPlayerIntent(new Intent(this, TestAppActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .putExtra(TestAppActivity.KEY_FROM_PLAYER, true));

        // global player의 로고 이미지를 설정. default: null, null로 설정한 경우 기본 이미지를 사용.
        MMMediaPlayerLibrary.getConfig().setGlobalPlayerLogo(R.drawable.global_player_logo);

        // 재생시 status bar에 나타나는 아이콘 설정. default: null, null로 설정한 경우 기본 이미지를 사용.
        MMMediaPlayerLibrary.getConfig().setStatusbarSmallIcon(R.drawable.statusbar_small_icon);
    }
}

