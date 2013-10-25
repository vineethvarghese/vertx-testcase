def server = vertx
    .createHttpServer()
    .setAcceptBacklog(5000)
    .setTCPKeepAlive(false)
    .setReceiveBufferSize(4*1024)
    .setSendBufferSize(300)
    .setTCPNoDelay(true)
server.requestHandler {request ->
  request.bodyHandler { body ->
    container.logger.info "The total body received was ${body.length()}) bytes"
  }
  container.logger.info "Sending Ping"
  request.response.end("Ping")
}
server.listen(8080)