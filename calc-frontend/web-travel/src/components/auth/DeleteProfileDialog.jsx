import React, { useState } from 'react';
import {
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Alert,
  CircularProgress,
} from '@mui/material';

export default function DeleteProfileDialog({ open, onClose, onConfirm }) {
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleConfirm = async () => {
    setError('');
    if (!password) {
      setError('Please enter your password');
      return;
    }
    setLoading(true);
    try {
      // Вызываем callback с паролем
      await onConfirm(password);
    } catch (e) {
      setError(e.message || 'Password confirmation failed');
    } finally {
      setLoading(false);
    }
  };

  const handleClose = () => {
    setPassword('');
    setError('');
    onClose();
  };

  return (
    <Dialog open={open} onClose={handleClose}>
      <DialogTitle>Confirm Profile Deletion</DialogTitle>
      <DialogContent>
        <TextField
          label="Enter your password"
          type="password"
          fullWidth
          value={password}
          onChange={e => setPassword(e.target.value)}
          autoFocus
          disabled={loading}
          margin="dense"
        />
        {error && <Alert severity="error" sx={{ mt: 2 }}>{error}</Alert>}
      </DialogContent>
      <DialogActions>
        <Button onClick={handleClose} disabled={loading}>Cancel</Button>
        <Button onClick={handleConfirm} color="error" variant="contained" disabled={loading}>
          {loading ? <CircularProgress size={24} color="inherit" /> : 'Delete'}
        </Button>
      </DialogActions>
    </Dialog>
  );
}
