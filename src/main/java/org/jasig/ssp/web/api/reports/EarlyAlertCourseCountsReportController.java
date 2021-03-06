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


import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.jasig.ssp.model.ObjectStatus;
import org.jasig.ssp.model.reference.Campus;
import org.jasig.ssp.security.permissions.Permission;
import org.jasig.ssp.service.EarlyAlertService;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.jasig.ssp.service.external.TermService;
import org.jasig.ssp.service.reference.CampusService;
import org.jasig.ssp.transferobject.reports.EarlyAlertCourseCountsTO;
import org.jasig.ssp.util.DateTerm;
import org.jasig.ssp.util.csvwriter.AbstractCsvWriterHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;


/**
 * Service methods for Reporting on Early Alert Counts per Course
 * <p>
 * Mapped to URI path <code>/1/report/earlyalertcoursecounts</code>
 */
@Controller
@RequestMapping("/1/report/earlyalertcoursecounts")
public class EarlyAlertCourseCountsReportController extends ReportBaseController<EarlyAlertCourseCountsTO> {

    private static String REPORT_URL = "/reports/earlyAlertCourseCountsReport.jasper";
    private static String REPORT_FILE_TITLE = "Early_Alert_Course_Counts_Report";
    private static String TOTAL_STUDENTS = "totalStudents";

    private static final Logger LOGGER = LoggerFactory
            .getLogger(EarlyAlertCourseCountsReportController.class);

    @Autowired
    protected transient TermService termService;

    @Autowired
    protected transient CampusService campusService;

    @Autowired
    protected transient EarlyAlertService earlyAlertService;


    @InitBinder
    public void initBinder(final WebDataBinder binder) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT,
                Locale.US);
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                dateFormat, true));
    }


    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize(Permission.SECURITY_REPORT_READ)
    public @ResponseBody
    void getNumberOfEarlyAlertsByCourse(
            final HttpServletResponse response,
            final @RequestParam(required = false) UUID campusId,
            final @RequestParam(required = false) String termCode,
            final @RequestParam(required = false) Date createDateFrom,
            final @RequestParam(required = false) Date createDateTo,
            final @RequestParam(required = false) ObjectStatus objectStatus,
            final @RequestParam(required = false, defaultValue = DEFAULT_REPORT_TYPE) String reportType)
            throws ObjectNotFoundException, IOException {

        final DateTerm dateTerm = new DateTerm(createDateFrom, createDateTo, termCode, termService);
        final Map<String, Object> parameters = Maps.newHashMap();
        final Campus campus = SearchParameters.getCampus(campusId, campusService);

        if ( StringUtils.isBlank(termCode) || termCode.trim().toLowerCase().equals("not used") && createDateFrom != null ) {
            dateTerm.setTerm(null);
        } else if (termCode != null && createDateFrom == null) {
            dateTerm.setStartEndDates(null, null);
        }

        SearchParameters.addCampusToParameters(campus, parameters);
        SearchParameters.addDateTermToMap(dateTerm, parameters);

        List<EarlyAlertCourseCountsTO> results = earlyAlertService.getStudentEarlyAlertCountSetPerCourses(dateTerm.getTermCodeNullPossible(), dateTerm.getStartDate(), dateTerm.getEndDate(), campus, objectStatus);
        parameters.put(TOTAL_STUDENTS, earlyAlertService.getStudentEarlyAlertCountSetPerCoursesTotalStudents(dateTerm.getTermCodeNullPossible(), dateTerm.getStartDate(), dateTerm.getEndDate(), campus, objectStatus));

        if ( results == null) {
             results = new ArrayList<EarlyAlertCourseCountsTO>();
        }

        renderReport( response,  parameters, results, REPORT_URL, reportType, REPORT_FILE_TITLE);
    }

    @Override
    protected boolean overridesCsvRendering() {
        return true;
    }

    @Override
    public String[] csvHeaderRow(Map<String, Object> reportParameters, Collection<EarlyAlertCourseCountsTO> reportResults,
                                 String reportViewUrl, String reportType, String reportName,
                                 AbstractCsvWriterHelper csvHelper) {
        return new String[] {
                "CAMPUS",
                "TERM",
                "COURSE TITLE",
                "COURSE",
                "TOTAL STUDENTS REPORTED",
                "TOTAL ALERTS ON COURSE"
        };
    }

    @Override
    public List<String[]> csvBodyRows(EarlyAlertCourseCountsTO reportResultElement, Map<String, Object> reportParameters,
                                      Collection<EarlyAlertCourseCountsTO> reportResults, String reportViewUrl, String reportType, String reportName,
                                      AbstractCsvWriterHelper csvHelper) {
        return csvHelper.wrapCsvRowInList(new String[] {
                reportResultElement.getCampusName(),
                reportResultElement.getTermCode(),
                reportResultElement.getCourseTitle(),
                reportResultElement.getCourseName(),
                csvHelper.formatLong(reportResultElement.getTotalStudentsReported()),
                csvHelper.formatLong(reportResultElement.getTotalAlerts())
        });
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}