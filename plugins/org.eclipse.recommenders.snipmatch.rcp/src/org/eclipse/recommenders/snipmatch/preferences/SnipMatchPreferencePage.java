package org.eclipse.recommenders.snipmatch.preferences;

import java.io.File;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.recommenders.snipmatch.rcp.Activator;
import org.eclipse.recommenders.snipmatch.search.IndexCreator;
import org.eclipse.recommenders.snipmatch.util.SnipMatchMessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */

public class SnipMatchPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private DirectoryFieldEditor localSnippetsRepo = null;
	private DirectoryFieldEditor searchIndexDir = null;
	private Button updateIndex = null;

	public SnipMatchPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Set SnipMatch Preferences.");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {
		localSnippetsRepo = new DirectoryFieldEditor(PreferenceConstants.LOCAL_SNIPPETS_REPO, "&Local Snippets Repository:", getFieldEditorParent());
		searchIndexDir = new DirectoryFieldEditor(PreferenceConstants.SEARCH_INDEX_DIR, "&Search Index Directory:", getFieldEditorParent());
		addField(localSnippetsRepo);
		addField(searchIndexDir);

		updateIndex = new Button(getFieldEditorParent(), SWT.PUSH);
		updateIndex.setText(PreferenceConstants.UPDATE_INDEX);
		GridData data = new GridData(SWT.END, SWT.CENTER, false, false);
		data.horizontalSpan = 3;
		updateIndex.setLayoutData(data);

		updateIndex.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				storedCurrentValues();
				String localSnippetRepoDirPath = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.LOCAL_SNIPPETS_REPO);
				String indexDirPath = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.SEARCH_INDEX_DIR);

				if (isValidPaths(localSnippetRepoDirPath, indexDirPath)) {
					IndexCreator indexCreator = new IndexCreator();
					indexCreator.createIndex(localSnippetRepoDirPath, indexDirPath);
					SnipMatchMessageDialog.openInformation("Index Updated", "Lucene Index Updated");
				}
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

	public void storedCurrentValues() {
		localSnippetsRepo.store();
		searchIndexDir.store();
	}

	/**
	 * This method validate the user given file paths before create or update the index.
	 * @param localSnippetRepoDirPath Path to local snippets repository
	 * @param indexDirPath Path to Lucene index directory
	 * @return true or false
	 */
	public boolean isValidPaths(String localSnippetRepoDirPath, String indexDirPath) {

		if ((localSnippetRepoDirPath == null || "".equals(localSnippetRepoDirPath)) && (indexDirPath == null || "".equals(indexDirPath))) {
			SnipMatchMessageDialog.openError("Invalid Path", "Enter Snippet Repository and Index Directory Paths.");
			return false;
		} else if (localSnippetRepoDirPath == null || "".equals(localSnippetRepoDirPath)) {
			SnipMatchMessageDialog.openError("Invalid Path", "Enter Snippet Repository Path.");
			return false;
		} else if (indexDirPath == null || "".equals(indexDirPath)) {
			SnipMatchMessageDialog.openError("Invalid Path", "Enter Index Directory Path.");
			return false;
		} else {
			File repoDir = new File(localSnippetRepoDirPath);
			File indexDir = new File(indexDirPath);
			if (!repoDir.isDirectory() || !repoDir.exists()) {
				SnipMatchMessageDialog.openError("Invalid Path", "Snippet Repository Directory does not exist.");
				return false;
			} else if (!indexDir.isDirectory() || !indexDir.exists()) {
				SnipMatchMessageDialog.openError("Invalid Path", "Index Directory does not exist.");
				return false;
			} else {
				return true;
			}
		}

	}

}
