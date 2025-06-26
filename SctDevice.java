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



import java.awt.Cursor;

import java.util.ArrayList;

import java.util.Arrays;

import java.util.Collections;

import java.util.Hashtable;

import java.util.Iterator;

import java.util.List;

import java.util.SortedSet;

import java.util.StringTokenizer;

import java.util.TreeSet;

import java.util.concurrent.ExecutionException;



import javax.swing.JOptionPane;



import conversion.Conversion;

import deklib.DekDevice;

import deklib.DekParameter;

import gui.JDialogNodeSelect;

import gui.JDialogProfileSelect;

import main.InitApp;

import CustomTypes.NodeElement;



public class SctDevice extends DekDevice {



  public DekParameter[] lastGetAllConfiglayerParameters = null;

  public String setErrorMessage = null;

  public boolean refreshFailed = true;

  public boolean selected = false;

  public boolean isLocal = false;

  public boolean showSetErrorMessage = true;

    

  public SctDevice(String iface, int vlanid, byte[] mac) {

    super(iface, vlanid, mac);

  }

  

  public SctDevice(String ipv4) {

    super(ipv4);

  }



  @Override

  public String toString()

  {

    String result = null;

    

    if (hasFirstGetAllDone())

    {

      if (refreshFailed)

      {

        result =

            "<html>&nbsp;<font size=5><b>"+getMacAsString()+"</b></font><br>"

          + "&nbsp;&nbsp;&nbsp;<font color=red><b>Refresh failed!</b></font><br>"

          + " <br>"

          + " <br>"

          + " <br>"

          + " <br>";

      }

      else

      {

        result =

                "<html>&nbsp;<font size=5><b>"+getMACAddrStored()+"</b></font><br>"

              + "&nbsp;&nbsp;&nbsp;Profile: <b>"+MarvellNet.phyModeIdToProfile(getPhyModeIDStored())+"</b><br>"

              + "&nbsp;&nbsp;&nbsp;HW product: <b>"+getHWProductStored()+"</b><br>"

              + "&nbsp;&nbsp;&nbsp;Acting as: <b>"+getGeneralNodeTypeStored()+"</b><br>"

              + "&nbsp;&nbsp;&nbsp;IPv4 address: <b>"+getIpv4AddressStored()+"</b><br>";

        if (isLegacy())

        {

          result += "&nbsp;&nbsp;&nbsp;<i>SCP only (no LCMP)</i><br>";

        }

        else if (isHttp())

        {

          result += "&nbsp;&nbsp;&nbsp;<i>HTTP connection</i><br>";

        }

        else

        {

          result += "&nbsp;&nbsp;&nbsp;<i>LCMP supported</i><br>";

        }

      }

    }

    else

    {      

      result =

          "<html>&nbsp;<font size=5><b>"+getMacAsString()+"</b></font><br>"

        + "&nbsp;&nbsp;&nbsp;<i>Click here to get info</i><br>"

        + " <br>"

        + " <br>"

        + " <br>"

        + " <br>";

    }

    return result;

  }

  

  public boolean refresh()

  {   

    DekParameter[] getall = null;

    InitApp.top.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

    if (MarvellNet.lcmpAllowed)

    {

      if (isLegacy() && hasFirstGetAllDone() == false)

      {

        checkType(MarvellNet.cfgPassword);

      }

    }

    if (!MarvellNet.scpAllowed && isLegacy())

    {

      getall = null;

    }

    else

    {

      int retries = 4;

      while (getall == null && retries-- > 0)

      {

        getall = getAllConfiglayer(MarvellNet.cfgPassword);

        if (getall == null)

        {

          System.err.println("Refresh failed ("+retries+")");

          try {

            Thread.sleep(1000);

          } catch (InterruptedException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

          }

        }

      }

    }

    InitApp.top.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));



    if (getall != null)

    {

      for (int i=0; i < getall.length; i++)

      {

        if (getall[i].name.contentEquals("SYSTEM.PRODUCTION.MAC_ADDR"))

        {

          lastGetAllConfiglayerParameters = getall;

          refreshFailed = false;

          return true;

        }

      }

    }

    refreshFailed = true;

    return false;

  }  

  

  private void updateStoredValue(String name, String value)

  {

    for (int i=0; i<lastGetAllConfiglayerParameters.length; i++)

    {

      if (lastGetAllConfiglayerParameters[i].name.contentEquals(name))

      {

        lastGetAllConfiglayerParameters[i].value = value;

      }

    }

  }

  

  private void updateStoredValue(String[] names, String[] values)

  {

    for (int j=0; j<names.length; j++)

    {

      for (int i=0; i<lastGetAllConfiglayerParameters.length; i++)

      {

        if (lastGetAllConfiglayerParameters[i].name.contentEquals(names[j]))

        {

          lastGetAllConfiglayerParameters[i].value = values[j];

        }

      }

    }

  }

  

  static public SctDevice createFromDiscover(int vlanid)

  {

    DekDevice[] devices = DekDevice.discover(vlanid);

    

    if (devices != null)

    {

      

      int sel = 0;

      if (devices.length > 1)

      {

        String[] macs = new String[devices.length];

        for (int i=0; i<devices.length; i++)

        {

          macs[i] = devices[i].getMacAsString()+"   "+devices[i].iface;

        }



        JDialogNodeSelect dialog = new JDialogNodeSelect(macs);

        dialog.setLocationRelativeTo(null);

        dialog.setVisible(true);

        if (dialog.ok)

        {

          sel = dialog.selected;

        }

      }

      SctDevice newdevice = new SctDevice(devices[sel].iface, devices[sel].vlanid, devices[sel].mac);

      newdevice.type = devices[sel].type;

      return newdevice;

    }

    else

    {

      return null;

    }

  }

  

  public boolean hasFirstGetAllDone()

  {

    return (lastGetAllConfiglayerParameters != null);

  }

  

  /**

   * Returns an Enumeration<String> containing the names of ConfigLayer parameters stored in this GhnDevice instance.

   * @return

   */

  public SortedSet<String> getAllStoredConfigLayerParamNames()

  {

    SortedSet<String> orderedParamNames=new TreeSet();

    if (lastGetAllConfiglayerParameters != null)

    {

      for (int i=0; i < lastGetAllConfiglayerParameters.length; i++)

      {

        orderedParamNames.add(lastGetAllConfiglayerParameters[i].name);

      }

    }

    return(orderedParamNames);

  }

  

  public DekParameter[] getAllStoredConfiglayer() {

    return lastGetAllConfiglayerParameters;

  }



  /** This just does a simple get with one parameter.

   * @return true if the device is still alive.

   */

  public boolean isAlive()

  {

    return (this.getConfiglayer(MarvellNet.cfgPassword, "SYSTEM.GENERAL.UPTIME") != null);

  }

  

  public boolean getTopologyInformation(boolean running, String [] params, Hashtable<String, String> values)

  {

    DekParameter param;

    boolean      ret = true;



    for (int i=0; i < params.length; i++)

    {

      if (running == true)

      {

        param = this.getConfiglayer(MarvellNet.cfgPassword, params[i]);

        if (param != null)

        {

          values.put(params[i], param.value);      

        }

        else

        {

          ret = false;

        }

      }

      else

      {

        String value;  

        value = getStoredConfigLayerParamValue(params[i]);

        if (value != null)

        {

          values.put(params[i], value);

        }

        else

        {

          ret = false;

        }

      }

    }



    return ret;

  }



  /** This just does a simple get with upgrade status parameter.

   * @return Upgrade Status if the device replies, null otherwise.

   */

  public String getRuntimeUpgradeStatus()

  {

    DekParameter param;

    String ret = null;



    param = this.getConfiglayer(MarvellNet.cfgPassword, "FLUPGRADE.GENERAL.STATUS");

    if (param != null)

    {

      ret = param.value;

    }



    return ret;

  }



  //////////////////////////////////////////////////////////

  /// GET FUNCTIONS

  //////////////////////////////////////////////////////////

  

  /**

   * Returns String containing the value of a ConfigLayer parameters stored in this GhnDevice instance.

   * @param paramName

   * @return

   */

  public String getStoredConfigLayerParamValue(String paramName)

  {

    String value=null;

    if (lastGetAllConfiglayerParameters != null)

    {

      for (int i=0; i < lastGetAllConfiglayerParameters.length; i++)

      {

        if (lastGetAllConfiglayerParameters[i].name.contentEquals(paramName))

        {

          value = lastGetAllConfiglayerParameters[i].value;

          break;

        }

      }

    }

    return(value);

  }

  

  /**

   * Recovers current FW Version running on the device

   * @return FW Version String or null if it is unknown

   */

  public String getFWVersionStored()

  {

    return getStoredConfigLayerParamValue("SYSTEM.GENERAL.FW_VERSION");

  }

  

  public String getASICVersionStored (){

    String sAsicVersion = getStoredConfigLayerParamValue("SYSTEM.GENERAL.ASIC_REV");

    return sAsicVersion;

  }

  

  /**

   * Retrieves currently stored (value in the device for last GET) for "SYSTEM.GENERAL.ASIC"

   * @return

   */

  public String getAsicStored (){

    String sAsic = getStoredConfigLayerParamValue("SYSTEM.GENERAL.ASIC");

    return sAsic;

  }

  

  /**

   * Retrieves currently stored (value in the device for last GET) for "SYSTEM.PRODUCTION.SERIAL_NUMBER"

   * @return

   */

  public String getSerialNumberStored(){

    String sSerialNumber = getStoredConfigLayerParamValue("SYSTEM.PRODUCTION.SERIAL_NUMBER");

    return sSerialNumber;

  }

  

  public String getMACAddrStored(){

    String sMacAddress = getStoredConfigLayerParamValue("SYSTEM.PRODUCTION.MAC_ADDR");

    return(sMacAddress.toUpperCase());

  }

    

  public String getStoredPairingPasswordString(){

    String sPairingPasswordString = getStoredConfigLayerParamValue("PAIRING.GENERAL.PASSWORD");

    return(sPairingPasswordString);

  }

  

  public String getProductionUnlock0PasswordStored() {

    String s = getStoredConfigLayerParamValue("SYSTEM.PRODUCTION.SECTOR0_UNLOCK_PASSWORD");

    return s;

  }

  /**

   * Retrieves currently stored (value in the device for last GET) for "NODE.GENERAL.DOMAIN_NAME"

   * @return

   */

  public String getDomainNameStored(){

    

    String sDomainName =  getStoredConfigLayerParamValue("NODE.GENERAL.DOMAIN_NAME"); 

    return sDomainName;

  }

  

  

  /**

   * Retrieves currently stored (value in the device for last GET) for "POWERMASK.VENDOR.NOTCHES"

   * @return

   */

  public String getPowerMaskVendorNotchesStored(){

    String s3=getStoredConfigLayerParamValue("POWERMASK.VENDOR.NOTCHES");

    return(s3);

  }

  

  

  /**

   * Retrieves currently stored (value in the device for last GET) for "POWERMASK.USER.NOTCHES"

   * @return

   */

  public String getPowerMaskUserNotchesStored(){

    String s3=getStoredConfigLayerParamValue("POWERMASK.USER.NOTCHES");

    return(s3);

  }

  

  /**

   * Retrieves currently stored (value in the device for last GET) for "POWERMASK.REGULATION.NOTCHES"

   * @return

   */

  public String getPowerMaskRegulationNotchesStored(){

    String s3=getStoredConfigLayerParamValue("POWERMASK.REGULATION.NOTCHES");

    return(s3);

  }

  

  /**

   * Retrieves currently stored (value in the device for last GET) for "POWERMASK.REGULATION.NOTCHES_ENABLE"

   * @return

   */

  public String getPowerMaskRegulationNotchesStatusStored(){

    String sRegNotchesStatus = getStoredConfigLayerParamValue("POWERMASK.REGULATION.NOTCHES_ENABLE");

    return sRegNotchesStatus; 

  }

  

  /**

   * Retrieves currently stored (value in the device for last GET) for "POWERMASK.VENDOR.NOTCHES_ENABLE"

   * @return

   */

  public String getPowerMaskVendorNotchesStatusStored () {

    String sVendorNotchesStatus = getStoredConfigLayerParamValue("POWERMASK.VENDOR.NOTCHES_ENABLE");

    return sVendorNotchesStatus;  

  }

  

  /**

   * Retrieves currently stored (value in the device for last GET) for "POWERMASK.USER.NOTCHES_ENABLE"

   * @return

   */

  public String getPowerMaskUserNotchesStatusStored () {

    String sUserNotchesStatus  = getStoredConfigLayerParamValue("POWERMASK.USER.NOTCHES_ENABLE");

    return sUserNotchesStatus;  

  }

  /**

   * Retrieves currently stored (value in the device for last GET) for "MCAST.GENERAL.IGMP_ENABLE"

   * @return

   */

  public String getIGMPEnableValueStored(){

    String sIGMPEnable  = getStoredConfigLayerParamValue("MCAST.GENERAL.IGMP_ENABLE");

    return sIGMPEnable; 

  }

  

  /**

   * Retrieves currently stored (value in the device for last GET) for "MCAST.GENERAL.MLD_ENABLE"

   * @return

   */

  public String getMLDStatusValueStored() {

    String sMLDStatus = getStoredConfigLayerParamValue("MCAST.GENERAL.MLD_ENABLE");

    return sMLDStatus;

  }

  

  /**

   * Retrieves currently stored (value in the device for last GET) for "MCAST.GENERAL.REPORT_BROADCAST_ALLOWED"

   * @return

   */

  public String getReportBroadcastAllowedStored () {

    String sDropAllowedStatus = getStoredConfigLayerParamValue("MCAST.GENERAL.REPORT_BROADCAST_ALLOWED");

    return sDropAllowedStatus;

  }

  

  public String getReportBroadcastModeStored() {

    String s = getStoredConfigLayerParamValue("MCAST.GENERAL.REPORT_BROADCAST_MODE");

    return s; 

  }

  public String getMcastFilteringEnabledStored () {

    String s = getStoredConfigLayerParamValue("MCAST.GENERAL.MCAST_FILTERING_ENABLE");

    return s;

  }

  

  public String getMcastFastleaveModeEnabledStored () {

    String s = getStoredConfigLayerParamValue("MCAST.GENERAL.FAST_LEAVE_ENABLE");

    return s;

  }



  public String getMcastVideoSourceModeStored () {

    String s = getStoredConfigLayerParamValue("MCAST.GENERAL.VIDEO_SOURCE_MODE");

    return s;

  }

  

  /**

   * Retrieves currently stored (value in the device for last GET) for "MCAST.GENERAL.IGMP_IP_RANGES_DEF"

   * @return

   */

  public String getIGMPIPRangesDefinedStored () {

    String sIGMPRanges = getStoredConfigLayerParamValue("MCAST.GENERAL.IGMP_IP_RANGES_DEF");

    return sIGMPRanges;

  }

  

  /**

   * Retrieves currently stored (value in the device for last GET) for "PHYMNG.GENERAL.RUNNING_PHYMODE_ID"

   * @return

   */

  public String getPhyModeIDStored(){

    String sPhyMode = getStoredConfigLayerParamValue("PHYMNG.GENERAL.RUNNING_PHYMODE_ID");

    return sPhyMode;

  }

    

  /**

   * Retrieves currently stored (value in the device for last GET) for "NODE.GENERAL.DOMAIN_ID_MODE"

   * @return

   */

  public String getDomainIDModeStored(){

    String sPhyMode = getStoredConfigLayerParamValue("NODE.GENERAL.DOMAIN_ID_MODE");

    return sPhyMode;

  }

  

  /**

   * Retrieves currently stored (value in the device for last GET) for "NODE.GENERAL.EXTENDED_SEED_IDX"

   * @return

   */

  public String getExtendedSeedStored(){

    String sPhyMode = getStoredConfigLayerParamValue("NODE.GENERAL.EXTENDED_SEED_IDX");

    return sPhyMode;

  }

  

  /**

   * Retrieves currently stored (value in the device for last GET) for "PHYMNG.GENERAL.WIRE_LENGTH"

   * @return

   */

  public String getWireLengthStored(){

    String s = getStoredConfigLayerParamValue("PHYMNG.GENERAL.WIRE_LENGTH");

    return s;

  }

  

  /**

   * Retrieves currently stored (value in the device for last GET) for "QOS.SCHED.DS_US_RATE"

   * @return

   */

  public String getDsUsRateStored(){

    String s = getStoredConfigLayerParamValue("QOS.SCHED.DS_US_RATE");

    return s;

  }

  

  /**

   * Retrieves currently stored (value in the device for last GET) for "NODE.GENERAL.DOMAIN_ID"

   * @return

   */

  public String getDomainIDStored(){

    String sDomainID = getStoredConfigLayerParamValue("NODE.GENERAL.DOMAIN_ID");

    return sDomainID;

  }

  

  /**

   * Retrieves currently stored (value in the device for last GET) for "AGC.GENERAL.TX_GAIN_CH1"

   * @return

   */

  public String getTXGainCH1Stored() {

    String sTxGain1 = getStoredConfigLayerParamValue("AGC.GENERAL.TX_GAIN_CH1");

    return sTxGain1;

  }

  

  /**

   * Retrieves currently stored (value in the device for last GET) for "PAIRING.GENERAL.PASSWORD"

   * @return

   */

  public String getPairingPasswordStringStored (){

    String sPairingPasswordString = getStoredConfigLayerParamValue("PAIRING.GENERAL.PASSWORD");

    return sPairingPasswordString;

  }

  

  /**

   * Retrieves currently stored (value in the device for last GET) for "NODE.GENERAL.DOMAIN_MASTER_MAC_ADDR"

   * @return

   */

  public String getDomainMasterMACStored(){

    String sDMmac = getStoredConfigLayerParamValue("NODE.GENERAL.DOMAIN_MASTER_MAC_ADDR");

    return sDMmac.toUpperCase();

  }

  

  /**

   * Retrieves currently stored (value in the device for last GET) for "NODE.GENERAL.NODE_TYPE"

   * @return

   */

  public String getGeneralNodeTypeStored (){

    String sNodeType = getStoredConfigLayerParamValue("NODE.GENERAL.NODE_TYPE");

    return sNodeType;

  }

  /**

   * Retrieves currently stored (value in the device for last GET) for "SYSTEM.PRODUCTION.HW_PRODUCT"

   * @return

   */

  public String getHWProductStored (){

    String sHWProduct = getStoredConfigLayerParamValue("SYSTEM.PRODUCTION.HW_PRODUCT");

    return sHWProduct;

  }

  

  /**

   * Retrieves currently stored (value in the device for last GET) for "SYSTEM.PRODUCTION.HW_REVISION"

   * @return

   */

  public String getHWRevisionStored (){

    String sHWRevision = getStoredConfigLayerParamValue("SYSTEM.PRODUCTION.HW_REVISION");

    return sHWRevision;

  }

  

  /**

   * Retrieves currently stored (value in the device for last GET) for "MASTERSELECTION.GENERAL.FORCE_NODE"

   * @return

   */

  public String getForceNodeTypeStored (){

    String sForceNodeType = getStoredConfigLayerParamValue("MASTERSELECTION.GENERAL.FORCE_NODE");

    return sForceNodeType;

  }

  

  /**

   * Retrieves currently stored (value in the device for last GET) for "SYSTEM.GENERAL.API_VERSION"

   * @return

   */

  public String getAPIVersionStored (){

    String sAPIVersion = getStoredConfigLayerParamValue("SYSTEM.GENERAL.API_VERSION");

    

    return sAPIVersion;

  }

  

  /**

   * Retrieves currently stored (value in the device for last GET) for "PAIRING.GENERAL.SECURED"

   * @return

   */

  public String getSecuredStored() {

    String sKeysRx = getStoredConfigLayerParamValue("PAIRING.GENERAL.SECURED");

    return sKeysRx;

  }

  

  /**

   * Retrieves currently stored (value in the device for last GET) for "PAIRING.GENERAL.PROCESS_START"

   * @return

   */

  public boolean getPairingRunningStored() {

    String sStart = getStoredConfigLayerParamValue("PAIRING.GENERAL.PROCESS_START");

    if (sStart == null)

    {

      return false;

    }

    else 

    {

      return (sStart.contentEquals("1")?true:false);

    }

  }



  /**

   * Retrieves currently stored (value in the device for last GET) for "FLUPGRADE.GENERAL.ONLY_EXTERNAL_MANAGED"

   * @return

   */

  public String getUpgradeExternalManagedStored() {

    String value=getStoredConfigLayerParamValue("FLUPGRADE.GENERAL.ONLY_EXTERNAL_MANAGED");

    if (value==null) {

      

      value="NO"; 

    }

    return value;

  }

  

  public ArrayList<byte[]> getMarvellGhnSlaveMACsStored(boolean filterZeros){

    ArrayList<byte[]> slave_macs=null;

    String paramValue = getStoredConfigLayerParamValue("DIDMNG.GENERAL.MACS");

    String twopointsSeparatedStringMACs[];

  

    if(paramValue!=null){

      paramValue=paramValue.toUpperCase();

      paramValue=paramValue.replace(":", "");//NO : anymore here

      //System.out.println("DIDMNG.GENERAL.MACS Value:"+paramValue);

      if(!paramValue.isEmpty()){

        

        if(paramValue.contains(",")){//Serveral MACs reported 

          

          twopointsSeparatedStringMACs=paramValue.split(",");

          slave_macs=new ArrayList<byte[]>();

          for(int i=0;i<twopointsSeparatedStringMACs.length;i++)

          {

          	boolean filter_mac = false;

          	

          	if ((filterZeros == true) && (twopointsSeparatedStringMACs[i].equalsIgnoreCase("000000000000") == true))

          	{

          		filter_mac = true; 

          	}

          	

          	if (filter_mac == false)

          	{

          		//twopointsSeparatedStringMACs[i]=twopointsSeparatedStringMACs[i].replace(":", "");

          		//System.out.println("Processing slave candidate: "+twopointsSeparatedStringMACs[i]+" against "+getMACAddrStored().replace(":", ""));

          		if(!twopointsSeparatedStringMACs[i].equals(getMACAddrStored())){//It is not our MAC

          			//System.out.println("Added slave: "+twopointsSeparatedStringMACs[i]);

          			slave_macs.add(Conversion.stringToByteArray(twopointsSeparatedStringMACs[i]));

          		}

          	}

          }

          

        }

        else

        {

        	//One single MAC reported

        	boolean filter_mac = false;

        	

        	if ((filterZeros == true) && (paramValue.equalsIgnoreCase("000000000000") == true))

        	{

        		filter_mac = true; 

        	}

        	

        	if (filter_mac == false)

        	{

        		slave_macs=new ArrayList<byte[]>(1);

        		if(!paramValue.equals(getMACAddrStored())){//It is not our MAC

        			slave_macs.add(Conversion.stringToByteArray(paramValue));

        		}

        	}

          

        }

      }

      

      

      

    }

    //System.out.println("DIDMNG.GENERAL.MACS CLParam:"+configLayerParam);

    return(slave_macs);

  }



  public ArrayList<byte[]> getMarvellGhnSlaveMACsStored(){

  	return getMarvellGhnSlaveMACsStored(true);

  }



  /**

   * Recovers stored <String MAC, String DID> for this device

   * @return

   */

  public Hashtable<String,Integer> getDIDSTableStored(){

    

    Hashtable<String,Integer> htDIDS = new Hashtable<String,Integer>();

    String s1 = getStoredConfigLayerParamValue("DIDMNG.GENERAL.DIDS");

    //StringTokenizer stkDIDS = new StringTokenizer(s1, ",");

    String[] stkDIDS=null;

    

    if(!s1.isEmpty())

    {

      if(s1.contains(",")){

        stkDIDS=s1.split(",");

      }else{

        stkDIDS=new String[1];

        stkDIDS[0]=s1;

      } 



      ArrayList<byte[]> remoteMacs = getMarvellGhnSlaveMACsStored(false);

      

      //Tricky check: Always a MAC is reported in entry .1

      if ((remoteMacs != null) && (remoteMacs.size() > 1))

      {

        String currentRemoteMac;



        for (int i=0; i<remoteMacs.size(); i++)

        {

          currentRemoteMac= Conversion.printByteArrayToHexString2PointsSeparated(remoteMacs.get(i));

          if (!currentRemoteMac.contentEquals("00:00:00:00:00:00"))

          {

          	if (i < stkDIDS.length)

          	{

            	//Indirect nodes will have DID 0. This will help us to filter indirect nodes.

              htDIDS.put(currentRemoteMac, Integer.parseInt(stkDIDS[i]));

          	}          	

          }

        }      	      	

      }

      

    }

    

    return htDIDS;

  }

  

  /**

   * Recovers stored <String DId, String TXBPS> for this device

   * @return

   */

  public Hashtable<Integer,String> getTxPHYStored(){

    

    Hashtable<Integer,String> htTxSpeed = new Hashtable<Integer,String>();

    String s1 = getStoredConfigLayerParamValue("DIDMNG.GENERAL.DIDS");

    String s2 = getStoredConfigLayerParamValue("DIDMNG.GENERAL.TX_BPS");

    

    String[] stkDIDS=null;// = s1.split(",");

    String[] txPhys=null; //s2.split(",");

    

    if(!s1.isEmpty()){

      if(s1.contains(",")){

        stkDIDS=s1.split(",");

      }else{

        stkDIDS=new String[1];

        stkDIDS[0]=s1;

      } 

    }

    else

    {

      System.err.println("No DIDS array!");

      return null;

    }

    

    if(!s2.isEmpty()){

      if(s2.contains(",")){

        txPhys=s2.split(",");

      }else{

        txPhys=new String[1];

        txPhys[0]=s2;

      } 

    }

    else

    {

      System.err.println("No TX_BPS array!");

      return null;

    }

    

    if (txPhys.length == stkDIDS.length)

    {

      for (int i = 0; i < stkDIDS.length; i++)

      {

        if (!stkDIDS[i].contentEquals("0"))

        {

          htTxSpeed.put(Integer.parseInt(stkDIDS[i]), txPhys[i]);

        }

      }

    }

    else

    {

      System.err.println("Invalid TX_BPS info!");

      return null;

    }

    

    return htTxSpeed;

  }

  

  /**

   * Recovers stored <String DId, String RXBPS> for this device

   * @return

   */

  public Hashtable<Integer,String> getRxPHYStored(){

    

    Hashtable<Integer,String> htRxSpeed = new Hashtable<Integer,String>();

    String s1 = getStoredConfigLayerParamValue("DIDMNG.GENERAL.DIDS");

    String s2 = getStoredConfigLayerParamValue("DIDMNG.GENERAL.RX_BPS");

    

    String[] stkDIDS=null;// = s1.split(",");

    String[] rxPhys=null; //s2.split(",");

    

    if(!s1.isEmpty()){

      if(s1.contains(",")){

        stkDIDS=s1.split(",");

      }else{

        stkDIDS=new String[1];

        stkDIDS[0]=s1;

      } 

    }

    else

    {

      System.err.println("No DIDS array!");

      return null;

    }

    

    if(!s2.isEmpty()){

      if(s2.contains(",")){

        rxPhys=s2.split(",");

      }else{

        rxPhys=new String[1];

        rxPhys[0]=s2;

      } 

    }

    else

    {

      System.err.println("No TX_BPS array!");

      return null;

    }

    

    if (rxPhys.length == stkDIDS.length)

    {

      for (int i = 0; i < stkDIDS.length; i++)

      {

        if (!stkDIDS[i].contentEquals("0"))

        {

          htRxSpeed.put(Integer.parseInt(stkDIDS[i]), rxPhys[i]);

        }

      }

    }

    else

    {

      System.err.println("Invalid RX_BPS info!");

      return null;

    }

    

    return htRxSpeed;

  }

  

  /**

   * Recovers stored <String SID, String StampMs, String RXFEC, String ERROR%> for this device

   * @return

   */

  public Hashtable<Integer,String[]> getRxTxputAndErrorsStored(){

    

    Hashtable<Integer,String[]> ht = new Hashtable<Integer,String[]>();

    String sLinkStatus = getStoredConfigLayerParamValue("FLOWMONITOR.STATS.LINK_STATUS");

    String sAll[]=sLinkStatus.split(",");

    

    for (int i = 0; i < sAll.length; i+=7)

    {

      String sSid = sAll[i+2];

      String[] sData = new String[3];

      sData[0] = sAll[i+1]; // Timestamp (ms)

      sData[1] = sAll[i+4]; // Rx FEC

      sData[2] = sAll[i+5]; // Rx FEC Error %

      ht.put(Integer.parseInt(sSid), sData);

      

    }

    return ht;

  }

  

  /**

   * Retrieves currently stored (value in the device for last GET) for "NODE.GENERAL.DEVICE_ID"

   * @return

   */

  public String getDeviceIDStored (){

    String sDeviceID =  getStoredConfigLayerParamValue("NODE.GENERAL.DEVICE_ID");

    return sDeviceID;

  }

  

  /**

   * Retrieves currently stored (value in the device for last GET) for "DHCP.GENERAL.ENABLED_IPV4"

   * @return

   */

  public String getDHCPIpv4ClientStatusStored(){

    String sDhcpIpv4ClientStatus = getStoredConfigLayerParamValue("DHCP.GENERAL.ENABLED_IPV4");

    return sDhcpIpv4ClientStatus;

  }

  

  /**

   * Retrieves currently stored (value in the device for last GET) for "TCPIP.IPV4.IP"

   * @return

   */

  public String getIpv4AddressStored(){

    String sIpv4Address= getStoredConfigLayerParamValue("TCPIP.IPV4.IP");

    if (sIpv4Address == null)

    {

      sIpv4Address = getStoredConfigLayerParamValue("TCPIP.IPV4.IP_ADDRESS");

    }

    return sIpv4Address;

  }

  

  /**

   *  Retrieves currently stored (value in the device for last GET) for "TCPIP.IPV4.NETMASK"

   * @return

   */

  public String getSubnetMaskStored(){

    String sNetmask= getStoredConfigLayerParamValue("TCPIP.IPV4.NETMASK");

    if (sNetmask == null)

    {

      sNetmask= getStoredConfigLayerParamValue("TCPIP.IPV4.IP_NETMASK");

    }

    return sNetmask;

  }

  

  /**

   * Retrieves currently stored (value in the device for last GET) for "TCPIP.IPV4.GATEWAY_IP" if exists, "TCPIP.IPV4.GATEWAY" otherwise

   * @return

   */

  public String getdefaultGateWayStored(){

    String sDefaultGw;

    sDefaultGw=getStoredConfigLayerParamValue("TCPIP.IPV4.GATEWAY_IP");

    if(sDefaultGw==null){

      sDefaultGw= getStoredConfigLayerParamValue("TCPIP.IPV4.GATEWAY");

    }

    

    return sDefaultGw;

  }

  

  /**

   * Retrieves currently stored (value in the device for last GET) for "DNS.GENERAL.IPV4"

   * @return

   */

  public String getDnsIpv4AddressStored(){

    String sDnsIpv4Address = getStoredConfigLayerParamValue("DNS.GENERAL.IPV4");

    return sDnsIpv4Address;

  }

  

  /**

   * Retrieves currently stored (value in the device for last GET) for "TCPIP.IPV4.ADDITIONAL_IP_ADDRESS"

   * @return

   */

  public String getIpv4AddressAdditionalStored(){

    String sIpv4AddressAdd= getStoredConfigLayerParamValue("TCPIP.IPV4.ADDITIONAL_IP_ADDRESS");

    return sIpv4AddressAdd;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "TCPIP.IPV4.ADDITIONAL_IP_NETMASK" as String

   * @return

   */

  public String getSubnetMaskAdditionalStored(){

    String sNetmaskAdd= getStoredConfigLayerParamValue("TCPIP.IPV4.ADDITIONAL_IP_NETMASK");

    return sNetmaskAdd;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "DHCP.GENERAL.ENABLED_IPV6" as String

   * @return

   */

  public String getDHCPIpv6ClientStatusStored(){

    String sDhcpIpv6ClientStatus = getStoredConfigLayerParamValue("DHCP.GENERAL.ENABLED_IPV6");

    return sDhcpIpv6ClientStatus;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "TCPIP.IPV6.IP_ADDRESS" or "TCPIP.IPV6.DHCP_ADDRESS" as String

   * @return

   */

  public String getIpv6AddressStored(){

    String sIpv6Address=null;

    sIpv6Address=getStoredConfigLayerParamValue("TCPIP.IPV6.IP_ADDRESS");

    if(sIpv6Address==null){

      sIpv6Address = getStoredConfigLayerParamValue("TCPIP.IPV6.DHCP_ADDRESS");

    }

    

    return sIpv6Address;

  } 

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "TCPIP.IPV6.IP_PREFIX" as String

   * @return

   */

  public String getIpv6PrefixStored(){

    String sIpv6Prefix = getStoredConfigLayerParamValue("TCPIP.IPV6.IP_PREFIX");

    return sIpv6Prefix;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "TCPIP.IPV6.GATEWAY" as String

   * @return

   */

  public String getGWIpv6AddressStored(){

    String sGWIpv6Address = getStoredConfigLayerParamValue("TCPIP.IPV6.GATEWAY");

    return sGWIpv6Address;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "TCPIP.IPV6.LINK_LOCAL_IP_ADDRESS" or "TCPIP.IPV6.LINK_LOCAL_ADDRESS" as String

   * @return

   */

  public String getLocalIpv6AddressStored(){

    String sLocalIpv6Address=null;

    sLocalIpv6Address=getStoredConfigLayerParamValue("TCPIP.IPV6.LINK_LOCAL_IP_ADDRESS");

    if(sLocalIpv6Address==null){

      sLocalIpv6Address = getStoredConfigLayerParamValue("TCPIP.IPV6.LINK_LOCAL_ADDRESS");

    }

    

    return sLocalIpv6Address;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "TCPIP.IPV6.LINK_LOCAL_IP_PREFIX" as String

   * @return

   */

  public String getLocalIpv6PrefixStored(){

    String sLocalIpv6Prefix = getStoredConfigLayerParamValue("TCPIP.IPV6.LINK_LOCAL_IP_PREFIX");

    return sLocalIpv6Prefix;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "TCPIP.IPV6.SLAAC_IP_ADDRESS" as String

   * @return

   */

  public String getIpv6SlaacAddressStored(){

    String sIpv6SlaacAddress = getStoredConfigLayerParamValue("TCPIP.IPV6.SLAAC_IP_ADDRESS");

    return sIpv6SlaacAddress;

  } 

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "TCPIP.IPV6.SLAAC_IP_PREFIX" as String

   * @return

   */

  public String getIpv6SlaacPrefixStored(){

    String sIpv6SlaacPrefix = getStoredConfigLayerParamValue("TCPIP.IPV6.SLAAC_IP_PREFIX");

    return sIpv6SlaacPrefix;

  } 

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "TCPIP.IPV6.ADDITIONAL_IP_ADDRESS" or "TCPIP.IPV6.MANUAL_ADDRESSES" as String

   * @return

   */

  public String getManualIpv6AddressStored(){

    String sManualIpv6Address=null;

    sManualIpv6Address=getStoredConfigLayerParamValue("TCPIP.IPV6.ADDITIONAL_IP_ADDRESS");

    if(sManualIpv6Address==null){

      sManualIpv6Address=getStoredConfigLayerParamValue("TCPIP.IPV6.MANUAL_ADDRESSES");

    }

    

    return sManualIpv6Address;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "TCPIP.IPV6.ADDITIONAL_IP_PREFIX" as String

   * @return

   */

  public String getManualIpv6PrefixStored(){

    String sManualIpv6Prefix = getStoredConfigLayerParamValue("TCPIP.IPV6.ADDITIONAL_IP_PREFIX");

    return sManualIpv6Prefix;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "DNS.GENERAL.IPV6" as String

   * @return

   */

  public String getDnsIpv6AddressStored(){

    String sDnsIpv6Address = getStoredConfigLayerParamValue("DNS.GENERAL.IPV6");

    return sDnsIpv6Address;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "SYSTEM.GENERAL.CHIPSET" as String

   * @return

   */

  public String getChipsetStored (){

    String sChipset = getStoredConfigLayerParamValue("SYSTEM.GENERAL.CHIPSET");

    return sChipset;

  }

  

  public String getDeviceNameStored (){

    String sChipset = getStoredConfigLayerParamValue("SYSTEM.PRODUCTION.DEVICE_NAME");

    return sChipset;

  }

  

  public String getDeviceDescriptionStored (){

    String sChipset = getStoredConfigLayerParamValue("SYSTEM.PRODUCTION.DEVICE_DESCRIPTION");

    return sChipset;

  }

  

  public String getDeviceManufacturerStored (){

    String sChipset = getStoredConfigLayerParamValue("SYSTEM.PRODUCTION.DEVICE_MANUFACTURER");

    return sChipset;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "ETHPHYCONF.ETHA.SPEED" as String

   * @return

   */

  public String getEthSpeedStored(int eth){

    String sEthASpeed = getStoredConfigLayerParamValue("ETHPHYCONF.ETH"+(eth==0?"A":"B")+".SPEED");

    return sEthASpeed;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "ETHIFDRIVER.ETHA.MODE" as String

   * @return

   */

  public String getEthModeStored(int eth){

    String sEthAMode = getStoredConfigLayerParamValue("ETHIFDRIVER.ETH"+(eth==0?"A":"B")+".MODE");

    return sEthAMode;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "ETHPHYCONF.ETHA.DUPLEX" as String

   * @return

   */

  public String getEthDuplexStored(int eth){

    String sEthADuplex = getStoredConfigLayerParamValue("ETHPHYCONF.ETH"+(eth==0?"A":"B")+".DUPLEX");

    return sEthADuplex;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "ETHIFDRIVER.ETHA.IFACE_TYPE" as String

   * @return

   */

  public String getEthTypeStored(int eth){

    String sEthAType = getStoredConfigLayerParamValue("ETHIFDRIVER.ETH"+(eth==0?"A":"B")+".IFACE_TYPE");

    return sEthAType;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "ETHIFDRIVER.ETHA.INTERNAL_PHY" as String

   * @return

   */

  public String getEthInternalPhyStored(int eth){

    String sEthAInternalPhy = getStoredConfigLayerParamValue("ETHIFDRIVER.ETH"+(eth==0?"A":"B")+".INTERNAL_PHY");

    return sEthAInternalPhy;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "ETHPHYCONF.ETHA.LINK" as String

   * @return

   */

  public String getEthLinkStored(int eth){

    String sEthALink = getStoredConfigLayerParamValue("ETHPHYCONF.ETH"+(eth==0?"A":"B")+".LINK");

    return sEthALink;

  }

  



  public String getEthEnabledStored(int eth) {

    String s = getStoredConfigLayerParamValue("ETHIFDRIVER.ETH"+(eth==0?"A":"B")+".ENABLED");

    return s;

  }

  

  public String getEthAutonegotationStored(int eth) {

    String s = getStoredConfigLayerParamValue("ETHPHYCONF.ETH"+(eth==0?"A":"B")+".AUTONEG");

    return s;

  }

  

  public String getEthCapabilitiesStored(int eth) {

    String s1000FD = getStoredConfigLayerParamValue("ETHPHYCONF.ETH"+(eth==0?"A":"B")+".CAP_1000_FD");

    String s100FD = getStoredConfigLayerParamValue("ETHPHYCONF.ETH"+(eth==0?"A":"B")+".CAP_100_FD");

    String s100HD = getStoredConfigLayerParamValue("ETHPHYCONF.ETH"+(eth==0?"A":"B")+".CAP_100_HD");

    String s10FD = getStoredConfigLayerParamValue("ETHPHYCONF.ETH"+(eth==0?"A":"B")+".CAP_10_FD");

    String s10HD = getStoredConfigLayerParamValue("ETHPHYCONF.ETH"+(eth==0?"A":"B")+".CAP_10_HD");

    String s = (s1000FD.contentEquals("YES")?"1000FD ":"") + 

        (s100FD.contentEquals("YES")?"100FD ":"") +

        (s100HD.contentEquals("YES")?"100HD ":"") + 

        (s10FD.contentEquals("YES")?"10FD ":"") + 

        (s10HD.contentEquals("YES")?"10HD":"");

    return s;

  }

  

  public String getEthPhyAddrStored(int eth) {

    String s = getStoredConfigLayerParamValue("ETHPHYCONF.ETH"+(eth==0?"A":"B")+".PHY_ADDR");

    return s;

  }

  

  public String getEthPhyIdStored(int eth) {

    String s = getStoredConfigLayerParamValue("ETHPHYCONF.ETH"+(eth==0?"A":"B")+".PHY_ID");

    return s;

  }

  

  public String getEthMiimIfaceStored(int eth) {

    String s = getStoredConfigLayerParamValue("ETHPHYCONF.ETH"+(eth==0?"A":"B")+".MIIM_IFACE");

    return s;

  }

  

  public String getEthPhyModeStored(int eth) {

    String s = getStoredConfigLayerParamValue("ETHPHYCONF.ETH"+(eth==0?"A":"B")+".MODE");

    return s;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "POWERSAVING.GENERAL.MODE" as String

   * @return

   */

  public String getPWSavingStatusStored (){

    String sPWSavingStatus = getStoredConfigLayerParamValue("POWERSAVING.GENERAL.MODE");

    return sPWSavingStatus;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "POWERSAVING.GENERAL.IDLE_TIME" as String

   * @return

   */

  public String getPWSavingTimerStored (){

    String sPWSavingTimer = getStoredConfigLayerParamValue("POWERSAVING.GENERAL.IDLE_TIME");

    return sPWSavingTimer;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "POWERMASK.GENERAL.RAW_POWERMASK" as String

   * @return

   */

  public String[] getRawPowerMaskValuesStored () {

    String sRawPowerMask = getStoredConfigLayerParamValue("POWERMASK.GENERAL.RAW_POWERMASK");

    if (sRawPowerMask != null)

    {

      String separatedValues[]=sRawPowerMask.split(",");

      if (separatedValues.length > 1)

      {

        return separatedValues;

      }

      else

      {

        return null;

      }

    }

    else

    {

      return null;

    }

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "SYNC.STATS.CLOCK_DEVIATION" as String

   * @return

   */

  public String[] getSyncClockDeviationStored () {

    String sClockDeviation = getStoredConfigLayerParamValue("SYNC.STATS.CLOCK_DEVIATION");

    if (sClockDeviation != null)

    {

      String separatedValues[]=sClockDeviation.split(",");

      if (separatedValues.length > 1)

      {

        return separatedValues;

      }

      else

      {

        return null;

      }

    }

    else

    {

      return null;

    }

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "LOGFILE.GENERAL.ENABLE" as String

   * @return

   */

  public String getLogFileStatusStored() {

    String sLogFileStatus = getStoredConfigLayerParamValue("LOGFILE.GENERAL.ENABLE"); 

    return sLogFileStatus;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "LOGFILE.GENERAL.DESTINATION" as String

   * @return

   */

  public String getLogFileDestinationStored() {

    String sLogFileDestination = getStoredConfigLayerParamValue("LOGFILE.GENERAL.DESTINATION"); 

    return sLogFileDestination;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "LOGFILE.GENERAL.DESTINATION" as String

   * @return

   */

  public String getLogFileFtpIntervalStored() {

    String sLogFileFtpInterval = getStoredConfigLayerParamValue("LOGFILE.GENERAL.UPLOAD_INTERVAL"); 

    return sLogFileFtpInterval;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "LOGFILE.GENERAL.DATA_INTERVAL" as String

   * @return

   */

  public String getLogFileDataIntervalStored() {

    String sLogFileDataInterval = getStoredConfigLayerParamValue("LOGFILE.GENERAL.DATA_INTERVAL");  

    return sLogFileDataInterval;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "LOGFILE.FTPSERVER.HOST" as String

   * @return

   */

  public String getLogFileFtpUrlStored() {

    String sLogFileFtpUrl = getStoredConfigLayerParamValue("LOGFILE.FTPSERVER.HOST"); 

    return sLogFileFtpUrl;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "LOGFILE.FTPSERVER.LOGIN" as String

   * @return

   */

  public String getLogFileFtpLoginStored() {

    String sLogFileFtpLogin = getStoredConfigLayerParamValue("LOGFILE.FTPSERVER.LOGIN");  

    return sLogFileFtpLogin;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "LOGFILE.FTPSERVER.PASSWORD" as String

   * @return

   */

  public String getLogFileFtpPwStored() {

    String sLogFileFtpPw = getStoredConfigLayerParamValue("LOGFILE.FTPSERVER.PASSWORD");  

    return sLogFileFtpPw;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "CLDRIVER.BCASTSUP.XPUT" as String

   * @return

   */

  public String getBcastSuppressionStored() {

    String sBcastSuppression = getStoredConfigLayerParamValue("CLDRIVER.BCASTSUP.XPUT");  

    return sBcastSuppression;

  }

  

  /**

   * Checks if exists a ConfigLayer parameter with Name "VLAN.CVLAN.ENABLE"

   * @return

   */

  public String getCVLANEnableStored() {

    String cvlanenable = getStoredConfigLayerParamValue("VLAN.CVLAN.ENABLE");

    return (cvlanenable);

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "VLAN.CVLAN.TAG_ETHA" as String

   * @return

   */

  public String getCVLANEthAValueStored() {

    String sCVLANethAvalue = getStoredConfigLayerParamValue("VLAN.CVLAN.TAG_ETHA"); 

    return sCVLANethAvalue;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "VLAN.CVLAN.TAG_ETHB" as String

   * @return

   */

  public String getCVLANEthBValueStored() {

    String sCVLANethAvalue = getStoredConfigLayerParamValue("VLAN.CVLAN.TAG_ETHB"); 

    return sCVLANethAvalue;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "VLAN.CVLAN.TAG_MGMT_INTERFACE" as String

   * @return

   */

  public String getCVLANFWValueStored() {

    String sCVLANfwvalue = getStoredConfigLayerParamValue("VLAN.CVLAN.TAG_MGMT_INTERFACE"); 

    return sCVLANfwvalue;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "VLAN.CVLAN.TAG_GHN_INTERFACE" as String

   * @return

   */

  public String getCVLANPLCValueStored() {

    String sCVLANplcvalue = getStoredConfigLayerParamValue("VLAN.CVLAN.TAG_GHN_INTERFACE"); 

    return sCVLANplcvalue;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for ""VLAN.CVLAN.TAG_SDIO"" as String

   * @return

   */

  public String getCVLANSDIOValueStored() {

    String sCVLANsdiovalue = getStoredConfigLayerParamValue("VLAN.CVLAN.TAG_SDIO"); 

    return sCVLANsdiovalue;

  }

  

  /**

   * Retrieves currently stored values (value in the device for last GET) for "VLAN.CVLAN.TRUNK_MODE" as String[]

   * @return

   */

  public String[] getCVLANTrunkMode() {

    String sCVLANtrunk = getStoredConfigLayerParamValue("VLAN.CVLAN.TRUNK_MODE"); 

    String[] sValuesSeparated = null;

    if (sCVLANtrunk != null)

    {

      sValuesSeparated=sCVLANtrunk.split(",");

    }



    return sValuesSeparated;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "VLAN.CVLAN.TRUNK_MODE" as String

   * @return

   */

  public String getQoSEnabledStored() {

    String sQoSEnabled = getStoredConfigLayerParamValue("PACKETCLASSIFIER.GENERAL.TYPE_CLASS_MAP_EN");  

    return sQoSEnabled;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "PACKETCLASSIFIER.GENERAL.VLAN_CLASS_MAP_EN" as String

   * @return

   */

  public String get8021pEnabledStored() {

    String s8021pEnabled = getStoredConfigLayerParamValue("PACKETCLASSIFIER.GENERAL.VLAN_CLASS_MAP_EN");  

    return s8021pEnabled;

  }

  

  /**

   * Checks if exists a ConfigLayer parameter with Name "PACKETCLASSIFIER.GENERAL.MATCHING_RULES"

   * @return

   */

  public boolean getQoSpresenceStored() {

    String matchingrules = getStoredConfigLayerParamValue("PACKETCLASSIFIER.GENERAL.MATCHING_RULES");

    return (matchingrules != null);

  }

  

  /**

   * Retrieves currently stored values (value in the device for last GET) for "PACKETCLASSIFIER.GENERAL.MATCHING_RULES" as String[]

   * @return

   */

  public String[] getQoSPacketDetectorStored() {

    String sPacketDetector = getStoredConfigLayerParamValue("PACKETCLASSIFIER.GENERAL.MATCHING_RULES"); 

    String sValuesSeparated[]=sPacketDetector.split(",");

    return(sValuesSeparated);

  }

  

  /**

   * Retrieves currently stored values (value in the device for last GET) for "PACKETCLASSIFIER.GENERAL.CLASSIFY_RULES" as String[]

   * @return

   */

  public String[] getQoSClassifierRuleStored() {

    String sClassifier = getStoredConfigLayerParamValue("PACKETCLASSIFIER.GENERAL.CLASSIFY_RULES"); 

    

    String sValuesSeparated[]=sClassifier.split(",");

    return(sValuesSeparated);

  }

  

  /**

   * Retrieves currently stored values (value in the device for last GET) for "PACKETCLASSIFIER.GENERAL.TYPE_CLASS_MAP" as String[]

   * @return

   */

  public String[] getQoSClassifierPriosStored() {

    String sClassifierPrios = getStoredConfigLayerParamValue("PACKETCLASSIFIER.GENERAL.TYPE_CLASS_MAP");  

    

    String sValuesSeparated[]=sClassifierPrios.split(",");

    return(sValuesSeparated);

  }

  

  /**

   * Retrieves currently stored values (value in the device for last GET) for "PACKETCLASSIFIER.GENERAL.VLAN_CLASS_MAP" as String[]

   * @return

   */

  public String[] get8021pClassifierPriosStored() {

    String s8021pPrios = getStoredConfigLayerParamValue("PACKETCLASSIFIER.GENERAL.VLAN_CLASS_MAP"); 

    

    String sValuesSeparated[]=s8021pPrios.split(",");

    return(sValuesSeparated);

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "PACKETCLASSIFIER.GENERAL.DEFAULT_CLASS" as String

   * @return

   */

  public String getQoSDefaultPrioStored() {

    String sDefaultPrio = getStoredConfigLayerParamValue("PACKETCLASSIFIER.GENERAL.DEFAULT_CLASS"); 

    return sDefaultPrio;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "PACKETCLASSIFIER.GENERAL.FRAME_TYPE" as String

   * @return

   */

  public String getQoSFrameTypeStored() {

    String sQoSFrametype = getStoredConfigLayerParamValue("PACKETCLASSIFIER.GENERAL.FRAME_TYPE"); 

    return sQoSFrametype; 

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "NTP.GENERAL.ENABLED" as String

   * @return

   */

  public String getNtpClientStatusStored(){

    String sNtpClientStatus = getStoredConfigLayerParamValue("NTP.GENERAL.ENABLED");

    return sNtpClientStatus;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "NTP.GENERAL.RESYNC_TIME" as String

   * @return

   */

  public String getNtpResynchTimeStored(){

    String sNtpResynchTime = getStoredConfigLayerParamValue("NTP.GENERAL.RESYNC_TIME");

    return sNtpResynchTime;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "NTP.GENERAL.HOST" as String

   * @return

   */

  public String getNtpServerIpAddressStored(){

    String sNtpServerIpAddress = getStoredConfigLayerParamValue("NTP.GENERAL.HOST");

    return sNtpServerIpAddress;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "CLOCK.GENERAL.TIME" as String

   * @return

   */

  public String getSytemClockTimeStored() {

    String sSystemTime =  getStoredConfigLayerParamValue("CLOCK.GENERAL.TIME"); 

    return sSystemTime;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "SYSTEM.GENERAL.UPTIME" as String

   * @return

   */

  public String getSystemUptime() {

    String sUptime = getStoredConfigLayerParamValue("SYSTEM.GENERAL.UPTIME");

    return sUptime;

  }

  

  /**

   * Retrieves currently stored value (value in the device for last GET) for "CLOCK.GENERAL.TIME_ZONE" as String

   * @return

   */

  public String getSytemTimeZoneStored() {

    String sSystemTimeZone = getStoredConfigLayerParamValue("CLOCK.GENERAL.TIME_ZONE"); 

    return sSystemTimeZone;

  }

  

  /**

   *  Retrieves currently stored value (value in the device for last GET) for "TR069.MANAGEMENTSERVER.URL" as String

   * @return

   */

  public String getTR069UrlStored() {

    String sTR069url = getStoredConfigLayerParamValue("TR069.MANAGEMENTSERVER.URL");  

    return sTR069url;

  }

  

  /**

   *  Retrieves currently stored value (value in the device for last GET) for "TR069.MANAGEMENTSERVER.USERNAME" as String

   * @return

   */

  public String getTR069UsernameStored() {

    String sTR069username = getStoredConfigLayerParamValue("TR069.MANAGEMENTSERVER.USERNAME");  

    return sTR069username;

  }

  

  /**

   *  Retrieves currently stored value (value in the device for last GET) for "TR069.MANAGEMENTSERVER.PASSWORD" as String

   * @return

   */

  public String getTR069PasswordStored() {

    String sTR069password = getStoredConfigLayerParamValue("TR069.MANAGEMENTSERVER.PASSWORD");  

    return sTR069password;

  }

  

  /**

   *  Retrieves currently stored value (value in the device for last GET) for "TR069.MANAGEMENTSERVER.PERIODIC_INF_ENABLED" as String

   * @return

   */

  public String getTR069PeriodicEnableStored() {

    String sTR069periodicEnable = getStoredConfigLayerParamValue("TR069.MANAGEMENTSERVER.PERIODIC_INF_ENABLED");  

    return sTR069periodicEnable;

  }

  

  /**

   *  Retrieves currently stored value (value in the device for last GET) for "TR069.MANAGEMENTSERVER.PERIODIC_INF_INTERVAL" as String

   * @return

   */

  public String getTR069PeriodicIntervalStored() {

    String sTR069periodicInterval = getStoredConfigLayerParamValue("TR069.MANAGEMENTSERVER.PERIODIC_INF_INTERVAL"); 

    return sTR069periodicInterval;

  }

  

  /**

   *  Retrieves currently stored value (value in the device for last GET) for "TR069.MANAGEMENTSERVER.STUN_ENABLED" as String

   * @return

   */

  public String getStunEnableStored() {

    String sStunEnable = getStoredConfigLayerParamValue("TR069.MANAGEMENTSERVER.STUN_ENABLED"); 

    return sStunEnable;

  }

  

  /**

   *  Retrieves currently stored value (value in the device for last GET) for "TR069.MANAGEMENTSERVER.STUN_SERVER_ADDRESS" as String

   * @return

   */

  public String getStunAddressStored() {

    String sStunServer = getStoredConfigLayerParamValue("TR069.MANAGEMENTSERVER.STUN_SERVER_ADDRESS");  

    return sStunServer;

  }

  

  /**

   *  Retrieves currently stored value (value in the device for last GET) for "TR069.MANAGEMENTSERVER.STUN_SERVER_PORT" as String

   * @return

   */

  public String getStunPortStored() {

    String sStunPort = getStoredConfigLayerParamValue("TR069.MANAGEMENTSERVER.STUN_SERVER_PORT"); 

    return sStunPort;

  }

  

  /**

   *  Retrieves currently stored value (value in the device for last GET) for "TR069.MANAGEMENTSERVER.STUN_USERNAME" as String

   * @return

   */

  public String getStunUsernameStored() {

    String sStunUsername = getStoredConfigLayerParamValue("TR069.MANAGEMENTSERVER.STUN_USERNAME");  

    return sStunUsername;

  }

  

  /**

   *  Retrieves currently stored value (value in the device for last GET) for "TR069.MANAGEMENTSERVER.STUN_PASSWORD" as String

   * @return

   */

  public String getStunPasswordStored() {

    String sStunPassword = getStoredConfigLayerParamValue("TR069.MANAGEMENTSERVER.STUN_PASSWORD");  

    return sStunPassword;

  }

  

  /**

   *  Retrieves currently stored value (value in the device for last GET) for "TR069.MANAGEMENTSERVER.STUN_MAX_KEEP_ALIVE" as String

   * @return

   */

  public String getStunMaxKeepAliveStored() {

    String sStunMaxKeepAlive = getStoredConfigLayerParamValue("TR069.MANAGEMENTSERVER.STUN_MAX_KEEP_ALIVE");  

    return sStunMaxKeepAlive;

  }

  

  /**

   *  Retrieves currently stored value (value in the device for last GET) for "TR069.MANAGEMENTSERVER.STUN_MIN_KEEP_ALIVE" as String

   * @return

   */

  public String getStunMinKeepAliveStored() {

    String sStunMinKeepAlive = getStoredConfigLayerParamValue("TR069.MANAGEMENTSERVER.STUN_MIN_KEEP_ALIVE");  

    return sStunMinKeepAlive;

  }

  

  /**

   *  Retrieves currently stored value (value in the device for last GET) for "FLUPGRADE.GENERAL.STATUS" as String

   * @return

   */

  public String getUpgradeStatus() {

    return getStoredConfigLayerParamValue("FLUPGRADE.GENERAL.STATUS");

  }

  

  public String getXputIndicatorStored() 

  {

    return getStoredConfigLayerParamValue("FLOWMONITOR.INFO.XPUT_INDICATOR");

  }



  public String getAccessPolicyStored()

  {

    String s = getStoredConfigLayerParamValue("PHYMNG.GENERAL.ACCESS_POLICY");

    if (s == null || s.contentEquals("0"))

    {

      return "HN";

    }

    else if (s.contentEquals("1"))

    {

      return "G.now";

    }

    else if (s.contentEquals("2"))

    {

      return "P2MP";

    }

    else if (s.contentEquals("5"))

    {

      return "PRO";

    }

    else if (s.contentEquals("6"))

    {

      return "LIFI";

    }

    else if (s.contentEquals("7"))

    {

      return "NEXT";

    }

    return "Unknown";

  }

  

  public String getVideoSourceStored()

  {

    return getStoredConfigLayerParamValue("MCAST.INFO.VIDEO_SOURCE_DID");

  }



  public String[] getVideoMacsListStored()

  {

    String s = getStoredConfigLayerParamValue("MCAST.INFO.VIDEO_MACS_LIST");

    if (s != null && s.contains(":"))

    {

      return s.split(",");

    }

    else

    {

      return null;

    }

  }



  public String getCVLANFilteringEnableStored() {

    String s = getStoredConfigLayerParamValue("VLAN.CVLAN.FILTERING_ENABLE");

    return s;

  }

  



  public String getCVLANConfigEthAStored() {

    String s = getStoredConfigLayerParamValue("VLAN.CVLAN.CONFIG_IF_ETHA");

    return s;

  }

  public String getCVLANConfigEthBStored() {

    String s = getStoredConfigLayerParamValue("VLAN.CVLAN.CONFIG_IF_ETHB");

    return s;

  }

  public String getCVLANConfigMgmtStored() {

    String s = getStoredConfigLayerParamValue("VLAN.CVLAN.CONFIG_IF_MGMT");

    return s;

  }

  public String getCVLANPVIDEthAStored() {

    String s = getStoredConfigLayerParamValue("VLAN.CVLAN.PVID_ETHA");

    return s;

  }

  public String getCVLANPVIDEthBStored() {

    String s = getStoredConfigLayerParamValue("VLAN.CVLAN.PVID_ETHB");

    return s;

  }

  public String getCVLANPVIDMgmtStored() {

    String s = getStoredConfigLayerParamValue("VLAN.CVLAN.PVID_MGMT");

    return s;

  }

  public String getCVLANAllowedEthAStored() {

    String s = getStoredConfigLayerParamValue("VLAN.CVLAN.ALLOWED_TAGS_IN_ETHA");

    return s;

  }

  public String getCVLANAllowedEthBStored() {

    String s = getStoredConfigLayerParamValue("VLAN.CVLAN.ALLOWED_TAGS_IN_ETHB");

    return s;

  }

  /**

   * Retrieves currently stored values (value in the device for last GET) for "BFT.GENERAL.LOCAL_MACS" as String[]

   * @return

   */

  public String[] getLocalMacsStored (){

    String sValuesMacs[]=null;

    String s = getStoredConfigLayerParamValue("BFT.GENERAL.LOCAL_MACS");

    if(s!=null){

      if(!s.isEmpty()){

        if(s.contains(",")){

          sValuesMacs= s.split(",");

        }else{          

          sValuesMacs=new String[1];

          sValuesMacs[0]=s;



        }

      }

      

    }



    return sValuesMacs;

  }

  

  int parseInt(String s)

  {

    if (s.contains("0x"))

    {

      s = s.substring(2);

      return Integer.valueOf(s, 16);

    }

    else

    {

      return Integer.parseInt(s);

    }

  }

  public String[][] getMacsInfoStored()

  {

    String[][] result = new String[1][4];

    String[] portTypes = {"None", "Ethernet A", "Ethernet B", "SDIO", "G.hn", "Firmware"};

    String value = getStoredConfigLayerParamValue("BFT.GENERAL.MACS_INFO");

    if (value != null && !value.isEmpty())

    {

      String[] values = value.split(",");

      if (values.length % 11 == 0)

      {

        result = new String[values.length/11][4];

        for (int i = 0; i < values.length/11; i++)

        {

          result[i][0] = String.format("%02X:%02X:%02X:%02X:%02X:%02X", 

              parseInt(values[i*11]),

              parseInt(values[i*11+1]),

              parseInt(values[i*11+2]),

              parseInt(values[i*11+3]),

              parseInt(values[i*11+4]),

              parseInt(values[i*11+5]));

          result[i][2] = parseInt(values[i*11+8]) == 0?"NO":"YES";

          result[i][3] = parseInt(values[i*11+9]) == 0?"NO":"YES";

          result[i][1] = portTypes[parseInt(values[i*11+10])];

          if (parseInt(values[i*11+10]) == 4)

          {

            result[i][1] += " (DId "+values[i*11+7]+")";

          }

        }

      }

    }



    return result;

  }

  

  public Hashtable<String,String> getNodeMACsStored() {

    String sDids[] = getStoredConfigLayerParamValue("DIDMNG.GENERAL.DIDS").split(",");

    String sMacs[] = null;

    Hashtable<String,String> ht = new Hashtable<String,String>();

    if (sDids.length > 1)

    {      

      sMacs = getStoredConfigLayerParamValue("DIDMNG.GENERAL.MACS").split(",");

      for (int i=1;i<sDids.length;i++)

      {

      	if(sMacs[i-1].equalsIgnoreCase("00:00:00:00:00:00") == false)

      	{

      		ht.put(sDids[i], sMacs[i-1]);

      	}

      }

    }

    if (ht.size() > 0)

    {

      return ht;

    }

    else

    {

      return null;

    }

  }

  

  public ArrayList<SctDevice> getMarvellGhnDevicesSeenBy() {

    ArrayList<SctDevice> remoteDevices = null;;

    ArrayList<byte[]> slave_macs=null;

    String twopointsSeparatedStringMACs[];

    int numDids;



    String sNumDids = getStoredConfigLayerParamValue("DIDMNG.GENERAL.NUM_DIDS");

    if(sNumDids == null)

    {

    	numDids = 0;

    }

    else

    {

    	numDids = Integer.parseInt(sNumDids);

    }

    

    String sMacs = null;

    if (numDids > 0)

    {

      sMacs = getStoredConfigLayerParamValue("DIDMNG.GENERAL.MACS");

      if(!sMacs.isEmpty()){



        sMacs=sMacs.toUpperCase().replace(":","");



        if(sMacs.contains(","))

        {

          twopointsSeparatedStringMACs=sMacs.split(",");

          slave_macs=new ArrayList<byte[]>();

          for(int i=0;i<twopointsSeparatedStringMACs.length;i++){

            //twopointsSeparatedStringMACs[i]=twopointsSeparatedStringMACs[i].replace(":", "");

          	if(twopointsSeparatedStringMACs[i].equalsIgnoreCase("000000000000") == false)

          	{

          		if(!twopointsSeparatedStringMACs[i].contentEquals(getMACAddrStored().replace(":", "")))

          		{

          			//It is not our MAC

          			slave_macs.add(Conversion.stringToByteArray(twopointsSeparatedStringMACs[i]));

          		}

          	}

          }

        }

        else

        {

        	if(sMacs.equalsIgnoreCase("000000000000") == false)

        	{

        		//Single MACS

        		if(!sMacs.contentEquals(getMACAddrStored().replace(":", "")))

        		{

        			slave_macs=new ArrayList<byte[]>(1);

        			slave_macs.add(Conversion.stringToByteArray(sMacs));

        		}

        	}

        }

      }

    }



    if((slave_macs!=null)&&(slave_macs.size()!=0))

    {

      remoteDevices=new ArrayList<SctDevice>();

      for(int i=0;i<slave_macs.size();i++)

      {

        // TODO the remote device could be a different type!

        SctDevice remoteDevice=new SctDevice(this.iface, this.vlanid, slave_macs.get(i));

        remoteDevices.add(remoteDevice);

      }

    }



    return(remoteDevices);

  }

  

  public String[] getNodeAccessListStored() {

    String sMacs = getStoredConfigLayerParamValue("NAP.GENERAL.ACCESS_ALLOWED_MAC_LIST");

    

    if (sMacs != null)

    {

    	String tmpsMacs[] = sMacs.split(",");

    	if (tmpsMacs.length >= 1 && tmpsMacs[0].length() > 1)

    	{      

    		return tmpsMacs;

    	}

    	else

    	{

    		return null;

    	}

    }

    else

    {

      return null;

    }

  }



  public String[] getNodeRequestedAccessList() {

  	String sMacs = getStoredConfigLayerParamValue("NAP.GENERAL.ACCESS_REQUEST_MAC_LIST");

  	

  	if (sMacs != null)

  	{

  		String tmpsMacs[] = sMacs.split(",");

  		if (tmpsMacs.length >= 1 && tmpsMacs[0].length() > 1)

  		{      

  			return tmpsMacs;

  		}

    	else

    	{

    		return null;

    	}

  	}

  	else

  	{

  		return null;

  	}

  }

  

  public String getAccessListControlEnabledStored() {

    String s = getStoredConfigLayerParamValue("NAP.GENERAL.ACCESS_CONTROL_ENABLED");

    return s;

  }



  public String[] getQoSDSCPClassMapListStored() {

    String dscpMap = getStoredConfigLayerParamValue("PACKETCLASSIFIER.GENERAL.DSCP_CLASS_MAP");



    return dscpMap.split(",");

  }



  public String getQoSDSCPClassMapEnabledStored() {

    String s = getStoredConfigLayerParamValue("PACKETCLASSIFIER.GENERAL.DSCP_CLASS_MAP_EN");

    return s;

  }

  

  public String getQoSARPClassMapEnabledStored() {

    String s = getStoredConfigLayerParamValue("PACKETCLASSIFIER.GENERAL.ARP_CLASS_MAP_EN");

    return s;

  }

  

  public String getQoSARPClassMapStored() {

    String s = getStoredConfigLayerParamValue("PACKETCLASSIFIER.GENERAL.ARP_CLASS_MAP");

    return s;

  }

  

  public String getQoSTCPIPv4ClassMapEnabledStored() {

    String s = getStoredConfigLayerParamValue("PACKETCLASSIFIER.GENERAL.TCPACKV4_CLASS_MAP_EN");

    return s;

  }

  

  public String getQoSTCPIPv4ClassMapStored() {

    String s = getStoredConfigLayerParamValue("PACKETCLASSIFIER.GENERAL.TCPACKV4_CLASS_MAP");

    return s;

  }

  

  public String getQoSTCPIPv6ClassMapEnabledStored() {

    String s = getStoredConfigLayerParamValue("PACKETCLASSIFIER.GENERAL.TCPACKV6_CLASS_MAP_EN");

    return s;

  }

  

  public String getQoSTCPIPv6ClassMapStored() {

    String s = getStoredConfigLayerParamValue("PACKETCLASSIFIER.GENERAL.TCPACKV6_CLASS_MAP");

    return s;

  }

  

  public String getQoSRulesOrderStored() {

    String s = getStoredConfigLayerParamValue("PACKETCLASSIFIER.GENERAL.RULES_ORDER");

    return s;

  }

  

  private NodeElement parseTopologyEntry(String nodeLine){

  	NodeElement node = new NodeElement();

  	

  	String line_elements[]=nodeLine.split("\\s+");



  	//Line has sth like     // 00:13:9d:00:1f:08 00:13:9d:00:1c:00  2 0



  	if (line_elements.length >= 1)

  	{		

  		node.nodeMAC    = line_elements[0];

  	}

  	else

  	{

  		node.nodeMAC    = "0000000000000";

  	}



  	if (line_elements.length >= 2)

  	{		

  		node.parentMAC  = line_elements[1];

  	}

  	else

  	{

  		node.parentMAC  = "0000000000000";

  	}



  	if (line_elements.length >= 3)

  	{		

  		node.numHops  = Integer.parseInt(line_elements[2]);

  	}

  	else

  	{

  		node.numHops   = 0;

  	}

  	if (line_elements.length >= 4)

  	{		

  		if(Integer.parseInt(line_elements[3]) == 0)

  		{

  			node.isMPR  = false;

  		}

  		else

  		{

  			node.isMPR  = true;

  		}

  	}

  	else

  	{

  		node.isMPR = false;

  	}

  	

  	return node;

  }

  

  public Hashtable<String,NodeElement> getTopologyInfoStored(StringBuilder topNode) {

    DekParameter param;

    Hashtable <String,NodeElement> topology_info = new Hashtable <String, NodeElement>();

    String top_node;

    ///Parameter returns sth like

    ///DIDMNG.GENERAL.TOPOLOGY_INFO = 00:13:9d:00:1f:0f 00:00:00:00:00:00  0 0

    // 00:13:9d:00:1c:00 00:13:9d:00:1f:0f  1 1

    // 00:13:9d:00:1f:08 00:13:9d:00:1c:00  2 0



    //We ask the modem for the current value instead of using stored one as this could change and we want to update up

    //with refresh button in the app

    param = this.getConfiglayer(MarvellNet.cfgPassword, "DIDMNG.GENERAL.TOPOLOGY_INFO");

    if (param != null)

    {

      String s = param.value;

      



    	s = s.toUpperCase();

    	s = s.replace(":", "");

    	

//      System.out.println("Reported nodes:\n" + s +"\n");



    	if (s.isEmpty() == false )

    	{

    		///Create an array with each entry to parse then each entry individually

    		String nodes[] = s.split("\\r?\\n");

    		

//        System.out.println("Processing entries. Number of nodes " + nodes.length +"\n");



    		for (int i = 0; i < nodes.length; i++)

    		{

    			NodeElement node = new NodeElement();

    			

    			node = parseTopologyEntry(nodes[i]);

//          System.out.println("Entry " + nodes[i] + " MAC: " + node.nodeMAC + " parent:" + node.parentMAC + "\n");

    			topology_info.put(node.nodeMAC, node);

    		}

    	}

    	//Once added all nodes to the hash, it has been added to each node who's his parent 

    	//Now, we need to iterate through all nodes to add in a parent who are his children 

      List<String>  nodes_mac = new ArrayList<String>(topology_info.keySet());

      Iterator<String> nodeSetIterator = nodes_mac.iterator();



      while (nodeSetIterator.hasNext())

      {

        String iter_mac = nodeSetIterator.next();

        NodeElement iter_node = topology_info.get(iter_mac);



        // For a given node who's parent MAC is not 0's (this is the HE)

        if (iter_node.parentMAC.contentEquals("000000000000") == false)

        {

          //Get the parent node

          NodeElement parent_node = topology_info.get(iter_node.parentMAC);

          

          //Add current node's MAC and replace parent node info

          if (parent_node.childMACs == null)

          {

            parent_node.childMACs = new ArrayList<String>();

          }

          parent_node.childMACs.add(iter_node.nodeMAC);

          topology_info.replace(iter_node.parentMAC, parent_node);          

        }

        else

        {

          topNode.append(iter_mac);

        }

      }

    }

    

    return topology_info;

  }

  

//////////////////////////////////////////////////////////

  /// SET FUNCTIONS

  //////////////////////////////////////////////////////////

  

  public boolean setAndCheckAndStoreParameter(String name, String value)

  {

    boolean result = setAndCheckParameter(name, value);

    

    if (result)

    {

      updateStoredValue(name, value);

    }

    return result;

  }

  

  public boolean setAndCheckAndStoreParameter(String[] names, String[] values)

  {

    boolean result = setAndCheckParameter(names, values);

    

    if (result)

    {

      updateStoredValue(names, values);

    }

    return result;

  }



  public boolean setAndCheckParameter(String name, String value)

  {

    String[] names = new String[1];

    names[0] = name;

    

    String[] values = new String[1];

    values[0] = value;

    

    boolean result = setAndCheckParameter(names, values);

    

    return result;

  }

  

  public boolean setAndCheckParameter(String[] names, String[] values)

  {

    boolean ret = true;

    

    InitApp.top.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

    DekParameter[] result = setConfiglayer(MarvellNet.cfgPassword, names, values);

    InitApp.top.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));



    if (result != null)

    {

      for (int i = 0; i < result.length; i++)

      {

        setErrorMessage = result[i].value;

        if (!result[i].value.contentEquals("OK"))

        {

          if (showSetErrorMessage)

          {

            JOptionPane.showMessageDialog(InitApp.top, "Set failed: Parameter error in node "+this.getMacAsString()+":\n"+result[i].name+"="+result[i].value,"Spirit Config Tool Error", JOptionPane.ERROR_MESSAGE);

          }

          ret = false;

        }

      }

    }

    else

    {

      ret = false;

      setErrorMessage = "No response";

      if (showSetErrorMessage)

      {

        JOptionPane.showMessageDialog(InitApp.top, "Set failed: No response from node "+this.getMacAsString(),"Spirit Config Tool Error", JOptionPane.ERROR_MESSAGE);

      }

    }

    

    return ret;

  }

  

  public boolean setNodeType (String sNodeTypeValue)

  {

    return setAndCheckAndStoreParameter("MASTERSELECTION.GENERAL.FORCE_NODE", sNodeTypeValue);

  }

  

  public boolean rebootByHWReset ()

  {

    return setAndCheckParameter("SYSTEM.GENERAL.HW_RESET","1");

  }

  

  public boolean setDomainName (String sNodeDomainName)

  {

    return setAndCheckAndStoreParameter("NODE.GENERAL.DOMAIN_NAME", sNodeDomainName);

  }

  

  public boolean setPhyModeID (String sMode)

  {

    return setAndCheckAndStoreParameter("PHYMNG.GENERAL.RUNNING_PHYMODE_ID", sMode);

  }

  

  public boolean setPairing ()

  {

    if(getStoredConfigLayerParamValue("PAIRING.GENERAL.PRESS_BUTTON")!=null)

    {

      return setAndCheckParameter("PAIRING.GENERAL.PRESS_BUTTON","1");

    }

    else if(getStoredConfigLayerParamValue("PAIRING.GENERAL.PROCESS_START")!=null)

    {

      return setAndCheckParameter("PAIRING.GENERAL.PROCESS_START","1");

    }

    else

    {

      return false;

    }

  }

  

  public boolean setUnpairing()

  {

    return setAndCheckParameter("PAIRING.GENERAL.LEAVE_SECURE_DOMAIN","1");

  }

  

  public boolean setPairingGeneralPassword(String sPW)

  {

    return setAndCheckAndStoreParameter("PAIRING.GENERAL.PASSWORD", sPW);

  }

  

  public boolean setConfigPasswordInDevice(String password)

  {

    return setAndCheckAndStoreParameter("SYSTEM.GENERAL.PASSWORD_CONFIG", password);

  }



  public boolean setDhcpIpv4(boolean enable)

  {

    String v;

    if (enable)

    {

      v = "YES";

    }

    else

    {

      v = "NO";

    }

    return setAndCheckAndStoreParameter("DHCP.GENERAL.ENABLED_IPV4", v);

  }

  

  public boolean setIpConfigFixed(String ip, String mask, String gw, String dns) 

  {  

    String[] names = {"TCPIP.IPV4.IP_ADDRESS", "TCPIP.IPV4.IP_NETMASK", "TCPIP.IPV4.GATEWAY", "DNS.GENERAL.IPV4"};

  

    String[] values = {ip, mask, gw, dns};



    return setAndCheckAndStoreParameter(names, values);

  }  



  public boolean setAdditionalIp1Config(String ip, String mask) {

    String[] names = {"TCPIP.IPV4.ADDITIONAL_IP_ADDRESS.1", "TCPIP.IPV4.ADDITIONAL_IP_NETMASK.1"};

    String[] values = {ip, mask};

    return setAndCheckAndStoreParameter(names, values);

  }

  

  public boolean setAdditionalIp2Config(String ip, String mask) {

    String[] names = {"TCPIP.IPV4.ADDITIONAL_IP_ADDRESS.2", "TCPIP.IPV4.ADDITIONAL_IP_NETMASK.2"};

    String[] values = {ip, mask};

    return setAndCheckAndStoreParameter(names, values);

  }

  

  public boolean setStartL2Upgrade(String section) 

  {  

    String[] names = {"FLUPGRADE.GENERAL.SOURCE", "FLUPGRADE.GENERAL.SECTION", "FLUPGRADE.GENERAL.START"};

    String[] values = {"L2", section, "1"};



    return setAndCheckParameter(names, values);

  }

  

  public boolean setStartTFTPUpgrade(String section, String serverIp, String filename) 

  {  

    String[] names = {"FLUPGRADE.GENERAL.SOURCE", "FLUPGRADE.GENERAL.SECTION", "FLUPGRADE.GENERAL.HOST", "FLUPGRADE.GENERAL.FILENAME", "FLUPGRADE.GENERAL.START"};

    String[] values = {"TFTP", section, serverIp, filename, "1"};



    return setAndCheckParameter(names, values);

  }

  

  public boolean setUserNotches(Notch[] notches)

  {

    String sUserNotches = getStoredConfigLayerParamValue("POWERMASK.USER.NOTCHES");



    int size = sUserNotches.split(",").length/3;

        

    for (int i = 0; i < size; i++)

    {

      if (notches != null && i < notches.length)

      {

        String notch = notches[i].start;

        notch += ","+ notches[i].stop;

        notch += ","+ String.valueOf((Integer.parseInt(notches[i].depth))*2);

        String[] names = {"POWERMASK.USER.NOTCHES."+(i+1)+".0"};

        String[] values = {notch, "YES", "1"};

        if (setAndCheckAndStoreParameter(names, values) == false)

        {

          return false;

        }

      }

      else

      {

        String[] names = {"POWERMASK.USER.NOTCHES."+(i+1)+".0"};

        String[] values = {"0,0,0", "YES", "1"};

        if (setAndCheckAndStoreParameter(names, values) == false)

        {

          return false;

        }



      }

    }

    if (notches != null && size < notches.length)

    {

      for (int i = size; i < notches.length; i++)

      {

        String notch = notches[i].start;

        notch += ","+ notches[i].stop;

        notch += ","+ String.valueOf((Integer.parseInt(notches[i].depth))*2);

        String[] names = {"POWERMASK.USER.NOTCHES."+(i+1)+".0"};

        String[] values = {notch, "YES", "1"};

        if (setAndCheckAndStoreParameter(names, values) == false)

        {

          return false;

        }

      }

    }

    

    String[] names = {"POWERMASK.USER.NOTCHES_ENABLE", "POWERMASK.GENERAL.NOTCHES_UPDATE"};

    String[] values = {"YES", "1"};



    return setAndCheckAndStoreParameter(names, values);



  }

  

  public boolean setAddNotch(String start, String stop, String depth) 

  {

    String sUserNotches = getStoredConfigLayerParamValue("POWERMASK.USER.NOTCHES");

    StringTokenizer stkUND = new StringTokenizer(sUserNotches, ",");

    int counter = 1;

    boolean bEmpty = true;

    String sError=null;

    

    while (stkUND.hasMoreTokens()&&bEmpty)

    {

      if (!stkUND.nextToken().equals("0")) 

      {

        counter++;

      } else 

      {

        bEmpty = false;

      }

    }

    if (counter>1) 

    {

      counter=(counter/3)+1;

    }

    depth = String.valueOf((Integer.parseInt(depth))*2); //multiply sDepth by 2 to pass it to 0.5dB units

    if ((counter<11)&&(Integer.parseInt(depth)<201)) 

    {

      String[] names = {"POWERMASK.USER.NOTCHES."+counter+".0", "POWERMASK.USER.NOTCHES_ENABLE", "POWERMASK.GENERAL.NOTCHES_UPDATE"};

      String[] values = {start+","+stop+","+depth, "YES", "1"};



      return setAndCheckAndStoreParameter(names, values);

    } 

    else 

    {

      if(counter>=11){

        sError="Number of notches exceeded (maximum is 10).";

      }

      if(Integer.parseInt(depth)>=201){

        sError="Depth value exceeded (maximum is 100dB).";

      }

      JOptionPane.showMessageDialog(InitApp.top, "Set notch failed: "+sError,"Spirit Config Tool Error", JOptionPane.ERROR_MESSAGE);

      return false;

    }

        

  }



  public boolean setRemoveNotch(String number) 

  {

    String[] names = {"POWERMASK.USER.NOTCHES."+number+".0", "POWERMASK.GENERAL.NOTCHES_UPDATE"};

    String[] values = {"0,0,0", "1"};



    return setAndCheckAndStoreParameter(names, values);

  }



  public boolean setPowersaving(int enable, String timer) {

    String[] names = {"POWERSAVING.GENERAL.MODE", "POWERSAVING.GENERAL.IDLE_TIME"};

    String[] values = {Integer.toString(enable), timer};



    return setAndCheckAndStoreParameter(names, values);

  }



  public boolean setMacAddr(String mac) {

    boolean result = setAndCheckParameter("SYSTEM.PRODUCTION.MAC_ADDR", mac);

    return result;

  }

  

  public boolean setSytemClockTime(String concat) {

    return setAndCheckAndStoreParameter("CLOCK.GENERAL.TIME", concat);

  }



  public boolean setSytemTimeZone(int timeZone) {

    return setAndCheckAndStoreParameter("CLOCK.GENERAL.TIME_ZONE", Integer.toString(timeZone));

  }



  public boolean setNtpClientAll(String enable, String resyncTime, String host) {

    String[] names = {"NTP.GENERAL.ENABLED", "NTP.GENERAL.RESYNC_TIME", "NTP.GENERAL.HOST"};

    String[] values = {enable, resyncTime, host};



    return setAndCheckAndStoreParameter(names, values);

  }



  public boolean setDhcpIpv6Config(boolean enable) {

    String v;

    if (enable)

    {

      v = "YES";

    }

    else

    {

      v = "NO";

    }

    return setAndCheckAndStoreParameter("DHCP.GENERAL.ENABLED_IPV6", v);

  }



  public boolean setDnsIpv6Address(String ip) {

    return setAndCheckAndStoreParameter("DNS.GENERAL.IPV6", ip);

  }



  public boolean setManualIpv6(String sManualIPAddress1, String sManualIPAddress2, String sManualIPAddress3, 

      String sManualIPAddress4, String sIPV6Address, String sIPV6GateWayAddress, String sAdditionalIPPrefix1, 

      String sAdditionalIPPrefix2, String sAdditionalIPPrefix3, String sAdditionalIPPrefix4, String sIPPrefix) 

  {

    String[] names = {"TCPIP.IPV6.IP_ADDRESS", "TCPIP.IPV6.GATEWAY", "TCPIP.IPV6.ADDITIONAL_IP_PREFIX", 

        "TCPIP.IPV6.IP_PREFIX", "TCPIP.IPV6.ADDITIONAL_IP_ADDRESS"};

    String[] values = {sIPV6Address, sIPV6GateWayAddress, sAdditionalIPPrefix1+","+sAdditionalIPPrefix2+","+sAdditionalIPPrefix3+","+sAdditionalIPPrefix4, 

        sIPPrefix,sManualIPAddress1+","+sManualIPAddress2+","+sManualIPAddress3+","+sManualIPAddress4};



    return setAndCheckAndStoreParameter(names, values);

  }



  public boolean setStartL2Upgrade(String section, String file) 

  {

    String[] names = {"FLUPGRADE.GENERAL.SOURCE", "FLUPGRADE.GENERAL.SECTION", "FLUPGRADE.GENERAL.MODEMFILENAME", "FLUPGRADE.GENERAL.START"};

    String[] values = {"L2", section, file, "1"};



    return setAndCheckParameter(names, values);

  }



  public boolean setNDIM(int mode, String domainName, String seed) {

    if (mode == 0)

    {

      String[] names = {"NODE.GENERAL.DOMAIN_ID_MODE", "NODE.GENERAL.DOMAIN_ID", "NODE.GENERAL.DOMAIN_NAME", "NODE.GENERAL.EXTENDED_SEED_IDX"};

      String[] values = {"3", "0", domainName, seed};

      return setAndCheckAndStoreParameter(names, values);    

    }

    else if (mode == 1)

    {

      String[] names = {"NODE.GENERAL.DOMAIN_ID_MODE", "NODE.GENERAL.DOMAIN_ID", "NODE.GENERAL.DOMAIN_NAME", "NODE.GENERAL.EXTENDED_SEED_IDX"};

      String[] values = {"1", "0", domainName, "0"};

      return setAndCheckAndStoreParameter(names, values);    

    }

    else if (mode == 2)

    {

      String[] names = {"NODE.GENERAL.DOMAIN_ID_MODE", "NODE.GENERAL.DOMAIN_ID", "NODE.GENERAL.DOMAIN_NAME", "NODE.GENERAL.EXTENDED_SEED_IDX"};

      String[] values = {"5", "0", domainName, "0"};

      return setAndCheckAndStoreParameter(names, values);    

    }

    else

    {

      return false;

    }

  }



  public boolean setBcastSuppression(String txput) {

    return setAndCheckAndStoreParameter("CLDRIVER.BCASTSUP.XPUT", txput);

  }



  public boolean setCVLAN(String enable) {

    return setAndCheckAndStoreParameter("VLAN.CVLAN.ENABLE", enable);

    

  }



  public boolean setCVLANtag(String tagEthA, String tagEthB, String tagMgmt, String tagGhn, String tagSDIO) 

  {

    String[] names = {"VLAN.CVLAN.TAG_ETHA", "VLAN.CVLAN.TAG_ETHB", "VLAN.CVLAN.TAG_MGMT_INTERFACE", "VLAN.CVLAN.TAG_GHN_INTERFACE", "VLAN.CVLAN.TAG_SDIO"};

    String[] values = {tagEthA, tagEthB, tagMgmt, tagGhn, tagSDIO};

    return setAndCheckAndStoreParameter(names, values);    

  }



  public boolean setCVLANtrunkSDIO(String enable) {

    return setAndCheckAndStoreParameter("VLAN.CVLAN.TRUNK_MODE.1", enable);

    

  }



  public boolean setCVLANtrunkETHA(String enable) {

    return setAndCheckAndStoreParameter("VLAN.CVLAN.TRUNK_MODE.2", enable);

    

  }



  public boolean setCVLANtrunkETHB(String enable) {

    return setAndCheckAndStoreParameter("VLAN.CVLAN.TRUNK_MODE.3", enable);

    

  }



  public boolean setCVLANtrunkFW(String enable) {

    return setAndCheckAndStoreParameter("VLAN.CVLAN.TRUNK_MODE.4", enable);

    

  }



  public boolean setCVLANtrunkPLC(String enable) {

    String[] names = new String[21];

    String[] values = new String[21];

    

    for (int i=0; i <=20; i++) 

    {

      names[i] = "VLAN.CVLAN.TRUNK_MODE."+(i+7);

      values[i] = enable;

    }

    return setAndCheckAndStoreParameter(names, values);

  }



  public boolean setQoSEnable(String enable) {

    return setAndCheckAndStoreParameter("PACKETCLASSIFIER.GENERAL.TYPE_CLASS_MAP_EN", enable);

    

  }



  public boolean setQoS8021p(String enable) {

    return setAndCheckAndStoreParameter("PACKETCLASSIFIER.GENERAL.VLAN_CLASS_MAP_EN", enable);

    

  }



  public boolean setQoSClassifierRule(String offset1, String bitmask1, String pattern1,String enable1, 

      String offset2, String bitmask2, String pattern2,String enable2,

      String offset3, String bitmask3, String pattern3,String enable3, String offset4, String bitmask4, String pattern4,String enable4,

      String offset5, String bitmask5, String pattern5,String enable5, String offset6, String bitmask6, String pattern6,String enable6,

      String offset7, String bitmask7, String pattern7,String enable7, String offset8, String bitmask8, String pattern8, String enable8) 

  {

    return setAndCheckAndStoreParameter("PACKETCLASSIFIER.GENERAL.CLASSIFY_RULES", 

            offset1+","+bitmask1+","+pattern1+","+enable1+","+offset2+","+bitmask2+","+pattern2+","+enable2+","+

            offset3+","+bitmask3+","+pattern3+","+enable3+","+offset4+","+bitmask4+","+pattern4+","+enable4+","+

            offset5+","+bitmask5+","+pattern5+","+enable5+","+offset6+","+bitmask6+","+pattern6+","+enable6+","+

            offset7+","+bitmask7+","+pattern7+","+enable7+","+offset8+","+bitmask8+","+pattern8+","+enable8);

  }



  public boolean setQoSDefaultPrio(String prio) {

    return setAndCheckAndStoreParameter("PACKETCLASSIFIER.GENERAL.DEFAULT_CLASS", prio);

    

  }



  public boolean setQoSPacketDetector(String offset1, String bitmask1, String pattern1, String enable1, String offset2, String bitmask2, String pattern2, String enable2) 

  {

    return setAndCheckAndStoreParameter("PACKETCLASSIFIER.GENERAL.MATCHING_RULES", 

            offset1+","+bitmask1+","+pattern1+","+enable1+","+

            offset2+","+bitmask2+","+pattern2+","+enable2);

  }



  public boolean setQoSClassifierPrios(String prio1, String prio2, String prio3, String prio4,  String prio5, String prio6, String prio7, String prio8) 

  {

    return setAndCheckAndStoreParameter("PACKETCLASSIFIER.GENERAL.TYPE_CLASS_MAP", 

            prio1+","+prio2+","+prio3+","+prio4+","+

            prio5+","+prio6+","+prio7+","+prio8);

  }



  public boolean setQoSClassifierPriosString(String prios) 

  {

    return setAndCheckAndStoreParameter("PACKETCLASSIFIER.GENERAL.TYPE_CLASS_MAP", prios);

  }



  public boolean setQoSFrameType(String type) {

    return setAndCheckAndStoreParameter("PACKETCLASSIFIER.GENERAL.FRAME_TYPE", type);

    

  }



  public boolean set8021pClassifierPrios(String prio1, String prio2, String prio3, String prio4, String prio5, String prio6, String prio7, String prio8) 

  {

    return setAndCheckAndStoreParameter("PACKETCLASSIFIER.GENERAL.VLAN_CLASS_MAP", 

        prio1+","+prio2+","+prio3+","+prio4+","+

        prio5+","+prio6+","+prio7+","+prio8);

  }



  public boolean set8021pClassifierPriosString(String vlanPrios) 

  {

    return setAndCheckAndStoreParameter("PACKETCLASSIFIER.GENERAL.VLAN_CLASS_MAP", vlanPrios);

  }



  public boolean setQoSDSCPClassMapList(String dscpMapPrios) {

    return setAndCheckAndStoreParameter("PACKETCLASSIFIER.GENERAL.DSCP_CLASS_MAP", dscpMapPrios);

  }



  public boolean setQoSDSCPClassMapEnabled(String enable) {

  	return setAndCheckAndStoreParameter("PACKETCLASSIFIER.GENERAL.DSCP_CLASS_MAP_EN", enable);

  }

  

  public boolean setQoSARPClassMapEnabled(String enable) {

  	return setAndCheckAndStoreParameter("PACKETCLASSIFIER.GENERAL.ARP_CLASS_MAP_EN", enable);

  }

  

  public boolean  setQoSARPClassMap(String prio) {

  	return setAndCheckAndStoreParameter("PACKETCLASSIFIER.GENERAL.ARP_CLASS_MAP", prio);

  }

  

  public boolean  setQoSTCPIPv4ClassMapEnabled(String enable) {

  	return setAndCheckAndStoreParameter("PACKETCLASSIFIER.GENERAL.TCPACKV4_CLASS_MAP_EN", enable);

  }

  

  public boolean  setQoSTCPIPv4ClassMap(String prio) {

  	return setAndCheckAndStoreParameter("PACKETCLASSIFIER.GENERAL.TCPACKV4_CLASS_MAP", prio);

  }

  

  public boolean  setQoSTCPIPv6ClassMapEnabled(String enable) {

  	return setAndCheckAndStoreParameter("PACKETCLASSIFIER.GENERAL.TCPACKV6_CLASS_MAP_EN", enable);

  }

  

  public boolean  setQoSTCPIPv6ClassMap(String prio) {

  	return setAndCheckAndStoreParameter("PACKETCLASSIFIER.GENERAL.TCPACKV6_CLASS_MAP", prio);

  }

  

  public boolean  setQoSRulesOrder(String rulesOrder) {

  	return setAndCheckAndStoreParameter("PACKETCLASSIFIER.GENERAL.RULES_ORDER", rulesOrder);

  }

  

  public boolean setLogFile(String enable, String sdataInterval, String sftpHost, String sftpLogin, String sftpPassword, String sUploadInterval, String sDestination) 

  {

    String[] names = {"LOGFILE.GENERAL.ENABLE", "LOGFILE.GENERAL.DATA_INTERVAL", "LOGFILE.FTPSERVER.HOST", "LOGFILE.FTPSERVER.LOGIN", "LOGFILE.GENERAL.UPLOAD_INTERVAL"};

    String[] values = {enable, sdataInterval, sftpHost, sftpLogin, sUploadInterval};

    boolean result = setAndCheckAndStoreParameter(names, values);  

  

    if (result)

    {

      if (getLogFileDestinationStored() !=null) 

      {

        String[] names1 = {"LOGFILE.GENERAL.DESTINATION"};

        String[] values1 = {sDestination};

        result = setAndCheckAndStoreParameter(names1, values1);    

      }

    }



    if (result)

    {

      if ((sftpPassword).equals("****") == false) 

      {

        String[] names1 = {"LOGFILE.FTPSERVER.PASSWORD"};

        String[] values1 = {sftpPassword};

        result = setAndCheckAndStoreParameter(names1, values1);    

      }

    }

    

    return result;

  }



  public boolean setTR069Config(String sServerInfEnabled, String sServerUrl, String sServerUserName, String sServerPassword, String sServerInfInterval)

  {

    String[] names = {"TR069.MANAGEMENTSERVER.URL", "TR069.MANAGEMENTSERVER.USERNAME", "TR069.MANAGEMENTSERVER.PERIODIC_INF_ENABLED", "TR069.MANAGEMENTSERVER.PERIODIC_INF_INTERVAL"};

    String[] values = {sServerUrl, sServerUserName, sServerInfEnabled, sServerInfInterval};

    boolean result = setAndCheckAndStoreParameter(names, values);    

    if (result)

    {

      if (sServerPassword.equals("****") == false)

      {

        String[] names1 = {"TR069.MANAGEMENTSERVER.PASSWORD"};

        String[] values1 = {sServerPassword};

        result = setAndCheckAndStoreParameter(names1, values1);    

  

      }

    }

    return result;

  }



  public boolean setSTUNConfig(String sStunEnabled, String sStunServerUrl, String sStunServerPort, String sStunUserName, String sStunPassword, String sStunMaxKeepAlive, String sStunMinKeepAlive) 

  {

    String[] names = {"TR069.MANAGEMENTSERVER.STUN_ENABLED", "TR069.MANAGEMENTSERVER.STUN_SERVER_ADDRESS", "TR069.MANAGEMENTSERVER.STUN_SERVER_PORT", "TR069.MANAGEMENTSERVER.STUN_USERNAME", "TR069.MANAGEMENTSERVER.STUN_MAX_KEEP_ALIVE", "TR069.MANAGEMENTSERVER.STUN_MIN_KEEP_ALIVE"};

    String[] values = {sStunEnabled, sStunServerUrl, sStunServerPort, sStunUserName, sStunMaxKeepAlive, sStunMinKeepAlive};

    boolean result = setAndCheckAndStoreParameter(names, values);    

    if (result)

    {

      if (sStunPassword.equals("****") == false)

      {

        String[] names1 = {"TR069.MANAGEMENTSERVER.STUN_PASSWORD"};

        String[] values1 = {sStunPassword};

        result = setAndCheckAndStoreParameter(names1, values1);    

  

      }

    }

    return result;

  }



  public boolean setIGMP(String igmpEnable, String mldEnable, String broadcast, String broadcastMode, String filtering, String range, String fastLeave, String videoSourceMode)

  {

    if (getMcastFastleaveModeEnabledStored() != null && getMcastVideoSourceModeStored() != null)

    {

      String[] names = {"MCAST.GENERAL.IGMP_ENABLE", "MCAST.GENERAL.IGMP_IP_RANGES_DEF", "MCAST.GENERAL.IGMP_ENABLE", "MCAST.GENERAL.MLD_ENABLE", "MCAST.GENERAL.REPORT_BROADCAST_ALLOWED", "MCAST.GENERAL.REPORT_BROADCAST_MODE", "MCAST.GENERAL.MCAST_FILTERING_ENABLE", "MCAST.GENERAL.FAST_LEAVE_ENABLE", "MCAST.GENERAL.VIDEO_SOURCE_MODE"};

      String[] values = {"NO", range, igmpEnable, mldEnable, broadcast, broadcastMode, filtering, fastLeave, videoSourceMode};

      return setAndCheckAndStoreParameter(names, values);            	

    }

    else if (getMcastFilteringEnabledStored() != null && getReportBroadcastModeStored() != null)

    {

      String[] names = {"MCAST.GENERAL.IGMP_ENABLE", "MCAST.GENERAL.IGMP_IP_RANGES_DEF", "MCAST.GENERAL.IGMP_ENABLE", "MCAST.GENERAL.MLD_ENABLE", "MCAST.GENERAL.REPORT_BROADCAST_ALLOWED", "MCAST.GENERAL.REPORT_BROADCAST_MODE", "MCAST.GENERAL.MCAST_FILTERING_ENABLE"};

      String[] values = {"NO", range, igmpEnable, mldEnable, broadcast, broadcastMode, filtering};

      return setAndCheckAndStoreParameter(names, values);        

    }

    else

    {

      String[] names = {"MCAST.GENERAL.IGMP_ENABLE", "MCAST.GENERAL.IGMP_IP_RANGES_DEF", "MCAST.GENERAL.IGMP_ENABLE", "MCAST.GENERAL.MLD_ENABLE", "MCAST.GENERAL.REPORT_BROADCAST_ALLOWED"};

      String[] values = {"NO", range, igmpEnable, mldEnable, broadcast};

      return setAndCheckAndStoreParameter(names, values);        

    }

  }



  public boolean setExtendedSeedIdx(String idx) {

    return setAndCheckAndStoreParameter("NODE.GENERAL.EXTENDED_SEED_IDX", idx);

    

  }



  public boolean factoryReset() {

    if (this.isLegacy())

    {

      return super.factoryReset(MarvellNet.frstPassword);

    }

    else

    {

      return super.factoryReset(MarvellNet.cfgPassword);

    }

  }



  public boolean setCVLANTransparentMode() {

    String[] names = {"VLAN.CVLAN.ALLOWED_TAGS_IN_ETHA", 

        "VLAN.CVLAN.ALLOWED_TAGS_IN_ETHB", 

        "VLAN.CVLAN.ALLOWED_TAGS_IN_FW", 

        "VLAN.CVLAN.ALLOWED_TAGS_IN_SDIO", 

        "VLAN.CVLAN.CONFIG_IF_ETHA",

        "VLAN.CVLAN.CONFIG_IF_ETHB",

        "VLAN.CVLAN.CONFIG_IF_MGMT",

        "VLAN.CVLAN.CONFIG_IF_SDIO",

        "VLAN.CVLAN.FILTERING_ENABLE",

        "VLAN.CVLAN.PVID_ETHA",

        "VLAN.CVLAN.PVID_ETHB",

        "VLAN.CVLAN.PVID_MGMT",

        "VLAN.CVLAN.PVID_SDIO",

        "VLAN.CVLAN.ENABLE"

        };

    String[] values = {"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0", 

        "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0", 

        "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0", 

        "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0", 

        "NONE", 

        "NONE", 

        "NONE", 

        "NONE",

        "NO",

        "0",

        "0",

        "0",

        "0",

        "NO"

        };

    return setAndCheckAndStoreParameter(names, values);        

  }



  public boolean setCVLANAccessPortMode(String vlanMgmt, String vlanETHA, String vlanETHB) {

    String[] names = {"VLAN.CVLAN.ALLOWED_TAGS_IN_ETHA", 

        "VLAN.CVLAN.ALLOWED_TAGS_IN_ETHB", 

        "VLAN.CVLAN.ALLOWED_TAGS_IN_FW", 

        "VLAN.CVLAN.ALLOWED_TAGS_IN_SDIO", 

        "VLAN.CVLAN.CONFIG_IF_ETHA",

        "VLAN.CVLAN.CONFIG_IF_ETHB",

        "VLAN.CVLAN.CONFIG_IF_MGMT",

        "VLAN.CVLAN.CONFIG_IF_SDIO",

        "VLAN.CVLAN.FILTERING_ENABLE",

        "VLAN.CVLAN.PVID_ETHA",

        "VLAN.CVLAN.PVID_ETHB",

        "VLAN.CVLAN.PVID_MGMT",

        "VLAN.CVLAN.PVID_SDIO",

        "VLAN.CVLAN.ENABLE"

        };

    String[] values = {vlanETHA+",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0", 

        vlanETHB+",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0", 

        vlanMgmt+","+(vlanETHA.contentEquals(vlanMgmt)?"0":vlanETHA)+","+(vlanETHB.contentEquals(vlanMgmt)?"0":vlanETHB)+",0,0,0,0,0,0,0,0,0,0,0,0,0", 

        "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0", 

        "ACCESS", 

        (vlanETHB.contentEquals("0")?"NONE":"ACCESS"), 

        "TRUNK", 

        "NONE",

        "YES",

        vlanETHA,

        vlanETHB,

        vlanMgmt,

        "0",

        "YES"

        };

    return setAndCheckAndStoreParameter(names, values);      

  }



  public boolean setCVLANTrunkPortMode(String vlanMgmt, String allowedMgmt, String allowedETHA, String allowedETHB) {

    String[] names = {"VLAN.CVLAN.ALLOWED_TAGS_IN_ETHA", 

        "VLAN.CVLAN.ALLOWED_TAGS_IN_ETHB", 

        "VLAN.CVLAN.ALLOWED_TAGS_IN_FW", 

        "VLAN.CVLAN.ALLOWED_TAGS_IN_SDIO", 

        "VLAN.CVLAN.CONFIG_IF_ETHA",

        "VLAN.CVLAN.CONFIG_IF_ETHB",

        "VLAN.CVLAN.CONFIG_IF_MGMT",

        "VLAN.CVLAN.CONFIG_IF_SDIO",

        "VLAN.CVLAN.FILTERING_ENABLE",

        "VLAN.CVLAN.PVID_ETHA",

        "VLAN.CVLAN.PVID_ETHB",

        "VLAN.CVLAN.PVID_MGMT",

        "VLAN.CVLAN.PVID_SDIO",

        "VLAN.CVLAN.ENABLE"

        };

    String[] values = {allowedETHA, 

        allowedETHB, 

        allowedMgmt, 

        "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0", 

        "TRUNK", 

        (allowedETHB.contentEquals("0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0")?"NONE":"TRUNK"), 

        "TRUNK", 

        "NONE",

        "YES",

        "0",

        "0",

        vlanMgmt,

        "0",

        "YES"

        };

    return setAndCheckAndStoreParameter(names, values);      

  }

  

  public boolean setCVLANOnlyManagementMode(String vlanMgmt) {

    String[] names = {"VLAN.CVLAN.ALLOWED_TAGS_IN_ETHA", 

        "VLAN.CVLAN.ALLOWED_TAGS_IN_ETHB", 

        "VLAN.CVLAN.ALLOWED_TAGS_IN_FW", 

        "VLAN.CVLAN.ALLOWED_TAGS_IN_SDIO", 

        "VLAN.CVLAN.CONFIG_IF_ETHA",

        "VLAN.CVLAN.CONFIG_IF_ETHB",

        "VLAN.CVLAN.CONFIG_IF_MGMT",

        "VLAN.CVLAN.CONFIG_IF_SDIO",

        "VLAN.CVLAN.FILTERING_ENABLE",

        "VLAN.CVLAN.PVID_ETHA",

        "VLAN.CVLAN.PVID_ETHB",

        "VLAN.CVLAN.PVID_MGMT",

        "VLAN.CVLAN.PVID_SDIO",

        "VLAN.CVLAN.ENABLE"

        };

    String[] values = {"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0", 

        "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0", 

        "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0", 

        "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0", 

        "TRUNK", 

        "NONE", 

        "TRUNK", 

        "NONE",

        "NO",

        "0",

        "0",

        vlanMgmt,

        "0",

        "YES"

        };

    return setAndCheckAndStoreParameter(names, values);      

  }



  public boolean setProductionUnlock0Password(String s) {

    return setAndCheckParameter("SYSTEM.PRODUCTION.SECTOR0_UNLOCK_PASSWORD", s);

  }



  public String[] setVideoMacInfoGet(String video)

  {

    ArrayList<String> nodes = new ArrayList<String>();

    if (setAndCheckAndStoreParameter("MCAST.INFO.VIDEO_MAC_INFO_GET", video))

    {

      refresh();

      String[] s = getStoredConfigLayerParamValue("MCAST.INFO.VIDEO_MAC_DIDS").split(",");

      int node = 0;

      for (int i=0; i<s.length; i++)

      {

        long bitmap = Long.parseLong(s[i]);

        for (int j=0;j<32;j++)

        {

          if ((bitmap & 1) == 1)

          {

            nodes.add(String.valueOf(node));

          }

          bitmap = bitmap/2;

          node++;

        }

      }

      if (nodes.size() > 0)

      {

        return nodes.toArray(new String[nodes.size()]);

      }

      else

      {

        return null;

      }

    }

    else

    {

      return null;

    }

  }

  

  public boolean setAccessListAddMac(String mac, int position) 

  {

    return setAndCheckAndStoreParameter("NAP.GENERAL.ACCESS_ALLOWED_MAC_LIST."+position, mac);

  }



  public boolean setAccessListEnabled(String enabled) 

  {

    return setAndCheckAndStoreParameter("NAP.GENERAL.ACCESS_CONTROL_ENABLED", enabled);

  }

}

