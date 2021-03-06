/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.ssp.web.api.reports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jasig.ssp.model.EarlyAlert;
import org.jasig.ssp.model.Person;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.jasig.ssp.util.service.stub.Stubs;
import org.jasig.ssp.web.api.validation.ValidationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import net.sf.jasperreports.engine.JRException;


public class CaseloadReportControllerIntegrationTest
		extends AbstractReportControllerIntegrationTest {

	@Autowired
	private CaseloadReportController controller;

	@Test
	public void testDefaultDataSet() throws ObjectNotFoundException, IOException, JRException {
		final MockHttpServletResponse response = new MockHttpServletResponse();
		controller.getCaseLoad(response, null, null, null, null, "csv");

		/// TODO Eliminate Spaces in Report TO Remove Extra commas
		final List<String> expectedReportBodyLines = new ArrayList<String>(4);
		expectedReportBodyLines.add("FIRST,LAST,DEPARTMENT,TOTAL COUNT,ACTIVE COUNT,A,IA,T,NP,NS");
		expectedReportBodyLines.add("Alan,Turing,Mathematics,2,2,2,0,0,0,0");
		expectedReportBodyLines.add("test,coach1,Not Available Yet,5,1,1,1,1,1,1");

		expectReportBodyLines(expectedReportBodyLines, response, null);
	}

	@Test
	public void testIncludesCoachesWithoutAnyCaseload()
			throws ObjectNotFoundException, IOException, JRException {

		personAttributesService.getCoachUsernames()
				.add(Stubs.PersonFixture.KEVIN_SMITH.username());

		final MockHttpServletResponse response = new MockHttpServletResponse();
		controller.getCaseLoad(response, null, null, null, null, "csv");

		// "body" is the actual results and the header that describes its columns.
		// This is as opposed to rows which precede the header, which describe
		// the filtering criteria
		final List<String> expectedReportBodyLines = new ArrayList<String>(4);
		/// TODO Eliminate Spaces in Report TO Remove Extra commas
		expectedReportBodyLines.add("FIRST,LAST,DEPARTMENT,TOTAL COUNT,ACTIVE COUNT,A,IA,T,NP,NS");
		expectedReportBodyLines.add("Kevin,Smith,Mathematics,0,0,0,0,0,0,0");
		expectedReportBodyLines.add("Alan,Turing,Mathematics,2,2,2,0,0,0,0");
		expectedReportBodyLines.add("test,coach1,Not Available Yet,5,1,1,1,1,1,1");

		expectReportBodyLines(expectedReportBodyLines, response, null);
	}

	@Test
	public void testNewEarlyAlertForValidCoachReflectedOnReport()
			throws ObjectNotFoundException, IOException, JRException, ValidationException {

		personAttributesService.getCoachUsernames()
				.add(Stubs.PersonFixture.KEVIN_SMITH.username());

		final Person jamesDoe =
				personService.get(Stubs.PersonFixture.JAMES_DOE.id());
		final Person kevinSmith =
				personService.get(Stubs.PersonFixture.KEVIN_SMITH.id());
		jamesDoe.setCoach(kevinSmith);
		personService.save(jamesDoe);
		sessionFactory.getCurrentSession().flush();

		final EarlyAlert earlyAlert =
				Stubs.arrangeEarlyAlert(personService, campusService);
		earlyAlert.setPerson(jamesDoe);
		earlyAlert.setClosedBy(null);
		earlyAlert.setClosedDate(null);
		earlyAlertService.create(earlyAlert);

		final MockHttpServletResponse response = new MockHttpServletResponse();
		controller.getCaseLoad(response, null, null, null, null, "csv");

		// "body" is the actual results and the header that describes its columns.
		// This is as opposed to rows which precede the header, which describe
		// the filtering criteria
		final List<String> expectedReportBodyLines = new ArrayList<String>(4);
		/// TODO Eliminate Spaces in Report TO Remove Extra commas
		expectedReportBodyLines.add("FIRST,LAST,DEPARTMENT,TOTAL COUNT,ACTIVE COUNT,A,IA,T,NP,NS");
		expectedReportBodyLines.add("Kevin,Smith,Mathematics,1,1,1,0,0,0,0");
		expectedReportBodyLines.add("Alan,Turing,Mathematics,2,2,2,0,0,0,0");
		expectedReportBodyLines.add("test,coach1,Not Available Yet,5,1,1,1,1,1,1");

		expectReportBodyLines(expectedReportBodyLines, response, null);
	}

	/**
	 * Same as {@link #testNewEarlyAlertForValidCoachReflectedOnReport()}, but
	 * we don't register one of the coaches with the PersonAttributesService.
	 * This failed in early impls b/c the coach list returned from the uPortal-
	 * backed PersonAttributesService didn't match the list of users with actual
	 * caseloads in the SSP database.
	 */
	@Test
	public void testNewEarlyAlertForMissingCoachReflectedOnReport()
			throws ObjectNotFoundException, ValidationException, IOException, JRException {
		final Person jamesDoe =
				personService.get(Stubs.PersonFixture.JAMES_DOE.id());
		final Person kevinSmith =
				personService.get(Stubs.PersonFixture.KEVIN_SMITH.id());
		jamesDoe.setCoach(kevinSmith);
		personService.save(jamesDoe);
		sessionFactory.getCurrentSession().flush();

		final EarlyAlert earlyAlert =
				Stubs.arrangeEarlyAlert(personService, campusService);
		earlyAlert.setPerson(jamesDoe);
		earlyAlert.setClosedBy(null);
		earlyAlert.setClosedDate(null);
		earlyAlertService.create(earlyAlert);

		final MockHttpServletResponse response = new MockHttpServletResponse();
		controller.getCaseLoad(response, null, null, null, null, "csv");

		// "body" is the actual results and the header that describes its columns.
		// This is as opposed to rows which precede the header, which describe
		// the filtering criteria
		final List<String> expectedReportBodyLines = new ArrayList<String>(4);
		/// TODO Eliminate Spaces in Report TO Remove Extra commas
		expectedReportBodyLines.add("FIRST,LAST,DEPARTMENT,TOTAL COUNT,ACTIVE COUNT,A,IA,T,NP,NS");
		expectedReportBodyLines.add("Kevin,Smith,Mathematics,1,1,1,0,0,0,0");
		expectedReportBodyLines.add("Alan,Turing,Mathematics,2,2,2,0,0,0,0");
		expectedReportBodyLines.add("test,coach1,Not Available Yet,5,1,1,1,1,1,1");

		expectReportBodyLines(expectedReportBodyLines, response, null);
	}
	
	/**
	 * {@link #testGetCaseloadWithFilters()}, 
	 * Test to make sure all the filters are implemented properly.
	 */
	@Test
	public void testGetCaseloadWithFilters()
			throws ObjectNotFoundException, ValidationException, IOException, JRException {


		sessionFactory.getCurrentSession().flush();

		final MockHttpServletResponse response = new MockHttpServletResponse();
		controller.getCaseLoad(response, 
				Stubs.HomeDepartmentFixture.MATHEMATICS.title(),
				Lists.newArrayList(Stubs.StudentTypeFixture.ILP.id()),
				Lists.newArrayList(Stubs.ServiceReasonFixture.TEST_SERVICE_REASON.id()),
				Lists.newArrayList(Stubs.SpecialServiceGroupFixture.TEST_SSG.id()),
				"csv");

		// "body" is the actual results and the header that describes its columns.
		// This is as opposed to rows which precede the header, which describe
		// the filtering criteria
		final List<String> expectedReportBodyLines = new ArrayList<String>(4);
		/// TODO Eliminate Spaces in Report TO Remove Extra commas
		expectedReportBodyLines.add("FIRST,LAST,DEPARTMENT,TOTAL COUNT,ACTIVE COUNT,A,IA,T,NP,NS");
		expectedReportBodyLines.add("Alan,Turing,Mathematics,0,0,0,0,0,0,0");
		
		expectReportBodyLines(expectedReportBodyLines, response, null);
		
		
		
	}
	
	@Test
	public void testGetCaseloadWithFiltersNotSpecialServiceGroups()
			throws ObjectNotFoundException, ValidationException, IOException, JRException {


		sessionFactory.getCurrentSession().flush();

		final MockHttpServletResponse response = new MockHttpServletResponse();
		controller.getCaseLoad(response, 
				Stubs.HomeDepartmentFixture.MATHEMATICS.title(),
				Lists.newArrayList(Stubs.StudentTypeFixture.ILP.id()),
				Lists.newArrayList(Stubs.ServiceReasonFixture.TEST_SERVICE_REASON.id()),
				null,
				"csv");

		// "body" is the actual results and the header that describes its columns.
		// This is as opposed to rows which precede the header, which describe
		// the filtering criteria
		final List<String> expectedReportBodyLines = new ArrayList<String>(4);
		/// TODO Eliminate Spaces in Report TO Remove Extra commas
		expectedReportBodyLines.add("FIRST,LAST,DEPARTMENT,TOTAL COUNT,ACTIVE COUNT,A,IA,T,NP,NS");
		expectedReportBodyLines.add("Alan,Turing,Mathematics,0,0,0,0,0,0,0");
		
		expectReportBodyLines(expectedReportBodyLines, response, null);
	}

	@Override
	protected Predicate<String> afterHeader() {
		return afterLineContaining("Case Load");
	}

}
