package com.lanky.videobanner;

import java.util.ArrayList;
import java.util.List;

/**
 * LankyBin create on 2020/11/23
 */
public class GlobalInfo {
    private List<String> playing_path_list = new ArrayList<>();

    public List<String> getPlaying_path_list() {
        return playing_path_list;
    }

    public void setPlaying_path_list(List<String> playing_path_list) {
        this.playing_path_list = playing_path_list;
    }

    public static class Constants{
        public static final String TAG = "lanky.videobanner";

        public static final String ACTION_NAME = "com.lanky.videobanner";
        public static final String RECEIVER_KEY_USB_MOUNT = "USB_MOUNT";
        public static final int RECEIVER_VALUE_USB_MOUNTED = 0;
        public static final int RECEIVER_VALUE_USB_UNMOUNTED = 1;
        public static final String RECEIVER_KEY_VIDEO_SELECTED = "VIDEO_SELECTED";
    }
}
