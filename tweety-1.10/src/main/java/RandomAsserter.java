import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import net.sf.tweety.arg.dung.DungTheory;
import net.sf.tweety.arg.dung.PreferredReasoner;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.Attack;
import net.sf.tweety.logics.pl.sat.Sat4jSolver;
import net.sf.tweety.logics.pl.sat.SatSolver;


public class RandomAsserter {
	
	DungTheory persuaderAF;
	ArrayList<DungTheory> audienceAFs;
	Argument topic;
	
	public RandomAsserter(Scenario start){
		SatSolver.setDefaultSolver(new Sat4jSolver());
		
		persuaderAF = start.getPersudersFramework();
		audienceAFs = start.getAudienceFrameworks();
		topic = start.getTopic();
	}

	public int randomlyAssert(int argLimit){
		ArrayList<Argument> allArgs = new ArrayList<Argument>(persuaderAF.getNodes());
		
		Collections.shuffle(allArgs);
		ArrayList<Argument> toAssert = new ArrayList<Argument>( allArgs.subList(0, argLimit) );
		
		int convinced = 0;
		for (DungTheory persuadee : audienceAFs){
			if (topicAcceptableWithAssertions(persuadee, toAssert)){
				convinced++;
			}
		}
		
		return convinced;
	}
	
	
	private boolean topicAcceptableWithAssertions(DungTheory persuadee, ArrayList<Argument> possibleAssertion) {
		DungTheory dt = new DungTheory(persuadee);
		dt.addAll(possibleAssertion);
		
		for (Attack att : persuaderAF.getAttacks()){
			if (dt.contains(att.getAttacked()) && dt.contains(att.getAttacker())){
				dt.add(att);
			}
		}
		
		PreferredReasoner gr = new PreferredReasoner(dt);
		System.out.println(String.format("Topic %s and framework %s", topic, dt));
		return gr.query(topic).getAnswerBoolean();
		
	}
	
	
	public static void main(String[] args){
		int i = 0;
		while(i < 100){
			Scenario s = (new ScenarioGenerator()).generate(20, 20);
			RandomAsserter r = new RandomAsserter(s);
			System.out.println(r.randomlyAssert(4));
			i++;
		}
	}
}
