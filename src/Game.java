import java.util.ArrayList;
import java.util.Random;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Game {
	private ArrayList<String> adjectiveList;
    private ArrayList<String> nounList;
    private ArrayList<String> colorList;
    private ArrayList<String> countryList;
    private ArrayList<String> nameList;
    private ArrayList<String> animalList;
    private ArrayList<String> timeList;
    private ArrayList<String> verbList;
    private ArrayList<String> fruitList;
    private ArrayList<String> alreadyUsed = new ArrayList<String>();
    private Random myRandom;
    private String path = "src/data";
    
    // Boot up the game
	public Game() {
		initializeWords();
		myRandom = new Random();
		startGame();
	}
	
	// Begin the game
	private void startGame() {
		int diff = 0;
		System.out.print("Enter the difficulty : ");
		Scanner ip = new Scanner(System.in);
		diff = ip.nextInt();
		ip.nextLine();
		for(int i = 3; i >= 1; i--) {
			System.out.println("Starting in " + i + "..");
			try {
			    Thread.sleep(2 * 500);
			} catch (InterruptedException ie) {
			    Thread.currentThread().interrupt();
			}
		}
		
		long timeS = 0, timeE = 0;
		String sentence = generateSentence(diff);
		System.out.println(sentence);
		timeS = System.nanoTime();
		String userInput = ip.nextLine();
		// System.out.println(userInput);
		timeE = System.nanoTime();
		System.out.println("Time taken : " + (timeE - timeS) / 1000000000 + " seconds.");
		getScore(sentence, userInput);
		ip.close();
	}
	
	// Get the score of the player
	private void getScore(String actual, String attempt) {
		String acW[] = actual.split(" ");
		String atW[] = attempt.split(" ");
		int minLength = (acW.length < atW.length) ? acW.length : atW.length;
		int matching = 0;
		for(int i = 0; i < minLength; i++)
			if(acW[i].equals(atW[i]))
				matching++;
		float accuracy = (matching * 100) / acW.length;
		System.out.println("Accuracy : " + accuracy);
		if(acW.length == atW.length && accuracy == 100.0)
			System.out.println("Perfect!");
	}
	
	// Initialize word lists
	private void initializeWords() {
		adjectiveList= readFromFile(path+"/adjective.txt"); 
        nounList = readFromFile(path+"/noun.txt");
        colorList = readFromFile(path+"/color.txt");
        countryList = readFromFile(path+"/country.txt");
        nameList = readFromFile(path+"/name.txt");      
        animalList = readFromFile(path+"/animal.txt");
        timeList = readFromFile(path+"/timeframe.txt");
        verbList = readFromFile(path+"/verb.txt");
        fruitList = readFromFile(path+"/fruit.txt");
	}
	
	private ArrayList<String> readFromFile(String path) {
		ArrayList<String> wordList = new ArrayList<>();
		try {
			File f = new File(path);
			Scanner read = new Scanner(f);
			while(read.hasNextLine()) {
				String word = read.nextLine();
				wordList.add(word);
			}
			read.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occured.");
			e.printStackTrace();
		}
		return wordList;
	}

	// Get a random word from the lists
    private String randomFrom(ArrayList<String> source){
        int index = myRandom.nextInt(source.size());
        return source.get(index);
    }	

    // Substitute the placeholders in the template
    private String getSubstitute(String label) {
        if (label.equals("country")) {
            return randomFrom(countryList);
        }
        if (label.equals("color")){
            return randomFrom(colorList);
        }
        if (label.equals("noun")){
            return randomFrom(nounList);
        }
        if (label.equals("name")){
            return randomFrom(nameList);
        }
        if (label.equals("adjective")){
            return randomFrom(adjectiveList);
        }
        if (label.equals("animal")){
            return randomFrom(animalList);
        }
        if (label.equals("timeframe")){
            return randomFrom(timeList);
        }
        if (label.equals("verb")){
            return randomFrom(verbList);
        }
        if (label.equals("fruit")){
            return randomFrom(fruitList);
        }
        if (label.equals("number")){
            return ""+myRandom.nextInt(50)+5;
        }
        return "**UNKNOWN**";
    }	
	
    // Process a placeholder in the template
    private String processWord(String w){
        int first = w.indexOf("<");
        int last = w.indexOf(">",first);
        
        if (first == -1 || last == -1){
            return w;
        }
        
        String prefix = w.substring(0,first);
        String suffix = w.substring(last+1);
        String sub = getSubstitute(w.substring(first+1,last));
        
        while(alreadyUsed.contains(sub)){
            sub = getSubstitute(w.substring(first+1,last));
            alreadyUsed.add(sub);
        }
        return prefix+sub+suffix;
    }
    
    // Generate a random sentence from the template and word lists
	public String generateSentence(int diff) {
		String sent = "";
		File f = null;
		if(diff == 1)
			f = new File("src/data/templateEasy.txt");
		else if(diff == 2)
			f = new File("src/data/templateMedium.txt");
		else if(diff == 3)
			f = new File("src/data/template/Medium.txt");
		Scanner read = null;
		try {
			read = new Scanner(f);
		} catch (FileNotFoundException e) {
			System.out.println("An error occured.");
			e.printStackTrace();
		}
		String temp = read.nextLine();
		
		String []words = temp.split(" ");
		for(String w : words) {
		//	System.out.print(w + " ");
			sent = sent + processWord(w) + " ";
		}
		read.close();
		
		return sent;
	}
	
	//Display the sentence
	public void Test() {
		System.out.println(generateSentence(1));
	}
	
	public static void main(String[] args) {
		new Game();
	}
}

