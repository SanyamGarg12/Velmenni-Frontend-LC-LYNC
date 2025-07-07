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
 
 import java.net.InetAddress;
 
 import java.net.UnknownHostException;
 
 import java.util.StringTokenizer;
 
 
 
 import javax.swing.GroupLayout;
 
 import javax.swing.GroupLayout.Alignment;
 
 import javax.swing.JButton;
 
 import javax.swing.JComboBox;
 
 import javax.swing.JFrame;
 
 import javax.swing.JLabel;
 
 import javax.swing.JOptionPane;
 
 import javax.swing.JPanel;
 
 import javax.swing.JTextField;
 
 import javax.swing.LayoutStyle.ComponentPlacement;
 
 import javax.swing.UIManager;
 
 import javax.swing.border.EtchedBorder;
 
 import javax.swing.border.TitledBorder;
 
 
 
 import main.InitApp;
 
 
 
 import javax.swing.JSeparator;
 
 
 
 import network.SctDevice;
 
 import javax.swing.SwingConstants;
 
 import javax.swing.JCheckBox;
 
 import javax.swing.border.LineBorder;
 
 import java.awt.BorderLayout;
 
 import javax.swing.border.CompoundBorder;
 
 import javax.swing.border.EmptyBorder;
 
 
 
 /**
 
  * Class that defines panel IPv4 Configuration of a G.hn node
 
  */
 
 public class JPanelIpConfig extends JUpdatablePanel {
 
     JComboBox cbIpConFig;
 
     private JLabel lblIpAddress;
 
     private JLabel lblSubnetMask;
 
     private JTextField jtfIpAddress;
 
     private JTextField jtfSubnetMask;
 
     private JLabel lblDefaultGw;
 
     private JTextField jtfDefaultGw;
 
     private JLabel lblDnsIpAddress;
 
     private JTextField jtfDnsIpAddress;
 
     private String sNotAvailable = "N/A";
 
     private JButton btnIPv4Update;
 
     private String [] sIpConfigOptions = {"Fixed", "DHCP"};
 
     private JTextField jtfIpAddress1;
 
     private JTextField jtfSubnetMask1;
 
     private JTextField jtfIpAddress2;
 
     private JTextField jtfSubnetMask2;
 
   private SctDevice modModem;
 
   private JPanel fixedConfiguration;
 
   private JLabel lblAdditionalIpAddresses;
 
   private JCheckBox chckbxAdditonalIp1, chckbxAdditonalIp2;
 
   private JLabel txtClickToCheck;
 
   private JPanel panel;
 
   private JButton btnCheckConnectivity;
 
   
 
     /**
 
      * Constructor of the panel IPv4 Configuration of a G.hn node
 
      * @param modSelectedModem Selected G.hn node from tree
 
      * @param jfFrame 
 
      */
 
     public JPanelIpConfig(SctDevice modSelectedModem) {
 
         modModem = modSelectedModem;
 
         Color clBlue = Color.blue;
 
         setPreferredSize(new Dimension(900, 700));
 
         
 
         String [] sIpv6Options = {"NO", "YES"};
 
         setLayout(null);
 
         
 
         
 
         JLabel lblIpConfig = new JLabel("Type of IP configuration:");
 
         lblIpConfig.setBounds(34, 63, 189, 14);
 
         add(lblIpConfig);
 
         lblIpConfig.setName("lblIpConfig");
 
         lblIpConfig.setFont(new Font("Tahoma", Font.BOLD, 11));
 
         
 
 
 
         cbIpConFig = new JComboBox(sIpConfigOptions);
 
         cbIpConFig.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
         cbIpConFig.addActionListener(new ActionListener() {
 
           public void actionPerformed(ActionEvent e) {
 
             if (cbIpConFig.getSelectedItem().toString().contentEquals("DHCP"))
 
             {
 
               fixedConfigurationEnable(false);
 
             }
 
             else
 
             {
 
               fixedConfigurationEnable(true);
 
             }
 
           }
 
         });
 
         cbIpConFig.setBounds(223, 60, 90, 20);
 
         add(cbIpConFig);
 
         cbIpConFig.setName("cbIpConFig");
 
         
 
         btnIPv4Update = new JButton("Update & Reboot");
 
         btnIPv4Update.setFont(new Font("Tahoma", Font.PLAIN, 12));
 
         btnIPv4Update.setBounds(34, 307, 166, 23);
 
         add(btnIPv4Update);
 
         btnIPv4Update.setName("btnIPv4Update");
 
         
 
         JLabel lblIpvConfiguration = new JLabel("IPv4 Configuration");
 
         lblIpvConfiguration.setName("lblTab");
 
         lblIpvConfiguration.setHorizontalAlignment(SwingConstants.LEFT);
 
         lblIpvConfiguration.setForeground(Color.BLUE);
 
         lblIpvConfiguration.setFont(new Font("Tahoma", Font.BOLD, 12));
 
         lblIpvConfiguration.setBounds(34, 27, 388, 15);
 
         add(lblIpvConfiguration);
 
         
 
         fixedConfiguration = new JPanel();
 
         fixedConfiguration.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
 
         fixedConfiguration.setBounds(34, 88, 573, 93);
 
         add(fixedConfiguration);
 
         fixedConfiguration.setLayout(null);
 
         
 
         lblIpAddress = new JLabel("IP Address:");
 
         lblIpAddress.setBounds(10, 14, 121, 14);
 
         fixedConfiguration.add(lblIpAddress);
 
         lblIpAddress.setName("lblIpAddress");
 
         lblIpAddress.setFont(new Font("Tahoma", Font.BOLD, 11));
 
         
 
         jtfIpAddress = new JTextField();
 
         jtfIpAddress.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
         jtfIpAddress.setBounds(141, 11, 119, 20);
 
         fixedConfiguration.add(jtfIpAddress);
 
         jtfIpAddress.setName("jtfIpAddress");
 
         jtfIpAddress.setColumns(10);
 
         
 
         lblSubnetMask = new JLabel("Subnet Mask:");
 
         lblSubnetMask.setBounds(270, 14, 108, 14);
 
         fixedConfiguration.add(lblSubnetMask);
 
         lblSubnetMask.setName("lblSubnetMask");
 
         lblSubnetMask.setFont(new Font("Tahoma", Font.BOLD, 11));
 
         
 
         jtfSubnetMask = new JTextField();
 
         jtfSubnetMask.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
         jtfSubnetMask.setBounds(388, 11, 119, 20);
 
         fixedConfiguration.add(jtfSubnetMask);
 
         jtfSubnetMask.setName("jtfSubnetMask");
 
         
 
         lblDefaultGw = new JLabel("Default Gateway:");
 
         lblDefaultGw.setBounds(10, 39, 121, 14);
 
         fixedConfiguration.add(lblDefaultGw);
 
         lblDefaultGw.setName("lblDefaultGw");
 
         lblDefaultGw.setFont(new Font("Tahoma", Font.BOLD, 11));
 
         
 
         jtfDefaultGw = new JTextField();
 
         jtfDefaultGw.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
         jtfDefaultGw.setBounds(141, 36, 119, 20);
 
         fixedConfiguration.add(jtfDefaultGw);
 
         jtfDefaultGw.setName("jtfDefaultGw");
 
         jtfDefaultGw.setColumns(10);
 
         
 
         lblDnsIpAddress = new JLabel("DNS IP Address:");
 
         lblDnsIpAddress.setBounds(10, 64, 121, 14);
 
         fixedConfiguration.add(lblDnsIpAddress);
 
         lblDnsIpAddress.setName("lblDnsIpAddress");
 
         lblDnsIpAddress.setFont(new Font("Tahoma", Font.BOLD, 11));
 
         
 
         jtfDnsIpAddress = new JTextField();
 
         jtfDnsIpAddress.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
         jtfDnsIpAddress.setBounds(141, 61, 119, 20);
 
         fixedConfiguration.add(jtfDnsIpAddress);
 
         jtfDnsIpAddress.setName("jtfDnsIpAddress");
 
         jtfDnsIpAddress.setColumns(10);
 
         
 
         jtfIpAddress1 = new JTextField();
 
         jtfIpAddress1.setBounds(175, 217, 119, 20);
 
         add(jtfIpAddress1);
 
         jtfIpAddress1.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
         jtfIpAddress1.setName("jtfIpAddress1");
 
         jtfIpAddress1.setColumns(10);
 
         
 
         JLabel lblSubnetMask1 = new JLabel("Subnet Mask:");
 
         lblSubnetMask1.setBounds(304, 220, 108, 14);
 
         add(lblSubnetMask1);
 
         lblSubnetMask1.setName("lblSubnetMask");
 
         lblSubnetMask1.setFont(new Font("Tahoma", Font.BOLD, 11));
 
         
 
         jtfSubnetMask1 = new JTextField();
 
         jtfSubnetMask1.setBounds(422, 217, 119, 20);
 
         add(jtfSubnetMask1);
 
         jtfSubnetMask1.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
         jtfSubnetMask1.setName("jtfSubnetMask1");
 
         
 
         jtfSubnetMask2 = new JTextField();
 
         jtfSubnetMask2.setBounds(422, 248, 119, 20);
 
         add(jtfSubnetMask2);
 
         jtfSubnetMask2.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
         jtfSubnetMask2.setName("jtfSubnetMask2");
 
         
 
         JLabel lblSubnetMask2 = new JLabel("Subnet Mask:");
 
         lblSubnetMask2.setBounds(304, 251, 108, 14);
 
         add(lblSubnetMask2);
 
         lblSubnetMask2.setName("lblSubnetMask2");
 
         lblSubnetMask2.setFont(new Font("Tahoma", Font.BOLD, 11));
 
         
 
         jtfIpAddress2 = new JTextField();
 
         jtfIpAddress2.setBounds(175, 248, 119, 20);
 
         add(jtfIpAddress2);
 
         jtfIpAddress2.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
         jtfIpAddress2.setName("jtfIpAddress2");
 
         jtfIpAddress2.setColumns(10);
 
         
 
         lblAdditionalIpAddresses = new JLabel("Additional IP addresses:");
 
         lblAdditionalIpAddresses.setName("lblIpConfig");
 
         lblAdditionalIpAddresses.setFont(new Font("Tahoma", Font.BOLD, 11));
 
         lblAdditionalIpAddresses.setBounds(34, 192, 189, 14);
 
         add(lblAdditionalIpAddresses);
 
         
 
         chckbxAdditonalIp1 = new JCheckBox("Additonal IP 1:");
 
         chckbxAdditonalIp1.addActionListener(new ActionListener() {
 
           public void actionPerformed(ActionEvent e) {
 
             setEnableAdditionalIp(1,chckbxAdditonalIp1.isSelected());
 
           }
 
         });
 
         chckbxAdditonalIp1.setFont(new Font("Tahoma", Font.BOLD, 11));
 
         chckbxAdditonalIp1.setBounds(44, 216, 131, 23);
 
         add(chckbxAdditonalIp1);
 
         
 
         chckbxAdditonalIp2 = new JCheckBox("Additonal IP 2:");
 
         chckbxAdditonalIp2.addActionListener(new ActionListener() {
 
           public void actionPerformed(ActionEvent e) {
 
         setEnableAdditionalIp(2,chckbxAdditonalIp2.isSelected());
 
           }
 
         });
 
         chckbxAdditonalIp2.setFont(new Font("Tahoma", Font.BOLD, 11));
 
         chckbxAdditonalIp2.setBounds(44, 247, 131, 23);
 
         add(chckbxAdditonalIp2);
 
         
 
         panel = new JPanel();
 
         panel.setBorder(new CompoundBorder(new LineBorder(new Color(128, 128, 128)), new EmptyBorder(1, 3, 1, 3)));
 
         panel.setBackground(new Color(250, 250, 210));
 
         panel.setBounds(34, 400, 621, 26);
 
         add(panel);
 
         panel.setLayout(new BorderLayout(0, 0));
 
         
 
         btnCheckConnectivity = new JButton("Check IPv4 Connectivity");
 
         panel.add(btnCheckConnectivity, BorderLayout.WEST);
 
         btnCheckConnectivity.addActionListener(new ActionListener() {
 
           public void actionPerformed(ActionEvent arg0) {
 
             btnCheckConnectivity.setEnabled(false);
 
         txtClickToCheck.setText("<html><font color=blue>Checking if IP "+modModem.getIpv4AddressStored()+" is reachable by this computer...");
 
 
 
             // Create a new thread and call ping within the thread. 
 
             Thread t = new Thread(new Runnable(){ 
 
               public void run()
 
               { 
 
                 InetAddress byName;
 
                 try {
 
                   byName = InetAddress.getByName(modModem.getIpv4AddressStored());
 
                   if (byName.isReachable(3000) == true)
 
                   {
 
                     txtClickToCheck.setText("<html><font color=green><b>IP "+modModem.getIpv4AddressStored()+" is reachable by this computer.");
 
                   }
 
                   else
 
                   {
 
                     txtClickToCheck.setText("<html><font color=red><b>IP "+modModem.getIpv4AddressStored()+" is not reachable by this computer.");
 
                   }
 
                 } catch(UnknownHostException e) {
 
                   txtClickToCheck.setText("<html><font color=red><b>IP "+modModem.getIpv4AddressStored()+" is invalid.");
 
                 } catch (Exception e) {
 
                   txtClickToCheck.setText("<html><font color=red><b>Unknown error.");
 
                 }
 
                 btnCheckConnectivity.setEnabled(true);
 
 
 
               } 
 
             }); //End runable/thread t
 
 
 
 
 
             // Start the thread so that the dialog will show. 
 
             t.start(); 
 
           }
 
         });
 
         btnCheckConnectivity.setName("btnIPv4Update");
 
         btnCheckConnectivity.setFont(new Font("Tahoma", Font.PLAIN, 12));
 
         
 
         txtClickToCheck = new JLabel();
 
         txtClickToCheck.setHorizontalAlignment(SwingConstants.CENTER);
 
         panel.add(txtClickToCheck);
 
         txtClickToCheck.setText("Click to check IP connectivity from this computer.");
 
         txtClickToCheck.setName("jtfIpAddress2");
 
         txtClickToCheck.setFont(new Font("Tahoma", Font.PLAIN, 11));
 
         btnIPv4Update.addActionListener(new ActionListener() {
 
             
 
             @Override
 
             public void actionPerformed(ActionEvent arg0) {
 
                 
 
                 String sSelectedStatus = (String) cbIpConFig.getSelectedItem();
 
                 
 
                 if (sSelectedStatus.contains("DHCP")) 
 
                 {
 
                   //DHCP enabled
 
                     modModem.setDhcpIpv4(true);					
 
                 } 
 
                 else 
 
                 {
 
                   //Fixed IP
 
                   if (!checkIp(jtfIpAddress.getText()))
 
                   {
 
                     jtfIpAddress.setBackground(Color.RED);
 
                     return;
 
                   }
 
                   else
 
                   {
 
                     jtfIpAddress.setBackground(Color.WHITE);
 
                   }
 
           if (!checkIp(jtfSubnetMask.getText()))
 
           {
 
             jtfSubnetMask.setBackground(Color.RED);
 
             return;
 
           }
 
           else
 
           {
 
             jtfSubnetMask.setBackground(Color.WHITE);
 
           }
 
           if (!checkIp(jtfDefaultGw.getText()))
 
           {
 
             jtfDefaultGw.setBackground(Color.RED);
 
             return;
 
           }
 
           else
 
           {
 
             jtfDefaultGw.setBackground(Color.WHITE);
 
           }
 
           if (!checkIp(jtfDnsIpAddress.getText()))
 
           {
 
             jtfDnsIpAddress.setBackground(Color.RED);
 
             return;
 
           }
 
           else
 
           {
 
             jtfDnsIpAddress.setBackground(Color.WHITE);
 
           }
 
                   if (!modModem.setIpConfigFixed(jtfIpAddress.getText(),jtfSubnetMask.getText(),jtfDefaultGw.getText(), jtfDnsIpAddress.getText()))
 
                   {
 
                     return;
 
                   }
 
           modModem.setDhcpIpv4(false);          
 
                 }
 
                 if (chckbxAdditonalIp1.isEnabled())
 
                 {
 
                   if (chckbxAdditonalIp1.isSelected())
 
                   {
 
                     if (!checkIp(jtfIpAddress1.getText()))
 
               {
 
                       jtfIpAddress1.setBackground(Color.RED);
 
                 return;
 
               }
 
               else
 
               {
 
                 jtfIpAddress1.setBackground(Color.WHITE);
 
               }
 
                     if (!checkIp(jtfSubnetMask1.getText()))
 
             {
 
                       jtfSubnetMask1.setBackground(Color.RED);
 
               return;
 
             }
 
             else
 
             {
 
               jtfSubnetMask1.setBackground(Color.WHITE);
 
             }
 
             if (!modModem.setAdditionalIp1Config(jtfIpAddress1.getText(),jtfSubnetMask1.getText()))
 
             {
 
               return;
 
             }
 
                   }
 
                   else
 
                   {
 
                     if (!modModem.setAdditionalIp1Config("0.0.0.0", "0.0.0.0"))
 
                     {
 
                       return;
 
                     }
 
                   }
 
                 }
 
         if (chckbxAdditonalIp2.isEnabled())
 
         {
 
           if (chckbxAdditonalIp2.isSelected())
 
           {
 
             if (!checkIp(jtfIpAddress2.getText()))
 
             {
 
               jtfIpAddress2.setBackground(Color.RED);
 
               return;
 
             }
 
             else
 
             {
 
               jtfIpAddress2.setBackground(Color.WHITE);
 
             }
 
             if (!checkIp(jtfSubnetMask2.getText()))
 
             {
 
               jtfSubnetMask2.setBackground(Color.RED);
 
               return;
 
             }
 
             else
 
             {
 
               jtfSubnetMask2.setBackground(Color.WHITE);
 
             }
 
             if (!modModem.setAdditionalIp1Config(jtfIpAddress2.getText(),jtfSubnetMask2.getText()))
 
             {
 
               return;
 
             }
 
           }
 
           else
 
           {
 
             if (!modModem.setAdditionalIp2Config("0.0.0.0", "0.0.0.0"))
 
             {
 
               return;
 
             }
 
           }
 
         }
 
         boolean setResult = modModem.rebootByHWReset();
 
         
 
         if (setResult)
 
         {
 
           WaitScreen.reboot(true);
 
         }
 
             }
 
         });
 
         
 
     }
 
      
 
     protected boolean checkIp(String ip) {
 
     String[] values = ip.split("\\.");
 
     int zeros = 0;
 
     int nozeros = 0;
 
     
 
     if (values.length == 4)
 
     {
 
       try {
 
         for (int i=0; i<4; i++)
 
         {
 
           int v = Integer.parseInt(values[i]);
 
           if (v >= 0 && v<= 255)
 
           {
 
             if (v == 0)
 
             {
 
               zeros++;
 
             }
 
             else
 
             {
 
               nozeros++;
 
             }
 
           }
 
         }
 
         if (zeros < 4)
 
         {
 
           if ((zeros + nozeros) == 4)
 
           {
 
             return true;
 
           }
 
         }
 
       }
 
       catch (Exception e)
 
       {
 
         e.printStackTrace();
 
         // return false
 
       }
 
     }
 
       
 
     return false;
 
   }
 
 
 
   protected void setEnableAdditionalIp(int i, boolean b) {
 
       if (i == 1)
 
       {
 
         jtfIpAddress1.setEnabled(b);
 
         jtfSubnetMask1.setEnabled(b);
 
       }
 
       else if (i == 2)
 
     {
 
       jtfIpAddress2.setEnabled(b);
 
       jtfSubnetMask2.setEnabled(b);
 
     }
 
   }
 
 
 
   private void fixedConfigurationEnable(boolean enable)
 
     {
 
       for (int i=0; i<fixedConfiguration.getComponentCount(); i++)
 
       {
 
         if (fixedConfiguration.getComponent(i).getClass().toString().contains("JTextField"))
 
         {
 
           ((JTextField)fixedConfiguration.getComponent(i)).setEditable(enable);
 
         }
 
       }
 
     }
 
   
 
   @Override
 
   public boolean changesNotAppliedYet()
 
   {
 
     boolean result = false;
 
     
 
     if ((cbIpConFig.getSelectedIndex() == 0) && (modModem.getDHCPIpv4ClientStatusStored().contentEquals("YES")))
 
     {
 
       result = true;
 
     }
 
     else
 
     {
 
       if (!jtfIpAddress.getText().contentEquals(modModem.getIpv4AddressStored()))
 
       {
 
         result = true;
 
       }
 
       if (!jtfSubnetMask.getText().contentEquals(modModem.getSubnetMaskStored()))
 
       {
 
         result = true;
 
       }
 
       if (!jtfDefaultGw.getText().contentEquals(modModem.getdefaultGateWayStored()))
 
       {
 
         result = true;
 
       }
 
       if (!jtfDnsIpAddress.getText().contentEquals(modModem.getDnsIpv4AddressStored()))
 
       {
 
         result = true;
 
       }
 
     }
 
     if ((cbIpConFig.getSelectedIndex() == 1) && (modModem.getDHCPIpv4ClientStatusStored().contentEquals("NO")))
 
     {
 
       result = true;
 
     }
 
     return result;
 
   }
 
   
 
     /**
 
      * Function to refresh and fill panel IPv4 Configuration of a G.hn node
 
      * @param selectedModem Selected G.hn node from tree
 
      * @return void
 
      */
 
     @Override
 
     public void update(){
 
         //Refresh Network Configuration panel
 
 
 
         String sDhcpConfig = modModem.getDHCPIpv4ClientStatusStored();
 
         if (sDhcpConfig != null){
 
             if (sDhcpConfig.contains("YES")) {
 
                 cbIpConFig.setSelectedItem(sIpConfigOptions[1]);
 
                 fixedConfigurationEnable(false);
 
             }else{ 
 
                 cbIpConFig.setSelectedItem(sIpConfigOptions[0]);
 
                 fixedConfigurationEnable(true);
 
             }
 
         }
 
         
 
         // Refresh Fixed Ip Configuration Panel
 
         String sIpAddress = modModem.getIpv4AddressStored();
 
         String sSubnetMask = modModem.getSubnetMaskStored();
 
         String sDefaultGw = modModem.getdefaultGateWayStored();
 
         String sDnsIpAddress = modModem.getDnsIpv4AddressStored();
 
         String sIpAddressAdd = modModem.getIpv4AddressAdditionalStored();
 
     String sSubnetMaskAdd = modModem.getSubnetMaskAdditionalStored();
 
 
 
         if (sIpAddressAdd != null){
 
             if (sIpAddressAdd.contains(",")) {
 
                 String [] sArrayIpAddressAdd = sIpAddressAdd.split(",");
 
         String [] sArraySubnetMaskAdd = sSubnetMaskAdd.split(",");
 
                 jtfIpAddress1.setText(sArrayIpAddressAdd[0]);
 
                 jtfIpAddress2.setText(sArrayIpAddressAdd[1]);
 
         jtfSubnetMask1.setText(sArraySubnetMaskAdd[0]);
 
         jtfSubnetMask2.setText(sArraySubnetMaskAdd[1]);
 
         if (sArrayIpAddressAdd[0].contentEquals("0.0.0.0"))
 
         {
 
           chckbxAdditonalIp1.setSelected(false);
 
           setEnableAdditionalIp(1, false);
 
         }
 
         else
 
         {
 
           chckbxAdditonalIp1.setSelected(true);
 
           setEnableAdditionalIp(1, true);
 
         }
 
         if (sArrayIpAddressAdd[1].contentEquals("0.0.0.0"))
 
         {
 
           chckbxAdditonalIp2.setSelected(false);
 
           setEnableAdditionalIp(2, false);
 
         }
 
         else
 
         {
 
           chckbxAdditonalIp2.setSelected(true);
 
           setEnableAdditionalIp(2, true);
 
         }
 
             } else {
 
                 jtfIpAddress1.setText(sIpAddressAdd);
 
                 jtfIpAddress2.setText(sNotAvailable);
 
           chckbxAdditonalIp2.setEnabled(false);
 
           setEnableAdditionalIp(2, false);
 
           jtfIpAddress2.setText(sNotAvailable);
 
             }
 
         } else {
 
           chckbxAdditonalIp1.setEnabled(false);
 
           chckbxAdditonalIp2.setEnabled(false);
 
           setEnableAdditionalIp(1, false);
 
           setEnableAdditionalIp(2, false);
 
             jtfIpAddress1.setText(sNotAvailable);
 
             jtfIpAddress2.setText(sNotAvailable);
 
         }
 
                 
 
         if (sIpAddress != null)
 
         {
 
             jtfIpAddress.setText(sIpAddress);
 
         }
 
         else
 
         {
 
             jtfIpAddress.setText(sNotAvailable);
 
         }
 
         if (sSubnetMask != null)
 
         {
 
             jtfSubnetMask.setText(sSubnetMask);
 
         }
 
         else
 
         {
 
             jtfSubnetMask.setText(sNotAvailable);
 
         }
 
         if (sDefaultGw != null)
 
         {
 
             jtfDefaultGw.setText(sDefaultGw);
 
         }
 
         else
 
         {
 
             jtfDefaultGw.setText(sNotAvailable);
 
         }
 
         if (sDnsIpAddress != null)
 
         {
 
             jtfDnsIpAddress.setText(sDnsIpAddress);
 
         }
 
         else
 
         {
 
             jtfDnsIpAddress.setText(sNotAvailable);
 
         }
 
     }
 
 }
 
 