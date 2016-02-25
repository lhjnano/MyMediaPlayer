package org.nhnnext.mediaplayerexample;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.MediaController;

public class MediaPlayerActivity extends AppCompatActivity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl {
    /**
     * SufaceHolder.Callback : Suface의 Stream을 CallBack하는 클래스
     * MediaPlayer.OnPreparedListener : Media 영상을 내보내기전에 처리하는 리스너
     * MediaController.MediaPlayerControl : 영상을 제어할 수 있는 인터페이스
     */
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private MediaPlayer mediaPlayer;

    // add Controller
    private MediaController mcontroller;

    // fileName
    private String filePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediaplayer);
        // send intent
        Intent intent = getIntent();
        Bundle bundle = (Bundle)intent.getExtras();
        String uuid = bundle.getString("uuid");

        // get file infomation [ filename, filepath ]
        FilePathManager filePathManager = FilePathManager.getInstance();
        String[] fileInfo = filePathManager.get(uuid);
        filePath = fileInfo[1];

        // surfaceView 등록
        surfaceView = (SurfaceView) findViewById(R.id.surface);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mcontroller.show();
        return false;
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        /**
         * surface 생성
         */
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            mediaPlayer.reset();
        }

        try {
            // local resource : raw에 포함시켜 놓은 리소스 호출
            Uri uri = Uri.parse(filePath);
            mediaPlayer.setDataSource(this, uri);

            // external URL : 외부 URL이나 path를 지정한 리소스
//            String path = "http://techslides.com/demos/sample-videos/small.mp4";
//            mediaPlayer.setDataSource(path);
            mediaPlayer.setDisplay(holder);                                    // 화면 호출
            mediaPlayer.prepare();                                             // 비디오 load 준비
            // mediaPlayer.setOnVideoSizeChangedListener(sizeChangeListener);  // 비디오 크기 변경 리스너

            mcontroller = new MediaController(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setScreenOnWhilePlaying(true);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    MediaPlayer.OnVideoSizeChangedListener sizeChangeListener = new MediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        }
    };

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int i) {
        mediaPlayer.seekTo(i);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    Handler handler;    // android.os.Handler
    @Override
    public void onPrepared(MediaPlayer mp) {
        mcontroller.setMediaPlayer(this);
        mcontroller.setAnchorView(findViewById(R.id.surface));
        mcontroller.setEnabled(true);

        handler = new Handler();
        handler.post(new Runnable() {
            public void run() {
                mcontroller.show();
            }
        });
    }


    // 인텐트 수신부

}
