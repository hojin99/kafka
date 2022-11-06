const { Kafka } = require('kafkajs')
const fs = require('fs')

const brokers = "localhost:29092"
const clientId = "my-node-test"
const topic = "my-topic"
const inFile = "../message.txt"

let intervalId;

// kafka, producer 생성
const kafka = new Kafka({
    clientId: clientId,
    brokers: [brokers]
})
const producer = kafka.producer()

/**
 * 파일 읽어서 현재 시간 메세지 보내기
 */
const sendMessage = () => {

    fs.readFile(inFile, 'utf8', async (err, data) => {
        if(err) {
            console.error(err)
            return
        } 

        console.log("send")
    
        data = "CreateTime:" + new Date().toISOString() + data.substring(data.indexOf("\n"))
        
        await producer.send({
            topic: topic,
            messages: [
                { value: data },
            ],
        })

    })

}

/**
 * kafka producer 연결, 작업 스케줄 시작
 */
const initKafka = async () => {
    await producer.connect()

    intervalId = setInterval(sendMessage, 1000*60);
}

initKafka()

// kafka producer 종료 (일정 시간 작업 후)
setTimeout(async () => {
    clearInterval(intervalId);
    console.log("end")

    await producer.disconnect()
}, 5*60*1000);
