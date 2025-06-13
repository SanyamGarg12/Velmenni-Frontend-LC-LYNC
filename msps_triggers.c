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
 *                                          Copyright (c) 2017/2020, MaxLinear, Inc.
 *  ***************************************************************************************
 *  </legal_notice>
 */

/**
 * @addtogroup msps
 * @{
 **/

/**
 * @file msps_triggers.c
 * @brief Implementation of the triggers of Msps
 *
 * @internal
 *
 * @author Maria Esteve
 * @date October 2017
 *
 **/

/*
 ************************************************************************ 
 ** File description
 ************************************************************************

Power saving scheme is based on the L1 mechanism described by ITU in G.9961 standard.
(Section 8.21 Operation in power saving modes in document Rec. ITU T G.9961 (2015)/Cor.3 (11/2016))

This mechanism defines a way for nodes to request the Domain Master (DM) to enter powersaving during part of the MAC cycle. 
For this purpose, the MAC Cycle is divided into slots of 1/16th and each End Point (EP) can request which slots it would like to use for power saving.
This decision will be taken based on traffic, temperature, or any other criteria that could be considered. 
The request is made by the EPs by sending an IAS_ShortInactivity.req message.
The DM will assess whether the request can be granted or it will try to accommodate the slots to the current scheduling and 
previous requests from other nodes in the domain. 
The selected slots (‘short inactivity schedule’ as named by ITU) for L1 power saving will be published in the MAP-As only, using the short inactivity schedule MAP auxiliary field.

Customer has complete access to the different trigger algorithms and their L2 configuration parameters.

All the trigger process is managed in MspsTriggersEvaluate.
It is executed periodically, as configured in MSPS.L1.TRIGGERS_PERIOD_MS.
It waits a configurable time MSPS_WAIT_MIN_UPTIME_MS to start to do not interfere in the booting process.

In this version, there is a restriction in the slots request that must be in steps of 2 slots.
This is configurable in the parameter MSPS_ACTIVE_SLOTS_STEP.
It MUST not be changed because it is dependant on MAC implementation, that only supports an EVEN NUMBER active slots.

Every algorithm, if enabled, is executed to limit the active slots depending on the information evaluated.
By default, the following processes will be operating, in order of precedence:

	• Reset trigger:
          static void MspsTriggerResetEvaluate(INT32U temperature);
	  If enabled, when the temperature is higher than the configured value during more than the configured time, the device will be reset to avoid damages.
          L2 configuration parameters are in MSPS.RESET subgroup.

	• Forced trigger (only for validation):
	  MSPS.L1.ACTIVE_SLOTS_FORCED is a L2 configuration parameter that will force a request with the configured number of active slots. Any other trigger will be ignored. It is intended only for validation purposes.

	• Temperature-based Externally triggered throttling:
	  Controlled externally, this mechanism allows 3rd party SoC to reduce the max % of time in active mode in order to reduce temperature of other chips in the same system.
          The maximum active slots value will be MSPS.EXTERNAL.ACTIVE_SLOTS_MAX but the device could request less depending on the other triggers.

	• Temperature-based Internally-triggered throttling:
	  This mechanism looks at an internal 88LX515x temp sensor, and reduces max % of time in active mode in order to reduce internal temp. 
          This mechanism ignores traffic and just focuses on ensuring chip is not damaged by high temp.
          Sensor temperature could be checked by TEMPSENSORS.GENERAL.MEASURE L2 parameter. It returns a value in (1/100)°C units with an accuracy of +/-4°C.
          As temperature changes slowly, it is checked every MSPS_TEMPERATURE_CHECK_PERIOD_ITERATIONS of the trigger period.

	• Priority detection:
	  If traffic of one or more previously configured priorities is detected, then exit immediately from power saving to ensure low latencies. 
          Temperature based throttling will still be applied to avoid damages.
          L2 configuration parameters are in MSPS.PRIORITY subgroup.

          Be aware that hardware could only monitor traffic associated to queues, but without distinguish which traffic is. 
          So, if there is more than one class associated to one queue, any of those classes would trigger the detection. 
          To avoid this, be sure that classes that should trigger the exit from power saving are not merged with other classes in the same queue.
          Also, it is assumed that this type of traffic is bidirectional, so, it must be detected in both transmitter and receiver and both must ask domain master to increase their active slots independently.
          In case of unidirectional traffic, as it will be detected only in transmitter, if the receiver remains with a configuration with few active slots, the latency will not be reduced.

         So, to enable this feature:
	 - Configure in PACKETCLASSIFIER rules, priorities and associated queues to detect the desired traffic. This configuration is available through SCT and PCK.
	   In case of the traffic could be detected using DSCP or VLAN, then use following parameters
           PACKETCLASSIFIER.GENERAL.VLAN_CLASS_MAP
	   PACKETCLASSIFIER.GENERAL.VLAN_CLASS_MAP_EN 
	   PACKETCLASSIFIER.GENERAL.RULES_ORDER = VLAN_DSCP
	   PACKETCLASSIFIER.GENERAL.DSCP_CLASS_MAP 
	   PACKETCLASSIFIER.GENERAL.DSCP_CLASS_MAP_EN 
	   If it is needed a specific traffic detection configuration, there is a set of rules available, one for each class. They could be configured to detect specific patterns in the packets by means of an offset, pattern and mask.
	   PACKETCLASSIFIER.GENERAL.CLASSIFY_RULES.1.0
	   PACKETCLASSIFIER.GENERAL.CLASSIFY_RULES.2.0 
	   PACKETCLASSIFIER.GENERAL.CLASSIFY_RULES.3.0 
	   PACKETCLASSIFIER.GENERAL.CLASSIFY_RULES.4.0 
	   PACKETCLASSIFIER.GENERAL.CLASSIFY_RULES.5.0 
	   PACKETCLASSIFIER.GENERAL.CLASSIFY_RULES.6.0 
	   PACKETCLASSIFIER.GENERAL.CLASSIFY_RULES.7.0 
	   PACKETCLASSIFIER.GENERAL.CLASSIFY_RULES.8.0 
	   Finally, it is needed to map the different classes to specific queues. 
           Remember that there are only 4 queues available, so it is not possible to distinguish different classes assigned to the same queue.
           PACKETCLASSIFIER.GENERAL.CLASS_TO_PRIO_QUEUE_MAP

	   - Once configured the Packet Classifier rules and their mapping to transmission queues, set the bitmap with the MSPS.PRIORITY.SERVICES for the priorities that we want to force an exit from power saving.
           - Enable MSPS.PRIORITY.ENABLE

	• Minimal state:
	  This trigger configures a minimal value of active slots when there is a very low traffic detected in the ethernets, only to maintain the network. 
          This is the state with maximum power saving.
          Traffic is checked with the triggers period and it is configurable through MSPS.MINIMAL L2 parameters.

	• Traffic-based power saving:
	  Based on actual traffic, firmware tries to minimize the % of time in active mode without impact in performance. Latency will always be increased when reducing the active slots number.
          Temperature based throttling will still be applied to avoid damages.
          L2 configuration parameters are in MSPS.THROUGHPUT subgroup.

	- So, with this configuration, 4 basic scenarios are supported related to traffic-based powersaving:
		• Minimal state: when there is very low traffic in the ethernet, with maximum power saving.
		• Active cycle 50%: this is the minimum value of active slots when non minimal traffic is detected. This minimum value is set to 50% to avoid high latencies.
		• Active cycle 50%-100%: depending on the traffic, the number of active slots will adapt to it. 
	 	  It is not a lineal adaptation and it is conservative to avoid packet loss or reduce performance.
		• Active cycle 100%: When all nodes in the network are working at 100%, the performance is better than in any other case.
		  This is because the MAC efficiency is reduced when using MSPS because a higher number of STXOP is needed.

        - When the number of slots requested by all nodes in the network is 16, then the MAC cycle is divided in 2 or 3 STXOP, with maximum performance.

        - When the number of slots requested by at least one node in the network is lower than 16, then the MAC cycle is divided by 8 STXOP, and one o more of them are used to enter
          one or more nodes in powersaving. Each STXOP has a size equivalent to 2 active slots, this is the reason that MSPS_ACTIVE_SLOTS_STEP is configured to 2.
          In this case, the performance is affected because increase the number of STXOPs in a MAC cycle reduces efficiency.

        - When the number of slots requested by at least one node in the network is lower than 16 and there are more than 8 nodes in the network, the MAC cycle is divided by 4 STXOP
          due current MAC limitations that could not manage properly MAPs too big. In this special case, only minimal state, 50% and 100% of activity are supported.

        - Any change in the traffic will request the maximum possible value of active slots, to avoid packet loss or impact in performance.
          This includes start/stop traffics.

        - Bursty traffic, like traffic when there is a video on demand, could be detected. 
          If this feature is not included, with bursty traffic the number of slots will be always the maximum due the changes in the traffic.

L2 parameters are documented in the msps*configlayer_doc.h file
All the algorithms are detailed in the code itself.

*/


/*
 ************************************************************************ 
 ** Included files
 ************************************************************************
 */

#include <types.h>
#include <string.h>
#include "syslog.h"
#include "ucos_ii_api.h"

#include "assertcheckpoint.h"
#include "misc_fun_pub.h"
#include "util_math.h"

#include "phy_data_stats.h"
#include "watchdog_user.h"

#include "msps_types.h"
#include "msps_log.h"
#include "msps_triggers.h"
#include "msps_events.h"
#include "msps_data.h"
#include "msps_counters.h"
#include "msps_triggers_data.h"

#include "config_layer_client.h"
#include "sys_configlayer.h"

#include "flow_monitor_types.h"
#include "flow_monitor_pub.h"

/*
 ************************************************************************
 ** Public variables
 ************************************************************************
 */

/*
 ************************************************************************
 ** Private constants
 ************************************************************************
 */

#define MSPS_TRIGGERS_INFO_SIZE (64)
#define MSPS_TRIGGERS_THROUGHPUT_DATA_SIZE (16) 

#define MSPS_TIMESTAMP_IS_ZERO(X)  ((X.tv_sec == 0) && (X.tv_usec == 0))
#define MSPS_TIMESTAMP_RESET(X)     (X.tv_sec = X.tv_usec = 0)

#define MSPS_MINIMAL_EXIT_VALUE     (mspsTriggersData.minimalExitSlots)

// First 2 configs are used to detect the need to increase the number of active slots
// when after most of the transmission the queues are almost full it means that the traffic has increased drastically
// and the number of active slots will be set to the maximum possible value
#define MSPS_THROUGHPUT_UP_FAST_MAX_BRURQ_MSG    (mspsTriggersData.throughputConfig[0]) // Number of frames sent without empty the queues to be used to increase the number of slots to its possible maximum 
#define MSPS_THROUGHPUT_UP_FAST_MAX_BRURQ_VALUE  (mspsTriggersData.throughputConfig[1]) // Average of the queues level to be used to increase number of slots to its possible maximum.

// Following 2 configs are used to detect the need to increase the number of active slots in two slots:
#define MSPS_THROUGHPUT_UP_SLOW_MAX_BRURQ_MSG    (mspsTriggersData.throughputConfig[2])  // Number of frames sent without empty the queues to be used to increase the number of slots.
#define MSPS_THROUGHPUT_UP_SLOW_MAX_BRURQ_VALUE  (mspsTriggersData.throughputConfig[3])  // Average of the queues level  to be used to increase number of slots.

// Following 2 configs are used to detect the possibility of reducing the number of active slots because queues are empty after transmit:
#define MSPS_THROUGHPUT_DOWN_UDP_BRURQ_MSG       (mspsTriggersData.throughputConfig[4]) // Number of frames sent without empty the queues to be used to reduce the number of slots.
#define MSPS_THROUGHPUT_DOWN_UDP_BRURQ_VALUE     (mspsTriggersData.throughputConfig[5]) // Average of the queues level to be used to reduce the number of slots in one.

#define MSPS_THROUGHPUT_MARGIN                   (mspsTriggersData.throughputConfig[6]) // This includes a margin for control frames 

#define MSPS_THROUGHPUT_TCP_MARGIN               (mspsTriggersData.throughputConfig[7]) // This factor will be applied to the estimated slots and divided by 10 to increase the margin when TCP traffic is detected 
#define MSPS_THROUGHPUT_DOWN_TCP_BRURQ_MSG       (mspsTriggersData.throughputConfig[8]) // In TCP, number of frames sent without empty the queues to be used to reduce the number of slots. Be more conservative to reduce active slots 
#define MSPS_THROUGHPUT_DOWN_TCP_BRURQ_VALUE     (mspsTriggersData.throughputConfig[9]) // In TCP, average of the queues level to be used to reduce the number of slots. Be more conservative to reduce active slots

#define MSPS_THROUGHPUT_DOWN_MAX_BRURQ_VALUE     (mspsTriggersData.throughputConfig[10]) // Maximum value in BRURQ allowed to reduce the number of slots.
#define MSPS_THROUGHPUT_DOWN_MAX_BRURQ_AVG       (mspsTriggersData.throughputConfig[11]) // Average of the maximum value in BRURQ allowed to reduce the number of slots.

#define MSPS_THROUGHPUT_BURSTY_MAX_MS            (mspsTriggersData.throughputConfig[12]) // Maximun time elapsed between traffic burst to be detected as bursty traffic. Units: microseconds.
#define MSPS_THROUGHPUT_BURSTY_MIN_MS            (mspsTriggersData.throughputConfig[13]) // Minimum time elapsed between traffic burst to consider them the same bursts. Units: microseconds                                               
#define MSPS_THROUGHPUT_BURSTY_MIN_CNT           (mspsTriggersData.throughputConfig[14]) // Minimum number of traffic burst in order to be detected as bursty traffic                                                                         
#define MSPS_THROUGHPUT_BURSTY_ENABLE            (mspsTriggersData.throughputConfig[15]) // Enables the bursty traffic detection. Video on demand traffic should be bursted and this situation could be detected to avoid minimal detection   
#define MSPS_THROUGHPUT_BURSTY_NUM2AVG_RATIO     (mspsTriggersData.throughputConfig2[7]) // Minimum ratio num frames/median num frames in order to consider it a traffic burst.
#define MSPS_THROUGHPUT_BURSTY_MIN_DIFF_PKTS     (mspsTriggersData.throughputConfig2[8]) // Minimum number of Unicast Packets at G.hn channel in order to consider it a traffic burst.

// Next 4 values are thresholds to increase guard slots for MSPS in case of noisy channels
#define MSPS_THROUGHPUT_EXTRA_SLOT_THR0          (mspsTriggersData.throughputConfig2[0]) // BLER or BLOCK_RETX percentage rate to increment slots by 2
#define MSPS_THROUGHPUT_EXTRA_SLOT_THR1          (mspsTriggersData.throughputConfig2[1]) // BLER or BLOCK_RETX percentage rate to increment slots by 4
#define MSPS_THROUGHPUT_EXTRA_SLOT_THR2          (mspsTriggersData.throughputConfig2[2]) // BLER or BLOCK_RETX percentage rate to increment slots by 6
#define MSPS_THROUGHPUT_EXTRA_SLOT_THR3          (mspsTriggersData.throughputConfig2[3]) // BLER or BLOCK_RETX percentage rate to increment slots by 8

#define MSPS_THRESHOLD_MANY_NODES_8_16           (mspsTriggersData.throughputConfig2[4]) //  When the number of nodes is more than 8 a simplified algorithm is used. With an estimated number of needed slots below this threshold, then request 8 active slots, in any other case request 16 active slots.

#define MSPS_THROUGHPUT_PACKET_SIZE_BYTES        (mspsTriggersData.throughputConfig2[5]) // Threshold for the average packet size in bytes in the ethernets. If it is lower than the configure value, an extra margin must be added to avoid packet loss because the device runs out of packet identifiers.
#define MSPS_THROUGHPUT_PACKET_SIZE_EXTRA_SLOTS  (mspsTriggersData.throughputConfig2[6]) // Margin to add when the average packet size is lower than the configured value.

#define MSPS_THROUGHPUT_CONFIG2_UNUSED           (mspsTriggersData.throughputConfig2[9]) // Unused configs from 9 to 15

#define MSPS_ACTIVE_SLOTS_STEP                   (2)  // Set to 2 to allow only even active slots, set to 1 to allow any number of active slots. 

#define MSPS_RESET_CAUSE "High temperature"
#define MSPS_RESET_MIN_UPTIME_MS (30000) // Time to wait after boot up before to perform a reset due high temperature

#define MSPS_WAIT_MIN_UPTIME_MS  (20000) // Time to wait before start reducing slots

#define MSPS_TEMPERATURE_CHECK_PERIOD_ITERATIONS (5) // Do not check temperature every time, because changes in temperature are slow 

#define MSPS_MIN_BLOCKS_NEEDED (100)  //Mininum number of blocks to estimate percentage of BLER or BLOCK_RETX


#define SET_STR_GREEN(COND) ((COND)?("\x1b[32m"):"")
#define SET_STR_CYAN(COND) ((COND)?("\x1b[36m"):"")
#define SET_STR_RED(COND) ((COND)?("\x1b[31m"):"")
#define UNSET_STR_COLOR(COND) ((COND)?("\x1b[0m"):"")

/*
 ************************************************************************
 ** Private type definitions
 ************************************************************************
 */
typedef struct
{
  INT8U removedActiveSlots;
  struct timeval timestampUp;
  struct timeval timestampDown;
  struct timeval timestampReset;
  INT8U  temperatureId;
  INT32U temperature;
} t_mspsInternal;

typedef struct
{
  struct timeval   priorityTimestamp;
  struct timeval   minimalTimestamp;
  struct timeval   burstyTimestamp;
  t_phy_data_stats phyDataStats;
  INT64U           unicastRxPkts;
  INT64U           unicastTxPkts;
  INT32U           avgPktSizeBytes;
  INT32U           dataNumMsg[MSPS_TRIGGERS_THROUGHPUT_DATA_SIZE];
  INT16U           dataBrurqMax[MSPS_TRIGGERS_THROUGHPUT_DATA_SIZE];
  INT16U           dataBrurqMsgAvg[MSPS_TRIGGERS_THROUGHPUT_DATA_SIZE];
  INT16U           dataBrurqValueAvg[MSPS_TRIGGERS_THROUGHPUT_DATA_SIZE];
  INT8U            usedSlots[MSPS_TRIGGERS_THROUGHPUT_DATA_SIZE];
  INT8U            dataId;
  INT8U            afterMinimalNumDataIds;
  INT8U            burstyCnt;
  BOOLEAN          tcpDetected[MSPS_TRIGGERS_THROUGHPUT_DATA_SIZE];
  BOOLEAN          tcp;
  BOOLEAN          minimal;
  BOOLEAN          dataValid;
  BOOLEAN          bursty;
} t_mspsThroughput;

typedef struct
{
  INT8U requests[MSPS_TRIGGERS_INFO_SIZE];
  INT8U slots[MSPS_TRIGGERS_INFO_SIZE];
  INT8U id;
} t_mspsInfo;

/*
 ************************************************************************
 ** Private variables
 ************************************************************************
 */
static BOOLEAN mspsTriggersInit = TRUE;
static BOOLEAN mspsTriggersAllowed = FALSE;
static BOOLEAN mspsResetAllowed = FALSE;
static INT8U mspsInternalResetCause = WATCHDOG_RESET_CAUSE_ERROR;
static t_mspsInternal mspsInternal;
static t_mspsThroughput mspsThroughput;
static t_mspsInfo mspsInfo;
static struct timeval now;
static INT8U mspsExtraBLERSlots = 0;

#if _CONFIG_MSPS_TRIGGERS_LOG_
/// Log of the component
static t_log    mspsTriggersLog;
#endif


/*
 ************************************************************************
 ** Private function implementation
 ************************************************************************
 */

#if _CONFIG_MSPS_TRIGGERS_LOG_
/**
 * @brief Returns current log level 
 *
 **/
t_log          MspsTriggersLogGet(void)
{
  return mspsTriggersLog;
}
#endif


/**
 * @brief Select the number of slots to applied for minimal
 *
 * It will depend on the number of sections that the mac cycle is divided by
 * When the numCycleDivisions is 4, due MAC limitations, a minimal different to 4 is not supported
 *
 **/
static INT8U MspsMinimalActiveSlotsToApply(void)
{
  INT8U slots_to_request;

  if ((mspsData.numCycleDivisions == 4) &&
      (mspsTriggersData.minimalActiveSlots < MSPS_NUM_ACTIVE_SLOTS_MINIMAL_MANY_NODES))
  {
    slots_to_request = 4;
  }
  else
  {
    slots_to_request = mspsTriggersData.minimalActiveSlots;
  }

  return slots_to_request;
}

/**
 * @brief External throttling 
 * @param[in] requestActiveSlots current value for the requested slots after previous triggers
 *
 * The configured value of maximum active slots will be directly applied as a maximum in the request
 *
 **/
static INT8U MspsTriggerExternalThrottlingEvaluate(INT8U requestActiveSlots)
{
  INT8U request_active_slots = requestActiveSlots;

  if ((mspsTriggersData.externalMaxActiveSlots < request_active_slots) &&
      (mspsTriggersData.externalMaxActiveSlots != MSPS_CONFIGLAYER_AUTOMATIC_ACTIVE_SLOTS))
  {
    request_active_slots = mspsTriggersData.externalMaxActiveSlots;
    if (request_active_slots != mspsData.currentActiveSlots)
    {
      MspsCounterInc(MSPS_CNT_TRIGGER_EXTERNAL);
    }
  }
#if _CONFIG_MSPS_TRIGGERS_LOG_
  LogPrint(MspsTriggersLogGet(), MSPS_LOG_LEVEL_TRIGGERS, "TRIGGERS External request %d slots\n",request_active_slots);
#endif
  return request_active_slots;
}

/**
 * @brief Reset 
 * @param[in] temperature Value of current internal asic temperature
 *
 * If the temperature value is higher than the threshold during the configured time, reset the device to avoid damages
 *
 **/
static void MspsTriggerResetEvaluate(INT32U temperature)
{
#if _CONFIG_MSPS_TRIGGERS_LOG_
  LogPrint(MspsTriggersLogGet(), MSPS_LOG_LEVEL_TRIGGERS, "TRIGGERS Reset\n");
#endif
  // Temperature is in 1/100 celsius degrees
  if (temperature > mspsTriggersData.resetTemp)
  {
    if (MSPS_TIMESTAMP_IS_ZERO(mspsInternal.timestampReset))
    {
      // Store the change timestamp
      mspsInternal.timestampReset = now;
    }
    else
    {
      struct timeval  diff_time;
      INT32U diff_ms;

      TimeTimevalSubtract(&now, &mspsInternal.timestampReset, &diff_time);
      diff_ms = (diff_time.tv_usec/1000) + (diff_time.tv_sec*1000);
      if (diff_ms >= mspsTriggersData.resetMs)
      {
        // Reset the device
        WatchdogReset(mspsInternalResetCause);
      }
    }
  }
  else
  {
    MSPS_TIMESTAMP_RESET(mspsInternal.timestampReset);
  }
} 

     
/**
 * @brief Internal throttling 
 * @param[in] requestActiveSlots current value for the requested slots after previous triggers
 * @param[in] temperature Value of current internal asic temperature
 *
 * If the temperature value is higher than the threshold to enter in throttling during more than the configured time, increase the number of inactive slots
 * If the temperature value is lower than the threshold to exit in throttling during more than the configured time, reduce the number of inactive slots
 * The number of inactive slot will be applied to reduce the maximum number of active slots.
 *
 **/
static INT8U MspsTriggerInternalThrottlingEvaluate(INT8U requestActiveSlots, INT32U temperature)
{
  INT8U request_active_slots = requestActiveSlots;
  INT8U active_slots_limit;
  
#if _CONFIG_MSPS_TRIGGERS_LOG_
  LogPrint(MspsTriggersLogGet(), MSPS_LOG_LEVEL_TRIGGERS, "TRIGGERS Internal Current Time: [%d sec, %d usec]", now.tv_sec, now.tv_usec);
#endif
    // Temperature is in 1/100 celsius degrees 
    if (temperature > mspsTriggersData.internalThresholds[MSPS_TRIGGERS_INTERNAL_THRESHOLD_UP].temperature)
    {
      MSPS_TIMESTAMP_RESET(mspsInternal.timestampDown);

      if (MSPS_TIMESTAMP_IS_ZERO(mspsInternal.timestampUp))
      {
        // Store the change timestamp
        mspsInternal.timestampUp = now;
      }
      else
      {
        struct timeval  diff_time;
        INT32U diff_ms;

        TimeTimevalSubtract(&now, &mspsInternal.timestampUp, &diff_time);
        diff_ms = (diff_time.tv_usec/1000) + (diff_time.tv_sec*1000);
        if (diff_ms >= mspsTriggersData.internalThresholds[MSPS_TRIGGERS_INTERNAL_THRESHOLD_UP].ms)
        {
          // Store the change timestamp
          mspsInternal.timestampUp = now;
          mspsInternal.removedActiveSlots += mspsTriggersData.internalThresholds[MSPS_TRIGGERS_INTERNAL_THRESHOLD_UP].slots;
          if (mspsInternal.removedActiveSlots > (mspsData.maxActiveSlots - mspsData.minActiveSlots))
          {
            mspsInternal.removedActiveSlots = (mspsData.maxActiveSlots - mspsData.minActiveSlots);
          }
        }
      }
    }
    else if (temperature < mspsTriggersData.internalThresholds[MSPS_TRIGGERS_INTERNAL_THRESHOLD_DOWN].temperature)
    {
      MSPS_TIMESTAMP_RESET(mspsInternal.timestampUp);

      if (MSPS_TIMESTAMP_IS_ZERO(mspsInternal.timestampDown))
      {
        // Store the change timestamp
        mspsInternal.timestampDown = now;
      }
      else
      {
        struct timeval  diff_time;
        INT32U diff_ms;

        TimeTimevalSubtract(&now, &mspsInternal.timestampDown, &diff_time);
        diff_ms = (diff_time.tv_usec/1000) + (diff_time.tv_sec*1000);

        if (diff_ms >= mspsTriggersData.internalThresholds[MSPS_TRIGGERS_INTERNAL_THRESHOLD_DOWN].ms)
        {
          // Store the change timestamp
          mspsInternal.timestampDown = now;
          if (mspsInternal.removedActiveSlots > mspsTriggersData.internalThresholds[MSPS_TRIGGERS_INTERNAL_THRESHOLD_DOWN].slots)
          {
            mspsInternal.removedActiveSlots-= mspsTriggersData.internalThresholds[MSPS_TRIGGERS_INTERNAL_THRESHOLD_DOWN].slots;
          }
          else
          {
            mspsInternal.removedActiveSlots = 0;
          }
        }
      }
    }
    else
    {
      // Temperature is OK, so reset timestamps
      MSPS_TIMESTAMP_RESET(mspsInternal.timestampUp);
      MSPS_TIMESTAMP_RESET(mspsInternal.timestampDown);
    }
    
    if (mspsData.maxActiveSlots > (mspsInternal.removedActiveSlots + mspsData.minActiveSlots))
    {
      active_slots_limit = (mspsData.maxActiveSlots - mspsInternal.removedActiveSlots);
    }
    else
    {
      active_slots_limit = mspsData.minActiveSlots;
    }

    if (request_active_slots > active_slots_limit)
    {
      request_active_slots = active_slots_limit;
      if ((request_active_slots < requestActiveSlots) && (request_active_slots != mspsData.currentActiveSlots))
      {
        MspsCounterInc(MSPS_CNT_TRIGGER_INTERNAL);
      }
    }
    
#if _CONFIG_MSPS_TRIGGERS_LOG_
    LogPrint(MspsTriggersLogGet(), MSPS_LOG_LEVEL_TRIGGERS, "Temperature %d removeSlots %d requestSlots %d",
          temperature,
          mspsInternal.removedActiveSlots,
          request_active_slots);
#endif
  return request_active_slots;
}

/**
 * @brief Throughput minimal detection 
 * @param[in] requestActiveSlots current value for the requested slots after previous triggers
 *
 * If there is very low traffic through ethernet during some configurable time,
 * enter in minimal state with a mininum number of active slots
 * Both ethernets will be checked, but broadcast packets will be ignored 
 * If packets and bytes are lower than the value configured, the condition will be true
 **/
static INT8U MspsTriggerMinimalEvaluate(INT8U requestActiveSlots)
{
  INT8U request_active_slots = requestActiveSlots;

  // Check minimal condition related with ethernet traffic
  // If there is very low traffic in the ethernet, the device will enter in minimal state, with very few active slots
  mspsThroughput.minimal = FALSE;

  if (mspsTriggersData.minimalEnable && (mspsData.bpsAlertMaintain == 0))
  {
    t_ethIfDriverStats diff;

    // Evaluate if the condition to enter in idle is true or not since last checking
    // Get pkts and bytes tx/rx in the ethernet since last checking
    NetEthernetDiffDataGet(&mspsThroughput.phyDataStats, &diff);
    
    // Get average pkt size in bytes
    INT32U  avg_tx_pkts_size_bytes = (diff.numTxPkts == 0) ? 0 : (diff.numTxBytes/diff.numTxPkts);
    INT32U  avg_rx_pkts_size_bytes = (diff.numRxPkts == 0) ? 0 : (diff.numRxBytes/diff.numRxPkts);
    mspsThroughput.avgPktSizeBytes = (diff.numTxPkts > diff.numRxPkts) ? avg_tx_pkts_size_bytes : avg_rx_pkts_size_bytes;
    
    // Check if idle condition is true
    if (((diff.numRxBytes + diff.numTxBytes) <= mspsTriggersData.minimalBytes) &&
        ((diff.numRxPkts + diff.numTxPkts) <= mspsTriggersData.minimalPkts ))
    {
      mspsThroughput.minimal = TRUE;
    }
    else
    {
      mspsThroughput.minimal = FALSE;
    }

    request_active_slots = MSPS_MINIMAL_EXIT_VALUE;
    if (mspsThroughput.minimal)
    {
      // Minimal situation detected. 
      // If it is maintained during more than mspsTriggersData.minimalMs, then enter in the number of active slots configured for minimal.
      if (MSPS_TIMESTAMP_IS_ZERO(mspsThroughput.minimalTimestamp))
      {
        mspsThroughput.minimalTimestamp = now;
#if _CONFIG_MSPS_TRIGGERS_LOG_
        LogPrint(MspsTriggersLogGet(), LOG_LEVEL_INFO_22, "TRIGGERS: TS == 0. Throughput minimal %d slots %d (%d %d %d %d)\n", mspsThroughput.minimal, request_active_slots,diff.numRxBytes,diff.numTxBytes,diff.numRxPkts,diff.numTxPkts);
#endif
      }
      else  
      {
        struct timeval  diff_time;
        INT32U diff_ms;
   
        TimeTimevalSubtract(&now, &mspsThroughput.minimalTimestamp, &diff_time);
        diff_ms = (diff_time.tv_usec/1000) + (diff_time.tv_sec*1000);
   
        if (diff_ms >= mspsTriggersData.minimalMs)
        {
          INT8U minimal_to_request;

          minimal_to_request = MspsMinimalActiveSlotsToApply();

          request_active_slots = minimal_to_request;
          if ((minimal_to_request < requestActiveSlots) &&
              (minimal_to_request != mspsData.currentActiveSlots))
          {
            MspsCounterInc(MSPS_CNT_TRIGGER_MINIMAL); 
#if _CONFIG_MSPS_TRIGGERS_LOG_
            LogPrint(MspsTriggersLogGet(), LOG_LEVEL_INFO_22, "TRIGGERS: TS >= minimalMs (%u). Throughput minimal %d slots %d\n", mspsTriggersData.minimalMs, mspsThroughput.minimal, request_active_slots);
#endif
          }
        }
      }
    }
    else
    {
      // Minimal situation not detected, clear timestamp
      if (!MSPS_TIMESTAMP_IS_ZERO(mspsThroughput.minimalTimestamp))
      {
#if _CONFIG_MSPS_TRIGGERS_LOG_
        LogPrint(MspsTriggersLogGet(), LOG_LEVEL_INFO_22, "TRIGGERS: TS != 0. Throughput minimal %d slots %d bps alert %d\n", mspsThroughput.minimal, request_active_slots, mspsData.bpsAlert);
#endif
      }
      MSPS_TIMESTAMP_RESET(mspsThroughput.minimalTimestamp);
    }

    if (request_active_slots > requestActiveSlots)
    {
      request_active_slots = requestActiveSlots;
    }
#if _CONFIG_MSPS_TRIGGERS_LOG_
    LogPrint(MspsTriggersLogGet(), LOG_LEVEL_INFO_9, "TRIGGERS Throughput minimal %d final slots %d \n", mspsThroughput.minimal, request_active_slots);
#endif
  }

  return request_active_slots;
}


/**
 * @brief Detects if there is a bursty traffic (video on demand profile) that prevent to decrease active slots from maximum.
 * @param[in] requestActiveSlots current value for the requested slots after previous triggers
 *
 **/
static BOOLEAN MspsIsBurstyTraffic(INT32U lastNumFrames, INT8U lastBRURq, INT32U avgNumFrames, INT64U unicastPktsDiff)
{
  if (MSPS_THROUGHPUT_BURSTY_ENABLE)
  {
    struct timeval  diff_time;
    INT32U diff_ms;
    BOOLEAN num_frames_cond;
    BOOLEAN unicast_pkt_cond;
    BOOLEAN last_brurq_cond;

    // We have three conditions which should be met to decide that there is a bursty traffic profile:
    //   * Instantaneous Number of Frames should be greater that median of the
    //     last MSPS_TRIGGERS_THROUGHPUT_DATA_SIZE number of message samples.
    //   * Instantaneous Number of packets receiver/transmitted by G.hn channel
    //     should be greater than a threshold.
    //   * Tx Queue occupation should be lower than a threshold, which stand
    //     for we could reduce the number of active slots.
    //
    //   If conditions are met MSPS_THROUGHPUT_BURSTY_MIN_CNT times with less
    //   than MSPS_THROUGHPUT_BURSTY_MAX_MS between burst, we decide that a
    //   bursty traffic profile is detected.

    num_frames_cond = (lastNumFrames > (MSPS_THROUGHPUT_BURSTY_NUM2AVG_RATIO * avgNumFrames));
    unicast_pkt_cond = (unicastPktsDiff > MSPS_THROUGHPUT_BURSTY_MIN_DIFF_PKTS);
    last_brurq_cond = (lastBRURq < MSPS_THROUGHPUT_DOWN_TCP_BRURQ_VALUE);

#if _CONFIG_MSPS_TRIGGERS_LOG_
    LogPrint(MspsTriggersLogGet(), LOG_LEVEL_INFO_24, "%s:: burstyTimestamp [%u:%u] lastNumFrames %s%u%s lastBRURq %s%u%s avgNumFrames %u unicastPktsDiff %s%lu%s\n", __FUNCTION__,
             mspsThroughput.burstyTimestamp.tv_sec,
             mspsThroughput.burstyTimestamp.tv_usec,
             SET_STR_GREEN(num_frames_cond),
             lastNumFrames,
             UNSET_STR_COLOR(num_frames_cond),
             SET_STR_GREEN(last_brurq_cond),
             lastBRURq,
             UNSET_STR_COLOR(last_brurq_cond),
             avgNumFrames,
             SET_STR_GREEN(unicast_pkt_cond),
             unicastPktsDiff,
             UNSET_STR_COLOR(unicast_pkt_cond)
            );
#endif
    MSPS_TIMESTAMP_RESET(diff_time);

    if (MSPS_TIMESTAMP_IS_ZERO(mspsThroughput.burstyTimestamp))
    {
      // First time we check conditions, so get a timestamp.
      mspsThroughput.burstyTimestamp = now;
      if (
          (num_frames_cond) &&
          (unicast_pkt_cond) &&
          (last_brurq_cond)
         )
      {
        // If conditions are met, increase burst counter.
        if (mspsThroughput.burstyCnt < MAX_INT8U)
        {
          mspsThroughput.burstyCnt++;
        }
      }
    }
    else  
    {
      TimeTimevalSubtract(&now, &mspsThroughput.burstyTimestamp, &diff_time);
      diff_ms = (diff_time.tv_usec/1000) + (diff_time.tv_sec*1000);

      // If time between bursts is greater than MSPS_THROUGHPUT_BURSTY_MAX_MS,
      // reset burst counter, reset timestamp and unset bursty traffic flag.
      if (diff_ms >= MSPS_THROUGHPUT_BURSTY_MAX_MS)
      {
        // clear timestamp
        MSPS_TIMESTAMP_RESET(mspsThroughput.burstyTimestamp);
        mspsThroughput.bursty = FALSE;
        mspsThroughput.burstyCnt = 0;
#if _CONFIG_MSPS_TRIGGERS_LOG_
        LogPrint(MspsTriggersLogGet(), LOG_LEVEL_INFO_24, "%s:: diff [%u:%u] %stoo high (%u >= %u)%s, reseting TS.\n", __FUNCTION__,
                 diff_time.tv_sec,
                 diff_time.tv_usec,
                 SET_STR_RED(TRUE),
                 diff_ms,
                 MSPS_THROUGHPUT_BURSTY_MAX_MS,
                 UNSET_STR_COLOR(TRUE)
                );
#endif
      }
      else if (diff_ms <= MSPS_THROUGHPUT_BURSTY_MIN_MS)
      {
        // If time between burst is less than MSPS_THROUGHPUT_BURSTY_MIN_MS,
        // consider these detected traffics as the same burst.
        if (
            (num_frames_cond) &&
            (unicast_pkt_cond) &&
            (last_brurq_cond)
           )
        {
          // update timestamp
          mspsThroughput.burstyTimestamp = now;
#if _CONFIG_MSPS_TRIGGERS_LOG_
          LogPrint(MspsTriggersLogGet(), LOG_LEVEL_INFO_24, "%s:: diff [%u:%u] %stoo low (%u <= %u)%s, updating TS.\n", __FUNCTION__,
                   diff_time.tv_sec,
                   diff_time.tv_usec,
                   SET_STR_CYAN(TRUE),
                   diff_ms,
                   MSPS_THROUGHPUT_BURSTY_MIN_MS,
                   UNSET_STR_COLOR(TRUE)
                  );
#endif
        }
      }
      else
      {
        // In order case, this could be a bursty traffic profile, increase
        // burst counter and check that it is higher than
        // MSPS_THROUGHPUT_BURSTY_MIN_CNT. In that case, set bursty traffic
        // flag.
        if (
            (num_frames_cond) &&
            (unicast_pkt_cond) &&
            (last_brurq_cond)
           )
        {
          // update timestamp
          mspsThroughput.burstyTimestamp = now;
          // If conditions are met, increase burst counter.
          if (mspsThroughput.burstyCnt < MAX_INT8U)
          {
            mspsThroughput.burstyCnt++;
          }
          // If we have detected MSPS_THROUGHPUT_BURSTY_MIN_CNT bursts, it is a bursty traffic profile.
          mspsThroughput.bursty = (mspsThroughput.burstyCnt > MSPS_THROUGHPUT_BURSTY_MIN_CNT);
#if _CONFIG_MSPS_TRIGGERS_LOG_
          LogPrint(MspsTriggersLogGet(), LOG_LEVEL_INFO_24, "%s:: diff [%u:%u] %scould be bursty traffic (Cnt %d)%s. Updating TS.\n", __FUNCTION__,
                   diff_time.tv_sec,
                   diff_time.tv_usec,
                   SET_STR_GREEN(TRUE),
                   mspsThroughput.burstyCnt,
                   UNSET_STR_COLOR(TRUE)
                  );
#endif
        }
      }
    }

#if _CONFIG_MSPS_TRIGGERS_LOG_
    LogPrint(MspsTriggersLogGet(), LOG_LEVEL_INFO_23, "%s:: diff_time [%u:%u] bursty %s\n", __FUNCTION__,
             diff_time.tv_sec,
             diff_time.tv_usec,
             BOOLEAN2STR(mspsThroughput.bursty)
            );
#endif
  }

  return mspsThroughput.bursty;
}

/**
 * @brief This function evaluate if the number of slots active should be increased due to noisy channel conditions
 * producing BLER (as a receiver) or RETX (as a transmitter)
 *
 * @return Number of slots to be increased due to noisy conditions.
 *
 **/

static INT8U MspsTriggerThroughputThrottlingExtraSlotsEvaluate(void)
{
  t_flowMonitorError flmon_err;
  static INT32U blockTxOld = 0;
  static INT64U blockRetxOld = 0;
  static INT32U blockRxOld = 0;
  static INT32U blockRxErrOld = 0;

  INT32U block_tx;
  INT64U block_retx;
  INT32U block_rx;
  INT32U block_rx_err;

  INT8U extra_slots = 0;

  //Get blocks stats from HW counters
  flmon_err = FlowMonitorBlockInfoGet(&block_tx, &block_retx, &block_rx, &block_rx_err, NULL, NULL);

  if (flmon_err == FLOW_MONITOR_ERR_NONE)
  {
    INT32U perc_retx = 0;
    INT32U perc_rx_err = 0;
    INT32U diff_tx = block_tx - blockTxOld;
    INT32U diff_retx = block_retx - blockRetxOld;
    INT32U diff_rx = block_rx - blockRxOld;
    INT32U diff_rx_err = block_rx_err - blockRxErrOld;

    INT32U perc_reference = 0;

    if ( (diff_rx > MSPS_MIN_BLOCKS_NEEDED) || (diff_tx > MSPS_MIN_BLOCKS_NEEDED))
    {
      //We need a minimum number of blocks tx or received in order to have a
      //reliable percentage
      if (diff_tx != 0)
      {
        perc_retx = (diff_retx*100)/diff_tx;
      }
      if (diff_rx != 0)
      {
        perc_rx_err = (diff_rx_err*100)/diff_rx;
      }

      //Note: An IIR filter for perc variables could be another option
      //for implementation


      //We get here the worst case retransmissions or block error rate received
      if (perc_rx_err > perc_retx)
      {
        perc_reference = perc_rx_err;
      }
      else
      {
        perc_reference = perc_retx;
      }


      if (perc_reference < MSPS_THROUGHPUT_EXTRA_SLOT_THR0)
      {
        extra_slots = 0;
      }
      else if (perc_reference < MSPS_THROUGHPUT_EXTRA_SLOT_THR1)
      {
        extra_slots = MSPS_ACTIVE_SLOTS_STEP;
      }
      else if (perc_reference < MSPS_THROUGHPUT_EXTRA_SLOT_THR2)
      {
        extra_slots = 2*MSPS_ACTIVE_SLOTS_STEP;
      }
      else if (perc_reference < MSPS_THROUGHPUT_EXTRA_SLOT_THR3)
      {
        extra_slots = 3*MSPS_ACTIVE_SLOTS_STEP;
      }
      else
      {
        extra_slots = 4*MSPS_ACTIVE_SLOTS_STEP;
      }
    }

#if _CONFIG_MSPS_TRIGGERS_LOG_
    LogPrint(MspsTriggersLogGet(), LOG_LEVEL_INFO_8,
        "block_tx %d block_retx %lu block_rx %d block_rx_err %d\n", block_tx, block_retx, block_rx, block_rx_err);
    LogPrint(MspsTriggersLogGet(), LOG_LEVEL_INFO_8,
        "blockTxOld %d blockRetxOld %lu blockRxOld %d blockRxErrOld %d\n", blockTxOld, blockRetxOld, blockRxOld, blockRxErrOld);
    LogPrint(MspsTriggersLogGet(), LOG_LEVEL_INFO_8,
        "diff_tx %d diff_retx %d diff_rx %d diff_rx_err %d\n", diff_tx, diff_retx, diff_rx, diff_rx_err);
    LogPrint(MspsTriggersLogGet(), LOG_LEVEL_INFO_8,
        "perc_retx %d perc_rx_err %d perc_reference %d extra_slots %d\n", perc_retx, perc_rx_err, perc_reference, extra_slots);
#endif

    //Store values for next call
    blockTxOld = block_tx;
    blockRetxOld = block_retx;
    blockRxOld = block_rx;
    blockRxErrOld = block_rx_err;
  }

  return extra_slots;

}




/**
 * @brief Throughput throttling 
 * @param[in] requestActiveSlots current value for the requested slots after previous triggers
 *
 * Depending on the throughput needs, adapt the number of active slots to reduce power consumption 
 * without affecting performance. Latency will be affected.
 *
 * Information to be used in the adaptation is from BRURQ (Bandwidth Reservation Update Request) information
 * included in the MSG-type PHY-header with information about queues state after a transmission
 * BRURQ Contains the number of accumulated bytes in the connection queue.
 * This value is specified in units of Kbytes, expressed as ceiling (number of bytes/1024). 
 *
 * Other parameters, as TCP detection, BLER or Retransmitions, are used depending on the scenario.
 *
 * Special cases:
 * - There is a minimum active slots value to ensure controlled latencies (recomended 8 slots)
 * - With any important change in the traffic, including when a traffic stops/starts, it is requested the maximum number of active slots
 *   This is done to adapt faster to changes in traffic, like Ixia generated or TCP traffics, without packet loss.
 * - If there is an important change in BPS, then request the maximum number of active slots
 * - If there is a reduction in BPS, then increase the slots estimation in this factor
 * - If there is BLER and RETX, add an extra margin to the estimated slots
 * - If there is traffic with small packets, add an extra margin to the estimated slots to avoid packet loss
 *   because there is a limitation in the number of packet identifiers in the system.
 * - If TCP traffic is detected, add an extra margin to the estimated slots
 * - Bursty traffic, like traffic in video on demand, could be detected. If not, then the number of slots will be always the maximum
 *   due the changes in the traffic.
 * - If there are more than 8 nodes in the network, go to a simpler algorithm due MAC limitations
 *
 **/
static INT8U MspsTriggerThroughputThrottlingEvaluate(INT8U requestActiveSlots)
{
  INT8U   request_active_slots = requestActiveSlots;
  INT64U  unicast_rx_pkts = 0;
  INT64U  unicast_tx_pkts = 0;
  INT64U  unicast_rx_pkts_diff = 0;
  INT64U  unicast_tx_pkts_diff = 0;
  INT64U  unicast_pkts_diff = 0;
  static INT32U  num_frames_hist_ordered[MSPS_TRIGGERS_THROUGHPUT_DATA_SIZE];
  INT32U  num_msgs = 0;
  INT32U  num_msgs_brurq = 0;
  INT32U  max_brurq = 0;
  INT32U  sum_brurq = 0;
  INT32U  i,j;
  INT32U  current_used_slots = 0;
  INT32U  modified_used_slots = 0;
  INT32U  num_used_slots = 0;
  INT32U  avg_value_brurq = 0;
  INT32U  avg_msg_brurq = 0;
  INT32U  avg_used_slots = 0;
  INT32U  avg_max_brurq = 0;
  INT32U  tcp_counter = 0;
  INT32U  last_id = 0;
  INT32U  current_num_msg = 0;
  INT32U  avg_num_msg = 0;
  INT8U   current_value_brurq = 0;
  INT8U   current_msg_brurq = 0;
  INT8U   needed_slots = 0;
  BOOLEAN bursty_traffic;

  memset(num_frames_hist_ordered, 0, (MSPS_TRIGGERS_THROUGHPUT_DATA_SIZE * sizeof(INT32U)));

  // Retrieve number of packets transmitted/received by G.hn channel. 
  FlowMonitorBlockInfoGet(NULL, NULL, NULL, NULL, &unicast_rx_pkts, &unicast_tx_pkts);

  unicast_rx_pkts_diff = unicast_rx_pkts - mspsThroughput.unicastRxPkts;
  unicast_tx_pkts_diff = unicast_tx_pkts - mspsThroughput.unicastTxPkts;

  // Evaluate how many packets where transfered since last checking.
  mspsThroughput.unicastRxPkts = unicast_rx_pkts;
  mspsThroughput.unicastTxPkts = unicast_tx_pkts;

  unicast_pkts_diff = unicast_rx_pkts_diff + unicast_tx_pkts_diff;

  // Get extra slots to be added depending on the BLER or ReTX levels detected
  mspsExtraBLERSlots = MspsTriggerThroughputThrottlingExtraSlotsEvaluate();

  // Compile information received from MAC statistics
  for (i = 0; i < MSPS_MAC_STATS_MAX_MAP_USAGE; i++)
  {
    if (mspsMacStatsUsage.usageIndex == i)
    {
      //This is the index currently under storing by the mac stats, so not usable
    }
    else
    {
      INT8U frame_number = mspsMacStatsUsage.usage[i].index;
      INT32U frame_dur_accum = 0;

      // Get statistics of brurq in data frames
      for (j = 0; j < frame_number; j++)
      {
        frame_dur_accum += mspsMacStatsUsage.usage[i].brurqData[j].frameDur;

        // Add IFG min for frames that are not messages. Only MSG and BMSG include IFG in the frame duration
        if (!mspsMacStatsUsage.usage[i].brurqData[j].brurqValid)
        {
          frame_dur_accum += mspsMacIFGMin;
        }

        //Storing if msg had brurq != 0 so tx didn't empty the queue
        if (mspsMacStatsUsage.usage[i].brurqData[j].mfldisi != 0)
        {
          //If is not 0 means the info is valid
          if (mspsMacStatsUsage.usage[i].brurqData[j].brurqValid)
          {
            num_msgs++;
            //If is not msg or bmsg brurq field is not present
            if (mspsMacStatsUsage.usage[i].brurqData[j].brurq != 0)
            {
              num_msgs_brurq++;
              sum_brurq += mspsMacStatsUsage.usage[i].brurqData[j].brurq;
              if (mspsMacStatsUsage.usage[i].brurqData[j].brurq > max_brurq)
              {
                max_brurq = mspsMacStatsUsage.usage[i].brurqData[j].brurq;
              }
            }
          }
        }
      }

      // Get max value for mac cycle occupation
      if (mspsMacCycleEffective != 0)
      {
        INT32U cycle_used_slots = CEIL((frame_dur_accum * mspsData.maxActiveSlots) , mspsMacCycleEffective);
        current_used_slots += cycle_used_slots;
        num_used_slots ++;
      }
    }
  }

  // Store MAC stats in an array to have information in a longer time
#if _CONFIG_MSPS_TRIGGERS_LOG_
  INT32U data_id = mspsThroughput.dataId;
#endif

  current_used_slots = (num_used_slots != 0) ? (current_used_slots / num_used_slots) : 0;
  mspsThroughput.usedSlots[mspsThroughput.dataId] = current_used_slots;
  mspsThroughput.dataBrurqMax[mspsThroughput.dataId] = max_brurq;
  mspsThroughput.dataBrurqMsgAvg[mspsThroughput.dataId] = (num_msgs != 0) ? ((num_msgs_brurq * 100)/num_msgs) : 0;
  mspsThroughput.dataBrurqValueAvg[mspsThroughput.dataId] = (num_msgs != 0) ? (sum_brurq/num_msgs) : 0;
  mspsThroughput.dataNumMsg[mspsThroughput.dataId] = num_msgs;
  mspsThroughput.tcpDetected[mspsThroughput.dataId] = mspsData.tcpDetected;

  mspsThroughput.dataId ++;
  if (mspsThroughput.dataId == MSPS_TRIGGERS_THROUGHPUT_DATA_SIZE)
  {
    mspsThroughput.dataId = 0;
  }

  // In order to evaluate the median of number of messages history, we need to sort it.
  // Therefore, we copy the circular buffer which provides this history and
  // sort it using InsertionSort, which is good enough for small data sets.
  memcpy(num_frames_hist_ordered, mspsThroughput.dataNumMsg, (MSPS_TRIGGERS_THROUGHPUT_DATA_SIZE * sizeof(INT32U)));

  InsertionSort(num_frames_hist_ordered, MSPS_TRIGGERS_THROUGHPUT_DATA_SIZE);

  avg_num_msg =  Median(num_frames_hist_ordered, MSPS_TRIGGERS_THROUGHPUT_DATA_SIZE);

  avg_value_brurq = 0;
  avg_msg_brurq = 0;
  avg_used_slots = 0;
  avg_max_brurq = 0;
  max_brurq = 0;
  needed_slots = 0;
  last_id = (mspsThroughput.dataId == 0) ? (MSPS_TRIGGERS_THROUGHPUT_DATA_SIZE - 1) : (mspsThroughput.dataId - 1);
  current_value_brurq = mspsThroughput.dataBrurqValueAvg[last_id];
  current_msg_brurq = mspsThroughput.dataBrurqMsgAvg[last_id];
  current_num_msg = mspsThroughput.dataNumMsg[last_id];
  tcp_counter = 0;

  // Get the average values in the last 16 iterations
  for (i=0; i< MSPS_TRIGGERS_THROUGHPUT_DATA_SIZE ; i++)
  {
    avg_msg_brurq +=  mspsThroughput.dataBrurqMsgAvg[i];
    avg_value_brurq +=  mspsThroughput.dataBrurqValueAvg[i];
    avg_max_brurq += mspsThroughput.dataBrurqMax[i];
    avg_used_slots += mspsThroughput.usedSlots[i];
    if (max_brurq < mspsThroughput.dataBrurqMax[i])
    {
      max_brurq = mspsThroughput.dataBrurqMax[i];
    }
    if (mspsThroughput.tcpDetected[i])
    {
      tcp_counter++;
    }
  }
  mspsThroughput.tcp = (tcp_counter >= 2);

  avg_msg_brurq = CEIL(avg_msg_brurq, MSPS_TRIGGERS_THROUGHPUT_DATA_SIZE);
  avg_value_brurq = CEIL(avg_value_brurq, MSPS_TRIGGERS_THROUGHPUT_DATA_SIZE);
  avg_used_slots = CEIL(avg_used_slots, MSPS_TRIGGERS_THROUGHPUT_DATA_SIZE);
  avg_max_brurq = CEIL(avg_max_brurq, MSPS_TRIGGERS_THROUGHPUT_DATA_SIZE);

#if _CONFIG_MSPS_TRIGGERS_LOG_
  LogPrint(MspsTriggersLogGet(), LOG_LEVEL_INFO_24, "%s:: dataId %u num_msg %u avg_num_msg %u last_id %u", __FUNCTION__, 
           data_id, num_msgs, avg_num_msg, last_id);
#endif

  bursty_traffic = MspsIsBurstyTraffic(current_num_msg, current_value_brurq, avg_num_msg, unicast_pkts_diff);

  if ((mspsThroughput.minimal) && !bursty_traffic)
  {
    mspsThroughput.dataValid = FALSE;
    mspsThroughput.afterMinimalNumDataIds = 0;
  }
  else
  {
    if (mspsThroughput.afterMinimalNumDataIds < MSPS_TRIGGERS_THROUGHPUT_DATA_SIZE)
    {
      mspsThroughput.afterMinimalNumDataIds++;
    }
    else
    {
      // Enough information to start with the throttling
      mspsThroughput.dataValid = TRUE;
    }
  }

  // When device is not in idle, and there is enough information
  // then adapt slots number to the needed throughput
  if (mspsThroughput.dataValid)
  {
    // Check bps and if it has been reduced apply the percentage to the used slots
    modified_used_slots = current_used_slots;
    if ((mspsData.bpsTxFactor > MSPS_BPS_FACTOR_MIN) || (mspsData.bpsRxFactor > MSPS_BPS_FACTOR_MIN))
    {
      INT32U bps_factor = (mspsData.bpsTxFactor > mspsData.bpsRxFactor) ? mspsData.bpsTxFactor : mspsData.bpsRxFactor;

      modified_used_slots = CEIL((current_used_slots * bps_factor),MSPS_BPS_FACTOR_MIN);
#if _CONFIG_MSPS_TRIGGERS_LOG_
      LogPrint(MspsTriggersLogGet(), LOG_LEVEL_INFO_8, "TRIGGERS Throughput bps info %2d %2d %2d slots %2d %2d\n", mspsData.bpsTxFactor, mspsData.bpsRxFactor, mspsData.bpsAlert, current_used_slots,modified_used_slots);
#endif
    }

    // Add a predefined margin
    modified_used_slots += MSPS_THROUGHPUT_MARGIN + mspsExtraBLERSlots;
    avg_used_slots += MSPS_THROUGHPUT_MARGIN + mspsExtraBLERSlots;

    // In case of TCP, increase margin
    if (mspsThroughput.tcp)
    {
      modified_used_slots = CEIL((modified_used_slots * MSPS_THROUGHPUT_TCP_MARGIN),10);
      avg_used_slots = CEIL((avg_used_slots * MSPS_THROUGHPUT_TCP_MARGIN),10);
    }

    // In case of small packets, increase margin
    if (mspsThroughput.avgPktSizeBytes < MSPS_THROUGHPUT_PACKET_SIZE_BYTES)
    {
      modified_used_slots  += MSPS_THROUGHPUT_PACKET_SIZE_EXTRA_SLOTS;
      avg_used_slots += MSPS_THROUGHPUT_PACKET_SIZE_EXTRA_SLOTS;
    }

    // First, decide which number of active slots use as a base for the algorithm
    if (mspsData.bpsAlert)
    {
      // If there is any alert of channel adaptation, do not reduce the active slots
      needed_slots = MSPS_CONFIGLAYER_MAX_ACTIVE_SLOTS_VALUE;
    }
    else if (mspsData.numCycleDivisions == MSPS_NUM_CYCLE_DIV_MANY_NODES)
    {
      // When there are too many nodes in the network, use a simplified algorithm
      // If the estimated slots are lower than a threshold, configure MSPS_NUM_ACTIVE_SLOTS_MANY_NODES_LOW
      // If not, configure the maximum number of active slots
      if (modified_used_slots < MSPS_THRESHOLD_MANY_NODES_8_16)
      {
        needed_slots = MSPS_NUM_ACTIVE_SLOTS_MANY_NODES_LOW;
      }
      else
      {
        needed_slots = MSPS_NUM_ACTIVE_SLOTS_MANY_NODES_HIGH;
      }

#if _CONFIG_MSPS_TRIGGERS_LOG_
      LogPrint(MspsTriggersLogGet(), LOG_LEVEL_INFO_11, "Cycle divided in 4 sections: modified %d needed %d\n", modified_used_slots, needed_slots);
#endif
    }
    else if (modified_used_slots > mspsData.currentActiveSlots)
    {
      // If number of used slots has increased, it is because other nodes in the network have used them, 
      // increase also slots in this node to avoid situation where a node needs to tx to us and we have not enough active slots
      needed_slots = modified_used_slots;
    }
    else
    {
      needed_slots = mspsData.currentActiveSlots;

      // Detect fast an increase in the traffic demand 
      if ((current_msg_brurq > MSPS_THROUGHPUT_UP_FAST_MAX_BRURQ_MSG) && 
          (current_value_brurq > MSPS_THROUGHPUT_UP_FAST_MAX_BRURQ_VALUE))
      {
        needed_slots =  mspsData.maxActiveSlots;
      }
      // If brurq is increasing, then increase active slots
      else if (((current_msg_brurq > MSPS_THROUGHPUT_UP_SLOW_MAX_BRURQ_MSG) && 
                (current_value_brurq > MSPS_THROUGHPUT_UP_SLOW_MAX_BRURQ_VALUE))) 
      {
        needed_slots +=2;
      }
      // Decrease slots if there is a high difference between slots used and current
      else if ((avg_used_slots <= (mspsData.currentActiveSlots - (2*MSPS_ACTIVE_SLOTS_STEP))) && (needed_slots > MSPS_ACTIVE_SLOTS_STEP))
      {
        needed_slots -= MSPS_ACTIVE_SLOTS_STEP;
      }
      else if ((avg_used_slots <= mspsData.currentActiveSlots) && (needed_slots > MSPS_ACTIVE_SLOTS_STEP))
      {
        // In this case, it is possible to reduce the number of active slots 
        // if different conditions are achieved.

        INT32U limit_msg_brurq;
        INT32U limit_value_brurq;

        if (mspsThroughput.tcp)
        {
          // In tcp, be more conservative to reduce active slots
          limit_msg_brurq = MSPS_THROUGHPUT_DOWN_TCP_BRURQ_MSG;
          limit_value_brurq = MSPS_THROUGHPUT_DOWN_TCP_BRURQ_VALUE;
        }
        else
        {
          limit_msg_brurq = MSPS_THROUGHPUT_DOWN_UDP_BRURQ_MSG;
          limit_value_brurq = MSPS_THROUGHPUT_DOWN_UDP_BRURQ_VALUE;
        }

        // Reduce the number of slots if possible, queues must be quite empty after transmit
        // or the number of used slots is lower that our current configured active slots number
        if ((avg_msg_brurq < limit_msg_brurq) && 
            (avg_value_brurq < limit_value_brurq) && 
            (current_msg_brurq < limit_msg_brurq) && 
            (current_value_brurq < limit_value_brurq) &&
            (max_brurq <= MSPS_THROUGHPUT_DOWN_MAX_BRURQ_VALUE) &&
            (avg_max_brurq <= MSPS_THROUGHPUT_DOWN_MAX_BRURQ_AVG))
        {
          needed_slots -= MSPS_ACTIVE_SLOTS_STEP;
        }
      }
    }

    // Check minimum values
    if (mspsTriggersData.throughputMinActiveSlots != MSPS_CONFIGLAYER_AUTOMATIC_ACTIVE_SLOTS)
    {
      if (needed_slots < mspsTriggersData.throughputMinActiveSlots)
      {
        needed_slots = mspsTriggersData.throughputMinActiveSlots;
      }
    }

    if (needed_slots < mspsData.minActiveSlots)
    {
      needed_slots = mspsData.minActiveSlots;
    }

    if (needed_slots < requestActiveSlots)
    {
      request_active_slots = needed_slots;
    }
  }

  if ((request_active_slots < requestActiveSlots) &&
      (request_active_slots != mspsData.currentActiveSlots))
  {
    MspsCounterInc(MSPS_CNT_TRIGGER_THROUGHPUT);
#if _CONFIG_MSPS_TRIGGERS_LOG_
    LogPrint(MspsTriggersLogGet(), MSPS_LOG_LEVEL_TRIGGERS, "TRIGGERS Throughput slots request %d current %d\n",request_active_slots,mspsData.currentActiveSlots);
#endif
  }

#if _CONFIG_MSPS_TRIGGERS_LOG_
  if (mspsThroughput.dataValid)
  {
    LogPrint(MspsTriggersLogGet(), LOG_LEVEL_INFO_10, "TRIGGERS Throughput brurq current %3d %3d avg %3d %3d size %d - tcp %d slots used %2d %2d %2d last %2d next %2d\n",
             current_msg_brurq, 
             current_value_brurq, 
             avg_msg_brurq,
             avg_value_brurq,
             mspsThroughput.avgPktSizeBytes,
             mspsThroughput.tcp,
             current_used_slots, 
             modified_used_slots, 
             avg_used_slots, 
             mspsData.currentActiveSlots,
             request_active_slots);

  }
  else
  {
    LogPrint(MspsTriggersLogGet(), LOG_LEVEL_INFO_11, "%s:: brurq current %3d %3d avg %3d %3d max %3d %3d NumMsg %3d\n", __FUNCTION__,
             current_msg_brurq, 
             current_value_brurq, 
             avg_msg_brurq,
             avg_value_brurq,
             max_brurq,
             avg_max_brurq, num_msgs);
  }
#endif

  return request_active_slots;
}

/*
 ************************************************************************
 ** Public function implementation
 ************************************************************************
 */

t_mspsError MspsTriggersInit(void)
{
  CHAR *buffer = NULL;
  t_configLayerError cl_err;

  // Get reset cause
  mspsInternalResetCause =  WatchdogResetCauseInstall(MSPS_RESET_CAUSE, NULL, NULL);
  checkpoint(mspsInternalResetCause != WATCHDOG_RESET_CAUSE_ERROR, 2);

  // Check last reset cause and increase reset counter if needed
  cl_err = ConfigLayerParamGet((void *)"SYSTEM.GENERAL.RESET_CAUSE", 0, 0, &buffer,
          CFL_FLAG_VALUE_IS_BINARY | CFL_FLAG_RUNTIME | CFL_FLAG_MALLOC );

  if ((cl_err == CONFIGLAYER_ERR_NONE) && (buffer != NULL))
  {
    buffer[SYSTEM_GENERAL_RESET_CAUSE_SIZE - 1]=0;
    if (strstr(buffer,MSPS_RESET_CAUSE)!=NULL)
    {
      INT32U reset_counter;

      cl_err = ConfigLayerParamGet((void *)"MSPS.STATS.RESET_COUNTER", 0, 0, &reset_counter,
               CFL_FLAG_VALUE_IS_BINARY | CFL_FLAG_RUNTIME);

      checkpoint(cl_err == CONFIGLAYER_ERR_NONE,3);

      if (cl_err == CONFIGLAYER_ERR_NONE)
      {
        reset_counter++;
        cl_err = ConfigLayerParamSet((void *)"MSPS.STATS.RESET_COUNTER", 0, 0, &reset_counter,
                 CFL_FLAG_VALUE_IS_BINARY | CFL_FLAG_RUNTIME);

        checkpoint(cl_err == CONFIGLAYER_ERR_NONE,4);
      }
    }
  }

  if (buffer != NULL)
  {
    free(buffer);
  }


#if _CONFIG_MSPS_TRIGGERS_LOG_
    // initialize our log
    mspsTriggersLog = SyslogCreate("mspsTriggers", LOG_LEVEL_ERROR | LOG_LEVEL_WARNING, NULL);

    if (NULL == mspsTriggersLog)
    {
      checkpoint(FALSE, 5);
    }
#endif

  MspsTriggersReset();

  return MSPS_ERR_NONE;
}

/************************************************************************/
t_mspsError MspsTriggersReset(void)
{
  // Init all variables
  mspsTriggersInit = TRUE;

  memset(&mspsInternal,0,sizeof(t_mspsInternal));
  memset(&mspsThroughput,0,sizeof(t_mspsThroughput));
  memset(&mspsInfo,16,sizeof(t_mspsInfo));
  mspsInfo.id = 0;
  return MSPS_ERR_NONE;
}

/************************************************************************/
t_mspsError MspsTriggersInfoDump(void)
{
  INT8U i;

  // Information for console
  iprintf("\nRequests : ");
  for (i=1 ; i<= MSPS_TRIGGERS_INFO_SIZE; i++)
  {
    iprintf("%d ",mspsInfo.requests[(mspsInfo.id + MSPS_TRIGGERS_INFO_SIZE - i)%MSPS_TRIGGERS_INFO_SIZE]);
  }
  iprintf("\n");
  
  iprintf("\nActive slots : ");
  for (i=1 ; i<= MSPS_TRIGGERS_INFO_SIZE; i++)
  {
    iprintf("%d ",mspsInfo.slots[(mspsInfo.id + MSPS_TRIGGERS_INFO_SIZE - i)%MSPS_TRIGGERS_INFO_SIZE]);
  }
  iprintf("\n");

  return MSPS_ERR_NONE;
}

/************************************************************************/

INT8U MspsTriggersAverageActiveSlotsGet(void)
{
  INT32U tmp = 0;
  INT8U i;

  for (i=0; i< MSPS_TRIGGERS_INFO_SIZE; i++)
  {
    tmp += mspsInfo.slots[i];
  }            
  tmp = CEIL(tmp,MSPS_TRIGGERS_INFO_SIZE);

  return (INT8U)tmp;
}

/************************************************************************/
t_mspsError MspsTriggersEvaluate(void)
{
  t_mspsError ret = MSPS_ERR_NONE;
  INT8U request_active_slots;
  INT8U minimal_to_request;

  GET_UPTIME(&now);

  if (!mspsTriggersAllowed || !mspsResetAllowed)
  {
    INT32U uptime_ms = (now.tv_sec * 1000) + (now.tv_usec/1000);
    if (!mspsTriggersAllowed)
    {
      mspsTriggersAllowed = (uptime_ms >= MSPS_WAIT_MIN_UPTIME_MS);
    }
    if (!mspsResetAllowed)
    {
      mspsResetAllowed = (uptime_ms >= MSPS_RESET_MIN_UPTIME_MS);
    }
  }

  // Protection against high temperatures must be checked also when MSPS is no supported in the network
  if (mspsTriggersData.resetEnable || mspsTriggersData.internalEnable)
  {
    mspsInternal.temperatureId++;
    if (mspsInternal.temperatureId >= MSPS_TEMPERATURE_CHECK_PERIOD_ITERATIONS)
    {
      t_configLayerError clayer_err;
      INT32S temperature_signed;
   
      clayer_err = ConfigLayerParamGet((void *)"TEMPSENSORS.GENERAL.MEASURE", 0, 0, &temperature_signed,
                   CFL_FLAG_VALUE_IS_BINARY | CFL_FLAG_RUNTIME);
    
      // Measure could fail, so, ignore this value if there is any error
      checkpoint(clayer_err == CONFIGLAYER_ERR_NONE, 10001);
      checkpoint(temperature_signed > 0, 10002);
    
      if ((clayer_err == CONFIGLAYER_ERR_NONE) && (temperature_signed > 0))
      {
        mspsInternal.temperatureId = 0;
        mspsInternal.temperature = temperature_signed;
        // Store maximum temperature for debug purposes
        if (mspsInternal.temperature > mspsTriggersData.statsMaxTemp)
        {
          mspsTriggersData.statsMaxTemp = mspsInternal.temperature;
        }
      }
    }
  }
    
    // First check if a reset due high temperature is needed
  if (mspsTriggersData.resetEnable && mspsResetAllowed)
  {
     MspsTriggerResetEvaluate(mspsInternal.temperature);
  }

  // Check triggers that reduce Active slots only if MSPS is supported in the network
  // Do not start with MSPS until some time after booting
  if (mspsData.mspsAllowed && mspsTriggersAllowed)
  {
    // Initial value for the request, start with the maximum possible value
    request_active_slots = mspsData.maxActiveSlots;
    mspsData.requestActiveSlots = MSPS_CONFIGLAYER_AUTOMATIC_ACTIVE_SLOTS;

    if (mspsData.forcedActiveSlots != MSPS_CONFIGLAYER_AUTOMATIC_ACTIVE_SLOTS)
    {
      /* Check forced Active Slots value, intended only for validation */
#if _CONFIG_MSPS_TRIGGERS_LOG_
      LogPrint(MspsTriggersLogGet(), MSPS_LOG_LEVEL_TRIGGERS, "TRIGGERS Forced\n");
#endif
      request_active_slots = mspsData.forcedActiveSlots;
      if (mspsData.forcedActiveSlots != mspsData.currentActiveSlots)
      {
        MspsCounterInc(MSPS_CNT_TRIGGER_FORCED);
      }
    }
    else
    {
#if _CONFIG_MSPS_TRIGGERS_LOG_
      LogPrint(MspsTriggersLogGet(), MSPS_LOG_LEVEL_TRIGGERS, "TRIGGERS Evaluate requests\n");
#endif
      // Clear trigger variables when triggers are disabled
      if (!mspsTriggersData.resetEnable)
      {
        MSPS_TIMESTAMP_RESET(mspsInternal.timestampReset);
      }
      if (!mspsTriggersData.internalEnable)
      {
        MSPS_TIMESTAMP_RESET(mspsInternal.timestampUp);
        MSPS_TIMESTAMP_RESET(mspsInternal.timestampDown);
      }
      if (!mspsTriggersData.priorityEnable)
      {
        MSPS_TIMESTAMP_RESET(mspsThroughput.priorityTimestamp);
      }
      if (!mspsTriggersData.throughputEnable)
      {
        mspsThroughput.dataValid = FALSE;
        mspsThroughput.dataId = 0;
      }

      /* Check external trigger */
      if (mspsTriggersData.externalEnable)
      {
        request_active_slots = MspsTriggerExternalThrottlingEvaluate(request_active_slots);
      }

      /* Check internal trigger */
      if (mspsTriggersData.internalEnable) 
      {
        request_active_slots = MspsTriggerInternalThrottlingEvaluate(request_active_slots, mspsInternal.temperature);
      }

      /* Check priority traffic detection */
      if (mspsTriggersData.priorityEnable && (mspsData.prioritiesDetected != 0))
      {
        // Priority traffic is enabled and configured priorities have been detected
        // In this case, traffic throttling must not be applied to reduce latency
        if (MSPS_TIMESTAMP_IS_ZERO(mspsThroughput.priorityTimestamp))
        {
#if _CONFIG_MSPS_TRIGGERS_LOG_
          LogPrint(MspsTriggersLogGet(), LOG_LEVEL_INFO_11, "High priority detected");
#endif
          // Do not check any trigger more, use as a request the maximum value decided by temperature protection triggers
          MspsCounterInc(MSPS_CNT_TRIGGER_PRIORITY);
        }
        mspsThroughput.priorityTimestamp = now;
        mspsData.prioritiesDetected = 0;
      }
      else if ((mspsTriggersData.priorityEnable) && (mspsData.prioritiesDetected == 0) && !MSPS_TIMESTAMP_IS_ZERO(mspsThroughput.priorityTimestamp))
      {
        // Wait until the configured time after the traffic has been detected for last time before applying the Traffic trigger
        struct timeval  diff_time;
        INT32U diff_ms;

        TimeTimevalSubtract(&now, &mspsThroughput.priorityTimestamp, &diff_time);
        diff_ms = (diff_time.tv_usec/1000) + (diff_time.tv_sec*1000);

        if (diff_ms >= mspsTriggersData.priorityExitMs)
        {
#if _CONFIG_MSPS_TRIGGERS_LOG_
          LogPrint(MspsTriggersLogGet(), LOG_LEVEL_INFO_11, "High priority timeout");
#endif
          MSPS_TIMESTAMP_RESET(mspsThroughput.priorityTimestamp);
          mspsData.prioritiesDetected = 0;
        }
      }
      else
      {
        /* Check throughput */
        if (mspsTriggersData.minimalEnable)
        {
          request_active_slots = MspsTriggerMinimalEvaluate(request_active_slots);
        }
        if (mspsTriggersData.throughputEnable)
        {
          request_active_slots = MspsTriggerThroughputThrottlingEvaluate(request_active_slots);
        }
      }
    }

    mspsInfo.requests[mspsInfo.id] = request_active_slots;
    mspsInfo.slots[mspsInfo.id] = mspsData.currentActiveSlots;
    mspsInfo.id = ((mspsInfo.id+1)%MSPS_TRIGGERS_INFO_SIZE);

    // Final requested slots must be limited by the configured steps in active slots.
    // Only allowed an exception with the minimalActiveSlots configuration

    minimal_to_request = MspsMinimalActiveSlotsToApply();

    if (request_active_slots != minimal_to_request)
    {
      request_active_slots = (((request_active_slots + MSPS_ACTIVE_SLOTS_STEP - 1) / MSPS_ACTIVE_SLOTS_STEP) * MSPS_ACTIVE_SLOTS_STEP);
    }

    // If request value is different than current value, send an event to the task to generate a schedule update
    if ((ret == MSPS_ERR_NONE) && ((request_active_slots != mspsData.currentActiveSlots) || (mspsTriggersInit == TRUE)))
    {
      mspsTriggersInit = FALSE;

      ret = MspsEvScheduleUpdateSend(request_active_slots);
      checkpoint(ret == MSPS_ERR_NONE, 1);
#if _CONFIG_MSPS_TRIGGERS_LOG_
      LogPrint(MspsTriggersLogGet(), LOG_LEVEL_INFO_22, "TRIGGERS temp %5.5d request %2.2d current %2.2d tcp %d bps %d %3d %3d extra %d\n", mspsInternal.temperature, request_active_slots, mspsData.currentActiveSlots,mspsThroughput.tcp, mspsData.bpsAlert,mspsData.bpsTxFactor,mspsData.bpsRxFactor, mspsExtraBLERSlots);
#endif
    }
  }

#if _CONFIG_MSPS_TRIGGERS_LOG_
  LogPrint(MspsTriggersLogGet(), LOG_LEVEL_INFO_21, "%s:: minimal %s%s%s bursty %s%s%s dataValid %s%s%s request %2.2d current %2.2d\n", __FUNCTION__,
             SET_STR_GREEN(mspsThroughput.minimal),
             BOOLEAN2STR(mspsThroughput.minimal),
             UNSET_STR_COLOR(mspsThroughput.minimal),
             SET_STR_GREEN(mspsThroughput.bursty),
             BOOLEAN2STR(mspsThroughput.bursty),
             UNSET_STR_COLOR(mspsThroughput.bursty),
             SET_STR_GREEN(mspsThroughput.dataValid),
             BOOLEAN2STR(mspsThroughput.dataValid),
             UNSET_STR_COLOR(mspsThroughput.dataValid),
             request_active_slots,
             mspsData.currentActiveSlots);
#endif

  return ret;
}

/**
 * @}
 **/
