FROM beevelop/java

ENV PORT 9200
ENV log_key 52dfdbf5-a0b6-42af-9153-104f21bfa394
ENV NEW_RELIC_LICENSE_KEY 4e1752d76b896584b362b30d4c8acf1f75a9bbb7
ENV NEW_RELIC_APP_NAME Careprice (Developmnent)

#NewRelic configuration
RUN echo 'deb http://apt.newrelic.com/debian/ newrelic non-free' |  tee /etc/apt/sources.list.d/newrelic.list
RUN wget -O- https://download.newrelic.com/548C16BF.gpg | apt-key add -
RUN apt-get update
RUN apt-get install newrelic-sysmond
RUN nrsysmond-config --set license_key=$NEW_RELIC_LICENSE_KEY
RUN /etc/init.d/newrelic-sysmond start
RUN groupadd -r docker
RUN usermod -a -G docker newrelic

ADD newrelic/newrelic.jar /opt/newrelic.jar
ADD newrelic/newrelic.yml /opt/newrelic.yml

ADD carePrices.jar /opt/
ADD run.sh /opt/run.sh

ENTRYPOINT ["sh", "/opt/run.sh"]



