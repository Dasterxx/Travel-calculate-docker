import React, { useState } from 'react';
import {
  Box,
  Card,
  CardActionArea,
  CardContent,
  CardMedia,
  Typography,
} from '@mui/material';
import COUNTRIES_DATA from './StaticCountriesData';

export default function CountriesCarousel({ onSelectCountry }) {
  const [hoveredCountryCode, setHoveredCountryCode] = useState(null);

  return (
    <Box sx={{ maxWidth: 900, mx: 'auto', p: 2 }}>
      <Typography variant="h4" mb={2} textAlign="center">
        Explore Countries
      </Typography>

      <Box
        sx={{
          display: 'flex',
          overflowX: 'auto',
          gap: 2,
          pb: 1,
          '&::-webkit-scrollbar': { height: 8 },
          '&::-webkit-scrollbar-thumb': { backgroundColor: '#ccc', borderRadius: 4 },
          scrollbarWidth: 'thin',
          scrollbarColor: '#ccc transparent',
        }}
      >
        {COUNTRIES_DATA.map((country) => (
          <Card
            key={country.code}
            sx={{
              minWidth: 160,
              flexShrink: 0,
              cursor: 'pointer',
              border: hoveredCountryCode === country.code ? '2px solid #1976d2' : '1px solid #ddd',
              boxShadow: hoveredCountryCode === country.code ? 6 : 1,
              transition: 'border-color 0.3s, box-shadow 0.3s',
            }}
            onClick={() => onSelectCountry(country)}
            onMouseEnter={() => setHoveredCountryCode(country.code)}
            onMouseLeave={() => setHoveredCountryCode(null)}
            title={`Click to view details about ${country.name}`}
          >
            <CardActionArea>
              <CardMedia
                component="img"
                height="100"
                image={country.flag}
                alt={`Flag of ${country.name}`}
                sx={{ objectFit: 'contain', p: 1 }}
              />
              <CardContent>
                <Typography variant="subtitle1" noWrap>
                  {country.name}
                </Typography>
              </CardContent>
            </CardActionArea>
          </Card>
        ))}
      </Box>
    </Box>
  );
}
