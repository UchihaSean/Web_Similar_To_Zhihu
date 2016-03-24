(function() {
    var signup = document.getElementById('form-signup'),
        signin = document.getElementById('form-signin');

    switchForms();
    checkSignup();
    checkSignin();

    // switch forms
    function switchForms() {
        var label = document.getElementById('form-label');
        label.addEventListener('click', function(e) {
            var signupCls = signup.classList,
                signinCls = signin.classList;
            if (signupCls.contains('hidden')) {
                signupCls.remove('hidden');
                signinCls.add('hidden');
                label.innerHTML = '登陆';
            } else {
                signinCls.remove('hidden');
                signupCls.add('hidden');
                label.innerHTML = '注册';
            }
        }, false);
    }

    // check signup form
    function checkSignup() {
        var nameIpt = signup['username'],
            emailIpt = signup['email'],
            pwdIpt = signup['password'],
            errArea = document.getElementById('signup-error'),
            submitBtn = document.getElementById('signup-submit');

        submitBtn.addEventListener('click', function(e) {
            var err = [];

            var name = nameIpt.value.trim();
            if (!name) {
                err.push('用户名不得为空');
            } else if (name.length < 2 || name.length > 16) {
                err.push('用户名长度为2～16字符');
            }

            var email = emailIpt.value.trim();
            if (!email) {
                err.push('邮箱地址不得为空');
            } else if (!/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(email)) {
                err.push('请输入有效的邮箱地址');
            }

            var pwd = pwdIpt.value;
            if (!pwd) {
                err.push('密码不得为空');
            } else if (pwd.length < 6 || pwd.length > 16) {
                err.push('密码长度为6～16字符');
            } else if (/^[0-9]*$/.test(pwd)) {
                err.push('密码不得为纯数字');
            }

            if (err.length) {
                errArea.innerHTML = err.join('<br>');
            } else {
                $.ajax({
                    type:'POST',
                    url:'/Register',
                    data:{'username':name,'email':email,'password':pwd},
                    dataType:"json",
                    success:function(data) {
                        //errArea.innerHTML="成功连接";
                        if (data.status=='true') {
                            sessionStorage.setItem("uid", data.uid);
                            window.location.href = './find.html';
                        } else {
                            errArea.innerHTML = "用户名或邮箱已存在";
                        }
                    },
                    error:function(){
                        errArea.innerHTML="服务器连接不上";
                    }
                });
            }
        }, false);
    }

    // check signin form
    function checkSignin() {
        var emailIpt = signin['email'],
            pwdIpt = signin['password'],
            errArea = document.getElementById('signin-error'),
            submitBtn = document.getElementById('signin-submit');

        submitBtn.addEventListener('click', function(e) {
            var err = [];

            var email = emailIpt.value.trim();
            var email = emailIpt.value.trim();
            if (!email) {
                err.push('邮箱地址不得为空');
            } else if (!/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(email)) {
                err.push('请输入有效的邮箱地址');
            }

            var pwd = pwdIpt.value;
            if (!pwd) {
                err.push('密码不得为空');
            }

            if (err.length) {
                errArea.innerHTML = err.join('<br>');
            } else {
                $.ajax({
                    type:'POST',
                    url:'/Login',
                    data:{'email':email,'password':pwd},
                    dataType:"json",
                    success:function(data){
                        if (data.status=="true"){
                            //errArea.innerHTML="成功连接";
                            sessionStorage.setItem("uid", data.uid);
                            window.location.href='./homepage.html';
                        } else {
                            errArea.innerHTML="用户名或密码错误";
                        }
                    },
                    error:function() {
                        errArea.innerHTML = "服务器连接不上";
                    }
                });
            }
        }, false);
    }
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
                    $(".pop-ques").append(
                     "<p><a class='link' href='question.html?qid="+data[i].qid+"'>"+data[i].title+"</a></p>"
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
            console.log("len="+len);
            for (var i=0;i<len;i++) {
                var img = "img/3.jpg";
                if (data[i].portrait != -1)
                    img = "/image/" + data[i].portrait;
                $(".pop-user").append(
                    "<a href='profile.html?uid=" + data[i].uid + "'><img src='" + img + "' alt='user-head'></a>"
                );
            }
        },
        error:function() {
            console.log("服务器无法连接");
        }
    });
})();
