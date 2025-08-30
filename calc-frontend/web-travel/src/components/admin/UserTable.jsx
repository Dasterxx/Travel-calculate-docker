import React, { useState, useMemo } from 'react';
import { useAuth } from '../../context/AuthContext';
import {
  Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
  IconButton, Switch, TextField, TablePagination, Stack, Typography, Tooltip
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';

export default function UserTable({ users, onEdit, onUserChange, onReload }) {
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [search, setSearch] = useState('');
  const [deleteId, setDeleteId] = useState(null);
  const [deleting, setDeleting] = useState(false);
  const [error, setError] = useState('');

  const { user } = useAuth();

  const filteredUsers = useMemo(() => {
    if (!search.trim()) return users;
    const lower = search.toLowerCase();
    return users.filter(u =>
      u.firstName.toLowerCase().includes(lower) ||
      u.lastName.toLowerCase().includes(lower) ||
      u.email.toLowerCase().includes(lower)
    );
  }, [users, search]);

  const handleDelete = async (personCode) => {
    if (!window.confirm('Delete this user?')) return;
    setDeleting(true);
    setError('');
    try {
      const resp = await fetch(`/api/admin/users/${personCode}`, {
        method: 'DELETE',
        headers: { Authorization: `Bearer ${localStorage.getItem('token')}`,
         'X-User-Email': user?.email || ''}
      });
      if (resp.ok) {
        onReload();
      } else {
        const data = await resp.json();
        setError(data.message || 'Delete failed');
      }
    } catch {
      setError('Network error');
    } finally {
      setDeleting(false);
    }
  };

  const handleBlacklistToggle = async (personCode, blacklisted) => {
    try {
      const resp = await fetch(`/api/admin/users/${personCode}/blacklist?blacklisted=${blacklisted}`, {
        method: 'PUT',
        headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
      });
      if (resp.ok) {
        onUserChange({ ...users.find(u => u.personCode === personCode), blackListed: blacklisted });
      } else {
        const data = await resp.json();
        setError(data.message || 'Failed to update blacklist');
      }
    } catch {
      setError('Network error');
    }
  };

  const handleChangePage = (e, newPage) => setPage(newPage);
  const handleChangeRowsPerPage = e => {
    setRowsPerPage(parseInt(e.target.value, 10));
    setPage(0);
  };

  return (
    <>
      <Stack direction="row" justifyContent="space-between" p={2}>
        <TextField
          label="Search by name or email"
          variant="outlined"
          size="small"
          value={search}
          onChange={e => setSearch(e.target.value)}
        />
        {error && <Typography color="error">{error}</Typography>}
      </Stack>
      <TableContainer>
        <Table size="small">
          <TableHead>
            <TableRow>
              <TableCell>Person Code</TableCell>
              <TableCell>Full Name</TableCell>
              <TableCell>Email</TableCell>
              <TableCell>Roles</TableCell>
              <TableCell>Blacklisted</TableCell>
              <TableCell align="right">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {filteredUsers.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map(user => (
              <TableRow key={user.personCode} hover>
                <TableCell>{user.personCode}</TableCell>
                <TableCell>{user.firstName} {user.lastName}</TableCell>
                <TableCell>{user.email}</TableCell>
                <TableCell>{Array.from(user.roles).join(', ')}</TableCell>
                <TableCell>
                  <Switch
                    checked={user.blackListed}
                    onChange={e => handleBlacklistToggle(user.personCode, e.target.checked)}
                    color="error"
                  />
                </TableCell>
                <TableCell align="right">
                  <Tooltip title="Edit">
                    <IconButton size="small" onClick={() => onEdit(user)}>✏️</IconButton>
                  </Tooltip>
                  <Tooltip title="Delete">
                    <IconButton size="small" color="error" disabled={deleting} onClick={() => handleDelete(user.personCode)}>
                      <DeleteIcon />
                    </IconButton>
                  </Tooltip>
                </TableCell>
              </TableRow>
            ))}
            {filteredUsers.length === 0 && (
              <TableRow>
                <TableCell colSpan={6} align="center">No users found</TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>
      <TablePagination
        rowsPerPageOptions={[5, 10, 25]}
        component="div"
        count={filteredUsers.length}
        rowsPerPage={rowsPerPage}
        page={page}
        onPageChange={handleChangePage}
        onRowsPerPageChange={handleChangeRowsPerPage}
      />
    </>
  );
}
