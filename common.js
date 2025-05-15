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
 *                                          Copyright (c) 2016/2020, MaxLinear, Inc.
 *  ***************************************************************************************
 *  </legal_notice>
 */

/*
  List of CFL errors:

  CONFIGLAYER_ERR_NONE = 0,     ////< No error, result oK
  CONFIGLAYER_ERR_NOT_SUPPORTED = -1, ////< Requested action not supported (ex.: dummy package, ...)
  CONFIGLAYER_ERR_NOT_INITIALIZED = -2, ////< Component not initialized
  CONFIGLAYER_ERR_NOT_STARTED = -3, ////< Component not started
  CONFIGLAYER_ERR_BAD_ARGS = -4,  ////< Wrong parameter(s)
  CONFIGLAYER_ERR_NO_MEMORY = -5, ////< Not enough memory
  // Error codes from 0 to -31 inclusive are reserved to be common errors to all components
  CONFIGLAYER_ERR_OTHER = -31,  ////< Unknown error
  // Errors specific to the component are defined here starting at -32
  CONFIGLAYER_ERR_GROUP_ALREADY_INSTALLED = -32,  ///< Trying to install a group twice
  CONFIGLAYER_ERR_SUBGROUP_ALREADY_INSTALLED = -33, ///< Trying to install a Subgroup twice
  CONFIGLAYER_ERR_GROUP_NOT_INSTALLED = -34,  ///< Searching for a group that has not been installed
  CONFIGLAYER_ERR_SUBGROUP_NOT_INSTALLED = -35, ///< Searching for a subgroup that has not been installed
  CONFIGLAYER_ERR_PARAM_NOT_INSTALLED = -36,  ///< Searching for a parameter that has not been installed
  CONFIGLAYER_ERR_PARAM_NOT_IN_CACHE = -37, ///< Parameter not in cache
  CONFIGLAYER_ERR_PARAM_SET = -38,  ///< Error detected when calling param set function
  CONFIGLAYER_ERR_PARAM_CHANGE = -39, ///< Error detected when calling param change function
  CONFIGLAYER_ERR_PARAM_GET = -40,  ///< Error detected when calling param get function
  CONFIGLAYER_ERR_CONVERT_PARAM_STRING = -41, ///< Could not find a parameter matching an old parameter string
  CONFIGLAYER_ERR_CONVERT_PARAM_CODE = -42, ///< Could not find a parameter matching an old parameter code
  CONFIGLAYER_ERR_CONVERT_PARAM_VALUE = -43,  ///< Could not convert an old parameter value to the new format
  CONFIGLAYER_ERR_NO_SET_CALLBACK = -44,  ///< No 'set' function callback was registered for the parameter
  CONFIGLAYER_ERR_NO_GET_CALLBACK = -45,  ///< No 'get' function callback was registered for the parameter
  CONFIGLAYER_ERR_NON_READABLE_PARAMETER = -46, ///< Cannot register 'get' function for non readable parameter
  CONFIGLAYER_ERR_NON_WRITABLE_PARAMETER = -47, ///< Cannot register 'set' function for non writable parameter
  CONFIGLAYER_ERR_COULD_NOT_OPEN_CONFIG_FILE = -48, ///< Cannot open configuration file
  CONFIGLAYER_ERR_CORRUPT_CONFIG_FILE = -49,  ///< The configuration file is corrupt
  CONFIGLAYER_ERR_NO_MORE_PARAMS_IN_FILE = -50, ///< There are no more parameters in the config file for the current subgroup
  CONFIGLAYER_ERR_GROUP_NOT_IN_FILE = -51,  ///< There is no such group in the configuration file
  CONFIGLAYER_ERR_SUBGROUP_NOT_IN_FILE = -52, ///< There is no such subgroup in the configuration file
  CONFIGLAYER_ERR_PARAM_NOT_IN_FILE = -53,  ///< There is no such param in the configuration file
  CONFIGLAYER_ERR_STORED_FLAG_NOT_SET = -54,  ///< The stored flag for the parameter is not set. File operation failed.
  CONFIGLAYER_ERR_PERMIT_NOT_GRANTED = -55, ///< Trying to perform an operation without the necessary permits
  CONFIGLAYER_ERR_COULD_NOT_OPEN_DUMP_FILE = -56, ///< Cannot open file to dump information
  CONFIGLAYER_ERR_HASH_ERROR = -57, ///< Private file hash is not correct
  CONFIGLAYER_ERR_INDEX_TOO_HIGH = -58, ///< Index used for a parameter is larger than number of elements
  CONFIGLAYER_ERR_CALLBACK_NOT_FOUND = -59, ///< Callback passed not registered for this parameter
  CONFIGLAYER_ERR_API_SET_ONLY_PARAMETER = -60, ///< Parameter cannot be set via console, scp, autoconf...
  CONFIGLAYER_ERR_CONFIG_FILES_UPDATE = -61,  ///< There was an error during the process of updating the config files
  CONFIGLAYER_ERR_COULD_NOT_ADD_PARAM_TYPE = -62, ///< There was an error during the process of adding a nwe parameter type
  CONFIGLAYER_ERR_ILLEGAL_PARAMETER_NAME = -63, ///< A parameter has been defined with too many '_' in it
  CONFIGLAYER_ERR_NO_TXT_CONFIGURATION_ALLOWED = -64, ///< The parameter cannot be configured via TXT files.
  CONFIGLAYER_ERR_CANNOT_CHANGE_PARAM_SIZE = -65, ///< A parameter with STORED flag on cannot change its size at run time
  CONFIGLAYER_ERR_WRONG_PERMIT = -66, ///< Trying to perform an operation with wrong permit set
  CONFIGLAYER_ERR_DOCUMENTATION = -67, ///< There is some error in the parameter documentation. It must not be left empty.
  CONFIGLAYER_ERR_PARAM_CHECK = -68,  ///< Error detected when calling param value check function
  CONFIGLAYER_ERR_PARAM_NOT_STORED = -69,  ///< Error detected when performing a Store operation over a non stored
CONFIGLAYER_ERR_FILE_ACCESS = -70,  ///< Error accessing configuration file
*/

function printErrorMsg(err)
{
  if(err!="")
    document.write('<div style="padding:10px;font-size:18px"><p><i><font color="red">'+err+'</font></i></p></div>');
}

function selectOptions(s,o)
{
  for(var i=0;i<o.length;i++)
    document.write('<option value="'+o[i]+'" '+((o[i]==s)?'selected':'')+'>'+o[i]+'</option>');
}

function selectOptionsWithText(s,o,ot)
{
  for(var i=0;i<o.length;i++)
    document.write('<option value="'+ot[i]+'" '+((ot[i]==s)?'selected':'')+'>'+o[i]+'</option>');
}

