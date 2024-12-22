package ikklos.ofindexbackend.controller;

import ikklos.ofindexbackend.domain.BookModel;
import ikklos.ofindexbackend.repository.BookRepository;
import ikklos.ofindexbackend.repository.PackRepository;
import ikklos.ofindexbackend.repository.UserRepository;
import ikklos.ofindexbackend.utils.UniversalBadReqException;
import ikklos.ofindexbackend.utils.UniversalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
            public String tag;
        }
        public Integer count;
        public Integer totalResult;
        public List<RespItem> items;
    }

    private final PackRepository packRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public SearchController(@Autowired PackRepository packRepository,@Autowired UserRepository userRepository,@Autowired BookRepository bookRepository){
        this.packRepository=packRepository;
        this.userRepository=userRepository;
        this.bookRepository=bookRepository;
    }

    @PostMapping
    public SearchBookResponse searchBook(@RequestBody SearchBookRequest request){
        SearchBookResponse response=new SearchBookResponse();

        response.totalResult= Math.toIntExact(bookRepository.count());

        Page<BookModel> books;

        if(request.bookClass==null){
            books=bookRepository.findBookModelsByNameLike("%"+request.text+"%", PageRequest.of(request.page,request.count));
        }else{
            books=bookRepository.findBookModelsByBookClassAndNameLike(request.bookClass, "%"+request.text+"%", PageRequest.of(request.page,request.count));
        }

        response.items=books.stream().map(
                bookModel -> {
                    SearchBookResponse.RespItem item=new SearchBookResponse.RespItem();
                    item.id=bookModel.getBookId();
                    item.author=bookModel.getAuthor();
                    item.cover=bookModel.getCover();
                    item.tag=bookModel.getTags();
                    item.name=bookModel.getName();
                    item.description=bookModel.getDescription();
                    return item;
                }
        ).toList();

        response.message="Result found";
        response.count=response.items.size();

        return response;

    }


    @GetMapping("/pack/{bookId}")
    public SearchPackResponse searchPackByBook(@PathVariable("bookId") Integer bookId) throws UniversalBadReqException {

        SearchPackResponse response=new SearchPackResponse();

        if(!bookRepository.existsById(bookId)){
            throw new UniversalBadReqException("Invalid Book ID");
        }

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
