package custom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GaleShapleyTest {

//	init variable
	private int numberOfTeam, numberOfEmployee, freePosition = 0;
	private Map<Integer, Team> teamsMap = new HashMap<Integer, Team>();
	private Map<Integer, Employee> employeesMap = new HashMap<Integer, Employee>();
	private double[] satisfaction;

	// constructor
	public GaleShapleyTest(int numberOfTeam, int numberOfEmployee, List<Team> teams, List<Employee> employees) {
		//
		this.numberOfTeam = numberOfTeam;
		this.numberOfEmployee = numberOfEmployee;

		// init default array
		this.satisfaction = new double[] { 0.0, 0.0 };

//		set map team
		for (Team t : teams) {
			this.teamsMap.put(t.getTeamId(), t);

			// update free position
			this.freePosition += t.getAvailablePosition();
		}

//		set map employee
		for (Employee e : employees) {
			this.employeesMap.put(e.getEmployeeId(), e);
		}

		// update free position (OUT OF BOUND error) -> free position not bigger than
		// the amount of employee <- use for looping
		this.freePosition = (this.freePosition > numberOfEmployee) ? numberOfEmployee : this.freePosition;

	}

	/**
	 * <pre>
	 * if b is more preferred to a
	 * 		return true
	 * else return false
	 * </pre>
	 **/
	private boolean ifBIsPreferredToA(int[] list, int a, int b) {

		for (int i = 0; i < list.length; i++) {
			if (list[i] == b) {
				return true;
			}

			if (list[i] == a) {
				return false;
			}
		}

		return false;
	}

	/**
	 * <pre>
	 * for list of employee
	 * 		if employee e is free 
	 * 			t = e's highest rank in prefer list
	 * 				do core matching
	 * 		else
	 * 			if t in low half of prefer list 
	 * 				for team in high half of prefer list
	 * 					do core matching
	 * </pre>
	 **/
	public Map<Integer, Team> matching(List<Employee> employees) {
		// reset satisfaction (?)
		System.err.println("i am matching rn");
		// loop the match on free position
		for (int i = 0; i < freePosition; i++) {

			// initial
			Team tempTeam;
			Employee e = employees.get(i); // get employee in list
			int[] preferList = e.getPreferList(); // get employee prefer list
			int eNew = e.getEmployeeId();

			// decide if employee is free
			if (e.isFree()) {
				// do core matching
				coreMatching(preferList, eNew);

			} // if employee do not free
			else {
				int currentTeam = e.getBelongTeam();
				int half = preferList.length / 2;
				int index;
				for (index = 0; index < preferList.length; index++) {
					if (preferList[index] == currentTeam) {
						break;
					}
				}

				// if current team in lower half of prefer list
				if (index >= half) {

					// get high half of prefer list
					int[] tempPreferList = new int[half];
					for (int k = 0; k < half; k++) {
						tempPreferList[k] = preferList[k];
					}

					// do core matching - if matching -> delete in old Team's list \/ update belong
					// Team
					if (coreMatching(tempPreferList, eNew)) {
						teamsMap.get(currentTeam).updateEmployeeList(eNew, -1);
					}
				}
			}

		} // end for employees

		// calculate satisfaction after matching
		setSatisfaction();

		return this.teamsMap;
	}// end matching

	/**
	 * <pre>
	 * if t[n] have free position 
	 * 		e join t 
	 * else 
	 * 		for list of member 
	 * 			if t prefer e to e[n] 
	 * 				e joint t 
	 * 				e[n] free 
	 * 			else 
	 * 				e still free
	 * </pre>
	 **/
	private boolean coreMatching(int[] preferList, int eNew) {
		Team tempTeam;
		for (int t : preferList) {
			tempTeam = this.teamsMap.get(t);
			int freePos = tempTeam.getAvailablePosition(); // get the available position of current team
			if (freePos > 0) {
				tempTeam.addEmployeeList(eNew);// add employee's id to list of team
				tempTeam.setAvailablePosition(freePos - 1); // set free position
				this.employeesMap.get(eNew).setFree(false); // update free 4 employee
				this.employeesMap.get(eNew).setBelongTeam(t); // update belong team

				return true; // matched
			} else {
				int[] teamPrefer = tempTeam.getPreferList(), employeeList = tempTeam.getEmployeeList();
				for (int eOld : employeeList) {
					if (ifBIsPreferredToA(teamPrefer, eOld, eNew)) {

						tempTeam.updateEmployeeList(eOld, eNew); // update list

						this.employeesMap.get(eOld).setFree(true); // update free 4 old employee
						this.employeesMap.get(eOld).setBelongTeam(-1); // delete belong team for old employee
						this.employeesMap.get(eNew).setFree(false); // update free 4 new employee
						this.employeesMap.get(eNew).setBelongTeam(t); // update belong team for new employee

						return true;
					}
				}

			}
		}

		return false;
	}

	// return satisfaction
	public double[] getSatisfaction() {
		return this.satisfaction;
	}

	/**
	 * calculate score of teams and employees
	 * 
	 **/
	private void setSatisfaction() {

		double satisfaction = 0;

		// get average score of teams
		for (int tIndex : teamsMap.keySet()) {
			Team t = teamsMap.get(tIndex);

			int[] eList = t.getEmployeeList(); // get employees of a Team

			// calculate every score of employee
			for (int eIndex : eList) {
				Employee eTemp = employeesMap.get(eIndex);

				if (eTemp == null) { // no employee was sign to the position
					satisfaction -= 40;
				} else
					satisfaction += getSimpleScore(new double[] { eTemp.getTechnicalSkill(), eTemp.getResponsibility(),
							eTemp.getAttitude(), eTemp.getExperience() });

			}

			satisfaction /= eList.length; // get average of employee list
		}

		this.satisfaction[0] = satisfaction / this.numberOfTeam; // add to record

		satisfaction = 0; // reset

		// get average score of employee
		for (int eIndex : employeesMap.keySet()) {
			Employee e = employeesMap.get(eIndex);

			// update employee score
			if (e.isFree())
				satisfaction -= 50; // unemployeed people
			else {
				Team t = teamsMap.get(e.getBelongTeam());

				satisfaction += getSimpleScore(new double[] { t.getDifficulty(), t.getHumans(),
						t.getPotentialOfProject(), t.getSalary(), t.getWorkingHour() });
			}

		}

		this.satisfaction[1] = satisfaction / this.numberOfEmployee; // add to record

	} // end setSatisfaction

	/**
	 * @parameter: array of scores
	 * @return: the score processed from input array
	 **/
	private double getSimpleScore(double[] scores) {
		double satisfaction = 0;

		for (double score : scores) {
			if (score > 5)
				score += (score - 5);
			else
				score *= (score / 10);
			satisfaction += score;
		}

		return satisfaction;
	}

	// test
	public static void main(String[] args) {
		// init
		List<Team> teams = new ArrayList<>();
		List<Employee> employees = new ArrayList<>();

		// define
		teams.add(new Team(0, 5, 6, 8, 7, 9, 2, new int[] { 1, 2, 0 }));
		teams.add(new Team(1, 4, 5, 6, 7, 7, 1, new int[] { 2 }));

//		System.out.println("team 0's employee list: " + Arrays.toString(null));

		employees.add(new Employee(0, 4, 6, 4, 8, 8, new int[] { 0, 1 }));
		employees.add(new Employee(1, 8, 7, 8, 6, 5, new int[] { 1 }));
		employees.add(new Employee(2, 4, 8, 7, 5, 4, new int[] { 1, 0 }));

		GaleShapleyTest algorithm = new GaleShapleyTest(2, 3, teams, employees);
		algorithm.matching(employees);
		double[] satisfaction = algorithm.getSatisfaction();
		System.out.println(Arrays.toString(satisfaction));
	}

}// end class
