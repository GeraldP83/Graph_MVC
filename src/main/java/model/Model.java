package model;

import java.util.ArrayList;
import java.util.List;

public class Model {
	
	
	private int radius;
	private int diameter;
	private ArrayList<Integer> centerVertices;
	private int[] eccentricity;
	private List<int[][]> calculatedDistAndWay;
	private int[][] adjazentValues;
	private String components;
	private ArrayList<Integer> artikulationen;
	private ArrayList<String> bruecken;
	private int size;
	boolean isCoherently;
	
	
	public Model(int size) {
		this.size = size;
		adjazentValues = new int[size][size];
		calculate(0, 0, 0);//just for initializing
	}

	public void calculate(int row, int column, int value) {
		adjazentValues[row][column] = value;
		adjazentValues[column][row] = value;
		resetValues();
		calculatedDistAndWay = calculateDistanceAndWay(adjazentValues);
		calculateEccentricity();
		calculateRadiusAndDiameter();
		calculateCenterVertices();
		components = prepareFoundComponentsForPresentating();
		artikulationen = calculateArticulations();
		bruecken = calculateBridges();
	}

	private void resetValues() {
		calculatedDistAndWay = new ArrayList<int[][]>();
		diameter = -1;
		radius = Integer.MAX_VALUE;
		centerVertices = new ArrayList<Integer>();
		eccentricity = new int[adjazentValues.length];
	}

	private ArrayList<int[][]> calculateDistanceAndWay(int[][] values) {
		ArrayList<int[][]> retVal = new ArrayList<>();
		boolean somethingChanged = true;
		int[][] distanceValues = initMatrixFromAdjaValues(values, "distance");
		int[][] wayValues = initMatrixFromAdjaValues(values, "way");
		int[][] expotentiated = values;
		
		for (int numberOfPow = 2; numberOfPow < values.length && somethingChanged; numberOfPow++) {
			expotentiated = powMatrix(expotentiated, values);
			somethingChanged = checkForChanges(expotentiated, numberOfPow, distanceValues, wayValues);
		}
		retVal.add(distanceValues);
		retVal.add(wayValues);
		return retVal;
	}

	/**
	 * iterates over the distanceMatrix and compares its values with the expoMatrix. If the distance value
	 * is 0 and the expoValue is > 0, than a new way is found. So add the number of pow to the distance field
	 * and 1 to the wayMatrix.
	 * @param expotentiated pow Matrix
	 * @param values adjazenzMatrix
	 * @param numberOfPow 
	 * @param distanceValues initialized distance matrix 
	 * @param wayValues initialized way matrix
	 * @return true if something has changed
	 */
	private boolean checkForChanges(int[][] expotentiated, int numberOfPow, int[][] distanceValues, int[][] wayValues) {
		boolean hasChanged = false;
		for (int row = 0; row < expotentiated.length; row++) {
			for (int columnInRow = row; columnInRow < expotentiated.length; columnInRow++) {
					if (distanceValues[row][columnInRow] == 0
						&& expotentiated[row][columnInRow] > 0 
						&& (!(row == columnInRow))) {
					distanceValues[row][columnInRow] = numberOfPow;
					wayValues[row][columnInRow] = 1;
					distanceValues[columnInRow][row] = numberOfPow;
					wayValues[columnInRow][row] = 1;
					hasChanged = true;
				}
			}
		}
		return hasChanged;
	}

	/**
	 * Multiplies the powMatrix with the adjazenzMatrix
	 * @param powMatrix the powMatrix(could also be potentiated already)
	 * @param adjazenzM the adjazenzMatrix
	 * @return
	 */
	private int[][] powMatrix(int[][] powMatrix, int[][] adjazenzM) {
		int[][] result = new int[adjazenzM.length][adjazenzM.length];
		for (int i = 0; i < powMatrix.length; i++) {
			for (int j = i; j < powMatrix.length; j++) {// j = i I only need to calculate the upper right side
				for (int k = 0; k < powMatrix.length; k++) {
					if (i == j) {
						result[i][j] += powMatrix[i][k] * adjazenzM[k][j];
					} else {
						result[i][j] += powMatrix[i][k] * adjazenzM[k][j];
						result[j][i] += powMatrix[i][k] * adjazenzM[k][j];
					}

				}
			}
		}
		return result;
	}

	/**
	 * calculates if the graph is coherently.
	 * If there are no 0 in the wayMatrix returns true.
	 * @return boolean
	 * 
	 */
	private boolean isCoherently() {
		for (int[] values : calculatedDistAndWay.get(1)) {
			for (int value : values) {
				if (value == 0) {
					isCoherently = false;
					return false;
				}
			}
		}
		isCoherently = true;
		return true;
	}

	/**
	 * the eccentricity --> the highest distance
	 */
	private void calculateEccentricity() {
		for (int row = 0; row < adjazentValues.length; row++) {
			for (int column = 0; column < adjazentValues.length; column++) {
				int tempVal = calculatedDistAndWay.get(0)[row][column];
				if (tempVal > eccentricity[row]) {
					eccentricity[row] = tempVal;
				}
			}
		}
	}

	/**
	 * The radius is the smallest eccentricity of the Graph but must not be 0
	 * The diameter is the biggest eccentricity of the Graph
	 * If the graph isn´t coherently both are set to -1
	 */
	private void calculateRadiusAndDiameter() {
		for (int i = 0; i < eccentricity.length; i++) {
			if (isCoherently()) {
				if (eccentricity[i] != 0 && eccentricity[i] < radius) {	radius = eccentricity[i]; }
				if (eccentricity[i] > diameter) { diameter = eccentricity[i]; }
				if (size == 1) { radius = 1; diameter = 1; }
			} else {
				radius = -1;
				diameter = -1;
			}
		}
	}

	/**
	 * Iterates over the eccentricities and compares its values with the
	 * radius. If they are equal, this vertex is a centervertex
	 */
	private void calculateCenterVertices() {
		if (size == 1) { centerVertices.add(1);; return; }
		for (int i = 0; i < eccentricity.length; i++) {
			if (eccentricity[i] == radius) {
				centerVertices.add(i + 1);
			}
		}
	}

	private String prepareFoundComponentsForPresentating() {
		int[] sameComponents = findComponents(calculatedDistAndWay.get(1));
		int component = 1;
		StringBuilder sb = new StringBuilder();
		int max = findMax(sameComponents);
		for (int j = 0; j < max; j++) {// max = the number of possibilities

			sb.append("Komponente " + component + ": {");
			for (int i = 0; i < sameComponents.length; i++) {
				if (sameComponents[i] == component) {
					sb.append(i + 1 + ", ");
				}
				if (i == sameComponents.length - 1) {
					sb.replace(sb.lastIndexOf(","), sb.length(), "}");
				}
			}
			sb.append("\n");
			component++;
		}
		return sb.toString();
	}

	private int findMax(int[] sameComponents) {
		int max = 0;
		for (int i = 0; i < sameComponents.length; i++) {
			if (sameComponents[i] > max) {
				max = sameComponents[i];
			}
		}
		return max;
	}

	/**
	 * Iterates over the first row of the waymatrix and jumps in every row with
	 * a 0 in the first one, and then searches the 1 in this row. Every jump
	 * increases the component counter +1.
	 * 
	 * @return int[]
	 */
	private int[] findComponents(int[][] valuesFromWay) {
		int[] components = new int[adjazentValues.length];
		components[0] = 1; // first element from the wayMatrix must be 1
		int component = 2;
		for (int col = 1; col < adjazentValues.length; col++) {
			if (valuesFromWay[0][col] == 0 && components[col] == 0) {
				components[col] = component;
				iterateOverALine(col, components, component, valuesFromWay);
				component++;
			}
			if (valuesFromWay[0][col] == 1) {
				components[col] = 1;
			}
		}
		return components;
	}

	private void iterateOverALine(int row, int[] sameComponents, int component,
			int[][] valuesFromWay) {
		for (int columns = row; columns < adjazentValues.length; columns++) {
			if (valuesFromWay[row][columns] == 1) {
				sameComponents[columns] = component;
			}
		}
	}

	/**
	 * Iterates over the adjaMAtrix and removes for every iteration only one
	 * vertex. After that the components are recalculated. If the number of
	 * components is increased by 2 (at least ) then this vertex is an
	 * articulation.
	 * 
	 * @return {@link List} A list containing the Articulations
	 */
	private ArrayList<Integer> calculateArticulations() {
		int[][] adja = getValuesFrom(adjazentValues);
		int[][] adjaToChangeWhileIteration = getValuesFrom(adja);
		int[][] newWay = new int[adja.length][adja.length];
		int componentsCounter = findMax(findComponents(calculatedDistAndWay.get(1)));// get the componentscounter from the actual waymatrix
		int componentsCounterToCompareWith = -1;
		ArrayList<Integer> foundArtikulationen = new ArrayList<>();
		boolean foundOne = false;
		for (int row = 0; row < adja.length; row++) {
			for (int column = 0; column < adja.length; column++) {
				if (adjaToChangeWhileIteration[row][column] == 1) {
					adjaToChangeWhileIteration[row][column] = 0;
					adjaToChangeWhileIteration[column][row] = 0;
					foundOne = true;
				}
			}
			if (foundOne) {
				newWay = calculateDistanceAndWay(adjaToChangeWhileIteration)
						.get(1);// calculate a wayMatrix with one vertex less
								// then the actual adja has
				componentsCounterToCompareWith = findMax(findComponents(newWay));
				if (componentsCounter + 1 < componentsCounterToCompareWith) {
					foundArtikulationen.add(row + 1);
				}
			}
			foundOne = false;
			adjaToChangeWhileIteration = getValuesFrom(adja);
		}
		return foundArtikulationen;
	}

	/**
	 * Nearly the same as Articulations, but row = column, because it removes a
	 * Edge only. After every edge a renew of the AdjaMatrix is necessary.
	 * 
	 * @return
	 */
	private ArrayList<String> calculateBridges() {
		int[][] adja = getValuesFrom(adjazentValues);
		int[][] newWay = new int[adja.length][adja.length];
		int componentsCounter = findMax(findComponents(calculatedDistAndWay
				.get(1)));// get the componentscounter from the actual waymatrix
		int componentsCounterToCompareWith = -1;
		ArrayList<String> foundBridges = new ArrayList<>();
		boolean foundOne = false;
		for (int row = 0; row < adja.length; row++) {
			for (int column = row; column < adja.length; column++) {
				adja = getValuesFrom(adja);
				if (adja[row][column] == 1) {
					adja[row][column] = 0;
					adja[column][row] = 0;
					foundOne = true;
					if (foundOne) {
						newWay = calculateDistanceAndWay(
								adja).get(1);// calculate a wayMatrix with one edge less than the actualadja has
						componentsCounterToCompareWith = findMax(findComponents(newWay));
						if (componentsCounter < componentsCounterToCompareWith) {
							foundBridges.add((row + 1 )+ "/" + (column + 1));
						}
					}
					adja[row][column] = 1;
					adja[column][row] = 1;
				}
			}
			foundOne = false;
		}
		return foundBridges;
	}

	private int[][] getValuesFrom(int[][] args) {
		int[][] retVal = new int[args.length][args.length];
		for (int i = 0; i < retVal.length; i++) {
			for (int j = 0; j < retVal.length; j++) {
				retVal[i][j] = args[i][j];
			}
		}
		return retVal;
	}
	private int[][] initMatrixFromAdjaValues(int [][] values, String type) {
		int valueDependsOfType = type.equals("distance") ? 0 : 1;
		int[][] temp = new int[values.length][values.length];
		for (int i = 0; i < adjazentValues.length; i++)
			for (int j = 0; j < adjazentValues.length; j++) {
				if (i == j) {
					temp[i][j] = valueDependsOfType;
				} 
				else if (values[i][j] == 1) {
					temp[i][j] = 1;
				}
				else {
					temp[i][j] = 0;
				}
			}
		return temp;
	}
	
	public int getRadius() {
		return radius;
	}

	public int getDiameter() {
		return diameter;
	}

	public ArrayList<Integer> getCenterVertices() {
		return centerVertices;
	}

	public int[] getEccentricity() {
		return eccentricity;
	}

	public List<int[][]> getCalculatedDistAndWay() {
		return calculatedDistAndWay;
	}

	public int[][] getAdjazentValues() {
		return adjazentValues;
	}

	public String getComponents() {
		return components;
	}

	public ArrayList<Integer> getArtikulationen() {
		return artikulationen;
	}

	public ArrayList<String> getBruecken() {
		return bruecken;
	}

	public boolean getCoherently() {
		return isCoherently;
	}

	
	
}
