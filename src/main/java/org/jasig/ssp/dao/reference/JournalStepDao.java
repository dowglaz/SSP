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
package org.jasig.ssp.dao.reference;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.jasig.ssp.dao.AuditableCrudDao;
import org.jasig.ssp.model.ObjectStatus;
import org.jasig.ssp.model.reference.JournalStep;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.jasig.ssp.util.sort.PagingWrapper;
import org.jasig.ssp.util.sort.SortDirection;
import org.jasig.ssp.util.sort.SortingAndPaging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import java.util.UUID;


/**
 * Data access class for the JournalStep reference entity.
 */
@Repository
public class JournalStepDao extends
		AbstractReferenceAuditableCrudDao<JournalStep>
		implements AuditableCrudDao<JournalStep> {

	private Logger LOGGER = LoggerFactory.getLogger(JournalStepDao.class);
	
	public JournalStepDao() {
		super(JournalStep.class);
	}

	public PagingWrapper<JournalStep> getAllForJournalTrack(
			final UUID journalTrackId, final SortingAndPaging sAndP) {

		final Criteria criteria = createCriteria();
		final Criteria subQuery = criteria
				.createCriteria("journalTrackJournalSteps");
		subQuery.add(Restrictions.eq("journalTrack.id", journalTrackId));
		sAndP.addStatusFilterToCriteria(subQuery);

		return processCriteriaWithStatusSortingAndPaging(criteria, sAndP);
	}
	

	public void softDeleteReferencingAssociations(UUID id) throws ObjectNotFoundException {
		JournalStep obj = get(id);
		String softDeleteAssociations = "update JournalTrackJournalStep set objectStatus = :objectStatus where journalStep = :journalStep";
		createHqlQuery(softDeleteAssociations).setInteger("objectStatus", ObjectStatus.INACTIVE.ordinal()).setEntity("journalStep", obj).executeUpdate();		
	}
	
	@Override
	public PagingWrapper<JournalStep> getAll(final SortingAndPaging sAndP) {
		SortingAndPaging sp = sAndP;
		if (sp == null) {
			sp = new SortingAndPaging(ObjectStatus.ACTIVE);
		}

		if (!sp.isSorted()) {
			sp.appendSortField("objectStatus", SortDirection.ASC);
			sp.appendSortField("name", SortDirection.ASC);
		}

		return super.getAll(sp);
	}
}