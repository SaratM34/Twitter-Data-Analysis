package DuplicateWords;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class WordRatio {
	
	public static void main(String[] args) throws IOException {
		
		
		double count = 0;
		double count1 = 0;
		
		
		try (BufferedReader br = new BufferedReader(new FileReader("C:/Users/user/Desktop/PBProject2/1stpart/uniqs.txt"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	
		    	
		    	
		    	count = count + 1;
		    	
		    
		    }
		    System.out.println("Count of Unique Words = " + count);
		}
		
		
		try (BufferedReader br = new BufferedReader(new FileReader("C:/Users/user/Desktop/PBProject2/1stpart/dups.txt"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	
		    	
		    	
		    	count1 = count1 + 1;
		    	
		    
		    }
		    System.out.println("Count of Duplicate Words =  " + count1);
		}
		
		System.out.println("Ratio of Unique Words to Duplicate Words = " + count/count1);
		
			
		
	}

}
