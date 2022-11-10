# kafka

## 환경
* intellij + spring boot
* 의존성 - spring-kafka + lombok ... 

## 실행
* java로 KafkaApplication을 실행 (Spring Boot Application)
* kafka 설정은 spring-kafka의 application.yml 프로퍼티 기반으로 함 (config 클래스는 구현 후 @Configuration을 comment해 둠)
* Producer는 curl, Postman 등을 통해 POST로 localhost:8080/kafka/sendMessage 호출 (파라메터 message)
* Consumer의 경우 여러 프로세스를 실행 시킬 경우 로드 밸런싱 및 Fail over가 적용 됨
  * 다수 프로세스 실행 시 최대 topic에 partition 개수까지만 로드 밸런싱이 됨
  * 다수 프로세스 실행 시 was가 포함 되기 때문에 server.port를 다르게 실행해야 함

## 참조


