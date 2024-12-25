package ikklos.ofindexbackend.filesystem;

import ikklos.ofindexbackend.domain.FileModel;
import ikklos.ofindexbackend.repository.FileRepository;
import ikklos.ofindexbackend.utils.UniversalBadReqException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class UserFileFinder {

    private final LocalDocumentConfigs localDocumentConfigs;
    private final FileRepository repository;

    public UserFileFinder(@Autowired LocalDocumentConfigs localDocumentConfigs,
                          @Autowired FileRepository bookRepository){
        this.localDocumentConfigs=localDocumentConfigs;
        this.repository=bookRepository;
    }

    @Nullable
    public Path getFilePath(FileModel fileModel) throws IOException {

        if(!repository.existsById(fileModel.getFileId()))return null;

        Path path=Path.of(
                localDocumentConfigs.localFileRoot,
                localDocumentConfigs.userDocument,
                fileModel.getUserId().toString()
        );
        if(!Files.isDirectory(path)) {
            //try {
            Files.createDirectories(path);
            //} catch (IOException e) {
            //    return null;
            //}
        }
        return path.resolve(fileModel.getFileId().toString());
    }

    public void uploadDocument(MultipartFile file, FileModel fileModel) throws IOException, UniversalBadReqException {
        Long currentTotal = repository.findFileModelsByUserId(fileModel.getUserId(), Sort.unsorted()).stream()
                .map(FileModel::getSize).reduce(0L,Long::sum);
        if(currentTotal+file.getSize()>localDocumentConfigs.userFileSpaceSize){
            throw new UniversalBadReqException("No enough user storage space:need:"+currentTotal);
        }
        Path path=getFilePath(fileModel);
        file.transferTo(path);
    }

}
