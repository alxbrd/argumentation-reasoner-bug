import java.util.ArrayList;

import net.sf.tweety.arg.dung.DungTheory;
import net.sf.tweety.arg.dung.syntax.Argument;


public class Scenario {

	DungTheory persuadersFramework;
	ArrayList<DungTheory> audienceFrameworks;
	Argument topic;
	
	
	public Scenario(DungTheory pf, ArrayList<DungTheory> afs, Argument t){
		persuadersFramework = pf;
		audienceFrameworks = afs;
		topic = t;
	}
	
	public String toString(){
		String s = "PERSUADER: " + persuadersFramework.toString();
		s += "\nAUDIENCE:";
		for (DungTheory dt : audienceFrameworks){
			s += "\n\t @" + dt.toString();
		}
		s += "\nTOPIC: " + topic;
		return s;
	}
	
	public DungTheory getPersudersFramework(){
		return persuadersFramework;
	}
	
	public ArrayList<DungTheory> getAudienceFrameworks(){
		return audienceFrameworks;
	}
	
	public Argument getTopic(){
		return topic;
	}
}
