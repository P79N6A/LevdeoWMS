package com.tre.jdevtemplateboot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * 1、创建项目需要的文件夹
 * 2、SAP调用JAVA
 * @author SongGuiFan
 */
@Component
public class MyCommandLineRunner implements CommandLineRunner {
	//条形码保存路径
	@Value("${my.barcodepath}")
	private String barcodepath;
	
	//上传临时文件夹
	@Value("${spring.servlet.multipart.location}")
	private String multipartLocation;

	@Override
	public void run(String... args) throws Exception {
		//创建项目需要的文件夹
		mkDir(barcodepath);
		mkDir(multipartLocation);

	}
	
	/**
	 * 创建项目需要的文件夹
	 * @param path
	 * @throws IOException
	 */
	private void mkDir(String path) throws IOException {
		File dirs = new File(path);
		if(!dirs.exists()){
			dirs.mkdirs();
		}
		if(!dirs.exists()){
			throw new IOException("创建文件夹失败：" + path);
		}

	}
}
