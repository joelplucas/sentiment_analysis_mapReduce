package joel.sentiment.mapReduce;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import joel.sentiment.LuceneEnglishAnalyzer;
import joel.sentiment.StopWordsReader;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

public class SentimentWordCounterMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {     
      
	private Text word = new Text();
	private static Set<String> stopWords =  new StopWordsReader();
	
	private Set<String> wordsFromReview = null;
	private int numUsefulVotes = 0;
	
    public void map(LongWritable key, Text value, 
    		 	OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {

    	JsonFactory jfactory = new JsonFactory();
    	JsonParser jParser = jfactory.createJsonParser(value.toString());
    	
    	boolean isFirstEndObj = false;
    	while (jParser.nextToken() != JsonToken.END_OBJECT || !isFirstEndObj) {
    		if("text".equals(jParser.getCurrentName())) {
    			jParser.nextToken();
    			wordsFromReview = getParsedWords(jParser.getText());
    		} else if("useful".equals(jParser.getCurrentName())) {
    			jParser.nextToken();
    			numUsefulVotes = jParser.getIntValue();
    		}
    		
    		if(JsonToken.END_OBJECT.equals(jParser.getCurrentToken())) {
    			isFirstEndObj = true;
    		}
    	}
    	       
        for(String wordText : wordsFromReview) {
            word.set(wordText);
            output.collect(word, new IntWritable(numUsefulVotes));        	
        }
    }

    private static Set<String> getParsedWords(String line) {
    	Set<String> parsedWords = new TreeSet<String>();
    	
    	line = removeNonAlphanumericCharsFromLine(line);
    	
    	LuceneEnglishAnalyzer luceneAnalyzer = new LuceneEnglishAnalyzer(line);
        TokenStream tokenStream = luceneAnalyzer.getTokenStream();
        TermAttribute termAttribute = luceneAnalyzer.getTermAttribute();
	    try {
			while (tokenStream.incrementToken()) {
			   	String wordText = termAttribute.term();
			    if(!isShortString(wordText) && !stopWords.contains(wordText) && !isNumeric(wordText)) {
			    	parsedWords.add(wordText);
			    }
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	    luceneAnalyzer.close();

    	return parsedWords;
    }    
    
	private static String removeExtraWhiteSpaceFromTerm(String textLine) {
		textLine = textLine.replaceAll("\\s{1,}", " ");
		textLine = textLine.toLowerCase();
		return textLine;
	}
	
	private static String removeNonAlphanumericCharsFromLine(String strLine) {
		strLine = strLine.replaceAll("[^A-Za-z0-9]", " ").trim();
		strLine = removeExtraWhiteSpaceFromTerm(strLine);
		return strLine;
	}
	
	private static boolean isShortString(String word) {
		if (word.length() <= 2) {
			return true;
		} else {
			return false;
		}
	}
	
	private static boolean isNumeric(String wordText) {  
	   try  {  
	      Integer.parseInt(wordText);  
	      return true;  
	   } catch(Exception e) {
	      return false;  
	   }  
	}
}