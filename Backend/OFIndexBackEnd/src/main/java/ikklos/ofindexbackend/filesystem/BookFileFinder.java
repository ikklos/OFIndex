package ikklos.ofindexbackend.filesystem;

import ikklos.ofindexbackend.domain.BookModel;
import ikklos.ofindexbackend.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class BookFileFinder {

    private final LocalDocumentConfigs localDocumentConfigs;
    private final BookRepository repository;

    public BookFileFinder(@Autowired LocalDocumentConfigs localDocumentConfigs,
                          @Autowired BookRepository bookRepository){
        this.localDocumentConfigs=localDocumentConfigs;
        this.repository=bookRepository;
    }

    @Nullable
    public Path getBookDocumentPath(BookModel book) throws IOException{

        if(!repository.existsById(book.getBookId()))return null;

        Path path=Path.of(
                localDocumentConfigs.localFileRoot,
                localDocumentConfigs.bookPath,
                book.getBookId().toString()
        );
        if(!Files.isDirectory(path)) {
            //try {
                Files.createDirectories(path);
            //} catch (IOException e) {
            //    return null;
            //}
        }
        return path.resolve(localDocumentConfigs.bookDocument);
    }

    public boolean bookDocumentExist(BookModel book) throws IOException {
        Path file=getBookDocumentPath(book);
        return file!=null&&Files.exists(file);
    }

    public void uploadDocument(MultipartFile file,BookModel book) throws IOException {
        Path path=getBookDocumentPath(book);
        file.transferTo(path);
    }

}
