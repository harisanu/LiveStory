package com.happicouch.livestory.models;

public class StoryListView {
    private String storyTitle;
    private byte[] storyThumbnail;

    public StoryListView(String storyTitle, byte[] storyThumbnail) {
        this.storyTitle = storyTitle;
        this.storyThumbnail = storyThumbnail;
    }

    public String getStoryTitle() {
        return storyTitle;
    }

    public byte[] getStoryThumbnail() {
        return storyThumbnail;
    }
}
