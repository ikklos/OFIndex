package ikklos.ofindexbackend.controller;

import ikklos.ofindexbackend.domain.BookModel;
import ikklos.ofindexbackend.repository.BookClassRepository;
import ikklos.ofindexbackend.repository.BookRepository;
import ikklos.ofindexbackend.utils.JwtUtils;
import ikklos.ofindexbackend.utils.UniversalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/create",produces = "application/json")
public class CreateController {

    public static class CreateBookRequest{
        public String name;
        public String author;
        public String description;
        public String cover;
        public String tags;
        public String isbn;
        public Integer bookClass;
    }

    public static class CreateBookResponse extends UniversalResponse{
        public Integer id;
    }

    private final BookRepository bookRepository;
    private final BookClassRepository bookClassRepository;

    public CreateController(@Autowired BookRepository bookRepository,
                            @Autowired BookClassRepository bookClassRepository){
        this.bookRepository=bookRepository;
        this.bookClassRepository=bookClassRepository;
    }

    @PostMapping("/book")
    public CreateBookResponse createBook(@RequestBody CreateBookRequest request,@RequestHeader("Authorization") String token){
        Integer userId= JwtUtils.getUserIdJWT(token);
        CreateBookResponse response=new CreateBookResponse();

        if(userId!=0){
            response.result=false;
            response.message="Not Administrator";
            return response;
        }

        if(!bookClassRepository.existsById(request.bookClass)){
            response.result=false;
            response.message="No such book class";
            return response;
        }

        BookModel bookModel=new BookModel();
        bookModel.setName(request.name);
        bookModel.setAuthor(request.author);
        bookModel.setDescription(request.description);
        bookModel.setCover(request.cover);
        bookModel.setTags(request.tags);
        bookModel.setIsbn(request.isbn);
        bookModel.setBookClass(request.bookClass);

        bookRepository.save(bookModel);

        response.id=bookModel.getBookId();
        response.result=true;
        response.message="Book created";

        return response;
    }

}
