package ikklos.ofindexbackend.controller;

import ikklos.ofindexbackend.domain.BookModel;
import ikklos.ofindexbackend.filesystem.BookFileFinder;
import ikklos.ofindexbackend.repository.BookClassRepository;
import ikklos.ofindexbackend.repository.BookRepository;
import ikklos.ofindexbackend.utils.JwtUtils;
import ikklos.ofindexbackend.utils.UniversalBadReqException;
import ikklos.ofindexbackend.utils.UniversalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    private final BookFileFinder bookFileFinder;

    public CreateController(@Autowired BookRepository bookRepository,
                            @Autowired BookClassRepository bookClassRepository,
                            @Autowired BookFileFinder bookFileFinder){
        this.bookRepository=bookRepository;
        this.bookClassRepository=bookClassRepository;
        this.bookFileFinder=bookFileFinder;
    }

    @PostMapping(value="/book",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CreateBookResponse createBook(@RequestPart("formData") CreateBookRequest request,
                                         @RequestPart("file") MultipartFile file,
                                         @RequestHeader("Authorization") String token) throws IOException, UniversalBadReqException {
        Integer userId= JwtUtils.getUserIdJWT(token);
        CreateBookResponse response=new CreateBookResponse();

        if(userId!=0){
            throw new UniversalBadReqException("Not Administrator");
        }

        if(!bookClassRepository.existsById(request.bookClass)){
            throw new UniversalBadReqException("No such book class");
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

        bookFileFinder.uploadDocument(file,bookModel);

        response.id=bookModel.getBookId();
        response.message="Book created";

        return response;
    }

}
