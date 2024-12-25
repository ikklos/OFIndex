package ikklos.ofindexbackend.controller;

import ikklos.ofindexbackend.domain.FileModel;
import ikklos.ofindexbackend.filesystem.UserFileFinder;
import ikklos.ofindexbackend.repository.FileRepository;
import ikklos.ofindexbackend.repository.UserRepository;
import ikklos.ofindexbackend.utils.JwtUtils;
import ikklos.ofindexbackend.utils.UniversalBadReqException;
import ikklos.ofindexbackend.utils.UserPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping(value = "/file",produces = "application/json")
public class FileController {


    public static class FileInfo {
        public Integer fileId;
        public Integer ownerId;
        public String name;
        public Long size;
    }

    public static class UploadFileRequest{
        public String name;
        public Boolean shared;
    }

    private final FileRepository fileRepository;
    private final UserFileFinder userFileFinder;
    private final UserRepository userRepository;

    @Autowired
    public FileController(FileRepository fileRepository,
                          UserFileFinder userFileFinder,
                          UserRepository userRepository){
        this.fileRepository=fileRepository;
        this.userFileFinder = userFileFinder;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<FileInfo> getUserFile(@RequestHeader("Authorization") String token){
        Integer userid= JwtUtils.getUserIdJWT(token);

        return fileRepository.findFileModelsByUserId(userid, Sort.unsorted()).stream().map(fileModel -> {
            FileInfo info=new FileInfo();
            info.fileId=fileModel.getFileId();
            info.ownerId=fileModel.getUserId();
            info.name=fileModel.getName();
            info.size=fileModel.getSize();
            return info;
        }).toList();
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer fileId,
                                                 @RequestHeader("Authorization") String token) throws IOException, UniversalBadReqException {
        Integer userId=JwtUtils.getUserIdJWT(token);

        var fileO=fileRepository.findById(fileId);
        if(fileO.isEmpty()){
            throw new UniversalBadReqException("No such file");
        }

        FileModel fileModel=fileO.get();
        if(fileModel.getShared()==0&&!Objects.equals(userId,fileModel.getUserId())
            && UserPermissions.noPermission(userRepository, userId, 5)){
            throw new UniversalBadReqException("Permission denied");
        }

        Path filePath = userFileFinder.getFilePath(fileModel);
        Resource resource = new org.springframework.core.io.UrlResource(filePath.toUri());
        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileModel.getName() + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/upload")
    public FileInfo uploadFile(@RequestPart("file") MultipartFile file,
                               @RequestPart("formData") UploadFileRequest request,
                               @RequestHeader("Authorization") String token) throws IOException, UniversalBadReqException {
        Integer userId= JwtUtils.getUserIdJWT(token);
        FileInfo response=new FileInfo();

        FileModel fileModel=new FileModel();
        fileModel.setName(request.name);
        fileModel.setUserId(userId);
        fileModel.setSize(file.getSize());
        fileModel.setShared(request.shared?1:0);

        fileRepository.save(fileModel);
        try {
            userFileFinder.uploadDocument(file, fileModel);
        }catch (Exception e){
            fileRepository.delete(fileModel);
            throw e;
        }

        response.name=fileModel.getName();
        response.size=fileModel.getSize();
        response.ownerId=userId;
        response.fileId=fileModel.getFileId();

        return response;
    }

}
