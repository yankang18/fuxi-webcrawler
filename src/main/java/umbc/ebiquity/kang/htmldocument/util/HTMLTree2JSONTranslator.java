package umbc.ebiquity.kang.htmldocument.util;

import java.util.UUID;

import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeBlankNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeEntityNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeNodeValue;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreePropertyNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeValueNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.nlp.ValueTypeInfo;

/**
 * This utility class transforms IHTMLTreeNode to JSON string or JSON object
 * 
 * @author yankang
 *
 */
public class HTMLTree2JSONTranslator {

	private static Gson gson;
	private static JsonParser jp;

	/*
	 * Types of JSON object correspond to types of IHTMLTreeNode counterpart.
	 */
	public static final String ENTITY_TYPE = "ENTITY";
	public static final String PROPERTY_TYPE = "PROPERTY";
	public static final String VALUE_TYPE = "VALUE";
	public static final String BNODE_TYPE = "BNODE";

	/*
	 * Possible attributes for JSON object of type ENTITY, PROPERTY and BNODE.
	 */
	public static final String ID_ATTRIBUTE = "id";
	public static final String TYPE_ATTRIBUTE = "type";
	public static final String NAME_ATTRIBUTE = "name";
	public static final String ENTITIES_ATTRIBUTE = "entities";
	public static final String PROPERTIES_ATTRIBUTE = "properties";
	public static final String BNODES_ATTRIBUTE = "bnodes";
	public static final String VALUES_ATTRIBUTE = "values";

	/*
	 * Possible attributes for JSON object of type VALUE.
	 */
	public static final String VALUE_CONTENT_ATTRIBUTE = "v_content";
	public static final String VALUE_TYPE_ATTRIBUTE = "v_type";
	public static final String VALUE_UNIT_ATTRIBUTE = "v_unit";

	/*
	 * Node names for possible BNODE
	 */
	public static final String BNODE_DEFAULT_NAME = "__bnode";
	public static final String VALUE_DEFAULT_NAME = "__value";

	/**
	 * Transform the specified JSONObject to a string representation that is
	 * human-readable.
	 * 
	 * @param object
	 *            the JSOBObject to be transformed
	 * @return a String representing the JSONObject that is human-readable
	 */
	public static String prettyPrint(JSONObject object) {
		if (gson == null) {
			gson = new GsonBuilder().setPrettyPrinting().create();
			jp = new JsonParser();
		}
		return gson.toJson(jp.parse(object.toJSONString()));
	}

	/**
	 * Translates the specified IHTMLTreeNode to a JSON representation.
	 * 
	 * @param node
	 *            the IHTMLTreeNode
	 * @return a JSONObject representing the IHTMLTreeNode
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject translate(IHTMLTreeNode node) {
		JSONObject object = new JSONObject();

		if (node instanceof HTMLTreeEntityNode) {

			String entityName = ((HTMLTreeEntityNode) node).getContent();
			JSONObject entityAttributes = addJSONObjectWithBasicInfo(object, entityName);
			entityAttributes.put(TYPE_ATTRIBUTE, ENTITY_TYPE);
			translateChildren(node, entityAttributes);

		} else if (node instanceof HTMLTreeBlankNode) {

			JSONObject attributes = new JSONObject();
			attributes.put(NAME_ATTRIBUTE, BNODE_DEFAULT_NAME);
			attributes.put(TYPE_ATTRIBUTE, BNODE_TYPE);
			object.put(BNODE_DEFAULT_NAME + "_1", attributes);
			translateChildren(node, attributes);
		}

		return object;
	}

	/**
	 * Translates the children of the specified IHTMLTreeNode into the children
	 * of specified JSONObject.
	 * 
	 * @param parentNode
	 *            the IHTMLTreeNode
	 * @param parentObject
	 *            the JSONObject representation of the IHTMLTreeNode
	 */
	@SuppressWarnings("unchecked")
	private static void translateChildren(IHTMLTreeNode parentNode, JSONObject parentObject) {

		int valueNodeCount = 0;
		int blankNodeCount = 0;
		for (IHTMLTreeNode c : parentNode.getChildren()) {

			if (c instanceof HTMLTreeValueNode) {

				JSONObject values = fetchAttribute(parentObject, VALUES_ATTRIBUTE);

				HTMLTreeValueNode valueNode = (HTMLTreeValueNode) c;
				HTMLTreeNodeValue nodeValue = valueNode.getMainValue();

				JSONObject attributes = new JSONObject();
				attributes.put(VALUE_CONTENT_ATTRIBUTE, nodeValue.getContent());
				ValueTypeInfo valueType = nodeValue.getValueTypeInfo();
				attributes.put(VALUE_TYPE_ATTRIBUTE, valueType.getValueType().name());
				String unit = valueType.getUnit();
//				System.out.println(nodeValue.getContent() + ", " + unit);
				if (unit != null && !unit.trim().isEmpty()) {
					attributes.put(VALUE_UNIT_ATTRIBUTE, unit.trim());
				}
				values.put(VALUE_DEFAULT_NAME + "_" + (++valueNodeCount), attributes);

				translateChildren(c, attributes);

			} else if (c instanceof HTMLTreeEntityNode) {

				JSONObject entities = fetchAttribute(parentObject, ENTITIES_ATTRIBUTE);
				String entityName = ((HTMLTreeEntityNode) c).getContent();

				JSONObject entityAttributes = addJSONObjectWithBasicInfo(entities, entityName);
				entityAttributes.put(TYPE_ATTRIBUTE, ENTITY_TYPE);

				translateChildren(c, entityAttributes);

			} else if (c instanceof HTMLTreePropertyNode) {

				JSONObject properties = fetchAttribute(parentObject, PROPERTIES_ATTRIBUTE);
				String propName = ((HTMLTreePropertyNode) c).getContent();

				JSONObject propAttributes = addJSONObjectWithBasicInfo(properties, propName);
				propAttributes.put(TYPE_ATTRIBUTE, PROPERTY_TYPE);

				translateChildren(c, propAttributes);

			} else if (c instanceof HTMLTreeBlankNode) {

				JSONObject bnodes = fetchAttribute(parentObject, BNODES_ATTRIBUTE);

				JSONObject attributes = new JSONObject();
				attributes.put(NAME_ATTRIBUTE, BNODE_DEFAULT_NAME);
				attributes.put(TYPE_ATTRIBUTE, BNODE_TYPE);

				bnodes.put(BNODE_DEFAULT_NAME + "_" + (++blankNodeCount), attributes);

				translateChildren(c, attributes);
			}
		}
	}

	/**
	 * Creates a sub-object for the specified JSONObject with the specified
	 * object name and populates this sub-object with basic information (i.e.,
	 * id and name).
	 * 
	 * @param object
	 *            the JSONObject for which a sub-object to be created
	 * @param objectName
	 *            the name of the sub-object to be created
	 * @return a JSONObject representing the sub-object
	 */
	@SuppressWarnings("unchecked")
	private static JSONObject addJSONObjectWithBasicInfo(JSONObject object, String objectName) {
		JSONObject attributes = new JSONObject();
		object.put(objectName, attributes);
		attributes.put(ID_ATTRIBUTE, generateUniqueId());
		attributes.put(NAME_ATTRIBUTE, objectName);
		return attributes;
	}

	/**
	 * Create attribute JSONObject on demand. Get an attribute of the specified
	 * JSONObject with the specified attribute name and creates one if no
	 * attribute found for the specified attribute name. This method only works
	 * for attribute is of type JSONObject.
	 * 
	 * @param object
	 *            the JSONObject for which an attribute to be created
	 * @param attributeName
	 *            the attribute name
	 * @return a JSONObject representing the attribute
	 */
	@SuppressWarnings("unchecked")
	private static JSONObject fetchAttribute(JSONObject object, String attributeName) {
		Object attribute = object.get(attributeName);
		if (attribute == null) {
			attribute = new JSONObject();
			object.put(attributeName, attribute);
		}
		return (JSONObject) attribute;
	}

	/**
	 * Generates an unique id of string type.
	 * 
	 * @return a String representing an unique id
	 */
	private static String generateUniqueId() {
		return UUID.randomUUID().toString();
	}
}
