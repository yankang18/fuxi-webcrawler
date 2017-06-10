package umbc.ebiquity.kang.htmltable.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import umbc.ebiquity.kang.htmltable.IAttributeHolder;
import umbc.ebiquity.kang.htmltable.IAttributeTracer;
import umbc.ebiquity.kang.htmltable.ITagHolder;

public class TableRecordAttributeTracer implements IAttributeTracer {

	private Map<String, Set<String>> tagPath2Attrs;
	private Set<String> attrsToBeTraced;

	public TableRecordAttributeTracer(Set<String> attributesToBeTraced) {
		attrsToBeTraced = attributesToBeTraced;
		tagPath2Attrs = new HashMap<String, Set<String>>();
	}

	public void mark(List<TableRecord> records) {
		for (TableRecord tr : records) {
			markAttributes(tr, tr);
			for (TableCell tc : tr.getTableCells()) {
				markAttributes(tc, tc);
				for (DataCell dc : tc.getDataCells()) {
					markAttributes(dc, dc);
				}
			}
		}
	}

	/**
	 * @param attributeHolder
	 * @param dcTagPath
	 */
	private void markAttributes(IAttributeHolder attributeHolder, ITagHolder taggedEntity) {
		String tagPath = taggedEntity.getTagPath();
		for (String attr : attrsToBeTraced) {
			if (notEmpty(attributeHolder.getAttributeValue(attr))) {
//				System.out.println("$$$$ "+tagPath + " -> " + attributeHolder.getAttributeValue(attr));
				// if this attribute exists in the data cell
				Set<String> markedAttrs = tagPath2Attrs.get(tagPath);
				if (null == markedAttrs) {
					markedAttrs = new HashSet<String>();
					tagPath2Attrs.put(tagPath, markedAttrs);
				}
				markedAttrs.add(attr);
			}
		}
	}

	private boolean notEmpty(String value) {
		return null != value && !"".equals(value.trim());
	}

	@Override
	public boolean isAttributeTraced(String tagPah, String attributeName){
		Set<String> attrs = tagPath2Attrs.get(tagPah);
		if(null != attrs){
			if(attrs.contains(attributeName)){
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public Set<String> getAttributesToBeTraced() {
		return attrsToBeTraced;
	}

}
