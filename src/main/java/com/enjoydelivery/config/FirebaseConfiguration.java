package com.enjoydelivery.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirebaseConfiguration {

  @Bean
  public FirebaseMessaging fireBaseMessaging() throws IOException {
    return FirebaseMessaging.getInstance(fireBaseApp());
  }

  @Bean
  public FirebaseApp fireBaseApp() throws IOException {
    FirebaseOptions options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.getApplicationDefault())
        .build();
    return FirebaseApp.initializeApp(options);
  }

}
