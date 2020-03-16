/*
 * Copyright (c) 2019 Federal Republic of Germany and the 16 federated states of
 * the Federal Republic of Germany All rights reserved. No warranty, explicit or
 * implicit, provided. Unauthorized copying of this file via any medium is
 * strictly prohibited. Authored by European Dynamics SA <info@eurodyn.com>
 */
package de.bund.okws.stubs.service.impl.operation;

import net.opengis.fes._2.*;
import net.opengis.wfs._2.GetFeatureType;
import net.opengis.wfs._2.QueryType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class GetFeatureStrategy implements RequestHandlerStrategy {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GetFeatureStrategy.class);
	
	private static final String REQUEST_GET_FEATURE = "REQUEST=GetFeature&";
	
	@Override
	public String execute(String inputXml) {
		List<String> result = new ArrayList<>();
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(GetFeatureType.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			final StringReader reader = new StringReader(getStringBuilder(inputXml).toString());
			JAXBElement<GetFeatureType> abstractJaxbElement = (JAXBElement<GetFeatureType>) jaxbUnmarshaller.unmarshal(reader);
			final GetFeatureType getFeatureType = abstractJaxbElement.getValue();
			final List<JAXBElement<? extends AbstractQueryExpressionType>> abstractQueryExpression = getFeatureType.getAbstractQueryExpression();
			abstractQueryExpression.forEach(expression -> getResult(result, expression));
			LOGGER.info("Query: {}", getFeatureType.toString());
			if (!result.isEmpty()) {
				return "fid=\"" + result.get(0) + "\"";
			}
		} catch (JAXBException e) {
			LOGGER.error("Error while reading the input", e);
		}
		return "fid=\"0\"";
	}
	
	private StringBuilder getStringBuilder(String inputXml) {
		StringBuilder builder = new StringBuilder();
		if(inputXml.startsWith(REQUEST_GET_FEATURE)) {
			builder.append(inputXml.substring(REQUEST_GET_FEATURE.length()));
		} else {
			builder.append(inputXml);
		}
		return builder;
	}
	
	private void getResult(List<String> result, JAXBElement<? extends AbstractQueryExpressionType> expression) {
		final AbstractQueryExpressionType abstractQueryExpressionType = expression.getValue();
		if (abstractQueryExpressionType instanceof QueryType) {
			final Object selectionClauseValue = ((QueryType) abstractQueryExpressionType).getAbstractSelectionClause().getValue();
			if (selectionClauseValue instanceof FilterType) {
				final ComparisonOpsType comparisonOpsType = ((FilterType) selectionClauseValue).getComparisonOps().getValue();
				if (comparisonOpsType instanceof BinaryComparisonOpType) {
					final List<JAXBElement<?>> binaryComparisonOpTypeExpression = ((BinaryComparisonOpType) comparisonOpsType).getExpression();
					binaryComparisonOpTypeExpression.stream().
							map(exp -> exp.getValue()).
							filter(exp -> exp instanceof LiteralType).
							flatMap(literalType -> ((LiteralType) literalType).getContent().stream()).
							forEach(cont -> result.add((String) cont));
				}
			}
		}
	}
}
