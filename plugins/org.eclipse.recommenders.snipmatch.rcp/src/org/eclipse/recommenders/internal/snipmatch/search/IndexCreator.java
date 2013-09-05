package org.eclipse.recommenders.internal.snipmatch.search;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.eclipse.recommenders.snipmatch.Snippet;
import org.eclipse.recommenders.utils.Throws;
import org.eclipse.recommenders.utils.gson.GsonUtil;

/**
 * This class creates the index for given directory of snippet files using Apache Lucene
 * 
 */
public class IndexCreator {

    /**
     * This method create the Lucene index
     * 
     * @param snippetsdir
     *            Path to snippets directory
     * @param indexDirectoryPath
     *            Path to index directory
     */
    public void createIndex(File snippetsdir, File indexdir) {

        if (!indexdir.exists()) {
            indexdir.mkdirs();
        }

        try {
            Directory index = FSDirectory.open(indexdir);
            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_35);
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_35, analyzer);
            config.setOpenMode(OpenMode.CREATE);
            IndexWriter writer = new IndexWriter(index, config);
            indexSnippets(writer, snippetsdir);
            writer.close();
        } catch (IOException e) {
            Throws.throwUnhandledException(e);
        }
    }

    /**
     * Index the snippets
     * 
     * @param writer
     *            Lucene IndexWriter
     * @param snippetsRepoPath
     *            Path to snippets directory
     */
    private void indexSnippets(IndexWriter writer, File snippetsDir) {

        if (snippetsDir.exists() && snippetsDir.canRead() && snippetsDir.isDirectory()) {

            File[] snippets = snippetsDir.listFiles();

            for (int i = 0; i < snippets.length; i++) {
                if (snippets[i].isFile()) {

                    Snippet snippet = null;
                    try {
                        snippet = GsonUtil.deserialize(snippets[i], Snippet.class);
                    } catch (Exception e) {
                        // e.printStackTrace();
                        continue;
                    }

                    Document doc = new Document();
                    doc.add(new Field("path", snippets[i].getPath(), Field.Store.YES, Field.Index.NO));
                    doc.add(new Field("summary", snippet.getSummary(), Field.Store.YES, Field.Index.ANALYZED));
                    try {
                        writer.addDocument(doc);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
