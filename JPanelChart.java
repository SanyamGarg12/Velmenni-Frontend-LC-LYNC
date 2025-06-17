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



 import java.awt.BasicStroke;
 
 import java.awt.BorderLayout;
 
 import java.awt.Color;
 
 import java.awt.Font;
 
 import java.awt.Paint;
 
 import java.awt.SystemColor;
 
 
 
 import javax.swing.JPanel;
 
 import javax.swing.JSplitPane;
 
 import javax.swing.border.EmptyBorder;
 
 
 
 import measpackets.MeasResponse;
 
 import network.MarvellNet;
 
 
 
 import org.jfree.chart.ChartFactory;
 
 import org.jfree.chart.ChartPanel;
 
 import org.jfree.chart.JFreeChart;
 
 import org.jfree.chart.annotations.XYTextAnnotation;
 
 import org.jfree.chart.axis.AxisLocation;
 
 import org.jfree.chart.axis.NumberAxis;
 
 import org.jfree.chart.axis.NumberTickUnit;
 
 import org.jfree.chart.plot.PlotOrientation;
 
 import org.jfree.chart.plot.XYPlot;
 
 import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
 
 import org.jfree.chart.renderer.xy.XYItemRenderer;
 
 import org.jfree.chart.title.LegendTitle;
 
 import org.jfree.chart.title.TextTitle;
 
 import org.jfree.data.xy.DefaultTableXYDataset;
 
 import org.jfree.data.xy.XYDataItem;
 
 import org.jfree.data.xy.XYDataset;
 
 import org.jfree.data.xy.XYSeries;
 
 
 
 import javax.swing.JCheckBox;
 
 
 
 import java.awt.event.ActionListener;
 
 import java.io.BufferedWriter;
 
 import java.io.FileWriter;
 
 import java.io.IOException;
 
 import java.awt.event.ActionEvent;
 
 
 
 public class JPanelChart extends JPanel{
 
 /*
 
  * Setabis' GAINs table per media type
 
 ======================================================================= 
 
 | Channel Name | #Gains | GainIdx | Gain PLC | Gain Coax | Gain Phone |
 
 =======================================================================
 
 | Rx 0         |     32 |       0 |  255     |  255      |  255 |
 
 | Rx 0         |     32 |       1 |  -26     |  -18      |  -18 |
 
 | Rx 0         |     32 |       2 |  -26     |  -18      |  -18 |
 
 | Rx 0         |     32 |       3 |  -26     |  -18      |  -18 |
 
 | Rx 0         |     32 |       4 |  -24     |  -18      |  -18 |
 
 | Rx 0         |     32 |       5 |  -22     |  -16      |  -16 |
 
 | Rx 0         |     32 |       6 |  -20     |  -14      |  -14 |
 
 | Rx 0         |     32 |       7 |  -18     |  -12      |  -12 |
 
 | Rx 0         |     32 |       8 |  -16     |  -10      |  -10 |
 
 | Rx 0         |     32 |       9 |  -14     |   -8      |   -8 |
 
 | Rx 0         |     32 |      10 |  -12     |   -6      |   -6 |
 
 | Rx 0         |     32 |      11 |  -10     |   -4      |   -4 |
 
 | Rx 0         |     32 |      12 |   -8     |   -2      |   -2 |
 
 | Rx 0         |     32 |      13 |   -6     |    0      |    0 |
 
 | Rx 0         |     32 |      14 |   -4     |    2      |    2 |
 
 | Rx 0         |     32 |      15 |   -2     |    4      |    4 |
 
 | Rx 0         |     32 |      16 |    0     |    6      |    6 |
 
 | Rx 0         |     32 |      17 |    2     |    8      |    8 |
 
 | Rx 0         |     32 |      18 |    4     |   10      |   10 |
 
 | Rx 0         |     32 |      19 |    6     |   12      |   12 |
 
 | Rx 0         |     32 |      20 |    8     |   14      |   14 |
 
 | Rx 0         |     32 |      21 |   10     |   16      |   16 |
 
 | Rx 0         |     32 |      22 |   12     |   18      |   18 |
 
 | Rx 0         |     32 |      23 |   14     |   20      |   20 |
 
 | Rx 0         |     32 |      24 |   16     |   22      |   22 |
 
 | Rx 0         |     32 |      25 |   18     |   24      |   24 |
 
 | Rx 0         |     32 |      26 |   20     |   26      |   26 |
 
 | Rx 0         |     32 |      27 |   22     |   28      |   28 |
 
 | Rx 0         |     32 |      28 |   24     |   30      |   30 |
 
 | Rx 0         |     32 |      29 |   26     |   32      |   32 |
 
 | Rx 0         |     32 |      30 |   28     |   34      |   34 |
 
 | Rx 0         |     32 |      31 |   30     |   36      |   36 |
 
     */                                      
 
   static final int AGC_VALUE_DISABLED = 255;
 
   static final int agc_turia_gains [][] = {
 
     {AGC_VALUE_DISABLED, -26, -26, -26, -24, -22, -20, -18, -16, -14, -12, -10, -8, -6, -4, -2,  0,  2,  4,  6,  8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30 }, /// PLC 
 
     {AGC_VALUE_DISABLED, -18, -18, -18, -18, -16, -14, -12, -10,  -8,  -6,  -4, -2,  0,  2,  4,  6,  8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36},  /// COAX
 
     {AGC_VALUE_DISABLED, -18, -18, -18, -18, -16, -14, -12, -10,  -8,  -6,  -4, -2,  0,  2,  4,  6,  8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36} /// PHONE
 
     };
 
 
 
   private enum mediaType {
 
                   MEDIA_PLC(0),
 
                   MEDIA_COAX(1),
 
                   MEDIA_PHONE(2);
 
                   private int value;
 
                   private mediaType (int value)
 
                   {
 
                     this.value = value;
 
                   }
 
   };
 
   
 
   /// Offsets to convert values to dBm/Hz
 
   static final int SNR_PHY_MON_OFFSET_TURIA = 10; 
 
   static final int CFR_PHY_MON_OFFSET_TURIA = 60;
 
   static final int NOISE_PHY_MON_OFFSET_TURIA = 61;
 
   
 
   JSplitPane splitPane;
 
     public JFreeChart chart, chart2;
 
     public ChartPanel chartpanel, chartpanel2;
 
     private XYPlot plot, plot2;
 
     
 
     private NumberAxis xAxis, xAxis2;
 
     private NumberAxis yAxis, yAxis2;
 
         
 
     public boolean minMaxMode = false;
 
     private boolean startMinMaxMode = true;
 
     private XYSeries seriesLast = null;
 
     private XYSeries seriesMin = null;
 
     private XYSeries seriesMax = null;
 
   private XYSeries seriesMinPrevious = null;
 
   private XYSeries seriesMaxPrevious = null;
 
     
 
   private XYSeries seriesLast2 = null;
 
   private XYSeries seriesMin2 = null;
 
   private XYSeries seriesMax2 = null;
 
   private XYSeries seriesMinPrevious2 = null;
 
   private XYSeries seriesMaxPrevious2 = null;
 
   private XYSeries[] seriesStored = {null, null, null, null, null};
 
   private XYSeries[] seriesStored2 = {null, null, null, null, null};
 
   private boolean[] seriesStore = {false, false, false, false, false};
 
   private Color[] seriesStoredColor = {new Color(0x7f,0xff,0xd4),new Color(0xff,0xa5,0x00),new Color(0xdd,0xa0,0xdd),new Color(0xbd,0xb7,0x6b),new Color(0xbc,0x75,0xd9)};
 
   private double minY, maxY, maxY2;
 
   
 
   private String title;
 
   private String typeOfMeasurement;
 
   
 
   public boolean mimo;
 
   protected boolean pause = false;
 
   
 
   private mediaType media;
 
   
 
   private Double dStartFreq; 
 
   private Double dCarrierSpacing; 
 
   private Double dCorrectionFactor; 
 
   private Boolean agc2only;
 
   private Boolean muba;
 
   
 
 
 
   
 
   XYTextAnnotation max_label, agc_label, max_label2, agc_label2;
 
   
 
     public JPanelChart(String title, String sTypeOfMeas, boolean mimo) {
 
       typeOfMeasurement = sTypeOfMeas;
 
       this.title = title;
 
       this.mimo = mimo;
 
     setLayout(new BorderLayout(0, 0));
 
         plotBaseGraph(sTypeOfMeas, mimo);
 
         chartpanel.setLocation(0, 0);
 
         chartpanel.setBackground(SystemColor.control);
 
         
 
       dStartFreq = new Double(0.0); 
 
       dCarrierSpacing = new Double(0.0); 
 
       dCorrectionFactor = new Double(0.0); 
 
       agc2only = new Boolean(true);
 
       muba = new Boolean(true);
 
 
 
     }
 
     
 
     private void prepareModeParameters (int profileId, boolean turia)
 
     {
 
       agc2only = false; /// Default value
 
     switch (profileId) {
 
     case 9: 
 
       if (!turia) dCorrectionFactor=102.3; //COAX 100
 
       media = mediaType.MEDIA_COAX;
 
       break;
 
     case 10: 
 
       if (!turia) dCorrectionFactor=108.0;//PHONE 100
 
       agc2only = true;
 
       media = mediaType.MEDIA_PHONE;
 
       break;
 
     case 11: 
 
       if (!turia) dCorrectionFactor=108.0;//PHONE 100 without notches
 
       agc2only = true;
 
       media = mediaType.MEDIA_PHONE;
 
       break;
 
     case 15:
 
     case 16: // PLC
 
     case 17:
 
     case 20: // PLC
 
     case 22: // PLC
 
       media = mediaType.MEDIA_PLC;
 
       muba = false;
 
       break;
 
     case 24: 
 
       media = mediaType.MEDIA_PHONE;
 
       if (!turia) dCorrectionFactor=108.0;//PHONE 100 without notches
 
       agc2only = true;
 
       break;
 
     case 25: 
 
       media = mediaType.MEDIA_COAX;
 
       if (!turia) dCorrectionFactor=107.0; //COAX 200
 
       break;
 
     case 26:
 
       media = mediaType.MEDIA_PHONE;
 
       if (!turia) dCorrectionFactor=114.0;//PHONE 200
 
       break;
 
     case 28: 
 
       media = mediaType.MEDIA_PHONE;
 
       if (!turia) dCorrectionFactor=114.0;//PHONE 200 Flat
 
       break;
 
     case 29: 
 
       media = mediaType.MEDIA_PHONE;
 
       if (!turia) dCorrectionFactor=114.0;//PHONE 100 MIMO
 
       break;
 
     default: // PLC
 
       media = mediaType.MEDIA_PLC;
 
       if (!turia) dCorrectionFactor=104.5; //PLC
 
       break;
 
     }
 
     
 
     dCarrierSpacing = 0.024414; // PLC value is the base for all other media
 
     switch (media) {
 
       case MEDIA_PLC:
 
         dStartFreq = 1.831112;
 
         if (turia)
 
         {
 
           if ((profileId == 15) || (profileId == 17))
 
           {
 
             dCarrierSpacing = dCarrierSpacing/2;  ///Robust modes has half of carrier spacing 
 
             dCorrectionFactor = 74.5; //A.Jimenez correction factor for robust modes
 
           }
 
           else
 
           {
 
             dCorrectionFactor = 78.0; //A.Jimenez correction factor
 
           }
 
         }
 
         else
 
         {
 
           dCorrectionFactor = 104.5; //PLC
 
         }
 
 
 
         break;
 
       case MEDIA_COAX:
 
         dStartFreq = 2.148224;
 
         dCarrierSpacing = dCarrierSpacing * 8;
 
         if (turia)
 
         {
 
           dCorrectionFactor = 72.75; // A.Jimenez correction factor
 
         }
 
         break;
 
       case MEDIA_PHONE:
 
         dStartFreq = 3.564928;
 
         dCarrierSpacing = dCarrierSpacing * 2;
 
         if (turia)
 
         {
 
           dCorrectionFactor = 80.0; // A.Jimenez correction factor
 
         }
 
         break;
 
 
 
     }
 
  /*   System.out.println("Profile " + profileId + " StartFreq " + dStartFreq + 
 
         " CarrierSpc " + dCarrierSpacing + " Correct " + dCorrectionFactor + 
 
         " media " + media + " agc2only " + agc2only + " muba " + muba);
 
         */
 
     }
 
     
 
   /// Returns SNR value in dBm/Hz
 
     private double getSnrValue(int idx, boolean [] rmsc, byte rawValue)
 
     {
 
       double snr_value;
 
       
 
     if (rmsc == null || rmsc[idx] == false)
 
     {
 
       snr_value = (((int) 0xFF & rawValue)*0.25 - SNR_PHY_MON_OFFSET_TURIA);
 
     }
 
     else
 
     {
 
       snr_value = -SNR_PHY_MON_OFFSET_TURIA;
 
     }
 
     return snr_value;
 
     }
 
     
 
     /// Returns noise or CFR value in dBm/Hz
 
     private double getSignalValue(int idx, byte rawValue, int agc_gain, boolean noise, boolean turia, boolean [] rmsc)
 
     {
 
       double noise_value;
 
       int    signal_offset_dbm;
 
       int    valid_carrier = 1;
 
       
 
       if (turia)
 
       {
 
         if (noise)
 
         {
 
           signal_offset_dbm = NOISE_PHY_MON_OFFSET_TURIA;
 
         }
 
         else
 
         {
 
           signal_offset_dbm = CFR_PHY_MON_OFFSET_TURIA;
 
           if ((rmsc != null) && (rmsc[idx] == true))
 
           {
 
             valid_carrier = 0;
 
           }
 
         }
 
       }
 
       else
 
       {
 
         signal_offset_dbm = 0;
 
       }
 
 
 
       noise_value = ((int) 0xFF & rawValue)*0.25 * valid_carrier - dCorrectionFactor - agc_gain - signal_offset_dbm;
 
       
 
       return noise_value;
 
     }
 
     
 
   public void plotsnr(MeasResponse measResponse, int profileId, boolean turia, boolean[] rmsc) 
 
     {
 
       if (!pause)
 
       {
 
         byte[] measPayload = measResponse.getMeasPayload();
 
         int measNValues = measResponse.getNValues();
 
         int fAverage = (int) Math.pow(2, measResponse.getFreqAvg());
 
            
 
       prepareModeParameters(profileId, turia);
 
       
 
 
 
         if (mimo)
 
         {
 
           measNValues = (int)(measNValues/2);
 
           splitPane.setDividerLocation(0.5);
 
         }
 
 
 
         for (int i = 0; i<seriesStore.length; i++)
 
         {
 
           if (seriesStored[i] == null && seriesStore[i] == true)
 
           {
 
             try {
 
             seriesStored[i] = (XYSeries) seriesLast.clone();
 
           } catch (CloneNotSupportedException e) {
 
             // TODO Auto-generated catch block
 
             e.printStackTrace();
 
           }
 
           }
 
           else if (seriesStore[i] == false)
 
           {
 
             seriesStored[i] = null;
 
           }
 
         }
 
         seriesLast = new XYSeries("CH1 Last", false, false);
 
         seriesMin = new XYSeries("CH1 Min", false, false);
 
         seriesMax = new XYSeries("CH1 Max", false, false);
 
         
 
       UpdateAGCLabel(measResponse, turia, media, mimo, agc2only, muba);
 
 
 
         for(int i = 0;i < measNValues;i++)
 
         {
 
           for(int j = 0;j < fAverage;j++){
 
             double snr_value;
 
             
 
             snr_value = getSnrValue(i, rmsc, measPayload[i]);
 
             XYDataItem value = new XYDataItem(dStartFreq+(i*fAverage + j)*dCarrierSpacing, snr_value);
 
             seriesLast.add(value);
 
 
 
             if (startMinMaxMode) {
 
               seriesMin.add(value);
 
               seriesMax.add(value);
 
             } else {	
 
               seriesMin.addOrUpdate(value.getXValue(), Math.min(value.getYValue(), (Double) seriesMinPrevious.getY(i*fAverage + j)));
 
               seriesMax.addOrUpdate(value.getXValue(), Math.max(value.getYValue(), (Double) seriesMaxPrevious.getY(i*fAverage + j)));
 
             }
 
           }
 
         }
 
 
 
         seriesMinPrevious= seriesMin;	
 
         seriesMaxPrevious= seriesMax;		
 
 
 
         if (mimo)
 
         {
 
           
 
           for (int i = 0; i<seriesStore.length; i++)
 
           {
 
             if (seriesStored2[i] == null && seriesStore[i] == true)
 
             {
 
               try {
 
                 seriesStored2[i] = (XYSeries) seriesLast2.clone();
 
               } catch (CloneNotSupportedException e) {
 
                 // TODO Auto-generated catch block
 
                 e.printStackTrace();
 
               }
 
             }
 
             else if (seriesStore[i] == false)
 
             {
 
               seriesStored2[i] = null;
 
             }
 
           }
 
           seriesLast2 = new XYSeries("CH2 Last", false, false);
 
           seriesMin2 = new XYSeries("CH2 Min", false, false);
 
           seriesMax2 = new XYSeries("CH2 Max", false, false);
 
 
 
           for(int i = 0;i < measNValues;i++)
 
           {
 
             for(int j = 0;j < fAverage;j++){
 
               double snr_value;
 
               
 
               snr_value = getSnrValue(i, rmsc, measPayload[i+measNValues]);
 
               XYDataItem value = new XYDataItem(dStartFreq+(i*fAverage + j)*dCarrierSpacing, snr_value);
 
               seriesLast2.add(value);
 
 
 
               if (startMinMaxMode) {
 
                 seriesMin2.add(value);
 
                 seriesMax2.add(value);
 
               } else {  
 
                 seriesMin2.addOrUpdate(value.getXValue(), Math.min(value.getYValue(), (Double) seriesMinPrevious2.getY(i*fAverage + j)));
 
                 seriesMax2.addOrUpdate(value.getXValue(), Math.max(value.getYValue(), (Double) seriesMaxPrevious2.getY(i*fAverage + j)));
 
               }
 
             }
 
           }
 
 
 
           seriesMinPrevious2 = seriesMin2; 
 
           seriesMaxPrevious2 = seriesMax2; 
 
         }
 
 
 
         plotCommon();
 
       }
 
     }
 
     
 
     public void plotnoise(MeasResponse measResponse, int profileId, boolean turia) 
 
   {
 
     if (!pause)
 
     {
 
       byte[] measPayload = measResponse.getMeasPayload();
 
       int measNValues = measResponse.getNValues();
 
       int fAverage = (int) Math.pow(2, measResponse.getFreqAvg());
 
       int agc_step = 3;
 
 
 
       prepareModeParameters(profileId, turia);
 
 
 
       if (mimo)
 
       {
 
         measNValues = (int)(measNValues/2);
 
         splitPane.setDividerLocation(0.5);
 
       }
 
       
 
       for (int i = 0; i<seriesStore.length; i++)
 
       {
 
         if (seriesStored[i] == null && seriesStore[i] == true)
 
         {
 
           try {
 
             seriesStored[i] = (XYSeries) seriesLast.clone();
 
           } catch (CloneNotSupportedException e) {
 
             // TODO Auto-generated catch block
 
             e.printStackTrace();
 
           }
 
         }
 
         else if (seriesStore[i] == false)
 
         {
 
           seriesStored[i] = null;
 
         }
 
       }
 
       seriesLast = new XYSeries("CH1 Last", false, false);
 
       seriesMin = new XYSeries("CH1 Min", false, false);
 
       seriesMax = new XYSeries("CH1 Max", false, false);
 
       
 
       UpdateAGCLabel(measResponse, turia, media, mimo, agc2only, muba);
 
 
 
       for(int i = 0;i < measNValues;i++)
 
       {
 
         int agc;
 
         
 
           agc = getAGCValue(measResponse, i, turia, media, mimo, agc2only, muba, 1, agc_step);
 
 
 
           if (agc != AGC_VALUE_DISABLED)
 
           {
 
             for(int j = 0;j < fAverage;j++)
 
             {
 
               double noise_value;
 
               
 
               noise_value = getSignalValue(i, measPayload[i], agc, true, turia, null);
 
               XYDataItem value = new XYDataItem(dStartFreq+(i*fAverage + j)*dCarrierSpacing, noise_value);
 
               seriesLast.add(value);
 
 
 
               if (startMinMaxMode) {
 
                 seriesMin.add(value);
 
                 seriesMax.add(value);
 
               } else {  
 
                 seriesMin.addOrUpdate(value.getXValue(), Math.min(value.getYValue(), (Double) seriesMinPrevious.getY(i*fAverage + j)));
 
                 seriesMax.addOrUpdate(value.getXValue(), Math.max(value.getYValue(), (Double) seriesMaxPrevious.getY(i*fAverage + j)));
 
               }
 
             }
 
           }
 
       }
 
 
 
       seriesMinPrevious= seriesMin; 
 
       seriesMaxPrevious= seriesMax;   
 
 
 
       if (mimo)
 
       {
 
         
 
         for (int i = 0; i<seriesStore.length; i++)
 
         {
 
           if (seriesStored2[i] == null && seriesStore[i] == true)
 
           {
 
             try {
 
               seriesStored2[i] = (XYSeries) seriesLast2.clone();
 
             } catch (CloneNotSupportedException e) {
 
               // TODO Auto-generated catch block
 
               e.printStackTrace();
 
             }
 
           }
 
           else if (seriesStore[i] == false)
 
           {
 
             seriesStored2[i] = null;
 
           }
 
         }
 
         seriesLast2 = new XYSeries("CH2 Last", false, false);
 
         seriesMin2 = new XYSeries("CH2 Min", false, false);
 
         seriesMax2 = new XYSeries("CH2 Max", false, false);
 
 
 
         for(int i = 0;i < measNValues;i++)
 
         {
 
           int agc;
 
 
 
           agc = getAGCValue(measResponse, i, turia, media, mimo, agc2only, muba, 2, agc_step);
 
           
 
           if (agc != AGC_VALUE_DISABLED)
 
           {
 
             for(int j = 0;j < fAverage;j++)
 
             {
 
               double noise_value;
 
               
 
               noise_value = getSignalValue(i, measPayload[i + measNValues], agc, true, turia, null);
 
               XYDataItem value = new XYDataItem(dStartFreq+(i*fAverage + j)*dCarrierSpacing, noise_value);
 
               seriesLast2.add(value);
 
 
 
               if (startMinMaxMode) {
 
                 seriesMin2.add(value);
 
                 seriesMax2.add(value);
 
               } else {  
 
                 seriesMin2.addOrUpdate(value.getXValue(), Math.min(value.getYValue(), (Double) seriesMinPrevious2.getY(i*fAverage + j)));
 
                 seriesMax2.addOrUpdate(value.getXValue(), Math.max(value.getYValue(), (Double) seriesMaxPrevious2.getY(i*fAverage + j)));
 
               }
 
             }
 
           }
 
         }
 
 
 
         seriesMinPrevious2 = seriesMin2; 
 
         seriesMaxPrevious2 = seriesMax2; 
 
       }
 
 
 
       plotCommon();
 
     }
 
   }
 
     
 
     public void plotCommon()
 
     {
 
       XYItemRenderer renderer = new StandardXYItemRenderer();
 
     DefaultTableXYDataset dataplot = new DefaultTableXYDataset();
 
     DefaultTableXYDataset dataplot2 = new DefaultTableXYDataset();
 
     
 
     renderer.setSeriesPaint(dataplot.getSeriesCount(), Color.green);
 
     dataplot.addSeries(seriesLast);
 
     if (mimo)
 
     {
 
       dataplot2.addSeries(seriesLast2);
 
     }
 
     for (int i = 0; i<seriesStored.length; i++)
 
     {
 
       if (seriesStored[i] != null)
 
       {
 
         renderer.setSeriesPaint(dataplot.getSeriesCount(), seriesStoredColor[i]);
 
         dataplot.addSeries(seriesStored[i]);
 
         if (mimo && seriesStored2[i] != null)
 
         {
 
           dataplot2.addSeries(seriesStored2[i]);
 
         }
 
       }
 
     }
 
 
 
     if (minMaxMode)
 
     {
 
       startMinMaxMode = false;
 
       renderer.setSeriesPaint(dataplot.getSeriesCount(), Color.RED);
 
       dataplot.addSeries(seriesMin);
 
       if (mimo)
 
       {
 
         dataplot2.addSeries(seriesMin2);
 
       }
 
       renderer.setSeriesPaint(dataplot.getSeriesCount(), Color.YELLOW);
 
       dataplot.addSeries(seriesMax);
 
       if (mimo)
 
       {
 
         dataplot2.addSeries(seriesMax2);
 
       }
 
     }
 
     else
 
     {
 
       startMinMaxMode = true;
 
     }
 
     
 
     plot.setRenderer(0, renderer);
 
     plot.setDataset(0, dataplot);
 
     plot.mapDatasetToRangeAxis(0, 0);
 
 
 
     if (mimo)
 
     {
 
       plot2.setRenderer(0, renderer);
 
       plot2.setDataset(0, dataplot2);
 
       plot2.mapDatasetToRangeAxis(0, 0);
 
     }
 
     
 
     adjustYRange();
 
     
 
     }
 
     
 
     public void plotcfr(MeasResponse measResponse, int profileId, boolean turia, boolean[] rmsc) 
 
     {
 
       if (!pause)
 
       {
 
         byte[] measPayload = measResponse.getMeasPayload();
 
         int measNValues = measResponse.getNValues();
 
         int fAverage = (int) Math.pow(2, measResponse.getFreqAvg());
 
         int agc_step = 3;
 
         if (turia)
 
         {
 
           agc_step = 2;
 
         }
 
 
 
       prepareModeParameters(profileId, turia);
 
         
 
         if (mimo)
 
         {
 
           measNValues = (int)(measNValues/2);
 
           splitPane.setDividerLocation(0.5);
 
         }
 
       
 
         for (int i = 0; i<seriesStore.length; i++)
 
         {
 
           if (seriesStored[i] == null && seriesStore[i] == true)
 
           {
 
             try {
 
               seriesStored[i] = (XYSeries) seriesLast.clone();
 
             } catch (CloneNotSupportedException e) {
 
               // TODO Auto-generated catch block
 
               e.printStackTrace();
 
             }
 
           }
 
           else if (seriesStore[i] == false)
 
           {
 
             seriesStored[i] = null;
 
           }
 
         }
 
 
 
         seriesLast = new XYSeries("CH1 Last", false, false);
 
         seriesMin = new XYSeries("CH1 Min", false, false);
 
         seriesMax = new XYSeries("CH1 Max", false, false);
 
       
 
       UpdateAGCLabel(measResponse, turia, media, mimo, agc2only, muba);
 
 
 
         for(int i = 0;i < measNValues;i++)
 
         {
 
           int agc;
 
 
 
           agc = getAGCValue(measResponse, i, turia, media, mimo, agc2only, muba, 1, agc_step);
 
           if (agc != AGC_VALUE_DISABLED)
 
           {
 
             for(int j = 0;j < fAverage;j++)
 
             {
 
               double cfr_value;
 
               
 
             cfr_value = getSignalValue(i, measPayload[i], agc, false, turia, rmsc);
 
               XYDataItem value = new XYDataItem(dStartFreq+(i*fAverage + j)*dCarrierSpacing, cfr_value);
 
               seriesLast.add(value);
 
               if (startMinMaxMode) {
 
                 seriesMin.add(value);
 
                 seriesMax.add(value);
 
               } else {  
 
                 seriesMin.addOrUpdate(value.getXValue(), Math.min(value.getYValue(), (Double) seriesMinPrevious.getY(i*fAverage + j)));
 
                 seriesMax.addOrUpdate(value.getXValue(), Math.max(value.getYValue(), (Double) seriesMaxPrevious.getY(i*fAverage + j)));
 
               }
 
             }
 
           }
 
         } 
 
 
 
         seriesMinPrevious= seriesMin;	
 
         seriesMaxPrevious= seriesMax;	
 
 
 
         if (mimo)
 
         {
 
           splitPane.setDividerLocation(0.5);
 
           
 
           for (int i = 0; i<seriesStore.length; i++)
 
           {
 
             if (seriesStored2[i] == null && seriesStore[i] == true)
 
             {
 
               try {
 
                 seriesStored2[i] = (XYSeries) seriesLast2.clone();
 
               } catch (CloneNotSupportedException e) {
 
                 // TODO Auto-generated catch block
 
                 e.printStackTrace();
 
               }
 
             }
 
             else if (seriesStore[i] == false)
 
             {
 
               seriesStored2[i] = null;
 
             }
 
           }
 
           seriesLast2 = new XYSeries("CH2 Last", false, false);
 
           seriesMin2 = new XYSeries("CH2 Min", false, false);
 
           seriesMax2 = new XYSeries("CH2 Max", false, false);
 
 
 
           for(int i = 0;i < measNValues;i++)
 
           {
 
             int agc;
 
 
 
             agc = getAGCValue(measResponse, i, turia, media, mimo, agc2only, muba, 2, agc_step);
 
             if (agc != AGC_VALUE_DISABLED)
 
             {
 
               for(int j = 0;j < fAverage;j++)
 
               {
 
                 double cfr_value;
 
                 
 
                 cfr_value = getSignalValue(i, measPayload[i+measNValues], agc, false, turia, rmsc);
 
                 XYDataItem value = new XYDataItem(dStartFreq+(i*fAverage + j)*dCarrierSpacing, cfr_value);
 
                 seriesLast2.add(value);
 
 
 
                 if (startMinMaxMode) {
 
                   seriesMin2.add(value);
 
                   seriesMax2.add(value);
 
                 } else {  
 
                   seriesMin2.addOrUpdate(value.getXValue(), Math.min(value.getYValue(), (Double) seriesMinPrevious2.getY(i*fAverage + j)));
 
                   seriesMax2.addOrUpdate(value.getXValue(), Math.max(value.getYValue(), (Double) seriesMaxPrevious2.getY(i*fAverage + j)));
 
                 }
 
               }
 
             }
 
           } 
 
 
 
           seriesMinPrevious2 = seriesMin2; 
 
           seriesMaxPrevious2 = seriesMax2; 
 
         }
 
 
 
         plotCommon();
 
       }
 
     }
 
     
 
     int getAGCValue(MeasResponse measResponse, int carrierIdx, boolean turia, mediaType media, boolean mimo, boolean agc2only, boolean muba, int mimoChannel, int agcStep)
 
     {
 
         int agc_idx = 0;
 
         int agc_value = 0;
 
         
 
     if ((media == mediaType.MEDIA_PLC) && !mimo)
 
     {
 
       if (carrierIdx < 1317)
 
       {
 
         agc_idx = measResponse.getAGC1();
 
       }
 
       else if (!turia)
 
       {
 
         agc_idx = measResponse.getAGC2();
 
       }
 
       else
 
       {
 
          agc_idx = measResponse.getAGC3();
 
       }
 
     }
 
     else if ((media == mediaType.MEDIA_PLC) && mimo && turia) // This is MIMO 50 and MIMO 100 for Turia
 
     {
 
       if ((carrierIdx < 1317) || (muba == false))
 
       {
 
           if (mimoChannel == 1)
 
           {
 
               agc_idx = measResponse.getAGC1();
 
           }
 
           else if (mimoChannel == 2)
 
           {
 
               agc_idx = measResponse.getAGC3();      		
 
           }
 
       }
 
       else
 
       {
 
           if (mimoChannel == 1)
 
           {
 
               agc_idx = measResponse.getAGC2();
 
           }
 
           else if (mimoChannel == 2)
 
           {
 
               agc_idx = measResponse.getAGC4();      		
 
           }
 
       }
 
     }
 
     else if (agc2only)
 
     {
 
       agc_idx = measResponse.getAGC2();
 
     }
 
     else
 
     {
 
         if ((mimo == true) && (mimoChannel == 2))
 
         {
 
             agc_idx = measResponse.getAGC2();
 
         }
 
         else
 
         {
 
             agc_idx = measResponse.getAGC1();    		
 
         }
 
     }
 
     
 
     if (turia)
 
     {
 
       agc_value = agc_turia_gains[media.value][agc_idx];
 
     }
 
     else
 
     {
 
       agc_value = agc_idx * agcStep;
 
     }
 
     return agc_value;
 
     }
 
         
 
     void UpdateAGCLabel(MeasResponse measResponse, boolean turia, mediaType media, boolean mimo, boolean agc2only, boolean muba)
 
     {
 
       if (turia & (media == mediaType.MEDIA_PLC) & mimo & muba)
 
       {
 
         agc_label.setText("AGC RX: "+ measResponse.getAGC1() + ","+ measResponse.getAGC2());
 
       }
 
       else if (turia & (media == mediaType.MEDIA_PLC) & !mimo)  
 
       {
 
         agc_label.setText("AGC RX: "+ measResponse.getAGC1() + ","+ measResponse.getAGC3());
 
       }
 
       else if (!turia & (media == mediaType.MEDIA_PLC) & !mimo)
 
       {
 
         agc_label.setText("AGC RX: "+ measResponse.getAGC1() + ","+ measResponse.getAGC2());
 
       }
 
       else if (agc2only)
 
       {
 
         agc_label.setText("AGC RX: "+ measResponse.getAGC2());
 
       }
 
       else
 
       {
 
         agc_label.setText("AGC RX: "+ measResponse.getAGC1());
 
       }
 
       
 
     if (mimo)
 
     {
 
       if (turia & (media == mediaType.MEDIA_PLC) & muba)
 
       {
 
         agc_label2.setText("AGC RX: "+ measResponse.getAGC3() + ","+ measResponse.getAGC4());
 
       }
 
       else if (turia & (media == mediaType.MEDIA_PLC) & !muba)
 
       {
 
         agc_label2.setText("AGC RX: "+ measResponse.getAGC3());
 
       }
 
       else
 
       {
 
         agc_label2.setText("AGC RX: "+ measResponse.getAGC2());
 
       }
 
     }
 
     }
 
     
 
     public void adjustYRange()
 
     {
 
       updateMinY();
 
       updateMaxY();
 
     if (minY < maxY)
 
     {
 
       yAxis.setRange(10*Math.floor(minY/10), 10*Math.ceil((maxY+5)/10));
 
     }
 
     xAxis.setRange(-0.1, 5*Math.ceil(seriesLast.getMaxX()/5));
 
     max_label.setX(5*Math.ceil(seriesLast.getMaxX()/5) - 10);
 
     max_label.setY(maxY);
 
     max_label.setText("MAX: "+maxY+" dB");
 
     agc_label.setX(5*Math.ceil(seriesLast.getMaxX()/5) - 10);
 
     agc_label.setY(10*Math.floor(minY/10) + 5);
 
 
 
     if (mimo)
 
     {
 
       if (minY < maxY)
 
       {
 
         yAxis2.setRange(10*Math.floor(minY/10), 10*Math.ceil((maxY+5)/10));
 
       }
 
       xAxis2.setRange(-0.1, 5*Math.ceil(seriesLast.getMaxX()/5));
 
       max_label2.setX(5*Math.ceil(seriesLast.getMaxX()/5) - 10);
 
       max_label2.setY(maxY2);
 
       max_label2.setText("MAX: "+maxY2+" dB");
 
       agc_label2.setX(5*Math.ceil(seriesLast.getMaxX()/5) - 10);
 
       agc_label2.setY(10*Math.floor(minY/10) + 5);
 
 
 
     }
 
     }
 
     
 
     public void plotBaseGraph(String sTypeOfMeasurement, boolean mimo)
 
     {
 
         chart = ChartFactory.createXYLineChart(
 
             title+(mimo?" (CH1)":""),
 
                 "Frequency (MHz)", 
 
                 sTypeOfMeasurement.contains("NOISE")?"NOISE (dBm/Hz)":sTypeOfMeasurement.contains("PSD")?"PSD (dBm/Hz)":"SNR (dB)", 
 
                 null,
 
                 PlotOrientation.VERTICAL,
 
                 false,
 
                 true,
 
                 false
 
         );
 
         
 
         TextTitle t = new TextTitle(title+(mimo?" (CH1)":""));
 
         t.setFont(new Font("Tahoma", Font.BOLD, 14));
 
     chart.setTitle(t);
 
     max_label = new XYTextAnnotation("MAX: ", 0, 0);
 
     max_label.setFont(new Font("Tahoma", Font.BOLD, 12));
 
     max_label.setPaint(Color.YELLOW);
 
     agc_label = new XYTextAnnotation("AGC RX: ", 0, 0);
 
     agc_label.setFont(new Font("Tahoma", Font.BOLD, 12));
 
     agc_label.setPaint(Color.WHITE);
 
 
 
     
 
     chart.setBackgroundPaint(SystemColor.control);
 
         plot = chart.getXYPlot();
 
         
 
         plot.addAnnotation(max_label);
 
     plot.addAnnotation(agc_label);
 
 
 
         plot.setBackgroundPaint(new Color(0, 30, 0));
 
         
 
     plot.setDomainGridlinePaint(Color.WHITE);
 
     plot.setDomainGridlineStroke(new BasicStroke(0.2f));
 
     plot.setRangeGridlinePaint(Color.WHITE);
 
     plot.setRangeGridlineStroke(new BasicStroke(0.2f));
 
         
 
     xAxis = (NumberAxis) plot.getDomainAxis();
 
     xAxis.setAutoTickUnitSelection(true);
 
     xAxis.setAutoRange(false);
 
 
 
     yAxis = (NumberAxis) plot.getRangeAxis();
 
     yAxis.setAutoTickUnitSelection(true);
 
     yAxis.setAutoRange(false);
 
     yAxis.setAutoRangeIncludesZero(false);
 
 
 
         chartpanel = new ChartPanel(chart);
 
         chartpanel.setMouseWheelEnabled(true);
 
         chartpanel.setMouseZoomable(true);
 
         chartpanel.setBounds(0, 0, 973, 484);
 
     chartpanel.setBorder(new EmptyBorder(6, 0, 0, 0));
 
     chartpanel.setMaximumDrawHeight(3000);
 
     chartpanel.setMinimumDrawHeight(100);
 
     chartpanel.setMaximumDrawWidth(3000);
 
     chartpanel.setMinimumDrawWidth(100);
 
 
 
         if (mimo)
 
         {
 
         chart2 = ChartFactory.createXYLineChart(
 
             title+" (CH2)",
 
             "Frequency (MHz)", 
 
             sTypeOfMeasurement.contains("NOISE")?"NOISE (dBm/Hz)":sTypeOfMeasurement.contains("PSD")?"PSD (dBm/Hz)":"SNR (dB)", 
 
             null,
 
             PlotOrientation.VERTICAL,
 
             false,
 
             true,
 
             false
 
         );
 
         
 
         TextTitle t2 = new TextTitle(title+(mimo?" (CH2)":""));
 
         t2.setFont(new Font("Tahoma", Font.BOLD, 14));
 
         chart2.setTitle(t2);
 
 
 
         chart2.setBackgroundPaint(SystemColor.control);
 
         
 
         plot2 = chart2.getXYPlot();
 
         
 
         max_label2 = new XYTextAnnotation("MAX: ", 0, 0);
 
         max_label2.setFont(new Font("Tahoma", Font.BOLD, 12));
 
         max_label2.setPaint(Color.YELLOW);
 
         agc_label2 = new XYTextAnnotation("AGC RX: ", 0, 0);
 
         agc_label2.setFont(new Font("Tahoma", Font.BOLD, 12));
 
         agc_label2.setPaint(Color.WHITE);
 
         
 
         plot2.addAnnotation(max_label2);
 
         plot2.addAnnotation(agc_label2);
 
 
 
         plot2.setBackgroundPaint(new Color(0, 30, 0));
 
         
 
         plot2.setDomainGridlinePaint(Color.WHITE);
 
         plot2.setDomainGridlineStroke(new BasicStroke(0.2f));
 
         plot2.setRangeGridlinePaint(Color.WHITE);
 
         plot2.setRangeGridlineStroke(new BasicStroke(0.2f));
 
         
 
         xAxis2 = (NumberAxis) plot2.getDomainAxis();
 
         xAxis2.setAutoTickUnitSelection(true);
 
         xAxis2.setAutoRange(false);
 
 
 
         yAxis2 = (NumberAxis) plot2.getRangeAxis();
 
         yAxis2.setAutoTickUnitSelection(true);
 
       yAxis2.setAutoRange(false);
 
       yAxis2.setAutoRangeIncludesZero(false);
 
             
 
         chartpanel2 = new ChartPanel(chart2);
 
         chartpanel2.setMouseWheelEnabled(true);
 
         chartpanel2.setMouseZoomable(true);
 
         chartpanel2.setBounds(0, 0, 973, 484);
 
         chartpanel2.setBorder(new EmptyBorder(6, 0, 0, 0));
 
         chartpanel2.setMaximumDrawHeight(3000);
 
         chartpanel2.setMinimumDrawHeight(100);
 
         chartpanel2.setMaximumDrawWidth(3000);
 
         chartpanel2.setMinimumDrawWidth(100);
 
         }
 
         
 
         if (!mimo)
 
         {
 
           this.add(chartpanel, BorderLayout.CENTER);
 
         }
 
         else
 
         {
 
           splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
 
               chartpanel, chartpanel2);
 
           splitPane.setBorder(new EmptyBorder(0,0,0,0));
 
           this.add(splitPane, BorderLayout.CENTER);
 
         }
 
     }
 
     
 
   public void updateMinY() {
 
     double y = Double.POSITIVE_INFINITY;
 
     
 
     for (int j = 0; j < plot.getDataset().getSeriesCount(); j++)
 
     {
 
       for (int i = 0; i < plot.getDataset().getItemCount(0); i++)
 
       {
 
         y = Math.min(y, plot.getDataset().getYValue(j, i));
 
       }
 
     }
 
     if (mimo)
 
     {
 
       for (int j = 0; j < plot2.getDataset().getSeriesCount(); j++)
 
       {
 
         for (int i = 0; i < plot2.getDataset().getItemCount(0); i++)
 
         {
 
           y = Math.min(y, plot2.getDataset().getYValue(j, i));
 
         }
 
       }
 
     }
 
     minY = (((double)Math.round(y*100))/100);
 
   }
 
     
 
   public String getMinY() {
 
 
 
     if (typeOfMeasurement.contains("SNR"))
 
     {
 
       return ""+minY+" dB";
 
     }
 
     else
 
     {
 
       return ""+minY+" dBm/Hz";
 
     }
 
   }
 
   
 
   public void updateMaxY() {
 
     double y = Double.NEGATIVE_INFINITY;
 
     
 
     if (mimo)
 
     {
 
       for (int j = 0; j < plot2.getDataset().getSeriesCount(); j++)
 
       {
 
         for (int i = 0; i < plot2.getDataset().getItemCount(0); i++)
 
         {
 
           y = Math.max(y, plot2.getDataset().getYValue(j, i));
 
         }
 
       }
 
       maxY2 = (((double)Math.round(y*100))/100);
 
     }
 
     for (int j = 0; j < plot.getDataset().getSeriesCount(); j++)
 
     {
 
       for (int i = 0; i < plot.getDataset().getItemCount(0); i++)
 
       {
 
         y = Math.max(y, plot.getDataset().getYValue(j, i));
 
       }
 
     }
 
 
 
     maxY = (((double)Math.round(y*100))/100);
 
   }
 
   
 
   public String getMaxY() {
 
     if (typeOfMeasurement.contains("SNR"))
 
     {
 
       return ""+maxY+" dB";
 
     }
 
     else
 
     {
 
       return ""+maxY+" dBm/Hz";
 
     }
 
   }
 
 
 
   public void saveChartAsCsv(int channel, String filename) throws IOException {
 
       BufferedWriter bfw = new BufferedWriter(new FileWriter(filename));
 
       XYPlot plot2save;
 
       if (channel == 2)
 
       {
 
         plot2save = plot2;
 
       }
 
       else
 
       {
 
         plot2save = plot;
 
       }
 
       if (plot2save.getDatasetCount() > 0)
 
       {
 
         for (int i = 0; i < plot2save.getDataset().getItemCount(0); i++)
 
         {
 
           bfw.write(String.valueOf(plot2save.getDataset().getXValue(0, i)));
 
           for (int j = 0; j < plot2save.getDataset().getSeriesCount(); j++)
 
           {
 
             bfw.write(","+plot2save.getDataset().getYValue(j, i));
 
           }
 
           bfw.write("\n");
 
         }
 
       }
 
       bfw.close();
 
   }
 
 
 
   public void store(int i, boolean selected) {
 
     seriesStore[i] = selected;
 
   }
 
 }
 
 