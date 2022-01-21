FROM adoptopenjdk/openjdk16:jre-16.0.1_9-alpine
ARG APP_PORT=9003
ENV APP_PORT=${APP_PORT}
WORKDIR /app
COPY maven/ ./
RUN adduser -D -g '' -h /app -u 10001 app \
	&& chown -R app:app /app
USER app
EXPOSE ${APP_PORT}
CMD ["/bin/sh", "./bin/startup.sh"]
