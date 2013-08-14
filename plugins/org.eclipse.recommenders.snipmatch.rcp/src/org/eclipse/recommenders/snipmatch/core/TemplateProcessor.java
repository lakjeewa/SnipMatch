package org.eclipse.recommenders.snipmatch.core;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.internal.corext.template.java.ImportsResolver;
import org.eclipse.jdt.internal.corext.template.java.JavaContext;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.jdt.internal.ui.text.template.contentassist.TemplateProposal;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.templates.GlobalTemplateVariables;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.swt.graphics.Point;
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

		// AbstractTextEditor activeEditor = (AbstractTextEditor)
		// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		//
		// activeEditor.setFocus();
		//
		// ISourceViewer sourceViewer = (ISourceViewer)
		// activeEditor.getAdapter(ITextOperationTarget.class);
		//
		// Template template = new Template("name", "description",
		// "java-statements", snippet.getCode(), true);
		//
		// IDocument doc =
		// activeEditor.getDocumentProvider().getDocument(activeEditor.getEditorInput());
		// SnipMatchJavaTemplatesPage page = new
		// SnipMatchJavaTemplatesPage(activeEditor, sourceViewer);
		// page.insertTemplate(template, doc);
		// page.dispose();

		AbstractTextEditor activeEditor = (AbstractTextEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();

		activeEditor.setFocus();

		ISourceViewer sourceViewer = (ISourceViewer) activeEditor.getAdapter(ITextOperationTarget.class);

		Point range = sourceViewer.getSelectedRange();

		Template template = new Template("", "", "org.eclipse.jdt.internal.corext.template.java.JavaContextType", snippet.getCode(), true);
		IRegion region = new Region(range.x, range.y);

		TemplateContextType contextType = new TemplateContextType("org.eclipse.jdt.internal.corext.template.java.JavaContextType");

		contextType.addResolver(new GlobalTemplateVariables.Cursor());
		contextType.addResolver(new GlobalTemplateVariables.WordSelection());
		contextType.addResolver(new GlobalTemplateVariables.Date());
		contextType.addResolver(new GlobalTemplateVariables.Dollar());
		contextType.addResolver(new GlobalTemplateVariables.LineSelection());
		contextType.addResolver(new GlobalTemplateVariables.Time());
		contextType.addResolver(new GlobalTemplateVariables.User());
		contextType.addResolver(new GlobalTemplateVariables.Year());
		contextType.addResolver(new ImportsResolver("import", "Import Statement"));

		ICompilationUnit cu = (ICompilationUnit) EditorUtility.getEditorInputJavaElement(activeEditor, false);
		Position p = new Position(range.x, range.y);

		TemplateContext ctx = new JavaContext(contextType, sourceViewer.getDocument(), p, cu);

		TemplateProposal proposal = new TemplateProposal(template, ctx, region, null);
		proposal.apply(sourceViewer, (char) 0, 0, 0);

	}

}
