var id=new Array();
var qus=new Array();
var ans=new Array();
var time=new Array();
var user=new Array();
var qid=new Array();
var uid ;
$(function(){
    uid = window.sessionStorage.getItem("uid");
    //获取关注者的回答和提问
    $.ajax({
        type:'POST',
        url:"/Homepage",
        data:{'uid':uid},
        dataType:"json",
        success:function(data){
            for (var i=0;i<data.length;i++){
                id[i]=data[i].uid;
                qus[i]=data[i].question;
                ans[i]=data[i].answer;
                time[i]=data[i].time;
                user[i]=data[i].username;
                qid[i]=data[i].qid;
                console.log(id[i]+" "+qus[i]+" "+ans[i]+" "+time[i]+" "+user[i]+" "+qid[i]);
                //根据是否有回答添加
                if (ans[i]==""){
                $("#left").append(
                    "<div class='meta list'>"+
                    "<div class='qus'>问题:</div><a href='question.html?qid="+qid[i]+"' class='qusLink'>"+qus[i]+"</a>"+
                    "<a href='profile.html?uid="+id[i]+"' class='qusUser'>"+user[i]+"</a><span>提问于 "+time[i]+"</span>"+
                    "</div>"
                );
                }else {
                    $("#left").append(
                        "<div class='meta list'>"+
                        "<div class='qus'>问题:</div><a href='question.html?qid="+qid[i]+"' class='qusLink'>"+qus[i]+"</a>"+
                        "<div class='qus'>回答:</div><p class='qusAns'>"+ans[i]+"</p>"+
                        "<a href='profile.html?uid="+id[i]+"' class='qusUser'>"+user[i]+"</a><span>回答于 "+time[i]+"</span>"+
                        "</div>"
                    );
                }
            }
        },
        error:function() {
            console.log("服务器无法连接");
        }
    });
});
