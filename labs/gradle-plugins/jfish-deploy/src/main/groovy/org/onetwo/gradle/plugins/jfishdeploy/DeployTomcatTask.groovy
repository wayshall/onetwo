package org.onetwo.gradle.plugins.jfishdeploy

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.Project

class DeployTomcatTask extends DefaultTask {
	String profile;
	
	@TaskAction
	public void deployTomcat(){
		profile = project.extensions.findByName("profile")
		Project project = getProject();
		//def config = JFishDeployPlugin.loadGroovyConfig(project.getRootProject(), profile, "config")
		//logger.lifecycle "deployTomcat loaded ${profile} config::"+config

		logger.lifecycle "start deploy ..."
		def config = project.extensions.findByName("jfishDeployer").config
		def mainDir = config.deploy.tomcats[0].baseDir + "/webapps/" + project.name
		project.copy {
			logger.lifecycle "${mainDir} bakup to ${config.deploy.bakDir}"
			from mainDir
			into "${config.deploy.bakDir}/${project.name}-${new Date().format('yyyyMMddHHmmss')}"
		}
		
		config.deploy.tomcats.eachWithIndex { tc, index ->
			def webappDir = "${tc.baseDir}/webapps"
			project.copy {
				logger.lifecycle "${project.name} deploy to ${webappDir}"
				project.delete "${webappDir}/${project.name}"
				from project.war
				rename { it.replace "-${project.version}", ''}
				into webappDir
			}
			logger.lifecycle "execute ${tc.baseDir}/bin/${tc.starter} "
			"cmd /c start ${tc.baseDir}/bin/${tc.starter}".execute()
		}
		
	}


}
