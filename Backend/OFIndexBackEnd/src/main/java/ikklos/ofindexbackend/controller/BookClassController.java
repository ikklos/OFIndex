package ikklos.ofindexbackend.controller;

import ikklos.ofindexbackend.repository.BookClassRepository;
import ikklos.ofindexbackend.utils.UniversalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value="/class",produces = "application/json")
public class BookClassController {

    public static class BookClassResponse extends UniversalResponse{
        public static class RespItem{
            public Integer id;
            public String name;
        }
        public Integer count;
        public List<RespItem> items;

    }

    private final BookClassRepository bookClassRepository;

    @Autowired
    public BookClassController(BookClassRepository repository){
        bookClassRepository=repository;
    }

    @GetMapping
    public BookClassResponse getBookClass(){
        BookClassResponse response=new BookClassResponse();
        response.items=bookClassRepository.findAll().stream().map(
                bookClassModel -> {
                    BookClassResponse.RespItem item=new BookClassResponse.RespItem();
                    item.id=bookClassModel.getClassId();
                    item.name=bookClassModel.getName();
                    return item;
                }
        ).toList();
        response.count=response.items.size();
        response.message="BookClass Found";
        return response;
    }

}
