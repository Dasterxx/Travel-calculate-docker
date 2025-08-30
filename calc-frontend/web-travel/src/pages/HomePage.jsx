import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Box, Typography, Button, Stack } from '@mui/material';

export default function HomePage() {
  const { user } = useAuth();

  return (
    <Box sx={{ textAlign: 'center', py: 8, px: 2 }}>
      <Typography variant="h2" component="h1" gutterBottom>
        Travel with Confidence
      </Typography>
      <Typography variant="h6" color="text.secondary" mb={4}>
        Get comprehensive travel insurance coverage for your next adventure
      </Typography>

      <Stack spacing={2} direction={{ xs: 'column', sm: 'row' }} justifyContent="center" mb={6}>
        {user ? (
          <Button component={Link} to="/insurance" variant="contained" size="large">
            Get Insurance Now
          </Button>
        ) : (
          <Button component={Link} to="/auth?type=register" variant="contained" size="large">
            Get Started
          </Button>
        )}
        <Button component={Link} to="/countries" variant="outlined" size="large">
          Explore Countries
        </Button>
      </Stack>

      <Box display="flex" justifyContent="center" gap={6} flexWrap="wrap">
        <Box maxWidth={280}>
          <Typography variant="h5" gutterBottom>Comprehensive Coverage</Typography>
          <Typography>Medical, trip cancellation, baggage loss, and more</Typography>
        </Box>
        <Box maxWidth={280}>
          <Typography variant="h5" gutterBottom>24/7 Support</Typography>
          <Typography>Assistance wherever you are in the world</Typography>
        </Box>
        <Box maxWidth={280}>
          <Typography variant="h5" gutterBottom>Easy Claims</Typography>
          <Typography>Simple online claims process</Typography>
        </Box>
      </Box>
    </Box>
  );
}
