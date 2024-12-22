package ikklos.ofindexbackend.controller;

import ikklos.ofindexbackend.domain.ShelfBookModel;
import ikklos.ofindexbackend.domain.ShelfModel;
import ikklos.ofindexbackend.repository.BookRepository;
import ikklos.ofindexbackend.repository.ShelfBookRepository;
import ikklos.ofindexbackend.repository.ShelfRepository;
import ikklos.ofindexbackend.utils.UniversalResponse;
import ikklos.ofindexbackend.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping(value = "/shelf",produces = "application/json")
public class ShelfController {

    public static class ShelfBook{
        public int bookId;
        public LocalDateTime addTime;
        public String name;
    }

    public static class BookShelfResponse extends UniversalResponse {
        public static class ResponseItem{
            public String name;
            public int index;
            public int count;
            public List<ShelfBook> books;
        }
        public int count;
        public List<ResponseItem> items;
    }

    public static class HistoryShelfResponse extends UniversalResponse{
        public int count;
        public List<ShelfBook> items;
    }

    public static class ShelfEditRequest{
        public Integer booklistId;
        public Integer bookId;
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

    @GetMapping
    public BookShelfResponse getAllBookShelf(@RequestHeader("Authorization") String token){
        Integer userId= JwtUtils.getUserIdJWT(token);

        BookShelfResponse response=new BookShelfResponse();
        response.result=true;

        var bookShelf=shelfRepository.findShelfModelsByUserId(userId, Sort.unsorted());
        response.items =new ArrayList<>();

        for(var shelf:bookShelf){

            if(shelf.getIndex()==0)continue;

            BookShelfResponse.ResponseItem respItem=new BookShelfResponse.ResponseItem();

            var shelfBooks=shelfBookRepository.findShelfBookModelsByShelfId(shelf.getShelfId(),Sort.unsorted());

            respItem.index=shelf.getIndex();
            respItem.name=shelf.getName();
            respItem.books=new ArrayList<>();

            for(var book:shelfBooks){
                ShelfBook bookItem=new ShelfBook();

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

    @GetMapping("/history")
    public HistoryShelfResponse getHistoryShelf(@RequestHeader("Authorization") String token){
        Integer userId= JwtUtils.getUserIdJWT(token);

        var historyShelf=shelfRepository.findShelfModelByUserIdAndIndex(userId,0);

        Integer shelfId;

        if(historyShelf.isEmpty()){
            ShelfModel history=new ShelfModel();
            history.setUserId(userId);
            history.setIndex(0);
            history.setName("history");
            shelfRepository.save(history);
            shelfId= history.getShelfId();
        }else{
            shelfId=historyShelf.get(0).getShelfId();
        }

        var shelfBooks=shelfBookRepository.findShelfBookModelsByShelfId(shelfId,Sort.unsorted());

        HistoryShelfResponse response=new HistoryShelfResponse();
        response.result=true;

        response.items=shelfBooks.stream().map(book->{
            ShelfBook sBook=new ShelfBook();
            var bookModel=bookRepository.findById(book.getBookId());
            if(bookModel.isEmpty())return null;
            sBook.addTime=book.getTimeStamp();
            sBook.bookId=book.getBookId();
            sBook.name=bookModel.get().getName();

            return sBook;
        }).filter(Objects::nonNull).toList();
        response.count=response.items.size();

        return response;

    }

    @DeleteMapping("/history")
    @Transactional
    public UniversalResponse clearHistoryShelf(@RequestHeader("Authorization") String token){
        Integer userId= JwtUtils.getUserIdJWT(token);

        UniversalResponse response=new UniversalResponse();

        var historyShelf=shelfRepository.findShelfModelByUserIdAndIndex(userId,0);

        Integer shelfId;

        if(historyShelf.isEmpty()){
            ShelfModel history=new ShelfModel();
            history.setUserId(userId);
            history.setIndex(0);
            history.setName("history");
            shelfRepository.save(history);
            response.result=true;
            response.message="No history shelf!";
            return response;
        }else{
            shelfId=historyShelf.get(0).getShelfId();
            shelfBookRepository.removeShelfBookModelsByShelfId(shelfId);
            response.result=true;
            response.message="history removed!";
            return response;
        }
    }

    private UniversalResponse shelfEditRequestTest(String token,ShelfEditRequest request){
        UniversalResponse response=new UniversalResponse();

        Integer userid=JwtUtils.getUserIdJWT(token);

        var shelf=shelfRepository.findById(request.booklistId);
        if(shelf.isEmpty()){
            response.result=false;
            response.message="No such shelf";
            return response;
        }

        if(!Objects.equals(shelf.get().getUserId(), userid)){
            response.result=false;
            response.message="Not your shelf";
            return response;
        }

        if(!bookRepository.existsById(request.bookId)){
            response.result=false;
            response.message="No such book";
            return response;
        }
        return null;
    }

    @PostMapping("/add")
    public UniversalResponse addBookToShelf(@RequestHeader("Authorization") String token,@RequestBody ShelfEditRequest request){


        UniversalResponse response=shelfEditRequestTest(token,request);
        if(response!=null)return response;
        response=new UniversalResponse();

        ShelfBookModel sBook=new ShelfBookModel();
        sBook.setBookId(request.bookId);
        sBook.setShelfId(request.booklistId);
        sBook.setTimeStamp(LocalDateTime.now());

        shelfBookRepository.save(sBook);

        response.result=true;
        response.message="Book added";
        return response;
    }

    @DeleteMapping("/remove")
    public UniversalResponse removeBookFromShelf(@RequestHeader("Authorization") String token,@RequestBody ShelfEditRequest request){
        UniversalResponse response=shelfEditRequestTest(token, request);
        if(response!=null)return response;
        response=new UniversalResponse();

        if(request.bookId!=null) {

            var sBooks = shelfBookRepository.findShelfBookModelByShelfIdAndBookId(request.booklistId, request.bookId);

            if (sBooks.isEmpty()) {
                response.result = false;
                response.message = "this book is not in this shelf";
                return response;
            }

            shelfBookRepository.deleteAll(sBooks);

            response.result = true;
            response.message = "Book removed";
        }else{
            var shelfOption=shelfRepository.findById(request.booklistId);

            if(shelfOption.isEmpty()){
                response.result = false;
                response.message = "shelf not exists";
                return response;
            }

            ShelfModel shelfModel=shelfOption.get();

            shelfBookRepository.deleteShelfBookModelsByShelfId(shelfModel.getShelfId());
            shelfRepository.delete(shelfModel);

            response.result = true;
            response.message = "Shelf removed";
        }
        return response;
    }

}
