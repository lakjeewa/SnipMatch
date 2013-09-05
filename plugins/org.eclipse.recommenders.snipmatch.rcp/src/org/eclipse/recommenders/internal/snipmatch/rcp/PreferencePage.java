package org.eclipse.recommenders.internal.snipmatch.rcp;

import java.io.File;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.recommenders.internal.snipmatch.search.IndexCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * This class represents a preference page that is contributed to the Preferences dialog. By subclassing
 * <samp>FieldEditorPreferencePage</samp>, we can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the preference store that belongs to the main
 * plug-in class. That way, preferences can be accessed directly via the preference store.
 */

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    private DirectoryFieldEditor localSnippetsRepo = null;
    // private DirectoryFieldEditor searchIndexDir = null;
    private Button updateIndex = null;

    public PreferencePage() {
        super(GRID);
        setDescription("Set SnipMatch Preferences.");
    }

    /**
     * Creates the field editors. Field editors are abstractions of the common GUI blocks needed to manipulate various
     * types of preferences. Each field editor knows how to save and restore itself.
     */
    public void createFieldEditors() {
        localSnippetsRepo = new DirectoryFieldEditor(Constants.P_LOCAL_SNIPPETS_REPO, "&Local Snippets Repository:",
                getFieldEditorParent());
        addField(localSnippetsRepo);

        updateIndex = new Button(getFieldEditorParent(), SWT.BORDER);
        updateIndex.setText("Update Index");
        GridData data = new GridData(SWT.END, SWT.CENTER, false, false);
        data.horizontalSpan = 3;
        updateIndex.setLayoutData(data);

        updateIndex.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                performApply();
                File repodir = new File(getPreferenceStore().getString(Constants.P_LOCAL_SNIPPETS_REPO));
                // XXX MB I prefer to use a "<parent folder>/index" rather than a subfolder below snippets:
                File indexdir = new File(repodir.getParentFile(), "index");
                IndexCreator indexCreator = new IndexCreator();
                indexCreator.createIndex(repodir, indexdir);
                MessageDialog.openInformation(getShell(), "Index Updated", "Lucene Index Updated");
            }
        });
    }

    public void init(IWorkbench workbench) {
        setPreferenceStore(SnipmatchActivator.getDefault().getPreferenceStore());
    }
}
