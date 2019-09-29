### Concurrent TCP Server with Kotlin
#### Getting started

- clone projet
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

- Unit test
```shell
$ mvn test
```