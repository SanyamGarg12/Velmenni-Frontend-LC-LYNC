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
 
 import java.awt.Cursor;
 
 import java.awt.Dimension;
 
 import java.awt.Font;
 
 import java.awt.Graphics;
 
 import java.awt.event.ActionEvent;
 
 import java.awt.event.ActionListener;
 
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
 
 
 
 /**
 
  * Class that defines panel to configure Log File of a G.hn node
 
  */
 
 public class JPanelVLAN64Config extends JUpdatablePanel {
 
     private JLabel lblVLANconf;
 
     private JTextField jftEthAVlanTagValue;
 
     private JButton buttonUpdateVLAN;
 
     private JTextField jftEthBVlanTagValue;
 
     private JTextField jftFWVlanTagValue;
 
     private JTextField jftPLCVlanTagValue;
 
     private JTextField jftSDIOVlanTagValue;
 
     private JLabel lblVlanTagConfiguration;
 
     private JLabel lblTrunkModeSDIO;
 
     private JLabel lblTrunkModeETHA;
 
     private JLabel lblTrunkModeETHB;
 
     private JLabel lblTrunkModeFW;
 
     private JLabel lblTrunkModePLC;
 
     private JComboBox comboBoxSDIO;
 
     private JComboBox comboBoxETHA;
 
     private JComboBox comboBoxETHB;
 
     private JComboBox comboBoxFW;
 
     private JComboBox comboBoxPLC;
 
     private JButton buttonUpdateTrunk;
 
     private	String [] sTrunkPortEnable = {"YES","NO"};
 
     private JButton btnDisable;
 
     private JLabel lblDisableVlanFeature;
 
   private SctDevice modem;
 
 
 
     /**
 
      * Constructor of the panel to configure Log File of a G.hn node
 
      * @param modSelectedModem Selected G.hn node from tree
 
      */
 
     public JPanelVLAN64Config(SctDevice modSelectedModem) {
 
     
 
             setPreferredSize(new Dimension(900, 440));
 
             
 
             modem = modSelectedModem;
 
             
 
             lblVLANconf = new JLabel("VLAN tag configuration: adding/removing tag");
 
             lblVLANconf.setBounds(34, 27, 334, 15);
 
             lblVLANconf.setName("lblVLANconf");
 
             lblVLANconf.setForeground(Color.BLUE);
 
             lblVLANconf.setFont(new Font("Tahoma", Font.BOLD, 12));
 
             lblVLANconf.setHorizontalAlignment(SwingConstants.LEFT);
 
             
 
             JLabel lblEthAVlanTag = new JLabel("ETHA VLAN tag:");
 
             lblEthAVlanTag.setBounds(34, 53, 155, 14);
 
             lblEthAVlanTag.setName("lblEthAVlanTag");
 
             lblEthAVlanTag.setFont(new Font("Tahoma", Font.BOLD, 11));
 
             
 
             jftEthAVlanTagValue = new JTextField();
 
             jftEthAVlanTagValue.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             jftEthAVlanTagValue.setBounds(188, 50, 57, 20);
 
             jftEthAVlanTagValue.setName("jftEthAVlanTagValue");
 
             jftEthAVlanTagValue.setToolTipText("Add a tag from 1 to 4095. Set 0 for no tagging");
 
             jftEthAVlanTagValue.setColumns(20);
 
 
 
             JLabel lblEthBVlanTag = new JLabel("ETHB VLAN tag:");
 
             lblEthBVlanTag.setBounds(34, 78, 155, 14);
 
             lblEthBVlanTag.setName("lblEthBVlanTag");
 
             lblEthBVlanTag.setFont(new Font("Tahoma", Font.BOLD, 11));
 
             
 
             jftEthBVlanTagValue = new JTextField();
 
             jftEthBVlanTagValue.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             jftEthBVlanTagValue.setBounds(188, 75, 57, 20);
 
             jftEthBVlanTagValue.setName("jftEthBVlanTagValue");
 
             jftEthBVlanTagValue.setToolTipText("Add a tag from 1 to 4095. Set 0 for no tagging");
 
             jftEthBVlanTagValue.setColumns(20);
 
             
 
             JLabel lblFwVlanTag = new JLabel("Mngt VLAN tag:");
 
             lblFwVlanTag.setBounds(34, 103, 155, 14);
 
             lblFwVlanTag.setName("lblFwVlanTag");
 
             lblFwVlanTag.setFont(new Font("Tahoma", Font.BOLD, 11));
 
             
 
             jftFWVlanTagValue = new JTextField();
 
             jftFWVlanTagValue.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             jftFWVlanTagValue.setBounds(188, 100, 57, 20);
 
             jftFWVlanTagValue.setName("jftFWVlanTagValue");
 
             jftFWVlanTagValue.setToolTipText("Add a tag from 1 to 4095. Set 0 for no tagging");
 
             jftFWVlanTagValue.setColumns(20);
 
             
 
             JLabel lblPlcVlanTag = new JLabel("G.hn VLAN tag:");
 
             lblPlcVlanTag.setBounds(34, 128, 155, 14);
 
             lblPlcVlanTag.setName("lblPlcVlanTag");
 
             lblPlcVlanTag.setFont(new Font("Tahoma", Font.BOLD, 11));
 
             
 
             jftPLCVlanTagValue = new JTextField();
 
             jftPLCVlanTagValue.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             jftPLCVlanTagValue.setBounds(188, 125, 57, 20);
 
             jftPLCVlanTagValue.setName("jftPLCVlanTagValue");
 
             jftPLCVlanTagValue.setToolTipText("Add a tag from 1 to 4095. Set 0 for no tagging");
 
             jftPLCVlanTagValue.setColumns(20);
 
             
 
             JLabel lblSdioVlanTag = new JLabel("SDIO VLAN tag:");
 
             lblSdioVlanTag.setBounds(34, 153, 155, 14);
 
             lblSdioVlanTag.setName("lblSdioVlanTag");
 
             lblSdioVlanTag.setFont(new Font("Tahoma", Font.BOLD, 11));
 
             
 
             jftSDIOVlanTagValue = new JTextField();
 
             jftSDIOVlanTagValue.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             jftSDIOVlanTagValue.setBounds(188, 150, 57, 20);
 
             jftSDIOVlanTagValue.setName("jftSDIOVlanTagValue");
 
             jftSDIOVlanTagValue.setToolTipText("Add a tag from 1 to 4095. Set 0 for no tagging");
 
             jftSDIOVlanTagValue.setColumns(20);
 
             
 
             buttonUpdateVLAN = new JButton("Update");
 
             buttonUpdateVLAN.setFont(new Font("Tahoma", Font.PLAIN, 12));
 
             buttonUpdateVLAN.setBounds(302, 99, 99, 23);
 
             buttonUpdateVLAN.setName("buttonUpdateVLAN");
 
             buttonUpdateVLAN.addActionListener(new ActionListener() {
 
                 public void actionPerformed(ActionEvent arg0) {
 
                         if ((isInteger.check(jftEthAVlanTagValue.getText()))&&(isInteger.check(jftEthBVlanTagValue.getText()))&&
 
                             (isInteger.check(jftFWVlanTagValue.getText()))&&(isInteger.check(jftPLCVlanTagValue.getText()))&&
 
                             (isInteger.check(jftSDIOVlanTagValue.getText()))){
 
 
 
                             modem.setCVLAN("YES");
 
                             modem.setCVLANtag(jftEthAVlanTagValue.getText(),jftEthBVlanTagValue.getText(),jftFWVlanTagValue.getText(),jftPLCVlanTagValue.getText(),jftSDIOVlanTagValue.getText());
 
                         }
 
                 }
 
             });
 
             
 
             
 
             
 
             lblVlanTagConfiguration = new JLabel("VLAN tag configuration: trunk ports");
 
             lblVlanTagConfiguration.setBounds(34, 190, 445, 15);
 
             lblVlanTagConfiguration.setName("lblVlanTagConfiguration");
 
             lblVlanTagConfiguration.setHorizontalAlignment(SwingConstants.LEFT);
 
             lblVlanTagConfiguration.setForeground(Color.BLUE);
 
             lblVlanTagConfiguration.setFont(new Font("Tahoma", Font.BOLD, 12));
 
             
 
             lblTrunkModeSDIO = new JLabel("Set trunk mode to SDIO port:");
 
             lblTrunkModeSDIO.setBounds(34, 217, 203, 14);
 
             lblTrunkModeSDIO.setName("lblTrunkModeSDIO");
 
             lblTrunkModeSDIO.setFont(new Font("Tahoma", Font.BOLD, 11));
 
             
 
             lblTrunkModeETHA = new JLabel("Set trunk mode to ETHA port:");
 
             lblTrunkModeETHA.setBounds(34, 242, 203, 14);
 
             lblTrunkModeETHA.setName("lblTrunkModeETHA");
 
             lblTrunkModeETHA.setFont(new Font("Tahoma", Font.BOLD, 11));
 
             
 
             lblTrunkModeETHB = new JLabel("Set trunk mode to ETHB port:");
 
             lblTrunkModeETHB.setBounds(34, 267, 203, 14);
 
             lblTrunkModeETHB.setName("lblTrunkModeETHB");
 
             lblTrunkModeETHB.setFont(new Font("Tahoma", Font.BOLD, 11));
 
             
 
             lblTrunkModeFW = new JLabel("Set trunk mode to Mngt port:");
 
             lblTrunkModeFW.setBounds(34, 292, 203, 14);
 
             lblTrunkModeFW.setName("lblTrunkModeFW");
 
             lblTrunkModeFW.setFont(new Font("Tahoma", Font.BOLD, 11));
 
             
 
             lblTrunkModePLC = new JLabel("Set trunk mode to all G.hn ports:");
 
             lblTrunkModePLC.setBounds(34, 317, 211, 14);
 
             lblTrunkModePLC.setName("lblTrunkModePLC");
 
             lblTrunkModePLC.setFont(new Font("Tahoma", Font.BOLD, 11));
 
             
 
             comboBoxSDIO = new JComboBox(sTrunkPortEnable);
 
             comboBoxSDIO.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             comboBoxSDIO.setBounds(247, 214, 70, 20);
 
             comboBoxSDIO.setName("comboBoxSDIO");
 
             
 
             comboBoxETHA = new JComboBox(sTrunkPortEnable);
 
             comboBoxETHA.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             comboBoxETHA.setBounds(247, 239, 70, 20);
 
             comboBoxETHA.setName("comboBoxETHA");
 
             
 
             comboBoxETHB = new JComboBox(sTrunkPortEnable);
 
             comboBoxETHB.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             comboBoxETHB.setBounds(247, 264, 70, 20);
 
             comboBoxETHB.setName("comboBoxETHB");
 
             
 
             comboBoxFW = new JComboBox(sTrunkPortEnable);
 
             comboBoxFW.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             comboBoxFW.setBounds(247, 289, 70, 20);
 
             comboBoxFW.setName("comboBoxFW");
 
             
 
             comboBoxPLC = new JComboBox(sTrunkPortEnable);
 
             comboBoxPLC.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             comboBoxPLC.setBounds(247, 314, 70, 20);
 
             comboBoxPLC.setName("comboBoxPLC");
 
             
 
             buttonUpdateTrunk = new JButton("Update");
 
             buttonUpdateTrunk.setFont(new Font("Tahoma", Font.PLAIN, 12));
 
             buttonUpdateTrunk.setBounds(345, 263, 99, 23);
 
             buttonUpdateTrunk.setName("buttonUpdateTrunk");
 
             buttonUpdateTrunk.addActionListener(new ActionListener() {
 
                 public void actionPerformed(ActionEvent arg0) {
 
                     modem.setCVLAN("YES");
 
                         if (comboBoxSDIO.getSelectedItem().equals("YES")){
 
                           modem.setCVLANtrunkSDIO("YES");
 
                             //modem.setCVLANtrunkSDIO("YES");
 
                         }else{
 
                             //modem.setCVLANtrunkSDIO("NO");
 
                           modem.setCVLANtrunkSDIO("NO");
 
                         }
 
                         if (comboBoxETHA.getSelectedItem().equals("YES")){
 
                             //modem.setCVLANtrunkETHA("YES");
 
                           modem.setCVLANtrunkETHA("YES");
 
                         }else{
 
                             //modem.setCVLANtrunkETHA("NO");
 
                           modem.setCVLANtrunkETHA("NO");
 
                         }
 
                         if (comboBoxETHB.getSelectedItem().equals("YES")){
 
                             //modem.setCVLANtrunkETHB("YES");
 
                           modem.setCVLANtrunkETHB("YES");
 
                         }else{
 
                             //modem.setCVLANtrunkETHB("NO");
 
                           modem.setCVLANtrunkETHB("NO");
 
                         }
 
                         if (comboBoxFW.getSelectedItem().equals("YES")){
 
                             //modem.setCVLANtrunkFW("YES");
 
                           modem.setCVLANtrunkFW("YES");
 
                         }else{
 
                             //modem.setCVLANtrunkFW("NO");
 
                           modem.setCVLANtrunkFW("NO");
 
                         }
 
                         if (comboBoxPLC.getSelectedItem().equals("YES")){
 
                             //modem.setCVLANtrunkPLC("YES");
 
                           modem.setCVLANtrunkPLC("YES");
 
                             
 
                         }else{
 
                             //modem.setCVLANtrunkPLC("NO");
 
                           modem.setCVLANtrunkPLC("NO");
 
                         }						
 
                 }
 
             });
 
             
 
             btnDisable = new JButton("Disable");
 
             btnDisable.setFont(new Font("Tahoma", Font.PLAIN, 12));
 
             btnDisable.setBounds(34, 384, 105, 23);
 
             btnDisable.setName("btnDisable");
 
             btnDisable.addActionListener(new ActionListener() {
 
               public void actionPerformed(ActionEvent arg0) {
 
                 modem.setCVLAN("NO");
 
                 modem.setCVLANtrunkSDIO("YES");
 
                 modem.setCVLANtrunkETHA("YES");
 
                 modem.setCVLANtrunkETHB("YES");
 
                 modem.setCVLANtrunkFW("YES");
 
                 modem.setCVLANtrunkPLC("YES");
 
                 modem.setCVLANtag("0","0","0","0","0");
 
               }
 
             });
 
             setLayout(null);
 
             
 
             lblDisableVlanFeature = new JLabel("Disable VLAN feature and all settings back to defaults");
 
             lblDisableVlanFeature.setBounds(34, 358, 387, 15);
 
             lblDisableVlanFeature.setName("lblDisableVlanFeature");
 
             lblDisableVlanFeature.setHorizontalAlignment(SwingConstants.LEFT);
 
             lblDisableVlanFeature.setForeground(Color.BLUE);
 
             lblDisableVlanFeature.setFont(new Font("Tahoma", Font.BOLD, 12));
 
             add(lblDisableVlanFeature);
 
             add(btnDisable);
 
             add(lblVlanTagConfiguration);
 
             add(lblVLANconf);
 
             add(lblSdioVlanTag);
 
             add(jftSDIOVlanTagValue);
 
             add(lblPlcVlanTag);
 
             add(jftPLCVlanTagValue);
 
             add(lblEthAVlanTag);
 
             add(jftEthAVlanTagValue);
 
             add(lblEthBVlanTag);
 
             add(jftEthBVlanTagValue);
 
             add(lblFwVlanTag);
 
             add(jftFWVlanTagValue);
 
             add(lblTrunkModeSDIO);
 
             add(lblTrunkModeETHA);
 
             add(lblTrunkModeETHB);
 
             add(lblTrunkModeFW);
 
             add(lblTrunkModePLC);
 
             add(comboBoxPLC);
 
             add(comboBoxETHA);
 
             add(comboBoxETHB);
 
             add(comboBoxFW);
 
             add(comboBoxSDIO);
 
             add(buttonUpdateVLAN);
 
             add(buttonUpdateTrunk);
 
 
 
         }
 
         
 
   @Override
 
     /**
 
      * Function to refresh and fill panel to configure Log File of a G.hn node
 
      * @param selectedModem Selected G.hn node from tree
 
      * @return void
 
      */
 
     public void update()	{
 
         //StringTokenizer stkVLANtrunk;
 
         String[] stkVLANtrunk;
 
         int i = 1;
 
         if (modem.getCVLANEthAValueStored() != null) {
 
 
 
             jftEthAVlanTagValue.setText(modem.getCVLANEthAValueStored());
 
             jftEthBVlanTagValue.setText(modem.getCVLANEthBValueStored());
 
             jftFWVlanTagValue.setText(modem.getCVLANFWValueStored());
 
             jftPLCVlanTagValue.setText(modem.getCVLANPLCValueStored());
 
             jftSDIOVlanTagValue.setText(modem.getCVLANSDIOValueStored());
 
             
 
             stkVLANtrunk = modem.getCVLANTrunkMode();
 
             
 
             //while (stkVLANtrunk.hasMoreTokens()) {
 
             if (stkVLANtrunk != null)
 
             {
 
               for(int j=0;j<stkVLANtrunk.length;j++){
 
                 String sVLANtrunkValue = stkVLANtrunk[j];//stkVLANtrunk.nextToken();
 
                 if (i==1) {
 
                   if (sVLANtrunkValue.contains("YES")) comboBoxSDIO.setSelectedItem(sTrunkPortEnable[0]);
 
                   else comboBoxSDIO.setSelectedItem(sTrunkPortEnable[1]);
 
                 } else if (i==2) {
 
                   if (sVLANtrunkValue.contains("YES")) comboBoxETHA.setSelectedItem(sTrunkPortEnable[0]);
 
                   else comboBoxETHA.setSelectedItem(sTrunkPortEnable[1]);
 
                 } else if (i==3) {
 
                   if (sVLANtrunkValue.contains("YES")) comboBoxETHB.setSelectedItem(sTrunkPortEnable[0]);
 
                   else comboBoxETHB.setSelectedItem(sTrunkPortEnable[1]);
 
                 } else if (i==4) {
 
                   if (sVLANtrunkValue.contains("YES")) comboBoxFW.setSelectedItem(sTrunkPortEnable[0]);
 
                   else comboBoxFW.setSelectedItem(sTrunkPortEnable[1]);
 
                 } else if (i==7) {
 
                   if (sVLANtrunkValue.contains("YES")) comboBoxPLC.setSelectedItem(sTrunkPortEnable[0]);
 
                   else comboBoxPLC.setSelectedItem(sTrunkPortEnable[1]);
 
                 } 
 
                 i++;	 
 
               }
 
             }
 
         }
 
     }
 
 
 
   public boolean isSupported() {
 
     return (modem.getCVLANEthAValueStored()!=null?true:false);
 
   }
 
 }
 
 