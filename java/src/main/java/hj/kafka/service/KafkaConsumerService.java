package hj.kafka.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics="my-topic", groupId ="my-group4")
    public void consume(String message) throws IOException {

        String[] msgs = message.split("\\n");

        String createTime = "";
        String btsName = "";
        String comName = "";
        String TechName = "";
        String measInfoId = "";
        int num = 0;
        int startNum = 0;

        for (String str : msgs) {
            num++;

            if(str.indexOf("CreateTime:") >= 0) {
                createTime = str.substring(11);
                startNum = num;
                continue;
            } else if(num == startNum+1) {
                btsName = str;
                continue;
            } else if(num == startNum+2) {
                comName = str;
                continue;
            } else if(num == startNum+3) {
                TechName = str;
                continue;
            } else if(str.indexOf("measInfoId,") >= 0) {
                measInfoId = str.substring(11);
                continue;
            } else if(str.indexOf("measTypes,") >= 0) {
                //skip
                continue;
            } else {
                str = createTime + "$" + btsName + "$" + comName + "$" + TechName + "$" + measInfoId + "$" + str;
            }
            System.out.println(str);
        }
    }
}
