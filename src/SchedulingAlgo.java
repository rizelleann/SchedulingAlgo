/*RIZELLE ANN D. BAHIN		CMSC 125-B   LAB2*/

import java.io.*;
import java.util.*;

public class SchedulingAlgo{
	
    public Process proc;
	public static ArrayList<Process> processes = new ArrayList<Process>();
	int numOfProcesses = 0;
	Double awt = 0.0;

	
	public void load_data(String filename) throws IOException{
		BufferedReader br = new BufferedReader (new FileReader(filename));
		String line;
		StringTokenizer st;
		String[] lineSplit = new String[4];
		while((line = br.readLine()) != null) {
			 proc = new Process();
			if(numOfProcesses != 0) {
				 st = new StringTokenizer(line);
				 while(st.hasMoreTokens()) {
					 proc.setProcNum(Integer.parseInt(st.nextToken()));
					 proc.setArrival(Integer.parseInt(st.nextToken()));
					 proc.setBurst(Integer.parseInt(st.nextToken()));
					 proc.setPriority(Integer.parseInt(st.nextToken()));
				 }
				 processes.add(proc);				
			}
			numOfProcesses++;
		}			
	}
	
	public void fcfs(ArrayList<Process> processes) {
		awt = 0.0;
		System.out.println("\n\n------------------------- FCFS ------------------------- ");
		System.out.println("\n\n\tProcess\t\tBurst Time(ms)\tWaiting Time(ms)" );		
		int[][] fcfsArr = new int[numOfProcesses][2];
		int sum = 0;
		for(int i = 0; i < processes.size(); i ++) {
			fcfsArr[i][0] = i + 1;
			if(i == 0) {
				fcfsArr[0][1] = 0;
				sum = fcfsArr[0][1];
				processes.get(0).setWaitingTime(0);
			} else {
				sum +=  processes.get(i-1).getBurst();
				processes.get(i).setWaitingTime(sum);
				fcfsArr[i][1] = sum;
			}
			awt += sum;
		}
		
		for(Process e: processes) {
			System.out.print("\t"+e.getProcNum() + "\t\t  ");
			System.out.println(e.getBurst() + "\t\t " + e.getWaitingTime());
		}
		awt = (awt / processes.size());
		System.out.println("\n\t   Average waiting time: " + awt + " ms");
		
	}
	
	public void sjf(ArrayList<Process> processes) {
		awt = 0.0;
		int sum = 0;
		System.out.println("\n\n========================= SJF =========================");
		System.out.println("\n\n\tProcess\t\tBurst Time(ms)\tWaiting Time(ms)" );		
		Collections.sort(processes, new CompareBurst());
		
		for(int i = 0; i < processes.size(); i++) {
			if(i == 0) {
				sum = 0;
				processes.get(0).setWaitingTime(sum);
			} else {
				sum += processes.get(i-1).getBurst();
				processes.get(i).setWaitingTime(sum);
			}
			awt += sum;
		}
		
		//prints according to burst time(from least to greatest)
		/*for(Process e: processes){
			System.out.print("\t "+ e.getProcNum() + "\t\t ");
            System.out.println(e.getBurst() + "\t\t " + e.getWaitingTime());
        }*/
		
		Collections.sort(processes, new CompareProcNum());
		for(Process e: processes) {
			System.out.print("\t"+e.getProcNum() + "\t\t  ");
			System.out.println(e.getBurst() + "\t\t " + e.getWaitingTime());
		}
		awt = (awt / processes.size());
		System.out.println("\n\t   Average waiting time: " + awt + " ms");
		
	}
	
	public void roundRobin(ArrayList<Process> processes) {
		System.out.println("\n\n+++++++++++++++++++++++++ Round Robin ++++++++++++++++++++++++");
		System.out.println("\n\n\tProcess\t\tBurst Time(ms)\tWaiting Time(ms)" );
		ArrayList<Process> temp = new ArrayList<>();		
		awt = 0.0;
		int time = 0;
		for(Process p : processes){
			temp.add(new Process(p.getProcNum(), p.getArrival(), p.getBurst(), p.getPriority(), 0, 0));
		}

		for(int i = 0; i < temp.size(); ) {
			for(int j = 0; j < temp.size(); j++) {
				if(temp.get(j).getBurst() > 0) {
					if((temp.get(j).getBurst() - 4) <= 0) {
						temp.get(j).setWaitingTime(time);
						time += temp.get(j).getBurst();
					} else {
						temp.get(j).setNumOfOccurence(temp.get(j).getNumOfOccurence() + 1);
						time += 4;
					}
					temp.get(j).setBurst(temp.get(j).getBurst() - 4);
					if(temp.get(j).getBurst() <= 0) {
						i++;
					}
				}
			}
		}		
		
		for(int i = 0; i < temp.size(); i ++) {
			temp.get(i).setWaitingTime(temp.get(i).getWaitingTime() - (temp.get(i).getNumOfOccurence() * 4));
			processes.get(i).setWaitingTime(temp.get(i).getWaitingTime());
			awt += temp.get(i).getWaitingTime();	
		}

		for(Process e: processes) {
			System.out.print("\t"+e.getProcNum() + "\t\t  ");
			System.out.println(e.getBurst() + "\t\t " + e.getWaitingTime());
		}
		
		awt = (awt / processes.size());
		System.out.println("\n\t   Average waiting time: " + awt + " ms");
	}
	
	public void srpt(ArrayList<Process> processes){
		System.out.println("\n\n:::::::::::::::::::::::::: SRPT ::::::::::::::::::::::::::");
		System.out.println("\n\n\tProcess\t\tBurst Time(ms)\tWaiting Time(ms)" );		
		ArrayList<Process> temp = new ArrayList<Process>();
		ArrayList<Process> que = new ArrayList<Process>();
		ArrayList<Process> finished = new ArrayList<>();
		awt = 0.0;
		int time = 0;
		for(Process p : processes){
			temp.add(new Process(p.getProcNum(), p.getArrival(), p.getBurst(), p.getPriority()));	
		}

		do {
			
			for(int i = 0; i < temp.size(); i++) {
				if(temp.get(i).getArrival() == time) {
					que.add(temp.get(i));
				} 
			}
			
			Collections.sort(que, new CompareBurst());
		
			if(que.get(0).getBurst() > 0) {
				que.get(0).setBurst(que.get(0).getBurst()-1);
				for(int j = 1; j < que.size(); j++) {
					que.get(j).setWaitingTime(que.get(j).getWaitingTime() + 1);
				}
			}
			
			if(que.get(0).getBurst() == 0) {
				finished.add(que.get(0));
				que.remove(0);
			}
			time++;
			
		} while(que.size() > 0);

		for(Process p: finished) {
			awt += p.getWaitingTime();
		}
		
		Collections.sort(finished, new CompareProcNum());//sorts back according to Process Num
		
		for(int i = 0; i < finished.size(); i++) {
			processes.get(i).setWaitingTime(finished.get(i).getWaitingTime());
		}
		
		for(Process e: processes) {
			System.out.print("\t"+e.getProcNum() + "\t\t  ");
			System.out.println(e.getBurst() + "\t\t " + e.getWaitingTime());
		}
			
		awt = (awt / finished.size());
		System.out.println("\n\t   Average waiting time: " + awt + " ms");
	}
	
	
	public void priority(ArrayList<Process> processes) {
		awt = 0.0;
		System.out.println("\n\n************************* Priority *************************");
		System.out.println("\n\n\tProcess\t\tBurst Time(ms)\tWaiting Time(ms)" );		
		int sum = 0;
		Collections.sort(processes, new ComparePriority());
		for(int i = 0; i < processes.size(); i++) {
			if(i == 0) {
				processes.get(0).setWaitingTime(0);
			} else {
				sum += processes.get(i - 1).getBurst();
				processes.get(i).setWaitingTime(sum);
			}
			awt += sum;
		}
		//prints according to priority
		/*for(Process e: processes) {
			System.out.print(e.getProcNum() + "\t\t " + e.getBurst() + "\t\t ");
			System.out.println(e.getWaitingTime());
		}*/
		
		Collections.sort(processes, new CompareProcNum()); // sorts again by process num
		for(Process e: processes) {
			System.out.print("\t"+e.getProcNum() + "\t\t  ");
			System.out.println(e.getBurst() + "\t\t " + e.getWaitingTime());
		}
		
		awt = (awt / processes.size());
		System.out.println("\n\t   Average waiting time: " + awt + " ms");
	}
	
	public static void main(String[] args) throws IOException {
		SchedulingAlgo algo = new SchedulingAlgo();
		int num;
		Scanner sc = new Scanner(System.in);
		System.out.println("Choose which file to read ... ");
		System.out.println("[1] process1.txt\t\t\t [2] process2.txt");
		System.out.print(">>>>  ");
		
		num = sc.nextInt();
		switch(num) {
			case 1:
				algo.load_data("process1.txt");
				break;
			case 2:
				algo.load_data("process2.txt");
				break;
			default:
				System.out.println("input is not one of the choices. . .");
				break;	
		}
	
		int choice;
		System.out.println("Choose Scheduling Algorithm");
		System.out.println("[1] FCFS\t [2] SJF\t\t [3] SRPT\n[4] PRIORITY\t [5] ROUND ROBIN\t [6] ALL");
		System.out.print(">>>>  ");
		choice = sc.nextInt();
		
		switch(choice) {
			case 1:	
				algo.fcfs(processes);
				break;
			case 2:
				algo.sjf(processes);
				break;
			case 3:
				algo.srpt(processes);
				break;
			case 4:
				algo.priority(processes);
				break;
			case 5:
				algo.roundRobin(processes);
				break;
			case 6:
				algo.fcfs(processes);
				algo.sjf(processes);
				algo.srpt(processes);
				algo.priority(processes);
				algo.roundRobin(processes);
				
				break;
			default:
				System.out.println("Input is not one of the choices");
				break;
		}
	}
}

class ComparePriority implements Comparator<Process>{

	@Override
	public int compare(Process o1, Process o2) {
		// TODO Auto-generated method stub
		return o1.getPriority() - o2.getPriority();
	}
	
}
class CompareBurst implements Comparator<Process> {

	@Override
	public int compare(Process o1, Process o2) {
		// TODO Auto-generated method stub
		return o1.getBurst() - o2.getBurst();
	}
	
}

class CompareProcNum implements Comparator<Process> {

	@Override
	public int compare(Process o1, Process o2) {
		// TODO Auto-generated method stub
		return o1.getProcNum() - o2.getProcNum();
	}
	
}

class CompareArrival implements Comparator<Process> {

	@Override
	public int compare(Process o1, Process o2) {
		// TODO Auto-generated method stub
		return o1.getArrival() - o2.getArrival();
	}
	
}

class Process{
	
	private int procNum;
	private int arrival;
	private int burst;
	private int priority;
	private int waitingTime;
	private int numOfOccurence;
	
	public Process(int procNum, int arrival, int burst, int priority) {
		this.procNum = procNum;
		this.arrival = arrival;
		this.burst = burst;
		this.priority = priority;
		this.waitingTime = 0;
	}
	public Process(int procNum, int arrival, int burst, int priority, int waitingTime, int numOfOccurence) {
		this.procNum = procNum;
		this.arrival = arrival;
		this.burst = burst;
		this.priority = priority;
		this.waitingTime = waitingTime;
		this.numOfOccurence = numOfOccurence;
	}
	
	
	public Process() {}
	
	public void setProcNum(int procNum) {
		this.procNum = procNum;
	}
	
	public void setArrival(int arrival) {
		this.arrival = arrival;
	}
	
	public void setBurst(int burst) {
		this.burst = burst;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public void setWaitingTime(int waitingTime) {
		this.waitingTime = waitingTime;
	}
	
	public void setNumOfOccurence(int numOfOccurence) {
		this.numOfOccurence = numOfOccurence;
	}
	
	public int getProcNum() {
		return procNum;
	}
	

	public int getArrival() {
		return arrival;
	}
	

	public int getBurst() {
		return burst;
	}
	

	public int getPriority() {
		return priority;
	}
	
	public int getWaitingTime() {
		return waitingTime;
	}
	
	public int getNumOfOccurence() {
		return numOfOccurence;
	}
}

