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
package org.openmrs.module.kenyaemr.calculation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Program;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationService;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.kenyacore.test.TestUtils;
import org.openmrs.module.kenyaemr.calculation.library.DeceasedPatientsCalculation;
import org.openmrs.module.kenyaemr.metadata.CommonMetadata;
import org.openmrs.module.kenyaemr.metadata.HivMetadata;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * Tests for {@link DeceasedPatientsCalculation}
 */
public class DeadPatientsCalculationTest extends BaseModuleContextSensitiveTest {

	@Autowired
	private CommonMetadata commonMetadata;
	@Autowired
	private HivMetadata hivMetadata;

	/**
	 * Setup each test
	 */
	@Before
	public void setup() throws Exception {
		executeDataSet("dataset/test-concepts.xml");
		commonMetadata.install();
		hivMetadata.install();
	}

	@Test
	public void evaluate_shouldCalculateDeadPatients() throws Exception {


		Program hivProgram = MetadataUtils.existing(Program.class, HivMetadata._Program.HIV);

		TestUtils.enrollInProgram(TestUtils.getPatient(2), hivProgram, TestUtils.date(2014, 3, 1));
		TestUtils.enrollInProgram(TestUtils.getPatient(6), hivProgram, TestUtils.date(2014, 3, 1));
		TestUtils.getPatient(6).setDead(true);
		TestUtils.getPatient(2).setDead(true);

		List<Integer> ptIds = Arrays.asList(2, 6, 7, 8);

		CalculationResultMap resultMap = new DeceasedPatientsCalculation().evaluate(ptIds, null, Context.getService(PatientCalculationService.class).createCalculationContext());
		Assert.assertTrue((Boolean) resultMap.get(2).getValue());
		Assert.assertFalse((Boolean) resultMap.get(7).getValue());
		Assert.assertFalse((Boolean) resultMap.get(8).getValue());
		Assert.assertTrue((Boolean) resultMap.get(6).getValue());

	}

}
