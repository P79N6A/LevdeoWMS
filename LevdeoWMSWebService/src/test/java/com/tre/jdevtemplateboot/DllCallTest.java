package com.tre.jdevtemplateboot;


import com.sun.jna.Library;
import com.sun.jna.Native;

interface JNATestDll extends Library {
    JNATestDll instanceDll = (JNATestDll) Native.load("E:\\LDWMS\\03_SOURCE\\LevdeoWMSWebService\\src\\main\\resources\\Winpplb.dll", JNATestDll.class);

    public int B_Get_DLL_Version(int nShowMessage);

    public int B_CreatePrn(int selection, String filename);

    public int B_Set_Originpoint(int hor, int ver);

    public int B_Del_Pcx(String pcxname);

    public int B_Draw_Box(int x, int y, int thickness, int hor_dots, int ver_dots);

    public int B_Print_Out(int labset);

    public void B_ClosePrn();
}
