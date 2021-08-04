# Springboot AWS SQS Services

## Run localstack (needed for SQS locally)

Execute docker-compose.yml

````
docker-compose up
````

## Create local queues
User processed queues

````
aws --endpoint-url=http://localhost:4566 --region=us-eats-1 sqs create-queue --queue-name async-user-queue
````