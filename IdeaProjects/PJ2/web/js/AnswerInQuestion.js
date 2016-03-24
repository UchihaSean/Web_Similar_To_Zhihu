/**
 * Created by Jly_wave on 6/20/15.
 * Answer Question in Qusetion Page
 */


//    初始值
//    登陆id uid
//    问题id qid
//    图片   image
var image=-1;
var uid = window.sessionStorage.getItem("uid");
var qid=getUrlParam('qid');

//提交回答
$(".input-wrapper>.submit").click(function(){

    //获取回答框内容
    var content=$(".publish-form textarea").val();
    if (content==""){
        alert("请输入回答内容");
    } else
        $.ajax({
            type:'POST',
            url: '/Answer',
            data:{
                "qid":qid,
                "uid":uid,
                "content":content,
                "image":image
            },
            dataType:'json',
            success: function (data) {
                //页面加载
                if (data.status=="true"){
                    location.reload();
                }
            },
            error: function(){
                console.log("服务器无法连接");
            }
        });
});
//图片按钮click 转接
$(".fa-picture-o").click(function(){
    $("#image").trigger("click");
});
//上传图片
function selectImage(file){
    if(!file.files || !file.files[0]){
        return;
    }
    var reader = new FileReader();
    reader.onload = function(evt){
        document.getElementById('image').src = evt.target.result;
        image = evt.target.result;
    }
    reader.readAsDataURL(file.files[0]);
}