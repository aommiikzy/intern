
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.LinkedList; 
import java.util.Queue; 




public class ActionRecognition extends JFrame {
	
	public String fileName1, filePath1, fileName2, filePath2, fileName3, filePath3;
	public int lineNo;
	
	
	public static void main(String [] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				ActionRecognition form = new ActionRecognition();
				form.setVisible(true);
			}
		});
        
    }
	
	static double dotProduct(double vectorTool[], double vectorTooth[]) {
		double sum = 0.0;
		for(int i=0; i<3; i++) {
			sum = sum + (vectorTool[i] * vectorTooth[i]);
		}
		return sum; 
	}
	
	static double[] normalize(double v[]) {
		double[] unitVector = new double[3];
		double magnitude = Math.abs(Math.sqrt(Math.pow(v[0],2) + Math.pow(v[1],2) + Math.pow(v[2],2)));
		unitVector[0] = v[0] / magnitude;
		unitVector[1] = v[1] / magnitude;
		unitVector[2] = v[2] / magnitude;
		return unitVector; 
	}
	
	public ActionRecognition() {
		
		super("Action Generator");
		setSize(600, 350);
		setLocation(400, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		// File name
		
		final JLabel showFileLog = new JLabel("Choose logfile",JLabel.CENTER);
		showFileLog.setBounds(10, 20, 575, 14);
		getContentPane().add(showFileLog);
		
		final JLabel showFileInst = new JLabel("Choose actionInst",JLabel.CENTER);
		showFileInst.setBounds(10, 140, 575, 14);
		getContentPane().add(showFileInst);
		
		final JLabel showFileList = new JLabel("Choose actionList",JLabel.CENTER);
		showFileList.setBounds(10, 160, 575, 14);
		getContentPane().add(showFileList);
		
		// Execution status
		
		final JLabel csResult = new JLabel("",JLabel.CENTER);
		csResult.setBounds(10, 90, 575, 14);
		getContentPane().add(csResult);

		final JLabel cfResult = new JLabel("",JLabel.CENTER);
		cfResult.setBounds(10, 260, 575, 14);
		getContentPane().add(cfResult);
		
		final JLabel ___ = new JLabel("-----------------------------------------------------------------",JLabel.CENTER);
		___.setBounds(10, 115, 580, 14);
		getContentPane().add(___);
		
		// Browse button
		
		JButton browseBtn1 = new JButton("logfile");
		browseBtn1.setBounds(170, 50, 120, 25);
		JButton browseBtn2 = new JButton("actionInst");
		browseBtn2.setBounds(170, 190, 120, 25);
		JButton browseBtn3 = new JButton("actionList");
		browseBtn3.setBounds(300, 190, 120, 25);
		
		// Create cram button
		
		JButton startBtn1 = new JButton("cram_start");
		startBtn1.setBounds(300, 50, 120, 25);
		JButton startBtn2 = new JButton("cram_finish");
		startBtn2.setBounds(235, 220, 120, 25);
		
		// Browse logfile
		
		browseBtn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fileopen1 = new JFileChooser("D:\\HMD Group Evaluation");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Text/CSV file", "json");
				fileopen1.addChoosableFileFilter(filter);
				
				int ret1 = fileopen1.showDialog(null, "Choose logfile");
				if (ret1 == JFileChooser.APPROVE_OPTION) {

					filePath1 = fileopen1.getSelectedFile().toString();
					fileName1 = filePath1.split("\\\\")[filePath1.split("\\\\").length-1];
					System.out.println(filePath1);
					showFileLog.setText(filePath1);
		        	csResult.setText("");
					
				}
			}
		});

		// Browse actionInst
		
		browseBtn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
								 
				JFileChooser fileopen2 = new JFileChooser("D:\\HMD Group Evaluation");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Text/CSV file", "json");
				fileopen2.addChoosableFileFilter(filter);
				
				int ret2 = fileopen2.showDialog(null, "Choose actionInst");
				if (ret2 == JFileChooser.APPROVE_OPTION) {

					filePath2 = fileopen2.getSelectedFile().toString();
					fileName2 = filePath2.split("\\\\")[filePath2.split("\\\\").length-1];
					showFileInst.setText(filePath2);
		        	csResult.setText("");
					
				}
			}
		});

		// Browse actionList
		
		browseBtn3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fileopen3 = new JFileChooser("D:\\HMD Group Evaluation");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Text/CSV file", "json");
				fileopen3.addChoosableFileFilter(filter);
				
				int ret3 = fileopen3.showDialog(null, "Choose actionList");
				if (ret3 == JFileChooser.APPROVE_OPTION) {

					filePath3 = fileopen3.getSelectedFile().toString();
					fileName3 = filePath3.split("\\\\")[filePath3.split("\\\\").length-1];
					showFileList.setText(filePath3);
		        	csResult.setText("");
					
				}
			}
		});
		
		
		// cram_start
		
		startBtn1.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				filePath1 = filePath1.replace(fileName1, "");
		        String line = null;
		        String firstLine = null;
		        
		        if(fileName1.contains("logfile")) {		// generate action list + cram_start command from raw data

					filePath1 = filePath1.replace(fileName1, "");
			        String outFileName = filePath1 + "actionList" + fileName1.replace("logfile", "");	// for getting endTime 2nd round
			        String outFileName2 = filePath1 + "cramStart" + fileName1.replace("logfile", "");
			        String outFileName3 = filePath1 + "cramStart2" + fileName1.replace("logfile", "");
		        	
			        try {
			            
			            FileReader fileReader = new FileReader(filePath1 + fileName1);	// read logfile
			            BufferedReader bufferedReader = new BufferedReader(fileReader);
	
			            FileWriter fileWriter = new FileWriter(outFileName);			// actionList
			            BufferedWriter outputFile = new BufferedWriter(fileWriter);
			            
			            FileWriter fileWriter2 = new FileWriter(outFileName2);			// cram_start
			            BufferedWriter outputFile2 = new BufferedWriter(fileWriter2);

			            FileWriter fileWriter3 = new FileWriter(outFileName3);			// cram_start
			            BufferedWriter outputFile3 = new BufferedWriter(fileWriter3);
			            
			            String currAction = "\"\"", prevAction = "\"\"", btnState = "\"\"", 
			            		currAnatomy = "\"\"", prevAnatomy = "\"\"", latestAnatomy = "\"\"", 
			            		startTime = "", endTime = "", description = "\"\"",
			            		currCorrectness = "\"\"", prevCorrectness = "\"\"",		// Right/Wrong
			            		currParent = "\"\"", prevParent = "\"\"", prevParent2 = "\"\"";				// Widening/Extension
			            double frcX, frcY, frcZ;
			            double dirX, dirY, dirZ;
			            double posX, posY, posZ;
			            String[] data = new String[21];
			            String[] toothdir = new String[3];
			            String actRecog = "";
			            String cmd = "", cmd2 = "";
			            
			            double mvmtDir[] = {0.0, 0.0, 0.0};
			            
			            Queue<Double> qX = new LinkedList<>();
			            Queue<Double> qY = new LinkedList<>();
			            Queue<Double> qZ = new LinkedList<>();
			            
			            firstLine = bufferedReader.readLine();
			            toothdir = firstLine.split(", ");
			            double toothDir[] = {Double.parseDouble(toothdir[0]),
			            						Double.parseDouble(toothdir[1]),
			            							Double.parseDouble(toothdir[2])};
			            			            
			            bufferedReader.readLine();
			            
			            lineNo = 0;
			            
			            while((line = bufferedReader.readLine()) != null) {
			            	
			            	lineNo = lineNo + 1;
			            	line = line.replace("{", "");
			            	data = line.split("\\{|, |\\}");	// get 19 data
			               
			            	frcX = Double.parseDouble(data[7]);
			            	frcY = Double.parseDouble(data[8]);
			            	frcZ = Double.parseDouble(data[9]);
			            				            				            	
			            	dirX = Double.parseDouble(data[10]);
			            	dirY = Double.parseDouble(data[11]);
			            	dirZ = Double.parseDouble(data[12]);
			            	double direction[] = {dirX, dirY, dirZ};

			            	posX = Double.parseDouble(data[1]);
			            	posY = Double.parseDouble(data[2]);
			            	posZ = Double.parseDouble(data[3]);
			            	
			            	qX.add(posX);
			            	qY.add(posY);
			            	qZ.add(posZ);
			            	
			            	if(lineNo > 35) {
			            		
				            	double positionDiff[] = {posX-qX.peek(), posY-qY.peek(), posZ-qZ.peek()};
				            	mvmtDir = normalize(positionDiff);
			            		qX.remove();
			            		qY.remove();
			            		qZ.remove();
			            	}
			            				            	
			            	btnState = data[14];
			            	currAnatomy = data[13];
			            	
			            	if(currAnatomy.equals("\"\"")) {
			            		
		            			if(!prevAnatomy.equals("\"\"")) {
		            				
		            				currAnatomy = prevAnatomy;
		            				
		            			} else {
		            				
		            				currAnatomy = latestAnatomy;
		            				
		            			}
		            			
		            		}
		            		
	            			latestAnatomy = currAnatomy;
			            	
			            	if(frcX!=0.0 && frcY!=0.0 && frcZ!=0.0) {	// collide the tooth
			            		
			            		if(btnState.contains("true")) {			// button pressed
			            			
			            			currAction = "\"Drill\"";
			            			
			            			double movement = dotProduct(mvmtDir, toothDir);

		            				if(Math.abs(movement) > 0.8) {		// quite parallel
		            					
		            					if(movement >= 0) {		// move downwards
		            						
		            						currParent = "\"Extension\"";
		            						
		            					} else {				// move upwards
		            						
		            						currParent = "\"\"";	
				            				currAction = "\"\"";
		            					}
		            					
		            				} else if(Math.abs(movement) < 0.4) {							// not parallel
		            					
		            					currParent = "\"Widening\"";
		            					
		            				} else {

		            					currParent = "\"Undefined\"";
		            				}

		            				
			            		} else if(btnState.contains("false")) {		// button not pressed
			            			
			            			currAction = "\"Place\"";
			            			currParent = "\"Place\"";
			         
			            		} 
			            		
			            		System.out.println(data[0] + " - " + dotProduct(mvmtDir, toothDir) + " - " + currParent + " - " + currAnatomy);
	            				
			            	} else {		// not collide the tooth
			            		
			            		currAction = "\"\"";
		            			currParent = "\"\"";
			            	}
			            	
            				
			            	if(Math.abs(dotProduct(direction, toothDir)) > 0.9) {	// tool alignment
			            		
			            		currCorrectness = "\"Correct\"";	// parallel
			            		
			            	} else {
			            		
			            		currCorrectness = "\"Incorrect\"";	// not parallel
			            		
			            	}
			            	

		            		endTime = data[0];
			            	
			            	if(!currAction.equals(prevAction) || !currAnatomy.equals(prevAnatomy) ||
			            			!currParent.equals(prevParent) || !currCorrectness.equals(prevCorrectness)) {	// something change?
			            		
			            		if(!prevAnatomy.equals("\"\"") && !prevAction.equals("\"\"")) {	// --> "Drill_CAD_Dentine_Lingual"
			            			
				        			description = "\"" + prevAction.replace("\"", "") + "_" + prevAnatomy.replace("\"", "") + "\"";
				        			
				        		}
			            		
				        		if(startTime.equals("")) startTime = data[0];
				        		
				        		if(currParent.equals(prevParent2) && !currParent.equals(prevParent) && !currParent.equals("\"\"")
				        				&& Double.parseDouble(endTime)-Double.parseDouble(startTime)<0.5) {
				        			
				        			System.out.println(prevParent + " -> " + currParent);
				        			prevParent = currParent;
				        		}
				        		prevParent2 = prevParent;
				        		
				        		double ET = Double.parseDouble(endTime) - 0.000001;
				        		
				        		actRecog = prevAction + ", " + prevAnatomy + ", " + startTime + ", " + String.format("%.6f", ET)
				        				+ ", " + description + ", " + prevParent + ", " + prevCorrectness;
				        		
				        		cmd = "cram_start_action(knowrob:" + prevAction + ", " + description + ", " + startTime + ", PA" + 
				        				", knowrob:" + prevParent + ", knowrob:" + prevAnatomy + ", " + prevCorrectness + ", ActionInst).";
				        		
				        		cmd2 = "cram_start_action(knowrob:" + prevParent + ", " + prevParent + ", " + startTime + ", PA" + 
				        				", knowrob:" + prevParent + ", knowrob:" + prevAnatomy + ", " + prevCorrectness + ", ActionInst).";
				        		
				        		//System.out.println(actRecog);
				        		
				        		description = "\"\"";
				        		startTime = data[0];
				        		if(!prevAction.equals("\"\"") && !prevAnatomy.equals("\"\"")) {
				        			outputFile.write(actRecog + "\n");
				        			outputFile2.write(cmd.replace("\"", "\'") + "\n");
				        			outputFile3.write(cmd2.replace("\"", "\'") + "\n");
				        		}
				        		
			            	}
			            	
			            	prevParent = currParent;
		            		prevAction = currAction;
		            		prevAnatomy = currAnatomy;
		            		prevCorrectness = currCorrectness;
			            	
			            }
			          
			            outputFile.close();
			            outputFile2.close();
			            outputFile3.close();
			            bufferedReader.close();
			            
			        	csResult.setText("Success!");
			            
			        }
			        catch(FileNotFoundException ex) {
			        	
			            System.out.println("Unable to open file '" + fileName1 + "'");                
			        }
			        catch(IOException ex) {
			        	
			            System.out.println("Error reading file '" + fileName1 + "'");     
			            System.out.println("Error writing to file '" + outFileName + "'");
			        }
			        
		        } else {
		        	
		        	csResult.setText("Invalid file");
		        	
		        }
		        
				
			}
			
		});
		
		

		// cram_finish
		
		startBtn2.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {

				filePath2 = filePath2.replace(fileName2, ""); // Inst
				filePath3 = filePath3.replace(fileName3, ""); // List
		        String lineInst = null;
		        String lineList = null;
		        
		        if(fileName3.contains("actionList") && fileName2.contains("actionInst") &&
		        		fileName3.split("_")[1].equals(fileName2.split("_")[1])) {
		        										// file1 = action LIST , file2 = action INST --> generate cram_finish
		        	
		        	filePath3 = filePath3.replace(fileName3, "");
		        	filePath2 = filePath2.replace(fileName2, "");
			        String outFileName = filePath2 + "cramFinish" + fileName3.replace("actionList", "");
		        	
			        try {
			            
			            FileReader fileReader3 = new FileReader(filePath3 + fileName3);  // List
			            BufferedReader actionListFile = new BufferedReader(fileReader3);
			            
			            FileReader fileReader2 = new FileReader(filePath2 + fileName2);  // Inst
			            BufferedReader actionInstFile = new BufferedReader(fileReader2);
	
			            FileWriter fileWriter = new FileWriter(outFileName);
			            BufferedWriter outputFile = new BufferedWriter(fileWriter);
			            
			            String endTime = "", actionInst = "", cmd = "";
			            
			            while((lineInst = actionInstFile.readLine()) != null) {
			            	
			            	if(lineInst.contains("ActionInst")) {
			            		
			            		actionInst = lineInst.replace(".", "").split("= ")[1];
			            		lineList = actionListFile.readLine();
			            		endTime = lineList.split(", ")[3];
			            		cmd = "cram_finish_action(" + actionInst + ", " + endTime + ").";
			        			outputFile.write(cmd + "\n");
			            		
			            	}
			            }
			          
			            outputFile.close();
			            actionInstFile.close();
			            actionListFile.close();
			            
			        	cfResult.setText("Success!");
			            
			        }
			        catch(FileNotFoundException ex) {
			            System.out.println("Unable to open file");                
			        }
			        catch(IOException ex) {
			            System.out.println("Error reading file");     
			            System.out.println("Error writing to file");
			        }
		        	
		        } else {
		        	
		        	cfResult.setText("Invalid file");
		        	
		        }
		        
				
			}
			
		});
		
		
		getContentPane().add(browseBtn1);
		getContentPane().add(browseBtn2);
		getContentPane().add(browseBtn3);
		getContentPane().add(startBtn1);   
		getContentPane().add(startBtn2);   
		
		
	}
	
}