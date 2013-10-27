def xml = """
<Soap:Envelope xmlns:Soap="http://schemas.xmlsoap.org/soap/envelope/">
   <Soap:Header/>
   <Soap:Body>
      <Response xmlns="http://www.digimon.com/response">
         <ResponseStatus>ACCEPTED</ResponseStatus>
      </Response>
   </Soap:Body>
</Soap:Envelope>
"""
def server = vertx
    .createHttpServer()
    .setAcceptBacklog(5000)
    .setTCPKeepAlive(false)
    .setReceiveBufferSize(6*1024)
    .setSendBufferSize(500)
    .setTCPNoDelay(true)
def logger = container.logger
server.requestHandler {request ->
  request.bodyHandler { body ->
    logger.info "The total body received was ${body.length()}) bytes"
  }
  logger.info "Sending Response XML"
  request.response.end(xml)
}
server.listen(8080)