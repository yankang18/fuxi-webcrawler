package umbc.ebiquity.kang.htmldocument.parser.htmltree;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeBlankNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeEntityNode;

public interface IHTMLTreeNodeEntitizer {

	EntitizingBlankNodeResult entitize(HTMLTreeBlankNode currentBNode);
	
	public static class EntitizingBlankNodeResult {

		private boolean entitilizable = false;
		private HTMLTreeEntityNode node;

		public boolean isEntitizable() {
			return entitilizable;
		}

		public void setEntityNode(HTMLTreeEntityNode entityNode) {
			node = entityNode;
		}

		public void setEntitilizable(boolean isEntitilizable) {
			entitilizable = isEntitilizable;
		}

		public HTMLTreeEntityNode getEntityNode() {
			return node;
		}
	}

}
