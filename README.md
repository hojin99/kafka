# kafka

## 환경

### docker
* kafka cluster 용 docker-compose.yml 구성 

### java
* intellij + spring boot
* 의존성 - spring-kafka + lombok ... 

### nodejs
* nodejs + express
* 의존성 - kafkajs ...

## 실행

### docker
* kafka cluster 실행 
  * docker/docker-compose up -d
* topic 생성, 삭제
  * docker/create-topic.sh
  * docker/delete-topcis.sh
* topic 조회
  * docker/list-topics.sh
  * docker/describe-topics.sh
* produce
  * docker/producer.sh 실행 후 stdin으로 메세지 입력
* consume
  * docker/cousumer1_1.sh, docker/cousumer1_2.sh 실행 (이중화)
  * docker/cousumer2.sh 실행 (다른 group id로 구독)
* 모니터링
  * docker/list-group.sh 실행
  * 각 topic, group-id, partition의 client id 및 offset 상태 확인

### java
* java로 KafkaApplication을 실행 (Spring Boot Application)
* kafka 설정은 spring-kafka의 application.yml 프로퍼티 기반으로 함 (config 클래스는 구현 후 @Configuration을 comment해 둠)
* Producer는 curl, Postman 등을 통해 POST로 localhost:8080/kafka/sendMessage 호출 (파라메터 message)
* Consumer의 경우 여러 프로세스를 실행 시킬 경우 로드 밸런싱 및 Fail over가 적용 됨
  * 다수 프로세스 실행 시 최대 topic에 partition 개수까지만 로드 밸런싱이 됨
  * 다수 프로세스 실행 시 was가 포함 되기 때문에 server.port를 다르게 실행해야 함

### nodejs
* node producer.sh 실행 (express로 메세지 입력 받아서 produce, localhost:3000/events/:event)
* node producer_bulk.sh 실행 (파일을 읽어서, 일정 횟수 반복에서 produce)
* node consumer.sh 실행 (consumer 실행)

## 참조
https://kafka.apache.org/documentation/#gettingStarted  
https://spring.io/projects/spring-kafka  
https://kafka.js.org/docs/getting-started  

