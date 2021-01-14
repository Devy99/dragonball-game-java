FROM openjdk:8

RUN mkdir /artifact
RUN cd artifact

WORKDIR /artifact

RUN apt update
RUN apt install curl

RUN apt-get install -y libxrender1 libxtst6 libxi6

RUN curl -L -H "Accept: application/vnd.github.v3+json" -H "Authorization: {token}" https://api.github.com/repos/Devy99/dragonball-game-java/actions/artifacts/35346488/zip --output game.zip

RUN unzip game.zip

CMD ["java", "-jar", "DragonBallGame-1.0.jar"]