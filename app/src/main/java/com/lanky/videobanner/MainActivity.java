package com.lanky.videobanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;
import android.widget.VideoView;

import com.lanky.videobanner.utils.CommonUtils;
import com.lanky.videobanner.video.VideoChooser;
import com.lanky.videobanner.video.VideoListFinder;

import java.util.List;

import static com.lanky.videobanner.GlobalInfo.Constants.TAG;

public class MainActivity extends AppCompatActivity {
    VideoView videoView;
    private Context mContext;
    GlobalInfo globalInfo = new GlobalInfo();
    MyReceiver mReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        registerMyReceiver();
        initView();
    }

    private void initView() {
        videoView = findViewById(R.id.main_video_view);
    }

    @Override
    protected void onResume() {
        if (CommonUtils.requestPms(mContext)) {
            Log.d(TAG, "onResume: pms granted");
            initVideoList();
        } else {
            Log.w(TAG, "onResume: pms not granted!");
        }
        super.onResume();
    }

    private void registerMyReceiver(){
        mReceiver =new MyReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction(GlobalInfo.Constants.ACTION_NAME);
        MainActivity.this.registerReceiver(mReceiver,filter);
    }

    private void initVideoList(){
        VideoListFinder videoListFinder = new VideoListFinder(mContext);
        globalInfo.setPlaying_path_list(videoListFinder.getVideo_path_list());
        for (String s : globalInfo.getPlaying_path_list()) {
            Log.d(TAG, "onResume: setPlaying_path_list:" + s);
        }
        if (globalInfo.getPlaying_path_list().size() == 0) {
            Log.d(TAG, "initVideoList: no video found,retry");
            Toast.makeText(mContext, "Waiting video...", Toast.LENGTH_SHORT).show();
        } else {
            setVideoFileList(globalInfo.getPlaying_path_list());
        }
    }

    public void setVideoFileList(List<String> playing_video_path_list) {
        final int[] i = {0};
        videoView.setVideoURI(Uri.parse(playing_video_path_list.get(i[0])));
        videoView.start();
        videoView.setOnCompletionListener(mp -> {
            i[0]++;
            if (i[0] == playing_video_path_list.size()) {
                i[0] = 0;
            }
            Log.d(TAG, "start play: " + i[0] + ",path:" + playing_video_path_list.get(i[0]));
            videoView.setVideoURI(Uri.parse(playing_video_path_list.get(i[0])));
            videoView.start();
        });
        videoView.setOnErrorListener((mp, what, extra) -> {
            Log.e(TAG, "videoView onError: play error" + what + "-" + extra);
            if (extra == 0) {
                Toast.makeText(mContext, "U盘连接异常", Toast.LENGTH_SHORT).show();
                videoView.setOnCompletionListener(mp1 -> videoView.suspend());
                videoView.setOnErrorListener((mp12, what1, extra1) -> {
                    videoView.suspend();
                    return true;
                });
            }
            i[0]++;
            if (i[0] > playing_video_path_list.size()) {
                i[0] = 0;
            }
            Log.d(TAG, "start play: " + i[0] + ",path:" + playing_video_path_list.get(i[0]));
            videoView.setVideoURI(Uri.parse(playing_video_path_list.get(i[0])));
            videoView.start();
            return true;
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_DPAD_DOWN:
                VideoChooser.getInstance(mContext).selectVideo();
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                Log.d(TAG, "onKeyDown: center");
                if (videoView.isPlaying()) {
                    videoView.pause();
                } else {
                    videoView.start();
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            videoView.start();
        } else {
            videoView.pause();
        }
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle=intent.getExtras();
            int usb_mount = bundle.getInt(GlobalInfo.Constants.RECEIVER_KEY_USB_MOUNT);
            if (GlobalInfo.Constants.RECEIVER_VALUE_USB_MOUNTED == usb_mount || GlobalInfo.Constants.RECEIVER_VALUE_USB_UNMOUNTED == usb_mount) {
                Log.d(TAG, "onReceive: USB mount status changed,refresh video list!");
                initVideoList();
                if (videoView.isPlaying()) {
                    videoView.suspend();
                }
            }

            boolean video_selected = bundle.getBoolean(GlobalInfo.Constants.RECEIVER_KEY_VIDEO_SELECTED);
            if (video_selected) {
                Log.d(TAG, "onReceive: video selected,refresh video list!");
                setVideoFileList(VideoChooser.getInstance(context).getList_selected());
            }
        }
    }
}