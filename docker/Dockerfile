FROM java:8

ENV PORT 9200
ENV redis_host 192.168.99.100


ADD run.sh /opt/
ADD carePrices.jar /opt/

ENTRYPOINT ["sh","/opt/run.sh"]



