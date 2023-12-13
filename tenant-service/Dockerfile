FROM maven:3.9-amazoncorretto-21

WORKDIR /tenant-service-api
COPY . .
#RUN mvn clean install

CMD mvn clean spring-boot:run -Dspring-boot.run.profiles=docker