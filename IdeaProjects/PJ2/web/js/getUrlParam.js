/**
 * Created by Jly_wave on 6/22/15.
 */
//地址栏获取参数
function getUrlParam(name)
{
    var reg
        = new RegExp("(^|&)"+
        name +"=([^&]*)(&|$)");
    var r
        = window.location.search.substr(1).match(reg);
    if (r!=null) return unescape(r[2]); return null;
}