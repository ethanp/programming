 @(username: String)(implicit request: RequestHeader)

$(function(){
    /* access username with "@username" */
    var ws = new WebSocket("@routes.Application.chatSocket(username).webSocketURL()");
    ws.onmessage = function (event) {
        var d = event.data;
        d = JSON.parse(d);
        $("#incoming").text(d.username);
        ws.send(JSON.stringify(
            {text: "something"}
        ))
    }

});
