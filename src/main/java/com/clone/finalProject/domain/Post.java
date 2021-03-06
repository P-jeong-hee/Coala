package com.clone.finalProject.domain;

import com.clone.finalProject.dto.postDto.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Table(indexes = {
        @Index(name = "searchCategory", columnList = "category"),
        @Index(name = "searchTitle", columnList = "postTitle")})
public class Post extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;

    @Column(nullable = false)
    private String postTitle;

    @Column(nullable = false)
    private String postComment;

    @Column
    private String postImg;

    @Column
    private String status;

    @Column
    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    public Post(PostRequestDto postRequestDto, User user) {
        this.postTitle = postRequestDto.getPostTitle();
        this.postComment = postRequestDto.getPostComment();
        this.postImg = postRequestDto.getPostImg();
        this.category = postRequestDto.getCategory();
        this.user = user;
        this.status = "noCheck";
    }

    public void update(PostRequestDto postRequestDto) {
        this.postTitle = postRequestDto.getPostTitle();
        this.postComment= postRequestDto.getPostComment();
        this.postImg=postRequestDto.getPostImg();
        this.category =postRequestDto.getCategory();
    }

    public void checkUpdate(String str) {
        this.status = str;
    }

}
