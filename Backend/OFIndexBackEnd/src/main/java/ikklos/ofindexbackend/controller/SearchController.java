package ikklos.ofindexbackend.controller;

import ikklos.ofindexbackend.repository.BookRepository;
import ikklos.ofindexbackend.repository.PackRepository;
import ikklos.ofindexbackend.repository.UserRepository;
import ikklos.ofindexbackend.response.UniversalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping(value="/search",produces = "application/json")
public class SearchController {

    public static class SearchPackResponseItem{
        public Integer packId;
        public String name;
        public Integer authorId;
        public String authorAvatar;
    }

    public static class SearchPackResponse extends UniversalResponse {
        public Integer count;
        public List<SearchPackResponseItem> items;
    }

    private final PackRepository packRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public SearchController(@Autowired PackRepository packRepository,@Autowired UserRepository userRepository,@Autowired BookRepository bookRepository){
        this.packRepository=packRepository;
        this.userRepository=userRepository;
        this.bookRepository=bookRepository;
    }

    @PostMapping("/pack/{bookId}")
    public SearchPackResponse searchPackByBook(@PathVariable("bookId") Integer bookId){

        SearchPackResponse response=new SearchPackResponse();

        if(!bookRepository.existsById(bookId)){
            response.result=false;
            response.message="Invalid Book ID";
            return response;
        }

        response.result=true;
        response.message="Found Book";

        var packs=packRepository.findAllByBookId(bookId);
        response.count=packs.size();
        response.items=packs.stream().map(pack->{

            var author=userRepository.findById(pack.getAuthorId());

            if(author.isEmpty())return null;

            SearchPackResponseItem ret=new SearchPackResponseItem();
            ret.packId=pack.getPackId();
            ret.authorId=pack.getAuthorId();
            ret.name=pack.getName();
            ret.authorAvatar=author.get().getAvatar();
            return ret;
        }).filter(Objects::nonNull).toList();

        return response;
    }

}
