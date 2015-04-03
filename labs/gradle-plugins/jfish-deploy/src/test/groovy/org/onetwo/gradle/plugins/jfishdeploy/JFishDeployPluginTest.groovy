package org.onetwo.gradle.plugins.jfishdeploy

import static org.junit.Assert.*

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

class JFishDeployPluginTest {
	
	@Test
	public void greeterPluginAddsGreetingTaskToProject() {
		Project project = ProjectBuilder.builder().build()
		project.apply plugin: 'jfishdeploy'
		
		assertTrue(project.tasks.deployTomcat instanceof DeployTomcatTask)

	}
}
