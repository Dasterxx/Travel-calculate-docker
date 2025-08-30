import { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { useNavigate, useLocation } from 'react-router-dom';
import { TextField, Button, Card, CardContent, Typography, Alert, Box } from '@mui/material';

export default function LoginForm() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const redirectPath = location.state?.from || '/insurance';

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    const result = await login(email, password);
    if (result.success) {
      navigate(redirectPath);
    } else {
      setError(result.message);
    }
  };

  return (
    <Box display="flex" justifyContent="center" alignItems="center" minHeight="100vh" bgcolor="#f5f5f5" p={2}>
      <Card sx={{ minWidth: 360, boxShadow: 3 }}>
        <CardContent>
          <Typography variant="h5" gutterBottom align="center">Login</Typography>
          <form onSubmit={handleSubmit} noValidate>
            <TextField
              label="Email"
              type="email"
              value={email}
              onChange={e => setEmail(e.target.value)}
              fullWidth
              margin="normal"
              required
              autoFocus
            />
            <TextField
              label="Password"
              type="password"
              value={password}
              onChange={e => setPassword(e.target.value)}
              fullWidth
              margin="normal"
              required
            />
            {error && <Alert severity="error" sx={{ mt: 2 }}>{error}</Alert>}
            <Button type="submit" variant="contained" color="primary" fullWidth sx={{ mt: 3 }}>
              Login
            </Button>
          </form>
        </CardContent>
      </Card>
    </Box>
  );
}
