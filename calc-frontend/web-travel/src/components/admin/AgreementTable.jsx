import React, { useState, useMemo } from 'react';
import {
  Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
  IconButton, TextField, TablePagination, Stack, Typography, Paper, Tooltip
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';

export default function AgreementTable({ agreements, onEdit, onDelete, onReload }) {
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [search, setSearch] = useState('');
  const [error, setError] = useState('');

  // Фильтрация по uuid, first person name, country и т.д.
  const filteredAgreements = useMemo(() => {
    if (!search.trim()) return agreements;
    const lower = search.toLowerCase();
    return agreements.filter(a => {
      const uuidMatch = a.uuid.toLowerCase().includes(lower);
      const personMatch = a.persons.some(p =>
        `${p.firstName} ${p.lastName}`.toLowerCase().includes(lower)
      );
      return uuidMatch || personMatch;
    });
  }, [agreements, search]);

  const handleDelete = async (uuid) => {
    if (!window.confirm('Delete this agreement?')) return;
    setError('');
    try {
      const resp = await fetch(`/api/admin/agreements/${uuid}`, {
        method: 'DELETE',
        headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
      });
      if (resp.ok) {
        onReload();
      } else {
        const data = await resp.json();
        setError(data.message || 'Delete failed');
      }
    } catch {
      setError('Network error');
    }
  };

  const handleChangePage = (_, newPage) => setPage(newPage);
  const handleChangeRowsPerPage = e => {
    setRowsPerPage(parseInt(e.target.value, 10));
    setPage(0);
  };

  return (
    <>
      <Stack direction="row" justifyContent="space-between" p={2}>
        <TextField
          label="Search by UUID or person name"
          variant="outlined"
          size="small"
          value={search}
          onChange={e => setSearch(e.target.value)}
        />
        {error && <Typography color="error">{error}</Typography>}
      </Stack>
      <TableContainer component={Paper}>
        <Table size="small">
          <TableHead>
            <TableRow>
              <TableCell>UUID</TableCell>
              <TableCell>Date From</TableCell>
              <TableCell>Date To</TableCell>
              <TableCell>Persons</TableCell>
              <TableCell>Countries</TableCell>
              <TableCell>Risks</TableCell>
              <TableCell align="right">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {filteredAgreements.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map(agreement => (
              <TableRow key={agreement.uuid} hover>
                <TableCell>{agreement.uuid}</TableCell>
                <TableCell>{new Date(agreement.agreementDateFrom).toLocaleDateString()}</TableCell>
                <TableCell>{new Date(agreement.agreementDateTo).toLocaleDateString()}</TableCell>
                <TableCell>{agreement.persons.map(p => `${p.firstName} ${p.lastName}`).join(', ')}</TableCell>
                <TableCell>{agreement.countriesToVisit.join(', ')}</TableCell>
                <TableCell>{agreement.selectedRisks.join(', ')}</TableCell>
                <TableCell align="right">
                  <Tooltip title="Edit">
                    <IconButton size="small" onClick={() => onEdit(agreement)}><EditIcon /></IconButton>
                  </Tooltip>
                  <Tooltip title="Delete">
                    <IconButton size="small" color="error" onClick={() => handleDelete(agreement.uuid)}><DeleteIcon /></IconButton>
                  </Tooltip>
                </TableCell>
              </TableRow>
            ))}

            {filteredAgreements.length === 0 && (
              <TableRow>
                <TableCell colSpan={7} align="center">No agreements found</TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>
      <TablePagination
        component="div"
        count={filteredAgreements.length}
        rowsPerPage={rowsPerPage}
        page={page}
        onPageChange={handleChangePage}
        onRowsPerPageChange={handleChangeRowsPerPage}
        rowsPerPageOptions={[5, 10, 25]}
      />
    </>
  );
}
