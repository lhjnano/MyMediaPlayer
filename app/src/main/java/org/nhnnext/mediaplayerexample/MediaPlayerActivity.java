package org.nhnnext.mediaplayerexample;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;

public class MediaPlayerActivity extends AppCompatActivity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl {
    /**
     * SufaceHolder.Callback : Suface의 Stream을 CallBack하는 클래스
     * MediaPlayer.OnPreparedListener : Media 영상을 내보내기전에 처리하는 리스너
     * MediaController.MediaPlayerControl : 영상을 제어할 수 있는 인터페이스
     */
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    MediaPlayer mediaPlayer;
    Button playButton;

    // add Controller
    MediaController mcontroller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediaplayer);

        // surfaceView 등록
        surfaceView = (SurfaceView) findViewById(R.id.surface);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        playButton = (Button)findViewById(R.id.play_btn);
        playButton.setOnClickListener(clickListener);

        Button stopButton = (Button) findViewById(R.id.stop_btn);
        stopButton.setOnClickListener(clickListener);

    }

    Button.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.play_btn) {
                Log.i("main", "play");
                startOrPause();
            } else if (v.getId() == R.id.stop_btn) {
                stopNprepare();
            }
        }
    };
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
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/too_cute");
            mediaPlayer.setDataSource(this, uri);

            // external URL : 외부 URL이나 path를 지정한 리소스
//            String path = "http://techslides.com/demos/sample-videos/small.mp4";
//            mediaPlayer.setDataSource(path);
            mediaPlayer.setDisplay(holder);                                    // 화면 호출
            mediaPlayer.prepare();                                             // 비디오 load 준비
            mediaPlayer.setOnCompletionListener(completionListener);        // 비디오 재생 완료 리스너
            // mediaPlayer.setOnVideoSizeChangedListener(sizeChangeListener);  // 비디오 크기 변경 리스너

            mcontroller = new MediaController(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setScreenOnWhilePlaying(true);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        /**
         * 비디오가 전부 재생된 상태의 리스너
         * @param mp : 현재 제어하고 있는 MediaPlayer
         */
        @Override
        public void onCompletion(MediaPlayer mp) {
            playButton.setText("Play");
        }
    };

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

    private void startOrPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playButton.setText("Play");
        } else {
            mediaPlayer.start();
            playButton.setText("Pause");
        }
    }

    private void stopNprepare() {
        MovieAPIAdapter movieAPIAdapter = new MovieAPIAdapter();
        movieAPIAdapter.execute();
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
}
