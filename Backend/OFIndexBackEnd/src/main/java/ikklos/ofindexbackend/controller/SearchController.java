package ikklos.ofindexbackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import ikklos.ofindexbackend.domain.BookModel;
import ikklos.ofindexbackend.domain.PackModel;
import ikklos.ofindexbackend.repository.BookRepository;
import ikklos.ofindexbackend.repository.PackRepository;
import ikklos.ofindexbackend.repository.UserPackLikeRepository;
import ikklos.ofindexbackend.repository.UserRepository;
import ikklos.ofindexbackend.utils.JwtUtils;
import ikklos.ofindexbackend.utils.UniversalBadReqException;
import ikklos.ofindexbackend.utils.UniversalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@CrossOrigin
@RequestMapping(value="/search",produces = "application/json")
public class SearchController {

    private final UserPackLikeRepository userPackLikeRepository;

    public static class SearchPackResponseItem{
        public Integer packId;
        public String name;
        public Integer authorId;
        public String authorAvatar;
        public String description;
        public Integer likeCount;
        public Boolean liked;

        public SearchPackResponseItem(PackModel pack){
            packId=pack.getPackId();
            authorId=pack.getAuthorId();
            name=pack.getName();
            description =pack.getDescription();
        }
    }

    public static class SearchPackResponse extends UniversalResponse {
        public Integer count;
        public Integer total;
        public List<SearchPackResponseItem> items;
    }

    public static class SearchBookRequest{
        public String text;
        public Integer bookClass;
        public Integer count;
        public Integer page;
    }

    public static class SearchBookResponse extends UniversalResponse{
        public static class RespItem{
            public Integer id;
            public String name;
            public String author;
            public String description;
            public String cover;
            public List<String> tags;
        }
        public Integer count;
        public Integer totalResult;
        public List<RespItem> items;
    }

    private final PackRepository packRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Autowired
    public SearchController(PackRepository packRepository,
                            UserRepository userRepository,
                            BookRepository bookRepository, UserPackLikeRepository userPackLikeRepository){
        this.packRepository=packRepository;
        this.userRepository=userRepository;
        this.bookRepository=bookRepository;
        this.userPackLikeRepository = userPackLikeRepository;
    }

    @PostMapping
    public SearchBookResponse searchBook(@RequestBody SearchBookRequest request) throws UniversalBadReqException {
        SearchBookResponse response=new SearchBookResponse();

        response.totalResult= Math.toIntExact(bookRepository.count());

        List<BookModel> books;

        String searchText="%"+request.text+"%";
        if(request.bookClass==null){
            books=bookRepository.findBookModelsByNameLike(searchText);
            books.addAll(bookRepository.findBookModelsByTagsLike(searchText));
            books.addAll(bookRepository.findBookModelsByDescriptionLike(searchText));
        }else{
            books=bookRepository.findBookModelsByBookClassAndNameLike(request.bookClass, searchText);
            books.addAll(bookRepository.findBookModelsByBookClassAndTagsLike(request.bookClass, searchText));
            books.addAll(bookRepository.findBookModelsByBookClassAndDescriptionLike(request.bookClass, searchText));
        }
        books=books.stream().distinct().toList();

        int from=request.page*request.count;
        int to=from+request.count;

        if(from<books.size()) {

            if (to >= books.size()) to = books.size();

            response.items = books.subList(from, to).stream().map(
                    bookModel -> {
                        SearchBookResponse.RespItem item = new SearchBookResponse.RespItem();
                        item.id = bookModel.getBookId();
                        item.author = bookModel.getAuthor();
                        item.cover = bookModel.getCover();
                        ObjectMapper objectMapper = new ObjectMapper();
                        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, String.class);
                        try {
                            item.tags = objectMapper.readValue(bookModel.getTags(), javaType);
                        } catch (JsonProcessingException e) {
                            Logger.getGlobal().log(Level.WARNING, "Book contains illegal tags!bookId:" + bookModel.getBookId());
                            return null;
                        }
                        item.name = bookModel.getName();
                        item.description = bookModel.getDescription();
                        return item;
                    }
            ).toList();
        }else
            response.items = new ArrayList<>();

        response.message="Result found";
        response.count=response.items.size();

        return response;

    }


    @GetMapping("/pack/{bookId}")
    public SearchPackResponse searchPackByBook(@PathVariable("bookId") Integer bookId,
                                               @RequestHeader(value = "Authorization",required = false) String token) throws UniversalBadReqException {
        Integer userId =token!=null? JwtUtils.getUserIdJWT(token):null;
        SearchPackResponse response=new SearchPackResponse();

        if(!bookRepository.existsById(bookId)){
            throw new UniversalBadReqException("Invalid Book ID");
        }

        response.message="Found Book";

        var packs=packRepository.findAllByBookId(bookId);
        response.total=packs.size();
        response.items=packs.stream().map(pack->{

            var author=userRepository.findById(pack.getAuthorId());

            if(author.isEmpty())return null;
            if(pack.getShared()==0)return null;

            SearchPackResponseItem ret=new SearchPackResponseItem(pack);
            ret.authorAvatar=author.get().getAvatar();
            ret.liked=userId!=null&&userPackLikeRepository.existsUserPackLikeModelByUserIdAndPackId(userId,pack.getPackId());
            ret.likeCount=userPackLikeRepository.countAllByPackId(pack.getPackId());
            return ret;
        }).filter(Objects::nonNull).toList();
        response.count=response.items.size();

        return response;
    }


}
