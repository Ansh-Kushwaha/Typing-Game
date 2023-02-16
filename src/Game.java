import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.Timer;

public class Game {
	private HashMap<String, ArrayList<String>> wordLists;
    private ArrayList<String> alreadyUsed = new ArrayList<String>();
    private Random myRandom;
    private String path = "src"; // To play edit this path only upto src folder
    private String newSent;
    
    private int diff;
    private JButton button4;
	private JLabel bottomInfo = new JLabel();
	private JTextArea sentence = new JTextArea();
	private JTextArea input = new JTextArea();
	private JLabel timeA;
	private JLabel accuA;
	
	private long timeS;
	private long timeE;
	
	private float accuracy;
    
    // Boot up the game
	public Game() {
		wordLists = new HashMap<>();
		initializeWords();
		myRandom = new Random();
		diff = 0;
		showInterface();
	}
	
	// For running game on console
//	private void startGame() {
//		int diff = 0;
//		System.out.print("Enter the difficulty : ");
//		Scanner ip = new Scanner(System.in);
//		diff = ip.nextInt();
//		ip.nextLine();
//		for(int i = 3; i >= 1; i--) {
//			System.out.println("Starting in " + i + "..");
//			try {
//			    Thread.sleep(2 * 500);
//			} catch (InterruptedException ie) {
//			    Thread.currentThread().interrupt();
//			}
//		}
//		
//		long timeS = 0, timeE = 0;
//		String sentence = generateSentence(diff);
//		System.out.println(sentence);
//		timeS = System.nanoTime();
//		String userInput = ip.nextLine();
//		// System.out.println(userInput);
//		timeE = System.nanoTime();
//		System.out.println("Time taken : " + (timeE - timeS) / 1000000000 + " seconds.");
//		getScore(sentence, userInput);
//		ip.close();
//	}
	
	// Get the score of the player
//	private void getScore(String actual, String attempt) {
//		String acW[] = actual.split(" ");
//		String atW[] = attempt.split(" ");
//		int minLength = (acW.length < atW.length) ? acW.length : atW.length;
//		int matching = 0;
//		for(int i = 0; i < minLength; i++)
//			if(acW[i].equals(atW[i]))
//				matching++;
//		float accuracy = (matching * 100) / acW.length;
//		System.out.println("Accuracy : " + accuracy);
//		if(acW.length == atW.length && accuracy == 100.0)
//			System.out.println("Perfect!");
//	}
	
	// Initialize word lists
	private void initializeWords() {
		String []catg = {"adjective", "noun", "color", "country", "name", "animal", "timeframe", "verb", "fruit"};
		for(String c : catg) {
			ArrayList<String> list = readFromFile(path + "/data/" + c + ".txt");
			wordLists.put(c, list);
		}
	}
	
	private ArrayList<String> readFromFile(String path) {
		ArrayList<String> list = new ArrayList<>();
		try {
			File f = new File(path);
			Scanner read = new Scanner(f);
			while(read.hasNextLine()) {
				String word = read.nextLine();
				list.add(word);
			}
			read.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occured.");
			e.printStackTrace();
		}
		return list;
	}
	
	// Get a random word from the lists
    private String randomFrom(ArrayList<String> words){
        int index = myRandom.nextInt(words.size());
        return words.get(index);
    }	

    // Substitute the placeholders in the template
    private String getSubstitute(String label) {
    	if(label == "number")
    			return ""+myRandom.nextInt(50)+5;
    	if(wordLists.containsKey(label))
    		return randomFrom(wordLists.get(label));
    	return "**Unknown**";
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
        }
        alreadyUsed.add(sub);
        return prefix+sub+suffix;
    }
    
    // Generate a random sentence from the template and word lists
	private String generateSentence(int diff) {
		String sent = "";
		File f = null;
		if(diff == 1)
			f = new File(path + "/data/templateEasy.txt");
		else if(diff == 2)
			f = new File(path + "/data/templateMedium.txt");
		else if(diff == 3)
			f = new File(path + "/data/templateHard.txt");
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
	
	private void showInterface(){
		JFrame window = new JFrame();
		window.setTitle("Typing Game");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(960, 640);
		window.setResizable(true);
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		window.getContentPane().setBackground(new Color(75, 75, 75));
		window.setLayout(new BorderLayout());
		
		ImageIcon icon = new ImageIcon(path + "/image/icon.png");
		window.setIconImage(icon.getImage());
		
		JPanel top = new JPanel();
		JPanel bottom = new JPanel();
		JPanel body = new JPanel();
		JPanel leftBar = new JPanel();
		JPanel rightBar = new JPanel();
		
		Color darkG = new Color(75, 75, 75);
		Color lightG = new Color(95, 95, 95);
		Color mediumG = new Color(85, 85, 85);
		top.setBackground(darkG);
		bottom.setBackground(darkG);
		body.setBackground(lightG);
		leftBar.setBackground(mediumG);
		rightBar.setBackground(mediumG);
		
		top.setPreferredSize(new Dimension(10, 80));
		bottom.setPreferredSize(new Dimension(10, 80));
		body.setPreferredSize(new Dimension(10, 10));
		leftBar.setPreferredSize(new Dimension(180, 10));
		rightBar.setPreferredSize(new Dimension(180, 10));
		
		Font fontL = new Font("MV Boli", Font.PLAIN, 32);
		Font fontM = new Font("MV Boli", Font.PLAIN, 18);
		Font fontS = new Font("MV Boli", Font.PLAIN, 12);
		
		window.add(top, BorderLayout.NORTH);
		window.add(bottom, BorderLayout.SOUTH);
		window.add(leftBar, BorderLayout.WEST);
		window.add(rightBar, BorderLayout.EAST);
		window.add(body, BorderLayout.CENTER);
		
		JLabel title = new JLabel();
		title.setText("Typing Game");
		title.setHorizontalTextPosition(JLabel.CENTER);
		title.setFont(fontL);
		title.setForeground(Color.WHITE);
		title.setVerticalTextPosition(JLabel.CENTER);
		top.add(title, BorderLayout.SOUTH);
		
		leftBar.setLayout(new BorderLayout());
		JPanel leftTop = new JPanel();
		leftTop.setPreferredSize(new Dimension(10, 60));
		leftTop.setOpaque(false);
		leftBar.add(leftTop, BorderLayout.NORTH);
		JLabel difficulty = new JLabel("Difficulty");
		difficulty.setFont(fontL);
		leftTop.add(difficulty, BorderLayout.CENTER);
		
		JPanel leftCenter = new JPanel(new FlowLayout(FlowLayout.LEADING, 24, 25));
		leftCenter.setOpaque(false);
		leftCenter.setPreferredSize(new Dimension(100, 10));
		leftBar.add(leftCenter, BorderLayout.CENTER);
		JButton button1 = new JButton("Easy");	
		JButton button2 = new JButton("Medium");
		JButton button3 = new JButton("Hard");
		JLabel eInfo = new JLabel("One sentence");
		JLabel mInfo = new JLabel("Two sentences");
		JLabel hInfo = new JLabel("Three sentences");
		eInfo.setFont(fontS);
		mInfo.setFont(fontS);
		hInfo.setFont(fontS);
		eInfo.setForeground(Color.WHITE);
		mInfo.setForeground(Color.WHITE);
		hInfo.setForeground(Color.WHITE);
		button1.setFont(fontM);
		button2.setFont(fontM);
		button3.setFont(fontM);
		button1.setForeground(Color.WHITE);
		button2.setForeground(Color.WHITE);
		button3.setForeground(Color.WHITE);
		button1.setBackground(Color.DARK_GRAY);
		button2.setBackground(Color.DARK_GRAY);
		button3.setBackground(Color.DARK_GRAY);
		button1.setPreferredSize(new Dimension(120, 40));
		button2.setPreferredSize(new Dimension(120, 40));
		button3.setPreferredSize(new Dimension(120, 40));
		button1.setFocusable(false);
		button2.setFocusable(false);
		button3.setFocusable(false);
		button1.setBorder(BorderFactory.createRaisedBevelBorder());
		button2.setBorder(BorderFactory.createRaisedBevelBorder());
		button3.setBorder(BorderFactory.createRaisedBevelBorder());
		JPanel easyP = new JPanel(new GridLayout(0, 1));
		JPanel mediumP = new JPanel(new GridLayout(0, 1));
		JPanel hardP = new JPanel(new GridLayout(0, 1));
		easyP.setOpaque(false);
		mediumP.setOpaque(false);
		hardP.setOpaque(false);
		easyP.add(button1);
		easyP.add(eInfo);
		mediumP.add(button2);
		mediumP.add(mInfo);
		hardP.add(button3);
		hardP.add(hInfo);
		leftCenter.add(easyP);
		leftCenter.add(mediumP);
		leftCenter.add(hardP);
		
		JPanel leftBottom = new JPanel(new FlowLayout(FlowLayout.LEADING, 10, 10));
		leftBottom.setPreferredSize(new Dimension(100, 100));
		leftBar.add(leftBottom, BorderLayout.SOUTH);
		leftBottom.setOpaque(false);
		
		String d = "Current Difficulty : ";
		JLabel currInfo = new JLabel(d);
		currInfo.setFont(fontS);
		currInfo.setForeground(Color.WHITE);
		currInfo.setOpaque(false);
		leftBottom.add(currInfo);
		
		button4 = new JButton("Start!");
		button4.setFont(fontL);
		button4.setForeground(Color.GRAY);
		button4.setBackground(Color.WHITE);
		button4.setPreferredSize(new Dimension(160, 40));
		button4.setBorder(BorderFactory.createEtchedBorder());
		button4.setFocusable(false);
		leftBottom.add(button4);
		
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currInfo.setText(d + "Easy");
				button1.setBorder(BorderFactory.createLoweredBevelBorder());
				button2.setBorder(BorderFactory.createRaisedBevelBorder());
				button3.setBorder(BorderFactory.createRaisedBevelBorder());
				button1.setEnabled(false);
				button2.setEnabled(true);
				button3.setEnabled(true);
				diff = 1;
			}
		});
		
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currInfo.setText(d + "Medium");
				button1.setBorder(BorderFactory.createRaisedBevelBorder());
				button2.setBorder(BorderFactory.createLoweredBevelBorder());
				button3.setBorder(BorderFactory.createRaisedBevelBorder());
				button1.setEnabled(true);
				button2.setEnabled(false);
				button3.setEnabled(true);
				diff = 2;
			}
		});
		
		button3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currInfo.setText(d + "Hard");
				button1.setBorder(BorderFactory.createRaisedBevelBorder());
				button2.setBorder(BorderFactory.createRaisedBevelBorder());
				button3.setBorder(BorderFactory.createLoweredBevelBorder());
				button1.setEnabled(true);
				button2.setEnabled(true);
				button3.setEnabled(false);
				diff = 3;
			}
		});
		
		bottomInfo.setOpaque(false);
		bottomInfo.setPreferredSize(new Dimension(470, 40));
		bottomInfo.setFont(fontL);
		bottomInfo.setForeground(Color.WHITE);
		bottomInfo.setHorizontalAlignment(JLabel.CENTER);
		bottomInfo.setVerticalAlignment(JLabel.CENTER);
		// bottom.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		
		button4.addActionListener(new ActionListener() {
			int index = 3;
			@Override
			public void actionPerformed(ActionEvent e) {
				if(diff == 0) {
					bottomInfo.setText("Select Difficulty");
				}
				else {
					button4.setEnabled(false);
					Timer timer = new Timer(1000, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							bottomInfo.setText("Starting in " + index);
							index--;
							if (index <= 0) {
								((Timer)e.getSource()).stop();
								displaySentence();
								detectE();
							}
						}
					});
					timer.setInitialDelay(0);
					timer.start();
				}
			}
		});
		

		//body.setLayout(new FlowLayout(FlowLayout.LEADING, 25, 20));
		body.setLayout(new GridLayout(0, 1, 25, 40));
		body.setBorder(BorderFactory.createEmptyBorder(35, 35, 35, 35));
		sentence.setPreferredSize(new Dimension(535, 120));
		sentence.setBackground(new Color(100, 100, 100));
		sentence.setFont(fontM);
		sentence.setForeground(Color.WHITE);
		sentence.setFocusable(false);
		sentence.setLineWrap(true);
		sentence.setEditable(false);
		sentence.setOpaque(true);
		sentence.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(mediumG, 3 , true), "Test your typing skills", 0, 0 , fontS, new Color(190, 190, 190)));
		
		input.setPreferredSize(new Dimension(535, 120));
		input.setBackground(mediumG);
		input.setFont(fontM);
		input.setForeground(Color.LIGHT_GRAY);
		input.setLineWrap(true);
		input.setOpaque(true);
		input.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(lightG, 3 , true), "Type here", 0, 0 , fontS, new Color(210, 210, 210)));
		body.add(sentence);
		body.add(input);
		
		rightBar.setLayout(new BorderLayout());
		JPanel rightTop = new JPanel();
		rightTop.setPreferredSize(new Dimension(10, 60));
		rightTop.setOpaque(false);
		rightBar.add(rightTop, BorderLayout.NORTH);
		JLabel score = new JLabel("Score");
		score.setFont(fontL);
		rightTop.add(score, BorderLayout.CENTER);
		
		JPanel rightCenter = new JPanel(new FlowLayout(FlowLayout.LEADING, 8, 10));
		rightCenter.setOpaque(false);
		rightBar.add(rightCenter, BorderLayout.CENTER);
		
		// Test borders
		rightCenter.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 5));
		leftCenter.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 5));
		
		JPanel timeS = new JPanel(new GridLayout(0, 1));
		timeS.setOpaque(false);
		rightCenter.add(timeS);
		
		JLabel time = new JLabel("Time :");
		time.setPreferredSize(new Dimension(150, 20));
		time.setFont(fontM);
		time.setForeground(Color.WHITE);
		time.setHorizontalTextPosition(JLabel.LEFT);
		time.setOpaque(false);
		timeS.add(time);
		
		timeA = new JLabel("--/--");
		timeA.setFont(fontM);
		timeA.setForeground(Color.WHITE);
		timeA.setBackground(lightG);
		timeA.setHorizontalTextPosition(JLabel.RIGHT);
		timeA.setOpaque(false);
		timeA.setHorizontalAlignment(JLabel.CENTER);
		timeS.add(timeA);
		
		JPanel accuS = new JPanel(new GridLayout(0, 1));
		accuS.setOpaque(false);
		rightCenter.add(accuS);
		
		JLabel accuracy = new JLabel("Accuracy :");
		accuracy.setPreferredSize(new Dimension(150, 20));
		accuracy.setFont(fontM);
		accuracy.setForeground(Color.WHITE);
		accuracy.setHorizontalTextPosition(JLabel.LEFT);
		accuracy.setOpaque(false);
		accuS.add(accuracy);
		
		accuA = new JLabel("--");
		accuA.setFont(fontM);
		accuA.setForeground(Color.WHITE);
		accuA.setBackground(lightG);
		accuA.setOpaque(false);
		accuA.setHorizontalAlignment(JLabel.CENTER);
		accuS.add(accuA);
		
		JPanel rightBottom = new JPanel(new GridLayout(0, 1, 12, 24));
		rightBottom.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		rightBottom.setPreferredSize(new Dimension(10, 240));
		rightBottom.setOpaque(false);
		rightBar.add(rightBottom, BorderLayout.SOUTH);
		JLabel bestT = new JLabel("Best Timings:");
		bestT.setFont(new Font("MV Boli", Font.PLAIN, 24));
		rightBottom.add(bestT, BorderLayout.NORTH);
		
		JPanel easyT = new JPanel(new GridLayout(0, 1, 0, 0));
		JPanel mediumT = new JPanel(new GridLayout(0, 1, 0, 8));
		JPanel hardT = new JPanel(new GridLayout(0, 1, 0, 8));
		
		easyT.setOpaque(false);
		mediumT.setOpaque(false);
		hardT.setOpaque(false);
		
		JLabel label1 = new JLabel("Easy");
		label1.setFont(fontM);
		label1.setForeground(Color.WHITE);
		JLabel eMin = new JLabel("--/--");
		eMin.setFont(fontM);
		eMin.setForeground(Color.WHITE);
		JLabel label2 = new JLabel("Medium");
		label2.setFont(fontM);
		label2.setForeground(Color.WHITE);
		JLabel mMin = new JLabel("--/--");
		mMin.setFont(fontM);
		mMin.setForeground(Color.WHITE);
		JLabel label3 = new JLabel("Hard");
		label3.setFont(fontM);
		label3.setForeground(Color.WHITE);
		JLabel hMin = new JLabel("--/--");
		hMin.setFont(fontM);
		hMin.setForeground(Color.WHITE);
		easyT.add(label1);
		easyT.add(eMin);
		mediumT.add(label2);
		mediumT.add(mMin);
		hardT.add(label3);
		hardT.add(hMin);
		
		rightBottom.add(easyT);
		rightBottom.add(mediumT);
		rightBottom.add(hardT);
		
		bottom.setLayout(new BorderLayout());
		JPanel bottomLeft = new JPanel();
		JPanel bottomRight = new JPanel();
		JPanel bottomCenter = new JPanel();
		bottomLeft.setOpaque(false);
		bottomLeft.setPreferredSize(new Dimension(260, 10));
		bottomLeft.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		bottomLeft.setLayout(new GridLayout(0, 1));
		bottomRight.setOpaque(false);
		bottomRight.setPreferredSize(new Dimension(260, 10));
		bottomRight.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		bottomRight.setLayout(new GridLayout(0, 1));
		bottomCenter.setOpaque(false);
		bottomCenter.setPreferredSize(new Dimension(10, 10));
		bottomCenter.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 5));
		bottomCenter.setLayout(new GridLayout(1, 0, 40, 15));

		bottom.add(bottomLeft, BorderLayout.WEST);
		bottom.add(bottomCenter, BorderLayout.CENTER);
		bottom.add(bottomRight, BorderLayout.EAST);
		bottomLeft.add(bottomInfo, BorderLayout.CENTER);
		
		JLabel madeBy = new JLabel();
		madeBy.setText("Made by Ansh Kushwaha");
		madeBy.setOpaque(false);
		madeBy.setFont(fontM);
		madeBy.setForeground(new Color(210, 210, 210));
		madeBy.setHorizontalAlignment(JLabel.CENTER);
		madeBy.setVerticalAlignment(JLabel.CENTER);
		bottomRight.add(madeBy, BorderLayout.CENTER);
		
		JPanel resP = new JPanel();
		resP.setOpaque(false);
		JPanel clrP = new JPanel();
		clrP.setOpaque(false);
		JButton reset = new JButton();
		JButton clear = new JButton();
		JLabel resI = new JLabel();
		resI.setText("Reset game to start new attempt.");
		resI.setFont(fontS);
		resI.setForeground(Color.WHITE);
		reset.setText("Reset");
		reset.setFont(fontM);
		reset.setFocusable(false);
		reset.setForeground(Color.WHITE);
		reset.setBackground(new Color(40, 40 , 40));
		reset.setPreferredSize(new Dimension(160, 40));
		reset.setBorder(BorderFactory.createEtchedBorder());
		resP.add(reset, BorderLayout.CENTER);
		resP.add(resI);
		JLabel clrI = new JLabel();
		clrI.setText("Clear all data! (To restart the game)");
		clrI.setFont(fontS);
		clrI.setForeground(Color.WHITE);
		clear.setText("Clear!");
		clear.setFont(fontM);
		clear.setFocusable(false);
		clear.setForeground(Color.WHITE);
		clear.setBackground(new Color(40, 40 , 40));
		clear.setPreferredSize(new Dimension(160, 40));
		clear.setBorder(BorderFactory.createEtchedBorder());
		clrP.add(clear, BorderLayout.CENTER);
		clrP.add(clrI);
		bottomCenter.add(resP, BorderLayout.WEST);
		bottomCenter.add(clrP, BorderLayout.EAST);
		
		window.setVisible(true);
	}
	
	private void displaySentence() {
		newSent = generateSentence(diff);
		sentence.setText(newSent);
	}
	
	private Action displayScore = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			timeE = System.nanoTime();
			bottomInfo.setText("Done");
			displayTime();
			displayAccuracy();
			input.setEditable(false);
			input.setFocusable(false);
		}
	};
	
	private void detectE() {
		bottomInfo.setText("Go");
		timeS = System.nanoTime();
		KeyStroke keyStroke = KeyStroke.getKeyStroke("ENTER");
        Object actionKey = input.getInputMap(JTextArea.WHEN_FOCUSED).get(keyStroke);
        input.getActionMap().put(actionKey, displayScore);
	}
	
	private void displayTime() {
		long timeTaken = (timeE - timeS) / 1000000000;
		timeA.setText(Long.toString(timeTaken) + " seconds");
	}
	
	private void displayAccuracy() {
		String acW[] = sentence.getText().split(" ");
		String atW[] = input.getText().split(" ");
		
		int minLength = (acW.length < atW.length) ? acW.length : atW.length;
		
		int matching = 0;
		for(int i = 0; i < minLength; i++)
			if(acW[i].equals(atW[i]))
				matching++;
		accuracy = (matching * 100) / acW.length;
		accuA.setText(Float.toString(accuracy) + "%");
	}
	
	public static void main(String[] args) {
		new Game();
	}
}

