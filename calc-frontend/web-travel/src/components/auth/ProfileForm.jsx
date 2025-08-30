import React, { useState, useEffect } from 'react';
import {
  TextField,
  Button,
  Card,
  CardContent,
  Typography,
  Alert,
  Box,
  Chip,
  Stack
} from '@mui/material';

export default function ProfileForm({ userData }) {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    birthDate: '',
    gender: '',
    personCode: '',
  });
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (userData) {
      setFormData({
        firstName: userData.firstName || '',
        lastName: userData.lastName || '',
        email: userData.email || '',
        birthDate: userData.birthDate || '',
        gender: userData.gender || '',
        personCode: userData.personCode || '',
      });
    }
  }, [userData]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess(false);
    console.log('Submitting formData:', formData);
    if (!formData.firstName.trim() || !formData.lastName.trim()) {
        setError('First Name and Last Name cannot be empty');
        return;
      }

    try {
      const response = await fetch('/api/auth/update', {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        },
        body: JSON.stringify(formData)
      });
      if (response.ok) {
        setSuccess(true);
        setTimeout(() => setSuccess(false), 3000);
      } else {
        const data = await response.json();
        if (data.errors && Array.isArray(data.errors)) {
          const messages = data.errors.map(e => e.description || e.errorCode).join(', ');
          setError(messages);
        } else {
          setError(data.message || 'Failed to update profile');
        }
      }
    } catch (error) {
      setError('An unexpected error occurred. Please try again.');
    }
  };

  return (
    <Box>
      <Card sx={{ minWidth: 400, boxShadow: 3 }}>
        <CardContent>
          <Typography variant="h5" gutterBottom align="center">Profile</Typography>
          <form onSubmit={handleSubmit} noValidate>
            <TextField
              label="First Name"
              name="firstName"
              value={formData.firstName}
              onChange={handleChange}
              fullWidth
              margin="normal"
              required
              inputProps={{ maxLength: 15, pattern: "^[A-Za-z \-]+$"
 }}
            />

            <TextField
              label="Last Name"
              name="lastName"
              value={formData.lastName}
              onChange={handleChange}
              fullWidth
              margin="normal"
              required
              inputProps={{ maxLength: 15, pattern: "^[A-Za-z \-]+$"
 }}
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
              label="Birth Date"
              name="birthDate"
              type="date"
              value={formData.birthDate}
              onChange={handleChange}
              fullWidth
              margin="normal"
              InputLabelProps={{ shrink: true }}
              required
            />
            <TextField
              label="Gender"
              name="gender"
              value={formData.gender}
              onChange={handleChange}
              fullWidth
              margin="normal"
              placeholder="Male / Female / Other"
            />
            <TextField
              label="Person Code"
              name="personCode"
              value={formData.personCode}
              onChange={handleChange}
              fullWidth
              margin="normal"
              InputProps={{ readOnly: true }}
              placeholder="X-12345"
            />

            {/* Только для чтения: роли, статус, премия, blacklist */}
            <Stack direction="row" spacing={1} mt={2} mb={2}>
              {userData.roles && Array.from(userData.roles).map(role => (
                <Chip key={role} label={role} color="primary" />
              ))}
              {userData.isEnabled && <Chip label="Enabled" color="success" />}
              {userData.blackListed && <Chip label="Blacklisted" color="error" />}
            </Stack>
            {userData.personPremium && (
              <Typography variant="subtitle2" color="text.secondary">
                Premium: {userData.personPremium}
              </Typography>
            )}

            {success && <Alert severity="success" sx={{ mt: 2 }}>Profile updated successfully!</Alert>}
            {error && <Alert severity="error" sx={{ mt: 2 }}>{error}</Alert>}

            <Button type="submit" variant="contained" color="primary" fullWidth sx={{ mt: 3 }}>
              Update Profile
            </Button>
          </form>
        </CardContent>
      </Card>
    </Box>
  );
}
