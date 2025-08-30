import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { FaPlaneDeparture } from 'react-icons/fa';
import {
  AppBar,
  Toolbar,
  Typography,
  Box,
  Button,
  Menu,
  MenuItem,
} from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import { useState } from 'react';

export default function Header() {
  const { user, setUser, setTokens } = useAuth();
  const navigate = useNavigate();
  const [anchorEl, setAnchorEl] = useState(null);

  const handleMenuOpen = (event) => setAnchorEl(event.currentTarget);
  const handleMenuClose = () => setAnchorEl(null);

  // Добавлен полноценный logout с вызовом backend
  const handleLogout = async () => {
    try {
      const jwt = localStorage.getItem('token');

      if (!jwt) {
        console.warn('No JWT token found for logout');
        setTokens(null);
        setUser(null);
        navigate('/', { replace: true });
        window.location.reload();
        handleMenuClose();
        return;
      }

      const response = await fetch('/api/auth/logout', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${jwt}`,
        },
      });

      if (response.ok) {
        localStorage.removeItem('token');
        setTokens(null);
        setUser(null);
        window.location.href = 'http://localhost/';
      } else {
        console.error('Logout failed');
      }
    } catch (error) {
      console.error('Logout error:', error);
    } finally {
      handleMenuClose();
    }
  };



  const authOptions = (
    <>
      <MenuItem
        onClick={() => {
          navigate('/auth?type=login');
          handleMenuClose();
        }}
      >
        Login
      </MenuItem>
      <MenuItem
        onClick={() => {
          navigate('/auth?type=register');
          handleMenuClose();
        }}
      >
        Register
      </MenuItem>
    </>
  );

  return (
    <AppBar position="static" color="primary" elevation={3}>
      <Toolbar sx={{ justifyContent: 'space-between' }}>
        <Box
          display="flex"
          alignItems="center"
          component={Link}
          to="/"
          sx={{ textDecoration: 'none', color: 'inherit' }}
        >
          <FaPlaneDeparture size={24} style={{ marginRight: 8 }} />
          <Typography variant="h6" component="div">
            Travel Insurance
          </Typography>
        </Box>

        <Box
          sx={{ display: { xs: 'none', md: 'flex' }, alignItems: 'center' }}
          gap={2}
        >
          <Button component={Link} to="/" color="inherit">
            Home
          </Button>
          <Button component={Link} to="/countries" color="inherit">
            Countries
          </Button>
          {user ? (
            <>
              <Button component={Link} to="/profile" color="inherit">
                Profile
              </Button>
              <Button component={Link} to="/insurance" color="inherit">
                Insurance
              </Button>
              <Button
                onClick={handleLogout}
                color="inherit"
                variant="outlined"
                size="small"
                sx={{ ml: 2 }}
              >
                Logout
              </Button>
            </>
          ) : (
            <>
              <Button
                onClick={handleMenuOpen}
                color="inherit"
                startIcon={<MenuIcon />}
              >
                Account
              </Button>
              <Menu
                anchorEl={anchorEl}
                open={Boolean(anchorEl)}
                onClose={handleMenuClose}
                anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
                transformOrigin={{ vertical: 'top', horizontal: 'right' }}
              >
                {authOptions}
              </Menu>
            </>
          )}
        </Box>
      </Toolbar>
    </AppBar>
  );
}
