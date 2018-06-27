import java.io.*;
import java.util.StringTokenizer;

public class JobQueue {
    
    private int[] assignedWorker;
    private long[] startTime;

    private FastScanner in;
    private PrintWriter out;

	private long[][] nextFreeTime;
	//private int[] workers;
	private int numWorkers;
	private int[] jobs;

    
	private int size;
	//private long minWorker;

    public static void main(String[] args) throws IOException {
        new JobQueue().solve();
    }

	/* Worker Object
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
			this.assignedTime = time;
		}

	}
	*/
	// Array of Workers
	//private List<Worker> workers;

    private void readData() throws IOException {
        numWorkers = in.nextInt();
        int m = in.nextInt();
        jobs = new int[m];
	//workers = new int[numWorkers];
	nextFreeTime = new long[numWorkers][2];
	int job = 0;
        for (int i = 0; i < m; ++i) {
		job = in.nextInt();
		if ( i < numWorkers ) {
			nextFreeTime[i][0] = i;		// workerID
			nextFreeTime[i][1] = job;	// job duration 
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

        assignedWorker = new int[jobs.length];
        startTime = new long[jobs.length];
        
	
	boolean working = true;
	
	int time = 0;
	int duration = 0;
	int jobCount = 0;
	size = numWorkers;
	
        while ( working ) {

		/*
		if( time == 0 ) {
			int k = 0;
			for( k = 0; k < numWorkers; k++ ) {
				if( k < jobs.length ) {
					nextFreeTime[k] = jobs[k];	//assign job k to worker k
					siftUp( k );	// re-Heap
					assignedWorker[k] = k;
					startTime[k] = time;
				}
			}
			time++;
			jobCount = k;
		}
		else {
		*/
			/*
			while ( workers[minWorker] <= time && jobCount < jobs.length ) {

				while( jobs[jobCount] == 0 && jobCount < jobs.length ) {
					assignedWorker[jobCount] = workers[minWorker];
					startTime[jobCount] = time;
					jobCount++;
				}	
		
				duration = jobs[jobCount] + time;
				nextFreeTime[0] = duration;
				assignedWorker[jobCount] = workers[minWorker];
				siftDown( 0 );
				//siftUp(nextFreeTime.length -1);
				startTime[jobCount] = time;
				
				jobCount++;
				
			}
			time++;
			*/
			
			while ( nextFreeTime[0][1] <= time && jobCount < jobs.length ) {

				//minWorker = nextFreeTime[0][1];

				if( jobs[jobCount] == 0 ) {
					while( jobs[jobCount] == 0 && jobCount < jobs.length ) {
						assignedWorker[jobCount] = (int)nextFreeTime[0][1];
						startTime[jobCount] = time;
						jobCount++;
					}	
				}
		
				
				duration = jobs[jobCount] + time;
				nextFreeTime[0][1] = duration;
				
				assignedWorker[jobCount] = (int)nextFreeTime[0][0];
				
				siftDown( 0 );
				

					//siftUp(nextFreeTime.length -1);
				startTime[jobCount] = time;

				jobCount++;
				
			}
			time++;
			
		//}

		if( jobCount == jobs.length ) {
			working = false;
		}
        }

    }
	/*
	private void bottomUpHeap() {

		int i = nextFreeTime.length/2;

		for( i = nextFreeTime.length/2; i >= 0; i-- ) {
			int k = i;
			long value = nextFreeTime[k];
			boolean heap = false;

			while( !heap && 2*k <= nextFreeTime.length ) {
				int j = 2*k;
				if( j < nextFreeTime.length ) {
					if( nextFreeTime[j] < nextFreeTime[j+1] ) {
						j = j+1;
					}
				}

				if( value <= nextFreeTime[j] ) {
					heap = true;
				} else {
					long temp = nextFreeTime[k];
					nextFreeTime[k] = nextFreeTime[j];
					nextFreeTime[j] = temp;
					k = j;
				}
				nextFreeTime[k] = value;
			}
		}
	}
	*/
	
	/*
	private void siftUp( int i ) {
		
		if( i > 0 ) {		
			int parent = (i-1)/2;
			if( nextFreeTime[parent] > nextFreeTime[i] ) {
				long temp = nextFreeTime[parent];
				nextFreeTime[parent] = nextFreeTime[i];
				nextFreeTime[i] = temp;

				int temp2 = workers[parent];
				workers[parent] = workers[i];
				workers[i] = temp2;
			} else if ( nextFreeTime[parent] == nextFreeTime[i] ) {
				if( workers[parent] > workers[i] ) {
					long temp = nextFreeTime[parent];
					nextFreeTime[parent] = nextFreeTime[i];
					nextFreeTime[i] = temp;

					int temp2 = workers[parent];
					workers[parent] = workers[i];
					workers[i] = temp2;
				}
			}			
			i = parent;
			siftUp(i);
		}

	}
	*/
	
	// need to heap the nextFreeTime[]
	private void siftDown( int i ) {
		
		int minIndex = i;

		int left = 2*i+1;
		if( left < size ) {
		if( nextFreeTime[left][1] <= nextFreeTime[minIndex][1] ) {
			if( nextFreeTime[left][1] == nextFreeTime[minIndex][1] ) {
				if( nextFreeTime[left][0] < nextFreeTime[minIndex][0] ) {	
					minIndex = left;
				}
				
			} else {
				minIndex = left;
			}
		}
		}

		int right = 2*i+2;
		if( right < size ) {
		if( nextFreeTime[right][1] <= nextFreeTime[minIndex][1] ) {
			if( nextFreeTime[right][1] == nextFreeTime[minIndex][1] ) {
				if( nextFreeTime[right][0] < nextFreeTime[minIndex][0] ) {
					minIndex = right;
				}				
			} else {
				minIndex = right;
			}		
		}
		}

		if( i != minIndex ) {
			long temp = nextFreeTime[i][1];
			long oldWorker = nextFreeTime[i][0];

			nextFreeTime[i][0] = nextFreeTime[minIndex][0];
			nextFreeTime[i][1] = nextFreeTime[minIndex][1];

			nextFreeTime[minIndex][1] = temp;
			nextFreeTime[minIndex][0] = oldWorker;

			/*
			int temp2 = workers[i];
			workers[i] = workers[minIndex];
			workers[minIndex] = temp2;
			*/
			
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
