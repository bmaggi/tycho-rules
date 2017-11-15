/*****************************************************************************
 * Copyright (c) 2017 Benoît Maggi.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Benoît Maggi - Initial API and implementation
 *****************************************************************************/
package com.github.bmaggi.tycho.rules;

import org.apache.maven.enforcer.rule.api.EnforcerRule;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.project.MavenProject;
import org.eclipse.tycho.core.BundleProject;

/**
 * Custom enforcer rule to assert that a field is correctly valued in
 * Manifest.MF
 * 
 * @author Benoît Maggi
 */
public class CheckManifestParameter extends AbstractEclipsePluginEnforcerRule implements EnforcerRule {

	/**
	 * Name of the tested field in the Manifest
	 */
	private String field = "";

	/**
	 * Expected value in the tested field
	 */
	private String value = "";

	@Override
	public void enforceTychoRule(BundleProject bundleProject, MavenProject project) throws EnforcerRuleException {
		String fieldValue = bundleProject.getManifestValue(field, project);
		log.info("Retrieved - " + field + " : " + fieldValue);
		if (!value.equals(fieldValue)) {
			throw new EnforcerRuleException("Wrong value in Manifest");
		}

	}

}
