FROM jetbrains/teamcity-agent:latest

LABEL maintainer="Maurycy Krzemiński"

# JDK preparation start

# Amazon Coretto 17
ARG JDK_URL='https://corretto.aws/downloads/latest/amazon-corretto-17-x64-linux-jdk.tar.gz'

USER root
RUN set -eux; \
    curl -LfsSo /tmp/openjdk.tar.gz ${JDK_URL}; \
    mkdir -p /usr/local/java/openjdk; \
    cd /usr/local/java/openjdk; \
    tar -xf /tmp/openjdk.tar.gz --strip-components=1; \
    rm -rf /tmp/openjdk.tar.gz;

ENV JAVA_HOME=/usr/local/java/openjdk

USER buildagent
# JDK preparation end
##################################