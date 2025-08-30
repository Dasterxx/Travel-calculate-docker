import React from 'react';
import { Box, Typography, TextField } from '@mui/material';

function DatesSection({ from, to, onFromChange, onToChange }) {
  return (
    <Box mb={4}>
      <Typography variant="h5" mb={2}>
        Agreement Dates
      </Typography>
      <Box display="flex" gap={2} flexWrap="wrap">
        {[{ label: 'Agreement Date From', value: from, onChange: onFromChange },
          { label: 'Agreement Date To', value: to, onChange: onToChange }].map(({ label, value, onChange }) => (
          <TextField
            key={label}
            label={label}
            type="datetime-local"
            value={value}
            onChange={onChange}
            InputLabelProps={{ shrink: true }}
            sx={{
              flex: 1,
              minWidth: 220,
              backgroundColor: 'white',
              borderRadius: 1,
              boxShadow: 1,
              // Чтобы встроить стили для фокуса и границ при необходимости
              '& .MuiOutlinedInput-root': {
                '&.Mui-focused fieldset': {
                  borderColor: 'primary.main',
                  boxShadow: '0 0 0 2px rgba(25,118,210,0.3)',
                },
              },
            }}
            InputProps={{
              sx: {
                // Для WebKit (Chrome, Edge, Safari)
                '&::-webkit-calendar-picker-indicator': {
                  cursor: 'pointer',
                  filter: 'invert(0.4) brightness(1.2)', // Сделать иконку темнее и ярче
                  transition: 'filter 0.3s ease',
                  '&:hover': {
                    filter: 'invert(0) brightness(1.5)', // При наведении ярче
                  },
                },
                // Для Firefox (ограниченно, но попытка)
                '&::-moz-focus-inner': {
                  border: 0,
                },
              },
            }}
          />
        ))}
      </Box>
    </Box>
  );
}

export default DatesSection;
