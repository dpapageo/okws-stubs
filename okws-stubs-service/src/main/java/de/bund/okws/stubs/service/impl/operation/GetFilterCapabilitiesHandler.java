package de.bund.okws.stubs.service.impl.operation;

import net.opengis.fes._2.*;
import org.springframework.stereotype.Service;

import javax.xml.namespace.QName;

@Service
public class GetFilterCapabilitiesHandler {
	
	private static final String GML = "gml";
	private static final String RESOURCE_ID = "ResourceId";
	private static final String FES_NAMESPACE_URI = "http://www.opengis.net/fes/2.0";
	
	private static final String OPERAND_GREATER_OR_EQUAL_THAN = ">=";
	private static final String OPERAND_EQUALS = "=";
	private static final String OPERAND_LESS_THAN = "<";
	private static final String OPERAND_LESS_OR_EQUAL_THAN = "<=";
	private static final String OPERAND_GREATER_THAN = ">";
	private static final String OPERAND_BETWEEN = "Between";
	private static final String OPERAND_LIKE = "Like";
	private static final String OPERAND_IS_NULL = "isNull";
	private static final String OPERAND_UNEQUAL = "<>";
	
	
	public FilterCapabilities getFilterCapabilities(final ObjectFactory fesObjectFactory) {
		final FilterCapabilities filterCapabilities = fesObjectFactory.createFilterCapabilities();
		
		filterCapabilities.setScalarCapabilities(getScalarCapabilitiesType(fesObjectFactory));
		filterCapabilities.setSpatialCapabilities(getSpatialCapabilitiesType(fesObjectFactory));
		filterCapabilities.setIdCapabilities(getIdCapabilitiesType(fesObjectFactory));
		
		return filterCapabilities;
	}
	
	private IdCapabilitiesType getIdCapabilitiesType(ObjectFactory fesObjectFactory) {
		final IdCapabilitiesType idCapabilitiesType = fesObjectFactory.createIdCapabilitiesType();
		final ResourceIdentifierType resourceIdentifierType = fesObjectFactory.createResourceIdentifierType();
		resourceIdentifierType.setName(new QName(FES_NAMESPACE_URI, RESOURCE_ID));
		idCapabilitiesType.getResourceIdentifier().add(resourceIdentifierType);
		return idCapabilitiesType;
	}
	
	private SpatialCapabilitiesType getSpatialCapabilitiesType(final ObjectFactory fesObjectFactory) {
		final SpatialCapabilitiesType spatialCapabilitiesType = fesObjectFactory.createSpatialCapabilitiesType();
		spatialCapabilitiesType.setSpatialOperators(getSpatialOperatorsType(fesObjectFactory));
		spatialCapabilitiesType.setGeometryOperands(getGeometryOperandsType(fesObjectFactory));
		return spatialCapabilitiesType;
	}
	
	private GeometryOperandsType getGeometryOperandsType(ObjectFactory fesObjectFactory) {
		final GeometryOperandsType geometryOperandsType = fesObjectFactory.createGeometryOperandsType();
		geometryOperandsType.getGeometryOperand().add(getGeometryOperand(fesObjectFactory, "Point"));
		geometryOperandsType.getGeometryOperand().add(getGeometryOperand(fesObjectFactory, "LineString"));
		geometryOperandsType.getGeometryOperand().add(getGeometryOperand(fesObjectFactory, "Polygon"));
		return geometryOperandsType;
	}
	
	private GeometryOperandsType.GeometryOperand getGeometryOperand(final ObjectFactory fesObjectFactory, final String name) {
		final GeometryOperandsType.GeometryOperand geometryOperandsTypeGeometryOperand = fesObjectFactory.createGeometryOperandsTypeGeometryOperand();
		geometryOperandsTypeGeometryOperand.setName(new QName(GML, name));
		return geometryOperandsTypeGeometryOperand;
	}
	
	private SpatialOperatorsType getSpatialOperatorsType(final ObjectFactory fesObjectFactory) {
		final SpatialOperatorsType spatialOperatorsType = fesObjectFactory.createSpatialOperatorsType();
		spatialOperatorsType.getSpatialOperator().add(getSpatialOperatorType(fesObjectFactory, "Box"));
		spatialOperatorsType.getSpatialOperator().add(getSpatialOperatorType(fesObjectFactory, "Intersects"));
		spatialOperatorsType.getSpatialOperator().add(getSpatialOperatorType(fesObjectFactory, "Contains"));
		spatialOperatorsType.getSpatialOperator().add(getSpatialOperatorType(fesObjectFactory, "Crosses"));
		spatialOperatorsType.getSpatialOperator().add(getSpatialOperatorType(fesObjectFactory, "Overlaps"));
		spatialOperatorsType.getSpatialOperator().add(getSpatialOperatorType(fesObjectFactory, "Within"));
		spatialOperatorsType.getSpatialOperator().add(getSpatialOperatorType(fesObjectFactory, "Touches"));
		spatialOperatorsType.getSpatialOperator().add(getSpatialOperatorType(fesObjectFactory, "Disjoint"));
		return spatialOperatorsType;
	}
	
	private SpatialOperatorType getSpatialOperatorType(final ObjectFactory fesObjectFactory, String name) {
		final SpatialOperatorType spatialOperatorType = fesObjectFactory.createSpatialOperatorType();
		spatialOperatorType.setName(name);
		return spatialOperatorType;
	}
	
	private ScalarCapabilitiesType getScalarCapabilitiesType(final ObjectFactory fesObjectFactory) {
		final ScalarCapabilitiesType scalarCapabilitiesType = fesObjectFactory.createScalarCapabilitiesType();
		scalarCapabilitiesType.setComparisonOperators(getComparisonOperatorsType(fesObjectFactory));
		return scalarCapabilitiesType;
	}
	
	private ComparisonOperatorsType getComparisonOperatorsType(final ObjectFactory fesObjectFactory) {
		final ComparisonOperatorsType comparisonOperatorsType = fesObjectFactory.createComparisonOperatorsType();
		comparisonOperatorsType.getComparisonOperator().add(getComparisonOperatorType(fesObjectFactory, OPERAND_EQUALS));
		comparisonOperatorsType.getComparisonOperator().add(getComparisonOperatorType(fesObjectFactory, OPERAND_UNEQUAL));
		comparisonOperatorsType.getComparisonOperator().add(getComparisonOperatorType(fesObjectFactory, OPERAND_LESS_THAN));
		comparisonOperatorsType.getComparisonOperator().add(getComparisonOperatorType(fesObjectFactory, OPERAND_LESS_OR_EQUAL_THAN));
		comparisonOperatorsType.getComparisonOperator().add(getComparisonOperatorType(fesObjectFactory, OPERAND_GREATER_THAN));
		comparisonOperatorsType.getComparisonOperator().add(getComparisonOperatorType(fesObjectFactory, OPERAND_GREATER_OR_EQUAL_THAN));
		comparisonOperatorsType.getComparisonOperator().add(getComparisonOperatorType(fesObjectFactory, OPERAND_BETWEEN));
		comparisonOperatorsType.getComparisonOperator().add(getComparisonOperatorType(fesObjectFactory, OPERAND_LIKE));
		comparisonOperatorsType.getComparisonOperator().add(getComparisonOperatorType(fesObjectFactory, OPERAND_IS_NULL));
		return comparisonOperatorsType;
	}
	
	private ComparisonOperatorType getComparisonOperatorType(final ObjectFactory fesObjectFactory, String s) {
		final ComparisonOperatorType comparisonOperatorType = fesObjectFactory.createComparisonOperatorType();
		comparisonOperatorType.setName(s);
		return comparisonOperatorType;
	}
}
