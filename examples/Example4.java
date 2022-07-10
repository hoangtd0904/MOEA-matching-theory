/* Copyright 2009-2019 David Hadka
 *
 * This file is part of the MOEA Framework.
 *
 * The MOEA Framework is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * The MOEA Framework is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the MOEA Framework.  If not, see <http://www.gnu.org/licenses/>.
 */
import java.util.Arrays;

import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.core.variable.RealVariable;
import org.moeaframework.problem.AbstractProblem;

/**
 * Demonstrates how a new problem is defined and used within the MOEA
 * Framework.
 */
public class Example4 {

	/**
	 * Implementation of the DTLZ2 function.
	 */
	public static class MyDTLZ2 extends AbstractProblem {

		/**
		 * Constructs a new instance of the DTLZ2 function, defining it
		 * to include 11 decision variables and 2 objectives.
		 */
		public MyDTLZ2(int i, int j) {
			super(1, 2, 1);
			
			System.out.println("hello " + i + " " + j);
		}

		/**
		 * Constructs a new solution and defines the bounds of the decision
		 * variables.
		 */
		@Override
		public Solution newSolution() {
			Solution solution = new Solution(getNumberOfVariables(), 
					getNumberOfObjectives(),getNumberOfConstraints());

			for (int i = 0; i < getNumberOfVariables(); i++) {
				solution.setVariable(i, new RealVariable(0.0, 1.0));
			}

//			System.out.println("IN SOLUTION");
			
			return solution;
		}
		
		/**
		 * Extracts the decision variables from the solution, evaluates the
		 * Rosenbrock function, and saves the resulting objective value back to
		 * the solution. 
		 */
		@Override
		public void evaluate(Solution solution) {
			double[] x = EncodingUtils.getReal(solution);
			double[] f = new double[numberOfObjectives];

			System.out.println("X: " + Arrays.toString(x));
			
			int k = numberOfVariables - numberOfObjectives + 1;

			double g = 0.0;
			for (int i = numberOfVariables - k; i < numberOfVariables; i++) {
				g += Math.pow(x[i] - 0.5, 2.0);
			}

			for (int i = 0; i < numberOfObjectives; i++) {
				f[i] = 1.0 + g;

				for (int j = 0; j < numberOfObjectives - i - 1; j++) {
					f[i] *= Math.cos(0.5 * Math.PI * x[j]);
				}

				if (i != 0) {
					f[i] *= Math.sin(0.5 * Math.PI * x[numberOfObjectives - i - 1]);
				}
			} 

			System.out.println("F: " + Arrays.toString(f));

			double[] f1 = new double[numberOfObjectives];
			for(int a = 0; a < numberOfObjectives; a ++) {
				f1[a] = Math.round(f[a]);
			}
			for(double d : f1) {
				if(d == 0.0) {
					System.out.println("CONSTRAINT EQUAL TO 0, STOP");
					solution.setConstraint(0,0.0);
				}
			}
			System.out.println("F1: " + Arrays.toString(f1));
			
			System.out.println("===========================================================");
			
			solution.setObjectives(f);
		}
		
	}
	
	public static void main(String[] args) {
		//configure and run the DTLZ2 function
		NondominatedPopulation result = new Executor()
				.withProblemClass(MyDTLZ2.class, 1, 2)
				.withAlgorithm("NSGAII")
				.withMaxEvaluations(10)
				.run();
				
		//display the results
		System.out.format("Objective1  Objective2%n");
		
		for (Solution solution : result) {
			System.out.format("%.4f      %.4f%n",
					solution.getObjective(0),
					solution.getObjective(1));
		}
	}
	
}
