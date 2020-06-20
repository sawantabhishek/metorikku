FROM openjdk:11-jre

ARG SPARK_VERSION=3.0.0

RUN wget -q https://archive.apache.org/dist/spark/spark-${SPARK_VERSION}/spark-${SPARK_VERSION}-bin-hadoop3.2.tgz \
    && tar -xzf spark-${SPARK_VERSION}-bin-hadoop3.2.tgz \
    && mv spark-${SPARK_VERSION}-bin-hadoop3.2 spark \
    && rm spark-${SPARK_VERSION}-bin-hadoop3.2.tgz \
    && cd /

RUN apt-get update \
    && apt-get install -y coreutils jq less inotify-tools python3 python3-setuptools \
    && rm -rf /var/lib/apt/lists/*

ARG AWS_SDK_VERSION=1.11.699
ARG HADOOP_AWS_VERSION=3.2.1
ARG HTTPCLIENT_VERSION=4.5.11
    
RUN wget -q https://repo1.maven.org/maven2/net/logstash/log4j/jsonevent-layout/1.7/jsonevent-layout-1.7.jar -P /spark/jars/
RUN wget -q https://repo1.maven.org/maven2/net/minidev/json-smart/1.1.1/json-smart-1.1.1.jar -P /spark/jars/
RUN wget -q https://repo1.maven.org/maven2/org/apache/hadoop/hadoop-aws/${HADOOP_AWS_VERSION}/hadoop-aws-${HADOOP_AWS_VERSION}.jar -P /spark/jars/
RUN wget -q https://repo1.maven.org/maven2/com/amazonaws/aws-java-sdk/${AWS_SDK_VERSION}/aws-java-sdk-${AWS_SDK_VERSION}.jar -P /spark/jars/
RUN wget -q https://repo1.maven.org/maven2/com/amazonaws/aws-java-sdk-core/${AWS_SDK_VERSION}/aws-java-sdk-core-${AWS_SDK_VERSION}.jar -P /spark/jars/
RUN wget -q https://repo1.maven.org/maven2/com/amazonaws/aws-java-sdk-s3/${AWS_SDK_VERSION}/aws-java-sdk-s3-${AWS_SDK_VERSION}.jar -P /spark/jars/

ADD log4j.json.properties /spark/conf/
ADD spark-defaults.conf /spark/conf/

ENV PYTHONHASHSEED 1
ENV SPARK_HOME /spark
ENV PATH="${HADOOP_PREFIX}/bin:/spark/bin:${PATH}"

ADD scripts /scripts
ENTRYPOINT ["/scripts/entrypoint-master.sh"]