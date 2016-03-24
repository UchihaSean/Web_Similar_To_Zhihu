/**
 * Created by zhangqi on 15/6/22.
 */
var uid;
$(".zm-pm-item-main").css("cursor","pointer");
$(function(){
    uid = window.sessionStorage.getItem("uid");
    //拉取私信
    $.ajax({
        type:'POST',
        url:'/ViewMessage',
        data:{uid:uid},
        dataType:"json",
        success:function(data){
            for(var i=0 ;i<data.length; i++){
                var portrait="img/3.jpg";
                if (data[i].sender_portrait!=-1)
                portrait="/image/"+data[i].sender_portrait+"";
                $("#mail-content").append(
                    "<div class='zm-pm-item' >"+
                    "<a  class='zm-item-link-avatar50'>"+
                    "<img class='zm-item-img-avatar50' src='"+portrait+"'>"+
                    "</a>"+
                    "<a href='profile.html?uid="+data[i].sender_uid+"'>"+data[i].sender_username+"</a>  发给 "+
                "<a href='profile.html?uid="+data[i].receiver_uid+"'>"+data[i].receiver_username+"</a>"+
                "<div class='zm-pm-item-main color"+i+"' name='"+data[i].mid+"'>"+data[i].content+
            "</div>"+ "<div class='zm-pm-item-meta'>"+ "<div class='zm-pm-item-date'>"+
                    data[i].time+
                "</div>"+
                "</div>"+
                "</div>"
                );
                console.log("i="+data[i].unread);
                console.log("receiver_uid="+data[i].receiver_uid+" uid="+uid);
                //根据是否为 未读且 收信人为自己标记
                if (data[i].unread=="1" && data[i].receiver_uid==uid){
                    $(".color"+i+"").css("color","red");
                } else {
                    $(".color"+i+"").css("color","black");
                }
            }
        },
        error:function() {
            console.log("服务器无法连接");
        }
    });
});

//私信阅读
$(document).on("click",".zm-pm-item-main",function(){
    uid = window.sessionStorage.getItem("uid");
    var it=$(this);
    console.log(it.css("color"));
    if (it.css("color")=="rgb(255, 0, 0)"){
        console.log(it.css("color"));
        $.ajax({
            type:'POST',
            url:'/ReadMessage',
            data:{
                mid:it.attr("name")
            },
            dataType:"json",
            success:function(data){
                if (data.status=="true"){
                    it.css("color","black");
                    location.reload();
                }
            },
            error:function() {
                console.log("服务器无法连接");
            }
        });
    }
});
