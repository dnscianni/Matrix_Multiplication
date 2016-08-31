/**
 * CS 331: Design and Analysis of Algorithms
 * Professor: G. S. Young
 *
 * Programming Assignment #1
 *
 * Program the following algorithms that we covered in the class: 
 * 
 * a. Classical matrix multiplication 
 * b. Divide-and-conquer matrix multiplication 
 * c. Strassen's matrix multiplication 
 * 
 * In order to obtain more accurate results, the algorithms should be tested with the 
 * same matrices of different sizes many times. The total time spent is then divided by the 
 * number of times the algorithm is performed to obtain the time taken to solve the given 
 * instance. Let the matrix size be n x n. Carry out a complete test of your algorithms with n 
 * = 2, 4, 8, 16, 32, 64, 128, 256, … (up to the largest size of n that your computer can 
 * handle) 
 *
 * David Scianni
 */
package edu.csupomona.cs.cs331.proj_1;

import java.util.Random;

/**
 * MatrixMultiplication is used to create and multiply matrices based on three
 * algorithms: The classic method, the divide and conquer method, and Strassen's
 * method.
 * 
 * @author David Scianni
 * 
 */
public class MatrixMultiplication {

	/**
	 * Main runs an infinite loop that will run based on what the computer can
	 * handle. First, it will generate n, which will increase exponentially by
	 * 2^i. Then it will generate 2 random matrices of size n x n, and will run
	 * each multiplication algorithm 30 times, while adding the time spent
	 * running the algorithms, and finally divinging that number by 30 to find
	 * the average time spent for each algorithm.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		final int TIMES = 30;
		int n;
		long startTime, endTime;
		long totalTimeC = 0;
		long totalTimeDC = 0;
		long totalTimeS = 0;
		int[][] A, B;

		for (int i = 1; i > 0; i++) {
			n = (int) Math.pow(2, i);

			A = generateMatrix(n);
			B = generateMatrix(n);

			for (int j = 0; j < TIMES; j++) {
				startTime = System.nanoTime();
				classicMM(A, B, A.length);
				endTime = System.nanoTime();
				totalTimeC += endTime - startTime;

				startTime = System.nanoTime();
				divideAndConquerMM(A, B, A.length);
				endTime = System.nanoTime();
				totalTimeDC += endTime - startTime;

				startTime = System.nanoTime();
				strassenMM(A, B, A.length);
				endTime = System.nanoTime();
				totalTimeS += endTime - startTime;
			}

			totalTimeC = totalTimeC / TIMES;
			totalTimeDC = totalTimeDC / TIMES;
			totalTimeS = totalTimeS / TIMES;

			System.out
					.println("For n="
							+ n
							+ ": \n\tClassic Matrix Multiplication time: "
							+ totalTimeC
							+ " nanoseconds.\n\tDivide and Conquer Matrix Multiplication time: "
							+ totalTimeDC
							+ " nanoseconds.\n\tStrassen's Matrix Multiplication time: "
							+ totalTimeS + " nanoseconds.\n");
		}
	}

	/**
	 * This method will generate a matrix of size n x n, filled with random
	 * numbers between 0 and 100
	 * 
	 * @param n
	 *            The size of the matrix
	 * @return an array of size n x n
	 */
	public static int[][] generateMatrix(int n) {
		Random r = new Random();
		int[][] matrix = new int[n][n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				matrix[i][j] = r.nextInt(100);
			}
		}
		return matrix;
	}

	/**
	 * Used for testing purposes, this method will display the matrix sent as an
	 * argument
	 * 
	 * @param matrix
	 *            the matrix to be displayed
	 * @param n
	 *            the size of the matrix
	 */
	public static void displayMatrix(int[][] matrix, int n) {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				System.out.printf("%10d", matrix[i][j]);
			}
			System.out.println();
		}
	}

	/**
	 * Will perform classic matrix multiplication using 3 nested for loops
	 * 
	 * @param A
	 *            One matrix to be multiplied
	 * @param B
	 *            Another matrix to be multiplied
	 * @param n
	 *            the size of the matrix
	 * @return a new array C which is the result of the matrix multiplication
	 */
	public static int[][] classicMM(int[][] A, int[][] B, int n) {
		int[][] C = new int[n][n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				C[i][j] = 0;
			}
		}

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				for (int k = 0; k < n; k++) {
					C[i][j] += A[i][k] * B[k][j];
				}
			}
		}
		return C;
	}

	/**
	 * Will perform divide and conquer matrix multiplication by recursively
	 * calling itself on smaller matrices made up of 1/4 of the original matrix
	 * 
	 * @param A
	 *            One matrix to be multiplied
	 * @param B
	 *            Another matrix to be multiplied
	 * @param n
	 *            the size of the matrix
	 * @return a new array C which is the result of the matrix multiplication
	 */
	public static int[][] divideAndConquerMM(int[][] A, int[][] B, int n) {
		int[][] C = new int[n][n];

		if (n == 1) {
			C[0][0] = A[0][0] * B[0][0];
			return C;
		} else {
			int[][] A11 = new int[n / 2][n / 2];
			int[][] A12 = new int[n / 2][n / 2];
			int[][] A21 = new int[n / 2][n / 2];
			int[][] A22 = new int[n / 2][n / 2];
			int[][] B11 = new int[n / 2][n / 2];
			int[][] B12 = new int[n / 2][n / 2];
			int[][] B21 = new int[n / 2][n / 2];
			int[][] B22 = new int[n / 2][n / 2];

			deconstructMatrix(A, A11, 0, 0);
			deconstructMatrix(A, A12, 0, n / 2);
			deconstructMatrix(A, A21, n / 2, 0);
			deconstructMatrix(A, A22, n / 2, n / 2);
			deconstructMatrix(B, B11, 0, 0);
			deconstructMatrix(B, B12, 0, n / 2);
			deconstructMatrix(B, B21, n / 2, 0);
			deconstructMatrix(B, B22, n / 2, n / 2);

			int[][] C11 = addMatrix(divideAndConquerMM(A11, B11, n / 2),
					divideAndConquerMM(A12, B21, n / 2), n / 2);
			int[][] C12 = addMatrix(divideAndConquerMM(A11, B12, n / 2),
					divideAndConquerMM(A12, B22, n / 2), n / 2);
			int[][] C21 = addMatrix(divideAndConquerMM(A21, B11, n / 2),
					divideAndConquerMM(A22, B21, n / 2), n / 2);
			int[][] C22 = addMatrix(divideAndConquerMM(A21, B12, n / 2),
					divideAndConquerMM(A22, B22, n / 2), n / 2);

			constructMatrix(C11, C, 0, 0);
			constructMatrix(C12, C, 0, n / 2);
			constructMatrix(C21, C, n / 2, 0);
			constructMatrix(C22, C, n / 2, n / 2);
		}

		return C;
	}

	/**
	 * Will use the strassenMMHelper method to multiply the two matrices
	 * 
	 * @param A
	 *            One matrix to be multiplied
	 * @param B
	 *            Another matrix to be multiplied
	 * @param n
	 *            the size of the matrix
	 * @return a new array C which is the result of the matrix multiplication
	 */
	public static int[][] strassenMM(int[][] A, int[][] B, int n) {
		int[][] C = new int[n][n];
		strassenMMHelper(A, B, C, n);
		return C;
	}

	/**
	 * Creates 7 new matrices P - V, based on Strassen's algorithm which will be
	 * used to find the matrix C, which is the result of the multiplication of A
	 * and B.
	 * 
	 * @param A
	 *            One matrix to be multiplied
	 * @param B
	 *            Another matrix to be multiplied
	 * @param C
	 *            the result of the matrix multiplication
	 * @param n
	 *            the size of the matrix
	 */
	public static void strassenMMHelper(int[][] A, int[][] B, int[][] C, int n) {

		if (n == 2) {
			C[0][0] = (A[0][0] * B[0][0]) + (A[0][1] * B[1][0]);
			C[0][1] = (A[0][0] * B[0][1]) + (A[0][1] * B[1][1]);
			C[1][0] = (A[1][0] * B[0][0]) + (A[1][1] * B[1][0]);
			C[1][1] = (A[1][0] * B[0][1]) + (A[1][1] * B[1][1]);
		} else {
			int[][] A11 = new int[n / 2][n / 2];
			int[][] A12 = new int[n / 2][n / 2];
			int[][] A21 = new int[n / 2][n / 2];
			int[][] A22 = new int[n / 2][n / 2];
			int[][] B11 = new int[n / 2][n / 2];
			int[][] B12 = new int[n / 2][n / 2];
			int[][] B21 = new int[n / 2][n / 2];
			int[][] B22 = new int[n / 2][n / 2];

			int[][] P = new int[n / 2][n / 2];
			int[][] Q = new int[n / 2][n / 2];
			int[][] R = new int[n / 2][n / 2];
			int[][] S = new int[n / 2][n / 2];
			int[][] T = new int[n / 2][n / 2];
			int[][] U = new int[n / 2][n / 2];
			int[][] V = new int[n / 2][n / 2];

			deconstructMatrix(A, A11, 0, 0);
			deconstructMatrix(A, A12, 0, n / 2);
			deconstructMatrix(A, A21, n / 2, 0);
			deconstructMatrix(A, A22, n / 2, n / 2);
			deconstructMatrix(B, B11, 0, 0);
			deconstructMatrix(B, B12, 0, n / 2);
			deconstructMatrix(B, B21, n / 2, 0);
			deconstructMatrix(B, B22, n / 2, n / 2);

			strassenMMHelper(addMatrix(A11, A22, n / 2),
					addMatrix(B11, B22, n / 2), P, n / 2);
			strassenMMHelper(addMatrix(A21, A22, n / 2), B11, Q, n / 2);
			strassenMMHelper(A11, subtractMatrix(B12, B22, n / 2), R, n / 2);
			strassenMMHelper(A22, subtractMatrix(B21, B11, n / 2), S, n / 2);
			strassenMMHelper(addMatrix(A11, A12, n / 2), B22, T, n / 2);
			strassenMMHelper(subtractMatrix(A21, A11, n / 2),
					addMatrix(B11, B12, n / 2), U, n / 2);
			strassenMMHelper(subtractMatrix(A12, A22, n / 2),
					addMatrix(B21, B22, n / 2), V, n / 2);

			int[][] C11 = addMatrix(
					subtractMatrix(addMatrix(P, S, P.length), T, T.length), V,
					V.length);
			int[][] C12 = addMatrix(R, T, R.length);
			int[][] C21 = addMatrix(Q, S, Q.length);
			int[][] C22 = addMatrix(
					subtractMatrix(addMatrix(P, R, P.length), Q, Q.length), U,
					U.length);

			constructMatrix(C11, C, 0, 0);
			constructMatrix(C12, C, 0, n / 2);
			constructMatrix(C21, C, n / 2, 0);
			constructMatrix(C22, C, n / 2, n / 2);
		}
	}

	/**
	 * Creates a new matrix based off of part of another matrix
	 * 
	 * @param initialMatrix
	 *            the initial matrix
	 * @param newMatrix
	 *            the new matrix created from the initial matrix
	 * @param a
	 *            the initial row position of initialMatrix used when creating
	 *            newMatrix
	 * @param b
	 *            the initial column position of initialMatrix used when
	 *            creating newMatrix
	 */
	private static void constructMatrix(int[][] initialMatrix,
			int[][] newMatrix, int a, int b) {

		int y = b;

		for (int i = 0; i < initialMatrix.length; i++) {
			for (int j = 0; j < initialMatrix.length; j++) {
				newMatrix[a][y++] = initialMatrix[i][j];
			}
			y = b;
			a++;
		}
	}

	/**
	 * Adds two matrices together
	 * 
	 * @param A
	 *            One matrix to be added
	 * @param B
	 *            Another matrix to be added
	 * @param n
	 *            the size of the matrix
	 * @return a new array C which is the result of the matrix addition
	 */
	private static int[][] addMatrix(int[][] A, int[][] B, int n) {

		int[][] C = new int[n][n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				C[i][j] = A[i][j] + B[i][j];
			}
		}
		return C;
	}

	/**
	 * Subtracts two matrices
	 * 
	 * @param A
	 *            One matrix to be subtracted
	 * @param B
	 *            Another matrix to be subtracted
	 * @param n
	 *            the size of the matrix
	 * @return a new array C which is the result of the matrix subtraction
	 */
	private static int[][] subtractMatrix(int[][] A, int[][] B, int n) {

		int[][] C = new int[n][n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				C[i][j] = A[i][j] - B[i][j];
			}
		}
		return C;
	}

	/**
	 * Creates a new matrix based off of part of another matrix
	 * 
	 * @param initialMatrix
	 *            the initial matrix
	 * @param newMatrix
	 *            the new matrix created from the initial matrix
	 * @param a
	 *            the initial row position of initialMatrix used when creating
	 *            newMatrix
	 * @param b
	 *            the initial column position of initialMatrix used when
	 *            creating newMatrix
	 */
	private static void deconstructMatrix(int[][] initialMatrix,
			int[][] newMatrix, int a, int b) {

		int y = b;
		for (int i = 0; i < newMatrix.length; i++) {
			for (int j = 0; j < newMatrix.length; j++) {
				newMatrix[i][j] = initialMatrix[a][y++];
			}
			y = b;
			a++;
		}
	}

}
