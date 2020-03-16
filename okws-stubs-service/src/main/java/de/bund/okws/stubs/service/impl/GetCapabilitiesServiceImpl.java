/*
 * Copyright (c) 2019 Federal Republic of Germany and the 16 federated states of
 * the Federal Republic of Germany All rights reserved. No warranty, explicit or
 * implicit, provided. Unauthorized copying of this file via any medium is
 * strictly prohibited. Authored by European Dynamics SA <info@eurodyn.com>
 */
package de.bund.okws.stubs.service.impl;

import de.bund.okws.stubs.service.GetCapabilitiesService;
import de.bund.okws.stubs.service.impl.operation.GetFilterCapabilitiesHandler;
import net.opengis.ows._1.*;
import net.opengis.wfs._2.ObjectFactory;
import net.opengis.wfs._2.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class GetCapabilitiesServiceImpl implements GetCapabilitiesService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GetCapabilitiesServiceImpl.class);
	
	
	private static final String JAXB_FORMATTED_OUTPUT = "jaxb.formatted.output";
	private static final String HTTP = "https://";
	private static final String PORT = ":8080";
	private static final String SERVICE_URL = "/api/wfsService";
	private static final String WSDL = "?wsdl";
	private static final String SERVICE_VERSION = "2.0.0";
	private static final String ORIGINAL_GML_3_SRS_CONVENTION = "urn:x-ogc:def:crs:EPSG:-1";
	private static final String STRASSENDATEN = "VI_STRASSENNETZ";
	private static final String APPLICATION_XML = "application/xml";
	private static final String SERVICE_PROVIDER_NAME = "SIB-SYSTEM";
	private static final String LANG = "DE";
	private static final String LANG_VALUE = "de_DE";
	private static final String WFS_SIB_BW_2 = "WFS SIB-BW2";
	private static final String SERVICE_TYPE = "WFS";
	private static final String CODE_SPACE = "http://www.opengeospatial.org/";
	private static final String ACCEPT_VERSIONS = "AcceptVersions";
	private static final String GET_CAPABILITIES_OPERATION = "GetCapabilities";
	private static final String GET_FEATURE_OPERATION = "GetFeature";
	private static final String DESCRIBE_FEATURE_OPERATION = "DescribeFeatureType";
	private static final String GET_PROPERTY_OPERATION = "GetPropertyValue";
	private static final String LIST_STORED_OPERATION = "ListStoredQueries";
	private static final String DESCRIBE_STORED_OPERATION = "DescribeStoredQueries";
	
	private final GetFilterCapabilitiesHandler getFilterCapabilitiesHandler;
	
	@Autowired
	public GetCapabilitiesServiceImpl(GetFilterCapabilitiesHandler getFilterCapabilitiesHandler) {
		this.getFilterCapabilitiesHandler = getFilterCapabilitiesHandler;
	}
	
	@Override
	public String getCapabilities() {
		
		try {
			File resource = new ClassPathResource("getCapabilities/getCapabilities.xml").getFile();
			final String content = new String(Files.readAllBytes(Paths.get(resource.getPath())), StandardCharsets.UTF_8);
			return content;
		} catch (IOException e) {
			LOGGER.error("Could not read file", e);
			final net.opengis.wfs._2.ObjectFactory wfsObjectFactory = new net.opengis.wfs._2.ObjectFactory();
			final net.opengis.fes._2.ObjectFactory fesObjectFactory = new net.opengis.fes._2.ObjectFactory();
			final net.opengis.ows._1.ObjectFactory owsObjectFactory = new net.opengis.ows._1.ObjectFactory();
			final WFSCapabilitiesType wfsCapabilitiesType = wfsObjectFactory.createWFSCapabilitiesType();
			wfsCapabilitiesType.setFilterCapabilities(getFilterCapabilitiesHandler.getFilterCapabilities(fesObjectFactory));
			wfsCapabilitiesType.setWSDL(getWSDL());
			wfsCapabilitiesType.setServiceProvider(getServiceProvider(owsObjectFactory));
			wfsCapabilitiesType.setVersion(SERVICE_VERSION);
			
			wfsCapabilitiesType.setServiceIdentification(getServiceIdentifation(owsObjectFactory));
			
			wfsCapabilitiesType.setFeatureTypeList(getFeatureTypeList(wfsObjectFactory));
			wfsCapabilitiesType.setOperationsMetadata(getOperationsMetadata(owsObjectFactory));
			
			return getGetCapabilitiesString(wfsObjectFactory, wfsCapabilitiesType);
		}
		
	}
	
	private ServiceIdentification getServiceIdentifation(final net.opengis.ows._1.ObjectFactory owsObjectFactory) {
		final ServiceIdentification serviceIdentification = owsObjectFactory.createServiceIdentification();
		serviceIdentification.setServiceType(getCodeType(owsObjectFactory));
		serviceIdentification.getProfile().add(WFS_SIB_BW_2);
		serviceIdentification.getTitle().add(getLanguageStringType(owsObjectFactory));
		return serviceIdentification;
	}
	
	private OperationsMetadata getOperationsMetadata(net.opengis.ows._1.ObjectFactory owsObjectFactory) {
		final OperationsMetadata operationsMetadata = owsObjectFactory.createOperationsMetadata();
		getOperations(owsObjectFactory, operationsMetadata);
		
		final DomainType constraint = owsObjectFactory.createDomainType();
		operationsMetadata.getConstraint().add(constraint);
		return operationsMetadata;
	}
	
	private void getOperations(net.opengis.ows._1.ObjectFactory owsObjectFactory, OperationsMetadata operationsMetadata) {
		operationsMetadata.getOperation().add(getOperation(owsObjectFactory, GET_CAPABILITIES_OPERATION));
		operationsMetadata.getOperation().add(getOperation(owsObjectFactory, GET_FEATURE_OPERATION));
		operationsMetadata.getOperation().add(getOperation(owsObjectFactory, DESCRIBE_FEATURE_OPERATION));
		operationsMetadata.getOperation().add(getOperation(owsObjectFactory, GET_PROPERTY_OPERATION));
		operationsMetadata.getOperation().add(getOperation(owsObjectFactory, LIST_STORED_OPERATION));
		operationsMetadata.getOperation().add(getOperation(owsObjectFactory, DESCRIBE_STORED_OPERATION));
	}
	
	private Operation getOperation(net.opengis.ows._1.ObjectFactory owsObjectFactory, String operationName) {
		final Operation operation = owsObjectFactory.createOperation();
		operation.setName(operationName);
		
		final DomainType constraint = owsObjectFactory.createDomainType();
		operation.getConstraint().add(constraint);
		final MetadataType metadataType = owsObjectFactory.createMetadataType();
		operation.getMetadata().add(metadataType);
		operation.getParameter().add(getParameter(owsObjectFactory));
		return operation;
	}
	
	private DomainType getParameter(net.opengis.ows._1.ObjectFactory owsObjectFactory) {
		final DomainType parameter = owsObjectFactory.createDomainType();
		parameter.setName(ACCEPT_VERSIONS);
		parameter.setAllowedValues(getAllowedValues(owsObjectFactory));
		return parameter;
	}
	
	private AllowedValues getAllowedValues(net.opengis.ows._1.ObjectFactory owsObjectFactory) {
		final AllowedValues allowedValues = owsObjectFactory.createAllowedValues();
		allowedValues.getValueOrRange().add(SERVICE_VERSION);
		return allowedValues;
	}
	
	private LanguageStringType getLanguageStringType(final net.opengis.ows._1.ObjectFactory owsObjectFactory) {
		final LanguageStringType languageStringType = owsObjectFactory.createLanguageStringType();
		languageStringType.setLang(LANG_VALUE);
		languageStringType.setValue(LANG);
		return languageStringType;
	}
	
	private CodeType getCodeType(net.opengis.ows._1.ObjectFactory owsObjectFactory) {
		final CodeType codeType = owsObjectFactory.createCodeType();
		codeType.setCodeSpace(CODE_SPACE);
		codeType.setValue(SERVICE_TYPE);
		return codeType;
	}
	
	private ServiceProvider getServiceProvider(net.opengis.ows._1.ObjectFactory owsObjectFactory) {
		final ServiceProvider serviceProvider = owsObjectFactory.createServiceProvider();
		serviceProvider.setProviderName(SERVICE_PROVIDER_NAME);
		serviceProvider.setServiceContact(getResponsiblePartySubsetType(owsObjectFactory));
		return serviceProvider;
	}
	
	private ResponsiblePartySubsetType getResponsiblePartySubsetType(net.opengis.ows._1.ObjectFactory owsObjectFactory) {
		final ResponsiblePartySubsetType responsiblePartySubsetType = owsObjectFactory.createResponsiblePartySubsetType();
		responsiblePartySubsetType.setContactInfo(owsObjectFactory.createContactType());
		responsiblePartySubsetType.setIndividualName(SERVICE_PROVIDER_NAME);
		return responsiblePartySubsetType;
	}
	
	private FeatureTypeListType getFeatureTypeList(net.opengis.wfs._2.ObjectFactory wfsObjectFactory) {
		final FeatureTypeListType featureTypeListType = wfsObjectFactory.createFeatureTypeListType();
		featureTypeListType.getFeatureType().add(getFeatureType(wfsObjectFactory));
		return featureTypeListType;
	}
	
	private FeatureTypeType getFeatureType(net.opengis.wfs._2.ObjectFactory wfsObjectFactory) {
		final FeatureTypeType featureType = wfsObjectFactory.createFeatureTypeType();
		featureType.setDefaultCRS(ORIGINAL_GML_3_SRS_CONVENTION);
		
		featureType.setExtendedDescription(getExtendedDescriptionType(wfsObjectFactory));
		featureType.setName(new QName("", STRASSENDATEN));
		featureType.setNoCRS(new FeatureTypeType.NoCRS());
		featureType.setOutputFormats(getOutputFormatListType(wfsObjectFactory));
		return featureType;
	}
	
	private OutputFormatListType getOutputFormatListType(net.opengis.wfs._2.ObjectFactory wfsObjectFactory) {
		final OutputFormatListType outputFormatListType = wfsObjectFactory.createOutputFormatListType();
		outputFormatListType.getFormat().add(APPLICATION_XML);
		return outputFormatListType;
	}
	
	private WFSCapabilitiesType.WSDL getWSDL() {
		final WFSCapabilitiesType.WSDL wsdl = new WFSCapabilitiesType.WSDL();
		try {
			wsdl.setTitle(HTTP + java.net.InetAddress.getLocalHost().getHostName() + PORT + SERVICE_URL + WSDL);
		} catch (UnknownHostException e) {
			LOGGER.error("Could not resolve server name", e);
		}
		return wsdl;
	}
	
	private ExtendedDescriptionType getExtendedDescriptionType(net.opengis.wfs._2.ObjectFactory wfsObjectFactory) {
		final ExtendedDescriptionType extendedDescriptionType = wfsObjectFactory.createExtendedDescriptionType();
		final ElementType elementType = wfsObjectFactory.createElementType();
		elementType.setName(STRASSENDATEN);
		elementType.setType(new QName("", STRASSENDATEN));
		extendedDescriptionType.getElement().add(elementType);
		return extendedDescriptionType;
	}
	
	private String getGetCapabilitiesString(final ObjectFactory wfsObjectFactory, final WFSCapabilitiesType wfsCapabilitiesType) {
		try {
			final JAXBContext context = JAXBContext.newInstance(wfsObjectFactory.getClass());
			final Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			final StringWriter stringWriter = new StringWriter();
			marshaller.marshal(wfsObjectFactory.createWFSCapabilities(wfsCapabilitiesType), stringWriter);
			return stringWriter.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}
}
