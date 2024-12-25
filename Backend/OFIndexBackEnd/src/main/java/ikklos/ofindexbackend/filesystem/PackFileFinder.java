package ikklos.ofindexbackend.filesystem;

import ikklos.ofindexbackend.domain.PackModel;
import ikklos.ofindexbackend.repository.BookRepository;
import ikklos.ofindexbackend.repository.PackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Component
public class PackFileFinder {

    private final LocalDocumentConfigs localDocumentConfigs;
    private final PackRepository packRepository;
    private final BookRepository bookRepository;

    public PackFileFinder(@Autowired LocalDocumentConfigs localDocumentConfigs,
                          @Autowired PackRepository packRepository,
                          @Autowired BookRepository bookRepository){
        this.localDocumentConfigs=localDocumentConfigs;
        this.packRepository=packRepository;
        this.bookRepository=bookRepository;
    }

    private Path getSharedPackRootPath(Integer bookid) throws IOException {

        if(!bookRepository.existsById(bookid))return null;

        Path path=Path.of(
            localDocumentConfigs.localFileRoot,
                localDocumentConfigs.bookPath,
                bookid.toString()
        );

        if(!Files.isDirectory(path))Files.createDirectories(path);
        return path;
    }

    @Nullable
    public Path getSharedPackDocumentPath(Integer bookid,Integer packid) throws IOException {
        var option= packRepository.findById(packid);
        if(option.isEmpty())
            return null;

        PackModel model= option.get();

        if(!Objects.equals(model.getBookId(), bookid))return null;

        Path path=getSharedPackRootPath(bookid);

        //TODO security

        path=path.resolve(packid.toString());

        return path;
    }

    public Path getPackDocumentPath(PackModel packModel){
        //TODO finish
        return null;
    }

}
