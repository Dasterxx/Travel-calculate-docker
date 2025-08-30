import React from 'react';
import { Button, Card, CardContent, Typography, Box, List, ListItem, ListItemText } from '@mui/material';

export default function CountryCard({ country, onBack }) {
  return (
    <Box display="flex" justifyContent="center" p={2} bgcolor="#f9f9f9" minHeight="100vh">
      <Card sx={{ maxWidth: 600, width: '100%' }}>
        <CardContent>
          <Typography variant="h4" gutterBottom>{country.name}</Typography>
          <Box component="img" src={country.flag} alt={`Flag of ${country.name}`} sx={{ width: '100%', maxHeight: 200, objectFit: 'contain', mb: 2 }} />

          <Typography variant="body1"><strong>Capital:</strong> {country.capital}</Typography>
          <Typography variant="body1"><strong>Population:</strong> {country.population.toLocaleString()}</Typography>
          <Typography variant="body1"><strong>Language:</strong> {country.languages.join(', ')}</Typography>
          <Typography variant="body1" gutterBottom><strong>Currency:</strong> {country.currency}</Typography>

          <Typography variant="h6" mt={3}>Travel Advisory:</Typography>
          <Typography variant="body2" mb={3}>{country.advisory}</Typography>

          <Typography variant="h6">Insurance Requirements:</Typography>
          <List dense>
            {country.requirements.map((req, i) => (
              <ListItem key={i} sx={{ pl: 0 }}>
                <ListItemText primary={req} />
              </ListItem>
            ))}
          </List>
        </CardContent>
      </Card>
    </Box>
  );
}
