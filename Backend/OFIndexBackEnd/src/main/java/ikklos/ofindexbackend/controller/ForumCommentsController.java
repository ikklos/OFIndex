package ikklos.ofindexbackend.controller;

import ikklos.ofindexbackend.domain.*;
import ikklos.ofindexbackend.repository.*;
import ikklos.ofindexbackend.utils.JwtUtils;
import ikklos.ofindexbackend.utils.UniversalBadReqException;
import ikklos.ofindexbackend.utils.UniversalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping(value = "/forum/comments",produces = "application/json")
public class ForumCommentsController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ForumMessageRepository forumMessageRepository;
    private final UserCommentLikeRepository userCommentLikeRepository;

    public static class CommentItem{
        public Integer commentId;
        public Integer userId;
        public String userName;
        public String userAvatar;
        public String text;
        public Integer likes;
        public Boolean liked;
        public LocalDateTime createTime;

        public CommentItem(CommentModel model, UserModel userModel, UserCommentLikeRepository repository){
            commentId=model.getCommentId();
            userId=model.getUserId();
            userName=userModel.getUsername();
            userAvatar=userModel.getAvatar();
            text=model.getText();
            likes=repository.countAllByCommentId(commentId);
            liked=repository.existsByUserIdAndCommentId(userId,commentId);
            createTime=model.getTimeStamp();
        }
    }

    public static class PostCommentItem extends CommentItem{
        public List<CommentItem> comments;
        public Integer count;
        public PostCommentItem(CommentModel model, UserModel userModel, UserCommentLikeRepository repository) {
            super(model, userModel,repository);
            comments=new ArrayList<>();
        }
    }

    public static class PostCommentsResponse{
        public Integer postId;
        public List<PostCommentItem> comments;
        public Integer count;
        public Integer total;
    }

    public static class AddCommentRequest{
        public Integer postId;
        public String text;
        public Integer parent;
    }

    @Autowired
    public ForumCommentsController(PostRepository postRepository,
                                   UserRepository userRepository,
                                   CommentRepository commentRepository,
                                   ForumMessageRepository forumMessageRepository,
                                   UserCommentLikeRepository userCommentLikeRepository){

        this.postRepository=postRepository;
        this.userRepository=userRepository;
        this.commentRepository=commentRepository;
        this.forumMessageRepository = forumMessageRepository;
        this.userCommentLikeRepository = userCommentLikeRepository;
    }

    @GetMapping("/{postid}")
    public PostCommentsResponse getPostComments(@PathVariable Integer postid) throws UniversalBadReqException{

        if(!postRepository.existsById(postid))
            throw new UniversalBadReqException("No such post");

        List<CommentModel> comments=commentRepository.findCommentModelsByPostId(postid, Sort.by(Sort.Direction.DESC,"timeStamp"));

        PostCommentsResponse response=new PostCommentsResponse();
        response.postId=postid;
        response.comments=comments.stream().map(
            commentModel -> {
                if(commentModel.getParentComment()!=null)return null;

                var userO=userRepository.findById(commentModel.getUserId());
                if(userO.isEmpty())return null;
                UserModel userModel=userO.get();

                PostCommentItem item=new PostCommentItem(commentModel,userModel,userCommentLikeRepository);
                item.comments=comments.stream().map(commentModel1 -> {
                    if(!Objects.equals(commentModel1.getParentComment(), commentModel.getCommentId()))return null;

                    var userO1=userRepository.findById(commentModel1.getUserId());
                    if(userO1.isEmpty())return null;
                    UserModel userModel1=userO1.get();

                    return new CommentItem(commentModel1,userModel1,userCommentLikeRepository);
                }).filter(Objects::nonNull).toList();
                item.count =item.comments.size();
                return item;
            }
        ).filter(Objects::nonNull).toList();
        response.count = response.comments.size();
        response.total = comments.size();
        return response;
    }

    @PostMapping("/add")
    public CommentItem addComment(@RequestBody AddCommentRequest request,
                                  @RequestHeader("Authorization") String token) throws UniversalBadReqException {
        Integer userId = JwtUtils.getUserIdJWT(token);

        var postO=postRepository.findById(request.postId);
        if(postO.isEmpty()){
            throw new UniversalBadReqException("No such post");
        }
        PostModel postModel=postO.get();

        CommentModel parentModel=null;

        if (request.parent != null) {
            var parentO=commentRepository.findById(request.parent);
            if(parentO.isEmpty())
                throw new UniversalBadReqException("No such parent comment");
            else if(parentO.get().getParentComment()!=null)
                throw new UniversalBadReqException("parent comment is a comment reply");
            else
                parentModel=parentO.get();
        }

        CommentModel commentModel = new CommentModel();
        commentModel.setUserId(userId);
        commentModel.setParentComment(request.parent);
        commentModel.setText(request.text);
        commentModel.setPostId(request.postId);
        commentModel.setTimeStamp(LocalDateTime.now());

        commentRepository.save(commentModel);

        ForumMessageModel.addForumMessage(forumMessageRepository,
                userId,postModel.getUserId(),3,
                "Made comment to you post:"+postModel.getPostId(),false);
        if(parentModel!=null){
            ForumMessageModel.addForumMessage(forumMessageRepository,
                    userId, parentModel.getUserId(), 3,
                    "Made comment to you comment:"+parentModel.getCommentId(),false);
        }

        var userO = userRepository.findById(userId);
        if (userO.isEmpty()) throw new UniversalBadReqException("User token illegal");

        return new CommentItem(commentModel, userO.get(),userCommentLikeRepository);
    }

    @GetMapping("/like/{commentId}")
    public UniversalResponse likeComment(@PathVariable Integer commentId,
                                      @RequestHeader("Authorization") String token) throws UniversalBadReqException{
        Integer userid= JwtUtils.getUserIdJWT(token);

        var commentO=commentRepository.findById(commentId);
        if(commentO.isEmpty()){
            throw new UniversalBadReqException("No such comment");
        }
        CommentModel commentModel=commentO.get();

        if(userCommentLikeRepository.existsByUserIdAndCommentId(userid,commentId)){
            throw new UniversalBadReqException("Already liked");
        }

        UserCommentLikeModel userCommentLikeModel=new UserCommentLikeModel();
        userCommentLikeModel.setCommentId(commentId);
        userCommentLikeModel.setUserId(userid);
        userCommentLikeModel.setLikeTime(LocalDateTime.now());
        userCommentLikeRepository.save(userCommentLikeModel);

        ForumMessageModel.addForumMessage(forumMessageRepository,
                userid, commentModel.getUserId(), 1,"Liked your Comment:"+commentModel.getCommentId(),false);

        UniversalResponse response=new UniversalResponse();
        response.message="Comment liked";
        return response;
    }

    @DeleteMapping("/like/{commentId}")
    public UniversalResponse unlikeComment(@RequestHeader("Authorization") String token,
                                        @PathVariable Integer commentId) throws UniversalBadReqException {
        Integer userId= JwtUtils.getUserIdJWT(token);

        var likeO=userCommentLikeRepository.findUserPostLikeModelByCommentIdAndUserId(commentId,userId);
        if(likeO.isEmpty())
            throw new UniversalBadReqException("Not liked yet");

        userCommentLikeRepository.delete(likeO.get());

        UniversalResponse response=new UniversalResponse();
        response.message="Comment unliked";
        return response;
    }

}
