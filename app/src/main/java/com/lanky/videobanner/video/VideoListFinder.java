package com.lanky.videobanner.video;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * LankyBin create on 2020/11/23
 */
public class VideoListFinder {
    private Context mContext;

    private List<VideoFileEntity> video_file_list = new ArrayList<>();

    public VideoListFinder(Context context) {
        mContext = context;
        video_file_list = getVideoFiles();
    }

//    private static VideoListFinder mVideoListFinder = null;
//
//    public static VideoListFinder getInstance(Context context) {
//        if (mVideoListFinder == null) {
//            mVideoListFinder = new VideoListFinder(context);
//        }
//        return mVideoListFinder;
//    }

    private List<String> video_path_list = new ArrayList<>();
    private List<String> video_name_list = new ArrayList<>();

    public List<String> getVideo_path_list() {
        video_path_list.clear();
        for (VideoFileEntity videoFile : video_file_list) {
            video_path_list.add(videoFile.getPath());
        }
        return video_path_list;
    }

    public List<String> getVideo_name_list() {
        video_name_list.clear();
        for (VideoFileEntity videoFile : video_file_list) {
            video_name_list.add(videoFile.getPath());
        }
        return video_name_list;
    }

    private List<VideoFileEntity> getVideoFiles() {
        List<VideoFileEntity> files = new ArrayList<>();
        ContentResolver contentResolver = mContext.getContentResolver();
        try (Cursor c = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null)) {
            int columnIndexOrThrow_DATA = c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
            int columnIndexOrThrow_SIZE = c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE);

            while (c.moveToNext()) {
                String path = c.getString(columnIndexOrThrow_DATA);
//                Log.d(TAG, "path:" + path);
                int position_do = path.lastIndexOf(".");
                if (position_do == -1) {
                    continue;
                }
                int position_x = path.lastIndexOf(File.separator);
                if (position_x == -1) {
                    continue;
                }
                String displayName = path.substring(position_x + 1);
                long size = c.getLong(columnIndexOrThrow_SIZE);
                VideoFileEntity info = new VideoFileEntity();
                info.setName(displayName);
                info.setPath(path);
                info.setSize(size);
                files.add(info);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }
}
