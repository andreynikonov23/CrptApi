package ismp.crpt.ru;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class CrptApi {
    private final URI CRYPT_API_CREATE_DOC_URL = URI.create("https://ismp.crpt.ru/api/v3/lk/documents/create");
    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private final AtomicInteger requestCounter = new AtomicInteger();
    private TimeUnit timeUnit;
    private int requestLimit;

    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        if (requestLimit <= 0) {
            throw new RuntimeException();
        } else {
            init(timeUnit, requestLimit);
        }
    }

    public HttpResponse<String> createDoc(Document document, String signature) {
        HttpResponse<String> response = null;
        if (!(recordAndCheckRequestLimit())) {
            try {
                String json = JSONCrptApiDocConverter.convertDocToJSON(document);
                response = sendRequestToCrptApi(json);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Request Limit");
        }
        return response;
    }

    private void init(TimeUnit timeUnit, int requestLimit) {
        this.timeUnit = timeUnit;
        this.requestLimit = requestLimit;
        resetCounter();
    }

    private void resetCounter() {
        System.out.println("reset counter");
        final Runnable runnable = () -> requestCounter.set(0);
        scheduledExecutorService.scheduleAtFixedRate(runnable, 0, 1, timeUnit);
    }
    private boolean recordAndCheckRequestLimit() {
        synchronized (this) {
            System.out.println(requestCounter.incrementAndGet());
            return requestCounter.get() > requestLimit;
        }
    }


    private HttpResponse<String> sendRequestToCrptApi(String body) throws IOException, InterruptedException {
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
    @EqualsAndHashCode
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

        @Override
        public String toString() {
            return String.format("Document {description: {%s}," +
                    " doc_id: \"%s\"," +
                    " doc_status: \"%s\"," +
                    " doc_type: \"LP_INTRODUCE_GOODS\"," +
                    " importRequest: true," +
                    " owner_inn: \"%s\"," +
                    " participant_inn: \"%s\"," +
                    " producer_inn: \"%s\"," +
                    " production_date: \"%s\"," +
                    " production_type: \"%s\",}" +
                    " products: \"%s\"," +
                    " reg_date: \"%s\"," +
                    " reg_number: \"%s\"", description, id, status, importRequest, ownerInn, participantInn, producerInn, productionDate.toString(), productionType, products, regDate.toString(), regNumber);
        }
    }
    @JsonAutoDetect
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
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

        @Override
        public String toString() {
            return String.format("Product {certificate_document: \"%s\"," +
                    " certificate_document_date: \"%s\"," +
                    " certificate_document_number: \"%s\"," +
                    " owner_inn: \"%s\"," +
                    " producer_inn: \"%s\"," +
                    " production_date: \"%s\"," +
                    " tnved_code: \"%s\"," +
                    " uit_code: \"%s\"," +
                    " uitu_code: \"%s\"}", certificateDocument, certificateDocumentDate.toString(), certificateDocumentNumber, ownerInn, producerInn, productionDate.toString(), tnvedCode, uitCode, uituCode);
        }
    }
    @JsonAutoDetect
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    static class Description {
        @JsonProperty("participantInn")
        private String participantInn;

        @Override
        public String toString() {
            return String.format("Description {participantInn: \"%s\"}", participantInn);
        }
    }

    static class JSONCrptApiDocConverter {
        private static String convertDocToJSON(Document document) throws IOException {
            StringWriter stringWriter = new StringWriter();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(stringWriter, document);
            return stringWriter.toString();
        }
    }
}
