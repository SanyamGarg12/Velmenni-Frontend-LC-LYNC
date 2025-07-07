# SNR Tab Missing - Troubleshooting Guide

## Problem Description
After compiling HTML and JS files and uploading to firmware, the SNR tab is not visible in the web interface.

## Root Cause Analysis

The SNR tab visibility is controlled by the `MEASUREDEBUG.GENERAL.ENABLE` parameter. This parameter "Enables/Disables SNR, CFR, Noise, etc measures for debugging".

## Solutions

### Solution 1: Enable Measurements in Configuration

1. **Check Current Setting:**
   - Navigate to the device configuration
   - Look for parameter: `MEASUREDEBUG.GENERAL.ENABLE`
   - Current value should be `YES` or `true`

2. **Enable Measurements:**
   - Set `MEASUREDEBUG.GENERAL.ENABLE = YES`
   - Save configuration
   - Restart the device if required

### Solution 2: Verify Backend Variable Substitution

The SNR page depends on several backend-injected variables. Ensure these are properly substituted:

**Required Variables:**
- `$$PHYMNG.GENERAL.RUNNING_PHYMODE_ID$$`
- `$$MEDIA_TYPE$$`
- `$$ASIC_TYPE$$`
- `$$FREQ_AVG$$`
- `$$SYSTEM.PRODUCTION.MAC_ADDR$$`
- `$$NODE.GENERAL.DEVICE_ID$$`
- `$$DIDMNG.GENERAL.DIDS$$`
- `$$DIDMNG.GENERAL.MACS$$`
- `$$MEASUREDEBUG.GENERAL.ENABLE$$`

**Test Method:**
1. Open `test_snr.html` in your browser
2. Check if variables show actual values or placeholder text ($$VARIABLE$$)
3. If placeholders are shown, backend variable substitution is not working

### Solution 3: Check File Inclusion

Ensure all required files are included in the compilation:

**Required Files:**
- `snr_and_psd.html`
- `snr_measurements.js`
- `common.js`
- `styles.css`

**Verification:**
1. Check if files exist in the compiled binary
2. Verify file permissions
3. Ensure no build scripts exclude these files

### Solution 4: Browser Console Debugging

1. **Open Browser Developer Tools:**
   - Press F12 or right-click → Inspect
   - Go to Console tab

2. **Check for Errors:**
   - Look for JavaScript errors
   - Check for 404 errors (missing files)
   - Verify Chart.js CDN is accessible

3. **Test SNR Page Directly:**
   - Navigate directly to `snr_and_psd.html`
   - Check if page loads without errors

### Solution 5: Conditional Logic Implementation

The updated code now includes conditional logic:

1. **In `index.html`:**
   ```javascript
   // Hide SNR menu if measurements are disabled
   if ("$$MEASUREDEBUG.GENERAL.ENABLE$$" !== "YES" && "$$MEASUREDEBUG.GENERAL.ENABLE$$" !== "true") {
     document.getElementById('snr-link').style.display = 'none';
   }
   ```

2. **In `snr_and_psd.html`:**
   - Shows disabled message when measurements are off
   - Prevents page initialization when disabled

## Configuration Parameters

### Key Parameters to Check:

1. **MEASUREDEBUG.GENERAL.ENABLE**
   - **Purpose:** Enables/Disables SNR, CFR, Noise measurements
   - **Values:** YES/NO or true/false
   - **Default:** Check device documentation

2. **PHYMNG.GENERAL.RUNNING_PHYMODE_ID**
   - **Purpose:** Current running PHY mode
   - **Required:** For SNR calculations

3. **MEDIA_TYPE**
   - **Purpose:** Type of media (PLC, COAX, PHONE)
   - **Required:** For frequency calculations

## Testing Steps

1. **Use the Test Page:**
   - Open `test_snr.html`
   - Check all test sections
   - Verify variable substitution

2. **Manual Testing:**
   - Enable `MEASUREDEBUG.GENERAL.ENABLE`
   - Restart device
   - Check if SNR tab appears
   - Test SNR page functionality

3. **Network Testing:**
   - Ensure IP connectivity between nodes
   - Verify network topology is correct
   - Check if TX/RX nodes are available

## Common Issues

1. **SNR Tab Not Visible:**
   - Check `MEASUREDEBUG.GENERAL.ENABLE` setting
   - Verify backend variable substitution
   - Check browser console for errors

2. **SNR Page Shows Error:**
   - Check if all backend variables are substituted
   - Verify network connectivity
   - Check device firmware version

3. **No Measurement Data:**
   - Ensure network topology is correct
   - Check if TX/RX nodes are properly configured
   - Verify measurement parameters

## Prevention

1. **Always check `MEASUREDEBUG.GENERAL.ENABLE` before deployment**
2. **Test backend variable substitution in development**
3. **Include all required files in compilation**
4. **Test SNR functionality after firmware updates**

## Support

If issues persist:
1. Check device logs for errors
2. Verify firmware version compatibility
3. Contact technical support with:
   - Device model and firmware version
   - Configuration settings
   - Browser console errors
   - Network topology information 