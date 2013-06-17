package joel.sentiment.model;


import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;

@XmlRootElement(name = "stopWords")
@XmlAccessorType(XmlAccessType.FIELD)
public class StopWords {

	@XmlElement(name="word")
	private List<String> words = new ArrayList<String>();
	
	public List<String> getStopWords() {
		return words;
	}
}

