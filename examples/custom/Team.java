package custom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @overview: Team is a group that responsibility to a specific project that
 *            demand employee to work. Provide some field that employees care
 *            about like 'field related' to their abilities, 'potential of
 *            project', 'difficulty' of project, 'salary', environment and 'working hour';
 * @update: add 'Available Positions' that handle problem 
 **/
public class Team {

//	attribute
	private int teamId; 
	private int potentialOfProject, difficulty, salary, humans, workingHour, availablePosition;
	private int[] preferList, employeeList; 
	
//	constructor
	public Team(int id, int potentialOfProject, int difficulty, int salary, 
			int humans, int workingHour, int availablePosition, int[] preferList) {
		//attribute to calculate satisfaction
		this.potentialOfProject = potentialOfProject;
		this.difficulty = difficulty;
		this.salary = salary;
		this.humans = humans;
		this.workingHour = workingHour;
		
		// attribute to manage 
		this.teamId = id; 
		this.availablePosition = availablePosition;
		this.preferList = preferList; // prefer list from input
		this.employeeList = new int[availablePosition]; // list of employee 
		Arrays.fill(this.employeeList, -1);
	}

//	getter & setter
	public int getPotentialOfProject() {
		return potentialOfProject;
	}

	public void setPotentialOfProject(int potentialOfProject) {
		this.potentialOfProject = potentialOfProject;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public int getHumans() {
		return humans;
	}

	public void setHumans(int humans) {
		this.humans = humans;
	}

	public int getWorkingHour() {
		return workingHour;
	}

	public void setWorkingHour(int workingHour) {
		this.workingHour = workingHour;
	}

	public int getAvailablePosition() {
		return availablePosition;
	}

	public void setAvailablePosition(int availablePosition) {
		this.availablePosition = availablePosition;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public int[] getPreferList() {
		return preferList;
	}

	public void setPreferList(int[] preferList) {
		this.preferList = preferList;
	}

	public int[] getEmployeeList() {
		return employeeList;
	}
	
	// current position is list's length - free position
	public void addEmployeeList(int eNew) {
		this.employeeList[this.employeeList.length - this.availablePosition] = eNew;
	}	

//	replace old employee with the new one
	public void updateEmployeeList(int eOld, int eNew) {
		
		for(int i = 0; i < this.employeeList.length ; i++) {
			if(this.employeeList[i] == eOld) {
				this.employeeList[i] = eNew;
				break;
			}
		}
	}

	public static void main(String[] args) {
		Team t = new Team(0, 5, 5, 6, 4, 6, 2, new int[] {0});
		System.out.println("employee list: " + Arrays.toString(t.getEmployeeList()));
	}
	
	
} // end class
