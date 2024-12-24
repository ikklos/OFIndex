package ikklos.ofindexbackend.controller;

import ikklos.ofindexbackend.repository.BookRepository;
import ikklos.ofindexbackend.utils.UniversalBadReqException;
import ikklos.ofindexbackend.utils.UniversalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/book",produces = "application/json")
public class BookController {


    public static class BookInfoResponse extends UniversalResponse {
        public Integer bookId;
        public String name;
        public String author;
        public String description;
        public String cover;
        public String tag;
        public String isbn;
        public Integer bookClass;
    }

    private final BookRepository repository;

    public BookController(@Autowired BookRepository repository){
        this.repository=repository;
    }

    @GetMapping("/{bookId}")
    public BookInfoResponse getBookInfo(@PathVariable("bookId") Integer bookId) throws UniversalBadReqException {

        if(bookId==null){
            throw new UniversalBadReqException(502,"No such book");
        }

        var book=repository.findById(bookId);

        if(book.isEmpty()){
            throw new UniversalBadReqException(502,"Invalid book ID");
        }

        BookInfoResponse response=new BookInfoResponse();
        response.message="Book found";

        response.bookId=book.get().getBookId();
        response.name=book.get().getName();
        response.author=book.get().getAuthor();
        response.description=book.get().getDescription();
        response.isbn=book.get().getIsbn();
        response.tag=book.get().getTags();
        response.cover=book.get().getCover();
        response.bookClass=book.get().getBookClass();

        return response;
    }

}
