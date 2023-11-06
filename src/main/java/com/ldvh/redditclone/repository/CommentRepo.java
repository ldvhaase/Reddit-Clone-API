package com.ldvh.redditclone.repository;

import com.ldvh.redditclone.model.Comment;
import com.ldvh.redditclone.model.Post;
import com.ldvh.redditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}