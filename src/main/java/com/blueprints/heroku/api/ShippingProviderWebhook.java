package com.blueprints.heroku.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shipping")
public class ShippingProviderWebhook {


    @PostMapping("/shipping_offers")
public ResponseEntity<String> getShippingOffers(@RequestBody String payload) {

    String response = "[\n" +
            "  {\n" +
            "    \"offer\": \"TRADITIONAL\",\n" +
            "    \"provider_rate\": \"USPS_TRADITIONAL_RATE\",\n" +
            "    \"service_level\": \"TRADITIONAL\",\n" +
            "    \"delivery_estimate\": {\n" +
            "      \"starts_at\": \"2019-08-24T14:15:22Z\",\n" +
            "      \"ends_at\": \"2019-08-24T14:15:22Z\",\n" +
            "      \"expires_at\": \"2019-08-24T14:15:22Z\"\n" +
            "    },\n" +
            "    \"quote\": {\n" +
            "      \"price\": 0,\n" +
            "      \"currency\": \"USD\"\n" +
            "    }\n" +
            "  }\n" +
            "]";
    return ResponseEntity.ok(response);
}


}
