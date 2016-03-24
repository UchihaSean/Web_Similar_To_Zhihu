/**
 * Created by Jly_wave on 6/20/15.
 */
var qus;
var ans=new Array();
var page= 1;
var qid=getUrlParam('qid');
var aid;
var numInPage=0;
$(function(){
    $(".more").css("cursor","pointer");
    addmore(1);
});
//加载问题和回答 参数1为需要添加问题 0为只需要加载回答
$(".more").click(function(){
    addmore(0);
});

//加载问题和回答
function addmore(needQus){
    $.ajax({
        type: 'POST',
        url: '/GetQuestion',
        data: {"qid":qid, "page": page},
        dataType: "json",
        success: function (data) {
            qus=data.question;
            ans=data.answers;
            if (needQus==1) {
                $("#left").append(
                    "<div class='question'>" +
                    "<h3>" + qus.title + "</h3>" +
                    "<p class='meta'>" +
                    "<a href='profile.html?uid="+qus.uid+"' class='link'>" + qus.username + "</a> 提问于 <span>" + "2015-04-06 11:20" + "</span>" +
                    "</p>" +
                    "</div>"
                );
            }
            //分页面加载  one page=five answers
            for (var i = numInPage; i < ans.length; i++) {
                var image="";
                if (ans[i].image!=-1){
                    image= " <img src='/image/"+ans[i].image+"'>";
                }
                var portrait="img/3.jpg";
                if (ans[i].portrait!=-1){
                    portrait="/image/"+ans[i].portrait;
                }
                $("#left").append(
                    "<div class='answer-wrapper'>" +
                    "<div class='user clearfix'>" +
                    "<a href='profile.html?uid="+ans[i].uid+"''><img src='"+portrait+"' alt=''></a>" +
                    "<p>" +
                    "<a href='profile.html?uid="+ans[i].uid+"' class='name'>" + ans[i].username + "</a>" +
                    "<span class='description'>" + ans[i].introduction + "</span>" +
                    "</p>" +
                    "</div>" +
                    "<div class='answer'>" +
                    "<p>" + ans[i].text + "</p>" +image+
                    "</div>" +
                    "<div class='meta' name='"+ans[i].aid+"'>" +
                    "<span class='comments'>" + '评论('+ans[i].comment_num+')' + "</span>" +
                    "<span class='time'>" + ans[i].time + "</span>" +
                    "</div>" +
                    "<div class='comments-wrapper'>" +

                    "</div>"+
                    "</div>"
                );
            }
            if (ans.length<5) {
                numInPage=ans.length;
                $(".more").html("加载完毕");
            } else{
                page++;
                $(".more").html("加载更多");
            }
        },
        error: function () {
            console.log("服务器无法连接");
        }
    });
}
