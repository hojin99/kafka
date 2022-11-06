const { Kafka } = require('kafkajs')

const brokers = "localhost:29092"
const clientId = "my-node-test"
const topic = "my-topic"
const groupId = "my-group3"

const kafka = new Kafka({
    clientId: clientId,
    brokers: [brokers]
})

const consumer = kafka.consumer({ groupId: groupId})

const initKafka = async () => {
    console.log('start subscribe')
    await consumer.connect()
    await consumer.subscribe({
        topic: topic,
        fromBeginning: true
    })
    await consumer.run({
        eachMessage: async ({topic, partition, message}) => {
            console.log({
                value: message.value.toString(),
            })
        }
    })
}

initKafka()
