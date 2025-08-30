import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import DeleteProfileDialog from '../components/auth/DeleteProfileDialog';
import ProfileSection from '../components/common/ProfileSection';
import AgreementSection from '../components/common/AgreementSection';
import AdminAgreementsPanel from './AdminAgreementsPanel';
import AdminUsersPanel from './AdminUsersPanel';

import {
  Box,
  Typography,
  Alert,
  Button,
  Tabs,
  Tab,
} from '@mui/material';

import { useNavigate } from 'react-router-dom';

export default function ProfilePage() {
  const { user, logout } = useAuth();
  const [userData, setUserData] = useState(null);
  const [error, setError] = useState('');
  const [loadingUser, setLoadingUser] = useState(false);
  const [deleted, setDeleted] = useState(false);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [tabIndex, setTabIndex] = useState(0);
  const navigate = useNavigate();

  const fetchUser = async () => {
    setLoadingUser(true);
    setError('');
    try {
      const resp = await fetch('/api/auth/me', {
        headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
      });
      const data = await resp.json();
      if (resp.ok) setUserData(data.data);
      else setError(data.message || 'Failed to load user data');
    } catch {
      setError('Network error loading user data');
    } finally {
      setLoadingUser(false);
    }
  };

  useEffect(() => {
    if (!user) return;
    fetchUser();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [user]);

  const handleProfileDeleteConfirmed = async (password) => {
    setError('');
    if (!password) {
      setError('Please enter your password');
      return;
    }
    try {
      const resp = await fetch('/api/auth/delete', {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        },
        body: JSON.stringify({ password }),
      });

      if (resp.ok) {
        setDeleted(true);
        logout();
        setTimeout(() => navigate('/auth?type=login'), 3000);
      } else {
        const data = await resp.json();
        setError(data.message || 'Failed to delete profile');
      }
    } catch {
      setError('Network error deleting profile');
    }
  };

  if (!user) {
    return (
      <Box mt={6} textAlign="center">
        <Typography variant="h6">Please login to view your profile</Typography>
      </Box>
    );
  }

  if (deleted) {
    return (
      <Box mt={6} textAlign="center">
        <Alert severity="success">
          Profile deleted successfully. Redirecting to login...
        </Alert>
      </Box>
    );
  }

  const isAdmin = user.roles && user.roles.some(role => role === 'ROLE_ADMIN');

  if (isAdmin) {
    return (
      <>
        <Tabs
          value={tabIndex}
          onChange={(_, newValue) => setTabIndex(newValue)}
          sx={{ mb: 3 }}
          aria-label="Admin Tabs"
        >
          <Tab label="Agreements" />
          <Tab label="Users" />
        </Tabs>

        {tabIndex === 0 && <AdminAgreementsPanel />}
        {tabIndex === 1 && <AdminUsersPanel />}
      </>
    );
  }


  return (
    <Box
      maxWidth={700}
      mx="auto"
      mt={4}
      p={3}
      bgcolor="background.paper"
      borderRadius={2}
      boxShadow={3}
    >
      <Tabs
        value={tabIndex}
        onChange={(_, newValue) => setTabIndex(newValue)}
        sx={{ mb: 3 }}
        aria-label="Profile and Agreements Tabs"
      >
        <Tab label="Profile" />
        <Tab label="Agreements" />
      </Tabs>

      {tabIndex === 0 && (
        <ProfileSection userData={userData} loadingUser={loadingUser} error={error} />
      )}
      {tabIndex === 1 && (
        <AgreementSection />
      )}

      <Box mt={4}>
        <Button
          color="error"
          variant="outlined"
          fullWidth
          onClick={() => setDialogOpen(true)}
        >
          Delete Profile
        </Button>
        <DeleteProfileDialog
          open={dialogOpen}
          onClose={() => setDialogOpen(false)}
          onConfirm={handleProfileDeleteConfirmed}
          error={error}
        />
      </Box>
    </Box>
  );
}
