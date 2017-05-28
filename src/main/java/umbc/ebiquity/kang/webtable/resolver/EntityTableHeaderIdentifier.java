package umbc.ebiquity.kang.webtable.resolver;

import java.util.List;

import umbc.ebiquity.kang.webtable.core.TableRecord;

public interface EntityTableHeaderIdentifier {

	HeaderIdentificationResult identifyEntityHeaders(List<TableRecord> headerRecords, int skipCellNumber); 
}
