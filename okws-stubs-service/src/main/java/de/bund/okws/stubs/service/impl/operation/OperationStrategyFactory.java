/*
 * Copyright (c) 2019 Federal Republic of Germany and the 16 federated states of
 * the Federal Republic of Germany All rights reserved. No warranty, explicit or
 * implicit, provided. Unauthorized copying of this file via any medium is
 * strictly prohibited. Authored by European Dynamics SA <info@eurodyn.com>
 */
package de.bund.okws.stubs.service.impl.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperationStrategyFactory {
	
	private final GetCapabilitiesStrategy getCapabilitiesStrategy;
	private final GetFeatureStrategy getFeatureStrategy;
	private final EmptyStrategy emptyStrategy;
	
	@Autowired
	public OperationStrategyFactory(final GetCapabilitiesStrategy getCapabilitiesStrategy,
									final GetFeatureStrategy getFeatureStrategy,
									final EmptyStrategy emptyStrategy) {
		this.getCapabilitiesStrategy = getCapabilitiesStrategy;
		this.getFeatureStrategy = getFeatureStrategy;
		this.emptyStrategy = emptyStrategy;
	}
	
	public RequestHandlerStrategy createStrategy(String request) {
		if ("GetCapabilities".equals(request)) {
			return getCapabilitiesStrategy;
		} else if ("GetFeature".equals(request)) {
			return getFeatureStrategy;
		}
		return emptyStrategy;
	}
}
