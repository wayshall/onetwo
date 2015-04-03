package org.onetwo.gradle.plugins.jfishdeploy

import org.gradle.api.Plugin
import org.gradle.api.Project

class JFishDeployPlugin implements Plugin<Project> {

	@Override
	def void apply(Project project) {
		def configName = System.getProperty("profile")
		def configFile = project.file(configName.endsWith('.groovy')?configName:"${configName}.groovy")
		new ConfigSlurper(profile).parse(configFile.toURL())
		
		project.task("deployTomcat", type: DeployTomcatTask)
	}
	
}
