package org.eclipse.recommenders.snipmatch.search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.eclipse.recommenders.snipmatch.core.Snippet;
import org.eclipse.recommenders.utils.gson.GsonUtil;

/**
 * 
 * This is the SnipMatch search engine. This processes the query given by the user.
 * 
 */
public class SnipMatchSearchEngine {
    private String indexDirPath = null;

    public SnipMatchSearchEngine(String indexDirPath) {
        this.indexDirPath = indexDirPath;
    }

    /**
     * This method performs the Lucene search for a user given search query
     * 
     * @param queryLine
     *            Search query
     * @param indexPath
     *            Path to index directory
     * @return Search result
     */
    public ArrayList<Snippet> luceneSearch(String queryLine) {
        ArrayList<Snippet> searchResult = new ArrayList<Snippet>();

        if (queryLine.trim() != "") {
            IndexReader reader = null;
            try {
                reader = IndexReader.open(FSDirectory.open(new File(indexDirPath)));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            IndexSearcher searcher = new IndexSearcher(reader);
            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_35);
            QueryParser parser = new QueryParser(Version.LUCENE_35, "summary", analyzer);
            Query query = null;
            try {
                query = parser.parse(queryLine.trim() + "*"); // Add star to enable Wildcard Searches
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (query != null) {
                ScoreDoc[] hits = null;
                try {
                    hits = searcher.search(query, null, 100).scoreDocs;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < hits.length; i++) {
                    Document doc = null;
                    try {
                        doc = searcher.doc(hits[i].doc);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String path = doc.get("path");
                    File jsonFile = new File(path);
                    if (jsonFile.exists()) {
                        try {
                            Snippet snippet = GsonUtil.deserialize(jsonFile, Snippet.class);
                            searchResult.add(snippet);
                        } catch (Exception e) { // Catch all the exception at
                                                // deserializing json files
                            // e.printStackTrace();
                            continue;
                        }
                    }
                }

                try {
                    searcher.close();
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return searchResult;
    }
}
