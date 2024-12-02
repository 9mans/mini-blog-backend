package com.example.miniblog.service;

import com.example.miniblog.entity.Post;
import com.example.miniblog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        // 모든 게시글을 리스트로 반환
        return postRepository.findAll();
    }
}

