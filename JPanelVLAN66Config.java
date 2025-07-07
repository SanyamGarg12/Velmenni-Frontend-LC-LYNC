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
 
 import java.util.ArrayList;
 
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
 
 import javax.swing.DefaultComboBoxModel;
 
 import javax.swing.border.BevelBorder;
 
 import javax.swing.border.EtchedBorder;
 
 
 
 import main.InitApp;
 
 import javax.swing.JLayeredPane;
 
 import java.awt.BorderLayout;
 
 import javax.swing.border.LineBorder;
 
 import javax.swing.JTabbedPane;
 
 
 
 /**
 
  * Class that defines panel to configure Log File of a G.hn node
 
  */
 
 public class JPanelVLAN66Config extends JUpdatablePanel {
 
     private JLabel lblVLANconf;
 
   private SctDevice modem;
 
   private ArrayList<Integer> allowedTagsEthA;
 
   private ArrayList<Integer> allowedTagsEthB;
 
   private ArrayList<Integer> allowedTagsFW;
 
   private JPanel panelOption0, panelOption1, panelOption2;
 
   private JTextField tfAPMgmt;
 
   private JTextField tfAPETHA;
 
   private JTextField tfAPETHB;
 
   private JTextField tfTMMgmt;
 
   private JTextField tfAllowedEthA;
 
   private JTextField tfAllowedEthB;
 
   private JTextField textCurrentMode;
 
   private JTabbedPane tabbedPane;
 
   
 
     /**
 
      * Constructor of the panel to configure Log File of a G.hn node
 
      * @param modSelectedModem Selected G.hn node from tree
 
      */
 
     public JPanelVLAN66Config(SctDevice modSelectedModem) {
 
     
 
             setPreferredSize(new Dimension(900, 449));
 
             
 
             modem = modSelectedModem;
 
                         
 
             lblVLANconf = new JLabel("VLAN Configuration");
 
             lblVLANconf.setBounds(34, 27, 334, 15);
 
             lblVLANconf.setName("lblVLANconf");
 
             lblVLANconf.setForeground(Color.BLUE);
 
             lblVLANconf.setFont(new Font("Tahoma", Font.BOLD, 12));
 
             lblVLANconf.setHorizontalAlignment(SwingConstants.LEFT);
 
 
 
             JLabel lblEthBVlanTag = new JLabel("Select the tab with the desired VLAN mode:");
 
             lblEthBVlanTag.setBounds(34, 53, 350, 14);
 
             lblEthBVlanTag.setName("");
 
             lblEthBVlanTag.setFont(new Font("Tahoma", Font.BOLD, 11));
 
             setLayout(null);
 
             add(lblVLANconf);
 
             add(lblEthBVlanTag);
 
             
 
             tabbedPane = new JTabbedPane(JTabbedPane.TOP);
 
             tabbedPane.setBounds(33, 78, 611, 349);
 
             add(tabbedPane);
 
             panelOption0 = new JPanel();
 
             tabbedPane.addTab("Transparent", null, panelOption0, null);
 
             panelOption0.setLayout(null);
 
             JButton btnSetMode0 = new JButton("Set Transparent Mode");
 
             btnSetMode0.addActionListener(new ActionListener() {
 
               public void actionPerformed(ActionEvent arg0) {
 
                 modem.setCVLANTransparentMode();
 
                 modem.refresh();
 
           update();
 
               }
 
             });
 
             btnSetMode0.setBounds(10, 97, 240, 23);
 
             panelOption0.add(btnSetMode0);
 
             btnSetMode0.setFont(new Font("Tahoma", Font.PLAIN, 12));
 
             
 
             JLabel lblInThisMode = new JLabel("<html>In this mode, VLAN tagging and filtering is disabled.<br>\r\nThe node is <b>transparent</b> to any tagged frame.");
 
             lblInThisMode.setBounds(10, 11, 586, 75);
 
             lblInThisMode.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             panelOption0.add(lblInThisMode);
 
             
 
             panelOption1 = new JPanel();
 
             tabbedPane.addTab("Access Port", null, panelOption1, null);
 
             panelOption1.setLayout(null);
 
             
 
             JLabel label_13 = new JLabel("This node");
 
             label_13.setHorizontalAlignment(SwingConstants.CENTER);
 
             label_13.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             label_13.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
 
             label_13.setBounds(51, 87, 76, 52);
 
             panelOption1.add(label_13);
 
             
 
             JButton btnSetAccessPort = new JButton("Set Access Port Mode");
 
             btnSetAccessPort.addActionListener(new ActionListener() {
 
               public void actionPerformed(ActionEvent e) {
 
                 if (checkValue(tfAPMgmt, 1) && checkValue(tfAPETHA, 1) && checkValue(tfAPETHB, 1))
 
                 {
 
                   modem.setCVLANAccessPortMode(tfAPMgmt.getText(), tfAPETHA.getText(), tfAPETHB.getText());
 
                 modem.refresh();
 
             update();
 
                 }
 
               }
 
             });
 
             btnSetAccessPort.setFont(new Font("Tahoma", Font.PLAIN, 12));
 
             btnSetAccessPort.setBounds(10, 287, 240, 23);
 
             panelOption1.add(btnSetAccessPort);
 
             
 
             JLabel lblinThisMode = new JLabel("<html>In this mode, ETH A and ETH B are Access Ports. ETH B is optional.<br>\r\nVLAN tagging and filtering is enabled.<br>\r\nThe node can only be configured from G.hn using the Management VLAN.<br>\r\nTraffic in the Ethernet ports is tagged in ingress and untagged in egress.");
 
             lblinThisMode.setBounds(10, 199, 586, 89);
 
             lblinThisMode.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             panelOption1.add(lblinThisMode);
 
             
 
             tfAPMgmt = new JTextField();
 
             tfAPMgmt.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             tfAPMgmt.setBounds(429, 10, 86, 20);
 
             panelOption1.add(tfAPMgmt);
 
             tfAPMgmt.setColumns(10);
 
             
 
             tfAPETHA = new JTextField();
 
             tfAPETHA.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             tfAPETHA.setColumns(10);
 
             tfAPETHA.setBounds(429, 35, 86, 20);
 
             panelOption1.add(tfAPETHA);
 
             
 
             tfAPETHB = new JTextField();
 
             tfAPETHB.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             tfAPETHB.setColumns(10);
 
             tfAPETHB.setBounds(429, 60, 86, 20);
 
             panelOption1.add(tfAPETHB);
 
             
 
             JLabel lblValidTagsAre = new JLabel("Valid tags are 2-4094");
 
             lblValidTagsAre.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             lblValidTagsAre.setBounds(429, 91, 139, 14);
 
             panelOption1.add(lblValidTagsAre);
 
             
 
             JLabel label_9 = new JLabel("G.hn");
 
             label_9.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             label_9.setBounds(51, 38, 76, 14);
 
             panelOption1.add(label_9);
 
             
 
             JPanel panel_1 = new JPanel();
 
             panel_1.setBorder(new LineBorder(new Color(0, 0, 0), 2));
 
             panel_1.setBounds(87, 11, 4, 78);
 
             panelOption1.add(panel_1);
 
             
 
             JLabel label_10 = new JLabel("Management VLAN (used for remote configuration of this node)");
 
             label_10.setForeground(new Color(128, 0, 0));
 
             label_10.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             label_10.setBounds(101, 13, 324, 14);
 
             panelOption1.add(label_10);
 
             
 
             JLabel label_11 = new JLabel("Data VLAN A (connected to Ethernet A)");
 
             label_11.setForeground(new Color(0, 128, 0));
 
             label_11.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             label_11.setBounds(101, 38, 324, 14);
 
             panelOption1.add(label_11);
 
             
 
             JLabel lbldataVlanB = new JLabel("Data VLAN B (connected to Ethernet B) ");
 
             lbldataVlanB.setForeground(new Color(75, 0, 130));
 
             lbldataVlanB.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             lbldataVlanB.setBounds(101, 63, 324, 14);
 
             panelOption1.add(lbldataVlanB);
 
             
 
             JLabel lblDataB = new JLabel("Data B");
 
             lblDataB.setHorizontalTextPosition(SwingConstants.CENTER);
 
             lblDataB.setHorizontalAlignment(SwingConstants.CENTER);
 
             lblDataB.setForeground(new Color(75, 0, 130));
 
             lblDataB.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             lblDataB.setBounds(10, 151, 61, 14);
 
             panelOption1.add(lblDataB);
 
             
 
             JLabel lblEthB = new JLabel("ETH B");
 
             lblEthB.setHorizontalTextPosition(SwingConstants.CENTER);
 
             lblEthB.setHorizontalAlignment(SwingConstants.CENTER);
 
             lblEthB.setForeground(Color.BLACK);
 
             lblEthB.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             lblEthB.setBounds(10, 176, 61, 14);
 
             panelOption1.add(lblEthB);
 
             
 
             JLabel lblDataBa = new JLabel("Data A");
 
             lblDataBa.setHorizontalTextPosition(SwingConstants.CENTER);
 
             lblDataBa.setHorizontalAlignment(SwingConstants.CENTER);
 
             lblDataBa.setForeground(new Color(0, 128, 0));
 
             lblDataBa.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             lblDataBa.setBounds(103, 151, 61, 14);
 
             panelOption1.add(lblDataBa);
 
             
 
             JLabel lblEthA = new JLabel("ETH A");
 
             lblEthA.setHorizontalTextPosition(SwingConstants.CENTER);
 
             lblEthA.setHorizontalAlignment(SwingConstants.CENTER);
 
             lblEthA.setForeground(Color.BLACK);
 
             lblEthA.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             lblEthA.setBounds(103, 176, 61, 14);
 
             panelOption1.add(lblEthA);
 
             
 
             JPanel panel_2 = new JPanel();
 
             panel_2.setBorder(new LineBorder(new Color(0, 0, 0), 2));
 
             panel_2.setBounds(101, 139, 4, 52);
 
             panelOption1.add(panel_2);
 
             
 
             JPanel panel_3 = new JPanel();
 
             panel_3.setBorder(new LineBorder(new Color(0, 0, 0), 2));
 
             panel_3.setBounds(71, 139, 4, 52);
 
             panelOption1.add(panel_3);
 
             
 
             panelOption2 = new JPanel();
 
             tabbedPane.addTab("Trunk Port", null, panelOption2, null);
 
             panelOption2.setLayout(null);
 
             
 
             JLabel lblinThisMode_1 = new JLabel("<html>In this mode, ETH A and ETH B are Trunk Ports. ETH B is optional.\r\nVLAN filtering is enabled but tagging is disabled.<br>\r\nA list of allowed tags can be configured in the ETH A and ETH B.<br>\r\nNote: Add the Management VLAN to the allowed tags to allow management from that port.");
 
             lblinThisMode_1.setBounds(10, 199, 586, 87);
 
             lblinThisMode_1.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             panelOption2.add(lblinThisMode_1);
 
             
 
             JButton btnSetTrunkPort = new JButton("Set Trunk Port Mode");
 
             btnSetTrunkPort.addActionListener(new ActionListener() {
 
               public void actionPerformed(ActionEvent e) {
 
              if (checkValue(tfTMMgmt, 1) && checkValue(tfAllowedEthA, 16) && checkValue(tfAllowedEthB, 16))
 
               {
 
                 String allowedMgmt = createAllowedMgmt(tfTMMgmt.getText(), tfAllowedEthA.getText(), tfAllowedEthB.getText(), 16);
 
                 String allowedEthA = fillWith0(tfAllowedEthA.getText(), 16);
 
                 String allowedEthB = fillWith0(tfAllowedEthB.getText(), 16);
 
                 if (allowedMgmt == null)
 
                 {
 
                   JOptionPane.showMessageDialog(InitApp.top, "Invalid configuration!\nThe total number of different tags cannot be more than 16.", "Spirit Config Tool Error", JOptionPane.ERROR_MESSAGE);
 
                 }
 
                 else
 
                 {
 
                   modem.setCVLANTrunkPortMode(tfTMMgmt.getText(), allowedMgmt, allowedEthA, allowedEthB);
 
                   modem.refresh();
 
                   update();
 
                 }
 
               }
 
 
 
               }
 
             });
 
             btnSetTrunkPort.setFont(new Font("Tahoma", Font.PLAIN, 12));
 
             btnSetTrunkPort.setBounds(10, 287, 240, 23);
 
             panelOption2.add(btnSetTrunkPort);
 
             
 
             JPanel panel = new JPanel();
 
             panel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
 
             panel.setBounds(87, 11, 4, 52);
 
             panelOption2.add(panel);
 
             
 
             JLabel label_3 = new JLabel("G.hn");
 
             label_3.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             label_3.setBounds(51, 38, 76, 14);
 
             panelOption2.add(label_3);
 
             
 
             JLabel label_4 = new JLabel("This node");
 
             label_4.setHorizontalAlignment(SwingConstants.CENTER);
 
             label_4.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             label_4.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
 
             label_4.setBounds(51, 63, 76, 52);
 
             panelOption2.add(label_4);
 
             
 
             JLabel lblAllowedTagsIn = new JLabel("Allowed tags in ETH A:");
 
             lblAllowedTagsIn.setForeground(new Color(0, 128, 0));
 
             lblAllowedTagsIn.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             lblAllowedTagsIn.setBounds(137, 140, 133, 14);
 
             panelOption2.add(lblAllowedTagsIn);
 
             
 
             JLabel label_6 = new JLabel("ETH A");
 
             label_6.setHorizontalTextPosition(SwingConstants.CENTER);
 
             label_6.setHorizontalAlignment(SwingConstants.CENTER);
 
             label_6.setForeground(Color.BLACK);
 
             label_6.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             label_6.setBounds(109, 115, 61, 14);
 
             panelOption2.add(label_6);
 
             
 
             JPanel panel_4 = new JPanel();
 
             panel_4.setBorder(new LineBorder(new Color(0, 0, 0), 2));
 
             panel_4.setBounds(71, 115, 4, 76);
 
             panelOption2.add(panel_4);
 
             
 
             JPanel panel_5 = new JPanel();
 
             panel_5.setBorder(new LineBorder(new Color(0, 0, 0), 2));
 
             panel_5.setBounds(101, 115, 4, 23);
 
             panelOption2.add(panel_5);
 
             
 
             JLabel label_7 = new JLabel("Allowed tags in ETH B:");
 
             label_7.setForeground(new Color(75, 0, 130));
 
             label_7.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             label_7.setBounds(137, 174, 133, 14);
 
             panelOption2.add(label_7);
 
             
 
             JLabel label_8 = new JLabel("ETH B");
 
             label_8.setHorizontalTextPosition(SwingConstants.CENTER);
 
             label_8.setHorizontalAlignment(SwingConstants.CENTER);
 
             label_8.setForeground(Color.BLACK);
 
             label_8.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             label_8.setBounds(10, 140, 61, 14);
 
             panelOption2.add(label_8);
 
             
 
             JLabel lblManagementVlanused = new JLabel("Management VLAN (used for configuration of this node)");
 
             lblManagementVlanused.setForeground(new Color(128, 0, 0));
 
             lblManagementVlanused.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             lblManagementVlanused.setBounds(137, 82, 297, 14);
 
             panelOption2.add(lblManagementVlanused);
 
             
 
             tfTMMgmt = new JTextField();
 
             tfTMMgmt.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             tfTMMgmt.setColumns(10);
 
             tfTMMgmt.setBounds(437, 79, 86, 20);
 
             panelOption2.add(tfTMMgmt);
 
             
 
             JPanel panel_6 = new JPanel();
 
             panel_6.setBorder(new LineBorder(new Color(0, 0, 0), 2));
 
             panel_6.setBounds(101, 134, 32, 4);
 
             panelOption2.add(panel_6);
 
             
 
             tfAllowedEthA = new JTextField();
 
             tfAllowedEthA.setToolTipText("Add valid tags (2-4094) separated by commas. For example: 5,6,100,120");
 
             tfAllowedEthA.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             tfAllowedEthA.setColumns(10);
 
             tfAllowedEthA.setBounds(280, 137, 243, 20);
 
             panelOption2.add(tfAllowedEthA);
 
             
 
             tfAllowedEthB = new JTextField();
 
             tfAllowedEthB.setToolTipText("Add valid tags (2-4094) separated by commas. For example: 5,6,100,120");
 
             tfAllowedEthB.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             tfAllowedEthB.setColumns(10);
 
             tfAllowedEthB.setBounds(280, 171, 243, 20);
 
             panelOption2.add(tfAllowedEthB);
 
             
 
             JLabel label = new JLabel("Valid tags are 2-4094");
 
             label.setHorizontalAlignment(SwingConstants.RIGHT);
 
             label.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
             label.setBounds(384, 115, 139, 14);
 
             panelOption2.add(label);
 
             
 
             textCurrentMode = new JTextField();
 
             textCurrentMode.setFont(new Font("Tahoma", Font.PLAIN, 12));
 
             textCurrentMode.setEditable(false);
 
             textCurrentMode.setBounds(394, 50, 250, 20);
 
             add(textCurrentMode);
 
             textCurrentMode.setColumns(10);
 
 
 
         }
 
         
 
   protected String fillWith0(String s, int max) {
 
     String set = s;
 
     String[] list = s.split(",");
 
     for (int i = list.length; i < max; i++)
 
     {
 
       if (set == null)
 
       {
 
         set = "0";
 
       }
 
       else
 
       {
 
         set += ",0";
 
       }
 
     }  
 
     return set;  
 
   }
 
   
 
 
 
   private String remove0s(String s) {
 
     String set = null;
 
     String[] list = s.split(",");
 
     for (int i = 0; i < list.length; i++)
 
     {
 
       if (set == null)
 
       {
 
         set = list[i];
 
       }
 
       else if (!list[i].contentEquals("0"))
 
       {
 
         set += ","+list[i];
 
       }
 
     }  
 
     return set;  
 
   }
 
 
 
   protected String createAllowedMgmt(String tag, String list1, String list2, int max) {
 
     ArrayList<String> list = new ArrayList<String>();
 
     list.add(tag);
 
     String[] slist1 = list1.split(",");
 
     String[] slist2 = list2.split(",");
 
     String result = null;
 
     for (int i=0; i < slist1.length; i++)
 
     {
 
       if (!list.contains(slist1[i]))
 
       {
 
         list.add(slist1[i]);
 
       }
 
     }
 
     for (int i=0; i < slist2.length; i++)
 
     {
 
       if (!list.contains(slist2[i]))
 
       {
 
         list.add(slist2[i]);
 
       }
 
     }
 
     if (list.size() > 0 && list.size() <= max)
 
     {
 
       for (int i=list.size(); i < max; i++)
 
       {
 
         list.add("0");
 
       }
 
     }
 
     else
 
     {
 
       return null;
 
     }
 
     for (int i=0; i < list.size(); i++)
 
     {
 
       if (result == null)
 
       {
 
         result = list.get(i);
 
       }
 
       else
 
       {
 
         result += ","+list.get(i);
 
       }
 
     }
 
     return result;
 
   }
 
 
 
   protected boolean checkValue(JTextField textField, int max) {
 
     if (!textField.isEnabled())
 
     {
 
       textField.setText("0");
 
       return true;
 
     }
 
     if (textField.getText() != null)
 
     {
 
       String[] tags = textField.getText().split(",");
 
       if (tags.length <= max)
 
       {
 
         try
 
         {
 
           for (int i = 0; i < tags.length; i++)
 
           {
 
             int v = Integer.parseInt(tags[i]);
 
             if (v < 2 || v > 4094)
 
             {
 
               throw new Exception();
 
             }
 
           }
 
           textField.setBackground(Color.WHITE);
 
           return true;
 
         }
 
         catch (Exception e)
 
         {
 
           e.printStackTrace();
 
         }
 
       }
 
     }
 
     textField.setBackground(Color.RED);
 
     return false;
 
   }
 
 
 
   @Override
 
     /**
 
      * Function to refresh and fill panel to configure Log File of a G.hn node
 
      * @param selectedModem Selected G.hn node from tree
 
      * @return void
 
      */
 
     public void update()	
 
   {  
 
     String sEnable = modem.getCVLANEnableStored();
 
     String sConfigEthA = modem.getCVLANConfigEthAStored();
 
     String sConfigEthB = modem.getCVLANConfigEthBStored();
 
     String sConfigMgmt = modem.getCVLANConfigMgmtStored();
 
     String sFiltering = modem.getCVLANFilteringEnableStored();
 
     
 
     boolean ethB;
 
     if (modem.getEthEnabledStored(1).contentEquals("YES"))
 
     {
 
       tfAPETHB.setEnabled(true);
 
       tfAllowedEthB.setEnabled(true);
 
       ethB = true;
 
     }
 
     else
 
     {
 
       tfAPETHB.setEnabled(false);
 
       tfAllowedEthB.setEnabled(false);
 
       ethB = false;
 
     }
 
     
 
     if (sEnable.contentEquals("NO"))
 
     {
 
       textCurrentMode.setText("Current mode is Transparent");
 
       tabbedPane.setSelectedIndex(0);
 
     }
 
     else if (sFiltering.contentEquals("YES") && sConfigEthA.contentEquals("ACCESS") && (!ethB || sConfigEthB.contentEquals("ACCESS")) && sConfigMgmt.contentEquals("TRUNK"))
 
     {
 
       textCurrentMode.setText("Current mode is Access Port");
 
       tabbedPane.setSelectedIndex(1);
 
       tfAPMgmt.setText(modem.getCVLANPVIDMgmtStored());
 
       tfAPETHA.setText(modem.getCVLANPVIDEthAStored());
 
       tfAPETHB.setText(modem.getCVLANPVIDEthBStored());
 
     }
 
     else if (sFiltering.contentEquals("YES") && sConfigEthA.contentEquals("TRUNK") && modem.getCVLANPVIDEthAStored().contentEquals("0") && 
 
         (!ethB || (sConfigEthB.contentEquals("TRUNK") && (modem.getCVLANPVIDEthBStored().contentEquals("0")))) && 
 
             sConfigMgmt.contentEquals("TRUNK"))
 
     {
 
       textCurrentMode.setText("Current mode is Trunk Port");
 
       tabbedPane.setSelectedIndex(2);
 
       tfTMMgmt.setText(modem.getCVLANPVIDMgmtStored());
 
       tfAllowedEthA.setText(remove0s(modem.getCVLANAllowedEthAStored()));
 
       tfAllowedEthB.setText(remove0s(modem.getCVLANAllowedEthBStored()));
 
     }
 
     else
 
     {
 
       textCurrentMode.setText("Current mode is Unknown");
 
     }
 
 
 
   }
 
 
 
   public boolean isSupported() {
 
     return (modem.getCVLANFilteringEnableStored()!=null?true:false);
 
   }
 
 }
 
 