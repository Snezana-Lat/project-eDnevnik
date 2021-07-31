package com.iktakademija.eDnevnik.services;

import java.io.IOException;


import javax.servlet.http.HttpServletResponse;

public interface FileHandler {

	
	public void downloadLog(HttpServletResponse response) throws IOException;
}
