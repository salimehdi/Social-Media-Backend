package com.socialmedia.SocialMediaApp.httpEntities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {

    private String clientSecret;
    private String paymentIntentId;
    private String status;
    private Long amount;
    private String currency;

}
