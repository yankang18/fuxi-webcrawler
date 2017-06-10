package umbc.ebiquity.kang.htmltable.translator;

import java.util.List;

import umbc.ebiquity.kang.htmltable.core.TableRecord;

public interface IEntityTableHeaderTranslator {

	TableHeaderTranslationResult translator(List<TableRecord> headerRecords, int skipCellNumber); 
}
