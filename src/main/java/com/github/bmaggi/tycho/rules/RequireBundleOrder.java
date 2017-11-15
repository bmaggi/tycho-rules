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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.maven.enforcer.rule.api.EnforcerRule;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.project.MavenProject;
import org.eclipse.tycho.core.BundleProject;

/**
 * Custom enforcer rule to assert that required bundle are following a specific order
 * @author Benoît Maggi
 */
public class RequireBundleOrder extends AbstractEclipsePluginEnforcerRule implements EnforcerRule {

	/**
	 * Name of the tested field in the Manifest
	 */
	private static final String REQUIRED_BUNDLE = "Require-Bundle";

	/**
	 * Expected value in the tested field
	 */
	private List<String> order = new ArrayList<>();

	
	/**
	 * Expected value in the tested field
	 */
	private boolean inverse = false;	
	

	@Override
	public void enforceTychoRule(BundleProject bundleProject, MavenProject project) throws EnforcerRuleException {
		String requiredBundleList = bundleProject.getManifestValue(REQUIRED_BUNDLE,
				project);
		log.info("Retrieved - " + REQUIRED_BUNDLE + " : " + requiredBundleList);
		log.info("Retrieved -order " + order + " : " + order.size());
		
		String[] bundleArray = requiredBundleList.split(",");
		
		if (!order.isEmpty()) {
			if (inverse){
				Collections.reverse(order);
			} 		
			
			int i = 0;
			for (String bundle : bundleArray) {
				while (i<order.size() && !bundle.startsWith(order.get(i))){
					i++;
				}
				if (i == order.size()){
					throw new EnforcerRuleException("The plugin isn't following the required bundle order");
				}	
			}
		} else {
			log.warn("Order parameter should'nt be empty");
		}	
	}

}
