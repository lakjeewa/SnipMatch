package org.eclipse.recommenders.snipmatch.search;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.recommenders.snipmatch.rcp.UserEnvironment;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.themes.ITheme;
import org.eclipse.ui.themes.IThemeManager;

/**
 * 
 * This class creates the SnipMatch search box.
 * 
 */
public class SearchBox {

	private final int searchBoxWidth = 360;
	private Color searchBoxBackgroundColor;
	private Shell shell;
	private StyledText searchBoxText;
	private Font searchFont;

	/**
	 * Show the SnipMatch search box.
	 */
	public void show() {
		IThemeManager themeManager = PlatformUI.getWorkbench().getThemeManager();
		ITheme currentTheme = themeManager.getCurrentTheme();
		ColorRegistry colorRegistry = currentTheme.getColorRegistry();
		searchBoxBackgroundColor = colorRegistry.get("org.eclipse.recommenders.snipmatch.rcp.searchboxbackground");

		shell = new Shell(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.NO_TRIM | SWT.NO_FOCUS | SWT.NO_BACKGROUND);
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);

		FontRegistry fontRegistry = currentTheme.getFontRegistry();
		searchFont = fontRegistry.get("org.eclipse.recommenders.snipmatch.rcp.searchTextFont");
		searchBoxText = new StyledText(shell, SWT.BORDER);
		searchBoxText.setBackground(searchBoxBackgroundColor);
		searchBoxText.setMargins(8, 6, 8, 6);
		searchBoxText.setFont(searchFont);
		searchBoxText.setSize(searchBoxWidth, searchBoxText.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);

		searchBoxText.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent event) {
				shell.close();
			}

			@Override
			public void focusGained(FocusEvent e) {

			}

		});

		shell.pack();

		UserEnvironment env = new UserEnvironment();
		Point anchor = env.getSearchBoxAnchor();
		shell.setLocation(anchor.x, anchor.y);

		shell.open();
		shell.setFocus();

	}

}
