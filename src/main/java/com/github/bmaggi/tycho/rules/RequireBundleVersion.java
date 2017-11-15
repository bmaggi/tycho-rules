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
 * Custom enforcer rule to assert that all required bundle have *bundle-version*
 * set.
 * 
 * @author Benoît Maggi
 */
public class RequireBundleVersion extends AbstractEclipsePluginEnforcerRule implements EnforcerRule {

	/**
	 * Name of the tested field in the Manifest
	 */
	private static final String REQUIRED_BUNDLE = "Require-Bundle";

	private static final String BUNDLE_VERSION = "bundle-version";


	@Override
	public void enforceTychoRule(BundleProject bundleProject, MavenProject project) throws EnforcerRuleException {
		String requiredBundleList = bundleProject.getManifestValue(
				REQUIRED_BUNDLE, project);
		log.info("Retrieved - " + REQUIRED_BUNDLE + " : "
				+ requiredBundleList);
		String[] bundleArray = requiredBundleList.split(",");
		boolean hasAllBundleVersion = true;
		for (String bundle : bundleArray) {
			String[] split2 = bundle.split(";");
			boolean hasBundleVersion = false;
			for (String string : split2) {
				hasBundleVersion = hasBundleVersion
						|| string.startsWith(BUNDLE_VERSION);
			}
			hasAllBundleVersion = hasAllBundleVersion
					&& hasBundleVersion;
		}

		if (!hasAllBundleVersion) {
			throw new EnforcerRuleException(
					"A required bundle is missing its version");
		}
		
	}

}
