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
package org.jasig.ssp.service.jobqueue;

import org.jasig.ssp.service.jobqueue.JobExecutionResult;
import org.jasig.ssp.service.jobqueue.JobExecutor;

public class JobExecutionException extends RuntimeException {

	private JobExecutionResult jobExecutionResult;

	public JobExecutionException() {
		super();
	}

	public JobExecutionException(String message) {
		super(message);
	}

	public JobExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	public JobExecutionException(Throwable cause) {
		super(cause);
	}

	public JobExecutionException(JobExecutionResult jobExecutionResult) {
		this(jobExecutionResult.getCause());
		this.jobExecutionResult = jobExecutionResult;
	}

	public JobExecutionResult getJobExecutionResult() {
		return jobExecutionResult;
	}
}
