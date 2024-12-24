package ikklos.ofindexbackend.controller;

import ikklos.ofindexbackend.domain.BookModel;
import ikklos.ofindexbackend.domain.ForumMessageModel;
import ikklos.ofindexbackend.domain.PackModel;
import ikklos.ofindexbackend.filesystem.LocalDocumentConfigs;
import ikklos.ofindexbackend.repository.*;
import ikklos.ofindexbackend.utils.JwtUtils;
import ikklos.ofindexbackend.utils.PackContentResponse;
import ikklos.ofindexbackend.utils.UniversalBadReqException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping(value = "/upload",produces = "application/json")
public class UploadController {


    public static class UploadPackRequest {
        public Integer packId;
        public Integer bookId;
        public String name;
        public String description;
        public String content;
        public Boolean shared;
    }

    private final PackRepository packRepository;
    private final BookRepository bookRepository;
    private final LocalDocumentConfigs localDocumentConfigs;
    private final UserPackLikeRepository userPackLikeRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final ForumMessageRepository forumMessageRepository;

    @Autowired
    public UploadController(BookRepository bookRepository,
                            LocalDocumentConfigs localDocumentConfigs,
                            PackRepository packRepository,
                            UserPackLikeRepository userPackLikeRepository, SubscriptionRepository subscriptionRepository, ForumMessageRepository forumMessageRepository){
        this.bookRepository=bookRepository;
        this.packRepository = packRepository;
        this.localDocumentConfigs=localDocumentConfigs;
        this.userPackLikeRepository = userPackLikeRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.forumMessageRepository = forumMessageRepository;
    }

    @PostMapping("/pack")
    public PackContentResponse uploadPack(@RequestBody UploadPackRequest request,
                                                         @RequestHeader("Authorization") String token) throws UniversalBadReqException {
        Integer userid= JwtUtils.getUserIdJWT(token);

        PackModel packModel;
        boolean isUpdate;
        if(request.packId!=null){
            isUpdate = false;
            if(request.bookId!=null&&!bookRepository.existsById(request.bookId)){
                throw new UniversalBadReqException("No such book");
            }

            var packO=packRepository.findById(request.packId);
            if(packO.isEmpty()){
                throw new UniversalBadReqException("Invalid pack ID");
            }
            packModel=packO.get();

            if(!Objects.equals(packModel.getOwnerId(), userid)){
                throw new UniversalBadReqException("Not your pack");
            }

            if(request.name!=null)packModel.setName(request.name);
            if(request.description!=null)packModel.setDescription(request.description);
            if(request.bookId!=null)packModel.setBookId(request.bookId);
            if(request.content!=null)packModel.setContent(request.content);
            if(request.shared!=null)packModel.setShared(request.shared?1:0);
        }else{
            if(!bookRepository.existsById(request.bookId)){
                throw new UniversalBadReqException("No such book");
            }

            isUpdate=true;
            packModel=new PackModel();

            packModel.setName(request.name);
            packModel.setDescription(request.description);
            packModel.setBookId(request.bookId);
            if(request.content!=null)
                packModel.setContent(request.content);
            else
                packModel.setContent(localDocumentConfigs.packContentDefault);
            packModel.setShared(request.shared?1:0);
            packModel.setAuthorId(userid);
        }

        packModel.setOwnerId(userid);
        packModel.setUpdateTime(LocalDateTime.now());
        packRepository.save(packModel);

        if(packModel.getShared()!=0)
            subscriptionRepository.findSubscriptionModelsByFollowingId(userid).forEach(
                subscriptionModel -> {
                    ForumMessageModel.addForumMessage(forumMessageRepository,
                            userid,subscriptionModel.getFollowerId(),0,
                            (isUpdate?"Updated pack:":"Uploaded new pack:")+packModel.getPackId(),
                            subscriptionModel.getNotification()!=0);
                }
            );

        var bookO=bookRepository.findById(packModel.getBookId());
        if(bookO.isEmpty()){
            throw new UniversalBadReqException("This resource pack has invalid book ID");
        }
        BookModel bookModel=bookO.get();

        return new PackContentResponse(packModel,bookModel,null,userPackLikeRepository);
    }

}
