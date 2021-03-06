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
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.eclipse.tycho.PackagingType;
import org.eclipse.tycho.core.TychoProject;
import org.eclipse.tycho.core.osgitools.EclipseFeatureProject;
import org.eclipse.tycho.model.Feature;

/**
 * Custom enforcer rule to assert that the feature has the required provider
 * @author Benoît Maggi
 */
public class RequireFeatureProvider implements EnforcerRule {

	// parameter with current project 
	private static final String PROJECT = "${project}";

	/**
	 * Expected string in the provider field
	 */
	private String provider = "";
	
	
	/* (non-Javadoc)
	 * @see org.apache.maven.enforcer.rule.api.EnforcerRule#execute(org.apache.maven.enforcer.rule.api.EnforcerRuleHelper)
	 */
	@Override
	public void execute(EnforcerRuleHelper helper) throws EnforcerRuleException {
		Log log = helper.getLog();
		try {
			MavenProject project = (MavenProject) helper.evaluate(PROJECT);
			String packaging = project.getPackaging();
			log.info("Retrieved packaging: " + packaging);

			if (PackagingType.TYPE_ECLIPSE_FEATURE.equals(packaging)) {
				TychoProject projectType = (TychoProject) helper.getComponent(
						TychoProject.class.getName(), packaging);
				log.info("ProjectType: " + projectType);
				
				if (projectType instanceof EclipseFeatureProject) {
			        final Feature feature = Feature.loadFeature(project.getBasedir());
			        log.info("Provider: " + feature.getProvider());
					if (!provider.equals(feature.getProvider())){
						throw new EnforcerRuleException("The feature provider is missing");
					}			        
				}
			}

		} catch (ComponentLookupException e) {
			throw new EnforcerRuleException("Unable to lookup a component "
					+ e.getLocalizedMessage(), e);
		} catch (ExpressionEvaluationException e) {
			throw new EnforcerRuleException("Unable to lookup an expression "
					+ e.getLocalizedMessage(), e);
		} catch (Exception e) {
			throw new EnforcerRuleException(e.getLocalizedMessage(), e);
		}

	}

	/* (non-Javadoc)
	 * @see org.apache.maven.enforcer.rule.api.EnforcerRule#isCacheable()
	 */
	@Override
	public boolean isCacheable() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.apache.maven.enforcer.rule.api.EnforcerRule#getCacheId()
	 */
	@Override
	public String getCacheId() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.maven.enforcer.rule.api.EnforcerRule#isResultValid(org.apache.maven.enforcer.rule.api.EnforcerRule)
	 */
	@Override
	public boolean isResultValid(EnforcerRule arg0) {
		return false;
	}

}
