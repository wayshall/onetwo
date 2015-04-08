package org.onetwo.gradle.plugins.jfishdeploy

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.Copy

class JFishDeployPlugin implements Plugin<Project> {

	private Logger logger =  Logging.getLogger(JFishDeployPlugin.class)

	@Override
	def void apply(Project project) {
		
		def profile = System.getProperty("profile")
		JFishDeployer jfishDeployer = new JFishDeployer()
		project.extensions.add("jfishDeployer", jfishDeployer)
		
		if(profile==null){
			logger.lifecycle "no profile found!"
			project.extensions.add("profile", "dev")
			project.task("deployTomcat", type: DeployTomcatTask);
			return 
		}

		project.extensions.add("profile", profile)

		def config = loadGroovyConfig(project.getRootProject(), profile, "config")
		jfishDeployer.config = config
		//project.extraProperties.set("jdeployConfig", config)

		project.task("deployTomcat", type: DeployTomcatTask).dependsOn("war")
		def copyEnvConfig = project.task("copyEnvConfig", type: Copy){
			//def config = JFishDeployPlugin.loadGroovyConfig(project.getRootProject(), profile, "config")
			logger.lifecycle "copyEnvConfig loaded ${profile} config::"+config
			if(config!=null){
				logger.lifecycle "copy ${profile} config..."
				from config.deploy.configDir
				into "${project.buildDir}/resources/main"
			}
		}

		project.tasks.war.dependsOn copyEnvConfig
	}

	public static loadGroovyConfig(project, profile, configName) {
		Logger logger =  Logging.getLogger(JFishDeployPlugin.class)//project.getLogger()
		def configFile = project.file(configName.endsWith('.groovy')?configName:"${configName}.groovy")
		logger.lifecycle "load config file : ${configFile}"
		def configData = new ConfigSlurper(profile).parse(configFile.toURL())
		logger.lifecycle "loaded ${profile} config::"+configData
		return configData;
	}

	class JFishDeployer {
		ConfigObject config;

		def embeddedTomcat() {
			def tomcatVersion = "7.0.59";
			def jspVersion = "2.1"
			
			["org.apache.tomcat.embed:tomcat-embed-core:$tomcatVersion",
			 "org.apache.tomcat.embed:tomcat-embed-logging-log4j:$tomcatVersion",
			 "org.apache.tomcat.embed:tomcat-embed-jasper:$tomcatVersion",
			 "javax.servlet.jsp:jsp-api:${jspVersion}"]
		}
	}
	
}
