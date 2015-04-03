package org.onetwo.gradle.plugins.jfishdeploy

import org.gradle.api.Plugin
import org.gradle.api.Project

class JFishDeployPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		project.task("deployTomcat", type: DeployTomcatTask)
	}

}
