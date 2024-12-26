package ikklos.ofindexbackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import ikklos.ofindexbackend.domain.ForumMessageModel;
import ikklos.ofindexbackend.domain.PostModel;
import ikklos.ofindexbackend.domain.UserModel;
import ikklos.ofindexbackend.domain.UserPostLikeModel;
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
@RequestMapping(value = "/forum",produces = "application/json")
public class ForumPostController {


    private final ForumMessageRepository forumMessageRepository;
    private final SubscriptionRepository subscriptionRepository;

    public static class PostAddResponse extends UniversalResponse{
        public Integer postId;
    }

    public static class PostAddRequest{
        public String title;
        public String text;
        public List<String> pictures;
        public List<String> tags;
    }

    public static class PostGetResponse extends UniversalResponse{
        public Integer postId;
        public Integer posterId;
        public String posterName;
        public String posterAvatar;
        public Integer bookId;
        public Integer packId;
        public List<String> tags;
        public String title;
        public String text;
        public List<String> images;
        public Integer likes;
        public Boolean liked;
        public LocalDateTime createTime;

        public void setByModel(UserModel userModel){
            posterAvatar= userModel.getAvatar();
            posterName= userModel.getUsername();
        }

        public PostGetResponse setByModel(PostModel postModel) throws JsonProcessingException {
            postId=postModel.getPostId();
            bookId=postModel.getBookId();
            packId=postModel.getPackId();
            posterId=postModel.getUserId();
            title=postModel.getTitle();
            text=postModel.getText();
            createTime =postModel.getTimeStamp();
            ObjectMapper objectMapper=new ObjectMapper();
            JavaType javaType=objectMapper.getTypeFactory().constructParametricType(List.class,String.class);
            tags= objectMapper.readValue(postModel.getTags(),javaType);
            images =objectMapper.readValue(postModel.getImageurls(),javaType);
            if(images==null)images=new ArrayList<>();
            return this;
        }

        public PostGetResponse setLikes(UserPostLikeRepository repository,Integer userId){
            likes=repository.countAllByPostId(postId);
            liked=userId!=null&&repository.existsByUserIdAndPostId(userId,postId);
            return this;
        }
    }

    public static class PostListResponse extends UniversalResponse{
        public Integer count;
        public Integer total;
        public List<PostGetResponse> posts;
    }

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserPostLikeRepository userPostLikeRepository;

    @Autowired
    public ForumPostController(PostRepository postRepository,
                               UserRepository userRepository,
                               UserPostLikeRepository userPostLikeRepository, ForumMessageRepository forumMessageRepository, SubscriptionRepository subscriptionRepository){

        this.postRepository=postRepository;
        this.userRepository=userRepository;
        this.userPostLikeRepository = userPostLikeRepository;
        this.forumMessageRepository = forumMessageRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @PostMapping("/post/add")
    public PostAddResponse addPost(@RequestBody PostAddRequest request,
                                     @RequestHeader("Authorization") String token) throws JsonProcessingException {
        Integer userId= JwtUtils.getUserIdJWT(token);
        PostAddResponse response=new PostAddResponse();

        PostModel postModel=new PostModel();
        postModel.setUserId(userId);
        postModel.setTitle(request.title);
        postModel.setText(request.text);
        ObjectMapper mapper = new ObjectMapper();
        String s0= mapper.writeValueAsString(request.pictures);
        postModel.setImageurls(s0!=null?s0:"[]");
        postModel.setTimeStamp(LocalDateTime.now());

        List<String> commonTags=new ArrayList<>();
        for(String tag: request.tags){
            if(tag.startsWith("bookid:")){
                Integer bookid=Integer.valueOf(tag.substring(7));
                postModel.setBookId(bookid);
            }else if(tag.startsWith("packid:")){
                Integer packid=Integer.valueOf(tag.substring(7));
                postModel.setPackId(packid);
            }else{
                commonTags.add(tag);
            }
        }
        String s = mapper.writeValueAsString(commonTags);
        postModel.setTags(s!=null?s:"[]");

        postRepository.save(postModel);

        subscriptionRepository.findSubscriptionModelsByFollowingId(userId).forEach(
                subscriptionModel -> ForumMessageModel.addForumMessage(forumMessageRepository,
                        userId,subscriptionModel.getFollowerId(),0,
                        "Posted:"+postModel.getPostId(),
                        subscriptionModel.getNotification()!=0)
        );

        response.postId= postModel.getPostId();
        response.message="Post added";
        return response;
    }

    @GetMapping("/post/{postid}")
    public PostGetResponse getPostContent(@PathVariable("postid") Integer postId,
                                          @RequestHeader(value = "Authorization",required = false) String token)throws JsonProcessingException, UniversalBadReqException {

        Integer userId=token!=null?JwtUtils.getUserIdJWT(token):null;

        var postOption=postRepository.findById(postId);

        if(postOption.isEmpty()){
            throw new UniversalBadReqException("No such post");
        }

        PostModel postModel=postOption.get();

        PostGetResponse response=new PostGetResponse().setByModel(postModel).setLikes(userPostLikeRepository,userId);
        var userOption=userRepository.findById(postModel.getUserId());
        if(userOption.isPresent()){
            UserModel userModel=userOption.get();
            response.setByModel(userModel);
        }
        response.message="Post found";
        return response;
    }

    @GetMapping("/posts")
    public PostListResponse getPostList(@RequestParam Integer page,
                                        @RequestParam Integer pagesize) throws UniversalBadReqException {
        List<PostModel> posts=postRepository.findAll(Sort.by(Sort.Direction.ASC,"timeStamp"));

        PostListResponse response=new PostListResponse();

        int from=page*pagesize;
        int to=from+pagesize;

        if(from<posts.size()){
            if(to>=posts.size())to=posts.size();

            response.posts=posts.subList(from,to).stream().map(postModel -> {
                try {
                    PostGetResponse item=new PostGetResponse().setByModel(postModel);
                    var userO=userRepository.findById(postModel.getUserId());
                    if(userO.isPresent()){
                        item.setByModel(userO.get());
                        item.setLikes(userPostLikeRepository,userO.get().getUserid());
                    }
                    return item;
                } catch (JsonProcessingException e) {
                    return null;
                }
            }).filter(Objects::nonNull).toList();
        }else
            response.posts=new ArrayList<>();
        response.count=response.posts.size();
        response.total=posts.size();
        return response;
    }

    @GetMapping("/post/like/{postId}")
    public UniversalResponse likePost(@RequestHeader("Authorization") String token,
                                      @PathVariable Integer postId) throws UniversalBadReqException {
        Integer userId= JwtUtils.getUserIdJWT(token);

        var postO=postRepository.findById(postId);
        if(postO.isEmpty())
            throw new UniversalBadReqException("No such post");
        PostModel postModel=postO.get();

        if(userPostLikeRepository.existsByUserIdAndPostId(userId,postId))
            throw new UniversalBadReqException("Already liked");

        UserPostLikeModel userPostLikeModel=new UserPostLikeModel();
        userPostLikeModel.setUserId(userId);
        userPostLikeModel.setPostId(postId);
        userPostLikeModel.setLikeTime(LocalDateTime.now());

        userPostLikeRepository.save(userPostLikeModel);

        ForumMessageModel.addForumMessage(forumMessageRepository,
                userId,postModel.getUserId(),1,"Liked your post:"+postId,false);

        UniversalResponse response=new UniversalResponse();
        response.message="Post liked";
        return response;
    }

    @DeleteMapping("/post/like/{postId}")
    public UniversalResponse unlikePost(@RequestHeader("Authorization") String token,
                                      @PathVariable Integer postId) throws UniversalBadReqException {
        Integer userId= JwtUtils.getUserIdJWT(token);

        var likeO=userPostLikeRepository.findUserPostLikeModelByPostIdAndUserId(postId,userId);
        if(likeO.isEmpty())
            throw new UniversalBadReqException("Not liked yet");

        userPostLikeRepository.delete(likeO.get());

        UniversalResponse response=new UniversalResponse();
        response.message="Post unliked";
        return response;
    }

}
