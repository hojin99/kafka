package hj.kafka.service;

import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
@Service
public class KafkaConsumerService {

    @Value("${kafka.dest-dir}")
    String destDir;

    @KafkaListener(topics="${kafka.topic}", groupId ="${kafka.group-id}")
    public void consume(String message) throws IOException {

        log.info("consume!");
        String[] msgs = message.split("\\n");

        // 첫번째 4 줄은 전체 메세지의 요약 정보
        String createTime = "";
        String btsName = "";
        String comName = "";
        String TechName = "";
        String measInfoId = ""; // measInfoId로 구분 되어 하위에 measInfoId 별 row가 반복됨 (헤더 포함)

        // measInfoId 별 BufferedWriter
        HashMap<String,Object> files = new HashMap<>();
        // 현재 measInfoId의 BufferedWriter
        BufferedWriter curWriter = null;

        int num = 0; // 현재 라인 번호
        int startNum = 0; // CreateTime 라인번호

        // 라인 별로 loop를 돌면서 각 measInfoId의 BufferedWriter에 데이터 쓰기
        log.info("processing! - " + System.getProperty("PID") + ",length: " + msgs.length);
        for (String str : msgs) {
            num++;

            // windows 환경에서 테스트 시 줄바꿈 문자 제거
            str = str.replace("\r","");

//            log.info(str);

            if(str.indexOf("CreateTime:") >= 0) {
                // 초기화 (CreateTime이 메세지에 반복 될 경우에 대비한 코드)
                createTime = "";
                btsName = "";
                comName = "";
                TechName = "";
                files.clear();
                curWriter = null;
                // 메세지 시작
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
                measInfoId = str.substring(11).replace('/','_').replace(' ','_');

                try {
                    // 파일 생성
                    if (!files.containsKey(measInfoId)) {

                        String temp = destDir + "\\" + createTime.substring(0,16).replace("-","").replace(":","") + "-" + measInfoId + "-" + System.getProperty("PID") + ".dat";
                        log.info("temp  :" + temp);
                        File file = new File(temp);

                        if (file.exists()) file.delete();
                        file.createNewFile();

                        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                        files.put(measInfoId, bw);
                        curWriter = bw;
                    } else {
                        curWriter = (BufferedWriter) files.get(measInfoId);
                    }
                } catch (IOException e) {
                    log.error("measInfoId error : " + e.toString());
                }
                continue;
            } else if(str.indexOf("measTypes,") >= 0) {
                // 헤더는 출력하지 않음 (skip)
                continue;
            } else {
                try {
                    curWriter.write(createTime + "$" + btsName + "$" + comName + "$" + TechName + "$" + str);
                    curWriter.newLine();
                } catch(IOException e) {
                    log.error("curWriter.write error : " + e.toString());
                }
            }
        }

        try {
            // 파일 close
            log.info("clean!");
            Iterator<Map.Entry<String, Object>> entries = files.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, Object> entry = entries.next();
                ((BufferedWriter) entry.getValue()).close();
            }
        } catch(IOException e) {
            log.error("file close error : " + e.toString());
        }

    }
}
