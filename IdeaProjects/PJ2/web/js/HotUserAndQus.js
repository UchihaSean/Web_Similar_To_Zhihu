/**
 * Created by Jly_wave on 6/23/15.
 */
//热门问题获取
$.ajax({
    type:'POST',
    url:'/HotQus',
    data:{},
    dataType:"json",
    success:function(data){
        var len=5;
        if (data.length<len)
        len=data.length;
        for (var i=0;i<len;i++) {
            if (data[i].title!="")
            $("#hotQus ul").append(
                "<li class='rques'>"+
                "<a class='link' href='question.html?qid="+data[i].qid+"'>"+
                data[i].title+"</a></li>"
            );
        }
    },
    error:function() {
        console.log("服务器无法连接");
    }
});
//热门用户获取
$.ajax({
    type:'POST',
    url:'/HotUser',
    data:{},
    dataType:"json",
    success:function(data){
        var len=5;
        if (data.length<len)
            len=data.length;
        for (var i=0;i<len;i++) {
            var img="img/3.jpg";
            console.log(data[i].portrait);
            //是否有头像
            if (data[i].portrait!="-1")
                img="/image/"+data[i].portrait+"";
            console.log(img);
            if (data[i].title!="")
                $("#hotUser ul").append(
                "<li class='ruser clearfix'>"+
                "<a href='profile.html?uid="+data[i].uid+"'><img src='"+img+"' alt=''></a>"+
                "<div>"+
                "<a href='profile.html?uid="+data[i].uid+"' class='name'>"+data[i].username+"</a>"+
                "<p class='description'>"+data[i].introduction+"</p>"+
                "</div>"+
                "</li>"
                );
        }
    },
    error:function() {
        console.log("服务器无法连接");
    }
});