#### Clone & Build
```shell script
$ git clone https://github.com/sameei/interviews.git
$ cd interviews/QCount
$ mvn package


# package to install/release
$ ls target/interview-qcount-dist.tar.gz 

# sample of the package
$ cd target/interview-qcount-dist
```
#### Cli Arguments
```shell script
$ ./bin/QCount
INVALID_ARG: File ('f') is required!
usage: QCount
 -d <arg>        Date to filter DataSet
 -e <arg>        Select Counting Engine: SIMPLE, LAZY, WEIRED; Default:
                 SIMPLE
 -f <arg>        Path to the File
    --failfast   FailFast: Fail if encountered malformed csv-record;
                 Default=false
    --noheader   If the csv-file has no header
```
#### Successful Run
```shell script
$ ./bin/QCount -f ../../data-sample/cookies.csv -d 2018-12-09
AtY0laUfhglK3lC7
```
```shell script
$ ./bin/QCount -f ../../data-sample/cookies.csv -d 2018-12-08
fbcn5UAVanZf6UtG
SAZuXPGUrfbcn5UA
4sMM2LxV07bPJzwf
```
```shell script
$ ./bin/QCount -f ../../data-sample/cookies.csv -d 2018-12-07
4sMM2LxV07bPJzwf
```
```shell script
$ ./bin/QCount -f ../../data-sample/cookies.csv -d 2018-12-06
NONE_EMPTY
```
#### Run in DEBUG (verbos) mood
```shell script
$ DEBUG=true ./bin/QCount -f ../../data-sample/cookies.csv -d 2018-12-06
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