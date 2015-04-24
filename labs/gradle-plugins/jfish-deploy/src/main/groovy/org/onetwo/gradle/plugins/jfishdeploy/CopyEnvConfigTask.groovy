package org.onetwo.gradle.plugins.jfishdeploy

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.Project

class CopyEnvConfigTask extends DefaultTask {
	String profile;
	
	@TaskAction
	public void copyEnvConfigToBuildResource(){
		profile = project.extensions.findByName("profile")
		Project project = getProject();
		
		logger.lifecycle "execute copyEnvConfig"
		
		def config = project.extensions.findByName("jfishDeployer").config
		
		project.copy {
			logger.lifecycle "copy ${profile} config to ${project.buildDir}/resources/main ..."
			from config.deploy.configDir
			into "${project.buildDir}/resources/main"
		}
		
	}


}
