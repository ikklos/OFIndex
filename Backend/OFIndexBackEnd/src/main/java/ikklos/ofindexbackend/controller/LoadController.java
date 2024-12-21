package ikklos.ofindexbackend.controller;

import ikklos.ofindexbackend.filesystem.BookFileFinder;
import ikklos.ofindexbackend.repository.BookRepository;
import ikklos.ofindexbackend.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;

@RestController
@CrossOrigin
@RequestMapping(value = "/load",produces = "application/json")
public class LoadController {

    private final BookRepository bookRepository;
    private final BookFileFinder bookFileFinder;

    public LoadController(@Autowired BookRepository bookRepository,
                          @Autowired BookFileFinder bookFileFinder){
        this.bookFileFinder=bookFileFinder;
        this.bookRepository=bookRepository;
    }

    @PostMapping("/ebook/{bookid}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer bookid,
                                                 @RequestHeader("Authorization") String token) throws IOException {

        JwtUtils.getUserIdJWT(token);
        var booko=bookRepository.findById(bookid);

        if(booko.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Path filePath = bookFileFinder.getBookDocumentPath(booko.get());
        Resource resource = new org.springframework.core.io.UrlResource(filePath.toUri());
        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }

    }
}