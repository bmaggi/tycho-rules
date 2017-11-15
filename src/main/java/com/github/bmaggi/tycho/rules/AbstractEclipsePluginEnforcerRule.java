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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.maven.enforcer.rule.api.EnforcerRule;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.eclipse.tycho.PackagingType;
import org.eclipse.tycho.core.BundleProject;
import org.eclipse.tycho.core.TychoProject;

public abstract class AbstractEclipsePluginEnforcerRule implements EnforcerRule {

	// parameter with current project
	private static final String PROJECT = "${project}";

	protected Log log;

	// path to the report file ex: report.log
	private String report = "";

	@Override
	public void execute(EnforcerRuleHelper helper) throws EnforcerRuleException {
		log = helper.getLog();
		try {
			MavenProject project = (MavenProject) helper.evaluate(PROJECT);
			String packaging = project.getPackaging();
			log.info("Retrieved packaging: " + packaging);

			if (PackagingType.TYPE_ECLIPSE_PLUGIN.equals(packaging)) {
				TychoProject projectType = (TychoProject) helper.getComponent(TychoProject.class.getName(), packaging);
				log.info("ProjectType: " + projectType);

				if (projectType instanceof BundleProject) {
					enforceTychoRule((BundleProject) projectType, project);
				}

			}
		} catch (ComponentLookupException e) {
			throw new EnforcerRuleException("Unable to lookup a component " + e.getLocalizedMessage(), e);
		} catch (ExpressionEvaluationException e) {
			throw new EnforcerRuleException("Unable to lookup an expression " + e.getLocalizedMessage(), e);
		} catch (Exception e) {
			if (!"".equals(report)) {
				log.info("Report mode enable in " + report);
				try {
					Path path = Paths.get(report);
					Files.write(path, (e.getMessage()+"\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
				} catch (IOException ioe) {
					log.warn(ioe);
				}
			} else {
				throw new EnforcerRuleException("Unable to execute rule : " + e.getLocalizedMessage(), e);
			}
			
		}

	}

	public abstract void enforceTychoRule(BundleProject bundleProject, MavenProject project)
			throws EnforcerRuleException;

	@Override
	public boolean isCacheable() {
		return false;
	}

	@Override
	public boolean isResultValid(EnforcerRule cachedRule) {
		return false;
	}

	@Override
	public String getCacheId() {
		return null;
	}

}
