package com.lanky.videobanner.video;

/**
 * LankyBin create on 2020/11/23
 */
public class VideoFileEntity {
    private String Name;
    private String Path;
    private long Size;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public long getSize() {
        return Size;
    }

    public void setSize(long size) {
        Size = size;
    }
}
