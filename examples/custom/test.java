package custom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.core.variable.RealVariable;
import org.moeaframework.problem.AbstractProblem;
//import org.moeaframework.util.Vector;

public class test {

	/**
	 * <pre>
	 * &#64;flow:
	 * 		- get path file for input
	 * 		- load data
	 * 		- pass data to algorithm
	 * 		-
	 * </pre>
	 **/
	public static class MyProblem extends AbstractProblem {

		// init variables
		int[] employeeList;
		List<Employee> employees;
		List<Team> teams;
		GaleShapleyTest algorithm;

		/**
		 * @params: number of teams, number of employee, list of teams, list of employee
		 * @variables: old satisfaction scores
		 * @objectives: new satisfaction scores
		 **/
		public MyProblem(int numOfTeam, int numOfEmployee, List<Team> teams, List<Employee> employees) {
			super(2, 2); // always have 2 satisfaction scores

			// set list
			this.employees = employees;
			this.teams = teams;
			this.algorithm = new GaleShapleyTest(numOfTeam, numOfEmployee, teams, employees);
		}

		/**
		 * Constructs a new solution and defines the bounds of the decision variables.
		 */
		@Override
		public Solution newSolution() {
			Solution solution = new Solution(2, 2);

			solution.setVariable(0, new RealVariable(0, -100.0, 100.0));
			solution.setVariable(1, new RealVariable(0, -100.0, 100.0));

			return solution;
		}

		/**
		 * matching and get satisfaction
		 */
		@Override
		public void evaluate(Solution solution) {
			// get old satisfaction score
//			System.out.println("Evaluate");
			double[] oldSatisfactions = EncodingUtils.getReal(solution);

			// TODO : do call matching /\ do get new satisfaction /\ set attribute

			Collections.shuffle(employees); // shuffle employee list -> get random list to do the matching
			Map<Integer, Team> newMatch = algorithm.matching(employees);

			// get new satisfaction score
			double[] newSatisfactions = algorithm.getSatisfaction();

			// if new matching got better result
//			if (newSatisfactions[0] >= oldSatisfactions[0] || newSatisfactions[1] >= oldSatisfactions[1]) {
				for(int tId : newMatch.keySet()) {
					// parse employee list into map with team id -> already save in algorithm
					solution.setAttribute(Integer.toString(tId), newMatch.get(tId).getEmployeeList()); 
				}
				
//				System.out.println("OKAY SITUATION");
				// set new satisfaction score
				solution.setObjectives(newSatisfactions);
//				solution.setVariable(0, newSatisfactions[0]);
//			}

		}
	}

	// driven code
	public static void main(String[] args) {
		
		// TODO load file to get data
		
		//test fix data
		int numberOfEmployee = 10,
				numberOfTeam = 5;
		int[] teamId = {0,1,2,3,4};
		List<Employee> employees = new ArrayList<>();
		List<Team> teams = new ArrayList<>();
		
//		init employees
		employees.add(new Employee(11, 6, 6, 4, 7, 7, new int[] {0,2,1,3}));
		employees.add(new Employee(12, 2, 4, 2, 6, 8, new int[] {3,2,1,0,4}));
		employees.add(new Employee(13, 8, 9, 6, 9, 7, new int[] {2,1,3}));
		employees.add(new Employee(14, 7, 6, 8, 8, 9, new int[] {0,2,1,3}));
		employees.add(new Employee(15, 3, 5, 5, 7, 6, new int[] {1,0,2,3}));
		employees.add(new Employee(16, 6, 7, 3, 9, 5, new int[] {0,2,1,3}));
		employees.add(new Employee(17, 2, 4, 2, 7, 6, new int[] {4,3,2}));
		employees.add(new Employee(18, 8, 9, 2, 7, 7, new int[] {3,4,2}));
		employees.add(new Employee(19, 7, 6, 4, 6, 5, new int[] {0,3,2,4,1}));
		employees.add(new Employee(20, 3, 5, 3, 7, 8, new int[] {0,2,4,1}));
		//init teams
		teams.add(new Team(0, 6, 3, 9, 8, 7, 3, new int[]{14,13,11,15,18}));
		teams.add(new Team(1, 8, 6, 7, 7, 8, 2, new int[]{14,11,16,20,19,17}));
		teams.add(new Team(2, 7, 7, 6, 6, 9, 1, new int[]{13,18,16,14}));
		teams.add(new Team(3, 2, 3, 8, 7, 4, 5, new int[]{18,17,20,12,16,15,19,11,13}));
		teams.add(new Team(4, 4, 4, 5, 8, 5, 3, new int[]{11,12,14,15,16,17,19,20}));
		
		// TODO parse data to executor
		
		
		// configure and run the DTLZ2 function
		NondominatedPopulation result = new Executor()
				.withProblemClass(MyProblem.class, numberOfTeam, numberOfEmployee, teams, employees) //TODO parse data here
				.withAlgorithm("NSGAII")
				.withMaxEvaluations(10000)
				.run();

		// TODO display the results 
		System.out.format(" Average satisfaction score %n");
		System.out.format("     Team        Employee   %n");

		for (Solution solution : result) {
			System.out.format("%6.4f      %6.4f%n", solution.getObjective(0), solution.getObjective(1));
			System.out.println("Matching result: ");
			Map<String, Serializable> attribute = solution.getAttributes();
			for(int i : teamId) {
				System.out.println("Team " + i + ": " + Arrays.toString((int[]) attribute.get(Integer.toString(i))));
			}
		}

	}

}
