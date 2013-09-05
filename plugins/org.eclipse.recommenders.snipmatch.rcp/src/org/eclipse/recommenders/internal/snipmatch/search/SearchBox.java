package org.eclipse.recommenders.internal.snipmatch.search;

import static com.google.common.collect.Iterables.toArray;
import static org.eclipse.recommenders.internal.snipmatch.rcp.Constants.P_SEARCH_BOX_BACKGROUND;
import static org.eclipse.recommenders.internal.snipmatch.rcp.Constants.P_SEARCH_RESULTS_BACKGROUND;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.recommenders.internal.snipmatch.rcp.Constants;
import org.eclipse.recommenders.internal.snipmatch.rcp.SearchHandler;
import org.eclipse.recommenders.snipmatch.Snippet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.themes.ITheme;
import org.eclipse.ui.themes.IThemeManager;

/**
 * This class creates the SnipMatch search box.
 */
public class SearchBox {

    private final int searchBoxWidth = 360;
    private final int searchBoxHeight = 200;
    private Color searchboxBg;
    private Color searchresultsBg;
    private Shell shell;
    private Shell resultDisplayShell = null;
    private Table resultDisplayTable = null;
    private StyledText searchboxText;
    private Font searchboxFont;
    private UserEnvironment env;
    private EditorFocusListener editorFocusListener = null;
    private List<Snippet> searchResult = null;
    private List<Integer> tableIndexResultIndexMap = null;
    private SearchHandler searchhandler = null;

    public SearchBox(UserEnvironment userEnv, SearchHandler handler) {
        env = userEnv;
        searchhandler = handler;
    }

    /**
     * Show the SnipMatch search box.
     */
    public void show() {

        IThemeManager themeManager = PlatformUI.getWorkbench().getThemeManager();
        ITheme currentTheme = themeManager.getCurrentTheme();
        ColorRegistry colorRegistry = currentTheme.getColorRegistry();
        searchboxBg = colorRegistry.get(P_SEARCH_BOX_BACKGROUND);
        searchresultsBg = colorRegistry.get(P_SEARCH_RESULTS_BACKGROUND);

        shell = new Shell(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.NO_TRIM | SWT.NO_FOCUS
                | SWT.NO_BACKGROUND);
        shell.setBackgroundMode(SWT.INHERIT_DEFAULT);

        FontRegistry fontRegistry = currentTheme.getFontRegistry();
        searchboxFont = fontRegistry.get("org.eclipse.recommenders.snipmatch.rcp.searchTextFont");
        searchboxText = new StyledText(shell, SWT.BORDER);
        searchboxText.setBackground(searchboxBg);
        searchboxText.setMargins(8, 6, 8, 6);
        searchboxText.setFont(searchboxFont);
        searchboxText.setSize(searchBoxWidth, searchboxText.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
        searchboxText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                searchhandler.handleTyping(searchboxText.getText());
            }
        });

        searchboxText.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.keyCode == SWT.ARROW_DOWN || e.keyCode == SWT.ARROW_UP) {
                    if (resultDisplayTable != null) {
                        resultDisplayTable.setFocus();
                        resultDisplayTable.select(0);
                    }
                }
            }
        });

        // Add listener to capture a click event on editor
        editorFocusListener = new EditorFocusListener(shell);
        Display.getDefault().addFilter(SWT.FocusIn, editorFocusListener);

        shell.pack();

        Point anchor = env.getSearchBoxAnchor();
        shell.setLocation(anchor.x, anchor.y);

        shell.open();
        shell.setFocus();

        /**
         * When the searchBoxText disposes with shell editorFocusListener should be removed.
         */
        searchboxText.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                Display.getDefault().removeFilter(SWT.FocusIn, editorFocusListener);
            }
        });

    }

    /**
     * This method display the search result given by the SnipMatch search engine.
     * 
     * @param searchResult
     *            Search results
     */
    public void displayResults(List<Snippet> result) {
        searchResult = result;

        if (resultDisplayShell == null) {
            resultDisplayShell = new Shell(shell, SWT.BORDER | SWT.RESIZE);
            resultDisplayShell.setBackground(searchresultsBg);
            resultDisplayShell.setSize(searchBoxWidth, searchBoxHeight);
            resultDisplayShell.setLayout(new FillLayout(SWT.VERTICAL));
        }

        if (resultDisplayTable == null) {
            resultDisplayTable = new Table(resultDisplayShell, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
            resultDisplayTable.setBackground(searchresultsBg);
            resultDisplayTable.setLinesVisible(false);
            TableColumn col = new TableColumn(resultDisplayTable, SWT.LEFT);
            col.setText("");

            resultDisplayTable.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseDoubleClick(MouseEvent e) {
                    int resultIndex = tableIndexResultIndexMap.get(resultDisplayTable.getSelectionIndex());
                    searchhandler.selectEntry(resultIndex);
                }
            });

            resultDisplayTable.addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.keyCode == SWT.CR) {
                        int resultIndex = tableIndexResultIndexMap.get(resultDisplayTable.getSelectionIndex());
                        searchhandler.selectEntry(resultIndex);
                    } else if ((e.keyCode >= 97 && e.keyCode <= 122) || (e.keyCode >= 48 && e.keyCode <= 57)
                            || (e.keyCode >= 16777258 && e.keyCode <= 16777273) || e.keyCode == 32) {
                        // 97-122 Character
                        // 48-57 Digits
                        // 16777258-16777273 Num Locks
                        // 32 Space
                        searchboxText.append(Character.toString(e.character));
                        searchboxText.setSelection(searchboxText.getText().length());
                        searchboxText.setFocus();

                    } else if (e.keyCode == 8) {
                        // 8 Backspace
                        int length = searchboxText.getText().length();
                        if (length != 0)
                            searchboxText.replaceTextRange(length - 1, 1, "");
                        searchboxText.setFocus();
                    }
                }
            });

            resultDisplayTable.setRedraw(false);
        } else {
            resultDisplayTable.removeAll();
        }

        try {
            tableIndexResultIndexMap = new ArrayList<Integer>();
            for (int i = 0; i < searchResult.size(); i++) {
                Snippet snippet = searchResult.get(i);
                String[] patterns = toArray(snippet.getPatterns(), String.class);

                for (int j = 0; j < patterns.length; j++) {
                    TableItem item = new TableItem(resultDisplayTable, SWT.NONE);
                    item.setText(patterns[j]);
                    tableIndexResultIndexMap.add(i);
                }

            }

            resultDisplayTable.setFont(searchboxFont);
            resultDisplayTable.getColumn(0).pack();
            Point anchor = env.getSearchBoxAnchor();
            resultDisplayShell.setLocation(anchor.x, anchor.y + 35);
            resultDisplayTable.setVisible(true);

        } finally {
            resultDisplayTable.setRedraw(true);
        }

        if (!resultDisplayShell.isDisposed())
            resultDisplayShell.setVisible(true);
    }

}
