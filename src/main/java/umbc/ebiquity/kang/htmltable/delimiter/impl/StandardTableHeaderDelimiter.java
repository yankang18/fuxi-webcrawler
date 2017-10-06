package umbc.ebiquity.kang.htmltable.delimiter.impl;

import java.util.List;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmltable.delimiter.ITableHeaderDelimiter;
import umbc.ebiquity.kang.htmltable.util.HTMLTableValidator;

public class StandardTableHeaderDelimiter implements ITableHeaderDelimiter {

	List<ITableHeaderDelimiter> delimiters;
	
	public StandardTableHeaderDelimiter(){
		
	}
	
	@Override
	public HeaderDelimitedTable delimit(Element element) {
		HTMLTableValidator.isTable(element); 
		
		
		return null;
	}

}
