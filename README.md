### Concurrent TCP Server with Kotlin and Coroutines
#### Getting started

- clone project
```shell
$ git clone https://github.com/telkomdev/Poppy.git
```

- build and download dependencies
```shell
$ mvn clean package
```

- Run 
```shell
$ PORT=9000 QUEUE_SIZE=100 java -jar target/Poppy-1.0-SNAPSHOT-jar-with-dependencies.jar
```

- Client using `nc`
```shell
$ nc localhost 9000
$ hello
```

### Docker
```
$ make build
```

Run
```
$ docker run --rm -p 9000:9000 --env PORT=9000 --env QUEUE_SIZE=100 poppy
```

Test it
```
$ docker exec -it CONTAINER_ID nc localhost:9000
$ hello
```