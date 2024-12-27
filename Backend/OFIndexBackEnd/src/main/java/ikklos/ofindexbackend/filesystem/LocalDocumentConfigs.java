package ikklos.ofindexbackend.filesystem;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LocalDocumentConfigs {

    @Value("${ikklos.ofindexbackend.localFileRoot}")
    public String localFileRoot = "/of_index";

    @Value("${ikklos.ofindexbackend.localFileBookPath}")
    public String bookPath = "books";

    @Value("${ikklos.ofindexbackend.localFileBookDocuments}")
    public String bookDocument = "document.pdf";

    @Value("${ikklos.ofindexbackend.localFileUserPack}")
    public String userDocument = "users";

    @Value("${ikklos.ofindexbackend.packDefaultContent}")
    public String packContentDefault= "{\"note\":[{\"id\":\"67dc8bae-4343-4de7-8fd8-268574eed106\",\"name\":\"1111\",\"rect\":null,\"children\":[]},{\"id\":\"cbd6e4ce-a5e8-4ccb-90d2-ec580717fbac\",\"name\":\"1111\",\"rect\":null,\"children\":[]}],\"diagram\":{\"nodeData\":{\"id\":\"c9ee977189f3b1f1\",\"topic\":\"Root\",\"root\":true,\"children\":[]}}}";

    @Value("${ikklos.ofindexbackend.userFileSpace}")
    public long userFileSpaceSize = 200*1024*1024L;

}
