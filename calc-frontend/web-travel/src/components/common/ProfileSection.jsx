import React from 'react';
import ProfileForm from '../../components/auth/ProfileForm';
import { Box, Typography, CircularProgress, Alert } from '@mui/material';

export default function ProfileSection({ userData, loadingUser, error }) {
  if (loadingUser) {
    return (
      <Box textAlign="center" mt={6}>
        <CircularProgress />
        <Typography mt={2}>Loading profile data...</Typography>
      </Box>
    );
  }

  if (error) {
    return (
      <Box mt={6}>
        <Alert severity="error">{error}</Alert>
      </Box>
    );
  }

  return (
    <>
      <Typography variant="h4" mb={3}>
        Your Profile
      </Typography>
      {userData ? <ProfileForm userData={userData} /> : <Typography>No profile data available.</Typography>}
    </>
  );
}
