import React from 'react';
import { Box, FormControl, InputLabel, Select, MenuItem, IconButton, Typography, Button } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';

const COUNTRY_OPTIONS = [
  { value: 'USA', label: 'UNITED STATES' },
  { value: 'SPAIN', label: 'SPAIN' },
  { value: 'JAPAN', label: 'JAPAN' },
  { value: 'LATVIA', label: 'LATVIA' },
];

function CountriesSection({ countries, onAdd, onChange, onRemove }) {
  return (
    <Box mb={4}>
      <Typography variant="h5" mb={2}>Countries to Visit</Typography>
      {countries.map((country, idx) => (
        <Box key={idx} display="flex" alignItems="center" mb={2} gap={1}>
          <FormControl fullWidth>
            <InputLabel id={`country-label-${idx}`}>Country {idx + 1}</InputLabel>
            <Select
              labelId={`country-label-${idx}`}
              id={`country${idx}`}
              value={country}
              label={`Country ${idx + 1}`}
              onChange={e => onChange(idx, e.target.value)}
            >
              <MenuItem value=""><em>-- Select Country --</em></MenuItem>
              {COUNTRY_OPTIONS.map(opt => (
                <MenuItem key={opt.value} value={opt.value}>{opt.label}</MenuItem>
              ))}
            </Select>
          </FormControl>
          {countries.length > 1 && (
            <IconButton
              aria-label={`Remove country ${idx + 1}`}
              onClick={() => onRemove(idx)}
              color="error"
            >
              <DeleteIcon />
            </IconButton>
          )}
        </Box>
      ))}
      <Button variant="outlined" startIcon={<AddIcon />} onClick={onAdd}>
        Add Country
      </Button>
    </Box>
  );
}

export default CountriesSection;
