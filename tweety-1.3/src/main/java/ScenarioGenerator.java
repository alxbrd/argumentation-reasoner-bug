import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import net.sf.tweety.arg.dung.DungTheory;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.Attack;
import net.sf.tweety.arg.dung.util.DefaultDungTheoryGenerator;
import net.sf.tweety.arg.dung.util.DungTheoryGenerationParameters;

public class ScenarioGenerator {
	
	// Properties of Persuaders AF if none provided:
	private final static int DEFAULT_SIZE = 20;
	private final static double DEFAULT_PROBABILITY = 0.03;
	private final static boolean DEFAULT_SELF = true;
	private final static boolean DEFAULT_TREE = false;
	
	private DungTheory globalFramework;
	
	public ScenarioGenerator(){
		// if no framework provided, use the default generator to make one
		this(generatePersuaderFramework());
	}
	
	public ScenarioGenerator(DungTheory pf){
		globalFramework = pf;
	}
	
	private static DungTheory generatePersuaderFramework(){
		DungTheoryGenerationParameters params = new DungTheoryGenerationParameters();
		params.numberOfArguments = DEFAULT_SIZE;
		params.attackProbability = DEFAULT_PROBABILITY;
		params.avoidSelfAttacks = DEFAULT_SELF;
		params.enforceTreeShape = DEFAULT_TREE;
		
		DefaultDungTheoryGenerator g = new DefaultDungTheoryGenerator(params);
		return g.generate();
	}
	
	public Scenario generate(int audienceSize, int audienceBeliefSize){
		DungTheory pf = generatePersuadersFramework();
		Argument t = generateTopic(pf);
		ArrayList<DungTheory> afs = generateAudienceFrameworks(audienceSize, audienceBeliefSize, pf, t);
		Scenario s = new Scenario(pf, afs, t);
		
		return s;
	}
	
	private Argument generateTopic(DungTheory persuadersFramework) {
		// for now, topic is a random argument from persuaders framework
		ArrayList<Argument> allArguments = new ArrayList<Argument>(persuadersFramework.getNodes());
		Collections.shuffle(allArguments);
		return allArguments.get(0);
	}

	private DungTheory generatePersuadersFramework(){
		// for now, persuader knows everything
		return globalFramework;
	}
	
	private ArrayList<DungTheory> generateAudienceFrameworks(int audienceSize, int audienceBeliefSize, DungTheory persuadersFramework, Argument topic){
		ArrayList<DungTheory> afs = new ArrayList<DungTheory>();
		for (int i = 0; i < audienceSize; i++){
			afs.add(generateAudienceMember(audienceBeliefSize, persuadersFramework, topic));
		}
		return afs;
	}
	
	private DungTheory generateAudienceMember(int audienceBeliefSize, DungTheory persuadersFramework, Argument topic) {
		// for now, an audience member's framework is a random subgraph of persuaders framework
		DungTheory audienceMember = new DungTheory();
		
		ArrayList<Argument> allArguments = new ArrayList<Argument>(persuadersFramework.getNodes());
		Collections.shuffle(allArguments);
		ArrayList<Argument> audienceArguments = new ArrayList<Argument>( allArguments.subList(0, audienceBeliefSize));
		
		audienceMember.addAll(audienceArguments);
		audienceMember.add(topic); // for now, assume am's know the topic
		
		Set<Attack> allAttacks = persuadersFramework.getAttacks();
		for (Attack att : allAttacks){
			if (  audienceArguments.contains(att.getAttacked()) && audienceArguments.contains(att.getAttacker()) ){
				audienceMember.add(att);
			}
		}
		
		return audienceMember;
	}

	public static void main(String[] args) throws IOException{
		ScenarioGenerator sg = new ScenarioGenerator();
		Scenario s = sg.generate(10, 5);
		System.out.println(s);
		
	}
	
}
