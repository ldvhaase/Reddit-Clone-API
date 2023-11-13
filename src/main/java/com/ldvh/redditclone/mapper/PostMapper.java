package com.ldvh.redditclone.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.ldvh.redditclone.dto.PostRequest;
import com.ldvh.redditclone.dto.PostResponse;
import com.ldvh.redditclone.model.Post;
import com.ldvh.redditclone.model.Subreddit;
import com.ldvh.redditclone.model.User;
import com.ldvh.redditclone.model.Vote;
import com.ldvh.redditclone.model.VoteType;
import com.ldvh.redditclone.repository.CommentRepo;
import com.ldvh.redditclone.repository.VoteRepo;
import com.ldvh.redditclone.service.AuthService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.ldvh.redditclone.model.VoteType.DOWNVOTE;
import static com.ldvh.redditclone.model.VoteType.UPVOTE;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private CommentRepo commentRepository;
    @Autowired
    private VoteRepo voteRepository;
    @Autowired
    private AuthService authService;


    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "subreddit", source = "subredditName")
    @Mapping(target = "voteCount", constant = "0")
    @Mapping(target = "user", source = "user")
    public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    @Mapping(target = "upVote", expression = "java(isPostUpVoted(post))")
    @Mapping(target = "downVote", expression = "java(isPostDownVoted(post))")
    public abstract PostResponse mapToDto(Post post);

    Integer commentCount(Post post) {
        return commentRepository.findByPost(post).size();
    }

    String getDuration(Post post) {
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }

    boolean isPostUpVoted(Post post) {
        return checkVoteType(post, UPVOTE);
    }

    boolean isPostDownVoted(Post post) {
        return checkVoteType(post, DOWNVOTE);
    }

    private boolean checkVoteType(Post post, VoteType voteType) {
        if (authService.isLoggedIn()) {
            Optional<Vote> voteForPostByUser =
                    voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post,
                            authService.getCurrentUser());
            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType))
                    .isPresent();
        }
        return false;
    }

}