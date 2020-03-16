/*
 * Copyright (c) 2019 Federal Republic of Germany and the 16 federated states of
 * the Federal Republic of Germany All rights reserved. No warranty, explicit or
 * implicit, provided. Unauthorized copying of this file via any medium is
 * strictly prohibited. Authored by European Dynamics SA <info@eurodyn.com>
 */
package de.bund.okws.stubs.service.impl.operation;

import org.springframework.stereotype.Service;

@Service
public class EmptyStrategy implements RequestHandlerStrategy {
	
	@Override
	public String execute(String inputXml) {
		return null;
	}
}
