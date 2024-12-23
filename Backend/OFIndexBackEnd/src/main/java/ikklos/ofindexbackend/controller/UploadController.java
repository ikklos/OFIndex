package ikklos.ofindexbackend.controller;

import ikklos.ofindexbackend.domain.BookModel;
import ikklos.ofindexbackend.domain.PackModel;
import ikklos.ofindexbackend.filesystem.LocalDocumentConfigs;
import ikklos.ofindexbackend.repository.BookRepository;
import ikklos.ofindexbackend.repository.PackRepository;
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

    public UploadController(@Autowired BookRepository bookRepository,
                            @Autowired LocalDocumentConfigs localDocumentConfigs,
                            @Autowired PackRepository packRepository){
        this.bookRepository=bookRepository;
        this.packRepository = packRepository;
        this.localDocumentConfigs=localDocumentConfigs;
    }

    @PostMapping("/pack")
    public PackContentResponse uploadPack(@RequestBody UploadPackRequest request,
                                                         @RequestHeader("Authorization") String token) throws UniversalBadReqException {
        Integer userid= JwtUtils.getUserIdJWT(token);

        PackModel packModel;
        if(request.packId!=null){
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

            packModel=new PackModel();

            packModel.setName(request.name);
            packModel.setDescription(request.description);
            packModel.setBookId(request.bookId);
            if(request.content!=null)
                packModel.setContent(request.content);
            else
                packModel.setContent(localDocumentConfigs.packContentDefault);
            packModel.setShared(request.shared?1:0);
            packModel.setLikeCount(0);
            packModel.setAuthorId(userid);
        }

        packModel.setOwnerId(userid);
        packModel.setUpdateTime(LocalDateTime.now());
        packRepository.save(packModel);

        var bookO=bookRepository.findById(packModel.getBookId());
        if(bookO.isEmpty()){
            throw new UniversalBadReqException("This resource pack has invalid book ID");
        }
        BookModel bookModel=bookO.get();

        return new PackContentResponse(packModel,bookModel,null);
    }

}
