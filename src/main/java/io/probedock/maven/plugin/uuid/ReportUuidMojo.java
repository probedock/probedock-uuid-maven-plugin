package io.probedock.maven.plugin.uuid;

import java.util.UUID;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Generate a UUID report id and store it in the session properties.
 * The default name of the property is: probedock.report.uuid
 * 
 * @author Laurent Prevost laurent.prevost@probedock.io
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.INITIALIZE, requiresProject = true)
public class ReportUuidMojo extends AbstractMojo {
	@Parameter(defaultValue = "probedock.report.uuid")
	private String propertyName;
	
	@Parameter(defaultValue = "${session}", required = true)
	private MavenSession session;

	/**
	 * First run a post configuration process to ensure that the configuration
	 * is well done and complete the missing elements and/or defaults value
	 * 
	 * @throws MojoExecutionException When a command is not executable
	 * @throws MojoFailureException When a command has failed
	 */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		// Try to retrieve if a UUID was already generated from the top level project properties
		String generatedUuid = session.getTopLevelProject().getProperties().getProperty(propertyName);
		
		// If not generated, we generate a new one and set to the top level project properties
		if (generatedUuid == null || generatedUuid.isEmpty()) {
			generatedUuid = UUID.randomUUID().toString();
			session.getTopLevelProject().getProperties().setProperty(propertyName, generatedUuid);
		}
		
		// Finally, we also set it to the current project properties
		session.getCurrentProject().getProperties().setProperty(propertyName, generatedUuid);
	}
}
