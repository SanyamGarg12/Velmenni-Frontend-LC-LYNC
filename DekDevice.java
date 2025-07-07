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



import java.io.UnsupportedEncodingException;

import java.util.ArrayList;

import java.util.Arrays;

import java.util.Collections;



public class DekDevice 

{

  public byte type;

  public String iface;

  public byte[] mac;

  public String ipv4;

  public int vlanid = 0;



  private static final int TIMEOUT_DISCOVER = 20;

  private static final int TIMEOUT_GETSET = 10;

  

  public DekDevice(String iface, int vlanid, byte[] mac)

  {

    this.type = Command.DEVICETYPE_LCMP;

    this.iface = iface;

    this.mac = mac;

    this.ipv4 = "";

    this.vlanid = vlanid;

  }



  public DekDevice(String iface, byte[] mac)

  {

    this.type = Command.DEVICETYPE_LCMP;

    this.iface = iface;

    this.mac = mac;

    this.ipv4 = "";

    this.vlanid = 0;

  }

  

  public DekDevice(String ipv4)

  {

    this.type = Command.DEVICETYPE_HTTP;

    this.iface = "unknown";

    this.mac = new byte[6];

    this.ipv4 = ipv4;

    this.vlanid = 0;

  }

  

  @Override

  public String toString()

  {

    return String.format("%02X:%02X:%02X:%02X:%02X:%02X", mac[0], mac[1], mac[2], mac[3], mac[4], mac[5]);

  }

  

  public String getMacAsString()

  {

    return String.format("%02X:%02X:%02X:%02X:%02X:%02X", mac[0], mac[1], mac[2], mac[3], mac[4], mac[5]);

  }

  

  public boolean ethBoot()

  {

    return (type == Command.DEVICETYPE_ETHBOOT_V2 || type == Command.DEVICETYPE_ETHBOOT_V3);

  }

  

  public int ethBootVersion()

  {

    if (type == Command.DEVICETYPE_ETHBOOT_V2)

    {

      return 2;

    }

    else if (type == Command.DEVICETYPE_ETHBOOT_V3)

    {

      return 3;

    }

    else

    {

      return -1;

    }

    

  }

  

  public boolean isLegacy()

  {

    if (type == Command.DEVICETYPE_LEGACY)

    {

      return true;

    }

    else

    {

      return false;

    }

  }

  

  public boolean isHttp()

  {

    if (type == Command.DEVICETYPE_HTTP)

    {

      return true;

    }

    else

    {

      return false;

    }

  }

  

  public boolean checkType(String configPassword)

  {  

    boolean result = false;



    if (type == Command.DEVICETYPE_LCMP || type == Command.DEVICETYPE_LEGACY)

    {

      type = Command.DEVICETYPE_LCMP;

      DekParameter get = getConfiglayer(configPassword, "SYSTEM.PRODUCTION.MAC_ADDR");



      if (get == null)

      {

        type = Command.DEVICETYPE_LEGACY;

      }

      result = true;

    }

    else

    {

      result = true;

    }

    

    return result;

  }

  

  public DekParameter[] getAllConfiglayer(String configPassword)

  {

    return doConfiglayerOperation(configPassword, null, null);

  }

  

  public DekParameter   getConfiglayer(String configPassword, String paramName)

  {

    if (paramName != null)

    {

      String[] paramNames = new String[1];

      paramNames[0] = paramName;

      

//      System.out.println("DekDevice type " + this.type + " param " + paramName);

      DekParameter[] result = doConfiglayerOperation(configPassword, paramNames, null);

      if (result != null && result.length > 0)

      {

        return result[0];

      }

    }

    return null;

  }

  

  public DekParameter[] getConfiglayer(String configPassword, String[] paramNames)

  {

    if (paramNames != null)

    {

      return doConfiglayerOperation(configPassword, paramNames, null);

    }

    else

    {

      return null;

    }

  }

  

  public DekParameter   setConfiglayer(String configPassword, String paramName, String paramValue)

  {

    if (paramName != null && paramValue != null)

    {

      String[] paramNames = new String[1];

      paramNames[0] = paramName;

      String[] paramValues = new String[1];

      paramValues[0] = paramValue;

      

      DekParameter[] result = doConfiglayerOperation(configPassword, paramNames, paramValues);

      if (result != null && result.length > 0)

      {

        return result[0];

      }

    }

    return null;

  }

  

  public DekParameter[] setConfiglayer(String configPassword, String[] paramNames, String[] paramValues)

  {

    if (paramNames != null && paramValues != null)

    {

      return doConfiglayerOperation(configPassword, paramNames, paramValues);

    }

    else

    {

      return null;

    }

  }

  

  private DekParameter[] doConfiglayerOperation(String configPassword, String[] paramNames, String[] paramValues)

  {

    if (type == Command.DEVICETYPE_HTTP)

    {

      boolean logged = false;

      int max_retries = 3;

      int retries = 0;

      

      // Use HTTP commands

      // The login has a timeout, so use always

      while (logged == false && retries++ < max_retries)

      {

        logged = HttpCommands.login(ipv4, configPassword);

      }

      if (paramNames == null)

      {

        DekParameter[] params = null;

        retries = 0;

        while (params == null && retries++ < max_retries)

        {

          params = HttpCommands.getAll(ipv4);

        }

        return params;

      }

      else if (paramValues == null)

      {

        DekParameter[] result = new DekParameter[paramNames.length];

        DekParameter[] all = null;

        retries = 0;

        while (all == null && retries++ < max_retries)

        {

          all = HttpCommands.getAll(ipv4);

        }

        if (all != null)

        {

          for (int i = 0; i < paramNames.length; i++)

          {

            for (int j = 0; j < all.length; j++)

            {

              if (all[j].name.contentEquals(paramNames[i]))

              {

                result[i] = all[j];

                break;

              }

            }

          }

          return result;

        }

        else

        {

          return null;

        }

      }

      else

      {

        boolean httpok = false;

        retries = 0;

        while (httpok == false && retries++ < max_retries)

        {

          httpok = HttpCommands.set(ipv4, paramNames, paramValues);

        }

        if (httpok)

        {

          DekParameter[] result = new DekParameter[paramNames.length];

          for (int i = 0; i < paramNames.length; i++)

          {

            DekParameter parameter = new DekParameter();

            parameter.name = paramNames[i];

            parameter.nElem = 1;

            parameter.type = 0;

            parameter.value = "OK";

            result[i] = parameter;

          }

          return result;

        }

        else

        {

          return null;

        }

      }

    }

    else

    {

      ArrayList<DekParameter> arrayList;

      DekParameter[] result = null;

      byte[] command;

      if (paramNames == null)

      {

        command = Command.buildGetAllCongiglayerMsg(this.type, this.iface, this.vlanid, this.mac, configPassword);

      }

      else if (paramValues == null)

      {

        command = Command.buildGetCongiglayerMsg(this.type, this.iface, this.vlanid, this.mac, configPassword, paramNames);

      }

      else

      {

        command = Command.buildSetCongiglayerMsg(this.type, this.iface, this.vlanid, this.mac, configPassword, paramNames, paramValues);

      }

      byte[] reply = Command.sendMsg(command, TIMEOUT_GETSET);



      if ((reply != null) && ((Command.getMsgType(reply) == Command.MSGTYPE_PARAMS) || (Command.getMsgType(reply) == Command.MSGTYPE_PARAMSXT)))

      {

        int datalength = Command.getMsgParamsDataLength(reply);



        arrayList = new ArrayList<DekParameter>();

        DekParameter param = null;

        boolean value = false;

        int offset = Command.getMsgParamsDataOffset(reply);

        while (offset < datalength+Command.getMsgParamsDataOffset(reply))

        {

          int start = offset; 

          for (offset = start; offset < datalength+Command.getMsgParamsDataOffset(reply); offset++)

          {

            if (reply[offset] == 0)

            {

              byte[] auxarray = Arrays.copyOfRange(reply, start, offset);

              String aux;

              try {

                aux = new String(auxarray, "UTF-8");

                if (value)

                {

                  param.value = aux;

                  if (Command.getMsgType(reply) == Command.MSGTYPE_PARAMS)

                  {

                    param.type = (byte) 253;

                    param.nElem = 0;

                    offset++;

                  }

                  else

                  {

                    offset++;

                    param.type = reply[offset];

                    offset++;

                    param.nElem = (((int)(reply[offset])<<24)&0xFF000000)+(((int)(reply[offset+1])<<16)&0xFF0000)+(((int)(reply[offset+2])<<8)&0xFF00)+(((int)(reply[offset+3]))&0xFF);

                    offset+=4;

                  }

                  arrayList.add(param);

                  value = false;

                }

                else

                {

                  param = new DekParameter();

                  param.name = aux;

                  value = true;

                  offset++;

                }

              } catch (UnsupportedEncodingException e) {

                // TODO Auto-generated catch block

                e.printStackTrace();

              }

              start = offset;

              break;

            }

          }

        }

        Collections.sort(arrayList);

        result = arrayList.toArray(new DekParameter[]{});

      }

      else

      {

        System.err.println("DekLib: Error: invalid reply from DekHost (invalid message).");

      }



      return result;

    }

  }

  

  public boolean factoryReset(String frstPassword)

  {

    boolean result = false;

    byte[] command;

    byte[] reply;

    

    command = Command.buildFactoryResetMsg(this.type, this.iface, this.vlanid, this.mac, frstPassword);

    reply = Command.sendMsg(command, TIMEOUT_GETSET);

    

    if (reply != null)

    {

      if (Command.getMsgType(reply) == Command.MSGTYPE_COMMANDOK)

      {

        result = true;

      }

      else

      {

        System.err.println("DekLib: Error: invalid reply from DekHost (invalid message).");

      }

    }

    

    return result;  

  }

  

  static public DekDevice[] discover()

  {

    return discover(0);

  }

  

  static public DekDevice[] discover(int vlanid)

  {

    DekDevice[] devices = null;

    byte[] command = Command.buildDiscoverMsg(vlanid);

    byte[] result = Command.sendMsg(command, TIMEOUT_DISCOVER);

    

    if (result != null)

    {

//      System.err.println("DekLib: vlandid " + vlanid + "seq " + result[0] + "segs " + result[1] + 

//          "segnum " + result[2] + 

//          " type " + result[3] + " devices " + result[3 + 1]);

      devices = Command.createDevicesFromDiscoverResponse(result, vlanid);

    }

    return devices;

  }

}

