package ikklos.ofindexbackend.controller;

import ikklos.ofindexbackend.domain.BookModel;
import ikklos.ofindexbackend.filesystem.BookFileFinder;
import ikklos.ofindexbackend.repository.BookRepository;
import ikklos.ofindexbackend.utils.JwtUtils;
import ikklos.ofindexbackend.utils.UniversalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping(value = "/upload",produces = "application/json")
public class UploadController {

    private final BookRepository bookRepository;
    private final BookFileFinder bookFileFinder;

    public UploadController(@Autowired BookRepository bookRepository,
                            @Autowired BookFileFinder bookFileFinder){
        this.bookRepository=bookRepository;
        this.bookFileFinder=bookFileFinder;
    }

    @PostMapping("/book")
    public UniversalResponse uploadBook(@RequestParam("file") MultipartFile file,
                                        @RequestParam("id") Integer bookId,
                                        @RequestHeader("Authorization") String token) throws IOException {
        Integer userId= JwtUtils.getUserIdJWT(token);
        UniversalResponse response=new UniversalResponse();

        if(userId!=0){
            response.result=false;
            response.message="Not Administrator";
            return response;
        }

        var bookO=bookRepository.findById(bookId);
        if(bookO.isEmpty()){
            response.result=false;
            response.message="No such book,create it first";
            return response;
        }

        BookModel bookModel=bookO.get();
        if(bookFileFinder.bookDocumentExist(bookModel)){
            response.result=false;
            response.message="book file already exists";
            return response;
        }

        bookFileFinder.uploadDocument(file,bookModel);

        response.result=true;
        response.message="Uploaded";
        return response;
    }

}
