import org.example.dollarproduct.entity.Order;
import org.example.dollarproduct.entity.OrderDetail;
import org.example.share.config.global.entity.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.assertj.core.api.Assertions;

import java.util.List;

public class FeignTest {

    //실행하기 전에 FeignClient에 url이 local인지 확인
    //각 서버가 실행이 되고 있는지 확인
    @Test
    @DisplayName("getById api 통신 테스트")
    public void OrderFeigntest() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Order> response = restTemplate
                .getForEntity("http://localhost:8084/external/orders/1", Order.class);
      Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      System.out.println(response.getBody());
    }

    @Test
    @DisplayName("findOrderDetailsByProductId api 통신 테스트")
    public void OrderFeigntest2(){
        RestTemplate restTemplate = new RestTemplate();
        String productId = "1";
        ResponseEntity<List<OrderDetail>> response =
                restTemplate.exchange(
                        "http://localhost:8084/external/"+productId+"/orderDetails",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<OrderDetail>>() {} );
        List<OrderDetail> orderDetails = response.getBody();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(orderDetails).isNotNull();
        System.out.println(orderDetails.size());
    }

    @Test
    @DisplayName("findById api 통신 테스트")
    public void UserFeigntest() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<User> response = restTemplate
                .getForEntity("http://localhost:8082/external/users/1", User.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(response.getBody());
    }
}
