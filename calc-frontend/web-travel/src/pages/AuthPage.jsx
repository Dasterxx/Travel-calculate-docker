import { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import LoginForm from '../components/auth/LoginForm';
import RegisterForm from '../components/auth/RegisterForm';
import { Box, Typography, Link as MuiLink } from '@mui/material';
import { Link } from 'react-router-dom';

export default function AuthPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const queryParams = new URLSearchParams(location.search);
  const type = queryParams.get('type') || 'login';

  useEffect(() => {
    if (!['login', 'register'].includes(type)) {
      navigate('/auth?type=login', { replace: true });
    }
  }, [type, navigate]);

  return (
    <Box maxWidth={400} mx="auto" mt={6} p={3} boxShadow={3} borderRadius={2} bgcolor="background.paper">
      {type === 'login' ? <LoginForm /> : <RegisterForm />}

      <Typography mt={3} textAlign="center" variant="body2" color="text.secondary">
        {type === 'login' ? (
          <>Don't have an account? <MuiLink component={Link} to="/auth?type=register">Register</MuiLink></>
        ) : (
          <>Already have an account? <MuiLink component={Link} to="/auth?type=login">Login</MuiLink></>
        )}
      </Typography>
    </Box>
  );
}
