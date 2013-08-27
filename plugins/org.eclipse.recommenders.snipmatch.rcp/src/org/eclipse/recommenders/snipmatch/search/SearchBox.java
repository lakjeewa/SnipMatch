package org.eclipse.recommenders.snipmatch.search;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.recommenders.snipmatch.core.Snippet;
import org.eclipse.recommenders.snipmatch.handlers.CommandHandler;
import org.eclipse.recommenders.snipmatch.rcp.UserEnvironment;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
    private UserEnvironment env;
    private EditorFocusListener editorFocusListener = null;
    private List<Snippet> searchResult = null;
    private List<Integer> tableIndexResultIndexMap = null;
    private CommandHandler commandHandler = null;

    public SearchBox(UserEnvironment userEnv, CommandHandler handler) {
        env = userEnv;
        commandHandler = handler;
    }

    /**
     * Show the SnipMatch search box.
     */
    public void show() {

        IThemeManager themeManager = PlatformUI.getWorkbench().getThemeManager();
        ITheme currentTheme = themeManager.getCurrentTheme();
        ColorRegistry colorRegistry = currentTheme.getColorRegistry();
        searchBoxBackgroundColor = colorRegistry.get("org.eclipse.recommenders.snipmatch.rcp.searchboxbackground");
        searchResultBackgroundColor = colorRegistry.get("org.eclipse.recommenders.snipmatch.rcp.searchResultBackgroundColor");

        shell = new Shell(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.NO_TRIM | SWT.NO_FOCUS | SWT.NO_BACKGROUND);
        shell.setBackgroundMode(SWT.INHERIT_DEFAULT);

        // shell.setLayout(new RowLayout(SWT.VERTICAL)); // ************

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
                commandHandler.handleTyping(searchBoxText.getText());
            }
        });

        searchBoxText.addKeyListener(new KeyListener() {

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub

            }

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
         * When the searchBoxText disposes with shell editorFocusListener should
         * be removed.
         */
        searchBoxText.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                Display.getDefault().removeFilter(SWT.FocusIn, editorFocusListener);
            }
        });

    }

    /**
     * This method display the search result given by the SnipMatch search
     * engine.
     * 
     * @param searchResult
     *            Search results
     */
    public void displayResults(List<Snippet> result) {
        searchResult = result;

        if (resultDisplayShell == null) {
            resultDisplayShell = new Shell(shell, SWT.BORDER | SWT.RESIZE);
            resultDisplayShell.setBackground(searchResultBackgroundColor);
            resultDisplayShell.setSize(searchBoxWidth, searchBoxHeight);
            resultDisplayShell.setLayout(new FillLayout(SWT.VERTICAL));
        }

        if (resultDisplayTable == null) {
            resultDisplayTable = new Table(resultDisplayShell, SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
            // resultDisplayTable = new Table(shell, SWT.SINGLE | SWT.V_SCROLL |
            // SWT.H_SCROLL | SWT.BORDER);
            resultDisplayTable.setBackground(searchResultBackgroundColor);
            resultDisplayTable.setLinesVisible(false);
            TableColumn col = new TableColumn(resultDisplayTable, SWT.LEFT);
            col.setText("");

            // resultDisplayTable.addKeyListener(resultDisplayKeyListener);
            resultDisplayTable.addMouseListener(new MouseListener() {

                @Override
                public void mouseUp(MouseEvent e) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void mouseDown(MouseEvent e) {

                }

                @Override
                public void mouseDoubleClick(MouseEvent e) {
                    int resultIndex = tableIndexResultIndexMap.get(resultDisplayTable.getSelectionIndex());
                    commandHandler.selectEntry(resultIndex);
                }
            });

            resultDisplayTable.addKeyListener(new KeyListener() {

                @Override
                public void keyReleased(KeyEvent e) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.keyCode == SWT.CR) {
                        int resultIndex = tableIndexResultIndexMap.get(resultDisplayTable.getSelectionIndex());
                        commandHandler.selectEntry(resultIndex);
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
                ArrayList<String> patternList = snippet.getPatterns();
                String[] patterns = patternList.toArray(new String[patternList.size()]);
                // item.setText(patterns);

                for (int j = 0; j < patterns.length; j++) {
                    TableItem item = new TableItem(resultDisplayTable, SWT.NONE);
                    item.setText(patterns[j]);
                    tableIndexResultIndexMap.add(i);
                }

            }

            resultDisplayTable.setFont(searchFont);
            resultDisplayTable.getColumn(0).pack();
            Point anchor = env.getSearchBoxAnchor();
            resultDisplayShell.setLocation(anchor.x, anchor.y + 35);
            resultDisplayTable.setVisible(true);

        } finally {
            resultDisplayTable.setRedraw(true);
        }

        if (!resultDisplayShell.isDisposed())
            resultDisplayShell.setVisible(true);
        // shell.pack();
        // shell.open();
    }

}
