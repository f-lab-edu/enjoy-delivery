package com.enjoydelivery.service;

import com.enjoydelivery.exception.InvalidFireBaseException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PushService {

  private final FirebaseMessaging messaging;

  @Async
  public void sendMessage(Message message) {
    try {
      messaging.send(message);
    } catch (FirebaseMessagingException fme ) {
      throw new InvalidFireBaseException();
    }
  }
}
