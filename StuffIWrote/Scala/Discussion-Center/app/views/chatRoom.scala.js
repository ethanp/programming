 @(username: String)(implicit request: RequestHeader)

$(function(){
    /* access username with "@username" */
    var ws = new WebSocket("@routes.Application.chatSocket(username).webSocketURL()");
    ws.onmessage = function (event) {
        var d = JSON.parse(event.data);
        if (!!d.username)
            $("#incoming").text(d.username);
        else $("#incoming").text("no username sent");
        ws.send(JSON.stringify(
            {
                text: "something"
            }
        ));
    }

});
