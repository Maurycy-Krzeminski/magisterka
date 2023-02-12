FROM jetbrains/teamcity-agent:2022.10.2-linux-sudo

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

#enable docker

#USER root
#RUN usermod -aG docker buildagent

#USER buildagent
##################################

#install docker compose

USER root

RUN mkdir -p /usr/local/lib/docker/cli-plugins
RUN curl -SL https://github.com/docker/compose/releases/download/v2.16.0/docker-compose-linux-x86_64 -o /usr/local/lib/docker/cli-plugins/docker-compose
RUN chmod +x /usr/local/lib/docker/cli-plugins/docker-compose
#USER buildagent
##################################