/*
 * Copyright (c) 1995, 2008, Oracle and/or its a,ffiliates. All rights reserved.
 *,
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

package com.speedreader;

/* TextDemo.java requires no other files. */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.GroupLayout.*;

import java.lang.Thread;
import java.text.NumberFormat;
import java.util.ArrayList;


public class Demo extends JPanel implements ActionListener, PropertyChangeListener {
    private JTextField textField;
    private JTextArea textArea;
    
    private JFormattedTextField WPMField;
    
    
    // Labels 
    private JLabel textFieldLabel;
    private JLabel WPMLabel;
    
    private final static String newline = "\n";
    private final static double seconds = 60.0;
    private Reader reader;
    private int caretPosition;
    
    private static String WPMString = "Desired WPM: ";
    private static String textString = "Find: ";

    private int startingWPM = 300;
    private int WPM;
    private int timeForEachWord; // in milliseconds 
    private ArrayList<Chunk> chunks;
    
    private NumberFormat WPMFormat;
    
    final static Color HILIT_COLOR = new Color(229,255,204);
    final Highlighter hilit;
    final Highlighter.HighlightPainter painter;
    
    private JButton startButton = new JButton("Start");
    private JButton pauseButton = new JButton("Pause");
    
    
    
    public Demo(){
        super(new GridBagLayout());
        
        
        
        caretPosition = 0;
        WPM = 0;
        timeForEachWord = Math.round((float)(1/(startingWPM/seconds) * 1000));
        
        reader = new Reader(4, "ivanthefool.txt");
        chunks = new ArrayList<Chunk>();
        textField = new JTextField(20);
        textFieldLabel = new JLabel(textString);
        WPMLabel = new JLabel(WPMString);
        
        
        WPMField = new JFormattedTextField(WPMFormat);
        WPMField.setValue(new Integer(startingWPM));
        WPMField.setColumns(10);
        WPMField.addPropertyChangeListener("value", this);
        
        //adding actionListeners
        textField.addActionListener(this);
        startButton.addActionListener(this);
        pauseButton.addActionListener(this);
        
        // initializing TextArea
        textArea = new JTextArea(reader.getOriginalText(), 20 , 70);
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setFont(new Font("Serif", Font.PLAIN, 15));
        JScrollPane scrollPane = new JScrollPane(textArea);

        hilit = new DefaultHighlighter();
        painter = new DefaultHighlighter.DefaultHighlightPainter(HILIT_COLOR);
        textArea.setHighlighter(hilit);
        
        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;

        c.fill = GridBagConstraints.HORIZONTAL;
        add(startButton, c);
        add(pauseButton, c);
        
        //c.fill = GridBagConstraints.ABOVE_BASELINE_TRAILING;
        
        c.fill = GridBagConstraints.HORIZONTAL;
        add(textField, c);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        add(WPMField, c);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(scrollPane, c);
        
        
        //searchPassage("In a certain kingdom there ");
    }

    public void actionPerformed(ActionEvent e) {
    	if(e.getSource() == startButton){
    		try{
    			Thread.sleep(50);
    		}
    		catch(Exception ex){
    			System.out.println(ex.getMessage());
    		}
    		
    		start();
    		System.out.println("start");
    	}
    	else if(e.getSource() == pauseButton){
    		try{
    			Thread.sleep(1000);
    		}
    		catch(Exception ex){
    			System.out.println(ex.getMessage());
    		}
    		System.out.println("pause");
    	}
    	else if(e.getSource() == textField){    		
    		hilit.removeAllHighlights();
    		String text = textField.getText();
    		searchPassage(text);
    		try{
				Thread.sleep(2000);
			}
			catch(Exception ex){
				System.out.println("Time Error");
			}
			
    		
    	}
    }

    private  void start() {
    	caretPosition = 0;
		for(Chunk chunk: reader.getChunks()){
			int time = chunk.getNumWords() * timeForEachWord;
			System.out.println(chunk.getText());
			searchPassage(chunk.getText());
			try{
				Thread.sleep(time);
			}
			catch(Exception ex){
				System.out.println("Time Error");
			}
			//hilit.removeAllHighlights();
		}
	}

	private void searchPassage(String text) {
		
    	String content = textArea.getText().substring(caretPosition);
    	int index = content.indexOf(text) + caretPosition;
    	
    	
    	if(index >= 0){
    		try {
    			int end = index + text.length();
    			System.out.println("Highligthing");
    			hilit.addHighlight(index,end,painter);
    			caretPosition = end;
    		} catch (BadLocationException e) {
    			e.printStackTrace();
    		}
    	}
    	
    	
    	textField.selectAll();
    	
    	//Make sure the new text is visible, even if there
    	//was a selection in the text area.
    	textArea.setCaretPosition(textArea.getDocument().getLength());
		
	}

	/**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Quick Reader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add contents to the window.
        frame.add(new Demo());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
        
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		Object source = e.getSource();
		if(source == WPMField){
			WPM = (int) ((Number)WPMField.getValue()).doubleValue();
			timeForEachWord = Math.round((float)(1/(WPM/seconds) * 1000));
			System.out.println("WPM: " + WPM);
			System.out.println("time for each word in ms: " + timeForEachWord);
		}
		
	}
	
	


}
