package ikklos.ofindexbackend.filesystem;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:ofindex.properties")
public class LocalDocumentConfigs {

    @Value("${ikklos.ofindexbackend.localFileRoot}")
    public String localFileRoot = "/of_index";

    @Value("${ikklos.ofindexbackend.localFileBookPath}")
    public String bookPath = "books";

    @Value("${ikklos.ofindexbackend.localFileBookDocuments}")
    public String bookDocument = "document.pdf";

    @Value("${ikklos.ofindexbackend.localFileUserPack}")
    public String userDocument = "users";

}
