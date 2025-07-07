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



package deklib;



import java.io.IOException;

import java.io.UnsupportedEncodingException;

import java.net.DatagramPacket;

import java.net.DatagramSocket;

import java.net.InetAddress;

import java.net.SocketException;

import java.net.UnknownHostException;

import java.util.ArrayList;

import java.util.Arrays;



class Command {



  private static final byte MSGTYPE_COMMAND = (byte) 0;

  private static final byte DISCOVER = (byte) 0;



  private static final byte GETALLCONFIGLAYER = (byte) 2;

  private static final byte GETCONFIGLAYER = (byte) 3;

  private static final byte SETCONFIGLAYER = (byte) 4;

  private static final byte FACTORYRESET = (byte) 5;

  private static final byte STARTL2FWSERVER = (byte) 6;

  private static final byte STOPL2FWSERVER = (byte) 7;

  private static final byte GETSTATUSL2FWSERVER = (byte) 8;  

  private static final byte GETDEKVERSION = (byte) 9;

  

  static final byte MSGTYPE_COMMANDOK = (byte) 1;

  static final byte MSGTYPE_DEVICE = (byte) 5;

  static final byte MSGTYPE_PARAMS = (byte) 6;

  static final byte MSGTYPE_PARAMSXT = (byte) 7;

  static final byte MSGTYPE_L2FWSERVERSTATUS = (byte) 8;

  static final byte MSGTYPE_DEKVERSION = (byte) 9;



  static final byte DEVICETYPE_ETHBOOT_V2 = (byte) 0;

  static final byte DEVICETYPE_ETHBOOT_V3 = (byte) 1;

  static final byte DEVICETYPE_LEGACY = (byte) 2;

  static final byte DEVICETYPE_LCMP = (byte) 3;

  static final byte DEVICETYPE_HTTP = (byte) 4;

  

  static final byte L2FWSERVER_STATE_IDLE = (byte) 0;

  static final byte L2FWSERVER_STATE_WAITING = (byte) 1;

  static final byte L2FWSERVER_STATE_UPLOADING = (byte) 2;

  static final byte L2FWSERVER_STATE_UPLOADING_ETHBOOT_LOADER = (byte) 3;

  static final byte L2FWSERVER_STATE_UPLOADING_ETHBOOT_FW = (byte) 4;

  static final byte L2FWSERVER_STATE_SUCCESSFUL = (byte) 5;

  static final byte L2FWSERVER_STATE_ERROR = (byte) 6;

  

  static final int SEQNUM_OFFSET = 0;

  static final int SEGTOTAL_OFFSET = 1;

  static final int SEGNUM_OFFSET = 2;

  static final int MSGTYPE_OFFSET = 3;

  static final int PARAMS_OFFSET = MSGTYPE_OFFSET + 5;

  

  static int serverPort = 55567;

  static String serverUrl = "localhost";

  static final int MAX_REPLY_LENGTH = 100000;

  static final int MAX_SEGMENT_LENGTH = 2000;

  static final int MAX_NUM_SEGMENTS   = 512;

  

  static long sequenceNumber = 0;

  

  static byte[] buildDiscoverMsg(int vlanid)

  {

    // SEGMENTATION + MSGTYPE + COMMAND + INTERFACE + VLAN

    byte[] msg = new byte[3+1+1+1+2];

    int offset = 0;



    msg[offset++] = (byte) sequenceNumber++;

    msg[offset++] = 1; // 1 segment

    msg[offset++] = 0; // first segment

    msg[offset++] = MSGTYPE_COMMAND;

    msg[offset++] = DISCOVER;

    msg[offset++] = 0; // No interface

    msg[offset++] = (byte)((vlanid>>8)&0xFF);

    msg[offset++] = (byte)(vlanid&0xFF);

    

    return msg;

  }

    

  static byte[] buildFactoryResetMsg(byte deviceType, String iface, int vlanid, byte[] mac, String frstPassword)

  {

    byte[] fpw = frstPassword.getBytes();

    // SEGMENTATION MSGTYPE + COMMAND + DEVICE_TYPE + IFACE_LENGTH + IFACE + VLAN + MAC + PASSWORD_LENGTH + PASSWORD

    byte[] msg = new byte[3+1+1+1+1+iface.length()+2+6+1+fpw.length];

    int i;

    int offset = 0;

    

    msg[offset++] = (byte) sequenceNumber++;

    msg[offset++] = 1; // 1 segment

    msg[offset++] = 0; // first segment

    msg[offset++] = MSGTYPE_COMMAND;

    msg[offset++] = FACTORYRESET;

    msg[offset++] = (byte)deviceType;

    msg[offset++] = (byte)iface.length(); // No interface

    for (i = 0; i < iface.length(); i++)

    {

      msg[offset++] = (byte)iface.charAt(i); 

    }

    msg[offset++] = (byte)((vlanid>>8)&0xFF);

    msg[offset++] = (byte)(vlanid&0xFF);

    msg[offset++] = mac[0];

    msg[offset++] = mac[1];

    msg[offset++] = mac[2];

    msg[offset++] = mac[3];

    msg[offset++] = mac[4];

    msg[offset++] = mac[5];   

    msg[offset++] = (byte)frstPassword.length(); // No interface

    for (i = 0; i < fpw.length; i++)

    {

      msg[offset++] = fpw[i]; 

    }

    

    return msg;

  }

    

  static byte[] buildGetAllCongiglayerMsg(byte deviceType, String iface, int vlanid, byte[] mac, String password)

  {

    byte[] pw = password.getBytes();

    // SEGMENTATION + MSGTYPE + COMMAND + DEVICETYPE + IFACE_LENGTH + IFACE + VLAN + MAC + PASSWORDLENGTH + PASSWORD

    byte[] msg = new byte[3+1+1+1+1+iface.length()+2+6+1+pw.length];

    int i;

    int offset = 0;

    

    msg[offset++] = (byte) sequenceNumber++;

    msg[offset++] = 1; // 1 segment

    msg[offset++] = 0; // first segment

    msg[offset++] = MSGTYPE_COMMAND;

    msg[offset++] = GETALLCONFIGLAYER;

    msg[offset++] = deviceType;

    msg[offset++] = (byte)iface.length();

    for (i = 0; i < iface.length(); i++)

    {

      msg[offset++] = (byte)iface.charAt(i); 

    }

    msg[offset++] = (byte)((vlanid>>8)&0xFF);

    msg[offset++] = (byte)(vlanid&0xFF);

    msg[offset++] = mac[0];

    msg[offset++] = mac[1];

    msg[offset++] = mac[2];

    msg[offset++] = mac[3];

    msg[offset++] = mac[4];

    msg[offset++] = mac[5];

    

    msg[offset++] = (byte) pw.length;

    for (i = 0; i < pw.length; i++)

    {

      msg[offset++] = pw[i];

    }



    return msg;

  }

    

  static byte[] buildGetCongiglayerMsg(byte deviceType, String iface, int vlanid, byte[] mac, String password, String[] paramNames)

  {

    int i;

    int offset;

    int paramsLength = 0;

    String params = "";

    

    for (i = 0; i<paramNames.length; i++)

    {

      params += paramNames[i]+"\0";

    }

    

    byte[] pw = password.getBytes();

    byte[] param_value = params.getBytes();

    paramsLength = param_value.length;

    // SEGMENTATION + MSGTYPE + COMMAND + DEVICETYPE + IFACE_LENGTH + IFACE + VLAN + MAC + PASSWORDLENGTH + PASSWORD + PARAMSLENGTH + PARAMS

    byte[] msg = new byte[3+1+1+1+1+iface.length()+2+6+1+pw.length+4+paramsLength];

    

    offset = 0;

    msg[offset++] = (byte) sequenceNumber++;

    msg[offset++] = 1; // 1 segment

    msg[offset++] = 0; // first segment

    msg[offset++] = MSGTYPE_COMMAND;

    msg[offset++] = GETCONFIGLAYER;

    msg[offset++] = deviceType;

    msg[offset++] = (byte)iface.length();

    for (i = 0; i < iface.length(); i++)

    {

      msg[offset++] = (byte)iface.charAt(i); 

    }

    msg[offset++] = (byte)((vlanid>>8)&0xFF);

    msg[offset++] = (byte)(vlanid&0xFF);

    msg[offset++] = mac[0];

    msg[offset++] = mac[1];

    msg[offset++] = mac[2];

    msg[offset++] = mac[3];

    msg[offset++] = mac[4];

    msg[offset++] = mac[5];

    

    msg[offset++] = (byte) pw.length;

    for (i = 0; i < pw.length; i++)

    {

      msg[offset++] = pw[i];

    }



    msg[offset++] = (byte)((paramsLength>>24)&0xFF);

    msg[offset++] = (byte)((paramsLength>>16)&0xFF);

    msg[offset++] = (byte)((paramsLength>>8)&0xFF);

    msg[offset++] = (byte)(paramsLength&0xFF);



    for (i = 0; i < param_value.length; i++)

    {

      msg[offset++] = param_value[i];

    }

    return msg;

  }

    

  static byte[] buildSetCongiglayerMsg(byte deviceType, String iface, int vlanid, byte[] mac, String password, String[] paramNames, String[] paramValues)

  {

    int i;

    int offset;

    int paramsLength = 0;

    String params = "";

    for (i = 0; i<paramNames.length; i++)

    {

      params += paramNames[i]+"\0"+paramValues[i]+"\0";

    }

    byte[] pw = password.getBytes();

    byte[] param_value = params.getBytes();

    paramsLength = param_value.length;

    

    // SEGMENTATION + MSGTYPE + COMMAND + DEVICETYPE + IFACE_LENGTH + IFACE + VLAN + MAC + PASSWORD_LENGTH + PASSWORD + PARAMSLENGTH + PARAMS

    byte[] msg = new byte[3+1+1+1+1+iface.length()+2+6+1+pw.length+4+paramsLength];

    

    offset = 0;

    msg[offset++] = (byte) sequenceNumber++;

    msg[offset++] = 1; // 1 segment

    msg[offset++] = 0; // first segment

    msg[offset++] = MSGTYPE_COMMAND;

    msg[offset++] = SETCONFIGLAYER;

    msg[offset++] = deviceType;

    msg[offset++] = (byte)iface.length();

    for (i = 0; i < iface.length(); i++)

    {

      msg[offset++] = (byte)iface.charAt(i); 

    }

    msg[offset++] = (byte)((vlanid>>8)&0xFF);

    msg[offset++] = (byte)(vlanid&0xFF);

    msg[offset++] = mac[0];

    msg[offset++] = mac[1];

    msg[offset++] = mac[2];

    msg[offset++] = mac[3];

    msg[offset++] = mac[4];

    msg[offset++] = mac[5];

    

    msg[offset++] = (byte) pw.length;

    for (i = 0; i < pw.length; i++)

    {

      msg[offset++] = pw[i];

    }

    msg[offset++] = (byte)((paramsLength>>24)&0xFF);

    msg[offset++] = (byte)((paramsLength>>16)&0xFF);

    msg[offset++] = (byte)((paramsLength>>8)&0xFF);

    msg[offset++] = (byte)(paramsLength&0xFF);



    for (i = 0; i < param_value.length; i++)

    {

      msg[offset++] = param_value[i];

    }

    

    return msg;

  }



  public static byte[] buildStartL2FwServerMsg(String iface, int vlanid, byte[] mac, String file, String ethloader) 

  {

    // SEGMENTATION + MSGTYPE + COMMAND + IFACE_LENGTH + IFACE + VLAN + MAC + FILE_LENGTH + FILE + LOADER_LENGTH + LOADER

    int loader_length = 0;

    if (ethloader != null)

    {

      loader_length = ethloader.length();

    }

    byte[] msg = new byte[3+1+1+1+iface.length()+2+6+2+file.length()+2+loader_length];

    int i;

    int offset = 0;

    

    msg[offset++] = (byte) sequenceNumber++;

    msg[offset++] = 1; // 1 segment

    msg[offset++] = 0; // first segment

    msg[offset++] = MSGTYPE_COMMAND;

    msg[offset++] = STARTL2FWSERVER;

    msg[offset++] = (byte)iface.length(); // No interface

    for (i = 0; i < iface.length(); i++)

    {

      msg[offset++] = (byte)iface.charAt(i); 

    }

    msg[offset++] = (byte)((vlanid>>8)&0xFF);

    msg[offset++] = (byte)(vlanid&0xFF);

    msg[offset++] = mac[0];

    msg[offset++] = mac[1];

    msg[offset++] = mac[2];

    msg[offset++] = mac[3];

    msg[offset++] = mac[4];

    msg[offset++] = mac[5];  

    

    msg[offset++] = (byte)((file.length()>>8)&0xFF);

    msg[offset++] = (byte)(file.length()&0xFF);

    for (i = 0; i < file.length(); i++)

    {

      msg[offset++] = (byte)file.charAt(i); 

    }

    if (ethloader == null)

    {

      msg[offset++] = (byte)(0);

      msg[offset++] = (byte)(0);

    }

    else

    {

      msg[offset++] = (byte)((ethloader.length()>>8)&0xFF);

      msg[offset++] = (byte)(ethloader.length()&0xFF);

      for (i = 0; i < ethloader.length(); i++)

      {

        msg[offset++] = (byte)ethloader.charAt(i); 

      }

    }

    

    return msg;

  }



  public static byte[] buildGetStatusL2FwServerMsg() 

  {

    // SEGMENTATION + MSGTYPE + COMMAND

    byte[] msg = new byte[3+1+1];

    int offset = 0;

    

    msg[offset++] = (byte) sequenceNumber++;

    msg[offset++] = 1; // 1 segment

    msg[offset++] = 0; // first segment

    msg[offset++] = MSGTYPE_COMMAND;

    msg[offset++] = GETSTATUSL2FWSERVER;

    return msg;

  }



  static byte[] concat(DatagramPacket[] segments, int n_segments) 

  {

    byte[] merge = Arrays.copyOfRange(segments[0].getData(), 0, segments[0].getLength());

    for (int i=1; i<n_segments; i++)

    {

      byte[] bmod = Arrays.copyOfRange(segments[i].getData(), MSGTYPE_OFFSET, segments[i].getLength());

      byte[] merge2 = new byte[merge.length+bmod.length];

      System.arraycopy(merge, 0, merge2, 0, merge.length);

      System.arraycopy(bmod, 0, merge2, merge.length, bmod.length);

      merge = merge2;

    }

    return merge;

  }

  

  public static byte[] buildGetDekVersionMsg() {

    // SEGMENTATION + MSGTYPE + COMMAND

    byte[] msg = new byte[3+1+1];

    int offset = 0;

    

    msg[offset++] = (byte) sequenceNumber++;

    msg[offset++] = 1; // 1 segment

    msg[offset++] = 0; // first segment

    msg[offset++] = MSGTYPE_COMMAND;

    msg[offset++] = GETDEKVERSION;

    return msg;  

  }



  public static byte[] buildStopL2FwServerMsg() {

    // SEGMENTATION + MSGTYPE + COMMAND

    byte[] msg = new byte[3+1+1];

    int offset = 0;

    

    msg[offset++] = (byte) sequenceNumber++;

    msg[offset++] = 1; // 1 segment

    msg[offset++] = 0; // first segment

    msg[offset++] = MSGTYPE_COMMAND;

    msg[offset++] = STOPL2FWSERVER;

    return msg;  

  }



  public static byte[] sendMsg(byte[] message, int timeout)

  {

    byte[] reply = null;

    DatagramPacket[] segments = new DatagramPacket[MAX_NUM_SEGMENTS];

    int n_segments = 0;

    boolean waiting_seg = true;

    int txseqnum = 0, rxseqnum, rxsegnum, rxsegtotal;

    

    // Create a pool for segments

    for (int i=0; i<MAX_NUM_SEGMENTS; i++)

    {

      byte[] segment_data = new byte[MAX_SEGMENT_LENGTH];

      segments[i] = new DatagramPacket(segment_data, segment_data.length);

    }

    

    DatagramSocket clientSocket;

    try {

      clientSocket = new DatagramSocket();

    } catch (SocketException e) {

      e.printStackTrace();

      return null;

    }

    InetAddress IPAddress;

    try {

      IPAddress = InetAddress.getByName(serverUrl);

    } catch (UnknownHostException e) {

      clientSocket.close();

      e.printStackTrace();

      return null;

    }

    DatagramPacket sendPacket = new DatagramPacket(message, message.length, IPAddress, serverPort);

    try {

      clientSocket.send(sendPacket);

      clientSocket.setSoTimeout(timeout*1000);

      clientSocket.setReceiveBufferSize(MAX_NUM_SEGMENTS*MAX_SEGMENT_LENGTH);

      txseqnum = message[SEQNUM_OFFSET];

    } catch (IOException e) {

      clientSocket.close();

      e.printStackTrace();

      return null;

    }

//    System.out.println("DekLib: Sent message "+message.length+" bytes");

    

    while (waiting_seg)

    {

      try {

        clientSocket.receive(segments[n_segments]);

      } catch (IOException e) {

        clientSocket.close();

        System.err.println("DekLib: Timeout error ("+timeout+"s)");

        return null;

      }



      rxseqnum = segments[n_segments].getData()[SEQNUM_OFFSET];

      rxsegnum = segments[n_segments].getData()[SEGNUM_OFFSET];

      rxsegtotal = segments[n_segments].getData()[SEGTOTAL_OFFSET];

      

      if (rxseqnum != txseqnum)

      {

        System.err.println("DekLib: Segmentation error (unexpected sequence number)"+rxseqnum+","+txseqnum);

        clientSocket.close();

        return null;

      }

      if (rxsegnum != n_segments)

      {

        System.err.println("DekLib: Segmentation error (unexpected segment number)");

        clientSocket.close();

        return null;

      }

      n_segments++;

      if ((n_segments == rxsegtotal) || (n_segments >= MAX_NUM_SEGMENTS))

      {

        waiting_seg = false;

      }

      else

      {

        timeout = 1; // Now no need long timeout

      }

    }

    if (n_segments > 0)

    {

      reply = concat(segments, n_segments);

    }

//    System.out.println("DekLib: Received: "+reply.length+" bytes");



    clientSocket.close();

    

    return reply;

  }

  

  public static int getDekHostVersion() {

    // TODO Auto-generated method stub

    byte[] command = Command.buildGetDekVersionMsg();

    

    byte[] reply = Command.sendMsg(command, 3);

    if ((reply != null) && (reply[MSGTYPE_OFFSET] == Command.MSGTYPE_DEKVERSION))

    {

      return (((reply[MSGTYPE_OFFSET+1]<<16)&0xFF0000) + ((reply[MSGTYPE_OFFSET+2]<<8)&0xFF00) +((reply[MSGTYPE_OFFSET+3])&0xFF));

    }

    else

    {

      return -1;

    }

  }



  public static byte getMsgType(byte[] reply) {

    return reply[MSGTYPE_OFFSET];

  }



  public static byte getDeviceType(byte[] reply) {

    return reply[MSGTYPE_OFFSET+2];

  }



  public static int getMsgParamsDataLength(byte[] reply) {

    return (((int)(reply[MSGTYPE_OFFSET+1])<<24)&0xFF000000)+(((int)(reply[MSGTYPE_OFFSET+2])<<16)&0xFF0000)+(((int)(reply[MSGTYPE_OFFSET+3])<<8)&0xFF00)+(((int)(reply[MSGTYPE_OFFSET+4]))&0xFF);

  }



  public static int getMsgParamsDataOffset(byte[] reply) {

    return PARAMS_OFFSET;

  }



  public static DekDevice[] createDevicesFromDiscoverResponse(byte[] reply, int vlanid) {

    DekDevice[] newdevices = null;

    byte type;

    byte[] ifacesubarray;

    String iface;

    int ifacelen;

    byte[] mac;

    int ndevices;

    int offset = MSGTYPE_OFFSET;



    if (reply[offset] == Command.MSGTYPE_DEVICE)

    {

      offset++;

      ndevices = reply[offset];

      offset++;

      if (ndevices > 0)

      {

        newdevices = new DekDevice[ndevices];



        for (int i = 0; i<ndevices; i++)

        {

          type = reply[offset];

          offset++;

          ifacelen = (((int)(reply[offset]))&0xFF);

          offset++;

          ifacesubarray = Arrays.copyOfRange(reply, offset, offset+ifacelen);

          offset+=ifacelen;

          try {

            iface = new String(ifacesubarray, "UTF-8");

          } catch (UnsupportedEncodingException e) {

          // TODO Auto-generated catch block

            e.printStackTrace();

            return null;

          }

          mac = Arrays.copyOfRange(reply, offset, offset+6);

          offset+=6;

          newdevices[i] = new DekDevice(iface, vlanid, mac);

          newdevices[i].type = type;

        }

      }

      else

      {

        System.err.println("No devices discovered (" + ndevices + ")");

      }

    }

    return newdevices;

  }



  public static int getMsgL2FwServerStatusUploadedBytes(byte[] reply) {

    return (((int)(reply[MSGTYPE_OFFSET+2])<<24)&0xFF000000)+(((int)(reply[MSGTYPE_OFFSET+3])<<16)&0xFF0000)+(((int)(reply[MSGTYPE_OFFSET+4])<<8)&0xFF00)+(((int)(reply[MSGTYPE_OFFSET+5]))&0xFF);  

  }

  

  public static int getMsgL2FwServerStatusTotalBytes(byte[] reply) {

    return (((int)(reply[MSGTYPE_OFFSET+6])<<24)&0xFF000000)+(((int)(reply[MSGTYPE_OFFSET+7])<<16)&0xFF0000)+(((int)(reply[MSGTYPE_OFFSET+8])<<8)&0xFF00)+(((int)(reply[MSGTYPE_OFFSET+9]))&0xFF);  

  }



  public static int getMsgL2FwServerStatus(byte[] reply) {

    return reply[MSGTYPE_OFFSET+1];

  }

}

