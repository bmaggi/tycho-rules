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
package com.github.bmaggi.tycho.rules.utils;

public class BundleUtil {

	private BundleUtil() {
	}

	public static boolean isBundleReexported(String bundle) {
		boolean hasreexport = false;
		for (String option : bundle.split(OsgiKeyWords.BUNDLE_OPTION_SEPARATOR)) {
			hasreexport = hasreexport || option.startsWith(OsgiKeyWords.BUNDLE_VISIBILITY_REEXPORT);
		}
		return hasreexport;
	}

}
