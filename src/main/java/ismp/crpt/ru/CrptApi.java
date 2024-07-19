package ismp.crpt.ru;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class CrptApi {
    private final URI CRYPT_API_CREATE_DOC_URL = URI.create("https://ismp.crpt.ru/api/v3/lk/documents/create");
    private TimeUnit timeUnit;
    private int requestLimit;

    public CrptApi() {
        Product[] products = new Product[2];
        Product product1 = new Product("certificateDocument1", new Date(), "certificateDocument1", "certificateDocument1", "certificateDocument1", new Date(), "certificateDocument1", "certificateDocument1", "certificateDocument1");
        Product product2 = new Product("certificateDocument2", new Date(), "certificateDocument2", "certificateDocument2", "certificateDocument2", new Date(), "certificateDocument2", "certificateDocument2", "certificateDocument2");
        products[0] = product1;
        products[1] = product2;
        Document document = new Document(new Description("participantInn"), "id", "status", true, "ownerInn", "participantInn", "participantInn", new Date(), "participantInn", products, new Date(), "participantInn");
        Date productionDate = document.getProductionDate();
        createDoc(document, "test");
    }
    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.timeUnit = timeUnit;
        this.requestLimit = requestLimit;
    }

    public void createDoc(Document document, String signature) {
        try {
            String json = convertToJSON(document);
            sendRequestToCryptApi(json);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String convertToJSON(Document document) throws IOException {
        StringWriter stringWriter = new StringWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(stringWriter, document);
        return stringWriter.toString();
    }

    public HttpResponse sendRequestToCryptApi(String body) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(CRYPT_API_CREATE_DOC_URL)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @JsonAutoDetect
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    static
    class Document {
        private Description description;
        @JsonProperty("doc_id")
        private String id;
        @JsonProperty("doc_status")
        private String status;
        @JsonProperty("doc_type")
        private final String type = "LP_INTRODUCE_GOODS";
        private boolean importRequest;
        @JsonProperty("owner_inn")
        private String ownerInn;
        @JsonProperty("participant_inn")
        private String participantInn;
        @JsonProperty("producer_inn")
        private String producerInn;
        @JsonProperty("production_date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date productionDate;
        @JsonProperty("production_type")
        private String productionType;
        private Product[] products;
        @JsonProperty("reg_date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date regDate;
        @JsonProperty("reg_number")
        private String regNumber;

    }
    @JsonAutoDetect
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    static class Product {
        @JsonProperty("certificate_document")
        private String certificateDocument;
        @JsonProperty("certificate_document_date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date certificateDocumentDate;
        @JsonProperty("certificate_document_number")
        private String certificateDocumentNumber;
        @JsonProperty("owner_inn")
        private String ownerInn;
        @JsonProperty("producer_inn")
        private String producerInn;
        @JsonProperty("production_date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date productionDate;
        @JsonProperty("tnved_code")
        private String tnvedCode;
        @JsonProperty("uit_code")
        private String uitCode;
        @JsonProperty("uitu_code")
        private String uituCode;
    }
    @JsonAutoDetect
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    static class Description {
        @JsonProperty("participantInn")
        private String participantInn;
    }
    @JsonAutoDetect
    enum DocType {
        LP_INTRODUCE_GOODS;
    }
}
