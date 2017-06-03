package umbc.ebiquity.kang.webtable.Translator;

import java.util.List;

import umbc.ebiquity.kang.webtable.core.TableRecord;

public interface EntityTableHeaderTranslator {

	TableHeaderTranslationResult translator(List<TableRecord> headerRecords, int skipCellNumber); 
}
