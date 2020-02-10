package com.example.samsungblockchainsdk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;



// 기본 환경설정: 커널번호 10번 클릭하고 usb디버깅 허용으로 바꾸기, keystore 업데이트를 하고 계정 생성수 메타마스크 로그아웃후  연동시키기! 테스트넷에 이더받고 그걸로 실행하는 환경 구축하기
public class MainActivity extends AppCompatActivity {
    // MediaPlayer 객체생성
    MediaPlayer mediaPlayer;

    // 시작버튼
    Button startButton;
    //종료버튼
    Button stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // MediaPlayer 객체 할당
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.sound1);
        mediaPlayer.start();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void firsted(View view){
        Toast.makeText(getApplicationContext(),"버튼이 눌러졌습니다.",Toast.LENGTH_SHORT).show();
        Intent in = new Intent(MainActivity.this, Main2Activity.class);
        startActivity(in);

    }

    public void homePage(View view){
        Toast.makeText(getApplicationContext(),"버튼이 눌러졌습니다.",Toast.LENGTH_SHORT).show();
        Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse("https://developer.samsung.com/blockchain/platform/programming-guide/connect-with-hardware-wallet.html"));
        startActivity(in);

    }
    public void played(View view){
        // MediaPlayer 객체 할당
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.sound1);
        mediaPlayer.start();

    }

    public void stopped(View view){
        // 정지버튼
        mediaPlayer.stop();
        // 초기화
        mediaPlayer.reset();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // MediaPlayer 해지
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
