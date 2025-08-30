import React from 'react';
import { Link } from 'react-router-dom';
import { Card, CardContent, Typography, Grid, Box, Button } from '@mui/material';

export default function CountryList({ countries, onSelect }) {
  return (
    <Box p={3}>
      <Typography variant="h4" gutterBottom>Available Countries</Typography>
      <Grid container spacing={3}>
        {countries.map(country => (
          <Grid item xs={12} sm={6} md={4} key={country.code}>
            <Card sx={{ cursor: 'pointer', height: '100%', display: 'flex', flexDirection: 'column' }} onClick={() => onSelect(country)}>
              <Box sx={{ height: 140, overflow: 'hidden', display: 'flex', justifyContent: 'center', alignItems: 'center', bgcolor: '#f0f0f0' }}>
                <img src={country.flag} alt={`${country.name} flag`} style={{ maxHeight: '100%', maxWidth: '100%' }} />
              </Box>
              <CardContent sx={{ flexGrow: 1 }}>
                <Typography variant="h6" gutterBottom>{country.name}</Typography>
                <Typography variant="body2" color="text.secondary">{country.description}</Typography>
              </CardContent>
              <Box sx={{ p: 2 }}>
                <Button component={Link} to={`/countries/${country.code}`} variant="outlined" fullWidth>
                  Learn More
                </Button>
              </Box>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
}
