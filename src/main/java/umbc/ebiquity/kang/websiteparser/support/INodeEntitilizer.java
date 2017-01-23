package umbc.ebiquity.kang.websiteparser.support;

import umbc.ebiquity.kang.websiteparser.support.impl.BlankNode;
import umbc.ebiquity.kang.websiteparser.support.impl.EntityNode;

public interface INodeEntitilizer {

	EntitilizingBlankNodeResult entitilize(BlankNode currentBNode);
	
	public static class EntitilizingBlankNodeResult {

		private boolean entitilizable = false;
		private EntityNode node;

		public boolean isEntitilizable() {
			return entitilizable;
		}

		public void setEntityNode(EntityNode entityNode) {
			node = entityNode;
		}

		public void setEntitilizable(boolean isEntitilizable) {
			entitilizable = isEntitilizable;
		}

		public EntityNode getEntityNode() {
			return node;
		}
	}

}
