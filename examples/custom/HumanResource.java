package custom;

import java.io.IOException;
import java.io.Serializable;
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

public class HumanResource {

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
			double[] oldSatisfactions = EncodingUtils.getReal(solution);

			Collections.shuffle(employees); // shuffle employee list -> get random list to do the matching
			Map<Integer, Team> newMatch = algorithm.matching(employees);

			// get new satisfaction score
			double[] newSatisfactions = algorithm.getSatisfaction();

			// if new matching got better result
			for (int tId : newMatch.keySet()) {
				// parse employee list into map with team id -> already save in algorithm
				solution.setAttribute(Integer.toString(tId), newMatch.get(tId).getEmployeeList());
			}

			// set new satisfaction score
			solution.setObjectives(newSatisfactions);
		}
	}

	// driven code
	public static void main(String[] args) throws IOException {

		// TODO load file to get data
		ReadInput input = new ReadInput("examples/custom/data.xlsx", 15);
		input.load(); // load data

		List<Employee> employees = input.getEmployees();
		List<Team> teams = input.getTeams();
		
		int[] teamId = new int[teams.size()];
		int i = 0;
		for(Team t : teams) {
			teamId[i] = t.getTeamId();
			i++;
		}
		
		NondominatedPopulation result = new Executor()
				.withProblemClass(MyProblem.class, teams.size(), employees.size(), teams, employees) 
				.withAlgorithm("NSGAII").withMaxEvaluations(10000).run();

		// TODO display the results
		System.out.format(" Average satisfaction score %n");
		System.out.format("   Team     Employee   %n");

		for (Solution solution : result) {
			System.out.format(" %6.4f      %6.4f%n", solution.getObjective(0), solution.getObjective(1));
			System.out.println("=======================================\n Matching result: ");
			Map<String, Serializable> attribute = solution.getAttributes();
			for (int id : teamId) {
				System.out.println("Team " + id + ": " + Arrays.toString((int[]) attribute.get(Integer.toString(id))));
			}
		}

	}

}
