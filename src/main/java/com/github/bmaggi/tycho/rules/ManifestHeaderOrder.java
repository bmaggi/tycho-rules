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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

import org.apache.maven.enforcer.rule.api.EnforcerRule;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.eclipse.osgi.framework.util.Headers;
import org.eclipse.tycho.core.BundleProject;
import org.eclipse.tycho.core.osgitools.BundleReader;
import org.eclipse.tycho.core.osgitools.OsgiBundleProject;
import org.eclipse.tycho.core.osgitools.OsgiManifest;

import com.google.common.collect.Ordering;

/**
 * Custom enforcer rule to assert that manifest headers are following a specific
 * order (Wildcards at the end)
 * 
 * @author Benoît Maggi
 */

// FIXME so inefficient
class OrderingByFixedDefaultOrder extends Ordering<String> {

	// TODO check osgi norm and eclipse standard to chose correct order
	public static final String[] defaultOrder = { "Manifest-Version", "Bundle-ManifestVersion", "Bundle-SymbolicName",
			"Bundle-Name", "Bundle-Localization", "Bundle-Version", "Bundle-Activator", "Bundle-Vendor",
			"Bundle-RequiredExecutionEnvironment", "Require-Bundle", "Eclipse-BuddyPolicy", "Bundle-ActivationPolicy",
			"Export-Package" };

	@Override
	public int compare(String s1, String s2) {
		Integer position = getPosition(s1);
		Integer position2 = getPosition(s2);
		return position.compareTo(position2);
	}

	public int getPosition(String field) {
		for (int i = 0; i < defaultOrder.length; i++) {
			if (defaultOrder[i].equals(field)) {
				return i;
			}
		}
		return defaultOrder.length + 1;

	}
}

public class ManifestHeaderOrder extends AbstractEclipsePluginEnforcerRule implements EnforcerRule {

	// FIXME : Ask to open Tycho API OsgiBundleProject.getManifest()
	private OsgiManifest getHeader(Log log, MavenProject project, OsgiBundleProject oo) {
		log.debug("Start getHeader");
		Field bundleReaderField;
		try {
			bundleReaderField = OsgiBundleProject.class.getDeclaredField("bundleReader");
			bundleReaderField.setAccessible(true);
			BundleReader bundleReader = (BundleReader) bundleReaderField.get(oo);
			return bundleReader.loadManifest(project.getBasedir());
		} catch (NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void enforceTychoRule(BundleProject bundleProject, MavenProject project) throws EnforcerRuleException {
		if (bundleProject instanceof OsgiBundleProject) {
			OsgiManifest loadManifest = getHeader(log, project, (OsgiBundleProject) bundleProject);
			if (loadManifest != null) {
				Headers<String, String> headers = loadManifest.getHeaders();
				Enumeration<String> keys = headers.keys();
				ArrayList<String> list = Collections.list(keys);
				Ordering<String> orderingByFixedDefaultOrder = new OrderingByFixedDefaultOrder();
				if (!orderingByFixedDefaultOrder.isOrdered(list)) {
					Collections.sort(list, orderingByFixedDefaultOrder.compound(Ordering.natural()));
					throw new EnforcerRuleException("The bundle isn't following the standard manifest headers order :"
							+ Arrays.toString(OrderingByFixedDefaultOrder.defaultOrder) + "it should be"
							+ list.toString());
				}
			}
		}

	}

}
