/**
 * Created by Jly_wave on 6/22/15.
 */
var uid;
$(function(){
    uid=getUrlParam('uid');
    //个人信息拉取 关注 被关注数 头像
    $.ajax({
        type:'POST',
        url:'/RecentFollow',
        data:{uid:uid},
        dataType:"json",
        success:function(data){
            var follower=data.follower;
            var followee=data.followee;
            $("#follower_num").html(follower.length);
            for (var i=0;i<follower.length;i++){
                var portrait="img/3.jpg";
                if (follower[i].portrait!=-1)
                portrait="/image/"+follower[i].portrait+"";
                $(".profile-follower").append(
                    "<a href='profile.html?uid="+follower[i].uid+"'><img src='"+portrait+"' alt=''></a> "
                )
            }
            $("#followee_num").html(followee.length);
            for (var i=0;i<followee.length;i++){
                var portrait="img/3.jpg";
                if (followee[i].portrait!=-1)
                    portrait="/image/"+followee[i].portrait+"";
                $(".profile-followee").append(
                    "<a href='profile.html?uid="+followee[i].uid+"'><img src='"+portrait+"' alt=''></a> "
                )
            }
        },
        error:function() {
            console.log("服务器无法连接");
        }
    });
    //头像
    $.ajax({
        type:'POST',
        url:'/GetInfo',
        data:{uid:uid},
        dataType:"json",
        success:function(data){
            var portrait="img/3.jpg";
            if (data.portrait!=-1)
                portrait="/image/"+data.portrait+"";
            $(".profile-img img").attr("src",portrait);
        },
        error:function() {
            console.log("服务器无法连接");
        }
    });
});
