/*
 * Randy Andrews
 * COSC 311
 * Project1
 * Due 10/29/19
 * 
 * This project is meant to create a shop that uses a random number generator and creates
 * a specific population and determine how many servers are needed to keep wait time below a certain time.  
 * using a single queue/multiple server system. This Program uses Javas Random, linked list and queue
 * methods. I also used Davids Poisson for the distribution process.
 */
import java.util.*;

public class DonutShop {
	static int maxWait = 0, minWait = 0, total = 0;
	static int inService = 0, completedService = 0, demand = 0;
	static double avgTime = 0;
	// seeded random
	static Random r = new Random(97);

	public static int choice() {
		Scanner k = new Scanner(System.in);
		System.out.println("Type 1 for Heavy Demand or 2 for Low Demand");
		int answer = k.nextInt();
		// sends the mean and servers depending on choice of heavy or low
		if (answer == 1) {
			avgTime = 2;
			demand = 13;
			return 2;
		} else if (answer == 2) {
			avgTime = 0.25;
			demand = 4;
			return 8;
		} else
			return (Integer) null;
	}
	// number Generator from David Knuth
	public static int getPoissonRandom(double mean) {
		double L = Math.exp(-mean);
		double p = 1.0;
		int k = 0;
		do {
			k++;
			p *= Math.random();
		} while (p > L);
		return k - 1;
	}
	//
	public static void server(LinkedList<Integer> serve, Queue<NodeCus> list) {
		for (int i = 0; i < serve.size(); i++) {
			if (list.isEmpty())
				return;
			else if (serve.get(i) == null) {
				NodeCus cust = list.remove();
				serve.set(i, cust.service);
				inService++;
			} else if (serve.get(i) == 0) {
				NodeCus cust = list.remove();
				serve.set(i, cust.service);
				completedService++;
			}
		}
	}
	// finds service time
	public static void ServeTime(LinkedList<Integer> serve) {
		for (int j = 0; j < serve.size(); j++) {
			if (serve.get(j) != null) {
				serve.set(j, serve.get(j) - 1); // Decrement service times
			}

		}
	}
	// adds to the customer list
	public static void addTolist(int numOfCust, Queue<NodeCus> list) {
		for (int j = 0; j < numOfCust; j++) { // Adds customers to list
			list.add(new NodeCus(r.nextInt(demand)));
		}
	}
	// adds to wait
	public static void delay(Queue<NodeCus> list) {
		for (NodeCus cust : list) {
			cust.setWait(cust.getWait() + 1);
		}
	}
	// gets min
	public static void min(int cust) {
		int max = cust;
		if (max < minWait)
			minWait = max;
	}
	// gets max
	public static void max(int cust) {
		int min = cust;
		if (min > maxWait)
			maxWait = min;
	}
	// finds max min and average
	public static int maxMinAvg(Queue<NodeCus> list) {
		for (int j = 0; j < list.size(); j++) {
			NodeCus cust = list.remove();
			max(cust.wait);
			min(cust.wait);
			total += cust.wait;
			list.add(cust);
		}
		if (list.size() == 0)
			return 0;
		else {
			int avg = total / list.size();
			return avg;
		}
	}
	// main
	public static void main(String[] args) {
		LinkedList<Integer> serve = new LinkedList<Integer>();
		Queue<NodeCus> list = new LinkedList<NodeCus>();
		Scanner k = new Scanner(System.in);
		String answer = "";
		do {
			int servers = choice();
			while (servers > 0) {
				serve.add(null);
				servers--;
			}
			for (int i = 0; i < 20; i++) {
				int numOfCust = getPoissonRandom(2), aver;
				server(serve, list);
				delay(list);
				addTolist(numOfCust, list);
				aver = maxMinAvg(list);
				System.out.println("\n//////////////////////////////////////////////");
				System.out.println("Tick#: " + i);
				System.out.println("\tCustomers in service: " + inService);
				System.out.println("\tCustomers with completed service: " + completedService);
				System.out.println("\tCustomers in queue: " + list.size());
				System.out.println("\tTotal Wait Time: " + total);
				System.out.println("\tWait Time: " + minWait + ", " + aver + ", " + maxWait);
				System.out.println("////////////////////////////////////////////////\n");
				total = 0;
				ServeTime(serve);
			}
			System.out.println("Do it again, type Yes (aything else for no): ");
			answer = k.next();
		} while (answer.equalsIgnoreCase("yes"));
	}
}