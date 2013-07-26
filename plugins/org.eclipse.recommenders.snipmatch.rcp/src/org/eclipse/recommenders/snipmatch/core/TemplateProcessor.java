package org.eclipse.recommenders.snipmatch.core;

import org.eclipse.jdt.internal.corext.template.java.ImportsResolver;
import org.eclipse.jdt.internal.corext.template.java.StaticImportResolver;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.templates.DocumentTemplateContext;
import org.eclipse.jface.text.templates.GlobalTemplateVariables;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateProposal;
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

	private JavaEditor editor;

	/**
	 * Insert the selected snippet into editor
	 * @param snippet User selected snippet
	 */
	public void insertTemplate(Snippet snippet) {

		AbstractTextEditor activeEditor = (AbstractTextEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();

		activeEditor.setFocus();

		ISourceViewer sourceViewer = (ISourceViewer) activeEditor.getAdapter(ITextOperationTarget.class);

		Point range = sourceViewer.getSelectedRange();

		Template template = new Template("", "", "org.eclipse.recommenders.snipmatch.rcp.javaContextType", snippet.getCode(), true);
		IRegion region = new Region(range.x, range.y);

		// TemplateVariableResolver resolver = new TemplateVariableResolver();

		TemplateContextType contextType = new TemplateContextType("org.eclipse.recommenders.snipmatch.rcp.javaContextType");
		// TemplateContextType contextType = new JavaContextType();
		contextType.addResolver(new GlobalTemplateVariables.Cursor());
		contextType.addResolver(new GlobalTemplateVariables.WordSelection());
		contextType.addResolver(new GlobalTemplateVariables.Date());
		contextType.addResolver(new GlobalTemplateVariables.Dollar());
		contextType.addResolver(new GlobalTemplateVariables.LineSelection());
		contextType.addResolver(new GlobalTemplateVariables.Time());
		contextType.addResolver(new GlobalTemplateVariables.User());
		contextType.addResolver(new GlobalTemplateVariables.Year());
		ImportsResolver importsResolver = new ImportsResolver();
		contextType.addResolver(importsResolver);
		contextType.addResolver(new StaticImportResolver());

		TemplateContext ctx = new DocumentTemplateContext(contextType, sourceViewer.getDocument(), range.x, range.y);

		TemplateProposal proposal = new TemplateProposal(template, ctx, region, null);
		proposal.apply(sourceViewer, (char) 0, 0, 0);
	}

}
