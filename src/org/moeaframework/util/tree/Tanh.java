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
package org.moeaframework.util.tree;

/**
 * The node for calculating the hyperbolic tangent of a number.  The inputs and
 * outputs to this node are shown below:
 * 
 * <table border="1" cellpadding="3" cellspacing="0">
 *   <tr class="TableHeadingColor">
 *     <th width="25%" align="left">Name</th>
 *     <th width="25%" align="left">Type</th>
 *     <th width="50%" align="left">Description</th>
 *   </tr>
 *   <tr>
 *     <td>Argument 1</td>
 *     <td>Number</td>
 *     <td>The angle</td>
 *   </tr>
 *   <tr>
 *     <td>Return Value</td>
 *     <td>Number</td>
 *     <td>The hyperbolic tangent of the angle</td>
 *   </tr>
 * </table>
 * 
 * @see Math#tanh(double)
 */
public class Tanh extends Node {
	
	/**
	 * Constructs a new node for calculating the hyperbolic tangent of a number.
	 */
	public Tanh() {
		super(Number.class, Number.class);
	}
	
	@Override
	public Tanh copyNode() {
		return new Tanh();
	}
	
	@Override
	public Number evaluate(Environment environment) {
		return NumberArithmetic.tanh(
				(Number)getArgument(0).evaluate(environment));
	}

}
