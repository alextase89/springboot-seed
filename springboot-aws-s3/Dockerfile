FROM openjdk:8-jre-alpine
ARG APP_PORT=9090
RUN apk -v --update add python py-pip groff less bash jq openssl && \
    pip install --upgrade awscli==1.16.133 && \
    apk -v --purge del py-pip && \
    rm -rf /var/cache/apk/*
WORKDIR /app
COPY maven/ ./app
RUN adduser -D -g '' -h /app -u 10001 app \
	&& chown -R app:app /app
USER app
ENV APP_PORT=${APP_PORT}
EXPOSE ${APP_PORT}
CMD ["/bin/bash", "app/bin/startup.sh"]
