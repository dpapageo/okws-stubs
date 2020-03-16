/*s
 * Copyright (c) 2019 Federal Republic of Germany and the 16 federated states of
 * the Federal Republic of Germany All rights reserved. No warranty, explicit or
 * implicit, provided. Unauthorized copying of this file via any medium is
 * strictly prohibited. Authored by European Dynamics SA <info@eurodyn.com>
 */
package de.bund.okws.stubs.controller;

import de.bund.okws.stubs.service.GetOperationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/okws")
public class OkwsStubsController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OkwsStubsController.class);
	
	private final GetOperationService getOperationService;
	
	@Autowired
	public OkwsStubsController(GetOperationService getOperationService) {
		this.getOperationService = getOperationService;
	}
	
	@RequestMapping(value = "/stubs", method = {RequestMethod.GET})
	@GetMapping(produces = {"application/xml"})
	public ResponseEntity<String> getOperation(@RequestParam(name = "REQUEST") String request) {
		return new ResponseEntity<>(getOperationService.getResponse(request, null), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/stubs", method = {RequestMethod.POST})
	@PostMapping(produces = {"application/xml"})
	public ResponseEntity<String> getOperationPost(@RequestParam(name = "REQUEST") String request, @RequestBody String inputXml) {
		return new ResponseEntity<>(getOperationService.getResponse(request, inputXml), HttpStatus.OK);
	}
	
}
