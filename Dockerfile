FROM hseeberger/scala-sbt:17.0.2_1.6.2_3.1.1

RUN apt-get update && \
    apt-get install -y --no-install-recommends

RUN apt-get install -y libxrender1
RUN apt-get install -y libxtst6
RUN apt-get install -y libxi6
RUN apt-get install -y openjfx

EXPOSE 8080 8080
WORKDIR /uno
ADD . /uno
CMD sbt run