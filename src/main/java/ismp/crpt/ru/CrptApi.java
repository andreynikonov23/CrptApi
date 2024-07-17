package ismp.crpt.ru;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CrptApi {
    public CrptApi(){}
    public CrptApi(TimeUnit timeUnit, int requestLimit) {

    }

    public Document createDoc() {
        return new Document();
    }

    @JsonAutoDetect
    class Document {
        private String id;
        private String status;
        private DocType type;
        private boolean importRequest;
        private String ownerInn;
        private String participantInn;
        private List<Product> products;
        private Date regDate;
        private String regNumber;

    }
    @JsonAutoDetect
    class Product {
        private String certificateDocument;
        private Date certificateDate;
        private String certificateDocumentNumber;
        private String ownerInn;
        private String producerInn;
        private Date productionDate;
        private String tnvedCode;
        private String uitCode;
        private String uituCode;
    }
    @JsonAutoDetect
    enum DocType {
        LP_INTRODUCE_GOODS;
    }
}
