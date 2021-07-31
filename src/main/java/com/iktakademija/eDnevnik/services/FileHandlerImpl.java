package com.iktakademija.eDnevnik.services;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

@Service
public class FileHandlerImpl implements FileHandler{

	@Override
	public void downloadLog(HttpServletResponse response) throws IOException {
	File file=new File("C:\\Users\\Prasko\\Documents\\workspace-spring-tool-suite-4-4.10.0.RELEASE\\eDnevnik\\logs\\\\spring-boot-logging.log");
	String headerKey="Content-disposition";
	String headerValue="attachment;filename="+"spring-boot-logging.log";
	
	response.setHeader(headerKey, headerValue);
	response.setContentLength((int) file.length());

	InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

	FileCopyUtils.copy(inputStream, response.getOutputStream());
	}

}
