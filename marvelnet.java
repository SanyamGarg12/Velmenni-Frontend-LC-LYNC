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

 package network;

 import java.util.ArrayList;
 import javax.swing.JOptionPane;
 
 import main.InitApp;
 import network.SctDevice;
 /**
  * Class that discover visible networks containing all G.hn nodes
  */
 public class MarvellNet 
 {
   private final static String[][] profileTable = {
        //Name                  //Id          //Flags
       {"PLC 50MHz MIMO Boost",          "22",         "GEDW362F GEDW720 DW920 HN"},
       {"PLC 100MHz Boost",              "23",         "GEDW362F GEDW720 DW920 HN"},
       {"COAX 100MHz",                    "9",         "DCP362 DCP962C GAM P2MP LIFI"},
       {"PHONE 100MHz",                  "10",         "DCP362 DCP962P GAM G.now"},
       {"PHONE 100MHz Flat",             "11",         "DCP362 DCP962P GAM G.now"},
       {"PHONE 100MHz Boost",            "24",         "DCP362 DCP962P GAM G.now"},
       {"COAX 200MHz",                   "25",         "DCP962C HN P2MP LIFI"},
       {"PHONE 200MHz",                  "26",         "DCP962P HN G.now"},
       {"PHONE 200MHz Flat",             "28",         "DCP962P G.now"},
       {"PHONE 100MHz MIMO",             "29",         "DCP962P HN G.now"},
       {"PLC 100MHz MIMO Boost",          "7",         "DW920 HN"},
       {"PLC 41MHz MIMO Boost",          "15",         "DW920 DE910 PRO"},
       {"PLC 50MHz MIMO Boost",          "16",         "DW920 DE910 PRO"},
       {"PLC 41MHz Boost",               "17",         "DW920 DE910 PRO"},
       {"COAX 50MHz",                    "18",         "DCP962C P2MP LIFI"},
       {"PLC 18MHz",                     "31",         "DW920 DW910 DE910 NEXT"},
   };
   
   public static String cfgPassword = "paterna";
   public static String frstPassword = "betera";
   public static int vlanId = 0;
   
   public static boolean scpAllowed = true;
   public static boolean lcmpAllowed = true;
   
   public String accessPolicy = "Unknown";
   
     private ArrayList<SctDevice> modemSet; //nodes in same domain
     public boolean ethBoot = false;
     public String ethBootInterface = null;
     public int ethBootVersion = 0;
     
     /**
      * Constructor of the discovering of visible networks containing all G.hn nodes
      * @param inputFrame Input frame
      * @return void
      */
     public MarvellNet() 
     {
     SctDevice localModem = SctDevice.createFromDiscover(vlanId);
     
     ethBoot = false;
     
       if (localModem!=null)
       {
         if (localModem.ethBoot())
         {
           ethBoot = true;
           ethBootVersion = localModem.ethBootVersion();
           ethBootInterface = localModem.iface;
           modemSet = null;
         }
         else
         {
           modemSet = new ArrayList<SctDevice>();//Creates empty set of MAC String - GhnDevice instance table
 
           //We have devices discovered and at least one
 
         localModem.isLocal = true;
           if (localModem.refresh())
           {
             modemSet.add(localModem);
             ArrayList<SctDevice> remoteNodes = localModem.getMarvellGhnDevicesSeenBy();
             if (remoteNodes != null)
             {
               modemSet.addAll(remoteNodes);
             }
             // Detect the access mode
             accessPolicy = localModem.getAccessPolicyStored();
           }
         }
       }
       else
       {
         modemSet = null;
       }
     }
     
     public MarvellNet(String ipv4)
     {
       ethBoot = false;
       ethBootInterface = null;
       modemSet = new ArrayList<SctDevice>();
       SctDevice httpDevice = new SctDevice(ipv4);
       if (httpDevice.refresh() == true)
       {
         modemSet.add(httpDevice);
       accessPolicy = httpDevice.getAccessPolicyStored();
       }
 
     }
     
     public static boolean profileIsAccepted(int pos, SctDevice modem)
     {
       if (modem != null)
       {
         String product = modem.getHWProductStored();
         String accessPolicy = modem.getAccessPolicyStored();
         String flags = profileTable[pos][2];
         
         System.out.println("accessPolicy " + accessPolicy);
         if (flags.contains(product))
         {
           if (accessPolicy.contentEquals("HN") && flags.contains("HN"))
           {
             return true;
           }
           if (accessPolicy.contentEquals("G.now") && flags.contains("G.now"))
           {
             return true;
           }
           if (accessPolicy.contentEquals("P2MP") && flags.contains("P2MP"))
           {
             return true;
           }
           if (accessPolicy.contentEquals("PRO") && flags.contains("PRO"))
           {
             return true;
           }
           if (accessPolicy.contentEquals("LIFI") && flags.contains("LIFI"))
           {
             return true;
           }
         if (accessPolicy.contentEquals("NEXT") && flags.contains("NEXT"))
         {
           return true;
         }
         }
       }
       return false;
     }
     
   public static String[] getProfiles(SctDevice modem) {
     ArrayList<String> profiles = new ArrayList<String>();
     for (int i=0; i<profileTable.length; i++)
     {
       if (profileIsAccepted(i, modem))
       {
         profiles.add(profileTable[i][0]);
       }
     }
     if (profiles.size() > 0)
     {
       return profiles.toArray(new String[profiles.size()]);
     }
     else
     {
       return null;
     }
   }
 
     public String[] getProfiles()
     {
       
       return getProfiles(modemSet.get(0));
     }
     
     public static int profileStrToPhyModeIdAsInteger(String profile, SctDevice modem)
     {
       return (Integer.parseInt(profileStrToPhyModeId(profile, modem)));
     }
     
     public static String profileStrToPhyModeId(String profile, SctDevice modem)
     {
         if ((profile != null) && (modem != null))
         {
         String accessPolicy = modem.getAccessPolicyStored();
 
             for (int i=0; i<profileTable.length; i++)
             {
                 if ((profileTable[i][0].contentEquals(profile)) && (profileTable[i][2].contains(accessPolicy)))
                 {
                     return profileTable[i][1];
                 }
             }
         }
       return null;
     }
     
     
     public static String phyModeIdToProfile(int phyModeId)
     {
       return phyModeIdToProfile(Integer.toString(phyModeId));
     }
     
     public static String phyModeIdToProfile(String phyModeId)
     {
         if (phyModeId != null)
         {
             for (int i=0; i<profileTable.length; i++)
             {
                 if (profileTable[i][1].contentEquals(phyModeId))
                 {
                     return profileTable[i][0];
                 }
             }
         }
       return null;
     }
     
     /**
      * Recovers the collection of GhnDevices detected (Local and Remote)
      * @return
      */
     public ArrayList<SctDevice> getRemoteNodes(){
       ArrayList<SctDevice> remoteNodes = new ArrayList<SctDevice>(modemSet);
       remoteNodes.remove(0);
         return(remoteNodes);
     }
     
     public SctDevice getMasterNode(){
       SctDevice masterNode = null;
       for (int i=0; i < modemSet.size(); i++)
       {
         if (modemSet.get(i).hasFirstGetAllDone())
         {
           if (modemSet.get(i).getGeneralNodeTypeStored().equals("DOMAIN_MASTER"))
           {
             masterNode = modemSet.get(i);
           }
         }
       }
       return masterNode;
     }
     
     /**
      * Function to get String with Local Node MAC addresses
      * @return String
      */
     public String getLocalNodeMacString() {
       if (modemSet != null)
       {
           return modemSet.get(0).getMacAsString();
       }
       return null;
     }
     
   public int getNumberofNodes() {
     if (modemSet != null)
     {
       return modemSet.size();
     }
     else
     {
       return 0;
     }
   }
 
   public SctDevice getLocalNode() {
     if (modemSet != null)
     {
       return modemSet.get(0);
     }
     else
     {
       return null;
     }
   }
 
   public SctDevice getNodeAt(int selectedIndex) 
   {
     if (modemSet != null)
     {
       if (modemSet.size() > selectedIndex)
       {
         return modemSet.get(selectedIndex);
       }
     }
     return null;
   }
   
   public SctDevice getNodeWithMac(String mac, boolean removeColons) 
   {
     if (modemSet != null)
     {
       for (int i = 0; i < modemSet.size(); i++)
       {
         String iter_mac = modemSet.get(i).getMacAsString();
         if (removeColons == true)
         {
           iter_mac = iter_mac.replace(":", "");
         }
         if (iter_mac.contentEquals(mac))
         {
           return modemSet.get(i);
         }
       } 
     }
     return null;
   }
 
   public SctDevice getNodeWithMac(String mac) 
   {
     return getNodeWithMac(mac, false);
   }
 
   public boolean refreshAll()
   {
     if (modemSet != null)
     {
       for (int i = 0; i < modemSet.size(); i++)
       {
         if (modemSet.get(i).refresh() == false)
         {
           return false;
         }
       } 
       return true;
     }
     else
     {
       return false;
     }
   }
   
   public boolean setDomainName(String s) {
     ArrayList<SctDevice> remoteNodes = getRemoteNodes();
     SctDevice master = null;
     
     if (refreshAll() == false)
     {
       JOptionPane.showMessageDialog(InitApp.top, "Domain Name cannot be changed.\nOne of the nodes did not reply.", "Spirit Config Tool Error", JOptionPane.ERROR_MESSAGE);
       return false;
     }
     else
     {
       for (int i = 0; i < remoteNodes.size(); i++)
       {
         if (remoteNodes.get(i).getGeneralNodeTypeStored().contentEquals("END_POINT"))
         {
           remoteNodes.get(i).showSetErrorMessage = false;
           remoteNodes.get(i).setDomainName(s);
           remoteNodes.get(i).showSetErrorMessage = true;
         }
         else
         {
           master = remoteNodes.get(i);
         }
       }
       if (master != null)
       {
         master.showSetErrorMessage = false;
         master.setDomainName(s);
         master.showSetErrorMessage = true;
       }
       getLocalNode().setDomainName(s);
       return true;
     }
   }
 
   public boolean rebootAll() {
     ArrayList<SctDevice> remoteNodes = getRemoteNodes();
     SctDevice master = null;
     
     if (refreshAll() == false)
     {
       JOptionPane.showMessageDialog(InitApp.top, "Reboot cannot be done.\nOne of the nodes did not reply.", "Spirit Config Tool Error", JOptionPane.ERROR_MESSAGE);
       return false;
     }
     else
     {
       for (int i = 0; i < remoteNodes.size(); i++)
       {
         if (remoteNodes.get(i).getGeneralNodeTypeStored().contentEquals("END_POINT"))
         {
           remoteNodes.get(i).rebootByHWReset();
         }
         else
         {
           master = remoteNodes.get(i);
         }
       }
       if (master != null)
       {
         master.rebootByHWReset();
       }
       getLocalNode().rebootByHWReset();
       return true;
     }    
   }
   
   public boolean factoryResetAll() {
     ArrayList<SctDevice> remoteNodes = getRemoteNodes();
     SctDevice master = null;
     
     if (refreshAll() == false)
     {
       JOptionPane.showMessageDialog(InitApp.top, "Factory Reset cannot be done.\nOne of the nodes did not reply.", "Spirit Config Tool Error", JOptionPane.ERROR_MESSAGE);
       return false;
     }
     else
     {
       for (int i = 0; i < remoteNodes.size(); i++)
       {
         if (remoteNodes.get(i).getGeneralNodeTypeStored().contentEquals("END_POINT"))
         {
           remoteNodes.get(i).factoryReset();
         }
         else
         {
           master = remoteNodes.get(i);
         }
       }
       if (master != null)
       {
         master.factoryReset();
       }
       getLocalNode().factoryReset();
       return true;
     }    
   }
   
   public boolean pairingAll() {
     ArrayList<SctDevice> remoteNodes = getRemoteNodes();
     SctDevice master = null;
     
     if (refreshAll() == false)
     {
       JOptionPane.showMessageDialog(InitApp.top, "Pairing cannot be done.\nOne of the nodes did not reply.", "Spirit Config Tool Error", JOptionPane.ERROR_MESSAGE);
       return false;
     }
     else
     {
       for (int i = 0; i < remoteNodes.size(); i++)
       {
         if (remoteNodes.get(i).getGeneralNodeTypeStored().contentEquals("END_POINT"))
         {
           remoteNodes.get(i).setPairing();
           try {
             Thread.sleep(1000);
           } catch (InterruptedException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
           }
         }
         else
         {
           master = remoteNodes.get(i);
         }
       }
       if (master != null)
       {
         master.setPairing();
       }
       getLocalNode().setPairing();
       return true;
     }    
   }
   
   public boolean unpairingAll() {
     ArrayList<SctDevice> remoteNodes = getRemoteNodes();
     SctDevice master = null;
     
     if (refreshAll() == false)
     {
       JOptionPane.showMessageDialog(InitApp.top, "Unpairing cannot be done.\nOne of the nodes did not reply.", "Spirit Config Tool Error", JOptionPane.ERROR_MESSAGE);
       return false;
     }
     else
     {
       for (int i = 0; i < remoteNodes.size(); i++)
       {
         if (remoteNodes.get(i).getGeneralNodeTypeStored().contentEquals("END_POINT"))
         {
           remoteNodes.get(i).setUnpairing();
         }
         else
         {
           master = remoteNodes.get(i);
         }
       }
       if (master != null)
       {
         master.setUnpairing();
       }
       getLocalNode().setUnpairing();
       return true;
     }    
   }
 
   public boolean setPairingPasswordAll(String newPassword) {
     ArrayList<SctDevice> remoteNodes = getRemoteNodes();
     SctDevice master = null;
     
     if (refreshAll() == false)
     {
       JOptionPane.showMessageDialog(InitApp.top, "Password cannot be changed.\nOne of the nodes did not reply.", "Spirit Config Tool Error", JOptionPane.ERROR_MESSAGE);
       return false;
     }
     else
     {
       for (int i = 0; i < remoteNodes.size(); i++)
       {
         if (remoteNodes.get(i).getGeneralNodeTypeStored().contentEquals("END_POINT"))
         {
           remoteNodes.get(i).showSetErrorMessage = false;
           remoteNodes.get(i).setPairingGeneralPassword(newPassword);
           remoteNodes.get(i).showSetErrorMessage = true;
           try {
             Thread.sleep(1000);
           } catch (InterruptedException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
           }
         }
         else
         {
           master = remoteNodes.get(i);
         }
       }
       if (master != null)
       {
         master.showSetErrorMessage = false;
         master.setPairingGeneralPassword(newPassword);
         master.showSetErrorMessage = true;
       }
       getLocalNode().setPairingGeneralPassword(newPassword);
       return true;
     }    
   }
   
   public boolean setPhyModeID(String mode) 
   {
     ArrayList<SctDevice> remoteNodes = getRemoteNodes();
     SctDevice master = null;
     
     if (refreshAll() == false)
     {
       JOptionPane.showMessageDialog(InitApp.top, "Profile cannot be changed.\nOne of the nodes did not reply.", "Spirit Config Tool Error", JOptionPane.ERROR_MESSAGE);
       return false;
     }
     else
     {
       for (int i = 0; i < remoteNodes.size(); i++)
       {
         if (remoteNodes.get(i).getGeneralNodeTypeStored().contentEquals("END_POINT"))
         {
           remoteNodes.get(i).showSetErrorMessage = false;
           remoteNodes.get(i).setPhyModeID(mode);
           remoteNodes.get(i).showSetErrorMessage = true;
         }
         else
         {
           master = remoteNodes.get(i);
         }
       }
       if (master != null)
       {
         master.showSetErrorMessage = false;
         master.setPhyModeID(mode);
         master.showSetErrorMessage = true;
       }
       getLocalNode().setPhyModeID(mode);
       return true;
     }    
   }
 
   public boolean setExtendedSeedIdx(String idx) 
   {
     ArrayList<SctDevice> remoteNodes = getRemoteNodes();
     SctDevice master = null;
     
     if (refreshAll() == false)
     {
       JOptionPane.showMessageDialog(InitApp.top, "Seed Idx cannot be changed.\nOne of the nodes did not reply.", "Spirit Config Tool Error", JOptionPane.ERROR_MESSAGE);
       return false;
     }
     else
     {
       for (int i = 0; i < remoteNodes.size(); i++)
       {
         if (remoteNodes.get(i).getGeneralNodeTypeStored().contentEquals("END_POINT"))
         {
           remoteNodes.get(i).showSetErrorMessage = false;
           remoteNodes.get(i).setExtendedSeedIdx(idx);
           remoteNodes.get(i).showSetErrorMessage = true;
         }
         else
         {
           master = remoteNodes.get(i);
         }
       }
       if (master != null)
       {
         master.showSetErrorMessage = false;
         master.setExtendedSeedIdx(idx);
         master.showSetErrorMessage = true;
       }
       getLocalNode().setExtendedSeedIdx(idx);
       return true;
     }
   }
 
   public static boolean checkMacAddressFormat(String text) 
   {
 
     String validch = "0123456789abcdefABCDEF";
     if (text.length()==17)
     {
       if (validch.indexOf(text.charAt(0)) != -1)
       {
         if (validch.indexOf(text.charAt(1)) != -1)
         {
           if (':' == text.charAt(2))
           {
             if (validch.indexOf(text.charAt(3)) != -1)
             {
               if (validch.indexOf(text.charAt(4)) != -1)
               {
                 if (':' == text.charAt(5))
                 {
                   if (validch.indexOf(text.charAt(6)) != -1)
                   {
                     if (validch.indexOf(text.charAt(7)) != -1)
                     {
                       if (':' == text.charAt(8))
                       {
                         if (validch.indexOf(text.charAt(9)) != -1)
                         {
                           if (validch.indexOf(text.charAt(10)) != -1)
                           {
                             if (':' == text.charAt(11))
                             {
                               if (validch.indexOf(text.charAt(12)) != -1)
                               {
                                 if (validch.indexOf(text.charAt(13)) != -1)
                                 {
                                   if (':' == text.charAt(14))
                                   {
                                     if (validch.indexOf(text.charAt(15)) != -1)
                                     {
                                       if (validch.indexOf(text.charAt(16)) != -1)
                                       {
                                         return true;
                                       }
                                     }
                                   }
                                 }
                               }
                             }
                           }
                         }
                       }
                     }
                   }
                 }
               }
             }
           }
         }
       }
     }
     return false;
   }
 }
 