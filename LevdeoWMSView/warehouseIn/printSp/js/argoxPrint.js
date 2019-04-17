var argoxPrint = {
    ws: null,
    wsUrl: "ws://localhost:2012",
    connPrinter: false,
    //链接
    connection: function() {
        if ("WebSocket" in window) {
            argoxPrint.ws = new WebSocket(argoxPrint.wsUrl);
        } else if ("MozWebSocket" in window) {
            argoxPrint.ws = new MozWebSocket(argoxPrint.wsUrl);
        } else {
            toastr.warning("当前浏览器不支持websocket");
        }

        //注册各类回调
        argoxPrint.ws.onopen = function () {
            // toastr.info("连接后台打印服务成功");
            // 连接USB打印机
            if(constant.printerType == "USB"){
                argoxPrint.ws.send("B_EnumUSB");
                argoxPrint.ws.send("B_CreateUSBPort|1");
            }
            // 连接IP打印机
            else if(constant.printerType == "IP"){
                argoxPrint.ws.send("B_CreatePrn|13|"+constant.printerIP);
            }
        }
        argoxPrint.ws.onclose = function () {
            toastr.warning("与本地打印服务断开连接");
        }
        argoxPrint.ws.onerror = function () {
            toastr.error("连接本地打印服务失败");
        }
        argoxPrint.ws.onmessage = function (receiveMsg) {
            var func = receiveMsg.data.split("|")[0];
            if(func == "B_GetPrinterStatus"){
                // 0:等待列印!
                // 1:打印命令错误！
                // 2:USB端口开启失败
                // 3:条码格式错误！
                // 4:内存溢出！
                //// 4:碳带用完或安装错误！
                // 6:串口通信异常！
                // 7:纸张/碳带用完！
                // 9:未取得返回值！
                // 12:打印机暂停！
                // 50:打印机忙碌！
                var ret = receiveMsg.data.split("|")[1];
            } else if(func == "B_CreatePrn" || func == "B_CreateUSBPort"){
                var ret = receiveMsg.data.split("|")[1];
                if(ret == 0){
                    // toastr.info("连接打印机成功");
                    argoxPrint.connPrinter = true;
                }else{
                    toastr.error("连接打印机失败");
                    argoxPrint.connPrinter = false;
                }
            } else if(func == "B_Print_Out"){
                var thistr = $(params.wsinput).parents("tr");
                var trIndex = $(params.wsinput).parents("tr").index();
                var trnums = $("#mainTable tbody tr").length;

                var ret = receiveMsg.data.split("|")[1];
                if(ret == 0){
                    //播放打印成功音乐
                    $("#infomp3").attr("src", constant.projectURL+"/music/success.mp3");

                    //如果是最后一行，添加一行
                    if (trIndex == trnums - 1) {
                        tabFillData.items.push({});
                    }
                    tabFillData.$forceUpdate();

                    //入库交接单只读
                    $(params.wsinput).attr("readonly", true);
                    //行添加成功标志class
                    thistr.addClass("successtr");
                    thistr.removeClass("failed");
                    //解绑onblur事件
                    $(params.wsinput).removeAttr("onblur");

                    setTimeout(
                        function () {
                            if ($("#mainTable tbody")[0].scrollHeight <= $("#mainTable tbody")[0].clientHeight) {
                                //没有滚动条
                                $("#mainTable thead").html($("#tbhead1").html());
                            } else {
                                //如果有滚动条
                                $("#mainTable thead").html($("#tbhead2").html());
                            }
                            //光标移到下一行
                            $("input[name='wsTransition']:eq(" + (trIndex + 1) + ")").focus();
                        }, 100
                    );
                }else{
                    //行删除成功标志class
                    thistr.removeClass("successtr");
                    thistr.addClass("failed");
                    //光标停留在当前行
                    $(params.wsinput).focus();
                    //可以重新打印
                    params.wsinitVal = "";
                }
            }
        }
    }, 

    //尝试向打印后台发送消息
    sendMessage: function(text, vin) {
        if(argoxPrint.ws.readyState!=1 || !argoxPrint.connPrinter){
            var thistr = $(params.wsinput).parents("tr");
            if(argoxPrint.ws.readyState!=1){
                toastr.error("连接本地打印服务失败");
            }
            if(!argoxPrint.connPrinter){
                toastr.error("连接打印机失败");
            }
            //行删除成功标志class
            thistr.removeClass("successtr");
            thistr.addClass("failed");
            //播放失败音乐
            $("#infomp3").attr("src", constant.projectURL+"/music/fail.mp3");
            return;
        }

        //打印文字
        // argoxPrint.ws.send("B_Prn_Text|550|40|0|1|2|2|N|"+text);
        argoxPrint.ws.send("B_Prn_Text_TrueType|500|40|30|宋体|1|500|0|0|0|printText|"+text);
        //打印条码
        argoxPrint.ws.send("B_Prn_Barcode|450|80|0|1|1|6|60|B|"+vin);
        argoxPrint.ws.send("B_Print_Out|1");
        // argoxPrint.ws.send("B_ClosePrn");
    },

    //获取打印机状态
    getStatus: function() {
        if(argoxPrint.ws.readyState != 1){
            toastr.error("连接本地打印服务失败");
            return;
        }
        
        argoxPrint.ws.send("B_GetPrinterStatus");
        // argoxPrint.ws.send("B_ClosePrn");
    }
}