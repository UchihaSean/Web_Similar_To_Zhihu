/**
 * Created by Jly_wave on 6/23/15.
 */
var key;
//搜索
$(function() {
    key=getUrlParam("key");
    //获取问题 和 用户
    $.ajax({
        type: 'POST',
        url: '/Search',
        data: {
            keyword: key
        },
        dataType: "json",
        success: function (data) {
            //问题搜索
            for (var i = 0; i < data.question.length; i++) {
                $("#qusSearch").append(
                    "<div class='meta list'>" +
                    "<a href='question.html?qid=" + data.question[i].qid + "' class='qusLink'>" + data.question[i].title + "</a>" +
                    '</div>'
                );
            }
            //没有问题匹配
            console.log("question num=" + data.question.length);
            console.log("user num=" + data.user.length);
            if (data.question.length == 0) {
                $("#qusSearch").append(
                    "<div class='meta list'>没有问题匹配</div>"
                );
            }
            //用户搜索
            for (var i = 0; i < data.user.length; i++) {
                $("#userSearch").append(
                    "<div class='meta list'>" +
                    "<a href='profile.html?uid=" + data.user[i].uid + "' class='qusLink'>" + data.user[i].username + "</a>" +
                    '</div>'
                );
            }
            //没有用户匹配
            if (data.user.length == 0) {
                $("#userSearch").append(
                    "<div class='meta list'>没有用户匹配</div>"
                );
            }
        },
        error: function () {
            console.log("服务器无法连接");
        }
    });
});

