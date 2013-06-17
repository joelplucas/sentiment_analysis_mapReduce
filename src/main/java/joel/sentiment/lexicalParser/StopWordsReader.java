package joel.sentiment.lexicalParser;

import java.util.TreeSet;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

import joel.sentiment.model.StopWords;


@SuppressWarnings("serial")
public class StopWordsReader extends TreeSet<String> {

	public StopWordsReader() {
		super();
		addEnglishGrammaWords();
		convertStopWordsToLucene();
	}
	
	private void addEnglishGrammaWords() {		
		StopWords stopWords = new StopWords();
		this.addAll(stopWords);
	}
	
	private void convertStopWordsToLucene() {
        try {
    		String wordsTogether = getStringFromArray(this.toArray());
    		LuceneEnglishAnalyzer luceneAnalyzer = new LuceneEnglishAnalyzer(wordsTogether);			
            TokenStream tokenStream = luceneAnalyzer.getTokenStream();
            TermAttribute termAttribute = luceneAnalyzer.getTermAttribute();
    	    this.clear();
        	while (tokenStream.incrementToken()) {
			   	this.add(termAttribute.term());
			}
			luceneAnalyzer.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private String getStringFromArray(Object[] terms) {
		String termsTogether = "";
		for(Object term : terms) {
			termsTogether = termsTogether.concat((String)term).concat(" ");
		}
		return termsTogether;
	}

}