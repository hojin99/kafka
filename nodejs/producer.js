const express = require('express')
const app = express()
const port = 3000

const { Kafka } = require('kafkajs')

const kafka = new Kafka({
    clientId: 'my-app',
    brokers: ['localhost:29092']
})

const producer = kafka.producer()

const initKafka = async () => {
    await producer.connect()
}

app.post('/events/:event', async (req, res) => {
    console.log(req.params.event)

    await producer.send({
        topic: 'my-topic',
        messages: [
            { key: null, value: req.params.event },
        ],
    })

    res.send('successfully stored event : ' + req.params.event + '\n')
})

app.listen(port, async () => {
    console.log(`kafka app listening on port ${port}`)
})

initKafka()
