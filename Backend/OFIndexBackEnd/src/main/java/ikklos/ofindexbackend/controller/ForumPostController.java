package ikklos.ofindexbackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import ikklos.ofindexbackend.domain.PostModel;
import ikklos.ofindexbackend.domain.UserModel;
import ikklos.ofindexbackend.repository.PostRepository;
import ikklos.ofindexbackend.repository.UserRepository;
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
        public String images;
        public Integer likes;
        public LocalDateTime createTime;

        public PostGetResponse setByModel(UserModel userModel){
            posterAvatar= userModel.getAvatar();
            posterName= userModel.getUsername();
            return this;
        }

        public PostGetResponse setByModel(PostModel postModel) throws JsonProcessingException {
            postId=postModel.getPostId();
            bookId=postModel.getBookId();
            packId=postModel.getPackId();
            posterId=postModel.getUserId();
            title=postModel.getTitle();
            likes=postModel.getLikes();
            createTime =postModel.getTimeStamp();
            ObjectMapper objectMapper=new ObjectMapper();
            JavaType javaType=objectMapper.getTypeFactory().constructParametricType(List.class,String.class);
            tags= objectMapper.readValue(postModel.getTags(),javaType);
            images =objectMapper.readValue(postModel.getImageurls(),javaType);
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

    public ForumPostController(@Autowired PostRepository postRepository,
                               @Autowired UserRepository userRepository){

        this.postRepository=postRepository;
        this.userRepository=userRepository;
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
        postModel.setImageurls(s0);
        postModel.setLikes(0);
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
        postModel.setTags(s);

        postRepository.save(postModel);

        response.postId= postModel.getPostId();
        response.message="Post added";
        return response;
    }

    @GetMapping("/post/{postid}")
    public PostGetResponse getPostContent(@PathVariable("postid") Integer postId) throws JsonProcessingException, UniversalBadReqException {

        var postOption=postRepository.findById(postId);

        if(postOption.isEmpty()){
            throw new UniversalBadReqException("No such post");
        }

        PostModel postModel=postOption.get();

        PostGetResponse response=new PostGetResponse().setByModel(postModel);
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

        if(from>=posts.size()){
            throw new UniversalBadReqException("No enough post");
        }

        if(to>=posts.size())to=posts.size();

        response.posts=posts.subList(from,to).stream().map(postModel -> {
            try {
                PostGetResponse item=new PostGetResponse().setByModel(postModel);
                var userO=userRepository.findById(postModel.getUserId());
                userO.ifPresent(item::setByModel);
                return item;
            } catch (JsonProcessingException e) {
                return null;
            }
        }).filter(Objects::nonNull).toList();
        response.count=response.posts.size();
        response.total=posts.size();
        return response;
    }

}
