package com.ldvh.redditclone.repository;

import com.ldvh.redditclone.model.Post;
import com.ldvh.redditclone.model.User;
import com.ldvh.redditclone.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepo extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
