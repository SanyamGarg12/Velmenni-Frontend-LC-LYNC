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



 import java.awt.Color;
 
 import java.awt.Dimension;
 
 import java.awt.Font;
 
 import java.awt.Frame;
 
 import java.awt.Graphics;
 
 import java.awt.Toolkit;
 
 import java.awt.event.ActionEvent;
 
 import java.awt.event.ActionListener;
 
 import java.util.ArrayList;
 
 import java.util.Collections;
 
 import java.util.Hashtable;
 
 import java.util.Iterator;
 
 import java.util.List;
 
 import java.util.Set;
 
 
 
 import javax.swing.GroupLayout;
 
 import javax.swing.ImageIcon;
 
 import javax.swing.GroupLayout.Alignment;
 
 import javax.swing.JButton;
 
 import javax.swing.JComboBox;
 
 import javax.swing.JLabel;
 
 import javax.swing.JOptionPane;
 
 import javax.swing.JPanel;
 
 import javax.swing.LayoutStyle.ComponentPlacement;
 
 
 
 import main.InitApp;
 
 import main.JWindowSplash;
 
 
 
 import javax.swing.SwingConstants;
 
 import javax.swing.DefaultComboBoxModel;
 
 
 
 import network.MarvellNet;
 
 import network.SctDevice;
 
 
 
 import javax.swing.JTextField;
 
 import javax.swing.border.LineBorder;
 
 
 
 import java.awt.BorderLayout;
 
 
 
 import javax.swing.border.CompoundBorder;
 
 import javax.swing.border.EmptyBorder;
 
 
 
 /**
 
  * Class that defines panel SNR & CFR Panel of a G.hn node
 
  */
 
 public class JPanelNodeSNR extends JUpdatablePanel {
 
     private JLabel lblTab;
 
     private JComboBox comboBoxGraph, comboBoxTX;
 
     private JLabel lblTXNode, lblRxNode;
 
     private JButton buttonView;
 
     private JTextField lblRXMac;
 
     private JPanelChart jpChart;
 
     
 
     private String sModemIP = "";
 
     private String profile;
 
     private int iDID, iFreqAvg, iMeasType, iAskProbe;
 
   private JFrameSNR fSNR;
 
   private SctDevice modModem;
 
   private MarvellNet network;
 
     private String sDIDNode[];
 
     private JLabel lblNewLabel;
 
     private JPanel panel;
 
     /**
 
      * Constructor of the SNR & CFR Panel of a G.hn node
 
      * @param modSelectedModem Selected G.hn node from tree
 
      */
 
     public JPanelNodeSNR(SctDevice selectedModem, MarvellNet net) {
 
     
 
             setPreferredSize(new Dimension(640, 440));
 
             
 
             modModem = selectedModem;
 
             network = net;
 
             
 
             lblTab = new JLabel("Choose TX Node and type of measurement to show");
 
             lblTab.setBounds(34, 27, 388, 15);
 
             lblTab.setName("lblTab");
 
             lblTab.setForeground(Color.BLUE);
 
             lblTab.setFont(new Font("Tahoma", Font.BOLD, 12));
 
             lblTab.setHorizontalAlignment(SwingConstants.LEFT);
 
             
 
             lblTXNode = new JLabel("TX Node");
 
             lblTXNode.setBounds(34, 68, 75, 14);
 
             lblTXNode.setName("lblTXNode");
 
             lblTXNode.setFont(new Font("Tahoma", Font.BOLD, 11));
 
         
 
             lblRxNode = new JLabel("RX Node");
 
             lblRxNode.setBounds(205, 68, 88, 14);
 
             lblRxNode.setName("lblRxNode");
 
             lblRxNode.setFont(new Font("Tahoma", Font.BOLD, 11));
 
                      
 
             String [] sGraphType = {"SNR_PROBE","SNR_DATA", "PSD_RX", "NOISE"};
 
             
 
             comboBoxGraph = new JComboBox(sGraphType);
 
             comboBoxGraph.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             comboBoxGraph.setBounds(371, 88, 120, 21);
 
             comboBoxGraph.setName("comboBoxGraph");
 
             comboBoxGraph.setModel(new DefaultComboBoxModel(sGraphType));
 
             
 
             lblRXMac = new JTextField();
 
             lblRXMac.setEditable(false);
 
             lblRXMac.setBounds(205, 88, 156, 21);
 
             lblRXMac.setName("lblRXMac");
 
             lblRXMac.setHorizontalAlignment(SwingConstants.LEFT);
 
             lblRXMac.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             
 
             
 
             comboBoxTX = new JComboBox();
 
             comboBoxTX.setEnabled(false);
 
             comboBoxTX.setBounds(34, 88, 161, 21);
 
             comboBoxTX.setName("comboBoxTX");
 
             comboBoxTX.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
                     
 
             buttonView = new JButton("View");
 
             buttonView.setEnabled(false);
 
             buttonView.setFont(new Font("Tahoma", Font.BOLD, 12));
 
             buttonView.setBounds(501, 81, 110, 33);
 
             buttonView.setName("buttonView");
 
             buttonView.addActionListener(new ActionListener() {
 
               public void actionPerformed(ActionEvent arg0) {
 
                 boolean mimo = false;
 
                 
 
                 if (profile.contains("MIMO"))
 
                 {
 
                   mimo = true;
 
                 }
 
                 
 
                 if (sDIDNode.length>0){
 
                   iDID = Integer.parseInt(sDIDNode[comboBoxTX.getSelectedIndex()]);
 
 
 
                   if (comboBoxGraph.getSelectedIndex()==0) 
 
                   { //SNR probe
 
                     iMeasType = 0;
 
                     iAskProbe = 1;
 
                     iFreqAvg = 0;		
 
                   } 
 
                   else if (comboBoxGraph.getSelectedIndex()==1) 
 
                   { //SNR data
 
                     iMeasType = 1;
 
                     iAskProbe = 0;
 
                     iFreqAvg = 0;	
 
                   } 
 
                   else if (comboBoxGraph.getSelectedIndex()==2)  
 
                   { //CFR
 
                     if(mimo){//MIMO
 
                       iMeasType = 3;
 
                     } else { //SISO
 
                       iMeasType = 3;  
 
                     }
 
                     iAskProbe = 0;
 
                     iFreqAvg = 0; 
 
                   }
 
                   else // NOISE
 
                   {
 
               iMeasType = 20;
 
               iAskProbe = 1;
 
               iFreqAvg = 0; 
 
                   }
 
                   
 
                   boolean[] rmsc = GetRmsc(modModem.getRawPowerMaskValuesStored());
 
                   boolean turia = (modModem.getAsicStored().contentEquals("3142")?false:true);
 
             fSNR = new JFrameSNR(modModem, comboBoxGraph.getSelectedItem()+" from node "+comboBoxTX.getSelectedItem()+ " to node "+modModem.getMACAddrStored()+ " - "+profile, (String) comboBoxGraph.getSelectedItem(), 
 
                 mimo, iDID, iMeasType, iAskProbe, iFreqAvg,sModemIP, profile, turia, rmsc, network.getNodeWithMac(comboBoxTX.getSelectedItem().toString())); 
 
 
 
               fSNR.setLocationRelativeTo(null);
 
                   fSNR.setVisible(true);
 
 
 
                 } else {
 
                   JOptionPane.showMessageDialog(InitApp.top, "Incorrect link combination","ERROR", JOptionPane.ERROR_MESSAGE);	
 
                 }
 
               }
 
                  
 
             });
 
             setLayout(null);
 
             add(lblTab);
 
             add(lblTXNode);
 
             add(comboBoxTX);
 
             add(lblRxNode);
 
             add(lblRXMac);
 
             add(comboBoxGraph);
 
             add(buttonView);
 
             
 
             panel = new JPanel();
 
             panel.setBorder(new CompoundBorder(new LineBorder(new Color(128, 128, 128)), new EmptyBorder(5, 5, 5, 5)));
 
             panel.setBackground(new Color(250, 250, 210));
 
             panel.setBounds(34, 132, 576, 265);
 
             add(panel);
 
             panel.setLayout(new BorderLayout(0, 0));
 
             
 
             lblNewLabel = new JLabel("<html><b>Notes:</b><br><br>In order to get the measurements, this computer needs to have IP connectivity with the RX node.<br>For example, if the RX node IP address is 192.168.10.250 netmask 255.255.255.0, the IP of this computer should be 192.168.10.XXX.<br><br>There are several measurement types: <br><ul type=circle>\r\n<li><b>SNR_PROBE</b> gets the Signal/Noise in dB from the TX node to RX node.\r\n<li><b>SNR_DATA</b> also gets the SNR value from the TX node to the RX node but it does not use PROBE frames and requires data traffic from TX node to RX node. Use of SNR_PROBE is recommended.</li>\r\n<li><b>PSD_RX</b> gets the PSD received in dBm/Hz.</li><li><b>NOISE</b> gets the Noise with no signal in dBm/Hz.</li></ul>");
 
             panel.add(lblNewLabel);
 
             lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
 
             lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
 
 
 
         }
 
     
 
   protected boolean[] GetRmsc(String[] rawPowerMaskValuesStored)
 
   {
 
     if (rawPowerMaskValuesStored != null)
 
     {
 
       boolean[] rmsc = new boolean[rawPowerMaskValuesStored.length];
 
 
 
       for (int i = 0; i < rawPowerMaskValuesStored.length; i++)
 
       {
 
         int v = Integer.parseInt(rawPowerMaskValuesStored[i]);
 
 
 
         if (v ==255) 
 
         {
 
           rmsc[i] = true; 
 
         }
 
         else
 
         {
 
           rmsc[i] = false;
 
         }
 
       }
 
       return rmsc;
 
     }
 
     else
 
     {
 
       return null;
 
     }
 
   }
 
 
 
   @Override
 
     /**
 
      * Function to refresh and fill SNR & CFR panel of a G.hn node
 
      * @param selectedModem Selected G.hn node from tree
 
      * @return void
 
      */
 
     public void update(){
 
         
 
     lblRXMac.setText(modModem.getMACAddrStored());
 
     
 
     sModemIP =  modModem.getIpv4AddressStored();
 
     profile = MarvellNet.phyModeIdToProfile(modModem.getPhyModeIDStored());
 
       
 
       Hashtable<String,Integer> htDIDS = modModem.getDIDSTableStored();
 
         List<String>  macSet = new ArrayList<String>(htDIDS.keySet());
 
     Collections.sort(macSet);
 
     Iterator<String> macSetIterator = macSet.iterator();
 
     int iRows = 0;
 
     int counter = 0;
 
 
 
     //As it could be indirect nodes where DID is zero, these deviceIDs shall not be taken into account for 
 
     //this pannel
 
     while (macSetIterator.hasNext())
 
     {
 
         String macdbg = macSetIterator.next(); 
 
         Integer did   = htDIDS.get(macdbg); 
 
       if (htDIDS.get(macdbg) != 0)
 
       {
 
           iRows++;
 
       }    	
 
     }
 
 
 
     if (iRows > 1)
 
     {
 
       String sMacNode [] = new String [iRows-1];
 
       sDIDNode = new String [iRows-1];
 
       //Initialize iterator
 
       macSetIterator = macSet.iterator();
 
       
 
       while (macSetIterator.hasNext())
 
       {
 
         String sPeerMac = (String) macSetIterator.next();
 
         int sDID = (int) htDIDS.get(sPeerMac);
 
 
 
         if ((!sPeerMac.contentEquals(modModem.getMACAddrStored())) && (sDID != 0))
 
         {
 
           sMacNode [counter] = sPeerMac;
 
           sDIDNode [counter] = String.valueOf(sDID);
 
           counter = counter + 1;
 
         }
 
       }
 
       comboBoxTX.setModel(new DefaultComboBoxModel(sMacNode));
 
       comboBoxTX.setEnabled(true);
 
       buttonView.setEnabled(true);
 
     }
 
     else
 
     {
 
       comboBoxTX.setEnabled(false);
 
       buttonView.setEnabled(false);
 
     }
 
     }
 
     
 
 }
 
 