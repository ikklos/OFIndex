package ikklos.ofindexbackend.utils;

import ikklos.ofindexbackend.domain.BookModel;
import ikklos.ofindexbackend.domain.PackModel;
import ikklos.ofindexbackend.domain.UserModel;
import ikklos.ofindexbackend.repository.UserPackLikeRepository;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

public class PackContentResponse extends UniversalResponse{
    public Integer packId;
    public Integer bookId;
    public String bookName;
    public String bookCover;
    public Integer authorId;
    public String authorName;
    public String authorAvatar;
    public String name;
    public String description;
    public Integer likes;
    public Boolean liked;
    public LocalDateTime updateTime;
    public String content;

    public PackContentResponse(PackModel packModel, BookModel bookModel,
                               @Nullable UserModel userModel,
                               UserPackLikeRepository repository){
        packId=packModel.getPackId();
        bookId=packModel.getBookId();
        bookName=bookModel.getName();
        bookCover=bookModel.getCover();
        name=packModel.getName();
        description=packModel.getDescription();
        likes=repository.countAllByPackId(packId);
        liked=userModel != null&&repository.existsUserPackLikeModelByUserIdAndPackId(userModel.getUserid(),packId);
        updateTime=packModel.getUpdateTime();
        content=packModel.getContent();
        if(userModel!=null) {
            authorId=userModel.getUserid();
            authorAvatar = userModel.getAvatar();
            authorName = userModel.getUsername();
        }
    }

}
