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
package org.jasig.ssp.factory.impl;

import org.jasig.ssp.dao.NotificationDao;
import org.jasig.ssp.dao.NotificationRecipientDao;
import org.jasig.ssp.factory.*;
import org.jasig.ssp.model.Notification;
import org.jasig.ssp.model.NotificationRecipient;
import org.jasig.ssp.model.Person;
import org.jasig.ssp.service.ObjectNotFoundException;
import org.jasig.ssp.transferobject.NotificationTO;
import org.jasig.ssp.transferobject.PersonLiteTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Notification transfer object factory implementation
 */
@Service
@Transactional(readOnly = true)
public class NotificationTOFactoryImpl extends AbstractAuditableTOFactory<NotificationTO, Notification>
		implements NotificationTOFactory {

	public NotificationTOFactoryImpl() {
		super(NotificationTO.class, Notification.class);
	}

	@Autowired
	private transient NotificationDao dao;

    @Autowired
    private transient PersonTOFactory personTOFactory;

	protected NotificationDao getDao() {
		return dao;
	}

	@Override
	public Notification from(final NotificationTO tObject) throws ObjectNotFoundException {
		final Notification model = super.from(tObject);

//		if (tObject.getRecipient() != null) {
//			final PersonLiteTO personLiteTO = tObject.getRecipient();
//			final Person personFromPersonLite = new Person();
//			personFromPersonLite.setId(personLiteTO.getId());
//			personFromPersonLite.setFirstName(personLiteTO.getFirstName());
//			personFromPersonLite.setLastName(personLiteTO.getLastName());

//			model.setPerson(personFromPersonLite);
//		}

//		if (tObject.getSspRole() != null) {
//			model.setSspRole(tObject.getSspRole());
//		}

		if (tObject.getSubject() != null) {
			//TODO ??? model.setNotification(notification.get?());
		}

		return model;
	}
}
