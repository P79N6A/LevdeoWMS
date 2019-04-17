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
                var ret = receiveMsg.data.split("|")[1];
                if(ret == 0){
                    
                }else{
                    
                }
            }
        }
    }, 

    //尝试向打印后台发送消息
    sendMessage: function(text, vin) {
        if(argoxPrint.ws.readyState!=1 || !argoxPrint.connPrinter){
            if(argoxPrint.ws.readyState!=1){
                toastr.error("连接本地打印服务失败");
            }
            if(!argoxPrint.connPrinter){
                toastr.error("连接打印机失败");
            }
            //播放失败音乐
            //$("#infomp3").attr("src", constant.projectURL+"/music/fail.mp3");
            return;
        }

        //打印文字
        // argoxPrint.ws.send("B_Prn_Barcode|430|80|0|E30|2|6|60|B|"+vin);
        argoxPrint.ws.send("B_Prn_Text_TrueType|550|40|30|宋体|1|600|0|0|0|printText|"+text);
        //打印条码
        argoxPrint.ws.send("B_Prn_Barcode|480|80|0|1|1|6|60|B|"+vin);
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