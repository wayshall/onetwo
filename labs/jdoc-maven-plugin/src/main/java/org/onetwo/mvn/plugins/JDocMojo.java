package org.onetwo.mvn.plugins;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.util.Collection;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.file.FileScanner;
import org.onetwo.common.utils.xml.XmlUtils;
import org.onetwo.plugins.jdoc.Lexer.JavadocManager;
import org.onetwo.plugins.jdoc.data.JClassDoc;

/**
* @goal out
* @phase compile
*/
public class JDocMojo extends AbstractMojo {
	
	protected static final String DEFAULT_RESOURCE_PATTERN = ".+(Controller|Params|Data|Result)\\.java";
	
	/**
	* @parameter expression="${basedir}" defaultValue="${basedir}"
	*/
	private String basedir;

	public void execute() throws MojoExecutionException {
		FileScanner scaner = new FileScanner(DEFAULT_RESOURCE_PATTERN);
		JavadocManager jm = new JavadocManager();
		jm.setDocScaner(scaner);
		jm.setPathToScan(basedir+"/src/main/java");
		try {
			getLog().info( "scan path: "+basedir);
			jm.startScanDoc();
			Collection<JClassDoc> classDocs = jm.getClassDocs();
			String xml = XmlUtils.toXML(classDocs);
			FileUtils.writeStringToFile(new File(basedir+"/target/classes/jdoc.xml"), xml);
		} catch (Exception e) {
			getLog().error("jdoc build error: " + e.getMessage());
		}
	}
}
