package com.assingment.Milestone2.Kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer {
  public static final String topic = "mytopic";
    public static final String topic2 = "mytopic2";


    @Autowired
  private KafkaTemplate<String, String> kafkaTemp;
  
  public void publishToTopic(String message) {
	  System.out.println("Publishing to topic "+topic);
      System.out.println("Publishing to topic2 "+topic2);

      this.kafkaTemp.send(topic, message);
  }
    public void publishToTopic2(String message) {
        System.out.println("Publishing to topic2  "+topic2);

        this.kafkaTemp.send(topic2, message);
    }
}
