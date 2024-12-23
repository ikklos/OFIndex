package ikklos.ofindexbackend.controller;

import ikklos.ofindexbackend.domain.BookModel;
import ikklos.ofindexbackend.domain.PackModel;
import ikklos.ofindexbackend.domain.UserModel;
import ikklos.ofindexbackend.repository.BookRepository;
import ikklos.ofindexbackend.repository.PackRepository;
import ikklos.ofindexbackend.repository.UserRepository;
import ikklos.ofindexbackend.utils.JwtUtils;
import ikklos.ofindexbackend.utils.PackContentResponse;
import ikklos.ofindexbackend.utils.UniversalBadReqException;
import ikklos.ofindexbackend.utils.UniversalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping(value="/pack",produces = "application/json")
public class PackController {

    public static class UserPackListResponse extends UniversalResponse{
        public static class UserPackListItem{
            public Integer packId;
            public String packName;
            public Integer packLikes;
            public Boolean shared;
        }
        public Integer count;
        public List<UserPackListItem> packs;
    }

    private final PackRepository packRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Autowired
    public PackController(PackRepository packRepository,
                          UserRepository userRepository,
                          BookRepository bookRepository){
        this.packRepository=packRepository;
        this.userRepository=userRepository;
        this.bookRepository=bookRepository;
    }

    @GetMapping("/{packid}")
    public PackContentResponse getPackById(@PathVariable("packid") Integer packId,
                                           @RequestHeader("Authorization") String token) throws UniversalBadReqException {
        Integer userid= JwtUtils.getUserIdJWT(token);

        var packO=packRepository.findById(packId);
        if(packO.isEmpty()){
            throw new UniversalBadReqException("No such resource pack");
        }


        PackModel packModel=packO.get();

        if(packModel.getShared()==0&& !Objects.equals(userid, packModel.getAuthorId())){
            throw new UniversalBadReqException("Unshared pack and you are not its author");
        }

        var bookO=bookRepository.findById(packModel.getBookId());
        if(bookO.isEmpty()){
            throw new UniversalBadReqException("This resource pack has invalid book ID");
        }
        BookModel bookModel=bookO.get();

        var userO=userRepository.findById(packModel.getAuthorId());
        if(userO.isPresent()) {
            UserModel userModel = userO.get();
            return new PackContentResponse(packModel,bookModel,userModel);
        }else{
            return new PackContentResponse(packModel,bookModel,null);
        }

    }

    @GetMapping("/user/{userid}")
    public UserPackListResponse getUserPackList(@PathVariable("userid") Integer userId) throws UniversalBadReqException {

        if(!userRepository.existsById(userId)){
            throw new UniversalBadReqException("No such user");
        }

        UserPackListResponse response=new UserPackListResponse();
        response.packs=packRepository.findAllByAuthorId(userId, Sort.unsorted()).stream().map(packModel -> {
            UserPackListResponse.UserPackListItem item=new UserPackListResponse.UserPackListItem();
            item.packName=packModel.getName();
            item.packLikes=packModel.getLikeCount();
            item.packId=packModel.getPackId();
            item.shared=packModel.getShared()!=0;
            return item;
        }).toList();
        response.count=response.packs.size();

        return response;
    }
}
