package joel.sentiment;

import java.io.File;
import java.util.List;
import java.util.TreeSet;

import javax.xml.bind.JAXBContext;

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
		this.addAll(readStopWordsFromFile("stopWords.xml"));
	}
	
	private List<String> readStopWordsFromFile(String filePathName) {
		//String filePathName = "target/classes/META-INF/stopWords/" + fileName;
		StopWords stopWords = null;
		try {
		    JAXBContext context = JAXBContext.newInstance(StopWords.class);
		    stopWords = (StopWords)context.createUnmarshaller().unmarshal(new File(filePathName));
		} catch(Exception e) {
		    e.printStackTrace();
		    System.exit(0);
		}
		return stopWords.getStopWords();
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