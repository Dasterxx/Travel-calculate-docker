import Header from './Header';
import Footer from './Footer';
import { Box } from '@mui/material';

export default function Layout({ children }) {
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        minHeight: '100vh',
        bgcolor: 'background.default',
        color: 'text.primary'
      }}
    >
      <Header />
      <Box component="main" sx={{ flexGrow: 1, p: { xs: 2, sm: 3 } }}>
        {children}
      </Box>
      <Footer />
    </Box>
  );
}
