package main;



import gui.JDialogChangeConfigurationPassword;

import gui.JDialogChangeFactoryResetPassword;

import gui.JDialogChangeMgmtVlan;

import gui.JDialogInputValue;

import gui.JDialogMultiUpgrade;

import gui.JFrameNetworkAllParameters;

import gui.JDialogNetworkNotches;

import gui.JDialogRecover;

import gui.JFrameTrafficMonitor;

import gui.JDialogUpgrade;

import gui.JListNetwork;

import gui.JPanelAccessList;

import gui.JPanelAdvanced;

import gui.JPanelBasicConfig;

import gui.JPanelBridgeConfig;

import gui.JPanelEthernet;

import gui.JPanelGhnNetwork;

import gui.JPanelGnow;

import gui.JPanelHWInfo;

import gui.JPanelIpConfig;

import gui.JPanelIpv6Config;

import gui.JPanelLogFileConfig;

import gui.JPanelNTPConfig;

import gui.JPanelNetworkTopology;

import gui.JPanelIGMP;

import gui.JPanelNetworkInfo;

import gui.JPanelNoConnection;

import gui.JPanelNoDetection;

import gui.JPanelNodeSNR;

import gui.JPanelNotches;

import gui.JPanelQoSConfig;

import gui.JPanelQoSConfigWave2;

import gui.JPanelSpirit62Config;

import gui.JPanelSync;

import gui.JPanelTR069Config;

import gui.JPanelVLAN64Config;

import gui.JPanelVLAN66Config;

import gui.JUpdatablePanel;

import gui.WaitScreen;



import java.awt.BorderLayout;

import java.awt.Cursor;

import java.awt.Desktop;

import java.awt.Dimension;

import java.awt.Font;

import java.awt.Toolkit;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import java.util.Collection;

import java.util.Hashtable;

import java.util.Iterator;

import java.util.Set;

import java.util.Timer;

import java.util.TimerTask;



import javax.swing.JButton;

import javax.swing.JFrame;

import javax.swing.JLabel;

import javax.swing.JOptionPane;

import javax.swing.JPanel;

import javax.swing.JTabbedPane;

import javax.swing.border.EmptyBorder;



import network.MarvellNet;

import network.SctDevice;

import resources.ExtractResources;



import javax.swing.event.ListSelectionListener;



import deklib.DekBin;



import javax.swing.event.ChangeEvent;

import javax.swing.event.ChangeListener;

import javax.swing.event.ListSelectionEvent;

import javax.swing.JMenuBar;

import javax.swing.JMenuItem;

import javax.swing.JMenu;

import javax.swing.ImageIcon;

import javax.swing.JToolBar;

import java.awt.event.KeyEvent;

import java.awt.event.WindowAdapter;

import java.awt.event.WindowEvent;

import java.io.File;

import java.io.IOException;



import javax.swing.border.SoftBevelBorder;

import javax.swing.border.BevelBorder;

import java.awt.Component;

import javax.swing.SwingConstants;

import javax.swing.ToolTipManager;

import javax.swing.JCheckBoxMenuItem;

import javax.swing.JRadioButtonMenuItem;

import javax.swing.JScrollPane;

import javax.swing.JSeparator;

import java.awt.Window.Type;

import javax.swing.border.LineBorder;

import java.awt.Color;

import javax.swing.border.CompoundBorder;

import java.awt.SystemColor;

import java.awt.event.MouseAdapter;

import java.awt.event.MouseEvent;

import javax.swing.JToggleButton;



/**

 * Class that defines Main Application Frame of the Spirit Config Tool

 */

public class JWindowMain extends JFrame 

{	

	MarvellNet myNetwork;

	Set<String> setMacSet;

	Hashtable<String, SctDevice> hModemSet;

	String sLocalNode;

	

  private JPanel contentPane;

	

	JListNetwork jlNetwork;



	private JFrameNetworkAllParameters allparamview = null;

  private JFrameTrafficMonitor trafficmonitor = null;



	JPanel jpTabContainer;

	JTabbedPane[] jtNodeTabbedPane;

	int[] selectedTab;

	JTabbedPane[] jtNodeTabbedPaneExpertTabs;

	

	//String statusMessage ="";

	JLabel lblMessage;

	private JMenuBar menuBar;

	private JMenu mnNetwork;

	private JMenu mnHelp;

	private JMenuItem mntmRediscoverNetwork;

	private JMenuItem mntmClose;

	private JMenuItem mntmShowHelpWindow;

	private JMenuItem mntmAbout;

	private JPanel jpLeft;

	private JButton btnRediscover;

	private JToolBar jtoolbarDevice;

	private JLabel lblDeviceConfiguration;

	private JButton btnRefresh;

	private JScrollPane scrollPane;

	private JPanel jpNoDetection;

	private JPanel jpNoConnection;

	private JPanelNetworkInfo jpNetworkInfo;

	private JMenuItem mntmRefreshAllNodes;

	private JMenuItem mntmChangeSeedIdx;

	private JLabel lblTip;

	

	private boolean expertMode = true;

	private boolean httpMode = false;

	private String httpModeIp = "";

	

	private JPanel panel;

	private JButton btnReboot;

	private JLabel label_1;

	private JLabel label_2;

	private JMenuItem mntmChangeManagementVlan;

	private JMenuItem mntmViewNetworkMap;

	private JSeparator separator;

	private JMenuItem mntmViewNetworkParameters;

	private JMenuItem mntmPairingAllNodes;

	private JMenuItem mntmUnpairingAllNodes;

	private JMenuItem mntmChangePasswordIn;

	private JMenuItem mntmUpgradeMultipleNodes;

	private JMenuItem mntmFactoryResetLl;

	private JMenuItem mntmConfigureNotches;

	private JSeparator separator_1;

	private JMenuItem mntmNewMenuItem;

	private JSeparator separator_2;

		

	/**

	 * mainWindow constructor

	 */

	

	public JWindowMain() 

	{

	  setIconImage(Toolkit.getDefaultToolkit().getImage(JWindowMain.class.getResource("/resources/icon.gif")));

	  setTitle("Spirit Config Tool");

    // These workers use the frame pointer to change the Cursor

    

    ToolTipManager.sharedInstance().setInitialDelay(100);

    ToolTipManager.sharedInstance().setDismissDelay(20000);

    

    // Main panel

    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    addWindowListener(new WindowAdapter() {

      @Override

      public void windowClosing(WindowEvent e) {

        DekBin.stop();

        System.exit(0);

      }

    });

    setBounds(100, 100, 1170, 800);

    setMinimumSize(new Dimension(1190, 800));



    // Center on the screen

    setLocationRelativeTo(null);

    

    menuBar = new JMenuBar();

    menuBar.setBorder(null);

    menuBar.setFont(new Font("Tahoma", Font.PLAIN, 12));

    setJMenuBar(menuBar);

    

    JMenu mnDiscover = new JMenu("Discover");

    mnDiscover.setFont(new Font("Tahoma", Font.PLAIN, 12));

    mnDiscover.setMnemonic('D');

    menuBar.add(mnDiscover);

    

    mntmRediscoverNetwork = new JMenuItem("Re-discover Network");

    mntmRediscoverNetwork.setFont(new Font("Tahoma", Font.PLAIN, 12));

    mnDiscover.add(mntmRediscoverNetwork);

    mntmRediscoverNetwork.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        if (jlNetwork.getSelectedValue() != null)

        {

          if (changesInAnyTab(jlNetwork.getSelectedIndex()))

          {

            int selection = JOptionPane.showOptionDialog(InitApp.top, "You have changes that have not been applied yet.\nDiscard these changes?", "Discard changes",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,new Object[] { "Yes", "No" },"No");



            if (selection == 1) 

            {

              return;

            }

          }

        }

        WaitScreen.rediscover();

      }

    });

    mntmRediscoverNetwork.setIcon(new ImageIcon(JWindowMain.class.getResource("/resources/search.png")));

    mntmRediscoverNetwork.setSelectedIcon(null);

    

    JMenuItem mntmConnectToIp = new JMenuItem("Connect to IP...");

    mntmConnectToIp.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {

        if (jlNetwork.getSelectedValue() != null)

        {

          if (changesInAnyTab(jlNetwork.getSelectedIndex()))

          {

            int selection = JOptionPane.showOptionDialog(InitApp.top, "You have changes that have not been applied yet.\nDiscard these changes?", "Discard changes",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,new Object[] { "Yes", "No" },"No");



            if (selection == 1) 

            {

              return;

            }

          }

        }

        String s = (String) JOptionPane.showInputDialog(InitApp.top, 

            "Indicate the IP of the G.hn node:", "Connect to G.hn node through IP", 

            JOptionPane.QUESTION_MESSAGE, null,

            null, "10.10.1.69");

        if (s != null)

        {

          setHttpMode(s);

          WaitScreen.rediscover();

        }

      }

    });

    mnDiscover.add(mntmConnectToIp);

        

    mntmClose = new JMenuItem("Quit");

    mntmClose.setFont(new Font("Tahoma", Font.PLAIN, 12));

    mnDiscover.add(mntmClose);

    mntmClose.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {

        if (jlNetwork.getSelectedValue() != null)

        {

          if (changesInAnyTab(jlNetwork.getSelectedIndex()))

          {

            int selection = JOptionPane.showOptionDialog(InitApp.top, "You have changes that have not been applied yet.\nDiscard these changes?", "Discard changes",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,new Object[] { "Yes", "No" },"No");



            if (selection == 0) 

            {

              DekBin.stop();

              System.exit(0);

            }

            else

            {

              return;

            }

          }

        }

        ImageIcon iiExit = new ImageIcon(JWindowSplash.class.getResource("/resources/exit.gif"));

        int selection = JOptionPane.showOptionDialog(InitApp.top, "Quit Spirit Config Tool?", "Spirit Config Tool Message",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,iiExit,new Object[] { "Yes", "No" },"No");



        if (selection == 0) 

        {

          DekBin.stop();

          System.exit(0);

        }

      }

    });

    mntmClose.setIcon(new ImageIcon(JWindowMain.class.getResource("/resources/close.png")));

    

    mnNetwork = new JMenu("Network");

    mnNetwork.setFont(new Font("Tahoma", Font.PLAIN, 12));

    mnNetwork.setMnemonic('N');

    menuBar.add(mnNetwork);

    

    JMenuItem mntmChangeDomainName = new JMenuItem("Change Domain Name in all nodes");

    mntmChangeDomainName.setFont(new Font("Tahoma", Font.PLAIN, 12));

    mntmChangeDomainName.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {

        if (jlNetwork.getSelectedValue() != null)

        {

          if (changesInAnyTab(jlNetwork.getSelectedIndex()))

          {

            int selection = JOptionPane.showOptionDialog(InitApp.top, "You have changes that have not been applied yet.\nDiscard these changes?", "Discard changes",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,new Object[] { "Yes", "No" },"No");



            if (selection == 1) 

            {

              return;

            }

          }

        }

        if (myNetwork.getNumberofNodes() > 0)

        {

          String s = (String) JOptionPane.showInputDialog(InitApp.top, 

              "Indicate the new Domain Name", "Change Domain Name in all nodes", 

              JOptionPane.QUESTION_MESSAGE, null,

              null, ((SctDevice)jlNetwork.getSelectedValue()).getDomainNameStored());

          if (s != null)

          {

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            if (myNetwork.setDomainName(s))

            {

              WaitScreen.waitWithMessage("Applying changes. Please wait...", 15);

              refreshAll();

              JOptionPane.showMessageDialog(InitApp.top, "Domain Name changed in all the network nodes.", "Spirit Config Tool Info", JOptionPane.INFORMATION_MESSAGE);

            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

          }

        }

      }

    });

    

    mntmRefreshAllNodes = new JMenuItem("Refresh all nodes");

    mntmRefreshAllNodes.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {

        if (jlNetwork.getSelectedValue() != null)

        {

          if (changesInAnyTab(jlNetwork.getSelectedIndex()))

          {

            int selection = JOptionPane.showOptionDialog(InitApp.top, "You have changes that have not been applied yet.\nDiscard these changes?", "Discard changes",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,new Object[] { "Yes", "No" },"No");



            if (selection == 1) 

            {

              return;

            }

          }

        }

        refreshAll();

      }

    });

    mntmRefreshAllNodes.setFont(new Font("Tahoma", Font.PLAIN, 12));

    mnNetwork.add(mntmRefreshAllNodes);

    

    JMenuItem mntmRebootAllDevices = new JMenuItem("Reboot all nodes");

    mntmRebootAllDevices.setFont(new Font("Tahoma", Font.PLAIN, 12));

    mntmRebootAllDevices.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {

        if (jlNetwork.getSelectedValue() != null)

        {

          if (changesInAnyTab(jlNetwork.getSelectedIndex()))

          {

            int selection = JOptionPane.showOptionDialog(InitApp.top, "You have changes that have not been applied yet.\nDiscard these changes?", "Discard changes",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,new Object[] { "Yes", "No" },"No");



            if (selection == 1) 

            {

              return;

            }

          }

        }

        if (myNetwork.getNumberofNodes() > 0)

        {

          int selection = JOptionPane.showOptionDialog(InitApp.top, "Reboot all the network nodes?", "Reboot all nodes",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,new Object[] { "Yes", "No" },"No");



          if (selection == 0) 

          {

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            if (myNetwork.rebootAll())

            {

              WaitScreen.reboot(true);

            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

          }

        }

      }

    });

    mnNetwork.add(mntmRebootAllDevices);

    

    mntmFactoryResetLl = new JMenuItem("Factory Reset all nodes");

    mntmFactoryResetLl.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) 

      {

        if (jlNetwork.getSelectedValue() != null)

        {

          if (changesInAnyTab(jlNetwork.getSelectedIndex()))

          {

            int selection = JOptionPane.showOptionDialog(InitApp.top, "You have changes that have not been applied yet.\nDiscard these changes?", "Discard changes",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,new Object[] { "Yes", "No" },"No");



            if (selection == 1) 

            {

              return;

            }

          }

        }

        if (myNetwork.getNumberofNodes() > 0)

        {

          int selection = JOptionPane.showOptionDialog(InitApp.top, "Factory Reset all the network nodes?", "Factory Reset all nodes",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,new Object[] { "Yes", "No" },"No");



          if (selection == 0) 

          {

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            if (myNetwork.factoryResetAll())

            {

              WaitScreen.reboot(true);;

            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

          }

        }

      }

    });

    mntmFactoryResetLl.setFont(new Font("Tahoma", Font.PLAIN, 12));

    mnNetwork.add(mntmFactoryResetLl);

    

    separator_1 = new JSeparator();

    mnNetwork.add(separator_1);

    mnNetwork.add(mntmChangeDomainName);

    

    JMenuItem mntmChangeProfileIn = new JMenuItem("Change Profile in all nodes");

    mntmChangeProfileIn.setFont(new Font("Tahoma", Font.PLAIN, 12));

    mntmChangeProfileIn.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {

        if (jlNetwork.getSelectedValue() != null)

        {

          if (changesInAnyTab(jlNetwork.getSelectedIndex()))

          {

            int selection = JOptionPane.showOptionDialog(InitApp.top, "You have changes that have not been applied yet.\nDiscard these changes?", "Discard changes",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,new Object[] { "Yes", "No" },"No");



            if (selection == 1) 

            {

              return;

            }

          }

        }

        if (myNetwork.getNumberofNodes() > 0)

        {

          String[] phyModes = myNetwork.getProfiles();

          String sprofile = (String) JOptionPane.showInputDialog(InitApp.top, 

              "Indicate the new Profile", "Change Profile in all nodes", 

              JOptionPane.QUESTION_MESSAGE, null,

              phyModes, MarvellNet.phyModeIdToProfile(((SctDevice)jlNetwork.getSelectedValue()).getPhyModeIDStored()).toString());

          

          if (sprofile != null) 

          {

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            if (myNetwork.setPhyModeID(MarvellNet.profileStrToPhyModeId(sprofile, (SctDevice) jlNetwork.getSelectedValue())))

            {

              WaitScreen.waitWithMessage("Applying changes. Please wait...", 15);

              refreshAll();

              JOptionPane.showMessageDialog(InitApp.top, "Profile changed in all the network nodes.", "Spirit Config Tool Info", JOptionPane.INFORMATION_MESSAGE);



            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

          }

        }

      }

    });

    

    mntmChangeSeedIdx = new JMenuItem("Change Seed Idx in all nodes");

    mntmChangeSeedIdx.setVisible(false);

    mntmChangeSeedIdx.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        if (jlNetwork.getSelectedValue() != null)

        {

          if (changesInAnyTab(jlNetwork.getSelectedIndex()))

          {

            int selection = JOptionPane.showOptionDialog(InitApp.top, "You have changes that have not been applied yet.\nDiscard these changes?", "Discard changes",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,new Object[] { "Yes", "No" },"No");



            if (selection == 1) 

            {

              return;

            }

          }

        }

        if ((myNetwork.getNumberofNodes() > 0) &&

            ((SctDevice)jlNetwork.getSelectedValue()).getDomainIDModeStored().contentEquals("3"))

        {

          String s = (String) JOptionPane.showInputDialog(InitApp.top, 

              "Indicate the new Seed Index", "Change Seed Index in all nodes", 

              JOptionPane.QUESTION_MESSAGE, null,

              null, ((SctDevice)jlNetwork.getSelectedValue()).getExtendedSeedStored());

          if ((s != null) && (s.length()>0) &&((Integer.parseInt(s))>0) &&((Integer.parseInt(s))<=299))

          {

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            if (myNetwork.setExtendedSeedIdx(s))

            {

              WaitScreen.waitWithMessage("Applying changes. Please wait...", 15);

              refreshAll();

              JOptionPane.showMessageDialog(InitApp.top, "Seed Idx changed in all the network nodes.", "Spirit Config Tool Info", JOptionPane.INFORMATION_MESSAGE);

            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

          }   

          else

          {

            JOptionPane.showMessageDialog(InitApp.top, "New Seed Index value is invalid!", "Spirit Config Tool Error", JOptionPane.ERROR_MESSAGE);

          }



        }

        else

        {

          JOptionPane.showMessageDialog(InitApp.top, "Seed Index cannot be changed!", "Spirit Config Tool Error", JOptionPane.ERROR_MESSAGE);

        }

      }

    });

    mntmChangeSeedIdx.setFont(new Font("Tahoma", Font.PLAIN, 12));

    mnNetwork.add(mntmChangeSeedIdx);

    mnNetwork.add(mntmChangeProfileIn);

    

    mntmPairingAllNodes = new JMenuItem("Pairing all nodes");

    mntmPairingAllNodes.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {

        if (jlNetwork.getSelectedValue() != null)

        {

          if (changesInAnyTab(jlNetwork.getSelectedIndex()))

          {

            int selection = JOptionPane.showOptionDialog(InitApp.top, "You have changes that have not been applied yet.\nDiscard these changes?", "Discard changes",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,new Object[] { "Yes", "No" },"No");



            if (selection == 1) 

            {

              return;

            }

          }

        }

        if (myNetwork.getNumberofNodes() > 0)

        {

          int selection = JOptionPane.showOptionDialog(InitApp.top, "Start pairing in all the network nodes?", "Pairing all nodes",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,new Object[] { "Yes", "No" },"No");



          if (selection == 0) 

          {

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            if (myNetwork.pairingAll())

            {

              WaitScreen.waitWithMessage("Applying changes. Please wait...", 45);

              refreshAll();

              JOptionPane.showMessageDialog(InitApp.top, "All the network nodes are now paired.", "Spirit Config Tool Info", JOptionPane.INFORMATION_MESSAGE);

            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

          }

        }



      }

    });

    mntmPairingAllNodes.setFont(new Font("Tahoma", Font.PLAIN, 12));

    mnNetwork.add(mntmPairingAllNodes);

    

    mntmUnpairingAllNodes = new JMenuItem("Unpairing all nodes");

    mntmUnpairingAllNodes.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        if (jlNetwork.getSelectedValue() != null)

        {

          if (changesInAnyTab(jlNetwork.getSelectedIndex()))

          {

            int selection = JOptionPane.showOptionDialog(InitApp.top, "You have changes that have not been applied yet.\nDiscard these changes?", "Discard changes",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,new Object[] { "Yes", "No" },"No");



            if (selection == 1) 

            {

              return;

            }

          }

        }

        if (myNetwork.getNumberofNodes() > 0)

        {

          int selection = JOptionPane.showOptionDialog(InitApp.top, "Start unpairing in all the network nodes?", "Unpairing all nodes",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,new Object[] { "Yes", "No" },"No");



          if (selection == 0) 

          {

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            if (myNetwork.unpairingAll())

            {

              WaitScreen.waitWithMessage("Applying changes. Please wait...", 15);

              refreshAll();

              JOptionPane.showMessageDialog(InitApp.top, "All the network nodes are now unpaired.", "Spirit Config Tool Info", JOptionPane.INFORMATION_MESSAGE);

            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

          }

        }

      }

    });

    mntmUnpairingAllNodes.setFont(new Font("Tahoma", Font.PLAIN, 12));

    mnNetwork.add(mntmUnpairingAllNodes);

    

    mntmChangePasswordIn = new JMenuItem("Change Password in all nodes");

    mntmChangePasswordIn.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {

        if (jlNetwork.getSelectedValue() != null)

        {

          if (changesInAnyTab(jlNetwork.getSelectedIndex()))

          {

            int selection = JOptionPane.showOptionDialog(InitApp.top, "You have changes that have not been applied yet.\nDiscard these changes?", "Discard changes",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,new Object[] { "Yes", "No" },"No");



            if (selection == 1) 

            {

              return;

            }

          }

        }

        if (myNetwork.getNumberofNodes() > 0)

        {

          String s = (String) JOptionPane.showInputDialog(InitApp.top, 

              "Indicate the new Password:", "Change Password in all nodes", 

              JOptionPane.QUESTION_MESSAGE, null,

              null, ((SctDevice)jlNetwork.getSelectedValue()).getPairingPasswordStringStored());

          if (s != null)

          {

            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            if (myNetwork.setPairingPasswordAll(s))

            {

              WaitScreen.waitWithMessage("Applying changes. Please wait...", 15);

              refreshAll();

              JOptionPane.showMessageDialog(InitApp.top, "All the network nodes have now the new Password.", "Spirit Config Tool Info", JOptionPane.INFORMATION_MESSAGE);

            }

            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

          }

        }

      }

    });

    mntmChangePasswordIn.setFont(new Font("Tahoma", Font.PLAIN, 12));

    mnNetwork.add(mntmChangePasswordIn);

    

    mntmUpgradeMultipleNodes = new JMenuItem("Upgrade multiple nodes");

    mntmUpgradeMultipleNodes.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {

        if (jlNetwork.getSelectedValue() != null)

        {

          if (changesInAnyTab(jlNetwork.getSelectedIndex()))

          {

            int selection = JOptionPane.showOptionDialog(InitApp.top, "You have changes that have not been applied yet.\nDiscard these changes?", "Discard changes",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,new Object[] { "Yes", "No" },"No");



            if (selection == 1) 

            {

              return;

            }

          }

        }

        JDialogMultiUpgrade id = new JDialogMultiUpgrade(myNetwork);

        id.setLocationRelativeTo(null);

        id.setVisible(true);

        if (id.reboot)

        {

          WaitScreen.rediscover();

        }

      }

    });

    mntmUpgradeMultipleNodes.setFont(new Font("Tahoma", Font.PLAIN, 12));

    mnNetwork.add(mntmUpgradeMultipleNodes);

    

    mntmConfigureNotches = new JMenuItem("Configure Notches");

    mntmConfigureNotches.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {

        if (jlNetwork.getSelectedValue() != null)

        {

          if (changesInAnyTab(jlNetwork.getSelectedIndex()))

          {

            int selection = JOptionPane.showOptionDialog(InitApp.top, "You have changes that have not been applied yet.\nDiscard these changes?", "Discard changes",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,new Object[] { "Yes", "No" },"No");



            if (selection == 1) 

            {

              return;

            }

          }

        }

        JDialogNetworkNotches id = new JDialogNetworkNotches(myNetwork);

        id.setLocationRelativeTo(null);

        id.setVisible(true);

        if (id.ok)

        {

          refreshAll();

        }

      }

    });

    mntmConfigureNotches.setFont(new Font("Tahoma", Font.PLAIN, 12));

    mnNetwork.add(mntmConfigureNotches);

    

    separator = new JSeparator();

    mnNetwork.add(separator);

    

    // TODO: not for the moment

    /*

    mntmViewNetworkMap = new JMenuItem("View Network Graph");

    mntmViewNetworkMap.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {

        JDialogNetworkMap id = new JDialogNetworkMap(myNetwork);

        id.setLocationRelativeTo(null);

        id.setVisible(true);

      }

    });

    mntmViewNetworkMap.setFont(new Font("Tahoma", Font.PLAIN, 12));

    //mnNetwork.add(mntmViewNetworkMap);

    */

    

    mntmViewNetworkParameters = new JMenuItem("View Network Parameters");

    mntmViewNetworkParameters.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {

        if (jlNetwork.getSelectedValue() != null)

        {

          if (changesInAnyTab(jlNetwork.getSelectedIndex()))

          {

            int selection = JOptionPane.showOptionDialog(InitApp.top, "You have changes that have not been applied yet.\nDiscard these changes?", "Discard changes",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,new Object[] { "Yes", "No" },"No");



            if (selection == 1) 

            {

              return;

            }

          }

        }

        if (allparamview == null)

        {

          allparamview = new JFrameNetworkAllParameters(myNetwork);

          allparamview.setLocationRelativeTo(null);

          allparamview.setVisible(true);

        }

        else

        {

          allparamview.setVisible(true);

          allparamview.toFront(); 

          allparamview.net = myNetwork;

          allparamview.update();

        }

      }

    });

    

    mntmNewMenuItem = new JMenuItem("Traffic Monitor");

    mntmNewMenuItem.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {

        if (jlNetwork.getSelectedValue() != null)

        {

          if (changesInAnyTab(jlNetwork.getSelectedIndex()))

          {

            int selection = JOptionPane.showOptionDialog(InitApp.top, "You have changes that have not been applied yet.\nDiscard these changes?", "Discard changes",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,new Object[] { "Yes", "No" },"No");



            if (selection == 1) 

            {

              return;

            }

          }

        }

        if (trafficmonitor == null)

        {

          trafficmonitor = new JFrameTrafficMonitor(myNetwork);

          trafficmonitor.setLocationRelativeTo(null);

          trafficmonitor.setVisible(true);

        }

        else

        {

          trafficmonitor.dispose();

          trafficmonitor = new JFrameTrafficMonitor(myNetwork);

          trafficmonitor.setLocationRelativeTo(null);

          trafficmonitor.setVisible(true);



        }

      }

    });

    mnNetwork.add(mntmNewMenuItem);

    

    separator_2 = new JSeparator();

    mnNetwork.add(separator_2);

    mntmViewNetworkParameters.setFont(new Font("Tahoma", Font.PLAIN, 12));

    mnNetwork.add(mntmViewNetworkParameters);

    

    JMenu mnOptions = new JMenu("Options");

    mnOptions.setFont(new Font("Tahoma", Font.PLAIN, 12));

    mnOptions.setMnemonic('O');

    menuBar.add(mnOptions);

    

    JMenuItem mntmConfigurePassword = new JMenuItem("Change Configuration Password");

    mntmConfigurePassword.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {

        JDialogChangeConfigurationPassword id = new JDialogChangeConfigurationPassword(MarvellNet.cfgPassword);

        id.setLocationRelativeTo(null);

        id.setVisible(true);



        if (id.ok)

        {

          MarvellNet.cfgPassword = id.cfgPassword;

        }

      }

    });

    mntmConfigurePassword.setFont(new Font("Tahoma", Font.PLAIN, 12));

    mnOptions.add(mntmConfigurePassword);

    

    JMenuItem mntmChangeFactoryReset = new JMenuItem("Change Factory Reset Password");

    mntmChangeFactoryReset.setFont(new Font("Tahoma", Font.PLAIN, 12));

    mntmChangeFactoryReset.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {

        JDialogChangeFactoryResetPassword id = new JDialogChangeFactoryResetPassword(MarvellNet.frstPassword);

        id.setLocationRelativeTo(null);

        id.setVisible(true);



        if (id.ok)

        {

          MarvellNet.frstPassword = id.cfgPassword;

        }

      }

    });

    mnOptions.add(mntmChangeFactoryReset);

    

    mntmChangeManagementVlan = new JMenuItem("Change management VLAN");

    mntmChangeManagementVlan.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {

        if (jlNetwork.getSelectedValue() != null)

        {

          if (changesInAnyTab(jlNetwork.getSelectedIndex()))

          {

            int selection = JOptionPane.showOptionDialog(InitApp.top, "You have changes that have not been applied yet.\nDiscard these changes?", "Discard changes",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,new Object[] { "Yes", "No" },"No");



            if (selection == 1) 

            {

              return;

            }

          }

        }

        JDialogChangeMgmtVlan id = new JDialogChangeMgmtVlan(MarvellNet.vlanId);

        id.setLocationRelativeTo(null);

        id.setVisible(true);



        if (id.ok)

        {

          MarvellNet.vlanId = id.vlanId;

          WaitScreen.rediscover();

        }

      }

    });

    mntmChangeManagementVlan.setFont(new Font("Tahoma", Font.PLAIN, 12));

    mnOptions.add(mntmChangeManagementVlan);

    

    mnHelp = new JMenu("Help");

    mnHelp.setFont(new Font("Tahoma", Font.PLAIN, 12));

    mnHelp.setMnemonic('H');

    menuBar.add(mnHelp);

    

    mntmShowHelpWindow = new JMenuItem("Show Help");

    mntmShowHelpWindow.setFont(new Font("Tahoma", Font.PLAIN, 12));

    mntmShowHelpWindow.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        File file = new File("htmlhelp/index.html");

        if (file.exists())

        {

          try {

            Desktop.getDesktop().open( file );

          } catch (Exception e1) {

            // TODO Auto-generated catch block

            e1.printStackTrace();

          }

        }

        else

        {

          //JOptionPane.showMessageDialog(InitApp.top, "<html>Ups! The file <i>htmlhelp/index.html</i> does not exist!", "Spirit Config Tool Error", JOptionPane.WARNING_MESSAGE);

          // If htmlhelp directory does not exists, create a temporary PDF help included in the JAR

          File pdf = ExtractResources.extractFileToTmp("help.pdf");

          try {

            Desktop.getDesktop().open( pdf );

          } catch (Exception e1) {

            // TODO Auto-generated catch block

            e1.printStackTrace();

          }

        }

      }

    });

    mntmShowHelpWindow.setIcon(new ImageIcon(JWindowMain.class.getResource("/resources/help16.png")));

    mnHelp.add(mntmShowHelpWindow);

    

    mntmAbout = new JMenuItem("About");

    mntmAbout.setFont(new Font("Tahoma", Font.PLAIN, 12));

    mntmAbout.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        JWindowSplash splash = null;

        splash = new JWindowSplash(false);

        splash.setVisible(true);

      }

    });

    mntmAbout.setIcon(new ImageIcon(JWindowMain.class.getResource("/resources/rosca16.gif")));

    mnHelp.add(mntmAbout);

    

    contentPane = new JPanel();

    contentPane.setBorder(null);

    setContentPane(contentPane);

    contentPane.setLayout(new BorderLayout(0, 0));

    

    jpNetworkInfo = new JPanelNetworkInfo(myNetwork);

    contentPane.add(jpNetworkInfo, BorderLayout.NORTH);

    

    JPanel jpStatusBar = new JPanel();

    jpStatusBar.setBorder(new EmptyBorder(3, 6, 3, 6));

    contentPane.add(jpStatusBar, BorderLayout.SOUTH);

    jpStatusBar.setLayout(new BorderLayout(0, 0));

    

    JLabel lblCopyright = new JLabel("Version "+Version.versionString);

    jpStatusBar.add(lblCopyright, BorderLayout.WEST);

    lblCopyright.setFont(new Font("Tahoma", Font.PLAIN, 10));

    lblCopyright.setPreferredSize(new Dimension(200, 20));

    lblCopyright.setMinimumSize(new Dimension(46, 20));

    lblCopyright.setMaximumSize(new Dimension(46, 20));

    

    panel = new JPanel();

    panel.setBorder(new LineBorder(Color.GRAY));

    panel.setBackground(new Color(250, 250, 210));

    jpStatusBar.add(panel, BorderLayout.EAST);

    

    lblTip = new JLabel();

    lblTip.setFont(new Font("Tahoma", Font.PLAIN, 11));

    panel.add(lblTip);

    

    jpTabContainer = new JPanel();

    jpTabContainer.setFont(new Font("Tahoma", Font.PLAIN, 11));

    jpTabContainer.setBorder(new EmptyBorder(0, 8, 0, 0));

    jpTabContainer.setLayout(new BorderLayout(0, 0));

    contentPane.add(jpTabContainer, BorderLayout.CENTER);

    

    jtoolbarDevice = new JToolBar();

    jtoolbarDevice.setBorder(new EmptyBorder(4, 4, 4, 8));

    jtoolbarDevice.setFloatable(false);

    jpTabContainer.add(jtoolbarDevice, BorderLayout.NORTH);

    

    

    btnRefresh = new JButton("Refresh");

    btnRefresh.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {

        if (jlNetwork.getSelectedValue() != null)

        {

          if (changesInAnyTab(jlNetwork.getSelectedIndex()))

          {

            int selection = JOptionPane.showOptionDialog(InitApp.top, "You have changes that have not been applied yet.\nDiscard these changes?", "Discard changes",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,new Object[] { "Yes", "No" },"No");



            if (selection == 1) 

            {

              return;

            }

          }

          if (myNetwork.accessPolicy.contentEquals("PRO") || myNetwork.accessPolicy.contentEquals("NEXT"))

          {

            WaitScreen.refresh();

          }

          else

          {

            refresh();

          }

        }

      }

    });

    btnRefresh.setMnemonic('e');

    btnRefresh.setEnabled(false);



    btnRediscover = new JButton("Re-discover Network");

    btnRediscover.setMnemonic('r');

    btnRediscover.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        if (jlNetwork.getSelectedValue() != null)

        {

          if (changesInAnyTab(jlNetwork.getSelectedIndex()))

          {

            int selection = JOptionPane.showOptionDialog(InitApp.top, "You have changes that have not been applied yet.\nDiscard these changes?", "Discard changes",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,new Object[] { "Yes", "No" },"No");



            if (selection == 1) 

            {

              return;

            }

          }

        }

        WaitScreen.rediscover();

      }

    });

    btnRediscover.setFont(new Font("Tahoma", Font.PLAIN, 14));

    btnRediscover.setIcon(new ImageIcon(JWindowMain.class.getResource("/resources/rosca24.gif")));

    jtoolbarDevice.add(btnRediscover);

    

    JLabel label = new JLabel("    ");

    jtoolbarDevice.add(label);

    

    btnRefresh.setIcon(new ImageIcon(JWindowMain.class.getResource("/resources/refresh24.png")));

    btnRefresh.setFont(new Font("Tahoma", Font.PLAIN, 14));

    jtoolbarDevice.add(btnRefresh);

        

    label_1 = new JLabel("    ");

    jtoolbarDevice.add(label_1);

    

    btnReboot = new JButton("Reboot");

    btnReboot.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {

        if (jlNetwork.getSelectedValue() != null)

        {

          if (changesInAnyTab(jlNetwork.getSelectedIndex()))

          {

            int selection = JOptionPane.showOptionDialog(InitApp.top, "You have changes that have not been applied yet.\nDiscard these changes?", "Discard changes",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,new Object[] { "Yes", "No" },"No");



            if (selection == 1) 

            {

              return;

            }

          }

          boolean setResult = ((SctDevice)jlNetwork.getSelectedValue()).rebootByHWReset();

          

          if (setResult)

          {

            WaitScreen.reboot(true);

          }

          else

          {

            JOptionPane.showMessageDialog(InitApp.top, "Reboot failed or cancelled.","Spirit Config Tool Error", JOptionPane.ERROR_MESSAGE);

          }

        }

      }

    });

    btnReboot.setIcon(new ImageIcon(JWindowMain.class.getResource("/resources/reboot24.png")));

    btnReboot.setMnemonic('b');

    btnReboot.setFont(new Font("Tahoma", Font.PLAIN, 14));

    btnReboot.setEnabled(false);

    jtoolbarDevice.add(btnReboot);

    

    label_2 = new JLabel("    ");

    label_2.setMaximumSize(new Dimension(20000, 14));

    jtoolbarDevice.add(label_2);

    

    lblDeviceConfiguration = new JLabel("No device detected");

    lblDeviceConfiguration.setForeground(Color.BLUE);

    lblDeviceConfiguration.setBackground(Color.WHITE);

    lblDeviceConfiguration.setBorder(null);

    lblDeviceConfiguration.setHorizontalAlignment(SwingConstants.RIGHT);

    lblDeviceConfiguration.setMinimumSize(new Dimension(200, 32));

    lblDeviceConfiguration.setMaximumSize(new Dimension(400, 32));

    lblDeviceConfiguration.setFont(new Font("Tahoma", Font.BOLD, 20));

    jtoolbarDevice.add(lblDeviceConfiguration);



    jpNoDetection = new JPanelNoDetection();

    jpTabContainer.add(jpNoDetection, BorderLayout.CENTER);

    jpNoDetection.setVisible(false);

    

    jpNoConnection = new JPanelNoConnection();

    jpTabContainer.add(jpNoDetection, BorderLayout.CENTER);

    jpNoConnection.setVisible(false);



    jpLeft = new JPanel();

    jpLeft.setBorder(new EmptyBorder(3, 0, 0, 0));

    contentPane.add(jpLeft, BorderLayout.WEST);

    jpLeft.setLayout(new BorderLayout(0, 0));

    

    scrollPane = new JScrollPane();

    scrollPane.setPreferredSize(new Dimension(220, 2));

    scrollPane.setMinimumSize(new Dimension(220, 23));

    scrollPane.setMaximumSize(new Dimension(220, 32767));

    jpLeft.add(scrollPane, BorderLayout.CENTER);

    

    jlNetwork = new JListNetwork(myNetwork);

    scrollPane.setViewportView(jlNetwork);

    jlNetwork.setBorder(null);

    jlNetwork.addListSelectionListener(new ListSelectionListener() {

      public void valueChanged(ListSelectionEvent e) {



        if ((jlNetwork.getSelectedIndex() >= 0) && (jlNetwork.getSelectedIndex() != jlNetwork.prevSelectedIndex))

        {

          if (jlNetwork.getSelectedValue() != null)

          {

            if (changesInAnyTab(jlNetwork.prevSelectedIndex))

            {

              int selection = JOptionPane.showOptionDialog(InitApp.top, "You have changes that have not been applied yet.\nDiscard these changes?", "Discard changes",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,new Object[] { "Yes", "No" },"No");



              if (selection == 1) 

              {

                jlNetwork.setSelectedIndex(jlNetwork.prevSelectedIndex);

                return;

              }

            }

          }

          if (((SctDevice)jlNetwork.getSelectedValue()) != null)

          {

            if (!((SctDevice)jlNetwork.getSelectedValue()).hasFirstGetAllDone())

            {

              if (((SctDevice)jlNetwork.getSelectedValue()).refresh())

              {

                createTabs(jlNetwork.getSelectedIndex());

                jlNetwork.prevSelectedIndex = jlNetwork.getSelectedIndex();

              }

              else

              {

                JOptionPane.showMessageDialog(InitApp.top, "Ups! No connection with the remote node "+((SctDevice)(jlNetwork.getSelectedValue())).getMacAsString()+".", "Spirit Config Tool Error", JOptionPane.ERROR_MESSAGE);

                jlNetwork.setSelectedIndex(0);

                //createNoInfoTabs(jlNetwork.getSelectedIndex());

              }

            }

            else

            {

              createTabs(jlNetwork.getSelectedIndex());

              jlNetwork.prevSelectedIndex = jlNetwork.getSelectedIndex();

            }

          }

        }

      }

    });    

	}

 

	public void refresh()

	{

	  if (((SctDevice)jlNetwork.getSelectedValue()).refresh())

    {

      lblTip.setText("<html><font color=green><b>Refresh done!</b></font> The information of the node "+((SctDevice)jlNetwork.getSelectedValue()).getMacAsString()+" has been updated.");

      createTabs(jlNetwork.getSelectedIndex());

    }

    else

    {

      lblTip.setText("<html><font color=red><b>Refresh failed!</b></font> There is no connectivity with the node "+((SctDevice)jlNetwork.getSelectedValue()).getMacAsString()+".");

    }

    jlNetwork.repaint();

    repaint();

	}

	

	public void refreshAll()

	{

    myNetwork.refreshAll();

    for (int i=0; i< jlNetwork.getComponentCount(); i++)

    {

      try {

        if (((SctDevice)(Object)jlNetwork.getComponent(i)).refresh())

        {

          createTabs(i);

        }

      }

      catch (Exception e)

      {

        // Not a SctDevice, no refresh

      }

    }

    if (myNetwork.getNumberofNodes() > 0)

    {

      jlNetwork.setSelectedIndex(0);

      if (((SctDevice)jlNetwork.getSelectedValue()) != null)

      {

        ((SctDevice)jlNetwork.getSelectedValue()).refresh();

        createTabs(jlNetwork.getSelectedIndex());

      }

    }

    jlNetwork.repaint();

    repaint();

	}

	

  public void setHttpMode(String ip)

	{

	  httpMode = true;

	  httpModeIp = ip;

	}



  public boolean rediscoverNetwork() 

  {

    boolean result;

    

    if (allparamview != null)

    {

      allparamview.setVisible(false);

    }

    if (trafficmonitor != null)

    {

      trafficmonitor.dispose();

    }

    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));



    jpTabContainer.remove(jpNoDetection);

    if (jtNodeTabbedPane != null)

    {

      for (int i = 0; i < jtNodeTabbedPane.length; i++)

      {

        if (jtNodeTabbedPane[i] != null)

        {

          jpTabContainer.remove(jtNodeTabbedPane[i]);

          jtNodeTabbedPane[i].setVisible(false);

        }

        else

        {

          //System.out.println("Tab "+i+" is null!");

        }

      }

    }

    

    jlNetwork.changeNetwork(null);

    jlNetwork.prevSelectedIndex = -1;

    jpNetworkInfo.changeNetwork(null);

    jlNetwork.update();

    jpNetworkInfo.update();

    jlNetwork.setSelectedIndex(-1);

    mnNetwork.setVisible(false);



    if (httpMode)

    {

      myNetwork = new MarvellNet(httpModeIp);

    }

    else

    {

      myNetwork = new MarvellNet();

    }

    if (myNetwork.getNumberofNodes() > 0)

    {

      jpNoDetection.setVisible(false);

      jtNodeTabbedPane = new JTabbedPane[myNetwork.getNumberofNodes()];

      selectedTab = new int[myNetwork.getNumberofNodes()];

      jtNodeTabbedPaneExpertTabs = new JTabbedPane[myNetwork.getNumberofNodes()];

      jlNetwork.changeNetwork(myNetwork);

      jpNetworkInfo.changeNetwork(myNetwork);

      jlNetwork.update();

      jlNetwork.setSelectedIndex(0);

      jpNetworkInfo.update();

      jpNoDetection.setVisible(false);

      mnNetwork.setVisible(true);

      if (httpMode)

      {

        lblTip.setText("<html><b>Note:</b> Connected to "+httpModeIp+" throught HTTP connection.");

      }

      else

      {

        lblTip.setText("<html><b>Tip:</b> Use the <b>Refresh</b> button to update the information of the selected node.");

      }

      result = true;

    }

    else if (myNetwork.ethBoot)

    {

      // Show recover dialog

      JDialogRecover dialog = new JDialogRecover(myNetwork.ethBootInterface, myNetwork.ethBootVersion);

      dialog.setLocationRelativeTo(null);

      dialog.setVisible(true);

      result = true;

    }

    else

    {

      jpTabContainer.add(jpNoDetection, BorderLayout.CENTER);

      jpNoDetection.setVisible(true);

      btnRefresh.setEnabled(false);

      btnReboot.setEnabled(false);

      lblDeviceConfiguration.setText("Nothing detected");

      lblTip.setText("<html><b>Tip:</b> Check the Ethernet LED of the connected node. Is it blinking?");

      result = false;

    }

    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    return result;



  }



  public void createTabs(int i)

	{

    int fw_version  = 0;

    boolean spirit62 = false;

    SctDevice modSelectedModem = ((SctDevice)jlNetwork.getSelectedValue());

    

    try

    {

      fw_version = Integer.parseInt(modSelectedModem.getAPIVersionStored().substring(1, 4)); 

      if ( fw_version < 398)

      {

        spirit62 = true;

      }

    }

    catch (Exception e)

    {

      

    }



	  if (jtNodeTabbedPane[i] == null)

	  {

	    //Creating panels for G.hn node entity

	    JPanelBasicConfig jpBasicConfigPanel = new JPanelBasicConfig(modSelectedModem); 

	    JPanelGhnNetwork jpGhnNetworkPanel = new JPanelGhnNetwork(modSelectedModem);

	    JPanelIpConfig jpIpConfigPanel = new JPanelIpConfig(modSelectedModem);

	    JPanelIpv6Config jpIpv6ConfigPanel = new JPanelIpv6Config(modSelectedModem);

	    JPanelAdvanced jpAdvancedPanel = new JPanelAdvanced(modSelectedModem);

	    JPanelNotches jpNotchesPanel = new JPanelNotches(modSelectedModem);

	    JPanelHWInfo jpHWInfoPanel = new JPanelHWInfo(modSelectedModem);

	    JPanelEthernet jpEthernetPanel = new JPanelEthernet(modSelectedModem);

	    JPanelGnow jpGnowPanel = new JPanelGnow(modSelectedModem);

	    //JPanelNeighborConfig jpNeighborConfig = new JPanelNeighborConfig(modSelectedModem);

	    JPanelLogFileConfig jpLogFilePanel = new JPanelLogFileConfig(modSelectedModem);

	    JPanelNodeSNR jpNodeSNR = new JPanelNodeSNR(modSelectedModem, myNetwork);

	    JPanelVLAN64Config jpNodeVLAN64 = new JPanelVLAN64Config(modSelectedModem);

	    JPanelVLAN66Config jpNodeVLAN66 = new JPanelVLAN66Config(modSelectedModem);

	    JPanelQoSConfig jpNodeQoS = new JPanelQoSConfig(modSelectedModem);

	    JPanelQoSConfigWave2 jpNodeQoSWave2 = new JPanelQoSConfigWave2(modSelectedModem);

	    JPanelNTPConfig jpNodeNTP = new JPanelNTPConfig(modSelectedModem);

	    JPanelTR069Config jpNodeTR069 = new JPanelTR069Config(modSelectedModem);

	    JPanelIGMP jpNodeIGMP = new JPanelIGMP(modSelectedModem);

	    JPanelBridgeConfig jpNodeBridge = new JPanelBridgeConfig(modSelectedModem);

	    JPanelSync jpNodeSync = new JPanelSync(modSelectedModem);

	    JPanelAccessList jpAccessList = new JPanelAccessList(modSelectedModem);

	    JPanelSpirit62Config jpSpirit62ConfigPanel = new JPanelSpirit62Config(modSelectedModem);

	    JPanelNetworkTopology jpNetworkTopology = new JPanelNetworkTopology(modSelectedModem, myNetwork);

	    //Adding Tabs



	    jtNodeTabbedPane[i] = new JTabbedPane();

	    selectedTab[i] = 0;

	    jtNodeTabbedPane[i].addChangeListener(new ChangeListener() {

	        public void stateChanged(ChangeEvent arg0) {

	          if (jlNetwork.getSelectedIndex() >= 0)

	          {

	            if (selectedTab[jlNetwork.getSelectedIndex()] != ((JTabbedPane)arg0.getSource()).getSelectedIndex())

	            {

	              if (((JUpdatablePanel)(((JTabbedPane)arg0.getSource()).getComponentAt(selectedTab[jlNetwork.getSelectedIndex()]))).changesNotAppliedYet())

	              {

	                int selection = JOptionPane.showOptionDialog(InitApp.top, "You have changes that have not been applied yet.\nDiscard these changes?", "Discard changes",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,new Object[] { "Yes", "No" },"No");



	                if (selection == 1) 

	                {

	                  (((JTabbedPane)arg0.getSource())).setSelectedIndex(selectedTab[jlNetwork.getSelectedIndex()]);

	                  return;

	                }



	              }

	              ((JUpdatablePanel)(((JTabbedPane)arg0.getSource()).getSelectedComponent())).update();

	              selectedTab[jlNetwork.getSelectedIndex()] = ((JTabbedPane) arg0.getSource()).getSelectedIndex();

	            }

	          }

	        }

	      });

	    jtNodeTabbedPane[i].setFont(new Font("Tahoma", Font.PLAIN, 12));

	    jtNodeTabbedPane[i].setVisible(false);

	    jtNodeTabbedPaneExpertTabs[i] = new JTabbedPane();

	    jtNodeTabbedPaneExpertTabs[i].setVisible(false);

	    jtNodeTabbedPane[i].setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

	    

	    if (spirit62)

	    {

	       jtNodeTabbedPane[i].addTab("Spirit 6.2 Config", jpSpirit62ConfigPanel);

	    }

	    else

	    {

	      jtNodeTabbedPane[i].addTab("Basic Config", jpBasicConfigPanel);

	      jtNodeTabbedPane[i].addTab("G.hn Connections", jpGhnNetworkPanel);



	      // This tabs are hidden by default

	      jtNodeTabbedPaneExpertTabs[i].addTab("Ethernet", jpEthernetPanel);

	      jtNodeTabbedPaneExpertTabs[i].addTab("HW Config", jpHWInfoPanel);

	      if (jpGnowPanel.isSupported())

	      {

	        jtNodeTabbedPaneExpertTabs[i].addTab("G.now", jpGnowPanel);

	      }

	      jtNodeTabbedPaneExpertTabs[i].addTab("SNR & PSD", jpNodeSNR);

	      jtNodeTabbedPaneExpertTabs[i].addTab("IPv4 Config", jpIpConfigPanel);

	      jtNodeTabbedPaneExpertTabs[i].addTab("IPv6 Config", jpIpv6ConfigPanel);

	      //jtNodeTabbedPaneExpertTabs[i].addTab("NDIM", jpNeighborConfig);

	      jtNodeTabbedPaneExpertTabs[i].addTab("Notches", jpNotchesPanel);

	      boolean fenix = (modSelectedModem.getAsicStored().contentEquals("3142")?true:false);	      

      	boolean r555 = false;

  	    try

  	    {

  	      if (fw_version >= 555)

  	      {

  	      	r555 = true;

  	      }

  	    }

  	    catch (Exception e)

  	    {

  	      

  	    }

	      

      	// In r555 it was introduced the support for new packet classifier parameters for Wave-2

	      if ((fenix == true) || (r555 == false))

	      {

	      	jtNodeTabbedPaneExpertTabs[i].addTab("QoS Config", jpNodeQoS);

	      }

	      else

	      {

	      	jtNodeTabbedPaneExpertTabs[i].addTab("QoS Config", jpNodeQoSWave2);

	      }

	      if (jpNodeVLAN66.isSupported())

	      {

	        jtNodeTabbedPaneExpertTabs[i].addTab("VLAN", jpNodeVLAN66);

	      }

	      else if (jpNodeVLAN64.isSupported())

	      {

	        jtNodeTabbedPaneExpertTabs[i].addTab("VLAN", jpNodeVLAN64);

	      }

	      if (jpNodeTR069.isSupported())

	      {

	        jtNodeTabbedPaneExpertTabs[i].addTab("TR069 Config", jpNodeTR069);

	      }

	      jtNodeTabbedPaneExpertTabs[i].addTab("Multicast & Filtering", jpNodeIGMP);

	      jtNodeTabbedPaneExpertTabs[i].addTab("Bridge", jpNodeBridge);

	      jtNodeTabbedPaneExpertTabs[i].addTab("Sync", jpNodeSync);

	      if (myNetwork.getMasterNode() == modSelectedModem)

	      {

	      	if (myNetwork.accessPolicy.contentEquals("P2MP") || myNetwork.accessPolicy.contentEquals("PRO") || myNetwork.accessPolicy.contentEquals("NEXT"))

	      	{

	      		jtNodeTabbedPaneExpertTabs[i].addTab("Access List", jpAccessList);

	      	}

	      	if ((myNetwork.accessPolicy.contentEquals("PRO") || myNetwork.accessPolicy.contentEquals("NEXT")) && (fw_version > 680)) 

	      	{

	      		jtNodeTabbedPaneExpertTabs[i].addTab("Topology", jpNetworkTopology);

	      	}

	      }

	      jtNodeTabbedPaneExpertTabs[i].addTab("Log File", jpLogFilePanel);

	      jtNodeTabbedPaneExpertTabs[i].addTab("Time Config", jpNodeNTP);

	      jtNodeTabbedPaneExpertTabs[i].addTab("Advanced Config", jpAdvancedPanel);



	      // Move tabs to visible if needed

	      showExpertTabs(expertMode);

	      jtNodeTabbedPane[i].setName("Tabs of "+modSelectedModem);

	    }	    

	  }

	  for (int j = 0; j < jtNodeTabbedPane.length; j++)

	  {

	    if (jtNodeTabbedPane[j] != null)

	    {

	      jtNodeTabbedPane[j].setVisible(false);

	      jpTabContainer.remove(jtNodeTabbedPane[j]);

	    }

	  }

	  jtNodeTabbedPane[i].setVisible(true);

    jpTabContainer.add(jtNodeTabbedPane[i], BorderLayout.CENTER);



	  Component[] tabs = (Component[])jtNodeTabbedPane[i].getComponents();

	  jpNetworkInfo.update();

	  for (int j = 0; j < tabs.length; j++)

	  {

	    if (tabs[j].getClass().toString().contains("gui."))

	    {

        String class_name = tabs[j].getClass().toString();



        if (class_name.contains("JPanelNetworkTopology"))

        {

          //In case of Spirit PRO we must force the l 

          ((JPanelNetworkTopology)tabs[j]).update(true);

        }

        else

        {

          ((JUpdatablePanel)tabs[j]).update();

        }

	    }

	  }

	  

    

	  Component[] tabsexpert = (Component[])jtNodeTabbedPaneExpertTabs[i].getComponents();

	  for (int j = 0; j < tabsexpert.length; j++)

	  {

	    if (tabsexpert[j].getClass().toString().contains("gui."))

	    {

	      String class_name = tabsexpert[j].getClass().toString();



	      if (class_name.contains("JPanelNetworkTopology")) 

	      {

	        //In case of Spirit PRO we must force the l 

	        ((JPanelNetworkTopology)tabsexpert[j]).update(true);

	      }

	      else

	      {

	        ((JUpdatablePanel)tabsexpert[j]).update();

	      }

	    }

	  }

    for (int j = 0; j < myNetwork.getNumberofNodes(); j++)

    {

      myNetwork.getNodeAt(j).selected = false;

    }



	  lblDeviceConfiguration.setText(modSelectedModem.getMACAddrStored());

    btnRefresh.setEnabled(true);

    btnReboot.setEnabled(true);

    modSelectedModem.selected = true;

	  

	  validate();

    repaint();

	}

  

  protected boolean changesInAnyTab(int i)

  {

    if (i >= 0 && i < jtNodeTabbedPane.length)

    {

      if (jtNodeTabbedPane[i] != null)

      {

        Component[] tabs = (Component[])jtNodeTabbedPane[i].getComponents();

        for (int j = 0; j < tabs.length; j++)

        {

          if (tabs[j].getClass().toString().contains("gui."))

          {

            if (((JUpdatablePanel)tabs[j]).changesNotAppliedYet())

            {

              return true;

            }

          }

        }

      }

    }

    return false;

  }



  protected void showExpertTabs(boolean expertMode) {

    for (int j = 0; j < jtNodeTabbedPane.length; j++)

    {

      if (jtNodeTabbedPane[j] != null)

      {

        if (expertMode && jtNodeTabbedPaneExpertTabs != null)

        {

          while (jtNodeTabbedPaneExpertTabs[j].getComponentCount() > 0)

          {

            jtNodeTabbedPane[j].addTab(jtNodeTabbedPaneExpertTabs[j].getTitleAt(0), jtNodeTabbedPaneExpertTabs[j].getComponentAt(0));

          }

        }

      }

    }    

  }

  

  protected void createNoInfoTabs(int i) {

    jtNodeTabbedPane[i] = new JTabbedPane();

    jtNodeTabbedPane[i].setVisible(false);



    for (int j = 0; j < jtNodeTabbedPane.length; j++)

    {

      if (jtNodeTabbedPane[j] != null)

      {

        jtNodeTabbedPane[j].setVisible(false);

      }

    }

    jtNodeTabbedPane[i].setVisible(true);

    Component[] tabs = (Component[])jtNodeTabbedPane[i].getComponents();

    for (int j = 0; j < tabs.length; j++)

    {

      ((JUpdatablePanel)tabs[j]).update();

    }

    for (int j = 0; j < myNetwork.getNumberofNodes(); j++)

    {

      myNetwork.getNodeAt(j).selected = false;

    }

    lblDeviceConfiguration.setText(((SctDevice)jlNetwork.getSelectedValue()).getMACAddrStored());



    

  }

}



