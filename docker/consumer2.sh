docker-compose exec kafka kafka-console-consumer --topic my-topic --bootstrap-server kafka:9092 --group my-group2 --consumer-property client.id=test-console enable.auto.commit=true auto.offset.reset=latest 