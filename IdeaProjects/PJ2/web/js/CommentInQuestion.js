//评论
$(document).on("click",".answer-wrapper>.meta",comment);
var uid,aid;
function comment(){
     aid=$(this).attr("name");
    console.log(aid);
     //评论框显示
     it=$(this).next(".comments-wrapper");
     if (it.css("display")=="block"){
         it.css("display","none");
         //清空评论
         it.empty();
     } else {
         //获取评论
         $.ajax({
             type: 'POST',
             url: '/GetComment',
             data: {"aid": aid},
             dataType: 'json',
             success: function (data) {
                 it.css("display","block");
                 for (var i=0;i<data.length;i++){
                     var portrait="img/1.jpg";
                     if (data[i].portrait!=-1)
                          portrait="/image/"+data[i].portrait;
                     //添加评论
                     it.append(
                         "<div class='comment-item clearfix'>"+
                         "<a href='profile.html?uid="+data[i].uid+"'><img src='"+portrait+"' alt=''></a>"+
                         "<div class='content'>"+
                         "<p class='name'><a href='profile.html?uid="+data[i].uid+"'>"+data[i].username+"</a></p>"+
                         "<p class='comment'>"+data[i].content+"</p>"+
                         "<p class='meta'>"+data[i].time+"</p>"+
                         "</div>"+
                         "</div>"
                     );
                 }
                 //添加评论框
                 it.append(
                     " <div class='comment-form'>" +
                     "<form action=''> <textarea name='comment' value=''></textarea>" +
                     "<div class='btn-wrapper'><div>发表评论</div></div>" +
                     "</form>" +
                     "</div>"
                 );
             },
             error: function () {
                 it.css("display","block");
                 console.log("服务器无法连接");
             }
         });
     }
 }

//评论发送
$(document).on("click",".btn-wrapper div",function(){
    uid = window.sessionStorage.getItem("uid");
    console.log(aid);
    var content=$(this).parent("div").prev("textarea").val();
    if (content==""){
        alert("请输入评论内容");
    } else
    $.ajax({
        type: 'POST',
        url: '/SetComment',
        data: {
            "aid": aid,
            "uid": uid,
            "content":content
        },
        dataType: 'json',
        success: function (data) {
            if (data.status=="true"){
                location.reload();
            }
        },
        error: function () {
            console.log("服务器无法连接");
        }
    });
});