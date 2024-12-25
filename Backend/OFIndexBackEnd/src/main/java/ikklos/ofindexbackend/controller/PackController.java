package ikklos.ofindexbackend.controller;

import ikklos.ofindexbackend.domain.*;
import ikklos.ofindexbackend.repository.*;
import ikklos.ofindexbackend.utils.*;
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

    private final ForumMessageRepository forumMessageRepository;

    public static class UserPackListResponse extends UniversalResponse{
        public static class UserPackListItem{
            public Integer packId;
            public Integer bookId;
            public String packName;
            public Integer packLikes;
            public Boolean liked;
            public Boolean shared;

            public UserPackListItem(PackModel packModel){
                packName=packModel.getName();
                packId=packModel.getPackId();
                bookId=packModel.getBookId();
                shared=packModel.getShared()!=0;
            }
        }
        public Integer count;
        public List<UserPackListItem> packs;
    }

    public static class CopyPackResponse extends UniversalResponse{
        public Integer packId;
    }

    private final PackRepository packRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final UserPackLikeRepository userPackLikeRepository;

    @Autowired
    public PackController(PackRepository packRepository,
                          UserRepository userRepository,
                          BookRepository bookRepository,
                          UserPackLikeRepository userPackLikeRepository,
                          ForumMessageRepository forumMessageRepository){
        this.packRepository=packRepository;
        this.userRepository=userRepository;
        this.bookRepository=bookRepository;
        this.userPackLikeRepository=userPackLikeRepository;
        this.forumMessageRepository = forumMessageRepository;
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

        if(packModel.getShared()==0&& !Objects.equals(userid, packModel.getOwnerId())){
            throw new UniversalBadReqException("Unshared pack and you are not its owner");
        }

        var bookO=bookRepository.findById(packModel.getBookId());
        if(bookO.isEmpty()){
            throw new UniversalBadReqException("This resource pack has invalid book ID");
        }
        BookModel bookModel=bookO.get();

        var userO=userRepository.findById(packModel.getAuthorId());
        if(userO.isPresent()) {
            UserModel userModel = userO.get();
            return new PackContentResponse(packModel,bookModel,userModel,userPackLikeRepository);
        }else{
            return new PackContentResponse(packModel,bookModel,null,userPackLikeRepository);
        }

    }

    @DeleteMapping("/{packid}")
    public UniversalResponse deletePack(@PathVariable("packid") Integer packId,
                                        @RequestHeader("Authorization") String token) throws UniversalBadReqException {
        Integer userid= JwtUtils.getUserIdJWT(token);

        var packO=packRepository.findById(packId);
        if(packO.isEmpty()){
            throw new UniversalBadReqException("No such resource pack");
        }

        if(!Objects.equals(packO.get().getOwnerId(), userid)
            && !UserPermissions.isPermissionEnough(userRepository,userid,5))
            throw new UniversalBadReqException("Not your resource pack");

        packRepository.delete(packO.get());

        UniversalResponse response=new UniversalResponse();
        response.message="Removed";
        return response;
    }

    @GetMapping("/user/{userid}")
    public UserPackListResponse getUserPackList(@PathVariable("userid") Integer userId) throws UniversalBadReqException {

        if(!userRepository.existsById(userId)){
            throw new UniversalBadReqException("No such user");
        }

        UserPackListResponse response=new UserPackListResponse();
        response.packs=packRepository.findAllByOwnerId(userId, Sort.unsorted()).stream().map(packModel -> {
            UserPackListResponse.UserPackListItem item=new UserPackListResponse.UserPackListItem(packModel);
            item.packLikes=userPackLikeRepository.countAllByPackId(packModel.getPackId());
            item.liked=userPackLikeRepository.existsUserPackLikeModelByUserIdAndPackId(userId,packModel.getPackId());
            return item;
        }).toList();
        response.count=response.packs.size();

        return response;
    }

    @GetMapping("/user/{userid}/{bookid}")
    public UserPackListResponse getUserPackListByBook(@PathVariable("userid") Integer userId,
                                                      @PathVariable("bookid") Integer bookId) throws UniversalBadReqException {

        if(!userRepository.existsById(userId)) throw new UniversalBadReqException("No such user");

        if(!bookRepository.existsById(bookId)) throw new UniversalBadReqException("No such book");

        UserPackListResponse response=new UserPackListResponse();
        response.packs=packRepository.findAllByOwnerIdAndBookId(userId,bookId).stream().map(packModel -> {
            UserPackListResponse.UserPackListItem item=new UserPackListResponse.UserPackListItem(packModel);
            item.packLikes=userPackLikeRepository.countAllByPackId(packModel.getPackId());
            item.liked=userPackLikeRepository.existsUserPackLikeModelByUserIdAndPackId(userId,packModel.getPackId());
            return item;
        }).toList();
        response.count=response.packs.size();

        return response;
    }

    @GetMapping("/copy/{packId}")
    public CopyPackResponse copyPack(@PathVariable Integer packId,
                                      @RequestHeader("Authorization") String token) throws UniversalBadReqException {
        Integer userid= JwtUtils.getUserIdJWT(token);

        var packO=packRepository.findById(packId);
        if(packO.isEmpty()){
            throw new UniversalBadReqException("No such pack");
        }
        PackModel packModel=packO.get();

        if(packModel.getShared()==0&& !Objects.equals(packModel.getOwnerId(), userid)){
            throw new UniversalBadReqException("Permission denied");
        }

        PackModel newPack=new PackModel();
        newPack.setOwnerId(userid);
        newPack.setShared(0);
        newPack.setContent(packModel.getContent());
        newPack.setName(packModel.getName());
        newPack.setAuthorId(packModel.getAuthorId());
        newPack.setDescription(packModel.getDescription());
        newPack.setUpdateTime(LocalDateTime.now());
        newPack.setBookId(packModel.getBookId());

        packRepository.save(newPack);

        ForumMessageModel.addForumMessage(forumMessageRepository,
                userid,packModel.getOwnerId(),0,"Copied your pack:"+packModel.getPackId(),false);

        CopyPackResponse response=new CopyPackResponse();
        response.message="copy success";
        response.packId=newPack.getPackId();
        return response;
    }

    @GetMapping("/like/{packId}")
    public UniversalResponse likePack(@PathVariable Integer packId,
                                      @RequestHeader("Authorization") String token) throws UniversalBadReqException{
        Integer userid= JwtUtils.getUserIdJWT(token);

        var packO=packRepository.findById(packId);
        if(packO.isEmpty()){
            throw new UniversalBadReqException("No such pack");
        }
        PackModel packModel=packO.get();

        if(userPackLikeRepository.existsUserPackLikeModelByUserIdAndPackId(userid,packId)){
            throw new UniversalBadReqException("Already liked");
        }

        UserPackLikeModel userPackLikeModel=new UserPackLikeModel();
        userPackLikeModel.setUserId(userid);
        userPackLikeModel.setPackId(packId);
        userPackLikeModel.setLikeTime(LocalDateTime.now());
        userPackLikeRepository.save(userPackLikeModel);

        ForumMessageModel.addForumMessage(forumMessageRepository,
                userid,packModel.getOwnerId(),1,"Liked your pack:"+packModel.getPackId(),false);

        UniversalResponse response=new UniversalResponse();
        response.message="Pack liked";
        return response;
    }

    @DeleteMapping("/like/{packId}")
    public UniversalResponse unlikePack(@RequestHeader("Authorization") String token,
                                        @PathVariable Integer packId) throws UniversalBadReqException {
        Integer userId= JwtUtils.getUserIdJWT(token);

        var likeO=userPackLikeRepository.findUserPackLikeModelByUserIdAndPackId(userId,packId);
        if(likeO.isEmpty())
            throw new UniversalBadReqException("Not liked yet");

        userPackLikeRepository.delete(likeO.get());

        UniversalResponse response=new UniversalResponse();
        response.message="Pack unliked";
        return response;
    }

}
