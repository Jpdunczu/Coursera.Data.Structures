/*
Problem Description
Task. You are given a series of incoming network packets, and your task is to simulate their processing.
Packets arrive in some order. For each packet number 𝑖, you know the time when it arrived 𝐴𝑖 and the
time it takes the processor to process it 𝑃𝑖 (both in milliseconds). There is only one processor, and it
processes the incoming packets in the order of their arrival. If the processor started to process some
packet, it doesn’t interrupt or stop until it finishes the processing of this packet, and the processing of
packet 𝑖 takes exactly 𝑃𝑖 milliseconds.
The computer processing the packets has a network buffer of fixed size 𝑆. When packets arrive,
they are stored in the buffer before being processed. However, if the buffer is full when a packet
arrives (there are 𝑆 packets which have arrived before this packet, and the computer hasn’t finished
processing any of them), it is dropped and won’t be processed at all. If several packets arrive at the
same time, they are first all stored in the buffer (some of them may be dropped because of that —
those which are described later in the input). The computer processes the packets in the order of
their arrival, and it starts processing the next available packet from the buffer as soon as it finishes
processing the previous one. If at some point the computer is not busy, and there are no packets in
the buffer, the computer just waits for the next packet to arrive. Note that a packet leaves the buffer
and frees the space in the buffer as soon as the computer finishes processing it.

Input Format. 
The first line of the input contains the size 𝑆 of the buffer and the number 𝑛 of incoming
network packets. Each of the next 𝑛 lines contains two numbers. 𝑖-th line contains the time of arrival
𝐴𝑖 and the processing time 𝑃𝑖 (both in milliseconds) of the 𝑖-th packet. It is guaranteed that the
sequence of arrival times is non-decreasing (however, it can contain the exact same times of arrival in
milliseconds — in this case the packet which is earlier in the input is considered to have arrived earlier).
Constraints. All the numbers in the input are integers. 1 ≤ 𝑆 ≤ 105; 1 ≤ 𝑛 ≤ 105; 0 ≤ 𝐴𝑖 ≤ 106;
0 ≤ 𝑃𝑖 ≤ 103; 𝐴𝑖 ≤ 𝐴𝑖+1 for 1 ≤ 𝑖 ≤ 𝑛 − 1.

Output Format. 
For each packet output either the moment of time (in milliseconds) when the processor
began processing it or −1 if the packet was dropped (output the answers for the packets in the same
order as the packets are given in the input).

Time Limits. C: 2 sec, C++: 2 sec, Java: 6 sec, Python: 8 sec. C#: 3 sec, Haskell: 4 sec, JavaScript: 6 sec,
Ruby: 6 sec, Scala: 6 sec.

Memory Limit. 512MB.
*/

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
        this.finish_time_ = new ArrayList<Integer>();
    }

	//(Max time used: 3.10/6.00, max memory used: 199204864/536870912.)
	
    public Response Process(Request request) {
        // write your code here
	if( finish_time_.isEmpty() ) {
		// the buffer is empty, start processing immediately
		if( request.process_time > 0 ) {
			finish_time_.add(request.process_time);
		}
		return new Response(false,request.arrival_time);
	} else if ( size_ > finish_time_.size() ) {
		// Buffer is not empty, but there is room for the next packet.
		if( request.arrival_time < finish_time_.get(finish_time_.size()-1) ) {
			request.arrival_time = finish_time_.get(finish_time_.size()-1);
		}
		finish_time_.add(request.arrival_time + request.process_time);
		Response response = new Response(false,request.arrival_time);
		return response;
	} else {
		// Buffer is full.
		// check to see if the previous packets have finished processing by this packets arrival time.
		if( finish_time_.get(0) <= request.arrival_time ) {
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
