import org.example.dollarproduct.entity.Order;
import org.example.dollarproduct.entity.OrderDetail;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.assertj.core.api.Assertions;

import java.util.List;

public class ProductTest {

    @Test
    public void test() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Order> response = restTemplate
                .getForEntity("http://localhost:8084/external/orders/1", Order.class);
      Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      System.out.println(response.getBody());
    }
}
