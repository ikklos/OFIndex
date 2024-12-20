package ikklos.ofindexbackend.controller;

import ikklos.ofindexbackend.repository.BookRepository;
import ikklos.ofindexbackend.repository.ShelfBookRepository;
import ikklos.ofindexbackend.repository.ShelfRepository;
import ikklos.ofindexbackend.request.TokenRequest;
import ikklos.ofindexbackend.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/shelf",produces = "application/json")
public class ShelfController {


    public static class BookShelfResponse {
        public static class ResponseItem{
            public static class ShelfBook{
                public int bookId;
                public LocalDateTime addTime;
                public String name;
            }
            public String name;
            public int index;
            public int count;
            public List<ShelfBook> books;
        }
        public int count;
        public List<ResponseItem> items;
    }

    private final ShelfRepository shelfRepository;
    private final ShelfBookRepository shelfBookRepository;
    private final BookRepository bookRepository;

    public ShelfController(
            @Autowired ShelfRepository shelfRepository,
            @Autowired ShelfBookRepository shelfBookRepository,
            @Autowired BookRepository bookRepository){
        this.shelfRepository=shelfRepository;
        this.shelfBookRepository=shelfBookRepository;
        this.bookRepository = bookRepository;
    }

    @PostMapping
    public BookShelfResponse getAllBookShelf(@RequestBody TokenRequest request){
        Integer userId= JwtUtils.getUserIdJWT(request.token);

        BookShelfResponse response=new BookShelfResponse();

        var bookShelf=shelfRepository.findShelfModelsByUserId(userId, Sort.unsorted());
        response.items=new ArrayList<>();

        for(var shelf:bookShelf){

            if(shelf.getIndex()==0)continue;

            BookShelfResponse.ResponseItem respItem=new BookShelfResponse.ResponseItem();

            var shelfBooks=shelfBookRepository.findShelfBookModelsByShelfId(shelf.getShelfId(),Sort.unsorted());

            respItem.index=shelf.getIndex();
            respItem.name=shelf.getName();
            respItem.books=new ArrayList<>();

            for(var book:shelfBooks){
                BookShelfResponse.ResponseItem.ShelfBook bookItem=new BookShelfResponse.ResponseItem.ShelfBook();

                var bookModel=bookRepository.findById(book.getId());

                if(bookModel.isEmpty()) continue;

                bookItem.bookId=book.getBookId();
                bookItem.name=bookModel.get().getName();
                bookItem.addTime=book.getTimeStamp();

                respItem.books.add(bookItem);
            }
            respItem.count=respItem.books.size();
            response.items.add(respItem);
        }
        response.count=response.items.size();

        return response;
    }


}
