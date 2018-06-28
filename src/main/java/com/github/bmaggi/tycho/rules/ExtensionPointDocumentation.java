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
import java.io.FilenameFilter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.maven.enforcer.rule.api.EnforcerRule;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.project.MavenProject;
import org.eclipse.tycho.core.BundleProject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Custom enforcer rule to assert that an extension point is fully documented
 * 
 * @author Benoît Maggi
 */
public class ExtensionPointDocumentation extends AbstractEclipsePluginEnforcerRule implements EnforcerRule {

	// parameter with current project
	private static final String PROJECT = "${project}";

	private static final String DEFAULT_VALUE_SINCE = "[Enter the first release in which this extension point appears.]";

	private static final String DEFAULT_VALUE_EXAMPLES = "[Enter extension point usage example here.]";

	private static final String DEFAULT_VALUE_APIINFOS = "[Enter API information here.]";

	private static final String DEFAULT_VALUE_IMPLEMENTATION = "[Enter information about supplied implementation of this extension point.]";

	// TODO: this first version put every doc as required

	@Override
	public void enforceTychoRule(BundleProject bundleProject, MavenProject project) throws EnforcerRuleException {
		File basedir = project.getBasedir();
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return "plugin.xml".equals(name);
			}
		};
		File[] listFiles = basedir.listFiles(filter);

		if (listFiles.length == 1) { // maybe  just filter by exsd? (extension point file extension)
			File file = listFiles[0];
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			try {
				db = dbf.newDocumentBuilder();
				Document document = db.parse(file);
				NodeList elementsByTagName = document.getElementsByTagName("extension-point");
				for (int i = 0; i < elementsByTagName.getLength(); i++) {
					Node item = elementsByTagName.item(i);
					Node namedItem = item.getAttributes().getNamedItem("schema");
					String nodeValue = namedItem.getNodeValue();
					File extesinoPointFile = new File(basedir, nodeValue);
					Document parse = db.parse(extesinoPointFile);
					NodeList documentationNodeList = parse.getElementsByTagName("documentation");
					int lengthD = documentationNodeList.getLength();
					for (int j = 0; j < lengthD; j++) {
						Node documentationNode = documentationNodeList.item(j);
						String textContent = documentationNode.getTextContent();
						if (textContent!= null && textContent.contains("[Enter ")){ //TODO use the default values
							throw new EnforcerRuleException("The extension point isn't fully documented");
						}
						
					}	
				}
			} catch (ParserConfigurationException |SAXException |IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
