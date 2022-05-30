$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");

	var title = $("#recipient-name").val();
	var content = $("#message-text").val();

	console.log(title);console.log(content);

    $.post(
        CONTEXT_PATH + "/discuss/add",
        {"title":title,"content":content},

        function(data){
        data=$.parseJSON(data);
        //在提示框中显示消息
        $("#hintBody").text(data.msg);
        //显示提示框
        $("#hintModal").modal("show");
        //两秒后提示框消失
        	setTimeout(function(){
        		$("#hintModal").modal("hide");
        		if(data.code==0){
        		window.location.reload();
        		}
        	}, 2000);
        }
    );

}