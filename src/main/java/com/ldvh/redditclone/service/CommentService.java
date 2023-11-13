package com.ldvh.redditclone.service;

import com.ldvh.redditclone.dto.CommentsDto;
import com.ldvh.redditclone.exception.PostNotFoundException;
import com.ldvh.redditclone.exception.RedditException;
//import com.ldvh.redditclone.mapper.CommentMapper;
import com.ldvh.redditclone.model.Comment;
import com.ldvh.redditclone.model.NotificationEmail;
import com.ldvh.redditclone.model.Post;
import com.ldvh.redditclone.model.User;
import com.ldvh.redditclone.repository.CommentRepo;
import com.ldvh.redditclone.repository.PostRepo;
import com.ldvh.redditclone.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {
    private static final String POST_URL = "";
    private final PostRepo postRepo;
    private final UserRepo userRepo;
    private final AuthService authService;
//    private final CommentMapper commentMapper;
    ModelMapper mapper = new ModelMapper();
    private final CommentRepo commentRepo;
    private final MailBuilder mailContentBuilder;
    private final MailService mailService;

    public void save(CommentsDto commentsDto) {
        Post post = postRepo.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
        Comment comment = mapper.map(commentsDto, Comment.class); //commentMapper.map(commentsDto, post, authService.getCurrentUser());
        commentRepo.save(comment);

        String message = mailContentBuilder.build(post.getUser().getUsername() + " posted a comment on your post." + POST_URL);
        sendCommentNotification(message, post.getUser());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(), message));
    }

    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        Post post = postRepo.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
        List<Comment> comment = commentRepo.findByPost(post);
        List<CommentsDto> commentsDtosList = null;
        for(Comment commentIn: comment) {
            commentsDtosList.add(mapper.map(commentIn, CommentsDto.class));
        }

        return commentsDtosList;

//        return commentRepo.findByPost(post)
//                .stream()
//                .map(mapper::).collect(Collectors.toList());
    }

    public List<CommentsDto> getAllCommentsForUser(String userName) {
        User user = userRepo.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        List<Comment> commentList = commentRepo.findAllByUser(user);
        List<CommentsDto> commentsDtoList = null;
        for(Comment commentIn: commentList){
            commentsDtoList.add(mapper.map(commentIn, CommentsDto.class));
        }

        return commentsDtoList;


//        return commentRepo.findAllByUser(user)
//                .stream()
//                .map(commentMapper::mapToDto)
//                .collect(Collectors.toList());
    }

    public boolean containsSwearWords(String comment) {
        if (comment.contains("shit")) {
            throw new RedditException("Comments contains unacceptable language");
        }
        return false;
    }
}
