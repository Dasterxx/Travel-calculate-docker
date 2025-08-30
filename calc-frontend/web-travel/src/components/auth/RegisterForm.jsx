import { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import {
  TextField,
  Button,
  Card,
  CardContent,
  Typography,
  Alert,
  Box,
  MenuItem,
  Select,
  InputLabel,
  FormControl
} from '@mui/material';

export default function RegisterForm() {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    birthDate: '',
    gender: '',
  });
  const [error, setError] = useState('');
  const { register } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    const result = await register(formData);
    if (result.success) {
      navigate('/auth?type=login');
    } else {
      setError(result.message);
    }
  };

  return (
    <Box display="flex" justifyContent="center" alignItems="center" minHeight="100vh" bgcolor="#f0f2f5" p={2}>
      <Card sx={{ minWidth: 400, boxShadow: 3 }}>
        <CardContent>
          <Typography variant="h5" gutterBottom align="center">Register</Typography>
          <form onSubmit={handleSubmit} noValidate>
            <TextField
              label="First Name"
              name="firstName"
              value={formData.firstName}
              onChange={handleChange}
              fullWidth
              margin="normal"
              required
              autoFocus
            />
            <TextField
              label="Last Name"
              name="lastName"
              value={formData.lastName}
              onChange={handleChange}
              fullWidth
              margin="normal"
              required
            />
            <TextField
              label="Email"
              name="email"
              type="email"
              value={formData.email}
              onChange={handleChange}
              fullWidth
              margin="normal"
              required
            />
            <TextField
              label="Password"
              name="password"
              type="password"
              value={formData.password}
              onChange={handleChange}
              fullWidth
              margin="normal"
              required
            />
            <TextField
              label="Birth Date"
              name="birthDate"
              type="date"
              value={formData.birthDate}
              onChange={handleChange}
              fullWidth
              margin="normal"
              required
              InputLabelProps={{ shrink: true }}
            />
            <FormControl fullWidth margin="normal" required>
              <InputLabel id="gender-label">Gender</InputLabel>
              <Select
                labelId="gender-label"
                name="gender"
                value={formData.gender}
                label="Gender"
                onChange={handleChange}
              >
                <MenuItem value=""><em>Select gender</em></MenuItem>
                <MenuItem value="Male">Male</MenuItem>
                <MenuItem value="Female">Female</MenuItem>
              </Select>
            </FormControl>

            {error && <Alert severity="error" sx={{ mt: 2 }}>{error}</Alert>}
            <Button type="submit" variant="contained" color="primary" fullWidth sx={{ mt: 3 }}>
              Register
            </Button>
          </form>
        </CardContent>
      </Card>
    </Box>
  );
}
