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

}
