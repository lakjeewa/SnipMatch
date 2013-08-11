package org.eclipse.recommenders.snipmatch.core;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.AbstractTextEditor;

/**
 * TemplateProcessor process the selected snippet and insert the code into
 * editor
 * 
 * 
 */
public class TemplateProcessor {

	/**
	 * Insert the selected snippet into editor
	 * 
	 * @param snippet
	 *            User selected snippet
	 */
	public void insertTemplate(Snippet snippet) {

		AbstractTextEditor activeEditor = (AbstractTextEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();

		activeEditor.setFocus();

		ISourceViewer sourceViewer = (ISourceViewer) activeEditor.getAdapter(ITextOperationTarget.class);

		Template template = new Template("name", "description", "java-statements", snippet.getCode(), true);

		IDocument doc = activeEditor.getDocumentProvider().getDocument(activeEditor.getEditorInput());
		SnipMatchJavaTemplatesPage page = new SnipMatchJavaTemplatesPage(activeEditor, sourceViewer);
		page.insertTemplate(template, doc);
		page.dispose();

	}

}
