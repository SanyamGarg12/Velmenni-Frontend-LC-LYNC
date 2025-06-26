/*

 *  <legal_notice>

 *   MaxLinear, Inc. retains all right, title and interest (including all intellectual

 *   property rights) in and to this computer program, which is protected by applicable

 *   intellectual property laws.  Unless you have obtained a separate written license from

 *   MaxLinear, Inc. or an authorized licensee of MaxLinear, Inc., you are not authorized

 *   to utilize all or a part of this computer program for any purpose (including

 *   reproduction, distribution, modification, and compilation into object code), and you

 *   must immediately destroy or return all copies of this computer program.  If you are

 *   licensed by MaxLinear, Inc. or an authorized licensee of MaxLinear, Inc., your rights

 *   to utilize this computer program are limited by the terms of that license.

 *  

 *   This computer program contains trade secrets owned by MaxLinear, Inc. and, unless

 *   authorized by MaxLinear, Inc. in writing, you agree to maintain the confidentiality

 *   of this computer program and related information and to not disclose this computer

 *   program and related information to any other person or entity.

 *  

 *   Misuse of this computer program or any information contained in it may results in

 *   violations of applicable law.  MaxLinear, Inc. vigorously enforces its copyright,

 *   trade secret, patent, contractual, and other legal rights.

 *  

 *   THIS COMPUTER PROGRAM IS PROVIDED "AS IS" WITHOUT ANY WARRANTIES, AND 

 *    MAXLINEAR, INC.

 *   EXPRESSLY DISCLAIMS ALL WARRANTIES, EXPRESS OR IMPLIED, INCLUDING THE 

 *   WARRANTIES OF

 *   MERCHANTIBILITY, FITNESS FOR A PARTICULAR PURPOSE, TITLE, AND 

 *   NONINFRINGEMENT.

 *  

 *  ***************************************************************************************

 *                                          Copyright (c) 2016/2019, MaxLinear, Inc.

 *  ***************************************************************************************

 *  </legal_notice>

 */



 package gui;



 import java.awt.BorderLayout;
 
 import java.awt.EventQueue;
 
 
 
 import javax.swing.JFrame;
 
 import javax.swing.JPanel;
 
 import javax.swing.border.EmptyBorder;
 
 
 
 import measnetwork.MeasClient;
 
 import measpackets.MeasRequest;
 
 import measpackets.MeasResponse;
 
 import measpackets.MeasResponsePacket;
 
 import network.MarvellNet;
 
 import network.SctDevice;
 
 
 
 import java.awt.Toolkit;
 
 
 
 import javax.swing.JToolBar;
 
 
 
 import java.awt.Color;
 
 import java.awt.Cursor;
 
 
 
 import javax.swing.JButton;
 
 import javax.swing.JFileChooser;
 
 import javax.swing.JToggleButton;
 
 
 
 import java.awt.event.ActionListener;
 
 import java.awt.event.WindowAdapter;
 
 import java.awt.event.WindowEvent;
 
 import java.awt.image.BufferedImage;
 
 import java.io.File;
 
 import java.io.IOException;
 
 import java.net.InetAddress;
 
 import java.net.UnknownHostException;
 
 import java.awt.event.ActionEvent;
 
 
 
 import javax.swing.JLabel;
 
 import javax.swing.JOptionPane;
 
 
 
 import java.awt.Font;
 
 import java.awt.Graphics;
 
 import java.awt.SystemColor;
 
 
 
 import javax.swing.JTextField;
 
 
 
 import java.awt.Dimension;
 
 
 
 import javax.swing.JSeparator;
 
 import javax.swing.SwingConstants;
 
 import javax.imageio.ImageIO;
 
 import javax.swing.ImageIcon;
 
 import javax.swing.border.SoftBevelBorder;
 
 import javax.swing.filechooser.FileFilter;
 
 import javax.swing.filechooser.FileNameExtensionFilter;
 
 
 
 import org.jfree.chart.ChartUtilities;
 
 
 
 import main.InitApp;
 
 
 
 import javax.swing.border.BevelBorder;
 
 
 
 public class JFrameSNR extends JFrame{
 
 
 
   private JPanel contentPane;
 
   public JPanelChart chart;
 
   JToggleButton tglbtnMinmax;
 
   private JToggleButton tglbtnPause;
 
   
 
   private MeasRequest measRequest = null;
 
   private MeasResponse measResponse;
 
   private MeasResponsePacket measResponsePacket;
 
   private MeasClient measClient;
 
   private byte[] packetMeas = new byte[1472];
 
   private boolean pause = false;
 
   private boolean running = true;
 
   private int iDID, iMeasType, iFreqAvg, iAskProbe;
 
   private String sModemIP;
 
   private String sType;
 
   private String sProfile;
 
   private JLabel lblSeparator;
 
   private JLabel label_1;
 
   private JButton btnSaveAspng;
 
   private JButton btnSaveAscsv;
 
   private String title;
 
   private File currentDir;
 
   private JFrame thisFrame;
 
   private JToggleButton tbStore1;
 
   private JToggleButton tbStore2;
 
   private JLabel label_2;
 
   private JToggleButton tbStore3;
 
   private JToggleButton tbStore4;
 
   private JToggleButton tbStore5;
 
   private boolean turia;
 
   private JLabel label_4;
 
   private JTextField textState;
 
   private boolean[] pmRmsc;
 
   private SctDevice nodeTransmitter;
 
   private SctDevice nodeReceiver;
 
 
 
   /**
 
    * Create the frame.
 
    */
 
   public JFrameSNR(SctDevice modem, String ititle, String typeOfMeasure, boolean mimo, int iDIDInput, int iMeasTypeInput, int iAskProbeinput, int iFreqAvgInput, String sModemIPInput, String sProfileInput, boolean ituria, boolean[] rmsc, SctDevice nodeTx) 
 
   {
 
     setMinimumSize(new Dimension(700, 600));    
 
     thisFrame = this;
 
     this.turia = ituria;
 
     pmRmsc = rmsc;
 
     title = ititle;
 
     setTitle(title);
 
     setIconImage(Toolkit.getDefaultToolkit().getImage(JFrameSNR.class.getResource("/resources/icon.gif")));
 
     setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
 
     if (!mimo)
 
     {
 
       setBounds(100, 100, 1000, 600);
 
     }
 
     else
 
     {
 
       setBounds(100, 100, 1000, 800);
 
     }
 
     contentPane = new JPanel();
 
     contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
 
     contentPane.setLayout(new BorderLayout(0, 0));
 
     setContentPane(contentPane);
 
       
 
     JPanel panel = new JPanel();
 
     panel.setBounds(10, 11, 10, 10);
 
     contentPane.add(panel);
 
     panel.setLayout(new BorderLayout(0, 0));
 
     
 
     chart = new JPanelChart(title, typeOfMeasure, mimo);
 
 
 
     panel.add(chart, BorderLayout.CENTER);
 
     
 
     JToolBar toolBar = new JToolBar();
 
     toolBar.setFloatable(false);
 
     contentPane.add(toolBar, BorderLayout.NORTH);
 
     
 
     textState = new JTextField();
 
     textState.setMaximumSize(new Dimension(110, 2147483647));
 
     textState.setHorizontalAlignment(SwingConstants.CENTER);
 
     textState.setForeground(Color.WHITE);
 
     textState.setFont(new Font("Tahoma", Font.BOLD, 12));
 
     textState.setEditable(false);
 
     textState.setColumns(10);
 
     textState.setBackground(new Color(51, 153, 0));
 
     textState.setText("STARTING");
 
     toolBar.add(textState);
 
     
 
     tglbtnPause = new JToggleButton("Pause");
 
     tglbtnPause.setIcon(new ImageIcon(JFrameSNR.class.getResource("/resources/pause.png")));
 
     tglbtnPause.addActionListener(new ActionListener() {
 
       public void actionPerformed(ActionEvent e) {
 
         
 
         pause = tglbtnPause.isSelected();
 
       }
 
     });
 
     tglbtnPause.setFont(new Font("Tahoma", Font.PLAIN, 12));
 
     toolBar.add(tglbtnPause);
 
     
 
     label_4 = new JLabel("    ");
 
     label_4.setPreferredSize(new Dimension(14, 14));
 
     label_4.setMinimumSize(new Dimension(14, 14));
 
     toolBar.add(label_4);
 
     
 
     lblSeparator = new JLabel("    ");
 
     lblSeparator.setMinimumSize(new Dimension(14, 14));
 
     lblSeparator.setPreferredSize(new Dimension(14, 14));
 
     toolBar.add(lblSeparator);
 
     
 
     tglbtnMinmax = new JToggleButton("Min/Max");
 
     tglbtnMinmax.setIcon(new ImageIcon(JFrameSNR.class.getResource("/resources/minmax.png")));
 
     tglbtnMinmax.setFont(new Font("Tahoma", Font.PLAIN, 12));
 
     tglbtnMinmax.addActionListener(new ActionListener() {
 
       public void actionPerformed(ActionEvent e) {
 
         if (tglbtnMinmax.isSelected())
 
         {
 
           chart.minMaxMode = true;
 
         }
 
         else
 
         {
 
           chart.minMaxMode = false;
 
         }
 
       }
 
     });
 
     toolBar.add(tglbtnMinmax);
 
     
 
     tbStore1 = new JToggleButton("1");
 
     tbStore1.addActionListener(new ActionListener() {
 
       public void actionPerformed(ActionEvent e) {
 
         chart.store(0, tbStore1.isSelected());
 
       }
 
     });
 
     toolBar.add(tbStore1);
 
     tbStore1.setIcon(new ImageIcon(JFrameSNR.class.getResource("/resources/square_aquamarine.png")));
 
     
 
     tbStore2 = new JToggleButton("2");
 
     tbStore2.addActionListener(new ActionListener() {
 
       public void actionPerformed(ActionEvent e) {
 
         chart.store(1, tbStore2.isSelected());
 
       }
 
     });
 
     toolBar.add(tbStore2);
 
     tbStore2.setIcon(new ImageIcon(JFrameSNR.class.getResource("/resources/square_orange.png")));
 
     
 
     tbStore3 = new JToggleButton("3");
 
     tbStore3.addActionListener(new ActionListener() {
 
       public void actionPerformed(ActionEvent e) {
 
         chart.store(2, tbStore3.isSelected());
 
       }
 
     });
 
     tbStore3.setIcon(new ImageIcon(JFrameSNR.class.getResource("/resources/square_plum.png")));
 
     toolBar.add(tbStore3);
 
     
 
     tbStore4 = new JToggleButton("4");
 
     tbStore4.setIcon(new ImageIcon(JFrameSNR.class.getResource("/resources/square_kaki.png")));
 
     tbStore4.addActionListener(new ActionListener() {
 
       public void actionPerformed(ActionEvent e) {
 
         chart.store(3, tbStore4.isSelected());
 
       }
 
     });
 
     toolBar.add(tbStore4);
 
     
 
     tbStore5 = new JToggleButton("5");
 
     tbStore5.setIcon(new ImageIcon(JFrameSNR.class.getResource("/resources/square_purple.png")));
 
     tbStore5.addActionListener(new ActionListener() {
 
       public void actionPerformed(ActionEvent e) {
 
         chart.store(4, tbStore5.isSelected());
 
       }
 
     });
 
     toolBar.add(tbStore5);
 
     
 
     label_1 = new JLabel("    ");
 
     label_1.setPreferredSize(new Dimension(14, 14));
 
     label_1.setMinimumSize(new Dimension(14, 14));
 
     toolBar.add(label_1);
 
     
 
     btnSaveAspng = new JButton("Save as .PNG");
 
     btnSaveAspng.addActionListener(new ActionListener() {
 
       public void actionPerformed(ActionEvent e) {
 
         pause = true;
 
         thisFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
 
         try {
 
           String filename = "";
 
           JFileChooser c = new JFileChooser(currentDir);
 
           c.setSelectedFile(new File(title.replace(":", "")+".png"));
 
           FileFilter ff = new FileNameExtensionFilter("PNG picture file (*.png)", "png");
 
           c.setFileFilter(ff);
 
           c.setPreferredSize(new Dimension(600, 400));
 
           int rVal = c.showSaveDialog(thisFrame);
 
           thisFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
 
           if (rVal == JFileChooser.APPROVE_OPTION) {
 
             currentDir = c.getCurrentDirectory();
 
             String dir = c.getCurrentDirectory().toString();
 
             if (!dir.endsWith(java.io.File.separator)) {
 
               dir = dir + java.io.File.separator;
 
             }
 
             filename = dir + c.getSelectedFile().getName();
 
           }
 
           if (rVal == JFileChooser.CANCEL_OPTION) {
 
             pause = false;
 
             return;
 
           }
 
 
 
           if (chart.mimo)
 
           {
 
             File checkfile1 = new File(filename.substring(0,filename.indexOf(".png"))+" (CH1).png");
 
             if (checkfile1.exists()) {
 
               Object[] options = { "Yes", "No" };
 
               int n = JOptionPane.showOptionDialog(thisFrame, "File " + checkfile1.getName()
 
                   + " already exists.\nOverwrite file?", "File already exists",
 
                   JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
 
                   options, options[1]);
 
               if (n == 0) {
 
                 // Just continue
 
               } else {
 
                 pause = false;
 
                 return;
 
               }
 
             }
 
             chart.chart.setBackgroundPaint(Color.WHITE);
 
             ChartUtilities.saveChartAsPNG(checkfile1, chart.chart, 1200, 500);
 
             chart.chart.setBackgroundPaint(SystemColor.control);
 
             File checkfile2 = new File(filename.substring(0,filename.indexOf(".png"))+" (CH2).png");
 
             if (checkfile2.exists()) {
 
               Object[] options = { "Yes", "No" };
 
               int n = JOptionPane.showOptionDialog(thisFrame, "File " + checkfile2.getName()
 
                   + " already exists.\nOverwrite file?", "File already exists",
 
                   JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
 
                   options, options[1]);
 
               if (n == 0) {
 
                 // Just continue
 
               } else {
 
                 pause = false;
 
                 return;
 
               }
 
             }
 
             chart.chart2.setBackgroundPaint(Color.WHITE);
 
             ChartUtilities.saveChartAsPNG(checkfile2, chart.chart2, 1200, 500);
 
             chart.chart2.setBackgroundPaint(SystemColor.control);
 
             File checkfile = new File(filename);
 
             if (checkfile.exists()) {
 
               Object[] options = { "Yes", "No" };
 
               int n = JOptionPane.showOptionDialog(thisFrame, "File " + checkfile.getName()
 
                   + " already exists.\nOverwrite file?", "File already exists",
 
                   JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
 
                   options, options[1]);
 
               if (n == 0) {
 
                 // Just continue
 
               } else {
 
                 pause = false;
 
                 return;
 
               }
 
             }
 
             CombineTwoPngs(checkfile1, checkfile2, checkfile);
 
           }
 
           else
 
           {
 
             File checkfile = new File(filename);
 
             if (checkfile.exists()) {
 
               Object[] options = { "Yes", "No" };
 
               int n = JOptionPane.showOptionDialog(thisFrame, "File " + filename
 
                   + " already exists.\nOverwrite file?", "File already exists",
 
                   JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
 
                   options, options[1]);
 
               if (n == 0) {
 
                 // Just continue
 
               } else {
 
                 pause = false;
 
                 return;
 
               }
 
             }
 
             chart.chart.setBackgroundPaint(Color.WHITE);
 
             ChartUtilities.saveChartAsPNG(checkfile, chart.chart, 1200, 600);
 
             chart.chart.setBackgroundPaint(SystemColor.control);
 
           }
 
         } catch (Exception e2) {
 
           JOptionPane.showMessageDialog(thisFrame, "Error saving file:\n"+e2.getMessage(),"Spirit Config Tool Error", JOptionPane.ERROR_MESSAGE); 
 
         }
 
         pause = false;
 
       }
 
     });
 
     btnSaveAspng.setIcon(new ImageIcon(JFrameSNR.class.getResource("/resources/save.png")));
 
     btnSaveAspng.setFont(new Font("Tahoma", Font.PLAIN, 12));
 
     toolBar.add(btnSaveAspng);
 
     
 
     btnSaveAscsv = new JButton("Save as .CSV");
 
     btnSaveAscsv.addActionListener(new ActionListener() {
 
       public void actionPerformed(ActionEvent e) {
 
 
 
         pause = true;
 
         thisFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
 
         try {
 
           String filename = "";
 
           JFileChooser c = new JFileChooser(currentDir);
 
           c.setSelectedFile(new File(title.replace(":", "")+".csv"));
 
           FileFilter ff = new FileNameExtensionFilter("CSV file (*.csv)", "csv");
 
           c.setFileFilter(ff);
 
           c.setPreferredSize(new Dimension(600, 400));
 
           int rVal = c.showSaveDialog(thisFrame);
 
           thisFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
 
           if (rVal == JFileChooser.APPROVE_OPTION) {
 
             currentDir = c.getCurrentDirectory();
 
             String dir = c.getCurrentDirectory().toString();
 
             if (!dir.endsWith(java.io.File.separator)) {
 
               dir = dir + java.io.File.separator;
 
             }
 
             filename = dir + c.getSelectedFile().getName();
 
           }
 
           if (rVal == JFileChooser.CANCEL_OPTION) {
 
             pause = false;
 
             return;
 
           }
 
 
 
           if (chart.mimo)
 
           {
 
             File checkfile1 = new File(filename.substring(0,filename.indexOf(".csv"))+" (CH1).csv");
 
             if (checkfile1.exists()) {
 
               Object[] options = { "Yes", "No" };
 
               int n = JOptionPane.showOptionDialog(thisFrame, "File " + checkfile1.getName()
 
                   + " already exists.\nOverwrite file?", "File already exists",
 
                   JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
 
                   options, options[1]);
 
               if (n == 0) {
 
                 // Just continue
 
               } else {
 
                 pause = false;
 
                 return;
 
               }
 
             }
 
             chart.saveChartAsCsv(1, checkfile1.getAbsolutePath());
 
             File checkfile2 = new File(filename.substring(0,filename.indexOf(".csv"))+" (CH2).csv");
 
             if (checkfile2.exists()) {
 
               Object[] options = { "Yes", "No" };
 
               int n = JOptionPane.showOptionDialog(thisFrame, "File " + checkfile2.getName()
 
                   + " already exists.\nOverwrite file?", "File already exists",
 
                   JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
 
                   options, options[1]);
 
               if (n == 0) {
 
                 // Just continue
 
               } else {
 
                 pause = false;
 
                 return;
 
               }
 
             }
 
             chart.saveChartAsCsv(2, checkfile2.getAbsolutePath());
 
           }
 
           else
 
           {
 
             File checkfile = new File(filename);
 
             if (checkfile.exists()) {
 
               Object[] options = { "Yes", "No" };
 
               int n = JOptionPane.showOptionDialog(thisFrame, "File " + filename
 
                   + " already exists.\nOverwrite file?", "File already exists",
 
                   JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
 
                   options, options[1]);
 
               if (n == 0) {
 
                 // Just continue
 
               } else {
 
                 pause = false;
 
                 return;
 
               }
 
             }
 
             chart.saveChartAsCsv(1, checkfile.getAbsolutePath());
 
           }
 
         } catch (Exception e2) {
 
           JOptionPane.showMessageDialog(thisFrame, "Error saving file:\n"+e2.getMessage(),"Spirit Config Tool Error", JOptionPane.ERROR_MESSAGE); 
 
         }
 
         pause = false;
 
       }
 
     });
 
     btnSaveAscsv.setIcon(new ImageIcon(JFrameSNR.class.getResource("/resources/save.png")));
 
     btnSaveAscsv.setFont(new Font("Tahoma", Font.PLAIN, 12));
 
     toolBar.add(btnSaveAscsv);
 
     
 
     label_2 = new JLabel("    ");
 
     label_2.setPreferredSize(new Dimension(14, 14));
 
     label_2.setMinimumSize(new Dimension(14, 14));
 
     toolBar.add(label_2);
 
     
 
 
 
     currentDir = new File(".");
 
 
 
     // Arguments for thread
 
     iDID = iDIDInput;
 
     iMeasType = iMeasTypeInput;
 
     iAskProbe = iAskProbeinput;
 
     iFreqAvg = iFreqAvgInput;
 
     sModemIP = sModemIPInput;
 
     sProfile= sProfileInput;
 
     nodeReceiver = modem;
 
 
 
     // Create a new thread and call show within the thread. 
 
     Thread t = new Thread(new Runnable(){
 
       public void run()
 
       { 
 
         int p, q;
 
 
 
         measClient = new MeasClient(turia);
 
         int iRetriesCounter = 0;
 
 
 
         while (running)
 
         {
 
           if (!pause)
 
           {
 
             boolean error = false;
 
             
 
             measResponse = new MeasResponse(turia);
 
             packetMeas = new byte[1472];
 
 
 
             measRequest = new MeasRequest();
 
             InetAddress ModemIP = null;
 
 
 
             //Configure measurement
 
             measRequest.setPID(112211);
 
             measRequest.setTimeStamp(555);
 
             measRequest.setDID(iDID);         
 
 
 
             measRequest.setTimeAvg(4);
 
             measRequest.setAskProbe(iAskProbe);
 
             measRequest.setRemoveAGC(0);
 
 
 
             measRequest.setBandReq(0);   
 
             measRequest.setMeasType(iMeasType);
 
             measRequest.setFreqAvg(iFreqAvg);   
 
 
 
             try {
 
               ModemIP = InetAddress.getByName(sModemIP);
 
             } catch (UnknownHostException e1) {
 
               e1.printStackTrace();
 
               error = true;
 
               JOptionPane.showMessageDialog(InitApp.top, "Error in measurement: timeout.\nCheck IP connectivity between computer and G.hn nodes.\nAll IP addresses must be different.","Spirit Config Tool Error", JOptionPane.ERROR_MESSAGE);
 
             }
 
 
 
             if (!error)
 
             {
 
               long startTime = System.currentTimeMillis(); // Time Start
 
               if ((measResponse = measClient.getMeas(ModemIP, measRequest)).getErrorMeas() == 0)
 
               {// Measurement is correct
 
                 long ellapsedTime = System.currentTimeMillis()-startTime; // Ellapsed Time
 
 
 
                 /*
 
                   // Phy Mon Information
 
                       System.out.println("Phy Mon Information:");
 
                       System.out.println("Phy Metric: " + measResponse.getPhyMetric());
 
                   System.out.println("AGC1: " + measResponse.getAGC1());
 
                   System.out.println("AGC2: " + measResponse.getAGC2());
 
                   System.out.println("Freq Average (2^n): " + measResponse.getFreqAvg());
 
                   System.out.println("Time Average (2^n): " + measResponse.getTimeAvg());
 
                   System.out.println("Number of Captures: " + measResponse.getNCaptures());
 
                   System.out.println("Number of Values: " + measResponse.getNValues());
 
                   System.out.println("Bands Processing: " + measResponse.getBandProc());
 
                 */
 
                 
 
                 
 
                 int profileId = MarvellNet.profileStrToPhyModeIdAsInteger(sProfile, nodeReceiver);
 
                 // Plot Meas
 
                 if(measResponse.getPhyMetric() == 0){
 
                   update();
 
                   chart.plotsnr(measResponse, profileId, turia, pmRmsc);     
 
                 } else if(measResponse.getPhyMetric() == 1){
 
                   update();
 
                   chart.plotsnr(measResponse, profileId, turia, pmRmsc);      
 
                 } else if (measResponse.getPhyMetric() == 20){
 
                   update();
 
                   chart.plotnoise(measResponse, profileId, turia);
 
                 } else if (measResponse.getPhyMetric() == 3){
 
                   update();
 
                   chart.plotcfr(measResponse, profileId, turia, pmRmsc);
 
                 } else if (measResponse.getPhyMetric() == 4){
 
                   update();
 
                   chart.plotcfr(measResponse, profileId, turia, pmRmsc);
 
                 } else{
 
                   error = true;
 
                 }
 
               } 
 
               else 
 
               {            
 
                 error = true;
 
               }       
 
             }
 
 
 
             if (!error)
 
             {
 
               textState.setBackground(new Color(51, 153, 0));
 
               textState.setText("RUNNING");
 
             }
 
             else
 
             {
 
               textState.setBackground(new Color(204, 0, 0));
 
               textState.setText("ERROR");
 
             }
 
 
 
           }
 
           else
 
           {
 
             textState.setBackground(new Color(204, 204, 0));
 
             textState.setText("PAUSED");
 
           }
 
           try {
 
             Thread.sleep(500);
 
           } catch (InterruptedException e) {
 
             // TODO Auto-generated catch block
 
             e.printStackTrace();
 
           }
 
         }
 
       }
 
     }); //End runable/thread t
 
     t.start(); 
 
     
 
     if (nodeTx != null)
 
     {
 
       this.nodeTransmitter = nodeTx;
 
     
 
       // Create a new thread and call show within the thread. 
 
       Thread ttest = new Thread(new Runnable(){
 
         public void run()
 
         {  
 
           while (running)
 
           {
 
             if (!pause)
 
             {
 
               if(!nodeTransmitter.isAlive())
 
               {
 
                 System.err.println("Tx node "+nodeTransmitter.getMACAddrStored()+" is not responding.");
 
               }
 
               try
 
               {
 
                 Thread.sleep(200);
 
               } catch (InterruptedException e)
 
               {
 
                 // TODO Auto-generated catch block
 
                 e.printStackTrace();
 
               }
 
             }
 
           }
 
         }
 
         
 
         }); //End runable/thread ttest
 
   
 
       ttest.start(); 
 
     }
 
     
 
     addWindowListener(new WindowAdapter() {
 
       @Override
 
       public void windowClosing(WindowEvent e) {
 
         dispose();
 
         running = false;
 
       }
 
     });
 
   }
 
   
 
   void CombineTwoPngs(File checkfile1, File checkfile2, File checkfile)
 
   {
 
     try
 
     {
 
       BufferedImage i1 = ImageIO.read(checkfile1);
 
       BufferedImage i2 = ImageIO.read(checkfile2);
 
       BufferedImage merge = new BufferedImage(i1.getWidth(), i1.getHeight() + i2.getHeight(), i1.getType());
 
       Graphics g = merge.getGraphics();
 
       g.drawImage(i1, 0, 0, null);
 
       g.drawImage(i2, 0, i1.getHeight(), null);
 
       ImageIO.write(merge, "png", checkfile);
 
     } catch (IOException e)
 
     {
 
       // TODO Auto-generated catch block
 
       e.printStackTrace();
 
     }
 
 
 
   }
 
 
 
   void update()
 
   {
 
   }
 
 }
 
 