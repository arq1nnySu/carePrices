FROM beevelop/java

ENV PORT 9200
ENV log_key 52dfdbf5-a0b6-42af-9153-104f21bfa394
ENV fill_data false

ENV redis_host redis
ENV mongo_host mongo
ENV storage mysql
ENV mysql_url jdbc:mysql://mysql:3306/
ENV mysql_user careprices
ENV mysql_pwd careprices
ENV mysql_schema careprices


ADD run.sh /opt/
ADD carePrices.jar /opt/

ENTRYPOINT ["sh","/opt/run.sh"]



