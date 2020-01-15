# java-city-users

To build

    mvn clean install
    
To run the application via the `spring-boot:run` maven goal

    mvn spring-boot:run -Dspring-boot.run.arguments=London,50,51.5074,0.1278
    
To run the application as an executable jar file

    LOGGING_LEVEL_ROOT=error SPRING_MAIN_BANNER_MODE=off java -jar target/consuming-rest-0.0.1-SNAPSHOT.jar London 50 51.5074 0.1278
    
The usage is

    LOGGING_LEVEL_ROOT=error SPRING_MAIN_BANNER_MODE=off java -jar target/consuming-rest-0.0.1-SNAPSHOT.jar <city> <distance in miles> <latitude> <longitude>
    
So in the example the latitude and longitude of 51.5074 and 0.1278 corresponds to London .
