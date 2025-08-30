import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { fetchWithLogging } from '../utils/fetchWithLogging';
import CountriesCarousel from '../components/common/CountriesCarousel';
import CountryCard from '../components/common/CountryCard';
import { Box, CircularProgress, Typography, Alert, Button } from '@mui/material';
import COUNTRIES_DATA from '../components/common/StaticCountriesData';

export default function CountriesPage() {
  const [countries, setCountries] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const { countryCode } = useParams();
  const navigate = useNavigate();

  useEffect(() => {
    const fetchCountries = async () => {
      setLoading(true);
      setError('');
      try {
        // Если хотите использовать API — раскомментируйте
        /*
        const response = await fetchWithLogging('/api/countries', {
          credentials: 'include',
        });
        if (response.ok) {
          const data = await response.json();
          setCountries(data);
        } else {
          const data = await response.json();
          setError(data.message || 'Failed to load countries');
        }
        */
        // Для разработки используем статические данные
        setCountries(COUNTRIES_DATA);
      } catch (err) {
        setError('Network error. Please try again.');
      } finally {
        setLoading(false);
      }
    };

    fetchCountries();
  }, []);

  if (loading) {
    return (
      <Box className="container" textAlign="center" mt={4}>
        <CircularProgress />
        <Typography mt={2}>Loading countries...</Typography>
      </Box>
    );
  }

  if (error) {
    return (
      <Box className="container" mt={4}>
        <Alert severity="error">{error}</Alert>
      </Box>
    );
  }

  if (countryCode) {
    const selectedCountry = countries.find(c => c.code === countryCode);
    if (!selectedCountry) {
      return (
        <Box className="container" mt={4}>
          <Alert severity="error">Country not found</Alert>
          <Button onClick={() => navigate('/countries')} sx={{ mt: 2 }}>
            Back to countries list
          </Button>
        </Box>
      );
    }

    return (
      <Box className="container" sx={{ maxWidth: 900, mx: 'auto', p: 2 }}>
        <Button variant="outlined" onClick={() => navigate('/countries')} sx={{ mb: 2 }}>
          ← Back to countries
        </Button>
        <CountryCard country={selectedCountry} />
      </Box>
    );
  }

  return (
    <Box className="container" sx={{ maxWidth: 900, mx: 'auto', p: 2 }}>
      <CountriesCarousel
        countries={countries}
        onSelectCountry={(country) => navigate(`/countries/${country.code}`)}
      />
    </Box>
  );
}
