package ikklos.ofindexbackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ikklos.ofindexbackend.domain.PostModel;
import ikklos.ofindexbackend.repository.PostRepository;
import ikklos.ofindexbackend.utils.JwtUtils;
import ikklos.ofindexbackend.utils.UniversalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
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
        public String picture;
        public List<String> tags;
    }

    private final PostRepository postRepository;

    public ForumController(@Autowired PostRepository postRepository){
        this.postRepository=postRepository;
    }

    @PostMapping("/post/add")
    public PostAddResponse addPost(@RequestBody PostAddRequest request,
                                     @RequestHeader("Authorization") String token){
        Integer userId= JwtUtils.getUserIdJWT(token);
        PostAddResponse response=new PostAddResponse();

        PostModel postModel=new PostModel();
        postModel.setUserId(userId);
        postModel.setTitle(request.title);
        postModel.setText(request.text);
        postModel.setImageurls(request.picture);
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
        ObjectMapper mapper = new ObjectMapper();
        try {
            String s = mapper.writeValueAsString(commonTags);
            postModel.setText(s);
        } catch (JsonProcessingException e) {
            response.result=false;
            response.message="Failed to process tags";
            return response;
        }

        postRepository.save(postModel);

        response.postId= postModel.getPostId();
        response.result=true;
        response.message="Post added";
        return response;
    }

}
