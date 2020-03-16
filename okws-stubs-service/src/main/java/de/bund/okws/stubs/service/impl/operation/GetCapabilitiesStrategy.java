/*
 * Copyright (c) 2019 Federal Republic of Germany and the 16 federated states of
 * the Federal Republic of Germany All rights reserved. No warranty, explicit or
 * implicit, provided. Unauthorized copying of this file via any medium is
 * strictly prohibited. Authored by European Dynamics SA <info@eurodyn.com>
 */
package de.bund.okws.stubs.service.impl.operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class GetCapabilitiesStrategy implements RequestHandlerStrategy {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GetCapabilitiesStrategy.class);
	
	@Override
	public String execute(String inputXml) {
		LOGGER.info("getCapabilities");
		try {
			File resource = new ClassPathResource("getCapabilities/getCapabilities.xml").getFile();
			final String content = new String(Files.readAllBytes(Paths.get(resource.getPath())), StandardCharsets.UTF_8);
			return content;
		} catch (IOException e) {
			LOGGER.error("Could not read file", e);
		}
		return null;
	}
}
