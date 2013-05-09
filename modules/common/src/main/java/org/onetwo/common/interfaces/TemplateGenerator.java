package org.onetwo.common.interfaces;

import java.io.OutputStream;

public interface TemplateGenerator {
	public void generateIt();

	public void write(OutputStream out);

	public void write(String path);

}