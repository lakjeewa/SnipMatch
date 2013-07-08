package org.eclipse.recommenders.snipmatch.search;

import java.util.ArrayList;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.recommenders.snipmatch.core.Snippet;
import org.eclipse.recommenders.snipmatch.rcp.UserEnvironment;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
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
	private final int searchBoxHeight = 200;
	private Color searchBoxBackgroundColor;
	private Color searchResultBackgroundColor;
	private Shell shell;
	private Shell resultDisplayShell = null;
	private Table resultDisplayTable = null;
	private StyledText searchBoxText;
	private Font searchFont;
	private String lastQuery;
	private SnipMatchSearchEngine snipMatchSearchEngine;
	private UserEnvironment env;
	private boolean shellFocued = false;
	private boolean resultDisplayShellFocued = false;
	private FocusListener focusListener = null;

	public SearchBox(UserEnvironment userEnv) {
		env = userEnv;
	}

	/**
	 * Show the SnipMatch search box.
	 */
	public void show() {

		snipMatchSearchEngine = SnipMatchSearchEngine.getInstance();

		IThemeManager themeManager = PlatformUI.getWorkbench().getThemeManager();
		ITheme currentTheme = themeManager.getCurrentTheme();
		ColorRegistry colorRegistry = currentTheme.getColorRegistry();
		searchBoxBackgroundColor = colorRegistry.get("org.eclipse.recommenders.snipmatch.rcp.searchboxbackground");
		searchResultBackgroundColor = colorRegistry.get("org.eclipse.recommenders.snipmatch.rcp.searchResultBackgroundColor");

		shell = new Shell(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.NO_TRIM | SWT.NO_FOCUS | SWT.NO_BACKGROUND);
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);

		FontRegistry fontRegistry = currentTheme.getFontRegistry();
		searchFont = fontRegistry.get("org.eclipse.recommenders.snipmatch.rcp.searchTextFont");
		searchBoxText = new StyledText(shell, SWT.BORDER);
		searchBoxText.setBackground(searchBoxBackgroundColor);
		searchBoxText.setMargins(8, 6, 8, 6);
		searchBoxText.setFont(searchFont);
		searchBoxText.setSize(searchBoxWidth, searchBoxText.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);

		searchBoxText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				handleTyping();
			}
		});

		searchBoxText.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent event) {
				shellFocued = false;
			}

			@Override
			public void focusGained(FocusEvent e) {
				shellFocued = true;
			}

		});

		shell.pack();

		Point anchor = env.getSearchBoxAnchor();
		shell.setLocation(anchor.x, anchor.y);

		shell.open();
		shell.setFocus();

	}

	/**
	 * This method handle the type event on the search box.
	 */
	private void handleTyping() {
		String query = searchBoxText.getText();
		if (lastQuery != null && lastQuery.trim().equals(query.trim()))
			return;

		lastQuery = query;
		ArrayList<Snippet> searchResult = snipMatchSearchEngine.search(query);
		displayResults(searchResult);
	}

	/**
	 * This method display the search result given by the SnipMatch search
	 * engine.
	 * 
	 * @param searchResult
	 *            Search results
	 */
	private void displayResults(ArrayList<Snippet> searchResult) {

		if (resultDisplayShell == null) {
			resultDisplayShell = new Shell(shell, SWT.BORDER | SWT.RESIZE);
			resultDisplayShell.setBackground(searchResultBackgroundColor);
			resultDisplayShell.setSize(searchBoxWidth, searchBoxHeight);
			resultDisplayShell.setLayout(new FillLayout(SWT.VERTICAL));
		}

		if (resultDisplayTable == null) {
			resultDisplayTable = new Table(resultDisplayShell, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);

			resultDisplayTable.setBackground(searchResultBackgroundColor);
			resultDisplayTable.setLinesVisible(false);
			TableColumn col = new TableColumn(resultDisplayTable, SWT.LEFT);
			col.setText("");

			// resultDisplayTable.addKeyListener(resultDisplayKeyListener);
			// resultDisplayTable.addMouseListener(resultDisplayMouseListener);
			resultDisplayTable.setRedraw(false);
		} else {
			resultDisplayTable.removeAll();
		}

		try {
			for (Snippet snippet : searchResult) {
				TableItem item = new TableItem(resultDisplayTable, SWT.NONE);
				ArrayList<String> patternList = snippet.getPatterns();
				String[] patterns = patternList.toArray(new String[patternList.size()]);
				item.setText(patterns);
			}

			resultDisplayTable.setFont(searchFont);
			resultDisplayTable.getColumn(0).pack();
			Point anchor = env.getSearchBoxAnchor();
			resultDisplayShell.setLocation(anchor.x, anchor.y + 35);

		} finally {
			resultDisplayTable.setRedraw(true);
		}

		resultDisplayTable.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent event) {
				resultDisplayShellFocued = false;

			}

			@Override
			public void focusGained(FocusEvent e) {
				resultDisplayShellFocued = true;
			}

		});

		if (!resultDisplayShell.isDisposed())
			resultDisplayShell.open();

		shell.setFocus();

	}

}
