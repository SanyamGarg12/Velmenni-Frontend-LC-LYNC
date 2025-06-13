public /*

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

import java.awt.Cursor;

import java.awt.Dimension;

import java.awt.Font;

import java.awt.Graphics;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import java.awt.event.ItemListener;

import java.io.File;

import java.util.StringTokenizer;



import javax.swing.ButtonGroup;

import javax.swing.GroupLayout;

import javax.swing.GroupLayout.Alignment;

import javax.swing.JButton;

import javax.swing.JComboBox;

import javax.swing.JFileChooser;

import javax.swing.JLabel;

import javax.swing.JOptionPane;

import javax.swing.JPanel;

import javax.swing.JRadioButton;

import javax.swing.JTextField;

import javax.swing.LayoutStyle.ComponentPlacement;

import javax.swing.SwingConstants;



import network.SctDevice;



import java.awt.GridLayout;

import java.awt.event.ItemEvent;

import javax.swing.border.EtchedBorder;

import javax.swing.JToggleButton;

import javax.swing.ImageIcon;



/**

* Class that defines panel to configure Log File of a G.hn node

*/

public class JPanelQoSConfig extends JUpdatablePanel {

   private JLabel lblQoSconf;

   private JButton buttonUpdateQoS;

   private JTextField jftDectectOff1;

   private JLabel lblQoS;

   private JPanel customPanel;

   private JComboBox comboBoxCriterion;

   private	String [] sQoScriterion = {"None","Custom","802.1p","DSCP IPv4","DSCP IPv6"};

   private	String [] sPacketDectection1 = {"None","IPv4","IPv6","Custom"};

   private	String [] sPacketDectection2 = {"None","Custom"};

   private String [] sPriorities = {"0","1","2","3","4","5","6","7"};

   private	String [] sFrameType = {"Ethernet frame","802.1Q Tagged Frame","802.1ad Double Tagged Frame"};

   private JTextField jftDectectOff2;

   private JTextField jtfDetectBit2;

   private JTextField jtfDetectPatt2;

   private JTextField jtfDetectBit1;

   private JTextField jtfDetectPatt1;

   private JTextField jtfRule1Bit;

   private JTextField jtfRule1Patt;

   private JTextField jtfRule1Offset;

   private JTextField jtfRule2Offset;

   private JTextField jtfRule2Bit;

   private JTextField jtfRule2Patt;

   private JLabel lblRule3;

   private JTextField jtfRule3Offset;

   private JTextField jtfRule3Bit;

   private JTextField jtfRule3Patt;

   private JLabel lblRule4;

   private JTextField jtfRule4Offset;

   private JTextField jtfRule4Bit;

   private JTextField jtfRule4Patt;

   private JLabel lblRule5;

   private JTextField jtfRule5Offset;

   private JTextField jtfRule5Bit;

   private JTextField jtfRule5Patt;

   private JLabel lblRule6;

   private JTextField jtfRule6Offset;

   private JTextField jtfRule6Bit;

   private JTextField jtfRule6Patt;

   private JLabel lblRule7;

   private JTextField jtfRule7Offset;

   private JTextField jtfRule7Bit;

   private JTextField jtfRule7Patt;

   private JLabel lblRule8;

   private JTextField jtfRule8Offset;

   private JTextField jtfRule8Bit;

   private JTextField jtfRule8Patt;

   private JLabel lblPacketDet2;

   private JLabel lblDefaultPriority;

   private JComboBox comboBoxDefaultPrio;

   private JComboBox comboBoxRule1prio;

   private JComboBox comboBoxRule2prio;

   private JComboBox comboBoxRule3prio;

   private JComboBox comboBoxRule4prio;

   private JComboBox comboBoxRule5prio;

   private JComboBox comboBoxRule6prio;

   private JComboBox comboBoxRule7prio;

   private JComboBox comboBoxRule8prio;

   private JLabel lblTypeOfFrame;

   private JComboBox comboBoxTypeFrame;

   private SctDevice modem;

   private JPanel panelCustomPacketDetection1;

   private JPanel panelCustomPacketDetection2;

   private JPanel panelPriorities;

   private JToggleButton btnPacketDetection1Enable, btnPacketDetection2Enable;



   /**

    * Constructor of the panel to configure Log File of a G.hn node

    * @param modSelectedModem Selected G.hn node from tree

    */

   public JPanelQoSConfig(SctDevice modSelectedModem) {

   

           setPreferredSize(new Dimension(900, 780));

           

           modem = modSelectedModem;

           

           lblQoSconf = new JLabel("QoS Configuration");

           lblQoSconf.setBounds(34, 27, 202, 15);

           lblQoSconf.setName("lblQoSconf");

           lblQoSconf.setForeground(Color.BLUE);

           lblQoSconf.setFont(new Font("Tahoma", Font.BOLD, 12));

           lblQoSconf.setHorizontalAlignment(SwingConstants.LEFT);

           

           

           lblQoS = new JLabel("QoS criterion:");

           lblQoS.setBounds(34, 53, 109, 14);

           lblQoS.setName("lblQoS");

           lblQoS.setFont(new Font("Tahoma", Font.BOLD, 11));

           

                       

           buttonUpdateQoS = new JButton("Update QoS Config");

           buttonUpdateQoS.setFont(new Font("Tahoma", Font.PLAIN, 12));

           buttonUpdateQoS.setBounds(443, 48, 168, 23);

           buttonUpdateQoS.setName("buttonUpdateQoS");

           buttonUpdateQoS.addActionListener(new ActionListener() {

               public void actionPerformed(ActionEvent arg0)

               {

                 int errorCode = 1;

                 String sOldQoSstatus= modem.getQoSEnabledStored();

                 String sOld8021pStatus= modem.get8021pEnabledStored();



                 modem.setQoSEnable("NO");

                 modem.setQoS8021p("NO");

                 modem.setQoSClassifierRule(Integer.toString(Integer.parseInt(jtfRule1Offset.getText())),

                     Integer.toString(Integer.parseInt((jtfRule1Bit.getText().substring(2,6)),16)),

                     Integer.toString(Integer.parseInt((jtfRule1Patt.getText().substring(2,6)),16)),

                     "1",Integer.toString(Integer.parseInt(jtfRule2Offset.getText())),

                     Integer.toString(Integer.parseInt((jtfRule2Bit.getText().substring(2,6)),16)),

                     Integer.toString(Integer.parseInt((jtfRule2Patt.getText().substring(2,6)),16)),

                     "1",Integer.toString(Integer.parseInt(jtfRule3Offset.getText())),

                     Integer.toString(Integer.parseInt((jtfRule3Bit.getText().substring(2,6)),16)),

                     Integer.toString(Integer.parseInt((jtfRule3Patt.getText().substring(2,6)),16)),

                     "1",Integer.toString(Integer.parseInt(jtfRule4Offset.getText())),

                     Integer.toString(Integer.parseInt((jtfRule4Bit.getText().substring(2,6)),16)),

                     Integer.toString(Integer.parseInt((jtfRule4Patt.getText().substring(2,6)),16)),

                     "1",Integer.toString(Integer.parseInt(jtfRule5Offset.getText())),

                     Integer.toString(Integer.parseInt((jtfRule5Bit.getText().substring(2,6)),16)),

                     Integer.toString(Integer.parseInt((jtfRule5Patt.getText().substring(2,6)),16)),

                     "1",Integer.toString(Integer.parseInt(jtfRule6Offset.getText())),

                     Integer.toString(Integer.parseInt((jtfRule6Bit.getText().substring(2,6)),16)),

                     Integer.toString(Integer.parseInt((jtfRule6Patt.getText().substring(2,6)),16)),

                     "1",Integer.toString(Integer.parseInt(jtfRule7Offset.getText())),

                     Integer.toString(Integer.parseInt((jtfRule7Bit.getText().substring(2,6)),16)),

                     Integer.toString(Integer.parseInt((jtfRule7Patt.getText().substring(2,6)),16)),

                     "1",Integer.toString(Integer.parseInt(jtfRule8Offset.getText())),

                     Integer.toString(Integer.parseInt((jtfRule8Bit.getText().substring(2,6)),16)),

                     Integer.toString(Integer.parseInt((jtfRule8Patt.getText().substring(2,6)),16)), "1");



                 modem.setQoSDefaultPrio(Integer.toString(comboBoxDefaultPrio.getSelectedIndex()));



                 if (comboBoxCriterion.getSelectedIndex()==1 || comboBoxCriterion.getSelectedIndex()==3 || comboBoxCriterion.getSelectedIndex()==4 ){ //custom or DSCP IPv4 or DSCP IPv6

                   modem.setQoSPacketDetector(Integer.toString(Integer.parseInt(jftDectectOff1.getText())),

                       Integer.toString(Integer.parseInt((jtfDetectBit1.getText().substring(2,6)),16)),

                       Integer.toString(Integer.parseInt((jtfDetectPatt1.getText().substring(2,6)),16)),

                       "1",

                       Integer.toString(Integer.parseInt(jftDectectOff2.getText())),

                       Integer.toString(Integer.parseInt((jtfDetectBit2.getText().substring(2,6)),16)),

                       Integer.toString(Integer.parseInt((jtfDetectPatt2.getText().substring(2,6)),16)),

                       btnPacketDetection2Enable.isSelected()?"YES":"NO");

                   modem.setQoSClassifierPrios(Integer.toString(comboBoxRule1prio.getSelectedIndex()),Integer.toString(comboBoxRule2prio.getSelectedIndex()),

                       Integer.toString(comboBoxRule3prio.getSelectedIndex()),Integer.toString(comboBoxRule4prio.getSelectedIndex()),

                       Integer.toString(comboBoxRule5prio.getSelectedIndex()),Integer.toString(comboBoxRule6prio.getSelectedIndex()),

                       Integer.toString(comboBoxRule7prio.getSelectedIndex()),Integer.toString(comboBoxRule8prio.getSelectedIndex()));



                   modem.setQoSFrameType(Integer.toString(comboBoxTypeFrame.getSelectedIndex()));



                   modem.setQoSEnable("YES");



                 } else if (comboBoxCriterion.getSelectedIndex()==2){ //802.1p

                   modem.set8021pClassifierPrios(Integer.toString(comboBoxRule1prio.getSelectedIndex()),Integer.toString(comboBoxRule2prio.getSelectedIndex()),

                       Integer.toString(comboBoxRule3prio.getSelectedIndex()),Integer.toString(comboBoxRule4prio.getSelectedIndex()),

                       Integer.toString(comboBoxRule5prio.getSelectedIndex()),Integer.toString(comboBoxRule6prio.getSelectedIndex()),

                       Integer.toString(comboBoxRule7prio.getSelectedIndex()),Integer.toString(comboBoxRule8prio.getSelectedIndex()));



                   modem.setQoS8021p("YES");



                 } else if (comboBoxCriterion.getSelectedIndex()==0) { //None

                   modem.setQoSEnable("NO");

                 }

               }

           });

           

           comboBoxCriterion = new JComboBox(sQoScriterion);

           comboBoxCriterion.setToolTipText("<html><h3>QoS criterion</h3><p>The selected option indicates how <b>this</b> node will classify its transmitted traffic.</p><br><p><b>None</b></p><p>All the traffic is transmitted with the default priority</p><br><p><b>Custom</b></p><p>The node will use the indicated configuration.</p><br><p><b>802.1p</b></p><p>The node will transmit using the priority indicated by the IEEE 802.1p priority field.<br><i>Note: Use the priority fields to change the priority order.</i></p><br><p><b>DSCP IPv4</p><p>This option is actually a custom configuration.<br>This custom configuration checks the DS field in IPv4 headers (RFC 2474).</p><br><p><b>DSCP IPv6</p><p>This option is another custom configuration.</p><br>The example values can be used to create a new custom configuration.</p></html>");

           comboBoxCriterion.setFont(new Font("Tahoma", Font.PLAIN, 11));

           comboBoxCriterion.setBounds(178, 50, 119, 20);

           comboBoxCriterion.setName("comboBoxCriterion");

           comboBoxCriterion.addItemListener(new ItemListener() {

             public void itemStateChanged(ItemEvent arg0) {

               if (comboBoxCriterion.getSelectedIndex()==0){ //None

                 enableCustomPanel(false);

                 setCustomTo0();



               } else if (comboBoxCriterion.getSelectedIndex()==1){ //Custom

                 enableCustomPanel(true);



               } else if (comboBoxCriterion.getSelectedIndex()==2){ //802.1p

                 enableCustomPanel(false);

                 enablePanelPriorities(true);



                 comboBoxDefaultPrio.setSelectedItem(sPriorities[0]);



                 jftDectectOff1.setText("0");

                 jftDectectOff2.setText("0");

                 jtfDetectBit1.setText("0x0000");

                 jtfDetectBit2.setText("0x0000");

                 jtfDetectPatt1.setText("0x0000");

                 jtfDetectPatt2.setText("0x0000");

                 jtfRule1Offset.setText("0");

                 jtfRule1Bit.setText("0x0000");

                 jtfRule1Patt.setText("0x0000");

                 jtfRule2Offset.setText("0");

                 jtfRule2Bit.setText("0x0000");

                 jtfRule2Patt.setText("0x0000");

                 jtfRule3Offset.setText("0");

                 jtfRule3Bit.setText("0x0000");

                 jtfRule3Patt.setText("0x0000");

                 jtfRule4Offset.setText("0");

                 jtfRule4Bit.setText("0x0000");

                 jtfRule4Patt.setText("0x0000");

                 jtfRule5Offset.setText("0");

                 jtfRule5Bit.setText("0x0000");

                 jtfRule5Patt.setText("0x0000");

                 jtfRule6Offset.setText("0");

                 jtfRule6Bit.setText("0x0000");

                 jtfRule6Patt.setText("0x0000");

                 jtfRule7Offset.setText("0");

                 jtfRule7Bit.setText("0x0000");

                 jtfRule7Patt.setText("0x0000");

                 jtfRule8Offset.setText("0");

                 jtfRule8Bit.setText("0x0000");

                 jtfRule8Patt.setText("0x0000");



                 comboBoxRule1prio.setSelectedItem(sPriorities[0]);

                 comboBoxRule2prio.setSelectedItem(sPriorities[1]);

                 comboBoxRule3prio.setSelectedItem(sPriorities[2]);

                 comboBoxRule4prio.setSelectedItem(sPriorities[3]);

                 comboBoxRule5prio.setSelectedItem(sPriorities[4]);

                 comboBoxRule6prio.setSelectedItem(sPriorities[5]);

                 comboBoxRule7prio.setSelectedItem(sPriorities[6]);

                 comboBoxRule8prio.setSelectedItem(sPriorities[7]);



               } 

               else if (comboBoxCriterion.getSelectedIndex()==3){ //DSCP IPv4

                 enableCustomPanel(true);



                 enablePanelPacketDetection(1, true);

                 jftDectectOff1.setText("6");

                 jtfDetectBit1.setText("0xFFFF");

                 jtfDetectPatt1.setText("0x0800");

                 enablePanelPacketDetection(2, false);



                 jtfRule1Offset.setText("7");

                 jtfRule1Bit.setText("0x00E0");

                 jtfRule1Patt.setText("0x0000");

                 jtfRule2Offset.setText("7");

                 jtfRule2Bit.setText("0x00E0");

                 jtfRule2Patt.setText("0x0020");

                 jtfRule3Offset.setText("7");

                 jtfRule3Bit.setText("0x00E0");

                 jtfRule3Patt.setText("0x0040");

                 jtfRule4Offset.setText("7");

                 jtfRule4Bit.setText("0x00E0");

                 jtfRule4Patt.setText("0x0060");

                 jtfRule5Offset.setText("7");

                 jtfRule5Bit.setText("0x00E0");

                 jtfRule5Patt.setText("0x0080");

                 jtfRule6Offset.setText("7");

                 jtfRule6Bit.setText("0x00E0");

                 jtfRule6Patt.setText("0x00A0");

                 jtfRule7Offset.setText("7");

                 jtfRule7Bit.setText("0x00E0");

                 jtfRule7Patt.setText("0x00C0");

                 jtfRule8Offset.setText("7");

                 jtfRule8Bit.setText("0x00E0");

                 jtfRule8Patt.setText("0x00E0");



                 comboBoxRule1prio.setSelectedItem(sPriorities[0]);

                 comboBoxRule2prio.setSelectedItem(sPriorities[1]);

                 comboBoxRule3prio.setSelectedItem(sPriorities[2]);

                 comboBoxRule4prio.setSelectedItem(sPriorities[3]);

                 comboBoxRule5prio.setSelectedItem(sPriorities[4]);

                 comboBoxRule6prio.setSelectedItem(sPriorities[5]);

                 comboBoxRule7prio.setSelectedItem(sPriorities[6]);

                 comboBoxRule8prio.setSelectedItem(sPriorities[7]);

               }

               else if (comboBoxCriterion.getSelectedIndex()==4){ //DSCP IPv6

                 enableCustomPanel(true);



                 enablePanelPacketDetection(1, true);

                 jftDectectOff1.setText("6");

                 jtfDetectBit1.setText("0xFFFF");

                 jtfDetectPatt1.setText("0x86DD");

                 enablePanelPacketDetection(2, false);



                 jtfRule1Offset.setText("7");

                 jtfRule1Bit.setText("0x0E00");

                 jtfRule1Patt.setText("0x0000");

                 jtfRule2Offset.setText("7");

                 jtfRule2Bit.setText("0x0E00");

                 jtfRule2Patt.setText("0x0200");

                 jtfRule3Offset.setText("7");

                 jtfRule3Bit.setText("0x0E00");

                 jtfRule3Patt.setText("0x0400");

                 jtfRule4Offset.setText("7");

                 jtfRule4Bit.setText("0x0E00");

                 jtfRule4Patt.setText("0x0600");

                 jtfRule5Offset.setText("7");

                 jtfRule5Bit.setText("0x0E00");

                 jtfRule5Patt.setText("0x0800");

                 jtfRule6Offset.setText("7");

                 jtfRule6Bit.setText("0x0E00");

                 jtfRule6Patt.setText("0x0A00");

                 jtfRule7Offset.setText("7");

                 jtfRule7Bit.setText("0x0E00");

                 jtfRule7Patt.setText("0x0C00");

                 jtfRule8Offset.setText("7");

                 jtfRule8Bit.setText("0x0E00");

                 jtfRule8Patt.setText("0x0E00");



                 comboBoxRule1prio.setSelectedItem(sPriorities[0]);

                 comboBoxRule2prio.setSelectedItem(sPriorities[1]);

                 comboBoxRule3prio.setSelectedItem(sPriorities[2]);

                 comboBoxRule4prio.setSelectedItem(sPriorities[3]);

                 comboBoxRule5prio.setSelectedItem(sPriorities[4]);

                 comboBoxRule6prio.setSelectedItem(sPriorities[5]);

                 comboBoxRule7prio.setSelectedItem(sPriorities[6]);

                 comboBoxRule8prio.setSelectedItem(sPriorities[7]);

               }

             }

           });

           setLayout(null);

           add(lblQoS);

           add(comboBoxCriterion);

           add(lblQoSconf);

           add(buttonUpdateQoS);

           

           customPanel = new JPanel();

           customPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

           customPanel.setBounds(34, 108, 577, 337);

           add(customPanel);

           customPanel.setLayout(null);

           

           comboBoxTypeFrame = new JComboBox(sFrameType);

           comboBoxTypeFrame.setBounds(150, 11, 244, 20);

           customPanel.add(comboBoxTypeFrame);

           comboBoxTypeFrame.setFont(new Font("Tahoma", Font.PLAIN, 11));

           comboBoxTypeFrame.setName("comboBoxTypeFrame");

           comboBoxTypeFrame.setToolTipText("Type of traffic frames to be classified");

           

           lblTypeOfFrame = new JLabel("Type of Frame:");

           lblTypeOfFrame.setBounds(10, 14, 107, 14);

           customPanel.add(lblTypeOfFrame);

           lblTypeOfFrame.setName("lblTypeOfFrame");

           lblTypeOfFrame.setFont(new Font("Tahoma", Font.BOLD, 11));

                                               

                                               JLabel lblPacketClassification = new JLabel("Packet classification:");

                                               lblPacketClassification.setBounds(10, 139, 160, 14);

                                               customPanel.add(lblPacketClassification);

                                               lblPacketClassification.setName("lblPacketClassification");

                                               lblPacketClassification.setFont(new Font("Tahoma", Font.BOLD, 11));

                                               

                                               JLabel lblOffset = new JLabel("Offset:");

                                               lblOffset.setBounds(180, 139, 63, 14);

                                               customPanel.add(lblOffset);

                                               lblOffset.setName("lblOffset");

                                               lblOffset.setFont(new Font("Tahoma", Font.BOLD, 11));

                                               

                                               JLabel lblBitmask = new JLabel("Bitmask:");

                                               lblBitmask.setBounds(253, 139, 85, 14);

                                               customPanel.add(lblBitmask);

                                               lblBitmask.setName("lblBitmask");

                                               lblBitmask.setFont(new Font("Tahoma", Font.BOLD, 11));

                                               

                                               JLabel lblPattern = new JLabel("Pattern:");

                                               lblPattern.setBounds(369, 139, 85, 14);

                                               customPanel.add(lblPattern);

                                               lblPattern.setName("lblPattern");

                                               lblPattern.setFont(new Font("Tahoma", Font.BOLD, 11));

                                               

                                               JLabel lblRule1 = new JLabel("Rule 1:");

                                               lblRule1.setBounds(67, 160, 64, 14);

                                               customPanel.add(lblRule1);

                                               lblRule1.setName("lblRule1");

                                               lblRule1.setFont(new Font("Tahoma", Font.BOLD, 11));

                                               

                                               jtfRule1Offset = new JTextField();

                                               jtfRule1Offset.setBounds(178, 157, 44, 20);

                                               customPanel.add(jtfRule1Offset);

                                               jtfRule1Offset.setFont(new Font("Tahoma", Font.PLAIN, 11));

                                               jtfRule1Offset.setName("jtfRule1Offset");

                                               jtfRule1Offset.setToolTipText("Offset in Ethernet packet (number of 2Bytes words in decimal)");

                                               jtfRule1Offset.setColumns(20);

                                               

                                               jtfRule1Bit = new JTextField();

                                               jtfRule1Bit.setBounds(251, 157, 108, 20);

                                               customPanel.add(jtfRule1Bit);

                                               jtfRule1Bit.setFont(new Font("Tahoma", Font.PLAIN, 11));

                                               jtfRule1Bit.setName("jtfRule1Bit");

                                               jtfRule1Bit.setToolTipText("4 bytes bitmask");

                                               jtfRule1Bit.setColumns(20);

                                               

                                               jtfRule1Patt = new JTextField();

                                               jtfRule1Patt.setBounds(367, 157, 107, 20);

                                               customPanel.add(jtfRule1Patt);

                                               jtfRule1Patt.setFont(new Font("Tahoma", Font.PLAIN, 11));

                                               jtfRule1Patt.setName("jtfRule1Patt");

                                               jtfRule1Patt.setToolTipText("4 bytes pattern");

                                               jtfRule1Patt.setColumns(20);

           

           JLabel lblRule2 = new JLabel("Rule 2:");

           lblRule2.setBounds(67, 181, 64, 14);

           customPanel.add(lblRule2);

           lblRule2.setName("lblRule2");

           lblRule2.setFont(new Font("Tahoma", Font.BOLD, 11));

           

           jtfRule2Offset = new JTextField();

           jtfRule2Offset.setBounds(178, 178, 44, 20);

           customPanel.add(jtfRule2Offset);

           jtfRule2Offset.setFont(new Font("Tahoma", Font.PLAIN, 11));

           jtfRule2Offset.setName("jtfRule2Offset");

           jtfRule2Offset.setToolTipText("Offset in Ethernet packet (number of 2Bytes words in decimal)");

           jtfRule2Offset.setColumns(20);

           

           jtfRule2Bit = new JTextField();

           jtfRule2Bit.setBounds(251, 178, 108, 20);

           customPanel.add(jtfRule2Bit);

           jtfRule2Bit.setFont(new Font("Tahoma", Font.PLAIN, 11));

           jtfRule2Bit.setName("jtfRule2Bit");

           jtfRule2Bit.setToolTipText("4 bytes bitmask");

           jtfRule2Bit.setColumns(20);

           

           jtfRule2Patt = new JTextField();

           jtfRule2Patt.setBounds(367, 178, 107, 20);

           customPanel.add(jtfRule2Patt);

           jtfRule2Patt.setFont(new Font("Tahoma", Font.PLAIN, 11));

           jtfRule2Patt.setName("jtfRule2Patt");

           jtfRule2Patt.setToolTipText("4 bytes pattern");

           jtfRule2Patt.setColumns(20);

           

           lblRule3 = new JLabel("Rule 3:");

           lblRule3.setBounds(67, 202, 64, 14);

           customPanel.add(lblRule3);

           lblRule3.setName("lblRule3");

           lblRule3.setFont(new Font("Tahoma", Font.BOLD, 11));

           

           jtfRule3Offset = new JTextField();

           jtfRule3Offset.setBounds(178, 199, 44, 20);

           customPanel.add(jtfRule3Offset);

           jtfRule3Offset.setFont(new Font("Tahoma", Font.PLAIN, 11));

           jtfRule3Offset.setName("jtfRule3Offset");

           jtfRule3Offset.setToolTipText("Offset in Ethernet packet (number of 2Bytes words in decimal)");

           jtfRule3Offset.setColumns(20);

           

           jtfRule3Bit = new JTextField();

           jtfRule3Bit.setBounds(251, 199, 108, 20);

           customPanel.add(jtfRule3Bit);

           jtfRule3Bit.setFont(new Font("Tahoma", Font.PLAIN, 11));

           jtfRule3Bit.setName("jtfRule3Bit");

           jtfRule3Bit.setToolTipText("4 bytes bitmask");

           jtfRule3Bit.setColumns(20);

           

           jtfRule3Patt = new JTextField();

           jtfRule3Patt.setBounds(367, 199, 107, 20);

           customPanel.add(jtfRule3Patt);

           jtfRule3Patt.setFont(new Font("Tahoma", Font.PLAIN, 11));

           jtfRule3Patt.setName("jtfRule3Patt");

           jtfRule3Patt.setToolTipText("4 bytes pattern");

           jtfRule3Patt.setColumns(20);

           

           lblRule4 = new JLabel("Rule 4:");

           lblRule4.setBounds(67, 223, 64, 14);

           customPanel.add(lblRule4);

           lblRule4.setName("lblRule4");

           lblRule4.setFont(new Font("Tahoma", Font.BOLD, 11));

           

           jtfRule4Offset = new JTextField();

           jtfRule4Offset.setBounds(178, 220, 44, 20);

           customPanel.add(jtfRule4Offset);

           jtfRule4Offset.setFont(new Font("Tahoma", Font.PLAIN, 11));

           jtfRule4Offset.setName("jtfRule4Offset");

           jtfRule4Offset.setToolTipText("Offset in Ethernet packet (number of 2Bytes words in decimal)");

           jtfRule4Offset.setColumns(20);

           

           jtfRule4Bit = new JTextField();

           jtfRule4Bit.setBounds(251, 220, 108, 20);

           customPanel.add(jtfRule4Bit);

           jtfRule4Bit.setFont(new Font("Tahoma", Font.PLAIN, 11));

           jtfRule4Bit.setName("jtfRule4Bit");

           jtfRule4Bit.setToolTipText("4 bytes bitmask");

           jtfRule4Bit.setColumns(20);

           

           jtfRule4Patt = new JTextField();

           jtfRule4Patt.setBounds(367, 220, 107, 20);

           customPanel.add(jtfRule4Patt);

           jtfRule4Patt.setFont(new Font("Tahoma", Font.PLAIN, 11));

           jtfRule4Patt.setName("jtfRule4Patt");

           jtfRule4Patt.setToolTipText("4 bytes pattern");

           jtfRule4Patt.setColumns(20);

           

           lblRule5 = new JLabel("Rule 5:");

           lblRule5.setBounds(67, 244, 64, 14);

           customPanel.add(lblRule5);

           lblRule5.setName("lblRule5");

           lblRule5.setFont(new Font("Tahoma", Font.BOLD, 11));

           

           jtfRule5Offset = new JTextField();

           jtfRule5Offset.setBounds(178, 241, 44, 20);

           customPanel.add(jtfRule5Offset);

           jtfRule5Offset.setFont(new Font("Tahoma", Font.PLAIN, 11));

           jtfRule5Offset.setName("jtfRule5Offset");

           jtfRule5Offset.setToolTipText("Offset in Ethernet packet (number of 2Bytes words in decimal)");

           jtfRule5Offset.setColumns(20);

           

           jtfRule5Bit = new JTextField();

           jtfRule5Bit.setBounds(251, 241, 108, 20);

           customPanel.add(jtfRule5Bit);

           jtfRule5Bit.setFont(new Font("Tahoma", Font.PLAIN, 11));

           jtfRule5Bit.setName("jtfRule5Bit");

           jtfRule5Bit.setToolTipText("4 bytes bitmask");

           jtfRule5Bit.setColumns(20);

           

           jtfRule5Patt = new JTextField();

           jtfRule5Patt.setBounds(367, 241, 107, 20);

           customPanel.add(jtfRule5Patt);

           jtfRule5Patt.setFont(new Font("Tahoma", Font.PLAIN, 11));

           jtfRule5Patt.setName("jtfRule5Patt");

           jtfRule5Patt.setToolTipText("4 bytes pattern");

           jtfRule5Patt.setColumns(20);

           

           lblRule6 = new JLabel("Rule 6:");

           lblRule6.setBounds(67, 265, 64, 14);

           customPanel.add(lblRule6);

           lblRule6.setName("lblRule6");

           lblRule6.setFont(new Font("Tahoma", Font.BOLD, 11));

           

           jtfRule6Offset = new JTextField();

           jtfRule6Offset.setBounds(178, 262, 44, 20);

           customPanel.add(jtfRule6Offset);

           jtfRule6Offset.setFont(new Font("Tahoma", Font.PLAIN, 11));

           jtfRule6Offset.setName("jtfRule6Offset");

           jtfRule6Offset.setToolTipText("Offset in Ethernet packet (number of 2Bytes words in decimal)");

           jtfRule6Offset.setColumns(20);

           

           jtfRule6Bit = new JTextField();

           jtfRule6Bit.setBounds(251, 262, 108, 20);

           customPanel.add(jtfRule6Bit);

           jtfRule6Bit.setFont(new Font("Tahoma", Font.PLAIN, 11));

           jtfRule6Bit.setName("jtfRule6Bit");

           jtfRule6Bit.setToolTipText("4 bytes bitmask");

           jtfRule6Bit.setColumns(20);

           

           jtfRule6Patt = new JTextField();

           jtfRule6Patt.setBounds(367, 262, 107, 20);

           customPanel.add(jtfRule6Patt);

           jtfRule6Patt.setFont(new Font("Tahoma", Font.PLAIN, 11));

           jtfRule6Patt.setName("jtfRule6Patt");

           jtfRule6Patt.setToolTipText("4 bytes pattern");

           jtfRule6Patt.setColumns(20);

           

           lblRule7 = new JLabel("Rule 7:");

           lblRule7.setBounds(67, 286, 64, 14);

           customPanel.add(lblRule7);

           lblRule7.setName("lblRule7");

           lblRule7.setFont(new Font("Tahoma", Font.BOLD, 11));

           

           jtfRule7Offset = new JTextField();

           jtfRule7Offset.setBounds(178, 283, 44, 20);

           customPanel.add(jtfRule7Offset);

           jtfRule7Offset.setFont(new Font("Tahoma", Font.PLAIN, 11));

           jtfRule7Offset.setName("jtfRule7Offset");

           jtfRule7Offset.setToolTipText("Offset in Ethernet packet (number of 2Bytes words in decimal)");

           jtfRule7Offset.setColumns(20);

           

           jtfRule7Bit = new JTextField();

           jtfRule7Bit.setBounds(251, 283, 108, 20);

           customPanel.add(jtfRule7Bit);

           jtfRule7Bit.setFont(new Font("Tahoma", Font.PLAIN, 11));

           jtfRule7Bit.setName("jtfRule7Bit");

           jtfRule7Bit.setToolTipText("4 bytes bitmask");

           jtfRule7Bit.setColumns(20);

           

           jtfRule7Patt = new JTextField();

           jtfRule7Patt.setBounds(367, 283, 107, 20);

           customPanel.add(jtfRule7Patt);

           jtfRule7Patt.setFont(new Font("Tahoma", Font.PLAIN, 11));

           jtfRule7Patt.setName("jtfRule7Patt");

           jtfRule7Patt.setToolTipText("4 bytes pattern");

           jtfRule7Patt.setColumns(20);

           

           lblRule8 = new JLabel("Rule 8:");

           lblRule8.setBounds(67, 307, 64, 14);

           customPanel.add(lblRule8);

           lblRule8.setName("lblRule8");

           lblRule8.setFont(new Font("Tahoma", Font.BOLD, 11));

           

           jtfRule8Offset = new JTextField();

           jtfRule8Offset.setBounds(178, 304, 44, 20);

           customPanel.add(jtfRule8Offset);

           jtfRule8Offset.setFont(new Font("Tahoma", Font.PLAIN, 11));

           jtfRule8Offset.setName("jtfRule8Offset");

           jtfRule8Offset.setToolTipText("Offset in Ethernet packet (number of 2Bytes words in decimal)");

           jtfRule8Offset.setColumns(20);

           

           jtfRule8Bit = new JTextField();

           jtfRule8Bit.setBounds(251, 304, 108, 20);

           customPanel.add(jtfRule8Bit);

           jtfRule8Bit.setFont(new Font("Tahoma", Font.PLAIN, 11));

           jtfRule8Bit.setName("jtfRule8Bit");

           jtfRule8Bit.setToolTipText("4 bytes bitmask");

           jtfRule8Bit.setColumns(20);

           

           jtfRule8Patt = new JTextField();

           jtfRule8Patt.setBounds(367, 304, 107, 20);

           customPanel.add(jtfRule8Patt);

           jtfRule8Patt.setFont(new Font("Tahoma", Font.PLAIN, 11));

           jtfRule8Patt.setName("jtfRule8Patt");

           jtfRule8Patt.setToolTipText("4 bytes pattern");

           jtfRule8Patt.setColumns(20);

           

           panelCustomPacketDetection1 = new JPanel();

           panelCustomPacketDetection1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

           panelCustomPacketDetection1.setBounds(10, 42, 267, 86);

           customPanel.add(panelCustomPacketDetection1);

           panelCustomPacketDetection1.setLayout(null);

           

                       JLabel lblOffset1 = new JLabel("Offset 1:");

                       lblOffset1.setBounds(10, 39, 58, 14);

                       panelCustomPacketDetection1.add(lblOffset1);

                       lblOffset1.setName("lblOffset1");

                       lblOffset1.setFont(new Font("Tahoma", Font.BOLD, 11));

                       

                       JLabel lblBitmask1 = new JLabel("Bitmask 1:");

                       lblBitmask1.setBounds(77, 39, 85, 14);

                       panelCustomPacketDetection1.add(lblBitmask1);

                       lblBitmask1.setName("lblBitmask1");

                       lblBitmask1.setFont(new Font("Tahoma", Font.BOLD, 11));

                       

                       JLabel lblPattern1 = new JLabel("Pattern 1:");

                       lblPattern1.setBounds(169, 39, 85, 14);

                       panelCustomPacketDetection1.add(lblPattern1);

                       lblPattern1.setName("lblPattern1");

                       lblPattern1.setFont(new Font("Tahoma", Font.BOLD, 11));

                       

                       jtfDetectPatt1 = new JTextField();

                       jtfDetectPatt1.setBounds(169, 59, 91, 20);

                       panelCustomPacketDetection1.add(jtfDetectPatt1);

                       jtfDetectPatt1.setFont(new Font("Tahoma", Font.PLAIN, 11));

                       jtfDetectPatt1.setName("jtfDetectPatt1");

                       jtfDetectPatt1.setToolTipText("4 bytes pattern");

                       jtfDetectPatt1.setColumns(20);

                       

                       jtfDetectBit1 = new JTextField();

                       jtfDetectBit1.setBounds(77, 59, 82, 20);

                       panelCustomPacketDetection1.add(jtfDetectBit1);

                       jtfDetectBit1.setFont(new Font("Tahoma", Font.PLAIN, 11));

                       jtfDetectBit1.setName("jtfDetectBit1");

                       jtfDetectBit1.setToolTipText("4 bytes bitmask");

                       jtfDetectBit1.setColumns(20);

                       

                       jftDectectOff1 = new JTextField();

                       jftDectectOff1.setBounds(10, 59, 34, 20);

                       panelCustomPacketDetection1.add(jftDectectOff1);

                       jftDectectOff1.setFont(new Font("Tahoma", Font.PLAIN, 11));

                       jftDectectOff1.setName("jftDectectOff1");

                       jftDectectOff1.setToolTipText("Offset in Ethernet packet (number of 2Bytes words in decimal)");

                       jftDectectOff1.setColumns(20);

                       

                       JLabel lblPacketDet1 = new JLabel("Packet detection 1:");

                       lblPacketDet1.setBounds(10, 11, 132, 14);

                       panelCustomPacketDetection1.add(lblPacketDet1);

                       lblPacketDet1.setName("lblPacketDet1");

                       lblPacketDet1.setFont(new Font("Tahoma", Font.BOLD, 11));

                       

                       btnPacketDetection1Enable = new JToggleButton("Disabled");

                       btnPacketDetection1Enable.setFont(new Font("Tahoma", Font.PLAIN, 11));

                       btnPacketDetection1Enable.setFocusable(false);

                       btnPacketDetection1Enable.addActionListener(new ActionListener() {

                         public void actionPerformed(ActionEvent e) {

                           if (btnPacketDetection1Enable.isSelected())

                           {

                             enablePanelPacketDetection(1, true);

                           }

                           else

                           {

                             enablePanelPacketDetection(1, false);

                           }

                         }

                       });

                       btnPacketDetection1Enable.setBounds(171, 5, 89, 23);

                       panelCustomPacketDetection1.add(btnPacketDetection1Enable);

                       

                       panelCustomPacketDetection2 = new JPanel();

                       panelCustomPacketDetection2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

                       panelCustomPacketDetection2.setBounds(287, 42, 280, 86);

                       customPanel.add(panelCustomPacketDetection2);

                       panelCustomPacketDetection2.setLayout(null);

                       

                                   JLabel lblOffset2 = new JLabel("Offset 2:");

                                   lblOffset2.setBounds(10, 39, 59, 14);

                                   panelCustomPacketDetection2.add(lblOffset2);

                                   lblOffset2.setName("lblOffset2");

                                   lblOffset2.setFont(new Font("Tahoma", Font.BOLD, 11));

                                   

                                   JLabel lblBitmask2 = new JLabel("Bitmask 2:");

                                   lblBitmask2.setBounds(77, 39, 88, 14);

                                   panelCustomPacketDetection2.add(lblBitmask2);

                                   lblBitmask2.setName("lblBitmask2");

                                   lblBitmask2.setFont(new Font("Tahoma", Font.BOLD, 11));

                                   

                                   JLabel lblPattern2 = new JLabel("Pattern 2:");

                                   lblPattern2.setBounds(172, 39, 90, 14);

                                   panelCustomPacketDetection2.add(lblPattern2);

                                   lblPattern2.setName("lblPattern2");

                                   lblPattern2.setFont(new Font("Tahoma", Font.BOLD, 11));

                                   

                                   jtfDetectPatt2 = new JTextField();

                                   jtfDetectPatt2.setBounds(172, 59, 98, 20);

                                   panelCustomPacketDetection2.add(jtfDetectPatt2);

                                   jtfDetectPatt2.setFont(new Font("Tahoma", Font.PLAIN, 11));

                                   jtfDetectPatt2.setName("jtfDetectPatt2");

                                   jtfDetectPatt2.setToolTipText("4 bytes pattern");

                                   jtfDetectPatt2.setColumns(20);

                                   

                                   jtfDetectBit2 = new JTextField();

                                   jtfDetectBit2.setBounds(77, 59, 80, 20);

                                   panelCustomPacketDetection2.add(jtfDetectBit2);

                                   jtfDetectBit2.setFont(new Font("Tahoma", Font.PLAIN, 11));

                                   jtfDetectBit2.setName("jtfDetectBit2");

                                   jtfDetectBit2.setToolTipText("4 bytes bitmask");

                                   jtfDetectBit2.setColumns(20);

                                   

                                   jftDectectOff2 = new JTextField();

                                   jftDectectOff2.setBounds(10, 59, 34, 20);

                                   panelCustomPacketDetection2.add(jftDectectOff2);

                                   jftDectectOff2.setFont(new Font("Tahoma", Font.PLAIN, 11));

                                   jftDectectOff2.setName("jftDectectOff2");

                                   jftDectectOff2.setToolTipText("Offset in Ethernet packet (number of 2Bytes words in decimal)");

                                   jftDectectOff2.setColumns(20);

                                   

                                   lblPacketDet2 = new JLabel("Packet detection 2:");

                                   lblPacketDet2.setBounds(10, 11, 134, 14);

                                   panelCustomPacketDetection2.add(lblPacketDet2);

                                   lblPacketDet2.setName("lblPacketDet2");

                                   lblPacketDet2.setFont(new Font("Tahoma", Font.BOLD, 11));

                                   

                                   btnPacketDetection2Enable = new JToggleButton("Disabled");

                                   btnPacketDetection2Enable.setFont(new Font("Tahoma", Font.PLAIN, 11));

                                   btnPacketDetection2Enable.setFocusable(false);

                                   btnPacketDetection2Enable.addActionListener(new ActionListener() {

                                     public void actionPerformed(ActionEvent e) {

                              if (btnPacketDetection2Enable.isSelected())

                               {

                                 enablePanelPacketDetection(2, true);

                               }

                               else

                               {

                                 enablePanelPacketDetection(2, false);

                               }

                                     }

                                   });

                                   btnPacketDetection2Enable.setBounds(181, 5, 89, 23);

                                   panelCustomPacketDetection2.add(btnPacketDetection2Enable);

                                   

                                   panelPriorities = new JPanel();

                                   panelPriorities.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

                                   panelPriorities.setBounds(484, 135, 83, 196);

                                   customPanel.add(panelPriorities);

                                   panelPriorities.setLayout(null);

                                   

                                   JLabel lblPriority = new JLabel("Priority:");

                                   lblPriority.setBounds(10, 4, 64, 14);

                                   panelPriorities.add(lblPriority);

                                   lblPriority.setName("lblPriority");

                                   lblPriority.setFont(new Font("Tahoma", Font.BOLD, 11));

                                   

           comboBoxRule1prio = new JComboBox(sPriorities);

           comboBoxRule1prio.setBounds(10, 22, 64, 20);

           panelPriorities.add(comboBoxRule1prio);

           comboBoxRule1prio.setFont(new Font("Tahoma", Font.PLAIN, 11));

           comboBoxRule1prio.setName("comboBoxRule1prio");

           

           comboBoxRule2prio = new JComboBox(sPriorities);

           comboBoxRule2prio.setBounds(10, 43, 64, 20);

           panelPriorities.add(comboBoxRule2prio);

           comboBoxRule2prio.setFont(new Font("Tahoma", Font.PLAIN, 11));

           comboBoxRule2prio.setName("comboBoxRule2prio");

           

           comboBoxRule3prio = new JComboBox(sPriorities);

           comboBoxRule3prio.setBounds(10, 64, 64, 20);

           panelPriorities.add(comboBoxRule3prio);

           comboBoxRule3prio.setFont(new Font("Tahoma", Font.PLAIN, 11));

           comboBoxRule3prio.setName("comboBoxRule3prio");

           

           comboBoxRule4prio = new JComboBox(sPriorities);

           comboBoxRule4prio.setBounds(10, 85, 64, 20);

           panelPriorities.add(comboBoxRule4prio);

           comboBoxRule4prio.setFont(new Font("Tahoma", Font.PLAIN, 11));

           comboBoxRule4prio.setName("comboBoxRule4prio");

           

           comboBoxRule5prio = new JComboBox(sPriorities);

           comboBoxRule5prio.setBounds(10, 106, 63, 20);

           panelPriorities.add(comboBoxRule5prio);

           comboBoxRule5prio.setFont(new Font("Tahoma", Font.PLAIN, 11));

           comboBoxRule5prio.setName("comboBoxRule5prio");

           

           comboBoxRule6prio = new JComboBox(sPriorities);

           comboBoxRule6prio.setBounds(10, 127, 64, 20);

           panelPriorities.add(comboBoxRule6prio);

           comboBoxRule6prio.setFont(new Font("Tahoma", Font.PLAIN, 11));

           comboBoxRule6prio.setName("comboBoxRule6prio");

           

           comboBoxRule7prio = new JComboBox(sPriorities);

           comboBoxRule7prio.setBounds(10, 148, 64, 20);

           panelPriorities.add(comboBoxRule7prio);

           comboBoxRule7prio.setFont(new Font("Tahoma", Font.PLAIN, 11));

           comboBoxRule7prio.setName("comboBoxRule7prio");

           

           comboBoxRule8prio = new JComboBox(sPriorities);

           comboBoxRule8prio.setBounds(10, 169, 64, 20);

           panelPriorities.add(comboBoxRule8prio);

           comboBoxRule8prio.setFont(new Font("Tahoma", Font.PLAIN, 11));

           comboBoxRule8prio.setName("comboBoxRule8prio");

           

           lblDefaultPriority = new JLabel("Default priority:");

           lblDefaultPriority.setBounds(34, 83, 119, 14);

           add(lblDefaultPriority);

           lblDefaultPriority.setName("lblDefaultPriority");

           lblDefaultPriority.setFont(new Font("Tahoma", Font.BOLD, 11));

           

           comboBoxDefaultPrio = new JComboBox(sPriorities);

           comboBoxDefaultPrio.setToolTipText("<html><h3>Default priority</h3><p>This priority is used for every transmitted packet if the criterion is <b>None</b>.</p><br><p>It is used for non tagged packets if the criterion is <b>802.1p</b>.</p><br><p>It is used by packets that do not match the custom packet detection (1 or 2) if <br>the criterion is <b>Custom</b>.</p></html>");

           comboBoxDefaultPrio.setBounds(178, 81, 119, 20);

           add(comboBoxDefaultPrio);

           comboBoxDefaultPrio.setFont(new Font("Tahoma", Font.PLAIN, 11));

           comboBoxDefaultPrio.setName("comboBoxDefaultPrio");

       }

       

   protected void setCustomTo0() {

   // TODO Auto-generated method stub

     comboBoxDefaultPrio.setSelectedItem(sPriorities[0]);

   jftDectectOff1.setText("0");

   jftDectectOff2.setText("0");

   jtfDetectBit1.setText("0x0000");

   jtfDetectBit2.setText("0x0000");

   jtfDetectPatt1.setText("0x0000");

   jtfDetectPatt2.setText("0x0000");

   

   jtfRule1Offset.setText("0");

   jtfRule1Bit.setText("0x0000");

   jtfRule1Patt.setText("0x0000");

   jtfRule2Offset.setText("0");

   jtfRule2Bit.setText("0x0000");

   jtfRule2Patt.setText("0x0000");

   jtfRule3Offset.setText("0");

   jtfRule3Bit.setText("0x0000");

   jtfRule3Patt.setText("0x0000");

   jtfRule4Offset.setText("0");

   jtfRule4Bit.setText("0x0000");

   jtfRule4Patt.setText("0x0000");

   jtfRule5Offset.setText("0");

   jtfRule5Bit.setText("0x0000");

   jtfRule5Patt.setText("0x0000");

   jtfRule6Offset.setText("0");

   jtfRule6Bit.setText("0x0000");

   jtfRule6Patt.setText("0x0000");

   jtfRule7Offset.setText("0");

   jtfRule7Bit.setText("0x0000");

   jtfRule7Patt.setText("0x0000");

   jtfRule8Offset.setText("0");

   jtfRule8Bit.setText("0x0000");

   jtfRule8Patt.setText("0x0000");

   

   comboBoxRule1prio.setSelectedItem(sPriorities[0]);

   comboBoxRule2prio.setSelectedItem(sPriorities[0]);

   comboBoxRule3prio.setSelectedItem(sPriorities[0]);

   comboBoxRule4prio.setSelectedItem(sPriorities[0]);

   comboBoxRule5prio.setSelectedItem(sPriorities[0]);

   comboBoxRule6prio.setSelectedItem(sPriorities[0]);

   comboBoxRule7prio.setSelectedItem(sPriorities[0]);

   comboBoxRule8prio.setSelectedItem(sPriorities[0]);

 }



 public void updateCustomFields()

   {

   String stkQoSPktDetect[]=null;

   String stkQoSRules[]=null;

   String stkQoSPriorities[]=null;

   int i = 1;

   int j = 1;



   comboBoxTypeFrame.setSelectedItem(sFrameType[Integer.parseInt(modem.getQoSFrameTypeStored())]);

 //Combo boxes detectors 1 and 2

   stkQoSPktDetect = modem.getQoSPacketDetectorStored();

   //while (stkQoSPktDetect.hasMoreTokens()) {

   for(int n=0;n<stkQoSPktDetect.length;n++){

     String sQoSPktDetect = stkQoSPktDetect[n];//stkQoSPktDetect.nextToken();

     if (sQoSPktDetect.contains("x"))

     {

       sQoSPktDetect = Long.toString(Long.parseLong(sQoSPktDetect.substring(2), 16));

     }

     if (i==1) { //Offset 1    

       jftDectectOff1.setText(sQoSPktDetect);

     } else if (i==2) { //Bitmask 1

       String sDetectBit1 = Long.toHexString(Long.parseLong(sQoSPktDetect)).toUpperCase();

       while (sDetectBit1.length()<4) {

         sDetectBit1="0".concat(sDetectBit1);

       }

       jtfDetectBit1.setText("0x"+sDetectBit1);

     } else if (i==3) { //Pattern 1

       String sDetectPat1 = Long.toHexString(Long.parseLong(sQoSPktDetect)).toUpperCase();

       while (sDetectPat1.length()<4) {

         sDetectPat1="0".concat(sDetectPat1);

       }

       jtfDetectPatt1.setText("0x"+sDetectPat1);

     } else if (i==4) {  //enabled or disabled detector 1

       if (sQoSPktDetect.contentEquals("1"))

       {

         btnPacketDetection1Enable.setSelected(true);

         enablePanelPacketDetection(1, true);

       }

       else

       {

         btnPacketDetection1Enable.setSelected(false);

         enablePanelPacketDetection(1, false);

       }



     } else  if (i==5) { //Offset 2

       jftDectectOff2.setText(sQoSPktDetect);

     } else if (i==6) { //Bitmask 2

       String sDetectBit2 = Long.toHexString(Long.parseLong(sQoSPktDetect)).toUpperCase();

       while (sDetectBit2.length()<4) {

         sDetectBit2="0".concat(sDetectBit2);

       }

       jtfDetectBit2.setText("0x"+sDetectBit2);

     } else if (i==7) { //Pattern 2

       String sDetectPat2 = Long.toHexString(Long.parseLong(sQoSPktDetect)).toUpperCase();

       while (sDetectPat2.length()<4) {

         sDetectPat2="0".concat(sDetectPat2);

       }

       jtfDetectPatt2.setText("0x"+sDetectPat2);

     } else if (i==8) {    

       if (sQoSPktDetect.contentEquals("1"))

       {

         btnPacketDetection2Enable.setSelected(true);

         enablePanelPacketDetection(2, true);

       }

       else

       {

         btnPacketDetection2Enable.setSelected(false);

         enablePanelPacketDetection(2, false);

       }

     } 

     i++;   

   }//end for stkQoSPktDetect



   //Combo box QoS criterion

   stkQoSRules = modem.getQoSClassifierRuleStored();

   //while (stkQoSRules.hasMoreTokens()) {

   for(int n=0;n<stkQoSRules.length;n++){

     String sQoSRules = stkQoSRules[n];//stkQoSRules.nextToken();

     if (sQoSRules.contains("x"))

     {

       sQoSRules = Long.toString(Long.parseLong(sQoSRules.substring(2), 16));

     }

     if (j==1) { //Offset rule 1 

       jtfRule1Offset.setText(sQoSRules);

     } else if (j==2) { //Bitmask rule 1

       String sRule1Bit = Long.toHexString(Long.parseLong(sQoSRules)).toUpperCase();

       while (sRule1Bit.length()<4) {

         sRule1Bit="0".concat(sRule1Bit);

       }

       jtfRule1Bit.setText("0x"+sRule1Bit);

     } else if (j==3) {

       String sRule1Pat = Long.toHexString(Long.parseLong(sQoSRules)).toUpperCase();

       while (sRule1Pat.length()<4) {

         sRule1Pat="0".concat(sRule1Pat);

       }

       jtfRule1Patt.setText("0x"+sRule1Pat);

     } else if (j==5) {

       jtfRule2Offset.setText(sQoSRules);

     } else if (j==6) {

       String sRule2Bit = Long.toHexString(Long.parseLong(sQoSRules)).toUpperCase();

       while (sRule2Bit.length()<4) {

         sRule2Bit="0".concat(sRule2Bit);

       }

       jtfRule2Bit.setText("0x"+sRule2Bit);

     } else if (j==7) {

       String sRule2Pat = Long.toHexString(Long.parseLong(sQoSRules)).toUpperCase();

       while (sRule2Pat.length()<4) {

         sRule2Pat="0".concat(sRule2Pat);

       }

       jtfRule2Patt.setText("0x"+sRule2Pat);



     } else if (j==9) {

       jtfRule3Offset.setText(sQoSRules);

     } else if (j==10) {

       String sRule3Bit = Long.toHexString(Long.parseLong(sQoSRules)).toUpperCase();

       while (sRule3Bit.length()<4) {

         sRule3Bit="0".concat(sRule3Bit);

       }

       jtfRule3Bit.setText("0x"+sRule3Bit);

     } else if (j==11) {

       String sRule3Pat = Long.toHexString(Long.parseLong(sQoSRules)).toUpperCase();

       while (sRule3Pat.length()<4) {

         sRule3Pat="0".concat(sRule3Pat);

       }

       jtfRule3Patt.setText("0x"+sRule3Pat);

     } else if (j==13) {

       jtfRule4Offset.setText(sQoSRules);

     } else if (j==14) {

       String sRule4Bit = Long.toHexString(Long.parseLong(sQoSRules)).toUpperCase();

       while (sRule4Bit.length()<4) {

         sRule4Bit="0".concat(sRule4Bit);

       }

       jtfRule4Bit.setText("0x"+sRule4Bit);   

     } else if (j==15) {

       String sRule4Pat = Long.toHexString(Long.parseLong(sQoSRules)).toUpperCase();

       while (sRule4Pat.length()<4) {

         sRule4Pat="0".concat(sRule4Pat);

       }

       jtfRule4Patt.setText("0x"+sRule4Pat);



     } else if (j==17) {

       jtfRule5Offset.setText(sQoSRules);

     } else if (j==18) {

       String sRule5Bit = Long.toHexString(Long.parseLong(sQoSRules)).toUpperCase();

       while (sRule5Bit.length()<4) {

         sRule5Bit="0".concat(sRule5Bit);

       }

       jtfRule5Bit.setText("0x"+sRule5Bit);

     } else if (j==19) {

       String sRule5Pat = Long.toHexString(Long.parseLong(sQoSRules)).toUpperCase();

       while (sRule5Pat.length()<4) {

         sRule5Pat="0".concat(sRule5Pat);

       }

       jtfRule5Patt.setText("0x"+sRule5Pat);



     } else if (j==21) {

       jtfRule6Offset.setText(sQoSRules);

     } else if (j==22) {

       String sRule6Bit = Long.toHexString(Long.parseLong(sQoSRules)).toUpperCase();

       while (sRule6Bit.length()<4) {

         sRule6Bit="0".concat(sRule6Bit);

       }

       jtfRule6Bit.setText("0x"+sRule6Bit);



     } else if (j==23) {

       String sRule6Pat = Long.toHexString(Long.parseLong(sQoSRules)).toUpperCase();

       while (sRule6Pat.length()<4) {

         sRule6Pat="0".concat(sRule6Pat);

       }

       jtfRule6Patt.setText("0x"+sRule6Pat);

     } else if (j==25) {

       jtfRule7Offset.setText(sQoSRules);

     } else if (j==26) {

       String sRule7Bit = Long.toHexString(Long.parseLong(sQoSRules)).toUpperCase();

       while (sRule7Bit.length()<4) {

         sRule7Bit="0".concat(sRule7Bit);

       }

       jtfRule7Bit.setText("0x"+sRule7Bit);



     } else if (j==27) {

       String sRule7Pat = Long.toHexString(Long.parseLong(sQoSRules)).toUpperCase();

       while (sRule7Pat.length()<4) {

         sRule7Pat="0".concat(sRule7Pat);

       }

       jtfRule7Patt.setText("0x"+sRule7Pat);

     } else if (j==29) {

       jtfRule8Offset.setText(sQoSRules);

     } else if (j==30) {

       String sRule8Bit = Long.toHexString(Long.parseLong(sQoSRules)).toUpperCase();

       while (sRule8Bit.length()<4) {

         sRule8Bit="0".concat(sRule8Bit);

       }

       jtfRule8Bit.setText("0x"+sRule8Bit);

     } else if (j==31) {

       String sRule8Pat = Long.toHexString(Long.parseLong(sQoSRules)).toUpperCase();

       while (sRule8Pat.length()<4) {

         sRule8Pat="0".concat(sRule8Pat);

       }

       jtfRule8Patt.setText("0x"+sRule8Pat);

     }

     j++;   

   }

   }

   public void updatePriorities()

   {

   int k=1;

   //Combo box QoS criterion

     String stkQoSPriorities[];

     

     stkQoSPriorities = modem.getQoSClassifierPriosStored();

     

   //while (stkQoSPriorities.hasMoreTokens()) {

   for(int n=0;n<stkQoSPriorities.length;n++){

     String sQoSPrios = stkQoSPriorities[n];//stkQoSPriorities.nextToken();

     if (k==1) {

       comboBoxRule1prio.setSelectedItem(sPriorities[Integer.parseInt(sQoSPrios)]);

     } else if (k==2) {

       comboBoxRule2prio.setSelectedItem(sPriorities[Integer.parseInt(sQoSPrios)]);

     } else if (k==3) {

       comboBoxRule3prio.setSelectedItem(sPriorities[Integer.parseInt(sQoSPrios)]);

     } else if (k==4) {

       comboBoxRule4prio.setSelectedItem(sPriorities[Integer.parseInt(sQoSPrios)]);

     } else if (k==5) {

       comboBoxRule5prio.setSelectedItem(sPriorities[Integer.parseInt(sQoSPrios)]);

     } else if (k==6) {

       comboBoxRule6prio.setSelectedItem(sPriorities[Integer.parseInt(sQoSPrios)]);

     } else if (k==7) {

       comboBoxRule7prio.setSelectedItem(sPriorities[Integer.parseInt(sQoSPrios)]);

     } else if (k==8) {

       comboBoxRule8prio.setSelectedItem(sPriorities[Integer.parseInt(sQoSPrios)]);

     } 



     k++;   

   }

   }

 @Override

   /**

    * Function to refresh and fill panel to configure QoS of a G.hn node

    * @param selectedModem Selected G.hn node from tree

    * @return void

    */

 public void update(){

   

   // NONE by default

   enableCustomPanel(false);

   setCustomTo0();

   comboBoxCriterion.setSelectedItem(sQoScriterion[0]); 

   

   if (modem.getQoSpresenceStored()) {



     if (modem.getQoSEnabledStored().equals("YES"))

     {

       // CUSTOM

       comboBoxCriterion.setSelectedItem(sQoScriterion[1]);  

       updateCustomFields();

       updatePriorities();

     } 

     else if (modem.get8021pEnabledStored().equals("YES"))

     {

       // VLAN

       comboBoxCriterion.setSelectedItem(sQoScriterion[2]);

       updatePriorities();

     } 

     

     // Update default priority

     comboBoxDefaultPrio.setSelectedItem(sPriorities[Integer.parseInt(modem.getQoSDefaultPrioStored())]);

   }

 }



 public void enablePanelsPacketDetection(boolean enable)

 {

   if (!enable)

   {

     enablePanelPacketDetection(1, false);

     enablePanelPacketDetection(2, false);

   }

   for (int i=0; i<panelCustomPacketDetection1.getComponentCount(); i++)

   {

     panelCustomPacketDetection1.getComponent(i).setEnabled(enable);

   }

   for (int i=0; i<panelCustomPacketDetection2.getComponentCount(); i++)

   {

     panelCustomPacketDetection2.getComponent(i).setEnabled(enable);

   }

   if (enable)

   {

     enablePanelPacketDetection(1, btnPacketDetection1Enable.isSelected());

     enablePanelPacketDetection(2, btnPacketDetection2Enable.isSelected());

   }

 }

 public void enablePanelPacketDetection(int n, boolean enable)

 {

   JPanel panel = panelCustomPacketDetection1;

   if (n == 2)

   {

     panel = panelCustomPacketDetection2;

   }

   for (int i=0; i<panel.getComponentCount(); i++)

   {

     panel.getComponent(i).setEnabled(enable);

   }

   btnPacketDetection1Enable.setEnabled(true);

   btnPacketDetection2Enable.setEnabled(true);



   if (enable)

   {

     if (n == 1)

     {

       btnPacketDetection1Enable.setText("Enabled");

       btnPacketDetection1Enable.setSelected(true);

       btnPacketDetection1Enable.setEnabled(true);

     }

     else

     {

       btnPacketDetection2Enable.setText("Enabled");

       btnPacketDetection2Enable.setSelected(true);

     }

   }

   else

   {

     if (n == 1)

     {

       btnPacketDetection1Enable.setText("Disabled");

       btnPacketDetection1Enable.setSelected(false);

     }

     else

     {

       btnPacketDetection2Enable.setText("Disabled");

       btnPacketDetection2Enable.setSelected(false);

     } 

   }

 }

 

   /**

    * Function to enable prio field texts

    * @return void

    */

   public void enablePanelPriorities(boolean enable)

   {

   for (int i=0; i<panelPriorities.getComponentCount(); i++)

   {

     panelPriorities.getComponent(i).setEnabled(enable);

   }

   }

   

   

   /**

    * Function to disable combo boxes and field texts

    * @return void

    */

   public void enableCustomPanel(boolean enable)

   {

     for (int i=0; i<customPanel.getComponentCount(); i++)

     {

       customPanel.getComponent(i).setEnabled(enable);

     }

     enablePanelPriorities(enable);

     enablePanelsPacketDetection(enable);

   }

}

{
    
}
