package com.ldvh.redditclone.repository;

import com.ldvh.redditclone.model.Subreddit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubredditRepo extends JpaRepository<Subreddit, Long> {

    Optional<Subreddit> findByName(String subredditName);
}