package org.eclipse.recommenders.internal.snipmatch.rcp;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.internal.corext.template.java.ElementTypeResolver;
import org.eclipse.jdt.internal.corext.template.java.ImportsResolver;
import org.eclipse.jdt.internal.corext.template.java.JavaContext;
import org.eclipse.jdt.internal.corext.template.java.LinkResolver;
import org.eclipse.jdt.internal.corext.template.java.NameResolver;
import org.eclipse.jdt.internal.corext.template.java.TypeResolver;
import org.eclipse.jdt.internal.corext.template.java.VarResolver;
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
import org.eclipse.recommenders.snipmatch.Snippet;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.AbstractTextEditor;

/**
 * TemplateProcessor process the selected snippet and insert the code into editor
 */
// TODO MB: should we make this an application singleton via Guice?
public class TemplateProcessor {

    private static TemplateProcessor INSTANCE = null;
    private TemplateContextType javaContextType;
    private String contexId = "SnipMatch-Java-Context";

    private TemplateProcessor() {
        javaContextType = createContextType();
    }

    /**
     * Return a instance of TemplateProcessor
     * 
     * @return TemplateProcessor
     */
    public static TemplateProcessor getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TemplateProcessor();
        }
        return INSTANCE;
    }

    /**
     * Insert the selected snippet into editor
     * 
     * @param snippet
     *            User selected snippet
     */
    public void insertTemplate(Snippet snippet) {
        AbstractTextEditor activeEditor = (AbstractTextEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                .getActivePage().getActiveEditor();
        activeEditor.setFocus();
        ISourceViewer sourceViewer = (ISourceViewer) activeEditor.getAdapter(ITextOperationTarget.class);
        Point range = sourceViewer.getSelectedRange();
        Template template = new Template("", "", contexId, snippet.getCode(), true);
        IRegion region = new Region(range.x, range.y);
        ICompilationUnit cu = (ICompilationUnit) EditorUtility.getEditorInputJavaElement(activeEditor, false);
        Position p = new Position(range.x, range.y);
        TemplateContext ctx = new JavaContext(javaContextType, sourceViewer.getDocument(), p, cu);
        TemplateProposal proposal = new TemplateProposal(template, ctx, region, null);
        proposal.apply(sourceViewer, (char) 0, 0, 0);
    }

    /**
     * Create context type with variable resolvers
     * 
     * @return context type
     */
    private TemplateContextType createContextType() {

        TemplateContextType contextType = new TemplateContextType(contexId);
        contextType.addResolver(new GlobalTemplateVariables.Cursor());
        contextType.addResolver(new GlobalTemplateVariables.WordSelection());
        contextType.addResolver(new GlobalTemplateVariables.Date());
        contextType.addResolver(new GlobalTemplateVariables.Dollar());
        contextType.addResolver(new GlobalTemplateVariables.LineSelection());
        contextType.addResolver(new GlobalTemplateVariables.Time());
        contextType.addResolver(new GlobalTemplateVariables.User());
        contextType.addResolver(new GlobalTemplateVariables.Year());
        contextType.addResolver(new ImportsResolver("import", "Import Statement"));

        VarResolver varResolver = new VarResolver();
        varResolver.setType("var");
        contextType.addResolver(varResolver);

        TypeResolver typeResolver = new TypeResolver();
        typeResolver.setType("newType");
        contextType.addResolver(typeResolver);

        LinkResolver linkResolver = new LinkResolver();
        linkResolver.setType("link");
        contextType.addResolver(linkResolver);

        NameResolver nameResolver = new NameResolver();
        nameResolver.setType("newName");
        contextType.addResolver(nameResolver);

        ElementTypeResolver elementTypeResolver = new ElementTypeResolver();
        elementTypeResolver.setType("elemType");
        contextType.addResolver(elementTypeResolver);

        return contextType;
    }
}
