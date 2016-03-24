(function() {
    //提问和回答框切换
    var tabs = [].slice.call(document.querySelectorAll('#myTab .tab'));
    var tabpanes = [].slice.call(document.querySelectorAll('#myTab .tabpane'));

    tabs.forEach(function(tab) {
        tab.addEventListener('click', function(e) {
            var target = this;
            tabs.forEach(function(tab, i) {
                if (target === tab) {
                    tab.classList.add('active');
                    tabpanes[i].classList.add('active');
                } else {
                    tab.classList.remove('active');
                    tabpanes[i].classList.remove('active');
                }
            });
        }, false);
    });
})();
var uid,follow;
$(function(){
    //关注按钮
    uid=getUrlParam('uid');
    follow = window.sessionStorage.getItem("uid");
    console.log("uid="+uid);
    console.log("follow="+follow);
    if (follow==uid){
        location.href = "edit.html?uid="+uid+"";
    }
    $.ajax({
        type:'POST',
        url:'/FollowCheck',
        data:{follower_uid:uid,followee_uid:follow},
        dataType:"json",
        success:function(data){
            if (data.status=="true") {
                $("#follow").html("取消关注");
                $("#follow").css("background-color", "red");
            } else{
                $("#follow").html("关注");
                $("#follow").css("background-color", "#1575d5");
            }
        },
        error:function() {
            console.log("服务器无法连接");
        }
    });
    //个人（其他人的）信息获取
    $.ajax({
        type:'POST',
        url:'/Profile',
        data:{uid:uid},
        dataType:"json",
        success:function(data){
            for (var i=0;i<data.questions.length;i++){
                $("#tabQus").append(
                "<p><a href='question.html?qid="+data.questions[i].qid+"' class='link'>"+data.questions[i].question+"</a></p>"
                );
            }
            console.log(data.answers.length);
            for (var i=0;i<data.answers.length;i++){
                $("#tabAns").append(
                "<div class='ans-wrapper'>"+
                    "<p><a href='question.html?qid="+data.answers[i].qid+"' class='link emphasis'>"+data.answers[i].question+"</a></p>"+
                "<div class='answer'>"+data.answers[i].answer+"</div>"+
                "<p class='meta'>"+data.answers[i].time+"</p>"+
                "</div>"
                );
            }
        },
        error:function() {
            console.log("服务器无法连接");
        }
    });

});
//取消关注 和 关注
$("#follow").click(function(){
    uid=getUrlParam('uid');
    follow = window.sessionStorage.getItem("uid");
    var it=$(this);
    if ($(this).html()=="取消关注"){
        $.ajax({
            type:'POST',
            url:'/Unfollow',
            data:{follower_uid:uid,followee_uid:follow},
            dataType:"json",
            success:function(data){
                if (data.status=="true"){
                    it.html("关注");
                    it.css("background-color","#1575d5");
                }
            },
            error:function() {
                console.log("服务器无法连接");
            }
        });

    } else{
        $.ajax({
            type:'POST',
            url:'/Follow',
            data:{follower_uid:uid,followee_uid:follow},
            dataType:"json",
            success:function(data){
                if (data.status=="true"){
                    it.html("取消关注");
                    it.css("background-color","red");
                }
            },
            error:function() {
                console.log("服务器无法连接");
            }
        });
    }
});
//发私信
$("#message").click(function(){
    if ($(".message-index").css("display")=="none"){
        $(".message-index").css("display","block");
        uid=getUrlParam('uid');
        $.ajax({
            type:'POST',
            url:'/GetInfo',
            data:{uid:uid},
            dataType:"json",
            success:function(data){
                console.log(data.username);
                $(".message-name").html(data.username);
            },
            error:function() {
                console.log("服务器无法连接");
            }
        });
        $(".message-name").html($(".form-text-div").html());
    } else{
        $(".message-index").css("display","none");
    }
});
$("#message-quit").click(function(){
    $(".message-index").css("display","none");
});
$("#message-send").click(function(){
    uid=getUrlParam('uid');
    follow = window.sessionStorage.getItem("uid");
    $.ajax({
        type:'POST',
        url:'/SendMessage',
        data:{
            sender_uid:follow,
            receiver_uid:uid,
            content:$(".message-texarea").val()
        },
        dataType:"json",
        success:function(data){
            $(".message-index").css("display","none");
            alert("发送成功");
        },
        error:function() {
            console.log("服务器无法连接");
        }
    });
});

