package ismp.crpt.ru;

import java.net.http.HttpResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TestThread extends Thread {
    private final CrptApi crptApi;
    private final CrptApi.Document document;
    public TestThread (CrptApi crptApi, CrptApi.Document document) {
        this.crptApi = crptApi;
        this.document = document;
    }

    @Override
    public void run() {
        while (true) {
            HttpResponse<String> response = crptApi.createDoc(document, "test");
            if (response != null) {
                System.out.println(response.body());
            }
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
