import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class ComputeTardiness {	
	public static ProblemInstance readInstance(String filename){
		System.out.println(filename);
		ProblemInstance instance = null;
		
		try {
			int numJobs = 0;
			double[][] jobs = null;
			
			Scanner sc = new Scanner(new BufferedReader(new FileReader(filename)));
			if(sc.hasNextInt()){
				numJobs = sc.nextInt();
				jobs = new double[numJobs][2];
				int nextJobID = 0;
			
				while (sc.hasNextInt() && nextJobID < numJobs) {
					jobs[nextJobID][0] = sc.nextInt();
					jobs[nextJobID][1] = sc.nextInt();
					nextJobID++;
				}
			}
			sc.close();
			
			instance = new ProblemInstance(numJobs, jobs);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return instance;
	}

	// reads a problem, and outputs the result of both greedy and best-first
    public static void main (String args[]) {
		ProblemInstance instance = readInstance(args[0]);

		Greedy greedy = new Greedy(instance);
		Schedule greedySchedule = greedy.getSchedule();
		System.out.println("Greedy tardiness: " + greedySchedule.getTardiness());

		//BestFirst bestFirst = new BestFirst(instance);
        //Schedule bestFirstSchedule = bestFirst.getSchedule();
        //System.out.println("Best first tardiness: " + bestFirstSchedule.getTardiness());

		ExactAlgorithm exactAlgorithm = new ExactAlgorithm(instance);
		exactAlgorithm.getSchedule();

		double Tmax = 0;
		Schedule current = greedySchedule;
		while (current.getDepth() > 1) {
			if (current.getTardiness() > Tmax) {
				Tmax = current.getTardiness();
			}
			current = current.getPrevious();
		}

		ApproximationAlgorithm approximationAlgorithm = new ApproximationAlgorithm(instance.scale(0.1, Tmax));
	}
}
