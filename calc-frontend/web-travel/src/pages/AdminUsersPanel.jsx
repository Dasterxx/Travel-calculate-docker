import React, { useState, useEffect } from 'react';
import {
  Box, Typography, CircularProgress, Alert, Paper,
} from '@mui/material';
import UserTable from '../components/admin/UserTable';
import UserEditDialog from '../components/admin/UserEditDialog';

export default function AdminUsersPanel() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [editUser, setEditUser] = useState(null);

  const fetchUsers = async () => {
    setLoading(true);
    setError('');
    try {
      const resp = await fetch('/api/admin/users', {
        headers: { Authorization: `Bearer ${localStorage.getItem('token')}` },
      });
      const data = await resp.json();
      if (resp.ok) {
        setUsers(data);
      } else {
        setError(data.message || 'Failed to load users');
      }
    } catch {
      setError('Network error loading users');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  const handleUserUpdated = (updatedUser) => {
    setUsers((prev) => prev.map((u) => (u.personCode === updatedUser.personCode ? updatedUser : u)));
    setEditUser(null);
  };

  return (
    <Box sx={{ maxWidth: 900, mx: 'auto', mt: 4, p: 3, bgcolor: 'background.paper', borderRadius: 2, boxShadow: 3 }}>
      <Typography variant="h4" mb={3}>Admin Users</Typography>

      {error && <Alert severity="error" mb={2}>{error}</Alert>}
      {loading ? (
        <CircularProgress />
      ) : (
        <Paper>
          <UserTable
            users={users}
            onEdit={setEditUser}
            onUserChange={(changedUser) => {
              setUsers((prev) => prev.map(u => u.personCode === changedUser.personCode ? changedUser : u));
            }}
            onReload={fetchUsers}
          />
        </Paper>
      )}

      {editUser && (
        <UserEditDialog
          open={Boolean(editUser)}
          user={editUser}
          onClose={() => setEditUser(null)}
          onSave={handleUserUpdated}
        />
      )}
    </Box>
  );
}
