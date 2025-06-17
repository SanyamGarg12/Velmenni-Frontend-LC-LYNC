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
    }

    // Calculate SNR value based on JPanelChart.java implementation
    getSnrValue(idx, rmsc, rawValue) {
        if (rmsc && !rmsc[idx]) {
            return -100;
        }
        
        const val = rawValue & 0xFF;
        if (val === 0xFF) {
            return -100;
        }
        
        return val - 100;
    }

    // Calculate signal value based on JPanelChart.java implementation
    getSignalValue(idx, rawValue, agcGain, noise, turia, rmsc) {
        if (rmsc && !rmsc[idx]) {
            return -150;
        }

        const val = rawValue & 0xFF;
        if (val === 0xFF) {
            return -150;
        }

        let gain;
        if (this.media === MEDIA_TYPES.PLC) {
            gain = turia ? this.getTuriaGain(agcGain) : this.getSetabisGain(agcGain);
        } else {
            gain = this.getCoaxPhoneGain(agcGain);
        }

        const freqMhz = this.dStartFreq + (idx * this.dCarrierSpacing);
        const correction = noise ? 0 : this.dCorrectionFactor + (20 * Math.log10(freqMhz / 30.0));
        
        return -150 + val + gain + correction;
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

    // Helper functions for gain calculations
    getTuriaGain(agcGain) {
        // Implementation based on JPanelChart.java turia_gains table
        const turiaGains = [
            -26, -24, -22, -20, -18, -16, -14, -12,
            -10, -8, -6, -4, -2, 0, 2, 4,
            6, 8, 10, 12, 14, 16, 18, 20,
            22, 24, 26, 28, 30, 32, 34, 36
        ];
        return agcGain < turiaGains.length ? turiaGains[agcGain] : 0;
    }

    getSetabisGain(agcGain) {
        // Implementation based on JPanelChart.java setabis_gains table
        const setabisGains = [
            -26, -26, -26, -24, -22, -20, -18, -16,
            -14, -12, -10, -8, -6, -4, -2, 0,
            2, 4, 6, 8, 10, 12, 14, 16,
            18, 20, 22, 24, 26, 28, 30, 32
        ];
        return agcGain < setabisGains.length ? setabisGains[agcGain] : 0;
    }

    getCoaxPhoneGain(agcGain) {
        // Implementation based on JPanelChart.java coax/phone gains table
        const coaxPhoneGains = [
            -18, -18, -18, -18, -16, -14, -12, -10,
            -8, -6, -4, -2, 0, 2, 4, 6,
            8, 10, 12, 14, 16, 18, 20, 22,
            24, 26, 28, 30, 32, 34, 36, 38
        ];
        return agcGain < coaxPhoneGains.length ? coaxPhoneGains[agcGain] : 0;
    }

    // Process measurement data
    async processMeasurements(txNode, measureType) {
        try {
            // Get raw measurement data from device
            const measResponse = await this.fetchMeasurementData(txNode, measureType);
            
            // Process the data based on measurement type
            const data = {
                measurements: [],
                frequencies: [],
                agc: null
            };

            const numCarriers = measResponse.measurements.length;
            for (let i = 0; i < numCarriers; i++) {
                const freq = this.dStartFreq + (i * this.dCarrierSpacing);
                data.frequencies.push(freq);

                if (measureType === 'SNR_PROBE' || measureType === 'SNR_DATA') {
                    data.measurements.push(this.getSnrValue(i, measResponse.rmsc, measResponse.measurements[i]));
                } else {
                    const agcValue = this.getAGCValue(measResponse, i, true, this.media, false, this.agc2only, this.muba, 0, 0);
                    data.measurements.push(this.getSignalValue(i, measResponse.measurements[i], agcValue, measureType === 'NOISE', true, measResponse.rmsc));
                }
            }

            // Get AGC value if available
            if (measResponse.agcValues && measResponse.agcValues.length > 0) {
                data.agc = measResponse.agcValues[0];
            }

            return data;
        } catch (error) {
            console.error('Error processing measurements:', error);
            throw error;
        }
    }

    // Fetch raw measurement data from device
    async fetchMeasurementData(txNode, measureType) {
        // This would be replaced with actual device communication
        // For now, return simulated data
        return {
            measurements: Array(256).fill(0).map(() => Math.floor(Math.random() * 256)),
            agcValues: Array(256).fill(0).map(() => Math.floor(Math.random() * 32)),
            rmsc: Array(256).fill(true),
            timestamp: Date.now()
        };
    }
}

// Export for use in other files
if (typeof module !== 'undefined' && module.exports) {
    module.exports = SNRMeasurements;
} 