package org.example.dollarproduct;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.example.dollarproduct.entity.OrderDetail;
import org.example.dollarproduct.feign.FeignOrderClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.springframework.cloud.contract.wiremock.WireMockSpring.options;

@SpringBootTest
@ActiveProfiles("test")
@EnableConfigurationProperties
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WireMockConfig.class })
public class FeignTest {

    @Autowired
    private FeignOrderClient feignOrderClient;

    @Autowired
    public WireMockServer wireMockServer = new WireMockServer(options().port(8084));

    @Test
    public void Feigntest(){
        List<OrderDetail> orderDetails = feignOrderClient.findOrderDetailsByProductId(1L);
        Assertions.assertThat(orderDetails).isNotNull();
    }

}
