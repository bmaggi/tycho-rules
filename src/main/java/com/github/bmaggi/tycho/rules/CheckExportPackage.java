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

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.project.MavenProject;
import org.eclipse.tycho.core.BundleProject;

/**
 * Custom enforcer rule to assert that all package are exported in the manifest
 * 
 * @author Benoît Maggi
 */
public class CheckExportPackage extends AbstractEclipsePluginEnforcerRule {

	/**
	 * Name of the tested field in the Manifest
	 */
	private static final String EXPORT_PACKAGE = "Export-Package";	
	
    /**
     * Project's source directory as specified in the POM.
     * 
     * @parameter expression="${project.build.sourceDirectory}"
     * @readonly
     * @required
     */
    private final File sourceDirectory = new File("src");
 	

	@Override
	public void enforceTychoRule(BundleProject bundleProject,MavenProject project) throws EnforcerRuleException {
		String fieldValue = bundleProject.getManifestValue(EXPORT_PACKAGE, project);
		log.info("sourceDirectory - " + sourceDirectory + " : ");
		
		List<String> compileSourceRoots = project.getCompileSourceRoots();
//		List<String> compileClasspathElements;
//		try {
//			compileClasspathElements = project.getCompileClasspathElements();
//			log.info("compileClasspathElements - " + compileClasspathElements + " : ");
//		} catch (DependencyResolutionRequiredException e) {
//			log.error("Exception while trying to get the elements in classpath", e);
//		}
		log.info("compileSourceRoots - " + compileSourceRoots + " : ");
		
		Set<String> packageNamespace = getPackageNamespace(sourceDirectory.listFiles());
		
		log.info("Retrieved - " + EXPORT_PACKAGE + " : " + fieldValue);				
	
		List<String> exportedPackageList = Arrays.asList(fieldValue.split(","));
		for (String namespace : packageNamespace) {
			if (!exportedPackageList.contains(namespace)) {
				log.error(namespace+" should be exported");
				throw new EnforcerRuleException(namespace+" should be exported");
			}
		}
		
	}
    
	public Set<String> getPackageNamespace(File[] root) {
		Set<String> hashSet = new HashSet<>();
		for (final File file : root) {
			if (file.isDirectory()) {
				hashSet.addAll(getPackageNamespace(file));
							} else {
				hashSet.add(file.getParent().replace(File.separator, ".").replace("src.", ""));
			}
		}
		return hashSet;
	}
	
	public Set<String> getPackageNamespace(final File root) {
		Set<String> hashSet = new HashSet<>();
		for (final File file : root.listFiles()) {
			if (file.isDirectory()) {
				hashSet.addAll(getPackageNamespace(file));
			} else {
				hashSet.add(file.getParent().replace(File.separator, ".").replace("src.", ""));
			}
		}
		return hashSet;
	}



}
