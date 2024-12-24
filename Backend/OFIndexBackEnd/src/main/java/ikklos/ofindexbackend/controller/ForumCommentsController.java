package ikklos.ofindexbackend.controller;

import ikklos.ofindexbackend.domain.CommentModel;
import ikklos.ofindexbackend.domain.ForumMessageModel;
import ikklos.ofindexbackend.domain.PostModel;
import ikklos.ofindexbackend.domain.UserModel;
import ikklos.ofindexbackend.repository.CommentRepository;
import ikklos.ofindexbackend.repository.ForumMessageRepository;
import ikklos.ofindexbackend.repository.PostRepository;
import ikklos.ofindexbackend.repository.UserRepository;
import ikklos.ofindexbackend.utils.JwtUtils;
import ikklos.ofindexbackend.utils.UniversalBadReqException;
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

    public static class CommentItem{
        public Integer commentId;
        public Integer userId;
        public String userName;
        public String userAvatar;
        public String text;
        public Integer likes;
        public LocalDateTime createTime;

        public CommentItem(CommentModel model, UserModel userModel){
            commentId=model.getCommentId();
            userId=model.getUserId();
            userName=userModel.getUsername();
            userAvatar=userModel.getAvatar();
            text=model.getText();
            likes=model.getLikes();
            createTime=model.getTimeStamp();
        }
    }

    public static class PostCommentItem extends CommentItem{
        public List<CommentItem> comments;
        public Integer count;
        public PostCommentItem(CommentModel model, UserModel userModel) {
            super(model, userModel);
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

    public ForumCommentsController(@Autowired PostRepository postRepository,
                                   @Autowired UserRepository userRepository,
                                   @Autowired CommentRepository commentRepository, ForumMessageRepository forumMessageRepository){

        this.postRepository=postRepository;
        this.userRepository=userRepository;
        this.commentRepository=commentRepository;
        this.forumMessageRepository = forumMessageRepository;
    }

    @GetMapping("/{postid}")
    public PostCommentsResponse getPostComments(@PathVariable Integer postid) throws UniversalBadReqException{

        if(!postRepository.existsById(postid))
            throw new UniversalBadReqException("No such post");

        List<CommentModel> comments=commentRepository.findCommentModelsByPostId(postid, Sort.by(Sort.Direction.ASC,"timeStamp"));

        PostCommentsResponse response=new PostCommentsResponse();
        response.postId=postid;
        response.comments=comments.stream().map(
            commentModel -> {
                if(commentModel.getParentComment()!=null)return null;

                var userO=userRepository.findById(commentModel.getUserId());
                if(userO.isEmpty())return null;
                UserModel userModel=userO.get();

                PostCommentItem item=new PostCommentItem(commentModel,userModel);
                item.comments=comments.stream().map(commentModel1 -> {
                    if(!Objects.equals(commentModel1.getParentComment(), commentModel.getCommentId()))return null;

                    var userO1=userRepository.findById(commentModel1.getUserId());
                    if(userO1.isEmpty())return null;
                    UserModel userModel1=userO1.get();

                    return new CommentItem(commentModel1,userModel1);
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
        commentModel.setLikes(0);
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

        return new CommentItem(commentModel, userO.get());
    }
}
