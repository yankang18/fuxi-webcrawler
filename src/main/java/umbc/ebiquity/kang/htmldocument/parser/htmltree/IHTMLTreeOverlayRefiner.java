package umbc.ebiquity.kang.htmldocument.parser.htmltree;

/**
 * This class defines the behavior of transforming one
 * <code>IHTMLTreeOverlay</code> to another <code>IHTMLTreeOverlay</code>.
 * Classes implementing this interface typically refine the original
 * <code>IHTMLTreeOverlay</code>
 * 
 * @author yankang
 *
 */
public interface IHTMLTreeOverlayRefiner {

	/**
	 * Refine the inputed <code>IHTMLTreeOverlay</code>.
	 * 
	 * @param overlay
	 *            the <code>IHTMLTreeOverlay</code> to be refined, can not be
	 *            null
	 * @return an <code>IHTMLTreeOverlay</code>, can be null
	 */
	IHTMLTreeOverlay refine(IHTMLTreeOverlay overlay);

}
