 @(username: String)(implicit request: RequestHeader)

$(function(){
    /* access username with "@username" */

    var ws = new WebSocket("@routes.Application.chatSocket(username).webSocketURL()")
    ws.onmessage = function (event) {
        alert(event.data)
    }
});
