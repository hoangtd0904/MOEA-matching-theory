package custom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * <pre>
 *  @pseudoCode: pseudo code of gale-shapley algorithm
 * 
 * 	Initialize all men and women to free
 * 	while there exist a free man m who still has a woman w to propose to 
 * 	{
 *  	w = m's highest ranked such woman to whom he has not yet proposed
 *   	if w is free
 *     		(m, w) become engaged
 *   	else some pair (m', w) already exists
 *     		if w prefers m to m'
 *        		(m, w) become engaged
 *        		m' becomes free
 *     	else
 *        	(m', w) remain engaged    
 * 	}
 * 
 *  @new: 
 * Initialize all team and employee
 * while employee e is free and still have team t to join
 * {
 * 	 for list of teams
 * 		{
 * 			if team t still have free position
 * 				e join t
 * 			else 
 * 				for list of member 
 * 				{
 * 					if t prefer e to e[n] 
 * 						e[n] free
 * 						e join t
 * 					else 
 * 						e[n] still stable
 * 				}
 * 		}
 * }
 * 
 * ==> @drawbacks: 
 * 	- if team can have many members -> hard to check if employee satisfy with the team -> 2 solutions
 * 		+ check list of prefer teams before 
 *		+ get the satisfaction score after the matching to rematch 
 * 	- 
 * 
 *              TODO: solving problem related to how e and t match with each
 *              other
 * 
 *              set available position = n
 *              if t is the highest prefer in both e and e1,... en
 *              	for t's prefer list
 *              		get n employees
 *              
 *              - save team and employees in a map, a team has a list of employee that can be assigned into that team
 *              - prototype: {
 *              	team1 : {employee1, employee2, employee23, null},
 *              	team2 : {employee3, employee9, null, null, null, null},
 *              	team3 : {employee8, null, null},
 *              	...
 *                }
 *              
 *              	+ length of list employee depend on number of available position in a team
 *              	+ if an employee cannot find a team that match requirement, it can be stupid :D or can be call unemployed 
 *              	+ if a team cannot find a suitable employee, the position will be null (?) or can match with a stupid (?)
 *              
 *              - calculate the satisfy of team and employee: (for each) -> array
 *                + (score from req * rank / total employee) + (score from req * rank/ total team) - unemployed/(total employee) // temporary
 *                + if 70% above avg -> pass
 *                + else failed
 * </pre>
 **/
public class GaleShapleyAlgorithm {

//	number of employee
	/**
	 * @new: constructor init number of team and employee
	 * 
	 **/
	private int numberOfTeam, numberOfEmployee; // temporary team
	private Team[] teams;
	private Employee[] employees;

	public GaleShapleyAlgorithm(int numberOfTeam) {
//		this.teams = teams;
//		this.employees = employees;
//		Team[] teams, Employee[] employees
//		this.numberOfTeam = teams.length;
//		this.numberOfEmployee = employees.length;

		this.numberOfTeam = numberOfTeam;
	}

//	return true if team(t) prefer employee1(e1) over employee(e)
	private boolean tPrefersE1OverE(int prefer[][], int t, int e, int e1) {

//	check if the team care much about other than current employee	
		for (int i = 0; i < numberOfTeam; i++) {

			// If e1 comes before e in list of t,
			// then t prefers their current engagement,
			// don't do anything
			if (prefer[t][i] == e1)
				return true;

			// If e comes before e1 in t's list,
			// then free her current engagement
			// and engage her with m
			if (prefer[t][i] == e)
				return false;
		}
		return false;
	}

	/**
	 * @return: random list
	 * @param: bound of list
	 **/
	private List<Integer> getRandList(int bound) {
		List<Integer> array = new ArrayList<>();

		Random rand = new Random();
		int count = 0;

		while (count < bound) {
			int num = rand.nextInt(bound);

			if (!array.contains(num)) {
				count++;
				array.add(num);

			}
		}

		return array;
	}

	// Prints stable matching for N boys and
	// N girls. Boys are numbered as 0 to
	// N-1. Girls are numbered as N to 2N-1.
	/**
	 * @new: get parameter as list of object
	 * 
	 * 
	 **/
	private int[] stableMarriage(int prefer[][], int[] oldPartner) {

		// Stores partner of women. This is our
		// output array that stores passing information.
		// The value of wPartner[i] indicates the partner
		// assigned to woman N+i. Note that the woman
		// numbers between N and 2*N-1. The value -1
		// indicates that (N+i)'th woman is free

		/**
		 * @new: member of a team, maybe manage by object (?) TODO: each team have a
		 *       list of employees
		 **/
		int wPartner[] = oldPartner;
//		Arrays.fill(wPartner, -1);

		// An array to store availability of men.
		// If mFree[i] is false, then man 'i' is
		// free, otherwise engaged.

		/**
		 * @new: manage by object (?)
		 * 
		 **/
		boolean mFree[] = new boolean[numberOfTeam];
		System.out.println("mFree: " + Arrays.toString(mFree));

		// Initialize all men and women as free
		int freeCount = numberOfTeam;

		// While there are free men
		while (freeCount > 0) {
			// Pick the first free man
			// (we could pick any)
//			System.out.println("====================================================================");
//			int m;
			List<Integer> mArr = getRandList(numberOfTeam);
//			System.out.println("[Check m free]");
			for (int m : mArr) {
//				System.out.println("- m" + m + ": " + mFree[m]);
				if (mFree[m] == true)
					break;
//			}

				String status = (mFree[m] == true) ? "engaged" : "free";
//				System.out.println("------>\n  + m" + m + " is " + status + " and his prefer list is "
//						+ Arrays.toString(prefer[m]));

				// One by one go to all women
				// according to m's preferences.
				// Here m is the picked free man

				for (int i = 0; i < numberOfTeam && mFree[m] == false; i++) {
					int w = prefer[m][i];

					String wStatus = (wPartner[w - numberOfTeam] == -1) ? "free" : "engaged";
//					System.out.println("  + w" + w + " is " + "m" + m + "'s current highest priority and " + wStatus);
					// The woman of preference is free,
					// w and m become partners (Note that
					// the partnership maybe changed later).
					// So we can say they are engaged not married
					if (wStatus.equals("free")) {
						wPartner[w - numberOfTeam] = m;
						mFree[m] = true;
						freeCount--;

//						System.out.println("====> wPartner is " + Arrays.toString(wPartner));
					}

					else // If w is not free
					{
						// Find current engagement of w
						int m1 = wPartner[w - numberOfTeam];
//						System.out.println("   -> w" + w + "'s current engagement is m" + m1);
						// If w prefers m over her current engagement m1,
						// then break the engagement between w and m1 and
						// engage m with w.
//						System.out.println("   +------------------------------------------------+");
						if (tPrefersE1OverE(prefer, w, m, m1) == false) {
//							System.out.println("   | w" + w + " prefer m" + m + " to m" + m1);
							wPartner[w - numberOfTeam] = m;
							mFree[m] = true;
							mFree[m1] = false;
//							System.out.println("   | ===> wPartner: " + Arrays.toString(wPartner));
						}
//						System.out.println("   +------------------------------------------------+");
//						System.out.println();
					} // End of Else
				} // End of the for loop that goes
					// to all women in m's list
			} // End of main while loop

			// Print the solution
		}
			return wPartner;
	}

	// Driver Code
	public static void main(String[] args) {
		int prefer[][] = new int[][] { { 7, 5, 6, 4 }, { 5, 6, 4, 7 }, { 4, 5, 6, 7 }, { 4, 5, 6, 7 }, { 1, 3, 2, 0 },
				{ 3, 2, 0, 1 }, { 0, 2, 1, 3 }, { 3, 1, 2, 0 } };
		int numberOfTeam = 4;

		GaleShapleyAlgorithm ga = new GaleShapleyAlgorithm(numberOfTeam);
		ArrayList<Integer> arr = new ArrayList<>();
		List<Integer> arr1 = new ArrayList<Integer>() {
		};

		int[] partners = new int[4];
		System.out.println(Arrays.toString(partners));

		int[] wPartner = ga.stableMarriage(prefer, partners);

		System.out.println(Arrays.toString(wPartner));

		System.out.println("-------------------------------------------------");

		System.out.println("Woman Man");
		for (int i = 0; i < numberOfTeam; i++) {
			System.out.print(" ");
			System.out.println(i + numberOfTeam + "     " + wPartner[i]);
		}
	}
} // end class
