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
		def deployer = project.extensions.findByName("jfishDeployer")
		if(deployer==null){
			logger.lifecycle "no profile deloyer found, ignore task!"
			return
		}
		def config = project.extensions.findByName("jfishDeployer").config
		
		config.deploy.tomcats.eachWithIndex { tc, index ->
			//stop tomcat
			if(tc.terminator){
				def cmd = tc.terminator
				if(cmd.startsWith(":")){
					cmd = "${tc.baseDir}/bin/${tc.terminator.substring(1)}"
				}
				logger.lifecycle "stop the tomcat server with: ${cmd} "
				def proc = cmd.execute(System.getenv() as String[], new File(tc.baseDir))
				logger.lifecycle "info===>>>\n ${proc.text}"
			}
			
			//bak
			def mainDir = "${tc.baseDir}/webapps/project.name"
			project.copy {
				logger.lifecycle "${mainDir} bakup to ${config.deploy.bakDir}"
				from mainDir
				into "${config.deploy.bakDir}/${project.name}-${new Date().format('yyyyMMddHHmmss')}"
			}
			
			//deploy war
			def webappDir = "${tc.baseDir}/webapps"
			logger.lifecycle "delete app dir : ${webappDir}/${project.name}"
			project.delete "${webappDir}/${project.name}"
			project.copy {
				logger.lifecycle "${project.name} deploy to ${webappDir}"
				from project.war
				rename { it.replace "-${project.version}", ''}
				into webappDir
			}
			
			//start tomcat
			if(tc.starter){
//				"cmd /c start ${tc.baseDir}/bin/${tc.starter}".execute()
				def cmd = tc.starter
				if(cmd.startsWith(":")){
					cmd = "cmd /c start ${tc.baseDir}/bin/${tc.starter.substring(1)}"
				}
				logger.lifecycle "start the tomcat server with: ${cmd} "
				cmd.execute(System.getenv() as String[], new File(tc.baseDir))
			}
		}
		
	}


}
