const { Kafka } = require('kafkajs')

const kafka = new Kafka({
    clientId: 'node-test',
    brokers: ['localhost:29092']
})

const consumer = kafka.consumer({ groupId: 'my-group3'})

const initKafka = async () => {
    console.log('start subscribe')
    await consumer.connect()
    await consumer.subscribe({
        topic: 'my-topic',
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
