package ismp.crpt.ru;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        CrptApi crptApi = new CrptApi(TimeUnit.MINUTES, 2);
        CrptApi.Document document = new CrptApi.Document(new CrptApi.Description("participantInn"), "doc_id", "doc_status", true, "owner_inn", "participant_inn", "producer_inn", new Date(), "production_type", new CrptApi.Product[]{new CrptApi.Product("certificate_document", new Date(), "certificate_document_number1", "owner_inn1", "producer_inn", new Date(), "tnved_code", "uit_code", "uitu_code"), new CrptApi.Product("certificate_document", new Date(), "certificate_document_number1", "owner_inn1", "producer_inn", new Date(), "tnved_code", "uit_code", "uitu_code")}, new Date(), "reg_number");
        TestThread thread1 = new TestThread(crptApi, document);
        thread1.start();
        TestThread thread2 = new TestThread(crptApi, document);
        thread2.start();
        TestThread thread3 = new TestThread(crptApi, document);
        thread3.start();
        TestThread thread4 = new TestThread(crptApi, document);
        thread4.start();
        TestThread thread5 = new TestThread(crptApi, document);
        thread5.start();
        TestThread thread6 = new TestThread(crptApi, document);
        thread6.start();
    }
}