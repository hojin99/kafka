const express = require('express')
const app = express()
const port = 3000

const { Kafka } = require('kafkajs')

const brokers = "localhost:29092"
const clientId = "my-node-test"
const topic = "my-topic"

// kafka, producer 생성
const kafka = new Kafka({
    clientId: clientId,
    brokers: [brokers]
})

const producer = kafka.producer()

/**
 * kafka producer 연결
 */
const initKafka = async () => {
    await producer.connect()
}

/**
 * post 메세지 받아서 kafka 메세지 보내기
 */
app.post('/events/:event', async (req, res) => {
    console.log(req.params.event)

    await producer.send({
        topic: topic,
        messages: [
            { key: null, value: req.params.event },
        ],
    })

    res.send('successfully stored event : ' + req.params.event + '\n')
})

// express 서비스 listen
app.listen(port, async () => {
    console.log(`kafka app listening on port ${port}`)
})

initKafka()
