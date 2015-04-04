package org.onetwo.gradle.plugins.jfishdeploy

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.Project

class CopyEnvConfigTask extends DefaultTask {
	String profile;
	
	@TaskAction
	public void copyEnvConfig222(){
		logger.lifecycle "execute copyEnvConfig"
			
		profile = project.extensions.findByName("profile")
		//def config = JFishDeployPlugin.loadGroovyConfig(project.getRootProject(), profile, "config")
		def config = project.extensions.findByName("jfishDeployer").config
		logger.lifecycle "deployTomcat loaded ${profile} config::"+config
		if(config!=null){
			logger.lifecycle "copy ${profile} config..."
			from config.deploy.configDir
			into "${buildDir}/resources/main"
		}
	}


}
