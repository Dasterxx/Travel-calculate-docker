import React, { useState, useEffect } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  TextField,
  DialogActions,
  Button,
  Alert,
  Stack,
  FormGroup,
  FormControlLabel,
  Checkbox,
  Typography,
} from '@mui/material';

export default function UserEditDialog({ open, onClose, user, onSave }) {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    personCode: '',
    blackListed: false,
    roles: new Set(),
  });

  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    if (user) {
      setFormData({
        firstName: user.firstName || '',
        lastName: user.lastName || '',
        email: user.email || '',
        personCode: user.personCode || '',
        blackListed: user.blackListed || false,
        roles: new Set(user.roles || []),
      });
    }
  }, [user]);

  const availableRoles = ['ROLE_USER', 'ROLE_ADMIN'];

  const handleChange = e => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const toggleRole = role => {
    setFormData(prev => {
      const roles = new Set(prev.roles);
      if (roles.has(role)) roles.delete(role);
      else roles.add(role);
      return { ...prev, roles };
    });
  };

  const handleSubmit = async e => {
    e.preventDefault();
    setError('');
    setSaving(true);

    try {
      // Подготовка тела запроса
      const body = {
        firstName: formData.firstName,
        lastName: formData.lastName,
        email: formData.email,
        blackListed: formData.blackListed,
        roles: Array.from(formData.roles),
      };

      // Вызов API обновления
      const resp = await fetch(`/api/admin/users/${user.personCode}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        },
        body: JSON.stringify(body),
      });

      if (resp.ok) {
        const updatedUser = await resp.json();
        onSave(updatedUser);
      } else {
        const data = await resp.json();
        setError(data.message || 'Failed to update user');
      }
    } catch {
      setError('Network error');
    } finally {
      setSaving(false);
    }
  };

  return (
    <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle>Edit User {formData.firstName} {formData.lastName}</DialogTitle>
      <DialogContent>
        {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
        <form id="edit-user-form" onSubmit={handleSubmit}>
          <TextField
            label="First Name"
            name="firstName"
            value={formData.firstName}
            onChange={handleChange}
            fullWidth
            margin="normal"
            required
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
            label="Person Code"
            name="personCode"
            value={formData.personCode}
            disabled
            fullWidth
            margin="normal"
          />
          <FormGroup sx={{ mt: 2 }}>
            <FormControlLabel
              control={
                <Checkbox
                  checked={formData.blackListed}
                  onChange={e => setFormData(prev => ({ ...prev, blackListed: e.target.checked }))}
                />
              }
              label="Blacklisted"
            />
          </FormGroup>

          <Typography sx={{ mt: 2, mb: 1 }}>Roles:</Typography>
          <Stack direction="row" spacing={2}>
            {availableRoles.map(role => (
              <FormControlLabel
                key={role}
                control={
                  <Checkbox
                    checked={formData.roles.has(role)}
                    onChange={() => toggleRole(role)}
                  />
                }
                label={role.replace('ROLE_', '')}
              />
            ))}
          </Stack>
        </form>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} disabled={saving}>Cancel</Button>
        <Button form="edit-user-form" type="submit" variant="contained" disabled={saving}>
          {saving ? 'Saving...' : 'Save'}
        </Button>
      </DialogActions>
    </Dialog>
  );
}
