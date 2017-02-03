// 存放主要交互逻辑代码
// javascript模块化
var seckill = {
    //封装秒杀相关ajax的url
    URL: {
        now: function () {
            return '/seckill/time/now';
        },
        exposer: function (seckillId) {
            return '/seckill/' + seckillId + '/exposer';
        },
        execute: function (seckillId, md5) {
            return '/seckill/' + seckillId + '/' + md5 + '/execution';
        }
    },
    validatePhone: function (phone) {
        if (phone && phone.length == 11 && !isNaN(phone)) {
            return true;
        } else {
            return false;
        }
    },
    handerSecKill: function (seckillId, node) {
        //获取秒杀地址，控制显示逻辑，执行秒杀逻辑
        node.hide()
            .html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');//按钮
        $.post(seckill.URL.exposer(seckillId), {}, function (result) {
            if (result && result.success) {
                var exposer = result.data;
                if (exposer && exposer.exposed) {
                    //开启秒杀
                    //获取秒杀地址
                    var md5 = exposer.md5;
                    var killUrl = seckill.URL.execute(seckillId, md5);
                    console.log('killUrl:' + killUrl);
                    //绑定一次点击事件
                    $('#killBtn').one('click', function () {
                        //执行秒杀请求的操作
                        //1.先禁用按钮
                        $(this).addClass('disabled');
                        //2.发送秒杀请求
                        $.post(killUrl, {}, function (result) {
                            if (result && result.success) {
                                var killResult = result.data;
                                var state = killResult.state;
                                var stateInfo = killResult.stateInfo;
                                //3.显示秒杀结果
                                node.html('<span class="label label-success">' + stateInfo + '</span>');
                            }
                        });
                    });
                    node.show();
                } else {
                    //未开启秒杀
                    var now = result.now;
                    var start = result.start;
                    var end = result.end;
                    //重新计算计时逻辑
                    seckill.countdown(seckillId, now, start, end);
                }
            } else {
                Console.log('秒杀地址错误');
            }
        });
    },
    countdown: function (seckillId, nowTime, startTime, endTime) {
        var seckillBox = $('#seckill-box');
        if (nowTime > endTime) {
            seckillBox.html('秒杀已经结束');
        } else if (nowTime < startTime) {
            //秒杀未开启，计时事情绑定
            var killTime = new Date(startTime + 1000);
            seckillBox.countdown(killTime, function (event) {
                //时间格式
                var format = event.strftime('秒杀倒计时：%D天 %H时 %M分 %S秒');
                seckillBox.html(format);
            }).on('finish.countdown', function () {
                //获取秒杀地址，控制显示逻辑，执行秒杀
                seckill.handerSecKill(seckillId, seckillBox);
            });
        } else {
            seckill.handerSecKill(seckillId, seckillBox);
        }
    },
    //详情页秒杀逻辑
    detail: {
        //详情页初始化
        init: function (param) {
            //手机验证和登录，计时交互
            //规划我们的交互流程
            var killPhone = $.cookie('killPhone');

            //验证手机号
            if (!seckill.validatePhone(killPhone)) {
                //绑定Phone，控制输出
                var killPhoneModal = $("#killPhoneModal");
                killPhoneModal.modal({
                    show: true, //显示弹出层
                    backdrop: "static", //禁止关闭位置
                    keyboard: false //关闭键盘事件
                });
                $("#killPhoneBtn").click(function () {
                    var inputPhone = $("#killPhoneKey").val();
                    if (seckill.validatePhone(inputPhone)) {
                        //电话写入cookie
                        $.cookie('killPhone', inputPhone, {expires: 7, path: "/seckill"});
                        //刷新页面
                        window.location.reload();
                    } else {
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">电话号码错误！</label>').show(300);
                    }
                });
            };
            var seckillId = param['seckillId'];
            var startTime = param['startTime'];
            var endTime = param['endTime'];
            //已经登录
            $.get(seckill.URL.now(), function (result) {
                if (result && result['success']) {
                    var nowTime = result.data;
                    seckill.countdown(seckillId, nowTime, startTime, endTime);
                } else {
                    console.log('result:' + result);
                }
            });
        }
    }
}