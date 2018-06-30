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

		public Worker( int job, long time) {
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
	int jobLength = 0;
	assignedWorker = new int[m] ;
   	startTime = new long[m];
        for (int i = 0; i < m; i++) {
		jobLength = in.nextInt();
		if( i < numWorkers ) {
			Worker worker = new Worker(i,jobLength);
			nextFreeTime.add(i,worker);
			startTime[i] = 0;
			assignedWorker[i] = i;
		}
            jobs[i] = jobLength;
        }
    }

    private void writeResponse() {
        for (int i = 0; i < jobs.length; ++i) {
            out.println(assignedWorker[i] + " " + startTime[i]);
        }
    }

    private void assignJobs() {

	boolean working = true;
	int size = nextFreeTime.size();
	int time = 0;
	int duration = 0;
	int jobCount = size;

        while ( working ) {
		while ( nextFreeTime.get(0).getTime() == time && jobCount < jobs.length ) {

			while( jobs[jobCount] == 0 && jobCount < jobs.length ) {
				assignedWorker[jobCount] = (int)nextFreeTime.get(0).getJob();
				startTime[jobCount] = time;
				jobCount++;
			}	
		
			Worker oldWorker = nextFreeTime.remove(0);

			duration = jobs[jobCount] + time;
			assignedWorker[jobCount] = (int)oldWorker.getJob();

			Worker worker = new Worker((int)oldWorker.getJob(), duration);

			nextFreeTime.add(worker);
				
			siftUp(size-1);

			startTime[jobCount] = time;
				
			jobCount++;
		}
		time++;
			
		if( jobCount == jobs.length ) {
			working = false;
		}
        }
}

	private void siftUp( int i ) {
		
		if( i > 0 ) {		
			int parent = (i-1)/2;
			if( nextFreeTime.get(parent).getTime() > nextFreeTime.get(i).getTime() ) {
				Worker temp = (Worker)nextFreeTime.get(i);
				nextFreeTime.set(i,nextFreeTime.get(parent));
				nextFreeTime.set(parent,temp);
				i = parent;
				siftUp(i);
			} else if ( nextFreeTime.get(parent).getTime() == nextFreeTime.get(i).getTime() ) {
				if( nextFreeTime.get(parent).getJob() > nextFreeTime.get(i).getJob() ) {
					Worker temp2 = (Worker)nextFreeTime.get(i);
					nextFreeTime.set(i,nextFreeTime.get(parent));
					nextFreeTime.set(parent,temp2);
				}
			}			
			
		}

	}

	// need to heap the nextFreeTime[]
	private void siftDown( int i ) {
		int size = nextFreeTime.size();
		int minIndex = i;
		long time1 = 0;
		long time2 = 0;
		int left = 2*i +1;
		if( left < size ) {
		time1 = nextFreeTime.get(left).getTime();
		time2 = nextFreeTime.get(minIndex).getTime();
		if( time1 <= time2 ) {
			if( time1 == time2 ) {
				if( nextFreeTime.get(left).getJob() < nextFreeTime.get(minIndex).getJob() ) {	
					minIndex = left;
				}
				
			} else {
				minIndex = left;
			}
		}
		}

		int right = 2*i +2;
		if( right < size ) {
		time1 = nextFreeTime.get(right).getTime();
		if( time1 <= time2 ) {
			if( time1 == time2 ) {
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
