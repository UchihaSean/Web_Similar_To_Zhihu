/**
 * Created by Jly_wave on 6/22/15.
 */
var uid;
$(".ask-index").css("z-index","3");
$(function(){
    uid = window.sessionStorage.getItem("uid");
    if (uid==null)
       location.href="index.html";
    //id 转为头像
    $.ajax({
        type:'POST',
        url:'/Uid2Img',
        data:{
            uid:uid
        },
        dataType:"json",
        success:function(data){
            console.log(data.portrait);
            if (data.portrait!=-1){
                $(".nav-profile a img").attr("src","/image/"+data.portrait+"");
            } else{
                $(".nav-profile a img").attr("src","img/3.jpg");
            }
            $(".nav-profile a span").html(data.username);
        },
        error:function() {
            console.log("服务器无法连接");
        }
    });

});
//提问框的显示
$(".nav-ask").click(function(){
    if ($(".ask-index").css("display")=="none"){
        $(".ask-index").css("display","block");
    } else{
        $(".ask-index").css("display","none");
    }
});
//提问框的退出
$("#ask-quit").click(function(){
    $(".ask-index").css("display","none");
});
//提问框的发送
$("#ask-send").click(function(){
    uid = window.sessionStorage.getItem("uid");
    $.ajax({
        type:'POST',
        url:'/Ask',
        data:{
            uid:uid,
            title:$(".ask-index textarea").val(),
            content:""
        },
        dataType:"json",
        success:function(data){
            if (data.status=="true"){
                //alert("发送成功");
                $(".ask-index").css("display","none");
                location.href="find.html";
            }
        },
        error:function() {
            console.log("服务器无法连接");
        }
    });
});
//搜索则跳转到搜索页面并添加参数key=搜索内容
$(".fa-search").click(function(){
    location.href = "search.html?key="+$(this).parent().prev("input").val();
});
//按钮click 跳转添加
$(".submenu").click(function(){
    if ($(this).find("i").hasClass("fa-user")) {
        $(this).find("a").attr("href","homepage.html");
    }
    if ($(this).find("i").hasClass("fa-envelope-o")) {
        $(this).find("a").attr("href","mail.html?uid="+uid+"");
    }
    if ($(this).find("i").hasClass("fa-cog")) {
        $(this).find("a").attr("href","edit.html?uid="+uid+"");
    }
    if ($(this).find("i").hasClass("fa-sign-out")) {
        $(this).find("a").attr("href","index.html");
    }
});
$(".menu a").click(function(){
    if ($(this).html()=="首页")
     $(this).attr("href","homepage.html");
    if ($(this).html()=="发现")
        $(this).attr("href","find.html");

})

//私信未读个数标记
$(".nav-profile").hover(function(){
    uid = window.sessionStorage.getItem("uid");
    var it=$(this).find(".fa-envelope-o").parent("a");
    console.log("uid="+uid);
    $.ajax({
        type:'POST',
        url:'/UnreadNum',
        data:{
            uid:uid
        },
        dataType:"json",
        success:function(data){
            console.log("message num="+data.num);
            if (data.num!=0){
                it.empty();
                it.append(
                "<i class='fa fa-envelope-o'></i>私信 ("+data.num+")"
                );
            }
        },
        error:function() {
            console.log("服务器无法连接");
        }
    });
});
