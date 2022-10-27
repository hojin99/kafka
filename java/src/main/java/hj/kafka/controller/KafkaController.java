package hj.kafka.controller;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka/")
//@RequiredArgsConstructor
public class KafkaController {
    private final KafkaTemplate producer;

    public KafkaController(KafkaTemplate producer) {
        this.producer = producer;
    }

    @GetMapping(value = "/sendMessage")
    public String sendMessage(String message) {

        this.producer.send("my-topic", message);

        return "success";
    }
}
