def xml = """
<Soap:Envelope xmlns:Soap='http://schemas.xmlsoap.org/soap/envelope/'>
  <Soap:Header/>
  <Soap:Body>
    <Document xsi:schemaLocation='urn:iso:std:iso:20022:tech:xsd:pacs.008.001.04' xmlns='urn:iso:std:iso:20022:tech:xsd:pacs.008.001.04' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>
      <FIToFICstmrCdtTrf>
        <GrpHdr>
          <MsgId>AAAA/121109-CCT/EUR/443</MsgId>
          <CreDtTm>2012-11-09T10:09:13</CreDtTm>
          <NbOfTxs>1</NbOfTxs>
          <SttlmInf>
            <SttlmMtd>INDA</SttlmMtd>
          </SttlmInf>
          <InstgAgt>
            <FinInstnId>
              <BICFI>AAAAGB2L</BICFI>
            </FinInstnId>
          </InstgAgt>
          <InstdAgt>
            <FinInstnId>
              <BICFI>BBBBIE2D</BICFI>
            </FinInstnId>
          </InstdAgt>
        </GrpHdr>
        <CdtTrfTxInf>
          <PmtId>
            <InstrId>AAAA/121109-CCT/EUR/443/1</InstrId>
            <EndToEndId>CROPS/SX-25T/2012-10-13</EndToEndId>
            <TxId>AAAA/121109-CCT/EUR/443/1</TxId>
          </PmtId>
          <IntrBkSttlmAmt Ccy='EUR'>750000</IntrBkSttlmAmt>
          <IntrBkSttlmDt>2012-11-09</IntrBkSttlmDt>
          <InstdAmt Ccy='EUR'>75000</InstdAmt>
          <ChrgBr>SHAR</ChrgBr>
          <UltmtDbtr>
            <Nm>Biogenetics - CROPS</Nm>
            <PstlAdr>
              <StrtNm>Growth Street</StrtNm>
              <BldgNb>17</BldgNb>
              <PstCd>G5 OTW</PstCd>
              <TwnNm>Glasgow</TwnNm>
              <Ctry>GB</Ctry>
            </PstlAdr>
          </UltmtDbtr>
          <Dbtr>
            <Nm>Biogenetics - HQ</Nm>
            <PstlAdr>
              <StrtNm>Corn Street</StrtNm>
              <BldgNb>13</BldgNb>
              <PstCd>W6 8DR</PstCd>
              <TwnNm>London</TwnNm>
              <Ctry>GB</Ctry>
            </PstlAdr>
          </Dbtr>
          <DbtrAcct>
            <Id>
              <Othr>
                <Id>46373892034012</Id>
              </Othr>
            </Id>
          </DbtrAcct>
          <DbtrAgt>
            <FinInstnId>
              <BICFI>AAAAGB2L</BICFI>
            </FinInstnId>
          </DbtrAgt>
          <CdtrAgt>
            <FinInstnId>
              <BICFI>CCCCIE2D</BICFI>
            </FinInstnId>
          </CdtrAgt>
          <Cdtr>
            <Nm>Seed Inc.</Nm>
            <PstlAdr>
              <StrtNm>Grain Lane</StrtNm>
              <BldgNb>27</BldgNb>
              <TwnNm>Dublin</TwnNm>
              <Ctry>IE</Ctry>
            </PstlAdr>
          </Cdtr>
          <CdtrAcct>
            <Id>
              <IBAN>IE29CCCC93115212345678</IBAN>
            </Id>
          </CdtrAcct>
          <Purp>
            <Cd>GDDS</Cd>
          </Purp>
          <RmtInf>
            <Strd>
              <RfrdDocInf>
                <Tp>
                  <CdOrPrtry>
                    <Cd>CINV</Cd>
                  </CdOrPrtry>
                </Tp>
                <Nb>SX-25T</Nb>
                <RltdDt>2012-10-13</RltdDt>
              </RfrdDocInf>
            </Strd>
          </RmtInf>
        </CdtTrfTxInf>
      </FIToFICstmrCdtTrf>
    </Document>
  </Soap:Body>
</Soap:Envelope>
"""

vertx.setPeriodic(100) {
  def client = vertx.createHttpClient(port: 8080, host: "ec2-54-206-8-215.ap-southeast-2.compute.amazonaws.com", keepAlive: false)
  try {
    client
        .setReuseAddress(true)
        .setSoLinger(0)
        .setTCPNoDelay(true)
        .setUsePooledBuffers(true)
        .setSendBufferSize(6*1024)
        .setReceiveBufferSize(500)
    client.exceptionHandler { e ->
      container.logger.error("Unable to connect", e)
    }

    def request = client.post("/xml/") { resp ->
      container.logger.info "Got a response: ${resp.statusCode}"
    }
    request.exceptionHandler { e ->
      container.logger.error("Unable to send request", e)
    }
    request.setTimeout(10000)
    request.end(xml)
  } finally {
    //client.close()
  }


}
