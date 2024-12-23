package ikklos.ofindexbackend.controller;

import ikklos.ofindexbackend.repository.CommentRepository;
import ikklos.ofindexbackend.repository.PostRepository;
import ikklos.ofindexbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping(value = "/forum/comments",produces = "application/json")
public class ForumCommentsController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public static class PostCommentItem{
        public Integer commentId;
        public Integer userId;
        public String userName;
        public String userAvatar;
        //TODO add comments
    }

    public static class PostCommentsResponse{
        public Integer postId;
    }

    public ForumCommentsController(@Autowired PostRepository postRepository,
                                   @Autowired UserRepository userRepository,
                                   @Autowired CommentRepository commentRepository){

        this.postRepository=postRepository;
        this.userRepository=userRepository;
        this.commentRepository=commentRepository;
    }

    //@GetMapping("/{postid}")
    //public


}
