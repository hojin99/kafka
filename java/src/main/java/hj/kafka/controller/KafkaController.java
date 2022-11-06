package hj.kafka.controller;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/kafka/")
public class KafkaController {
    private final KafkaTemplate producer;

    public KafkaController(KafkaTemplate producer) {
        this.producer = producer;
    }

    @PostMapping(value = "/sendMessage")
    public String sendMessage(@RequestParam Map<String, Object> param) {

        this.producer.send("my-topic", (String)param.get("message"));

        return "success";
    }
}
