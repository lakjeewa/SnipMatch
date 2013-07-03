package org.eclipse.recommenders.snipmatch.rcp;


import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;

/**
 * 
 * This class provide the information about the user current environment.
 * 
 */
public class UserEnvironment {

	private IEditorPart editor;
	private StyledText styledText;

	
	public UserEnvironment(IEditorPart javaEditor) {
		editor = javaEditor;
	}

	/**
	 * This method find the location to place the SnipMatch search box.
	 * 
	 * @return Point coordinate of the location
	 */
	public Point getSearchBoxAnchor() {

		if (editor != null) {
			styledText = (StyledText) editor.getAdapter(Control.class);
			return styledText.toDisplay(styledText.getCaret().getLocation().x, styledText.getCaret().getLocation().y + styledText.getCaret().getSize().y);
		} else {
			return null;
		}
	}

}
