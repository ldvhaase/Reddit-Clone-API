package com.ldvh.redditclone.repository;

import com.ldvh.redditclone.model.Post;
import com.ldvh.redditclone.model.Subreddit;
import com.ldvh.redditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepo extends JpaRepository<Post, Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}
