# mmplayer-android-demo
Music Mall Demo

* musicmallweb 라이브러리는 별도의 다운로드 없이 아래와 같은 방법으로 사용 가능합니다.

* project build.gradle file
```diff
apply plugin: 'com.android.application'

+  repositories {
+      maven { url 'https://sktechx-client.github.io/mmplayer-android' }
+  }

android {
  ...
}

dependencies {
+     compile 'com.sktechx:musicmallweb:[라이브러리 버전]'
}
```

* [라이브러리 버전 & 릴리즈 노트 확인](https://github.com/SKtechx-client/mmplayer-android/blob/master/RELEASE-NOTES.md)
* 그밖의 자세한 사용 방법은 MusicMall/build.gradle, TestAppActivity.java, TestApplication.java를 참고하십시오.
