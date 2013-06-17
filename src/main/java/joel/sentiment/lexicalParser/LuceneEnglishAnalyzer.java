package joel.sentiment.lexicalParser;

import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.util.Version;
import org.tartarus.snowball.ext.EnglishStemmer;

public class LuceneEnglishAnalyzer extends Analyzer {

	Reader stringReader;
	TokenStream tokenStream; 
	TermAttribute termAttribute;
	
	public LuceneEnglishAnalyzer(String text) {
        this.stringReader = new StringReader(text);
        this.tokenStream = tokenStream("", stringReader);
        this.termAttribute = tokenStream.getAttribute(TermAttribute.class);
	}

    public TokenStream getTokenStream() {
    	return this.tokenStream;
    }
    
    public TermAttribute getTermAttribute() {
    	return termAttribute;
    }

	public TokenStream tokenStream(String fieldName, Reader reader) {
        TokenStream tokenStream = new StandardTokenizer(Version.LUCENE_30, reader);
        tokenStream = new SnowballFilter(tokenStream, new EnglishStemmer());
        return tokenStream;
    }
}
