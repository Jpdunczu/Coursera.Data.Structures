import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

class Request {
    public Request(int arrival_time, int process_time) {
        this.arrival_time = arrival_time;
        this.process_time = process_time;
    }

    public int arrival_time;
    public int process_time;
}

class Response {
    public Response(boolean dropped, int start_time) {
        this.dropped = dropped;
        this.start_time = start_time;
    }

    public boolean dropped;
    public int start_time;
}

class Buffer {
    public Buffer(int size) {
        this.size_ = size;
	//this.counter = 0;
        this.finish_time_ = new ArrayList<Integer>();
    }

    public Response Process(Request request) {
        // write your code here
	if( finish_time_.isEmpty() ) {
		// the buffer is empty, start processing immediately
		if( request.process_time > 0 ) {
			//counter += request.process_time;
			//finish_time_.add(counter);
			finish_time_.add(request.process_time);
		}
		return new Response(false,request.arrival_time);
	} else if ( size_ > finish_time_.size() ) {
		// Buffer is not empty, but there is room for the next packet.
		if( request.arrival_time < finish_time_.get(finish_time_.size()-1) ) {
			request.arrival_time = finish_time_.get(finish_time_.size()-1);
		}
		//counter += request.process_time;
		finish_time_.add(request.arrival_time + request.process_time);
		Response response = new Response(false,request.arrival_time);
		return response;
	} else {
		// Buffer is full.
		// check to see if the previous packets have finished processing by this packets arrival time.
		if( finish_time_.get(0) <= request.arrival_time ) {
			//counter += request.process_time;
			if( request.arrival_time < finish_time_.get(finish_time_.size()-1) ) {
				request.arrival_time = finish_time_.get(finish_time_.size()-1);
			}
			finish_time_.add(request.arrival_time + request.process_time);
			Response response = new Response(false,request.arrival_time);
			finish_time_.remove(0);
			return response;
		}
	}
        return new Response(true, -1);
    }

    //private int counter;
    private int size_;
    private ArrayList<Integer> finish_time_;
}

class process_packages {
    private static ArrayList<Request> ReadQueries(Scanner scanner) throws IOException {
        int requests_count = scanner.nextInt();
        ArrayList<Request> requests = new ArrayList<Request>();
        for (int i = 0; i < requests_count; ++i) {
            int arrival_time = scanner.nextInt();
            int process_time = scanner.nextInt();
            requests.add(new Request(arrival_time, process_time));
        }
        return requests;
    }

    private static ArrayList<Response> ProcessRequests(ArrayList<Request> requests, Buffer buffer) {
        ArrayList<Response> responses = new ArrayList<Response>();
        for (int i = 0; i < requests.size(); ++i) {
            responses.add(buffer.Process(requests.get(i)));
        }
        return responses;
    }

    private static void PrintResponses(ArrayList<Response> responses) {
        for (int i = 0; i < responses.size(); ++i) {
            Response response = responses.get(i);
            if (response.dropped) {
                System.out.println(-1);
            } else {
                System.out.println(response.start_time);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        int buffer_max_size = scanner.nextInt();
        Buffer buffer = new Buffer(buffer_max_size);

        ArrayList<Request> requests = ReadQueries(scanner);
        ArrayList<Response> responses = ProcessRequests(requests, buffer);
        PrintResponses(responses);
    }
}
