package org.jasig.ssp.factory.external.impl;

import org.jasig.ssp.dao.external.FacultyCourseDao;
import org.jasig.ssp.factory.external.FacultyCourseTOFactory;
import org.jasig.ssp.model.external.FacultyCourse;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.jasig.ssp.transferobject.external.FacultyCourseTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * FacultyCourse transfer object factory implementation
 * 
 * @author jon.adams
 */
@Service
@Transactional(readOnly = true)
public class FacultyCourseTOFactoryImpl
		extends AbstractExternalDataTOFactory<FacultyCourseTO, FacultyCourse>
		implements FacultyCourseTOFactory {

	public FacultyCourseTOFactoryImpl() {
		super(FacultyCourseTO.class, FacultyCourse.class);
	}

	@Autowired
	private transient FacultyCourseDao dao;

	@Override
	protected FacultyCourseDao getDao() {
		return dao;
	}

	@Override
	public FacultyCourse from(final FacultyCourseTO tObject)
			throws ObjectNotFoundException {
		final FacultyCourse model = super.from(tObject);

		model.setFacultySchoolId(tObject.getFacultySchoolId());
		model.setTermCode(tObject.getTermCode());
		model.setFormattedCourse(tObject.getFormattedCourse());
		model.setTitle(tObject.getTitle());

		return model;
	}
}