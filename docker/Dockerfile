FROM openjdk:21

ENV APP_NAME="contas-pagar.jar"

COPY ./target/${APP_NAME} ${APP_NAME}
ENTRYPOINT ["java", "-jar", "contas-pagar.jar"]
