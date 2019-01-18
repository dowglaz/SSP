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
package org.jasig.ssp.dao;

import org.jasig.ssp.model.NotificationRead;
import org.jasig.ssp.service.reference.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


/**
 * DAO for the {@link NotificationRead} model
 */
@Repository
public class NotificationReadDao extends AbstractAuditableCrudDao<NotificationRead> implements AuditableCrudDao<NotificationRead> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationReadDao.class);

    @Autowired
	private transient ConfigService configService;


	/**
	 * Constructor that initializes the instance with the specific class types
	 * for super class method use.
	 */
	public NotificationReadDao() {
		super(NotificationRead.class);
	}

}
