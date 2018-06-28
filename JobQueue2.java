import java.io.*;
import java.util.StringTokenizer;

public class JobQueue2 {
    private int numWorkers;
    private int[] jobs;

    private int[] assignedWorker;
    private long[] startTime;

    private FastScanner in;
    private PrintWriter out;

    public static void main(String[] args) throws IOException {
        new JobQueue2().solve();
    }

	// Worker Object
	private class Worker {
		
		private int assignedJob;
		private long startTime;

		public Worker(int job, long time) {
			this.assignedJob = job;
			this.startTime = time;
		}

		public int getJob() {
			return assignedJob;
		}

		public long getTime() {
			return startTime;
		}

		public void setJob(int job) {
			this.assignedJob = job;
		}

		public void setTime(long time) {
			this.startTime = time;
		}

	}

	// Array of Workers
	private java.util.List<Worker> nextFreeTime;

    private void readData() throws IOException {
        numWorkers = in.nextInt();
        int m = in.nextInt();
        jobs = new int[m];

	// List of workers
	nextFreeTime = new java.util.ArrayList<>(numWorkers);
	int job = 0;

        for (int i = 0; i < m; ++i) {
		job = in.nextInt();
		if( i < numWorkers ) {
			Worker worker = new Worker(i,job);
			nextFreeTime.add(i,worker);
		}
            jobs[i] = job;
        }
    }

    private void writeResponse() {
        for (int i = 0; i < jobs.length; ++i) {
            out.println(assignedWorker[i] + " " + startTime[i]);
        }
    }

    private void assignJobs() {

	boolean working = true;
	
	int time = 0;
	int duration = 0;
	int jobCount = 0;
	int size = nextFreeTime.size();

        while ( working ) {
		while ( nextFreeTime.get(0).getTime() <= time && jobCount < jobs.length ) {

			while( jobs[jobCount] == 0 && jobCount < jobs.length ) {
				assignedWorker[jobCount] = (int)nextFreeTime.get(0).getJob();
				startTime[jobCount] = time;
				jobCount++;
			}	
		
			duration = jobs[jobCount] + time;
			nextFreeTime.get(0).setTime(duration);
				
			assignedWorker[jobCount] = (int)nextFreeTime.get(0).getJob();
				
			for( int i = size/2; i >= 0; --i ) {
				siftDown( i );
			}
				
			startTime[jobCount] = time;
				
			jobCount++;
				
		}
		time++;
			
		if( jobCount == jobs.length ) {
			working = false;
		}
        }
}

	// need to heap the nextFreeTime[]
	private void siftDown( int i ) {
		int size = nextFreeTime.size();
		int minIndex = i;

		int left = 2*i+1;
		if( left < size ) {
		if( nextFreeTime.get(left).getTime() <= nextFreeTime.get(minIndex).getTime() ) {
			if( nextFreeTime.get(left).getTime() == nextFreeTime.get(minIndex).getTime() ) {
				if( nextFreeTime.get(left).getJob() < nextFreeTime.get(minIndex).getJob() ) {	
					minIndex = left;
				}
				
			} else {
				minIndex = left;
			}
		}
		}

		int right = 2*i+2;
		if( right < size ) {
		if( nextFreeTime.get(right).getTime() <= nextFreeTime.get(minIndex).getTime() ) {
			if( nextFreeTime.get(right).getTime() == nextFreeTime.get(minIndex).getTime() ) {
				if( nextFreeTime.get(right).getJob() < nextFreeTime.get(minIndex).getJob() ) {
					minIndex = right;
				}				
			} else {
				minIndex = right;
			}		
		}
		}

		if( i != minIndex ) {
			Worker worker = (Worker)nextFreeTime.get(i);
			Worker newWorker = (Worker)nextFreeTime.get(minIndex);

			nextFreeTime.set(i, newWorker);
			nextFreeTime.set(minIndex, worker);
		
			siftDown( minIndex );
		}
	}
	

    public void solve() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        readData();
        assignJobs();
        writeResponse();
        out.close();
    }

    static class FastScanner {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastScanner() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}
