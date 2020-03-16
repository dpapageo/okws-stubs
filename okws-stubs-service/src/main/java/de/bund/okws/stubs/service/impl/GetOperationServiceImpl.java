/*
 * Copyright (c) 2019 Federal Republic of Germany and the 16 federated states of
 * the Federal Republic of Germany All rights reserved. No warranty, explicit or
 * implicit, provided. Unauthorized copying of this file via any medium is
 * strictly prohibited. Authored by European Dynamics SA <info@eurodyn.com>
 */
package de.bund.okws.stubs.service.impl;

import de.bund.okws.stubs.service.GetOperationService;
import de.bund.okws.stubs.service.impl.operation.OperationStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetOperationServiceImpl implements GetOperationService {
	
	private final OperationStrategyFactory operationStrategyFactory;
	
	@Autowired
	public GetOperationServiceImpl(OperationStrategyFactory operationStrategyFactory) {
		this.operationStrategyFactory = operationStrategyFactory;
	}
	
	@Override
	public String getResponse(String request, String inputXml) {
		return operationStrategyFactory.createStrategy(request).execute(inputXml);
	}
}
