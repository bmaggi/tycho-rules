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

import static org.junit.Assert.*;

import org.junit.Test;

public class BundleUtilTest {

	@Test
	public void testIsBundleReexported() {
		assertTrue(BundleUtil.isBundleReexported("org.eclipse.uml2.uml;bundle-version=\"5.1.0\";visibility:=reexport"));
		assertFalse(BundleUtil.isBundleReexported("org.eclipse.uml2.uml;bundle-version=\"5.1.0\""));
		assertFalse(BundleUtil.isBundleReexported(""));
	}

}
