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

import com.github.bmaggi.tycho.rules.utils.BundleUtil;

/**
 * This rule force the reexport of the package following the specified namespace
 * 
 * @author Benoît Maggi
 */
public class CheckReexportBundle extends AbstractEclipsePluginEnforcerRule implements EnforcerRule {

	/**
	 * Name of the tested field in the Manifest
	 */
	private static final String REQUIRED_BUNDLE = "Require-Bundle";



	private String namespace = "";

	private String force = "force";// otherwise restrict

	// force : force reexport of the namespace

	// restrict : forbid any export not in the namespace

	// org.eclipse.uml2.uml;bundle-version="5.1.0";visibility:=reexport,
	@Override
	public void enforceTychoRule(BundleProject bundleProject, MavenProject project) throws EnforcerRuleException {
		String requiredBundleList = bundleProject.getManifestValue(REQUIRED_BUNDLE, project);
		log.info("Retrieved - " + REQUIRED_BUNDLE + " : " + requiredBundleList);
		log.info("Namespace - " + namespace);
		String[] bundleArray = requiredBundleList.split(",");
		for (String bundle : bundleArray) {
			String[] split = bundle.split(";");
			if ("force".equals(force) && split[0].matches(namespace) && !BundleUtil.isBundleReexported(bundle)) {
				throw new EnforcerRuleException("Reexport required in plugin "+project.getArtifactId()+" for this bundle "+split[0]);
			}
			if (!"force".equals(force) && !split[0].matches(namespace) && BundleUtil.isBundleReexported(bundle)) {
				throw new EnforcerRuleException("Reexport forbidden in plugin "+project.getArtifactId()+" for this bundle "+split[0]);
			}
		}

	}



}
