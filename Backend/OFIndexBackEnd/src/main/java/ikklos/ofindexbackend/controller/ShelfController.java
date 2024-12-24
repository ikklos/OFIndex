package ikklos.ofindexbackend.controller;

import ikklos.ofindexbackend.domain.ShelfBookModel;
import ikklos.ofindexbackend.domain.ShelfModel;
import ikklos.ofindexbackend.repository.BookRepository;
import ikklos.ofindexbackend.repository.ShelfBookRepository;
import ikklos.ofindexbackend.repository.ShelfRepository;
import ikklos.ofindexbackend.utils.JwtUtils;
import ikklos.ofindexbackend.utils.UniversalBadReqException;
import ikklos.ofindexbackend.utils.UniversalResponse;
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
        public String cover;
        public LocalDateTime addTime;
        public String name;
    }

    public static class BookShelfResponse extends UniversalResponse {
        public static class ResponseItem{
            public String name;
            public int index;
            public int shelfId;
            public int count;
            public List<ShelfBook> books;
        }
        public int count;
        public List<ResponseItem> items;
    }

    public static class SimpleBookShelf extends UniversalResponse{
        public static class ResponseItem{
            public String name;
            public Integer booklistId;
        }
        public Integer count;
        public List<ResponseItem> booklists;
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

        var bookShelf=shelfRepository.findShelfModelsByUserId(userId, Sort.unsorted());
        response.items =new ArrayList<>();

        for(var shelf:bookShelf){

            if(shelf.getIndex()==0)continue;

            BookShelfResponse.ResponseItem respItem=new BookShelfResponse.ResponseItem();

            var shelfBooks=shelfBookRepository.findShelfBookModelsByShelfId(shelf.getShelfId(),Sort.unsorted());

            respItem.shelfId= shelf.getShelfId();
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
                bookItem.cover=bookModel.get().getCover();

                respItem.books.add(bookItem);
            }
            respItem.count=respItem.books.size();
            response.items.add(respItem);
        }
        response.count=response.items.size();

        return response;
    }

    @GetMapping("/simple")
    public SimpleBookShelf getAllBookShelfSimple(@RequestHeader("Authorization") String token){
        Integer userId= JwtUtils.getUserIdJWT(token);

        SimpleBookShelf response=new SimpleBookShelf();

        var bookShelf=shelfRepository.findShelfModelsByUserId(userId, Sort.unsorted());
        response.booklists =bookShelf.stream().map(shelfModel -> {
            SimpleBookShelf.ResponseItem item=new SimpleBookShelf.ResponseItem();
            item.booklistId=shelfModel.getShelfId();
            item.name=shelfModel.getName();
            return item;
        }).toList();
        response.count=response.booklists.size();

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

        response.items=shelfBooks.stream().map(book->{
            ShelfBook sBook=new ShelfBook();
            var bookModel=bookRepository.findById(book.getBookId());
            if(bookModel.isEmpty())return null;
            sBook.addTime=book.getTimeStamp();
            sBook.bookId=book.getBookId();
            sBook.name=bookModel.get().getName();
            sBook.cover=bookModel.get().getCover();

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
            response.message="No history shelf!";
            return response;
        }else{
            shelfId=historyShelf.get(0).getShelfId();
            shelfBookRepository.removeShelfBookModelsByShelfId(shelfId);
            response.message="history removed!";
            return response;
        }
    }

    private UniversalResponse shelfEditRequestTest(String token,ShelfEditRequest request) throws UniversalBadReqException {
        Integer userid=JwtUtils.getUserIdJWT(token);

        var shelf=shelfRepository.findById(request.booklistId);
        if(shelf.isEmpty()){
            throw new UniversalBadReqException("No such shelf");
        }

        if(!Objects.equals(shelf.get().getUserId(), userid)){
            throw new UniversalBadReqException("Not your shelf");
        }

        if(!bookRepository.existsById(request.bookId)){
            throw new UniversalBadReqException("No such book");
        }
        return null;
    }

    @PostMapping("/add")
    public UniversalResponse addBookToShelf(@RequestHeader("Authorization") String token,@RequestBody ShelfEditRequest request) throws UniversalBadReqException {


        UniversalResponse response=shelfEditRequestTest(token,request);
        if(response!=null)return response;
        response=new UniversalResponse();

        ShelfBookModel sBook=new ShelfBookModel();
        sBook.setBookId(request.bookId);
        sBook.setShelfId(request.booklistId);
        sBook.setTimeStamp(LocalDateTime.now());

        shelfBookRepository.save(sBook);

        response.message="Book added";
        return response;
    }

    @DeleteMapping("/remove")
    public UniversalResponse removeBookFromShelf(@RequestHeader("Authorization") String token,@RequestBody ShelfEditRequest request) throws UniversalBadReqException {
        UniversalResponse response=shelfEditRequestTest(token, request);
        if(response!=null)return response;
        response=new UniversalResponse();

        if(request.bookId!=null) {

            var sBooks = shelfBookRepository.findShelfBookModelByShelfIdAndBookId(request.booklistId, request.bookId);

            if (sBooks.isEmpty()) {
                throw new UniversalBadReqException("this book is not in this shelf");
            }

            shelfBookRepository.deleteAll(sBooks);

            response.message = "Book removed";
        }else{
            var shelfOption=shelfRepository.findById(request.booklistId);

            if(shelfOption.isEmpty()){
                throw new UniversalBadReqException("shelf not exists");
            }

            ShelfModel shelfModel=shelfOption.get();

            shelfBookRepository.deleteShelfBookModelsByShelfId(shelfModel.getShelfId());
            shelfRepository.delete(shelfModel);

            response.message = "Shelf removed";
        }
        return response;
    }

}
