import React from 'react';
import { Box, Typography, FormControl, InputLabel, Select, MenuItem } from '@mui/material';

const RISK_OPTIONS = [
  { value: 'TRAVEL_MEDICAL', label: 'Travel Medical' },
  { value: 'TRAVEL_THIRD_PARTY_LIABILITY', label: 'Third Party Liability' },
  { value: 'TRAVEL_EVACUATION', label: 'Evacuation' },
  { value: 'TRAVEL_CANCELLATION', label: 'Cancellation' },
  { value: 'TRAVEL_LOSS_BAGGAGE', label: 'Loss of Baggage' },
  { value: 'TRAVEL_SPORT_ACTIVITIES', label: 'Sport Activities' },
];

function InsuranceSection({ insuranceLimit, onLimitChange, selectedRisks, onRisksChange }) {
  return (
    <Box mb={4}>
      <FormControl fullWidth sx={{ mb: 3 }}>
        <InputLabel id="insurance-limit-label">Insurance Limit</InputLabel>
        <Select
          labelId="insurance-limit-label"
          id="insuranceLimit"
          value={insuranceLimit}
          label="Insurance Limit"
          onChange={onLimitChange}
        >
          <MenuItem value="10000">10,000 EUR</MenuItem>
          <MenuItem value="15000">15,000 EUR</MenuItem>
          <MenuItem value="20000">20,000 EUR</MenuItem>
          <MenuItem value="25000">25,000 EUR</MenuItem>
        </Select>
      </FormControl>

      <Typography variant="h5" mb={2}>Risks</Typography>
      <FormControl fullWidth>
        <InputLabel id="risks-label">Select Risk</InputLabel>
        <Select
          labelId="risks-label"
          id="selectedRisks"
          value={selectedRisks[0] || ''}
          label="Select Risk"
          onChange={onRisksChange}
        >
          <MenuItem value=""><em>-- Select Risk --</em></MenuItem>
          {RISK_OPTIONS.map(risk => (
            <MenuItem key={risk.value} value={risk.value}>{risk.label}</MenuItem>
          ))}
        </Select>
      </FormControl>
    </Box>
  );
}

export default InsuranceSection;
