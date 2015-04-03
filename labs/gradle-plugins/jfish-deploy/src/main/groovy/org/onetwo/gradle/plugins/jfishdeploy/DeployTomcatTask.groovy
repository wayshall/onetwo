package org.onetwo.gradle.plugins.jfishdeploy

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction;

class DeployTomcatTask extends DefaultTask {
	
	@TaskAction
	public void deployTomcat(){
		logger.lifecycle "copy war to servsers ..."
		def mainDir = config.deploy.tomcats[0].baseDir + "/webapps/" + project.name
		copy {
			logger.lifecycle "${mainDir} bak to ${config.deploy.bakDir}"
			from mainDir
			into "${config.deploy.bakDir}/${project.name}-${new Date().format('yyyyMMddHHmmss')}"
		}
		
		config.deploy.tomcats.eachWithIndex { tc, index ->
			def webappDir = "${tc.baseDir}/webapps"
			copy {
				logger.lifecycle "${project.name} deploy to ${webappDir}"
				delete "${webappDir}/${project.name}"
				with renameWarFile
				into webappDir
			}
			"cmd /c start ${tc.baseDir}/bin/${tc.starter}".execute()
		}
	}

}
