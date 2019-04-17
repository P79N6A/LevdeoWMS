package com.tre.jdevtemplateboot.common.util;

import org.xvolks.jnative.JNative;
import org.xvolks.jnative.Type;
import org.xvolks.jnative.exceptions.NativeException;

public class Demo {
    private static final String PRINTER_DLL = "Winpplb.dll";

    public void createPrn(Demo demo) throws Exception{
        JNative n= demo.getJNativeByFunction("B_CreatePrn");
        n.setParameter(0, Type.INT,String.valueOf(1));
        n.setParameter(1, Type.STRING, "USB001");
        n.invoke();//执行，获取返回值
    }
    public void createUSBPort(Demo demo) throws Exception{
        //	JNative n= demo.getJNativeByFunction("B_CreateUSBPort");
        //	 n.setParameter(0, Type.INT,String.valueOf(1));
        //	 n.invoke();
        int nUSBDataLen=0;
        byte[] pbuf;
        JNative n = demo.getJNativeByFunction("B_GetUSBBufferLen");
        n.setRetVal(Type.INT);
        n.invoke();
        nUSBDataLen = Integer.parseInt(n.getRetVal())+1;
        pbuf=new byte[nUSBDataLen];
        System.out.println(pbuf);
        n = demo.getJNativeByFunction("B_EnumUSB");
        n.setParameter(0, Type.STRING, pbuf);
        n.invoke();
        n = demo.getJNativeByFunction("B_CreatePort");
        n.setParameter(0, Type.INT, "1");
        n.invoke();
    }
    public void setOriginPoint(Demo demo) throws Exception{
        JNative n= demo.getJNativeByFunction("B_Set_Originpoint");
        n.setParameter(0, Type.INT,String.valueOf(0));
        n.setParameter(1, Type.INT,String.valueOf(0));
        n.invoke();
    }
    public void prnBarcode1(Demo demo) throws Exception{
        JNative n= demo.getJNativeByFunction("B_Prn_Barcode");
        n.setParameter(0, Type.INT, "26");
        n.setParameter(1, Type.INT, "8");
        n.setParameter(2, Type.INT, "0");
        n.setParameter(3, Type.STRING, "1");//条码类型
        n.setParameter(4, Type.INT, "2");//宽度1
        n.setParameter(5, Type.INT, "2");//宽度2
        n.setParameter(6, Type.INT, "72");//高
        char human='B';
        n.setParameter(7, human);
        n.setParameter(8, Type.STRING, "12356");//条码接入口
        n.invoke();
    }
    public void prnBarcode2(Demo demo) throws Exception{
        JNative n= demo.getJNativeByFunction("B_Prn_Barcode");
        n.setParameter(0, Type.INT, "298");
        n.setParameter(1, Type.INT, "8");
        n.setParameter(2, Type.INT, "0");
        n.setParameter(3, Type.STRING, "1");//条码类型
        n.setParameter(4, Type.INT, "2");//宽度1
        n.setParameter(5, Type.INT, "2");//宽度2
        n.setParameter(6, Type.INT, "72");//高
        n.setParameter(7, 'B');
        n.setParameter(8, Type.STRING, "12356");//条码接入口
        n.invoke();
    }
    public void prnBarcode3(Demo demo) throws Exception{
        JNative n= demo.getJNativeByFunction("B_Prn_Barcode");
        n.setParameter(0, Type.INT, "570");
        n.setParameter(1, Type.INT, "8");
        n.setParameter(2, Type.INT, "0");
        n.setParameter(3, Type.STRING, "1");//条码类型
        n.setParameter(4, Type.INT, "2");//宽度1
        n.setParameter(5, Type.INT, "2");//宽度2
        n.setParameter(6, Type.INT, "72");//高
        n.setParameter(7, 'B');
        n.setParameter(8, Type.STRING, "12356");//条码接入口
        n.invoke();
    }
    public void prnText1(Demo demo) throws Exception{
        JNative n= demo.getJNativeByFunction("B_Prn_Text");
        n.setParameter(0, Type.INT, String.valueOf(26));
        n.setParameter(1, Type.INT, String.valueOf(112));
        n.setParameter(2, Type.INT, String.valueOf(0));
        n.setParameter(3, Type.INT, String.valueOf(4)); //设置字体大小
        n.setParameter(4, Type.INT, String.valueOf(1));
        n.setParameter(5, Type.INT, String.valueOf(1));
        char mode='N';
        n.setParameter(6, mode);
        String st="测试测试";
        String st2 = new String(st.getBytes("Unicode"),"Unicode");//Unicode
        System.out.println(st2);
        n.setParameter(7, Type.STRING, st2);
        n.invoke();
    }
    public void prnText2(Demo demo) throws Exception{
        JNative n= demo.getJNativeByFunction("B_Prn_Text_TrueType");
        n.setParameter(0, Type.INT, "298");
        n.setParameter(1, Type.INT, "112");
        n.setParameter(2, Type.INT, "20");
        n.setParameter(3, Type.STRING, "宋体");
        n.setParameter(4, Type.INT, "1");
        n.setParameter(5, Type.INT, "700");
        n.setParameter(6, Type.INT, "0");
        n.setParameter(7, Type.INT, "0");
        n.setParameter(8, Type.INT, "0");
        n.setParameter(9, Type.STRING, "AA");
        n.setParameter(10, Type.STRING, "1111");

        n.invoke();

    }
    public void prnText3(Demo demo) throws Exception{
        JNative n= demo.getJNativeByFunction("B_Prn_Text_TrueType");
        n.setParameter(0, Type.INT, "570");
        n.setParameter(1, Type.INT, "112");
        n.setParameter(2, Type.INT, "20");
        n.setParameter(3, Type.STRING, "宋体");
        n.setParameter(4, Type.INT, "1");
        n.setParameter(5, Type.INT, "700");
        n.setParameter(6, Type.INT, "0");
        n.setParameter(7, Type.INT, "0");
        n.setParameter(8, Type.INT, "0");
        n.setParameter(9, Type.STRING, "AB");
        n.setParameter(10, Type.STRING, "商品名称");
        n.invoke();
    }
    public void printOut(Demo demo) throws Exception{
        JNative n= demo.getJNativeByFunction("B_Print_Out");
        n.setParameter(0, Type.INT, "2");
        n.invoke();
    }
    public void closePrn(Demo demo) throws Exception{
        JNative n= demo.getJNativeByFunction("B_ClosePrn");
        n.invoke();
    }
    public static void main(String [] args) throws Exception {
        /*可用方法
         * B_Set_Labwidth() 设置标签宽度
         * B_Set_Labgap(50,24); 设置“标签和Gap长度”。
         * */
        Demo demo = new Demo();
        /*加载dll文件*/
        System.loadLibrary("Winpplb");
        //demo.createPrn(demo);
        demo.createUSBPort(demo);

        demo.prnBarcode1(demo);
        demo.prnBarcode2(demo);
        demo.prnBarcode3(demo);
        demo.prnText1(demo);
        demo.prnText2(demo);
        demo.prnText3(demo);
        demo.printOut(demo);
        demo.closePrn(demo);
    }
    //public JNative getJNative(){


    //}
    // 通过方法名取得JNative对象
    public JNative getJNativeByFunction(String functionName) throws NativeException {
        return new JNative(PRINTER_DLL, functionName);
    }
}
