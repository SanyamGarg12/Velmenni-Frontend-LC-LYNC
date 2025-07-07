// Constants from JPanelNodeSNR.java and JPanelChart.java
const SNR_PHY_MON_OFFSET_TURIA = 10;
const CFR_PHY_MON_OFFSET_TURIA = 60;
const NOISE_PHY_MON_OFFSET_TURIA = 61;
const AGC_VALUE_DISABLED = 255;

// Media types from JPanelChart.java
const MEDIA_TYPES = {
    PLC: 0,
    COAX: 1,
    PHONE: 2
};

class SNRMeasurements {
    constructor() {
        this.dStartFreq = 0;
        this.dCarrierSpacing = 0;
        this.dCorrectionFactor = 0;
        this.agc2only = false;
        this.muba = false;
        this.media = MEDIA_TYPES.PLC;
        this.fAverage = 1;
    }

    // Set measurement parameters based on profile/media/turia/freqAvg (matches Java logic exactly)
    setMeasurementParams(profileId, media, turia, freqAvg) {
        this.media = media;
        this.fAverage = Math.pow(2, freqAvg);
        this.agc2only = false; // Default value from Java
        
        // Base carrier spacing from Java
        this.dCarrierSpacing = 0.024414; // PLC value is the base for all other media
        
        switch (profileId) {
            case 9: 
                if (!turia) this.dCorrectionFactor = 102.3; //COAX 100
                this.media = MEDIA_TYPES.COAX;
                break;
            case 10: 
                if (!turia) this.dCorrectionFactor = 108.0; //PHONE 100
                this.agc2only = true;
                this.media = MEDIA_TYPES.PHONE;
                break;
            case 11: 
                if (!turia) this.dCorrectionFactor = 108.0; //PHONE 100 without notches
                this.agc2only = true;
                this.media = MEDIA_TYPES.PHONE;
                break;
            case 15:
            case 16: // PLC
            case 17:
            case 20: // PLC
            case 22: // PLC
                this.media = MEDIA_TYPES.PLC;
                this.muba = false;
                break;
            case 24: 
                this.media = MEDIA_TYPES.PHONE;
                if (!turia) this.dCorrectionFactor = 108.0; //PHONE 100 without notches
                this.agc2only = true;
                break;
            case 25: 
                this.media = MEDIA_TYPES.COAX;
                if (!turia) this.dCorrectionFactor = 107.0; //COAX 200
                break;
            case 26:
                this.media = MEDIA_TYPES.PHONE;
                if (!turia) this.dCorrectionFactor = 114.0; //PHONE 200
                break;
            case 28: 
                this.media = MEDIA_TYPES.PHONE;
                if (!turia) this.dCorrectionFactor = 114.0; //PHONE 200 Flat
                break;
            case 29: 
                this.media = MEDIA_TYPES.PHONE;
                if (!turia) this.dCorrectionFactor = 114.0; //PHONE 100 MIMO
                break;
            default: // PLC
                this.media = MEDIA_TYPES.PLC;
                if (!turia) this.dCorrectionFactor = 104.5; //PLC
                break;
        }

        // Set frequency parameters based on media type (exact match with Java)
        switch (this.media) {
            case MEDIA_TYPES.PLC:
                this.dStartFreq = 1.831112;
                if (turia) {
                    if (profileId === 15 || profileId === 17) {
                        this.dCarrierSpacing = this.dCarrierSpacing / 2; // Robust modes has half of carrier spacing
                        this.dCorrectionFactor = 74.5; // A.Jimenez correction factor for robust modes
                    } else {
                        this.dCorrectionFactor = 78.0; // A.Jimenez correction factor
                    }
                } else {
                    this.dCorrectionFactor = 104.5; // PLC
                }
                break;
            case MEDIA_TYPES.COAX:
                this.dStartFreq = 2.148224;
                this.dCarrierSpacing = this.dCarrierSpacing * 8;
                if (turia) {
                    this.dCorrectionFactor = 72.75; // A.Jimenez correction factor
                }
                break;
            case MEDIA_TYPES.PHONE:
                this.dStartFreq = 3.564928;
                this.dCarrierSpacing = this.dCarrierSpacing * 2;
                if (turia) {
                    this.dCorrectionFactor = 80.0; // A.Jimenez correction factor
                }
                break;
        }
    }

    // Calculate SNR value based on JPanelChart.java implementation (exact match, but clamp to >=0)
    getSnrValue(idx, rmsc, rawValue) {
        let snr_value;
        
        if (rmsc == null || rmsc[idx] == false) {
            snr_value = ((rawValue & 0xFF) * 0.25 - SNR_PHY_MON_OFFSET_TURIA);
        } else {
            snr_value = -SNR_PHY_MON_OFFSET_TURIA;
        }
        // Clamp SNR to >= 0 (never negative)
        return Math.max(0, snr_value);
    }

    // Calculate signal value based on JPanelChart.java implementation (exact match)
    getSignalValue(idx, rawValue, agcGain, noise, turia, rmsc) {
        let signal_value;
        let signal_offset_dbm;
        let valid_carrier = 1;
        
        if (turia) {
            if (noise) {
                signal_offset_dbm = NOISE_PHY_MON_OFFSET_TURIA;
            } else {
                signal_offset_dbm = CFR_PHY_MON_OFFSET_TURIA;
                if ((rmsc != null) && (rmsc[idx] == true)) {
                    valid_carrier = 0;
                }
            }
        } else {
            signal_offset_dbm = 0;
        }

        signal_value = ((rawValue & 0xFF) * 0.25 * valid_carrier - this.dCorrectionFactor - agcGain - signal_offset_dbm);
        
        return signal_value;
    }

    // Get AGC value based on JPanelChart.java implementation
    getAGCValue(measResponse, carrierIdx, turia, media, mimo, agc2only, muba, mimoChannel, agcStep) {
        const agcValue = measResponse.agcValues[carrierIdx];
        if (agcValue === AGC_VALUE_DISABLED) {
            return AGC_VALUE_DISABLED;
        }

        if (turia) {
            if (mimo && !muba) {
                return agcValue + (mimoChannel * 32) + (agcStep * 64);
            } else {
                return agcValue + (agcStep * 32);
            }
        } else {
            return agcValue;
        }
    }

    // Process measurement data (matches Java logic exactly)
    async processMeasurements(txNode, measureType, params) {
        // params: {profileId, media, turia, freqAvg}
        if (params) {
            this.setMeasurementParams(params.profileId, params.media, params.turia, params.freqAvg);
        }
        
        try {
            // Set measurement type parameters to match Java exactly
            let measType, askProbe, freqAvg;
            switch(measureType) {
                case 'SNR_PROBE':
                    measType = 0;
                    askProbe = 1;
                    freqAvg = params.freqAvg || 0;
                    break;
                case 'SNR_DATA':
                    measType = 1;
                    askProbe = 0;
                    freqAvg = params.freqAvg || 0;
                    break;
                case 'PSD_RX':
                    measType = 3;
                    askProbe = 0;
                    freqAvg = params.freqAvg || 0;
                    break;
                case 'NOISE':
                    measType = 20;
                    askProbe = 1;
                    freqAvg = params.freqAvg || 0;
                    break;
                default:
                    throw new Error('Invalid measurement type: ' + measureType);
            }

            const measResponse = await this.fetchMeasurementData(txNode, measType, askProbe, freqAvg);
            
            const data = {
                measurements: [],
                frequencies: [],
                agc: null
            };

            const measPayload = measResponse.measurements;
            const measNValues = measResponse.nValues || measPayload.length;
            const fAverage = Math.pow(2, freqAvg);

            // Process measurements exactly as in Java plotsnr method
            for (let i = 0; i < measNValues; i++) {
                for (let j = 0; j < fAverage; j++) {
                    // Calculate frequency exactly as in Java
                    const freq = this.dStartFreq + (i * fAverage + j) * this.dCarrierSpacing;
                    
                    if (j === 0) { // Only add frequency once per carrier
                        data.frequencies.push(parseFloat(freq.toFixed(6))); // Match Java precision
                    }

                    let value;
                    if (measureType === 'SNR_PROBE' || measureType === 'SNR_DATA') {
                        value = this.getSnrValue(i, measResponse.rmsc, measPayload[i]);
                    } else if (measureType === 'NOISE') {
                        const agcValue = this.getAGCValue(measResponse, i, params.turia, this.media, false, this.agc2only, this.muba, 0, 0);
                        value = this.getSignalValue(i, measPayload[i], agcValue, true, params.turia, measResponse.rmsc);
                    } else { // PSD_RX
                        const agcValue = this.getAGCValue(measResponse, i, params.turia, this.media, false, this.agc2only, this.muba, 0, 0);
                        value = this.getSignalValue(i, measPayload[i], agcValue, false, params.turia, measResponse.rmsc);
                    }
                    
                    if (j === 0) { // Only add measurement once per carrier
                        data.measurements.push(value);
                    }
                }
            }

            // Get AGC value if available (match Java UpdateAGCLabel logic)
            if (measResponse.agcValues && measResponse.agcValues.length > 0) {
                const agcValue = measResponse.agcValues[0];
                if (agcValue !== AGC_VALUE_DISABLED) {
                    data.agc = agcValue;
                }
            }

            return data;
        } catch (error) {
            console.error('Error processing measurements:', error);
            throw error;
        }
    }

    // Fetch raw measurement data from device (replace with actual device communication)
    async fetchMeasurementData(txNode, measureType, askProbe, freqAvg) {
        try {
            // Create measurement request similar to Java MeasRequest
            const request = {
                pid: 112211,
                timestamp: 555,
                did: parseInt(txNode),
                timeAvg: 4,
                askProbe: askProbe,
                removeAGC: 0,
                bandReq: 0,
                measType: measureType,
                freqAvg: freqAvg
            };

            // For now, simulate realistic measurement data based on the Java logic
            // In a real implementation, this would communicate with the device
            const numCarriers = 256; // Typical number of carriers
            const measurements = [];
            const agcValues = [];
            const rmsc = [];

            // Generate realistic measurement data (mimic Java tool: smooth, positive SNR curve)
            for (let i = 0; i < numCarriers; i++) {
                // Simulate carrier quality (some carriers may be disabled)
                const isActive = Math.random() > 0.05; // 95% of carriers active (less masking)
                rmsc.push(!isActive); // Java: rmsc[i] = true means masked/disabled
                
                if (!isActive) {
                    measurements.push(255); // Disabled carrier (Java: 255 means masked)
                    agcValues.push(AGC_VALUE_DISABLED);
                } else {
                    // Generate a smooth, decreasing SNR curve with some noise (mimic Java)
                    // SNR starts high (~50dB) and gently decreases with frequency
                    const snrBase = 50 - (i * 0.08); // Smooth decrease
                    const snrNoise = (Math.random() - 0.5) * 2; // Small random noise
                    let snr = snrBase + snrNoise;
                    snr = Math.max(20, Math.min(55, snr)); // Clamp to [20, 55] dB
                    // Convert to raw value as Java expects: raw = (snr + offset) / 0.25
                    const raw = Math.round((snr + SNR_PHY_MON_OFFSET_TURIA) / 0.25);
                    measurements.push(Math.max(0, Math.min(255, raw)));
                    agcValues.push(Math.floor(Math.random() * 8) + 4); // AGC in [4, 11]
                }
            }

            return {
                measurements: measurements,
                agcValues: agcValues,
                rmsc: rmsc,
                nValues: numCarriers,
                timestamp: Date.now(),
                phyMetric: measureType === 20 ? 20 : (measureType === 3 || measureType === 4 ? 3 : 0)
            };
        } catch (error) {
            console.error('Error fetching measurement data:', error);
            throw new Error('Failed to fetch measurement data from device');
        }
    }

    // Export frequency range for chart axis
    getFrequencyRange(numCarriers) {
        const minFreq = this.dStartFreq;
        const maxFreq = this.dStartFreq + (numCarriers - 1) * this.fAverage * this.dCarrierSpacing;
        return { minFreq, maxFreq };
    }
}

// Export for use in other files
if (typeof module !== 'undefined' && module.exports) {
    module.exports = SNRMeasurements;
} 