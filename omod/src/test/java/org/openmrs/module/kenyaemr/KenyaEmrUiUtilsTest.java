/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.kenyaemr;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.module.kenyaemr.KenyaEmrUiUtils;

import java.util.*;

public class KenyaEmrUiUtilsTest {

	/**
	 * @see org.openmrs.module.kenyaemr.KenyaEmrUiUtils#formatDateNoTime(java.util.Date)
	 * @verifies format date as a string without time information
	 */
	@Test
	public void formatDateNoTime_shouldFormatDateWithoutTime() throws Exception {
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(Calendar.YEAR, 1981);
		cal.set(Calendar.MONTH, Calendar.MAY);
		cal.set(Calendar.DAY_OF_MONTH, 28);
		cal.set(Calendar.HOUR, 7);
		cal.set(Calendar.MINUTE, 30);
		cal.set(Calendar.SECOND, 12);

		Assert.assertEquals("28-May-1981", KenyaEmrUiUtils.formatDateNoTime(cal.getTime()));
	}

	/**
	 * @see org.openmrs.module.kenyaemr.KenyaEmrUiUtils#formatDateNoTime(java.util.Date)
	 * @verifies format null date as empty string
	 */
	@Test
	public void formatDateNoTime_shouldFormatNullDateAsEmptyString() throws Exception {
		Assert.assertEquals("", KenyaEmrUiUtils.formatDateNoTime(null));
	}
}