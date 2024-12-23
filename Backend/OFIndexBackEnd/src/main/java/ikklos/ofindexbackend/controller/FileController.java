package ikklos.ofindexbackend.controller;

import ikklos.ofindexbackend.repository.FileRepository;
import ikklos.ofindexbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping(value = "/file",produces = "application/json")
public class FileController {

    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    @Autowired
    public FileController(UserRepository userRepository,
                          FileRepository fileRepository){
        this.userRepository=userRepository;
        this.fileRepository=fileRepository;
    }



}
