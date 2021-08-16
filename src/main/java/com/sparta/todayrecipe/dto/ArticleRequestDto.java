package com.sparta.todayrecipe.dto;

public class ArticleRequestDto {
//    private final String username; // 삭제
    private final String title;
    private final String content;
    private final String imageUrl;

    public ArticleRequestDto(String title, String content, String imageUrl) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }
}
