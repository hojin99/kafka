package hj.kafka.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/kafka/")
public class KafkaController {
    private final KafkaTemplate producer;

    @Value(value = "${kafka.topic}")
    private String topic;

    public KafkaController(KafkaTemplate producer) {
        this.producer = producer;
    }

    @PostMapping(value = "/sendMessage")
    public String sendMessage(@RequestParam Map<String, Object> param) {

        ListenableFuture<SendResult<String, String>> future =
                this.producer.send(topic, (String)param.get("message"));

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error(ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("Sent message : offset - " + result.getRecordMetadata().offset());
            }
        });

        return "ok";
    }

}
