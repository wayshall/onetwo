package org.onetwo.gradle.plugins.jfishdeploy

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test
import static org.junit.Assert.*

class JFishDeployTaskTest {
	
	@Test
	public void canAddTaskToProject() {
		Project project = ProjectBuilder.builder().build()
		def task = project.task('deploy', type: DeployTomcatTask)
		assertTrue(task instanceof DeployTomcatTask)
		
		task.execute()
	}
}
