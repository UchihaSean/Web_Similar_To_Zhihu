/**
 * Created by Jly_wave on 6/21/15.
 */
/**
 * Created by Jly_wave on 6/21/15.
 */
var uid ;
$(function(){
    uid = window.sessionStorage.getItem("uid");
    //获取个人信息
    $.ajax({
        type:'POST',
        url:'/GetInfo',
        data:{uid:uid},
        dataType:"json",
        success:function(data){
            img="img/3.jpg";
            if (data.portrait!=-1)
             img="/image/"+data.portrait+"";
            $("input[name='username']").val(data.username);
            $("input[name='password']").val(data.password);
            $("input[name='signature']").val(data.introduction);
            $(".profile-avatar-module img").attr("src",img);
        },
        error:function() {
            console.log("服务器无法连接");
        }
    });
});
var image=-1;
//保存个人信息按钮
$("#savesetting").click(function(){
    uid = window.sessionStorage.getItem("uid");
    console.log(image);
    console.log($("input[name='username']").val());
    $.ajax({
        type:'POST',
        url:'/ChangeInfo',
        data:{
            uid:uid,
            password: $("input[name='password']").val(),
            introduction:$("input[name='signature']").val(),
            image:image
        },
        dataType:"json",
        success:function(data){
            alert("设置成功");
            location.reload();
        },
        error:function() {
            console.log("服务器无法连接");
        }
    });
});
//上传头像
$("#uploadButton").click(function(){
    $("#uploadFile").trigger("click");
});
function selectImage(file){
    if(!file.files || !file.files[0]){
        return;
    }
    var reader = new FileReader();
    reader.onload = function(evt){
        document.getElementById('uploadImage').src = evt.target.result;
        image = evt.target.result;
    }
    reader.readAsDataURL(file.files[0]);
}