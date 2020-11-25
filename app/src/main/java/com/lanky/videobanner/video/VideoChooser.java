package com.lanky.videobanner.video;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.lanky.videobanner.GlobalInfo;
import com.lanky.videobanner.R;

import java.util.ArrayList;
import java.util.List;

import static com.lanky.videobanner.GlobalInfo.Constants.TAG;

/**
 * LankyBin create on 2020/11/23
 */
public class VideoChooser {
    private Context mContext;
    private AlertDialog.Builder builder;
    VideoListFinder mVideoListFinder;
    private List<String> list_all = new ArrayList<>();
    private List<String> list_selected = new ArrayList<>();

    private Intent intent_broadcast = new Intent();

    public VideoChooser(Context context) {
        this.mContext = context;
        builder = new AlertDialog.Builder(mContext);
        mVideoListFinder = new VideoListFinder(mContext);
    }

    public List<String> getList_selected() {
        return list_selected;
    }

    private static VideoChooser mVideoChooser = null;

    public static VideoChooser getInstance(Context context) {
        if (mVideoChooser == null) {
            mVideoChooser = new VideoChooser(context);
        }
        return mVideoChooser;
    }

    public void selectVideo() {
        list_all = mVideoListFinder.getVideo_path_list();
        final List<Integer> choice = new ArrayList<>();
        String[] items = list_all.toArray(new String[list_all.size()]);
        boolean[] isSelect = new boolean[list_all.size()];

        builder = new AlertDialog.Builder(mContext).setIcon(R.mipmap.app_icon)
                .setTitle("Select video to play")
                .setMultiChoiceItems(items, isSelect, (dialogInterface, i, b) -> {
                    if (b) {
                        choice.add(i);
                    } else {
                        choice.remove((Integer) i);
                    }
                }).setPositiveButton("confirm", (dialogInterface, i) -> {
                    Toast.makeText(mContext, "Selected " + choice.size() + " videos", Toast.LENGTH_SHORT).show();
                    if (choice.size() != 0) {
                        list_selected.clear();
                        for (Integer integer : choice) {
                            list_selected.add(items[integer]);
                        }
                        intent_broadcast.putExtra(GlobalInfo.Constants.RECEIVER_KEY_VIDEO_SELECTED, true);
                        intent_broadcast.setAction(GlobalInfo.Constants.ACTION_NAME);
                        mContext.sendBroadcast(intent_broadcast);
                    }
                });
        builder.create().show();
    }
}
