
```shell script
$ git clone 
$ mvn package
$ cd ./target/quantcast-codeexercise-dist

$ ./bin/QCount -f ../../data-sample/cookies.csv -d 2018-12-06 -h
NONE_EMPTY

$ DEBUG=true ./bin/QCount -f ../../data-sample/cookies.csv -d 2018-12-06 -h
Project Path: '.'
Include ...
    ./conf
    ./libs/commons-cli-1.4.jar
    ./libs/commons-csv-1.8.jar
    ./libs/javatuples-1.2.jar
    ./libs/logback-classic-1.2.3.jar
    ./libs/logback-core-1.2.3.jar
    ./libs/quantcast-codeexercise-1.0-SNAPSHOT.jar
    ./libs/slf4j-api-1.7.25.jar
...
/Users/me/.sdkman/candidates/java/current/bin/java -cp ./conf:./libs/commons-cli-1.4.jar:./libs/commons-csv-1.8.jar:./libs/javatuples-1.2.jar:./libs/logback-classic-1.2.3.jar:./libs/logback-core-1.2.3.jar:./libs/quantcast-codeexercise-1.0-SNAPSHOT.jar:./libs/slf4j-api-1.7.25.jar -Xms2g -Xmx4g -XX:+UseG1GC -XX:+UnlockExperimentalVMOptions -XX:G1NewSizePercent=30 -XX:G1NewSizePercent=40 -XX:MaxGCPauseMillis=700 -XX:ParallelGCThreads=8 -XX:ConcGCThreads=2 -XX:InitiatingHeapOccupancyPercent=50 -XX:G1MixedGCLiveThresholdPercent=60 -XX:G1ReservePercent=10 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./log/{name}.heapdumps -Dlogback.configurationFile=./conf/logback-debug.xml io.github.sameei.interviews.quantcast.codingexercise.launcher.Launcher
...
10:50:49.227 INFO  io.github.sameei.interviews.quantcast.codingexercise.launcher.Launcher - Init ...
10:50:49.252 INFO  io.github.sameei.interviews.quantcast.codingexercise.launcher.Launcher - Setting: io.github.sameei.interviews.quantcast.codingexercise.launcher.CLI$Setting@3e2e18f2
10:50:49.253 INFO  io.github.sameei.interviews.quantcast.codingexercise.launcher.Launcher - CountingEngine Instantiating ...
10:50:49.255 INFO  io.github.sameei.interviews.quantcast.codingexercise.launcher.Launcher - Start Processing ...
10:50:49.257 DEBUG io.github.sameei.interviews.quantcast.codingexercise.counting.CountingSimply - Instantiated with DataParser{dtf=Value(YearOfEra,4,19,EXCEEDS_PAD)'-'Value(MonthOfYear,2)'-'Value(DayOfMonth,2)'T'Value(HourOfDay,2)':'Value(MinuteOfHour,2)':'Value(SecondOfMinute,2)Offset(+HH:MM,'+00:00'), df=Value(YearOfEra,4,19,EXCEEDS_PAD)'-'Value(MonthOfYear,2)'-'Value(DayOfMonth,2)}, SkipFirstLine: true, FailFast: false
10:50:49.281 WARN  io.github.sameei.interviews.quantcast.codingexercise.counting.CountingSimply - This Counting Class supports FailFast=false only to some degree!This means that this Class might not be able process the the input in some cases.
NONE_EMPTY
(base) ➜  quantcast-codeexercise-dist ./bin/QCount -f ../../data-sample/cookies.csv -d 2018-12-06 -h
NONE_EMPTY
```