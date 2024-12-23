package ikklos.ofindexbackend.filesystem;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
//@PropertySource("classpath:ofindex.properties")
//TODO better config
public class LocalDocumentConfigs {

    //@Value("${ikklos.ofindexbackend.localFileRoot}")
    public String localFileRoot = "/of_index";

    //@Value("${ikklos.ofindexbackend.localFileBookPath}")
    public String bookPath = "books";

    //@Value("${ikklos.ofindexbackend.localFileBookDocuments}")
    public String bookDocument = "document.pdf";

    //@Value("${ikklos.ofindexbackend.localFileUserPack}")
    public String userDocument = "users";

    public String packContentDefault= "{note:[{title: '',pos:{page: 1,x: 200,y: 300,width: 100,height: 100,}}],diagram:{name: 'C++',children:[{name: '1',children:[{name:'1',}]}]},additionFiles:[{name:'',url:''}]}";

}
