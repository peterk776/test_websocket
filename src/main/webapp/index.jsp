<html>
<body>
<h2>Test Websockets!</h2>

<script>
    function testWebsocket(url, userId, tenant, sessionId, xsrf) {

        var webSocket = new WebSocket(url);
        webSocket.onopen = function(event) {
            var params = {
                userId: userId,
                tenant: tenant,
                sessionId: sessionId,
                xsrf : xsrf,
                date: Date.now()
            };

            var jsonParams = JSON.stringify(params);
            webSocket.send(jsonParams);
            console.log("ws connection open")
        };

        webSocket.onerror = function (errorEvent) {
            console.log("ws error: " + errorEvent);
        };

        webSocket.onclose = function (closeEvent) {
            console.log("ws close: " + closeEvent.code + ", reason " + closeEvent.reason);
        };
    }

    testWebsocket('ws://localhost:8080/websock/listener', 'RFB', null, 'sessionid', 'xsrf')
</script>


</body>
</html>
