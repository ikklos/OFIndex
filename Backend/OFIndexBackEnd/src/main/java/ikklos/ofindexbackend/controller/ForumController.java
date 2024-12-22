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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/forum",produces = "application/json")
public class ForumController {

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
        public String posterAvatar;
        public Integer bookId;
        public Integer packId;
        public List<String> tags;
        public String title;
        public String images;
        public Integer likes;
        public LocalDateTime createTime;
    }

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public ForumController(@Autowired PostRepository postRepository,
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
        postModel.setText(s);

        postRepository.save(postModel);

        response.postId= postModel.getPostId();
        response.message="Post added";
        return response;
    }

    @GetMapping("/post/{postid}")
    public PostGetResponse getPostContent(@PathVariable Integer postid) throws JsonProcessingException, UniversalBadReqException {

        var postOption=postRepository.findById(postid);

        PostGetResponse response=new PostGetResponse();

        if(postOption.isEmpty()){
            throw new UniversalBadReqException("No such post");
        }

        PostModel postModel=postOption.get();

        response.postId=postModel.getPostId();
        response.bookId=postModel.getBookId();
        response.packId=postModel.getPackId();
        response.posterId=postModel.getUserId();
        response.title=postModel.getTitle();
        response.likes=postModel.getLikes();
        response.createTime =postModel.getTimeStamp();

        var userOption=userRepository.findById(postModel.getUserId());
        if(userOption.isPresent()){
            UserModel userModel=userOption.get();
            response.posterAvatar=userModel.getAvatar();
        }

        ObjectMapper objectMapper=new ObjectMapper();
        JavaType javaType=objectMapper.getTypeFactory().constructParametricType(List.class,String.class);
        response.tags= objectMapper.readValue(postModel.getTags(),javaType);
        response.images =objectMapper.readValue(postModel.getImageurls(),javaType);

        response.message="Post found";
        return response;
    }

}
