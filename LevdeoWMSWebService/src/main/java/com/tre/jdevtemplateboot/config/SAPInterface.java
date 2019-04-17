package com.tre.jdevtemplateboot.config;

import com.sap.conn.jco.*;
import com.sap.conn.jco.ext.DestinationDataProvider;
import com.sap.conn.jco.ext.ServerDataProvider;
import com.sap.conn.jco.server.*;
import com.tre.jdevtemplateboot.service.SAPInterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * SAP调用JAVA
 * @author
 */
@Component
public class SAPInterface implements CommandLineRunner {
	@Autowired
	private SAPInterfaceService sapInterfaceService;
	//配置文件是dev还是product：dev不运行；product运行
	@Value("${spring.profiles.active}")
	private String applicationType;

	static String SERVER_NAME = "JCO_SERVER";
	static String DESTINATION_WITHOUT_POOL = "ABAP_AS_WITHOUT_POOL";
	static String DESTINATION_WITH_POOL = "ABAP_AS_WITH_POOL";
	static
	{
		Properties connectProperties = new Properties();
		connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, "192.168.117.15");
		connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR, "00");
		connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, "105");
		connectProperties.setProperty(DestinationDataProvider.JCO_USER, "ITWMS");
		connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, "itwms123");
		connectProperties.setProperty(DestinationDataProvider.JCO_LANG, "ZH");
		createDataFile(DESTINATION_WITHOUT_POOL,"jcoDestination",connectProperties);
		connectProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, "3");
		connectProperties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT, "10");
		createDataFile(DESTINATION_WITH_POOL,"jcoDestination",connectProperties);

		Properties servertProperties = new Properties();
		servertProperties.setProperty(ServerDataProvider.JCO_GWHOST, "192.168.117.15");
		servertProperties.setProperty(ServerDataProvider.JCO_GWSERV, "sapgw00");
		servertProperties.setProperty(ServerDataProvider.JCO_PROGID, SERVER_NAME);
		servertProperties.setProperty(ServerDataProvider.JCO_REP_DEST, DESTINATION_WITH_POOL);
		servertProperties.setProperty(ServerDataProvider.JCO_CONNECTION_COUNT, "2");
		createDataFile(SERVER_NAME,"jcoServer",servertProperties);
	}

	/**
	 * 创建SAP服务及连接文件
	 * @param name
	 * @param suffix
	 * @param properties
	 */
	static void createDataFile (String name,String suffix,Properties properties)
	{
		File cfg = new File(name+"."+suffix);
		if(!cfg.exists())
		{
			try
			{
				FileOutputStream fos = new FileOutputStream(cfg,false);
				properties.store(fos, "sapCallWmsJava");
				fos.close();
			}
			catch (Exception e) {
				throw new RuntimeException("unable to create the destination file "+ cfg.getName());
			}
		}
	}

	@Override
	public void run(String... args) throws Exception {
		//dev不运行；product运行
		if("product".equals(applicationType)){
			//SAP调用JAVA
			sapCallJava();
		}
	}

	/**
	 * SAP调用JAVA
	 */
	private void sapCallJava() {
		JCoCustomRepository cusRep = JCo.createCustomRepository("MyCustomRepository");

		JCoServer server;
		try
		{
			server = JCoServerFactory.getServer(SERVER_NAME);
		}
		catch(JCoException ex)
		{
			throw new RuntimeException("Unable to create the server " + SERVER_NAME + " because of " + ex.getMessage(), ex);
		}

		String repDest = server.getRepositoryDestination();
		if(repDest!=null)
		{
			try
			{
				cusRep.setDestination(JCoDestinationManager.getDestination(repDest));
			}
			catch (JCoException e)
			{
				e.printStackTrace();
				System.out.println(">>> repository contains static function definition only");
			}
		}
		server.setRepository(cusRep);

		DefaultServerHandlerFactory.FunctionHandlerFactory factory = new DefaultServerHandlerFactory.FunctionHandlerFactory();
		//SAP调用后，JAVA端的处理函数
		JCoServerFunctionHandler funHandler;
		//定义SAP传入的TABLE参数
		JCoListMetaData tables;
		//定义方法，SAP端调用
		JCoFunctionTemplate funTemp;

		//定义输出参数
		JCoListMetaData exports = JCo.createListMetaData("EXPORTS");
		exports.add("RTN_CODE", JCoMetaData.TYPE_CHAR, 10, 10, JCoListMetaData.EXPORT_PARAMETER);
		exports.add("RTN_MSG", JCoMetaData.TYPE_CHAR, 200, 200, JCoListMetaData.EXPORT_PARAMETER);
		exports.lock();

		//==========================================入库交接单 START========================================
		//定义SAP传入的TABLE参数，ZWMS_STORGE_REC：SAP端参照的表
		tables = JCo.createListMetaData("TABLE");
		tables.add("OUT_TAB", JCoMetaData.TYPE_TABLE, 330, 660, 0, null, null, JCoListMetaData.IMPORT_PARAMETER, "ZWMS_STORGE_REC", null);
		tables.lock();

		//定义方法，SAP端调用ZWMS_RFC_STORAGE_REC
		funTemp = JCo.createFunctionTemplate("ZWMS_RFC_STORAGE_REC", null, exports, null, tables, null);
		cusRep.addFunctionTemplateToCache(funTemp);

		//SAP调用后，JAVA端的处理函数
		funHandler = new FunctionHandler();
		//注册，funTemp.getName()=ZWMS_RFC_STORAGE_REC
		factory.registerHandler(funTemp.getName(), funHandler);

		server.setCallHandlerFactory(factory);
		//==========================================入库交接单 END==========================================

		//==========================================发货通知单 START========================================
		//定义SAP传入的TABLE参数，ZWMS_CONSIGNMENT_NOTE：SAP端参照的表
		tables = JCo.createListMetaData("TABLE");
		tables.add("OUT_TAB", JCoMetaData.TYPE_TABLE, 330, 660, 0, null, null, JCoListMetaData.IMPORT_PARAMETER, "ZWMS_CONSIGNMENT_NOTE", null);
		tables.add("OUT_VIN", JCoMetaData.TYPE_TABLE, 330, 660, 0, null, null, JCoListMetaData.IMPORT_PARAMETER, "ZWMS_CON_VIN", null);
		tables.lock();

		//定义方法，SAP端调用ZWMS_RFC_CONSIGNMENT_NOTE
		funTemp = JCo.createFunctionTemplate("ZWMS_RFC_CONSIGNMENT_NOTE", null, exports, null, tables, null);
		cusRep.addFunctionTemplateToCache(funTemp);

		//SAP调用后，JAVA端的处理函数
		funHandler = new FunctionHandler();
		factory.registerHandler(funTemp.getName(), funHandler);

		server.setCallHandlerFactory(factory);
		//==========================================发货通知单 END==========================================

		//==========================================整车借用调拨归还 START==================================
		//定义SAP传入的TABLE参数，ZWMS_BORROW_DOC：SAP端参照的表
		tables = JCo.createListMetaData("TABLE");
		tables.add("OUT_TAB", JCoMetaData.TYPE_TABLE, 330, 660, 0, null, null, JCoListMetaData.IMPORT_PARAMETER, "ZWMS_BORROW_DOC", null);
		tables.lock();

		//定义方法，SAP端调用ZWMS_RFC_BORROW_DOC
		funTemp = JCo.createFunctionTemplate("ZWMS_RFC_BORROW_DOC", null, exports, null, tables, null);
		cusRep.addFunctionTemplateToCache(funTemp);

		//SAP调用后，JAVA端的处理函数
		funHandler = new FunctionHandler();
		factory.registerHandler(funTemp.getName(), funHandler);

		server.setCallHandlerFactory(factory);
		//==========================================整车借用调拨归还 END====================================

		server.start();
		System.out.println("The program can be stoped using <ctrl>+<c>");
	}

	/**
	 * SAP调用后，JAVA端的处理函数
	 */
	class FunctionHandler implements JCoServerFunctionHandler
	{
		@Override
		public void handleRequest(JCoServerContext serverCtx, JCoFunction function) throws AbapException, AbapClassException {
			String funName = function.getName();

			//入库交接单
			if("ZWMS_RFC_STORAGE_REC".equals(funName)) {
				sapInterfaceService.storage_rec(serverCtx, function);
			}
			//发货通知单
			else if("ZWMS_RFC_CONSIGNMENT_NOTE".equals(funName)) {
				sapInterfaceService.consignment_note(serverCtx, function);
			}
			//整车借用调拨归还
			else if("ZWMS_RFC_BORROW_DOC".equals(funName)) {
				sapInterfaceService.borrow_doc(serverCtx, function);
			}
		}
	}
}
