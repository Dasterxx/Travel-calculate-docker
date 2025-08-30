import React from 'react';
import { Box, Typography, TextField, Button, IconButton } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';

function PersonsSection({ persons, onChange, onAddPerson, onRemovePerson }) {
  return (
    <Box mb={4}>
      <Typography variant="h5" mb={2}>Person Details</Typography>
      {persons.map((person, idx) => (
        <Box key={idx} mb={3} p={2} border="1px solid #ccc" borderRadius={2} position="relative">
          <Typography variant="h6" mb={2}>Person {idx + 1}</Typography>

          <TextField
            label="First Name"
            value={person.firstName}
            onChange={e => onChange(idx, 'firstName', e.target.value)}
            fullWidth
            required
            sx={{ mb: 2 }}
          />
          <TextField
            label="Last Name"
            value={person.lastName}
            onChange={e => onChange(idx, 'lastName', e.target.value)}
            fullWidth
            required
            sx={{ mb: 2 }}
          />
          <TextField
            label="Birth Date"
            type="date"
            value={person.birthDate}
            onChange={e => onChange(idx, 'birthDate', e.target.value)}
            fullWidth
            required
            InputLabelProps={{ shrink: true }}
          />

          {persons.length > 1 && (
            <IconButton
              aria-label={`Remove person ${idx + 1}`}
              onClick={() => onRemovePerson(idx)}
              color="error"
              sx={{ position: 'absolute', top: 8, right: 8 }}
            >
              <DeleteIcon />
            </IconButton>
          )}
        </Box>
      ))}

      <Button variant="outlined" onClick={onAddPerson}>Add Person</Button>
    </Box>
  );
}

export default PersonsSection;
