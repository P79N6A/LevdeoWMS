package com.tre.jdevtemplateboot.common.util;

import com.sun.jna.Library;
import com.sun.jna.Native;
import org.springframework.beans.factory.annotation.Value;

public interface ArgoxPrinterUtils extends Library {
    ArgoxPrinterUtils instance = (ArgoxPrinterUtils) Native.load("Winpplb.dll", ArgoxPrinterUtils.class);

    //取得或秀出此函數庫版本資訊(获取版本号)
    public int B_Get_DLL_Version(int nShowMessage);

    //設定開始列印點(设置原点)
    public int B_Set_Originpoint(int hor, int ver);

    //删除存储于RAM或Flash中的图形。
    public int B_Del_Pcx(String pcxname);

    public int B_CreatePrn(int selection, String filename);

    //印出一行文字和加上跳号功能。
    public int B_Prn_Text(int x, int y, int ori, int font, int hor_factor, int ver_factor, char mode, String data);

    //開啟使用中文點矩陣字型檔案 (16*15 和 24*24)。
    public int B_Open_ChineseFont(String path);
    //印出一行文字，使用中文點矩陣字型檔案 (16*15 或 24*24)。
    public int B_Prn_Text_Chinese(int x, int y, int fonttype, String id_name, String data);

    //印出一行 True Type Font 文字。
    public int B_Prn_Text_TrueType(int x, int y, int FSize, String FType, int Fspin, int FWeight, int FItalic,
        int FUnline, int FStrikeOut, String id_name, String data);

    //印出一行 True Type Font 文字。
    public int B_Prn_Text_TrueType_Uni(int x, int y, int FSize, String FType, int Fspin, int FWeight, int FItalic,
        int FUnline, int FStrikeOut, String id_name, String data, int format);

    //印出一個 Maxi Code 2D Barcode。
    public int B_Bar2d_Maxi(int x, int y, int cl, int cc, int pc, String data);

    //印出一個條碼和加上跳號功能。
    public int B_Prn_Barcode(int x, int y, int ori, String type, int n, int w, int height, char human, String data);

    public int B_Draw_Box(int x, int y, int thickness, int hor_dots, int ver_dots);

    //取得 USB Printer 的裝置名稱及資料長度。
    public int B_GetUSBBufferLen();
    public int B_EnumUSB(byte[] bt);

    //開啟 Printer 工作。
    public int B_CreateUSBPort(int nPort);

    //取得 USB Printer的裝置名稱和裝置路徑。
    int B_GetUSBDeviceInfo(int nPort, byte[] pDeviceName, int pDeviceNameLen, byte[] pDevicePath, int pDevicePathLen);

    public int B_Print_Out(int labset);

    public void B_ClosePrn();

    public class ArgoxPrinter{
        public boolean doPrint(String vin, String name, String barcodePrinterIP){
            //初始化打印机
            ArgoxPrinterUtils doprint = ArgoxPrinterUtils.instance;
            int ret = doprint.B_CreatePrn(13, barcodePrinterIP);

            if (ret == 0){
                //打印机清空缓存
                doprint.B_Del_Pcx("*");
                //设置原点(0, 0)
                doprint.B_Set_Originpoint(0, 0);
                //打印：名称
                doprint.B_Prn_Text(570, 40, 0, 1, 2, 2, 'N', name);
                //打印条形码
                doprint.B_Prn_Barcode(510, 80, 0, "K", 1, 6, 60, 'B', vin);

                //打印一份
                doprint.B_Print_Out(1);
                //关闭打印机
                doprint.B_ClosePrn();

                return true;
            }else{
                return false;
            }
        }
    }
}
