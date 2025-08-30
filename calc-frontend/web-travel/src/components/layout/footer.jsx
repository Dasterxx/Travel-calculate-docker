import { Box, Typography, Link as MuiLink } from '@mui/material';
import { Link } from 'react-router-dom';

export default function Footer() {
  return (
    <Box
      component="footer"
      sx={{
        py: 2,
        px: 3,
        mt: 'auto',
        backgroundColor: 'primary.main',
        color: 'primary.contrastText',
        textAlign: 'center',
        display: 'flex',
        flexDirection: { xs: 'column', sm: 'row' },
        alignItems: 'center',
        justifyContent: 'space-between',
        gap: 2
      }}
    >
      <Typography variant="body2" sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
        <span role="img" aria-label="globe" style={{ marginRight: 6 }}>🌍</span>
        2024 Travel Insurance Inc.
      </Typography>

      <Box>
        <MuiLink component={Link} to="/countries" underline="hover" color="inherit" sx={{ mx: 1 }}>
          Countries Info
        </MuiLink>
        <MuiLink href="#" underline="hover" color="inherit" sx={{ mx: 1 }}>
          Privacy
        </MuiLink>
        <MuiLink href="#" underline="hover" color="inherit" sx={{ mx: 1 }}>
          Terms
        </MuiLink>
      </Box>
    </Box>
  );
}
