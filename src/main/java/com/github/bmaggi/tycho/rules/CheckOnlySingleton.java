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
 * Check that there is no singleton
 * 
 * @author Benoît Maggi
 */
public class CheckOnlySingleton extends AbstractEclipsePluginEnforcerRule implements EnforcerRule {

	private static final String BUNDLE_SYMBOLICNAME = "Bundle-SymbolicName";	

	@Override
	public void enforceTychoRule(BundleProject bundleProject, MavenProject project) throws EnforcerRuleException {
		String fieldValue = bundleProject.getManifestValue(BUNDLE_SYMBOLICNAME, project);
		log.info("Retrieved - " + BUNDLE_SYMBOLICNAME + " : " + fieldValue);
		if (!fieldValue.contains("singleton:=true")) {
			throw new EnforcerRuleException("This plugin should be singleton "+project.getArtifactId());
		}

	}

}
